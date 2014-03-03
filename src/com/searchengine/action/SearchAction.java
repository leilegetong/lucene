package com.searchengine.action;

import java.util.ArrayList;
import java.util.List;

import javassist.compiler.ast.Keyword;

import org.apache.commons.codec.language.Metaphone;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.util.Version;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.base.action.BaseAction;
import com.searchengine.core.MetaphoneReplacementAnalyzer;
import com.searchengine.core.MetaphoneReplacementFilter;
import com.searchengine.core.SearchEngineCore;
import com.searchengine.core.SearchObject;
import com.searchengine.entity.ContentObject;
import com.searchengine.util.PackContentObject;

/**
 *  Class Name: SearchAction.java   搜索类
 *  @author JLC
 *  @version 1.0
 */
public class SearchAction extends BaseAction {

    /**
     * 
     */
    private static final long serialVersionUID = 3141706082945311957L;
    /**
     * 搜索关键字
     */
    private String keyWord ;
    /**
     * 结果集
     */
    private List<ContentObject> searchResult;

    private ContentObject contentObj;
    /**
     *  Function:
     *  @author  
     *  @return
     *  @throws Exception
     */
    @Action(
      value = "index", 
      results = { @Result(name = SUCCESS,  location = "/result.jsp") }
    	)  
    public String searcheContent() throws Exception {
	
        searchResult =  new ArrayList<ContentObject>();
        try{
            Version v = Version.LUCENE_42;
            //取得查询对象
            IndexReader[] readers =  SearchObject.getInstance().getSearcherReads();
            MultiReader mReaders = new MultiReader(readers);
            IndexSearcher indexSearch = new  IndexSearcher(mReaders);
//            Analyzer analyzer =new StandardAnalyzer(v.LUCENE_42);
            Analyzer analyzer=new MetaphoneReplacementAnalyzer();
            //创建boolean查询
            String[] field = {"title","content"};
            BooleanClause.Occur[] flags = new BooleanClause.Occur[2];
            flags[0] = BooleanClause.Occur.SHOULD;
            flags[1] = BooleanClause.Occur.SHOULD;
            Query query1 = MultiFieldQueryParser.parse(v, keyWord, field, flags, analyzer);
            TopScoreDocCollector topCollector = TopScoreDocCollector.create(100000, true);
            System.out.println(query1.toString());
            indexSearch.search(query1, topCollector); 
            //取得查询结果
            TopDocs topDocs = topCollector.topDocs(); 
            int resultCount=topDocs.totalHits;

            //System.out.println(resultCount);
            for(int i=0;i<resultCount;i++){
                Document doc = indexSearch.doc(topDocs.scoreDocs[i].doc);
                //转换Document对象为内容对象
                //ContentObject content = PackContentObject.convertDocToContent(doc);
                ContentObject content = PackContentObject.convertDocToContent(doc, query1, analyzer);
                //加入到结果列表 返回给前台页面获取
                searchResult.add(content);
          	    getRequest().setAttribute("searchResult", searchResult);

            }
        }catch(Exception e){
            e.printStackTrace();
        }

       return SUCCESS;  
    }
    
    @Action(
    	      value = "test", 
    	      results = { @Result(name = SUCCESS,  location = "/result.jsp") }
    	    	)  
    public String test(){
    			return SUCCESS;
    }
    
    
    public String getKeyWord() {
        return keyWord;
    }
    public void setKeyWord(String keyWord) {
   
        this.keyWord = keyWord;
    }
    public List<ContentObject> getSearchResult() {
        return searchResult;
    }
    public ContentObject getContentObj() {
        return contentObj;
    }
    public void setContentObj(ContentObject contentObj) {
        this.contentObj = contentObj;
    }
    
    @Action(
            value = "add", 
            results = { @Result(name = SUCCESS, type="redirect", location = "/index.jsp") }
        )  
        public String addContent() throws Exception {  
            try{
                SearchEngineCore  se = SearchObject.getInstance().getLuceneContext("search");
                Document doc = PackContentObject.convertContentToDoc(contentObj);
                System.out.println(doc.get("title")+"sddsds");
                se.getTw().addDocument(doc);
                se.commitIndex();
                se.refreshData();
            }catch(Exception e){
                e.printStackTrace();
            }
           return SUCCESS;  
        }
}