package com.jikexueyuan.study;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

public class AnalyzerStudy {
	private static String str = "极客学院, Lucene 案例开发";
	
	public static void print(Analyzer analyzer) {
		StringReader stringReader = new StringReader(str);
		try {
			TokenStream tokenStream = analyzer.tokenStream("", stringReader);
			tokenStream.reset();
			CharTermAttribute term = tokenStream.getAttribute(CharTermAttribute.class);
			System.out.println("分词技术" + analyzer.getClass());
			while(tokenStream.incrementToken()) {
				System.out.println(term.toString() + "|");
			}
			System.out.println();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Analyzer analyzer = null;
		analyzer = new StandardAnalyzer(Version.LUCENE_43);
		AnalyzerStudy.print(analyzer);
		
		
		analyzer = new WhitespaceAnalyzer(Version.LUCENE_43);
		AnalyzerStudy.print(analyzer);
		
		analyzer = new SimpleAnalyzer(Version.LUCENE_43);
		AnalyzerStudy.print(analyzer);
		
		analyzer = new CJKAnalyzer(Version.LUCENE_43);
		AnalyzerStudy.print(analyzer);
		
		analyzer = new StopAnalyzer(Version.LUCENE_43);
		AnalyzerStudy.print(analyzer);
	}

}
