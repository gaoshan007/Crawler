package com.jikexueyuan.lucene.index.manager;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NRTManager;
import org.apache.lucene.search.NRTManager.TrackingIndexWriter;
import org.apache.lucene.search.NRTManagerReopenThread;
import org.apache.lucene.search.SearcherFactory;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.util.Version;

import com.jikexueyuan.lucene.index.model.ConfigBean;
import com.jikexueyuan.lucene.index.model.IndexConfig;

public class IndexManager {
	private IndexWriter indexWriter;
	private TrackingIndexWriter trackingIndexWriter;
	private NRTManager nrtManager;
	private NRTManagerReopenThread nrtManagerReopenThread;
	private ConfigBean configBean;
	private IndexCommitThread indexCommitThread;
	
	private static class LazyIndexManager {
		private static HashMap<String, IndexManager> indexManagerMap = new HashMap<String , IndexManager>();
		
		static {
			for(ConfigBean bean : IndexConfig.getConfig()) {
				indexManagerMap.put(bean.getIndexName(), new IndexManager(bean));
			}
		}
	}
	
	public static IndexManager getIndexManager(String indexName) {
		return LazyIndexManager.indexManagerMap.get(indexName);
	}
	
	private IndexManager(ConfigBean configBean) {
		String indexFile = configBean.getIndexPath() + "/"+ configBean.getIndexName() ;
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_43, configBean.getAnalyzer());
		indexWriterConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
		this.configBean = configBean;
		Directory directory = null;
		try {
			directory = NIOFSDirectory.open(new File(indexFile));
			if(IndexWriter.isLocked(directory)) {
					IndexWriter.unlock(directory);
			}
			this.indexWriter = new IndexWriter(directory, indexWriterConfig);
			this.trackingIndexWriter = new TrackingIndexWriter(indexWriter);
			this.nrtManager = new NRTManager(trackingIndexWriter, new SearcherFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
		setThread();
	}
	
	public IndexSearcher getIndexSearcher() {
		try {
			return this.nrtManager.acquire();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void relase(IndexSearcher indexSearcher) {
		try {
			this.nrtManager.release(indexSearcher);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	private void setThread() {
		this.nrtManagerReopenThread = new NRTManagerReopenThread(nrtManager, configBean.getIndexReopenMaxStaleSec(), configBean.getIndexReopenMinStaleSec());
		this.nrtManagerReopenThread.setName("NRTManager reopen thread");
		this.nrtManagerReopenThread.setDaemon(true);
		this.nrtManagerReopenThread.start();
		
		this.indexCommitThread = new IndexCommitThread(configBean.getIndexName() + "index commit thread");
		this.indexCommitThread.setDaemon(true);
		this.indexCommitThread.start();
	}
	
	
	private class IndexCommitThread extends Thread{
		private boolean flag = false;
		public IndexCommitThread(String name) {
			super(name);
		}
		
		public void run() {
			flag = true;
			while(flag) {
				try {
					indexWriter.commit();
					System.out.println(new Date().toLocaleString() + '\t' + configBean.getIndexName() + "\tcommit");
					TimeUnit.SECONDS.sleep(configBean.getIndexCommitSeconds());
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			super.run();
		}
		
	}
}







