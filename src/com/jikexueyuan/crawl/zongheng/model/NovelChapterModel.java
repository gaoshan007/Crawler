package com.jikexueyuan.crawl.zongheng.model;

public class NovelChapterModel {
	
	private String id;
	private String url;
	private String title;
	private int wordCount;
	private int chapterId;
	private long chapterTime;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getWordCount() {
		return wordCount;
	}
	public void setWordCount(int wordCount) {
		this.wordCount = wordCount;
	}
	public int getChapterId() {
		return chapterId;
	}
	public void setChapterId(int chapterId) {
		this.chapterId = chapterId;
	}
	public long getChapterTime() {
		return chapterTime;
	}
	public void setChapterTime(long chapterTime) {
		this.chapterTime = chapterTime;
	}
	
	
}
