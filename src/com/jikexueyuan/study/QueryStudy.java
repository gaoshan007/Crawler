package com.jikexueyuan.study;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.util.Version;

public class QueryStudy {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String key = "极客学院";
		String field = "name";
		String [] fields = {"name", "content"};
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_43);
		Query query = null;
		
		QueryParser parser = new QueryParser(Version.LUCENE_43, field, analyzer);
		
		try {
			query = parser.parse(key);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(QueryParser.class + query.toString());
		MultiFieldQueryParser parser1 = new MultiFieldQueryParser(Version.LUCENE_43, fields, analyzer);
		
		try {
			query = parser1.parse(key);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(MultiFieldQueryParser.class + query.toString());
		
		query = new TermQuery(new Term(field, key));
		System.out.println(TermQuery.class + query.toString());
		
		query = new PrefixQuery(new Term(field, key));
		System.out.println(PrefixQuery.class + query.toString());
		
		PhraseQuery query1 = new PhraseQuery();
		query1.setSlop(2);
		query1.add(new Term(field, "极客学院"));
		query1.add(new Term(field, "Lucene案例"));
		System.out.println(PhraseQuery.class + query1.toString());
		
		query = new WildcardQuery(new Term(field, "极客学院?"));
		System.out.println(WildcardQuery.class + query.toString());
		
		query = TermRangeQuery.newStringRange(field, "abc", "azz", false, false);
		System.out.println(TermRangeQuery.class + query.toString());
		
		BooleanQuery query2 = new BooleanQuery();
		query2.add(new TermQuery(new Term(field, "极客学院")), Occur.SHOULD);
		query2.add(new TermQuery(new Term(field, "Lucene")), Occur.MUST);
		query2.add(new TermQuery(new Term(field, "案例")), Occur.MUST_NOT);
	}

}
