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
<!-- 手动添加索引 -->
   <s:form  action="add">
    <table>
        <tr>
            <td>标题</td>
            <td><s:textfield  name="contentObj.title" />  </td>
        </tr>
        <tr>
            <td>内容</td>
            <td><s:textarea  name="contentObj.content" /></td>
        </tr>
        <tr>
            <td colspan="1"><s:submit value="添加"></s:submit> </td>
        </tr>
    </table>     
   </s:form>
</body>
</html>