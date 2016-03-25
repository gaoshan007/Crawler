package com.jikexueyuan.lucene.index.model;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.Version;

//该类的主要功能是保存索引的相关配置信息
public class ConfigBean {
	private String indexName = "index";
	private String indexPath = "/index/";
	private Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_43);
	private double indexReopenMaxStaleSec = 10;
	private double indexReopenMinStaleSec = 0.025;
	private int indexCommitSeconds = 60;
	
	public String getIndexName() {
		return indexName;
	}
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	public String getIndexPath() {
		return indexPath;
	}
	public void setIndexPath(String indexPath) {
		this.indexPath = indexPath;
	}
	public Analyzer getAnalyzer() {
		return analyzer;
	}
	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}
	public double getIndexReopenMaxStaleSec() {
		return indexReopenMaxStaleSec;
	}
	public void setIndexReopenMaxStaleSec(double indexReopenMaxStaleSec) {
		this.indexReopenMaxStaleSec = indexReopenMaxStaleSec;
	}
	public double getIndexReopenMinStaleSec() {
		return indexReopenMinStaleSec;
	}
	public void setIndexReopenMinStaleSec(double indexReopenMinStaleSec) {
		this.indexReopenMinStaleSec = indexReopenMinStaleSec;
	}
	public int getIndexCommitSeconds() {
		return indexCommitSeconds;
	}
	public void setIndexCommitSeconds(int indexCommitSeconds) {
		this.indexCommitSeconds = indexCommitSeconds;
	}

}
