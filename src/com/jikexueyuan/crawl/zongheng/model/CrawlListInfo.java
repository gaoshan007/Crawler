package com.jikexueyuan.crawl.zongheng.model;

public class CrawlListInfo {
	//URL地址
	private String url;
	//入口信息
	private String info;
	//刷新频率
	private int frequency;
	
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public int getFrequency() {
		return frequency;
	}
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	
}
