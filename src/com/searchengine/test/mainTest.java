package com.searchengine.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;

import com.searchengine.core.SearchEngineCore;
import com.searchengine.core.SearchObject;
import com.searchengine.entity.ContentObject;
import com.searchengine.util.FindTongYi;
import com.searchengine.util.PackContentObject;
import com.searchengine.util.WebContent;

public class mainTest {
    /**
     *  Function:抓取网页内容
     *  @author XC
     *  @return
     */
    public static List<Document>  getWebContentDocuments(){
        List<Document> docs = new ArrayList<Document>();
        WebContent wc = new WebContent();
        Document doc1 = PackContentObject.convertContentToDoc(wc.getContentFromTxt("/home/as/下载/cool.txt"));
        docs.add(doc1);
        System.out.println(doc1.get("title")+"sddssds");
        return docs;
    }
    /**
     * 创建索引
     */
    public static void createSearchEngineData(){
         SearchEngineCore  se = SearchObject.getInstance().getLuceneContext("search");

         List<Document> docList = getWebContentDocuments();
         for(Document doc:docList){
             try{
                 se.getTw().addDocument(doc);
                 se.commitIndex();
                 se.refreshData();
             }catch(Exception e){
                 e.printStackTrace();
             }
         }
    }

    
    
    /**
     * 创建搜索方法
     * @param keyWord
     */
    public static void searchContent(String keyWord){
        //List<ContentObject> searcheResult =  new ArrayList<ContentObject>();
        try{
            /*
            Version v = Version.LUCENE_42;
            //取得查询对象
            IndexReader[] readers =  SearchObject.getInstance().getSearcherReads();
            //多域查询
            MultiReader mReaders = new MultiReader(readers);
            IndexSearcher indexSearch = new  IndexSearcher(mReaders); 
            //自带的标准分词
            Analyzer analyzer =new StandardAnalyzer(v);
            //创建boolean查询
            BooleanQuery query = new BooleanQuery();
            String[] field = {"title", "content"};
            BooleanClause.Occur[] flags = new BooleanClause.Occur[2];
            flags[0] = BooleanClause.Occur.SHOULD;
            flags[1] = BooleanClause.Occur.SHOULD;
            Query query1 = MultiFieldQueryParser.parse(v, QueryParser.escape(keyWord), field, flags, analyzer);
            //必须满足该查询条件
            query.add(query1, Occur.MUST);
            */

            //精确查询，TermQuery 里面内容必须完全匹配才能查询到结果
            IndexSearcher indexSearch = SearchObject.getInstance().getSearcher("search");
            TermQuery  query = new TermQuery(new Term("title",keyWord));

            //10000为最多查询条数
            TopScoreDocCollector topCollector = TopScoreDocCollector.create(10000, true);
            indexSearch.search(query, topCollector); 
            //取得查询结果
            TopDocs topDocs = topCollector.topDocs(); 
            int resultCount=topDocs.totalHits;
            for(int i=0;i<resultCount;i++){
                Document doc = indexSearch.doc(topDocs.scoreDocs[i].doc);
                String content = doc.get("content");
                System.out.println("标题："+doc.get("title"));
                if(content.length()>=200)
                	System.out.println("内容："+content.substring(0,200));
                else
                	System.out.println("内容："+content);
            }
            System.out.println("查询结果条数："+resultCount);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /**
     * 查看索引
     */
    public static void checkIndex(){
    	SearchEngineCore se = SearchObject.getInstance().getLuceneContext("search");
    	se.checkIndex();
    }
    
    /**
     * 删除索引内容
     */
    public static void deleteContent(String title){
        try{
             SearchEngineCore  se =  SearchObject.getInstance().getLuceneContext("search");
             se.getTw().deleteDocuments(new Term("title",title));
             se.commitIndex();
       /**
             se.refreshData();
             se.getNRTManager().maybeRefresh();
       **/
         }catch(Exception e){
             e.printStackTrace();
         }  
    }

    /**
     * 更新索引内容
     * @param term  原索引
     * @param content  更新的内容
     */
    public static void updateContent(Term term,ContentObject content ){
        try{
            SearchEngineCore  se =  SearchObject.getInstance().getLuceneContext("search");
            Document doc = PackContentObject.convertContentToDoc(content);
            se.getTw().updateDocument(term,doc);
           
               se.commitIndex();
               /**
             se.refreshData();
             se.getNRTManager().maybeRefresh();
             **/
             
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    



    public static void main(String args[]){
        
/**
         //创建索引数据
        createSearchEngineData(); 
    		checkIndex();
        searchContent("cool.txt");
        		
        //更新所有title=网易新闻 的索引   如果title=网易新闻 的索引有多条，更新后只会全部删除
        Term term = new Term("title","网易新闻");
        System.out.println(term);
        ContentObject content  =new ContentObject();
        content.setTitle("test");
        content.setContent("测试内容");
        updateContent(term,content);
        //删除title=test  的索引
        deleteContent("test");
         * **/
        List<String> list = FindTongYi.getTongYi("cool");
        System.out.println(list.get(0));
       
    
    }

}