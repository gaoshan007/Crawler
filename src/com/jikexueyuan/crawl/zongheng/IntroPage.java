/**
 * 
 */
package com.jikexueyuan.crawl.zongheng;

import java.util.HashMap;

import com.jikexueyuan.crawl.CrawlBase;
import com.jikexueyuan.crawl.zongheng.db.ZonghengDB;
import com.jikexueyuan.crawl.zongheng.model.NovelInfoModel;
import com.jikexueyuan.util.JsonUtil;
import com.jikexueyuan.util.ParseMD5;
import com.jikexueyuan.util.RegexUtil;


public class IntroPage extends CrawlBase{
	private String url;
	private static HashMap<String, String> params;
	/*小说书名的正则表达式*/
	private static final String NAME = "<meta name=\"og:novel:book_name\" content=\"(.*?)\"/> ";
	/*小说作者的正则表达式*/
	private static final String AUTHOR = "<meta name=\"og:novel:author\" content=\"(.*?)\"/>";
	/*小说简简介正则*/
	private static final String DESC = "<meta property=\"og:description\" content=\"(.*?)\"/>";
	/*小说关键字正则*/
	private static final String TYPE = "<meta name=\"og:novel:category\" content=\"(.*?)\"/> ";
	/*最新章节正则*/
	private static final String LASTCHAPTER = "<a class=\"chap\" href=\".*?\">(.*?)<p>";
	/*小说字数*/
	private static final String WORDCOUNT = "<span title=\"(\\d*?)字\">";
	/*小说关键字第一层*/
	private static final String KEYWORDS = "<div class=\"keyword\">(.*?)</div>";
	/*关键字正则第二层*/
	private static final String KEYWORD = "<a.*?>(.*?)</a>";
	/*小说章节页URL正则*/
	private static final String CHAPTERLISTURL = "<meta name=\"og:novel:read_url\" content=\"(.*?)\"/>";
			
	
	
	static {
		params = new HashMap<String, String> ();
		params.put("Referer", "http://book.zongheng.com");
		params.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.63 Safari/537.36");
		params.put("Host", "book.zongheng.com");
	}

	public IntroPage(String url) {
		readPageByGet(url, params, "utf-8");
		this.url = url;
	}
	
	/*分析方法*/
	public NovelInfoModel analyzer() {
		NovelInfoModel bean = new NovelInfoModel();
		bean.setUrl(url);
		bean.setId(ParseMD5.parseStrToMD5(bean.getUrl()));
		bean.setName(getName());
		bean.setAuthor(getAuthor());
		bean.setDesc(getDesc());
		bean.setType(getType());
		bean.setLastChapter(getLastCharpter());
		bean.setWordCount(getWordCount());
		bean.setKeyWords(getKeyWord());
		bean.setChapterListUrl(getChapterListUrl());
		return bean;
	}
	
	
	/*获取小说的书名*/
	private String getName() {
		return RegexUtil.getFirstString(getPageSourceCode(), NAME, 1);
	}
	/*获取作者姓名*/
	private String getAuthor() {
		return RegexUtil.getFirstString(getPageSourceCode(), AUTHOR, 1);
	}
	/*提取简介页信息*/
	private String getDesc() {
		return RegexUtil.getFirstString(getPageSourceCode(), DESC, 1);
	}
	/*提取小说关键字*/
	private String getType() {
		return RegexUtil.getFirstString(getPageSourceCode(), TYPE, 1);
	}
	/*获取最新章节*/ 
	private String getLastCharpter() {
		return RegexUtil.getFirstString(getPageSourceCode(), LASTCHAPTER, 1);
	}
	/*获取小说字数信息*/
	private int getWordCount() {
		String wordCount = RegexUtil.getFirstString(getPageSourceCode(), WORDCOUNT, 1);
		return Integer.parseInt(wordCount);
	}
	/*获取关键字语句*/
	private String getKeyWordStr() {
		return RegexUtil.getFirstString(getPageSourceCode(), KEYWORDS, 1);
	}
	/*获取关键字*/
	private String getKeyWord() {
		return RegexUtil.getString(getKeyWordStr(), KEYWORD, " ", 1);
	}
	/*获取章节URL*/
	private String getChapterListUrl() {
		return RegexUtil.getFirstString(getPageSourceCode(), CHAPTERLISTURL, 1);
	}
	
	public static void main(String[] args) {
		IntroPage intropage = new IntroPage("http://book.zongheng.com/book/548323.html");
		System.out.println(intropage.getName());
		System.out.println(intropage.getAuthor());
		System.out.println(intropage.getDesc());
		System.out.println(intropage.getType());
		System.out.println(intropage.getLastCharpter());
		System.out.println(intropage.getWordCount());
		System.out.println(intropage.getKeyWord());
		System.out.println(intropage.getChapterListUrl());
		
		System.out.println(JsonUtil.parseJson(intropage.analyzer()));
		ZonghengDB db = new ZonghengDB();
		db.updateNovelInfo(intropage.analyzer());
	}
}
