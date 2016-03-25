package com.jikexueyuan.crawl.zongheng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.jikexueyuan.crawl.CrawlListPageBase;
import com.jikexueyuan.crawl.zongheng.db.ZonghengDB;

public class UpdateList extends CrawlListPageBase{
	
	private static HashMap<String, String> params;
	
	static {
		params = new HashMap<String, String> ();
		params.put("Referer", "http://book.zongheng.com");
		params.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.63 Safari/537.36");
		params.put("Host", "book.zongheng.com");
	}
	
	public UpdateList(String pageUrl) {
		super(pageUrl, "utf-8", params);
	}

	@Override
	public String getUrlRegexStr() {
		return "<a class=\"fs14\" href=\"(.*?)\"";
	}

	@Override
	public int getUrlRegexStrNum() {
		return 1;
	}
	
	public List<String> getPageUrl(boolean exceptOther) {
		List<String> urls = getPageUrl();
		if(exceptOther) {
			List<String> excepUrls = new ArrayList<String>();
			for(String url : urls) {
				if(url.indexOf("zongheng") > 0) {
					excepUrls.add(url);
				}
			}
			return excepUrls;
		}
		return urls;
	}
	
	public static void main(String[] args) {
		UpdateList updateList = new UpdateList("http://book.zongheng.com/store.html");
		for(String s:updateList.getPageUrl(true)) {
			System.out.println(s);
		}
		
		ZonghengDB db = new ZonghengDB();
		db.saveInfoUrls(updateList.getPageUrl(true));
	}
	

}
