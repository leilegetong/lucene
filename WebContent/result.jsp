<%@ page language="java" contentType="text/html; charset=UTF-8" 
    pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>这是结果</title>

</head>
  <body>

       <s:iterator  id="obj" value="searchResult" >
            标题：<s:property value="#obj.title"  escape="html"/></p>
            内容：<s:property value="#obj.content" escape="html" /></p>
            <hr>
       </s:iterator>
  </body>
</html>