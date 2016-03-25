package com.jikexueyuan.crawl.zongheng.db;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.jikexueyuan.crawl.zongheng.model.CrawlListInfo;
import com.jikexueyuan.crawl.zongheng.model.NovelChapterModel;
import com.jikexueyuan.crawl.zongheng.model.NovelInfoModel;
import com.jikexueyuan.crawl.zongheng.model.NovelReadModel;
import com.jikexueyuan.db.manager.DBServer;
import com.jikexueyuan.util.JsonUtil;
import com.jikexueyuan.util.ParseMD5;

public class ZonghengDB {
	
	private static final String POOLNAME = "proxool.jikexueyuan";

	//获取更新列表页地址信息
	public List<CrawlListInfo> getCrawlListInfos() {
		List<CrawlListInfo> infos = new ArrayList<CrawlListInfo>();
		DBServer dbServer = new DBServer(POOLNAME);
		try {
			String sql = "select * from crawllist where `state` = '1'";
			ResultSet rs = dbServer.select(sql);
			while(rs.next()) {
				CrawlListInfo info = new CrawlListInfo();
				infos.add(info);
				info.setUrl(rs.getString("url"));
				info.setInfo(rs.getString("info"));
				info.setFrequency(rs.getInt("frequency"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbServer.close();
		}
		return infos;
	}
	
	//随机获得小说简介页URL信息
	public String getRandIntroPageUrl(int state) {
		DBServer dbServer = new DBServer(POOLNAME);
		try {
			String sql = "select * from novelinfo where state = '" + state + "' order by rand() limit 1";
			ResultSet rs = dbServer.select(sql);
			while(rs.next()) {
				return rs.getString("url");
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			dbServer.close();
		}
		return null;
	}
	
	//随机获取小说章节页信息
	public NovelChapterModel getRandChapter(int state) {
		DBServer dbServer = new DBServer(POOLNAME);
		try {
			String sql = "select * from novelchapter where state = '" + state + "' order by rand() limit 1";
			ResultSet rs = dbServer.select(sql);
			while(rs.next()) {
				NovelChapterModel bean = new NovelChapterModel();
				bean.setId(rs.getString("id"));
				bean.setUrl(rs.getString("url"));
				bean.setChapterId(rs.getInt("chapterid"));
				bean.setChapterTime(rs.getLong("chaptertime"));
				return bean;
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			dbServer.close();
		}
		return null;
	}
	
	//保存小说章节信息
	public void saveNovelChapter(List<NovelChapterModel> beans, String novelId) {
		if(beans == null) {
			return ;
		}
		for(NovelChapterModel bean : beans) {
			if(!haveNovelChapter(bean.getId())) {
				inserNovelChapter(bean, novelId);
			}
		}
	}
	
	
	//将采集到的简介页url信息保存到数据库中
	public void saveInfoUrls(List<String> urls) {
		if(urls == null || urls.size() < 1) {
			return;
		}
		for(String url : urls) {
			String id = ParseMD5.parseStrToMD5(url);
			if(hasNovelInfo(id)) {
				updateInfoState(id, 1);
			} else {
				insertInfoUrl(id, url);
			}
		}
	}
	
	//判断数据库中是否已经存在该章节页信息
	private boolean haveNovelChapter(String id) {
		DBServer dbServer = new DBServer(POOLNAME);
		try {
			String sql = "select sum(1) as count from novelchapter where id = '" + id + "'";
			ResultSet rs = dbServer.select(sql);
			if(rs.next()) {
				int count = rs.getInt("count");
				return count > 0;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbServer.close();
		}
		return true;
	}
	
	
	//添加小说章节信息
	private void inserNovelChapter(NovelChapterModel bean, String noveId) {
		if(bean == null) {
			return ;
		}
		DBServer dbServer = new DBServer(POOLNAME);
		
		try {
			HashMap<Integer, Object> params = new HashMap<Integer, Object>();
			int i = 1;
			params.put(i++, bean.getId());
			params.put(i++, noveId);
			params.put(i++, bean.getUrl());
			params.put(i++, bean.getTitle());
			params.put(i++, bean.getWordCount());
			params.put(i++, bean.getChapterId());
			params.put(i++, bean.getChapterTime());
			long now = System.currentTimeMillis();
			params.put(i++, now);
			params.put(i++, "1");
			String columns = "id, novelid, url, title, wordcount, chapterid, chaptertime, createtime,state";
			dbServer.insert("novelchapter", columns, params);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbServer.close();
		}

	}
	
	//实现小说简介信息更新
	public void updateNovelInfo(NovelInfoModel bean) {
		if(bean == null) return;
		DBServer dbServer = new DBServer(POOLNAME);
		
		try {
			HashMap<Integer, Object> params = new HashMap<Integer, Object>();
			int i = 1;
			params.put(i++, bean.getName());
			params.put(i++, bean.getAuthor());
			params.put(i++, bean.getDesc());
			params.put(i++, bean.getType());
			params.put(i++, bean.getLastChapter());
			params.put(i++, bean.getChapterCount());
			params.put(i++, bean.getWordCount());
			params.put(i++, bean.getKeyWords());
			long now = System.currentTimeMillis();
			params.put(i++, now);
			params.put(i++, "0");
			
			String columns = "name, author, description, type, lastchapter, chapterlisturl, wordcount, keywords, updatetime, state";
			String condition = "where id = '" + bean.getId() + "'";
			dbServer.update("novelinfo", columns, condition, params);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbServer.close();
		}
	}
	
	//更新小说章节信息
	public void updateChapterState(String id, int state) {
		DBServer dbServer = new DBServer(POOLNAME);
		try {
			String sql = "update novelchapter set `state` = '" + state + "' where id = '" + id + "'";
			dbServer.update(sql);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbServer.close();
		}
	}
	
	
	//更新章节信息的state值
	public void updateInfoState(String id, int state) {
		DBServer dbServer = new DBServer(POOLNAME);
		try {
			String sql = "update novelinfo set `state` = '" + state + "' where id = '" + id + "'";
			dbServer.update(sql);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbServer.close();
		}
	}
	
	//判断小说简介信息是否存在
	private boolean hasNovelInfo(String id) {
		DBServer dbServer = new DBServer(POOLNAME);
		try {
			String sql = "select sum(1) as count from novelinfo where id = '" + id + "'";
			ResultSet rs = dbServer.select(sql);
			if(rs.next()) {
				int count = rs.getInt("count");
				return count > 0;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbServer.close();
		}
		return true;
	}
	
	//将采集到的简介页url保存到数据库中
	private void insertInfoUrl(String id, String url) {
		DBServer dbServer = new DBServer(POOLNAME);
		try {
			HashMap<Integer, Object> params = new HashMap<Integer, Object>();
			int i = 1;
			params.put(i++, id);
			params.put(i++, url);
			long now = System.currentTimeMillis();
			params.put(i++, now);
			params.put(i++, now);
			params.put(i++, 1);
			
			dbServer.insert("novelinfo", "id,url,createtime,updatetime, state", params);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbServer.close();
		}
	}
	
	//保存小说章节页信息
	public void saveNovelRead(NovelReadModel bean) {
		if(bean == null) {
			return ;
		}
		
		DBServer dbServer = new DBServer(POOLNAME);
		try {
			if(haveNovelRead(bean.getId())) {
				return;
			}
			HashMap<Integer, Object> params = new HashMap<Integer, Object>();
			int i = 1;
			params.put(i++, bean.getId());
			params.put(i++, bean.getUrl());
			params.put(i++, bean.getTitle());
			params.put(i++, bean.getWordCount());
			params.put(i++, bean.getChapterId());
			params.put(i++, bean.getContent());
			params.put(i++, bean.getChapterTime());
			long now = System.currentTimeMillis();
			params.put(i++, now);
			params.put(i++, now);
			
			String columns = "id,url,title,wordcount,chapterid,content,chaptertime, createtime, updatetime";
			dbServer.insert("novelchapterdetail", columns, params);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbServer.close();
		}
	}
	
	//判断小说章节也信息是否已经存在数据库中
	private boolean haveNovelRead(String id) {
		DBServer dbServer = new DBServer(POOLNAME);
		try {
			String sql = "select sum(1) as count from novelchapterdetail where id = '" + id + "'";
			ResultSet rs = dbServer.select(sql);
			if(rs.next()) {
				int count = rs.getInt("count");
				return count > 0;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbServer.close();
		}
		return true;
	}
	
	
	public void method() {
		DBServer dbServer = new DBServer(POOLNAME);
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbServer.close();
		}
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ZonghengDB db = new  ZonghengDB();
		System.out.println(JsonUtil.parseJson(db.getCrawlListInfos()));
		System.out.println(db.getRandIntroPageUrl(1));
		System.out.println(JsonUtil.parseJson(db.getRandChapter(1)));
		
	}

}
