/**
 * 
 */
package com.jikexueyuan.crawl.zongheng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.jikexueyuan.crawl.CrawlBase;
import com.jikexueyuan.crawl.zongheng.db.ZonghengDB;
import com.jikexueyuan.crawl.zongheng.model.NovelChapterModel;
import com.jikexueyuan.util.JsonUtil;
import com.jikexueyuan.util.ParseMD5;
import com.jikexueyuan.util.RegexUtil;

public class ChapterPage extends CrawlBase {
	
	private String url;
	private static HashMap<String, String> params;
	/*章节信息正则*/
	private static final String CHAPTER = "<td class=\"chapterBean\" chapterId=\"\\d*\" chapterName=\"(.*?)\" chapterLevel=\"\\d*\" wordNum=\"(.*?)\" updateTime=\"(\\d*?)\"><a href=\"(.*?)\" title=\".*?\">";
	private static final int[] array = {1, 2, 3, 4};
	
	
	static {
		params = new HashMap<String, String> ();
		params.put("Referer", "http://book.zongheng.com");
		params.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.63 Safari/537.36");
		params.put("Host", "book.zongheng.com");
	}
	
	public ChapterPage(String url) {
		readPageByGet(url, params, "utf-8");
		this.url = url;
	}
	
	/*章节信息分析方法*/
	private NovelChapterModel analyzer(String[] array, int i) {
		NovelChapterModel bean = new NovelChapterModel();
		//bean.setUrl(this.url);
		bean.setUrl(array[3]);
		bean.setId(ParseMD5.parseStrToMD5(bean.getUrl()));
		bean.setTitle(array[0]);
		bean.setWordCount(Integer.parseInt(array[1]));
		bean.setChapterTime(Long.parseLong(array[2]));
		
		bean.setChapterId(i);
		return bean;
	}
	
	public List<NovelChapterModel> analyzer() {
		List<NovelChapterModel> list = new ArrayList<NovelChapterModel>();
		List<String[]> arrays = getChapters();
		int i = 0;
		for(String[] array : arrays) {
			i++;
			list.add(analyzer(array, i));
		}
		return list;
	}
	
	
	/*采集章节信息: 章节名，字数，更新时间，阅读页地址*/
	public List<String[]> getChapters() {
		return RegexUtil.getList(getPageSourceCode(), CHAPTER, array);
	}
	
	
	
	public static void main(String[] args) {
		ChapterPage chapterPage = new ChapterPage("http://book.zongheng.com/showchapter/516267.html");
		for(String[] ss : chapterPage.getChapters()) {
			for(String s : ss) {
				System.out.println(s);
			}
			System.out.println("-------------------------------");
		}
		System.out.println(JsonUtil.parseJson(chapterPage.analyzer()));
		//00f89bfc6dda2e0a4473c7b696308cb0
		ZonghengDB db = new ZonghengDB();
		db.saveNovelChapter(chapterPage.analyzer(), "00f89bfc6dda2e0a4473c7b696308cb0");
	}
}
