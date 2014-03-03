package com.searchengine.core;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.LogDocMergePolicy;
import org.apache.lucene.index.LogMergePolicy;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NRTManager;
import org.apache.lucene.search.NRTManager.TrackingIndexWriter;
import org.apache.lucene.search.NRTManagerReopenThread;
import org.apache.lucene.search.SearcherFactory;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
public class SearchEngineCore {
    public  String INDEX_PATH = "/home/as/lucentindex/searchdata/";//"/data/lucene/store/";
    private  IndexWriter writer;
    private  Analyzer analyzer;
    private  Version version =Version.LUCENE_42;
    private  NRTManager nrtMgr;
    private  TrackingIndexWriter tw;
    private  SearcherManager sm;
    private   Directory directory;
    private  NRTManagerReopenThread reopenThread ;
    public SearchEngineCore(){}
    /**
     * 加载索引配置
     * @param index
     *@author XC
     */
    public SearchEngineCore(final String index,final String indexPath,final Analyzer analyzer){
        try {
            //索引文件路径
            INDEX_PATH = indexPath;
            //创建索引目录
            directory = FSDirectory.open(new File(INDEX_PATH));
            //标准分词器
//            analyzer =new StandardAnalyzer(version);// new IKAnalyzer();
            SearcherFactory searcherFactory = new SearcherFactory();
            LogMergePolicy mergePolicy = new LogDocMergePolicy();
            //索引基本配置
            //设置segment添加文档(Document)时的合并频率
            //值较小,建立索引的速度就较慢
            //值较大,建立索引的速度就较快,>10适合批量建立索引
            mergePolicy.setMergeFactor(5);
            //设置segment最大合并文档(Document)数
            //值较小有利于追加索引的速度
            //值较大,适合批量建立索引和更快的搜索
            mergePolicy.setMaxMergeDocs(1000);
            //启用复合式索引文件格式,合并多个segment
            mergePolicy.setUseCompoundFile(true);
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(version, analyzer);
            indexWriterConfig.setMaxBufferedDocs(10000);
            indexWriterConfig.setMergePolicy(mergePolicy);
            indexWriterConfig.setRAMBufferSizeMB(50);
            ///设置索引的打开模式 创建或者添加索引
            indexWriterConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
            //如果索引文件被锁，解锁索引文件
            if(IndexWriter.isLocked(directory)){  
                IndexWriter.unlock(directory);  
            }
            //创建索引器
            writer = new IndexWriter(directory,indexWriterConfig);
            //实现近实时搜索
            tw = new NRTManager.TrackingIndexWriter(writer);
            //最开始创建索引时必须先提交，不然引起读取方法报错
            writer.commit();
            nrtMgr = new NRTManager(tw, searcherFactory,true);
            sm =new SearcherManager(directory, searcherFactory);
            //创建IndexWriter 写入监听线程 5.0为创建5个线程，执行频率为0.025秒
            reopenThread = new NRTManagerReopenThread(nrtMgr, 5.0,0.025);
            reopenThread.setName("NRTManager reopen thread");
            reopenThread.setDaemon(true);
            reopenThread.start(); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     *  Function:取得索引对象
     *  @author XC
     *  @return
     */
    public IndexSearcher getSearcher() {
        try{
        return sm.acquire();
    }catch(Exception e){

    }
    return null;
    }

    public void releaseSearcher(IndexSearcher searcher) {
        try {
            sm.release(searcher);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 提交索引
     */
    public void commitIndex() {
        try {
            writer.commit();
            //writer.forceMerge(3);
        } catch (CorruptIndexException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 合并索引
     */
    public void forceMerge(){
        try {
             writer.forceMerge(3);
        } catch (CorruptIndexException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 关闭索引
     */
    public void closeAll(){
        try {
            if(writer!=null)
            writer.close();
        } catch (CorruptIndexException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 查看索引详情
     */
    public  void checkIndex(){
    		try {
				IndexReader reader = IndexReader.open(getDirectory());
				IndexSearcher search=new IndexSearcher(reader);
				System.out.println("索引个数"+reader.numDocs());
				for(int i=0;i<reader.numDocs();i++){
					    Document doc=search.doc(i);//依次获取每个docid对应的Document
					    //可以在此部，做个批量操作，加快写入速度
					    String content = doc.get("content");
		                System.out.println("标题："+doc.get("title"));
		                if(content.length()>=200)
		                	System.out.println("内容："+content.substring(0,200));
		                else
		                	System.out.println("内容："+content);
					    }
//					    reader.close();//关闭资源
//					    directory.close();//关闭连接
		
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    }
    /**
     * 刷新数据
     */
    public void refreshData(){
         try{
             sm.maybeRefresh();
         }catch (Exception e){
             e.printStackTrace();
         }
    }

    public NRTManager getNRTManager() {
        return nrtMgr;
    }

    public Version getVersion() {
        return version;
    }

    public Analyzer getAnalyzer() {
        return analyzer;
    }
    public  Directory getDirectory() {
        return directory;
    }
    public IndexWriter getWriter() {
        return writer;
    }
    public TrackingIndexWriter getTw() {
        return tw;
    }
}