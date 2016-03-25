package com.jikexueyuan.crawl;

import java.util.HashMap;
import java.util.List;

import com.jikexueyuan.util.RegexUtil;

public abstract class CrawlListPageBase extends CrawlBase{

	private String pageUrl;
	
	public CrawlListPageBase(String pageUrl, String charsetName) {
		readPageByGet(pageUrl, null, charsetName);
		this.pageUrl = pageUrl;
	}
	
	public CrawlListPageBase(String pageUrl, String charsetName, HashMap<String, String> params) {
		readPageByGet(pageUrl, params, charsetName);
		this.pageUrl = pageUrl;
	}
	
	public List<String> getPageUrl() {
		return RegexUtil.getArrayList(getPageSourceCode(), getUrlRegexStr(), this.pageUrl, getUrlRegexStrNum());
	}
	
	public abstract String getUrlRegexStr();
	
	public abstract int getUrlRegexStrNum();
		
}
