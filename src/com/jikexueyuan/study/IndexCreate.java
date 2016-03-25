package com.jikexueyuan.study;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class IndexCreate {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_43);
		IndexWriterConfig indexWriteConfig = new IndexWriterConfig(Version.LUCENE_43, analyzer);
		indexWriteConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
		Directory directory = null;
		IndexWriter indexWrite = null;
		try {
			directory = FSDirectory.open(new File("//Users//gaoshan//Documents//test"));
			if(indexWrite.isLocked(directory)) {
				indexWrite.unlock(directory);
			}
			indexWrite = new IndexWriter(directory, indexWriteConfig);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Document doc1 = new Document();
		doc1.add(new StringField("id", "abcde", Store.YES));
		doc1.add(new TextField("content", "极客学院", Store.YES));
		doc1.add(new IntField("num", 1, Store.YES));
	
		try {
			indexWrite.addDocument(doc1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		Document doc2 = new Document();
		doc2.add(new StringField("id", "abcff", Store.YES));
		doc2.add(new TextField("content", "Lucene案例开发", Store.YES));
		doc2.add(new IntField("num", 2, Store.YES));
	
		try {
			indexWrite.addDocument(doc2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			indexWrite.commit();
			indexWrite.close();
			directory.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
