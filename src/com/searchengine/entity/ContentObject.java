package com.searchengine.entity;

import java.util.Date;

/**
 *  Class Name: ContentObject.java  搜索引擎内容对象
 *  @author XC
 */
public class ContentObject {
    /**
     * 内容标题
     */
    private  String title;
    /**
     * 内容详细信息
     */
    private String content; 
    /**
     * 内容创建时间
     */
    
    /**
     * get set方法
     */
    private Date createDate = new Date();
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

    
}