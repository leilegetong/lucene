package com.searchengine.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;


public class FindTongYi {
		static FSDirectory dir;
		static IndexReader  reader ;
		static IndexSearcher  search;
	

	
	public static  List<String> getTongYi(String keyWord){
		
		  List<String> list = new ArrayList<String>();
			try {
				reader = IndexReader.open(getDirectory());
				search=new IndexSearcher(reader);
				//精确查询，TermQuery 里面内容必须完全匹配才能查询到结果
	            TermQuery  query = new TermQuery(new Term("word",keyWord));

	            //10000为最多查询条数
	            TopScoreDocCollector topCollector = TopScoreDocCollector.create(10000, true);
	            search.search(query, topCollector); 
	            //取得查询结果
	            TopDocs topDocs = topCollector.topDocs(); 
	            int resultCount=topDocs.totalHits;
	            for(int i=0;i<resultCount;i++){
	                Document doc = search.doc(topDocs.scoreDocs[i].doc);
	                list.add(doc.get("syn"));
	            }
			} catch (IOException e) {
				e.printStackTrace();
			
		    }
		
		return list; 
		
	}

	private static Directory getDirectory() {
		try {
			dir = FSDirectory.open(new File("/home/as/lucentindex/wordnetindex/"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	
	

}
