package com.jikexueyuan.crawl.zongheng;

import java.util.HashMap;

import com.jikexueyuan.crawl.CrawlBase;
import com.jikexueyuan.crawl.zongheng.db.ZonghengDB;
import com.jikexueyuan.crawl.zongheng.model.NovelReadModel;
import com.jikexueyuan.util.JsonUtil;
import com.jikexueyuan.util.ParseMD5;
import com.jikexueyuan.util.RegexUtil;

public class ReadPage extends CrawlBase{
	
	private String url;
	/*请求头信息*/
	private static HashMap<String, String> params;
	
	/*章节标题正则*/
	private static final String TITLE = "chapterName=\"(.*?)\"";
	/*章节字数正则*/
	private static final String WORDCOUNT = "itemprop=\"wordCount\">(\\d*?)</span>";
	/*章节正文正则*/
	private static final String CONTENT = "<div id=\"chapterContent\" class=\"content\" itemprop=\"acticleBody\">(.*?)</div>";
	

	static {
		params = new HashMap<String, String> ();
		params.put("Referer", "http://book.zongheng.com");
		params.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.63 Safari/537.36");
		params.put("Host", "book.zongheng.com");
	}
	
	public ReadPage(String url) {
		readPageByGet(url, params, "utf-8");
		this.url = url;
	}
	
	public NovelReadModel analyzer() {
		NovelReadModel bean = new NovelReadModel();
		bean.setUrl(this.url);
		bean.setId(ParseMD5.parseStrToMD5(bean.getUrl()));
		bean.setTitle(getTitle());
		bean.setWordCount(getWordCount());
		bean.setContent(getContent());
		return bean;
	}
	
	/*获得章节题目*/
	private String getTitle() {
		return RegexUtil.getFirstString(getPageSourceCode(), TITLE, 1);
	}
	/*获得章节字数*/
	private int getWordCount() {
		String wordCountStr = RegexUtil.getFirstString(getPageSourceCode(), WORDCOUNT, 1);
		return Integer.parseInt(wordCountStr);
	}
	/*获取章节正文*/
	private String getContent() {
		return RegexUtil.getFirstString(getPageSourceCode(), CONTENT, 1);
	}
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ReadPage readPage = new ReadPage("http://book.zongheng.com/chapter/516267/8418694.html ");
		System.out.println(readPage.getTitle());
		System.out.println(readPage.getWordCount());
		System.out.println(readPage.getContent());
		NovelReadModel bean = readPage.analyzer();
		System.out.println(JsonUtil.parseJson(bean));
		bean.setChapterId(2);
		bean.setChapterTime(1448341985000l);
		ZonghengDB db = new ZonghengDB();
		db.saveNovelRead(bean);
	}

}
