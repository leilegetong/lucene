<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
     <!-- 添加索引 -->
     <s:form action="index">
         <input type="text"  name="keyWord" ><input type="submit" name="" value="搜索">
     </s:form >
     <a href="addcontent.jsp" >添加内容</a>
     
  </body>
</html>