<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 2017/8/28
  Time: 22:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>生成架构代码</title>
    <style type="text/css">
        body { font-family:arial,sans-serif; font-size:9pt; }
        input
        { margin-right:10px;}
        .my_clip_button { width:60px; text-align:center; border:1px solid black; background-color:#ccc; margin:10px; padding:10px; cursor:pointer; font-size:9pt; }
        .my_clip_button.hover { background-color:#eee; }
        .my_clip_button.active { background-color:#aaa; }
        #tableid{width:500px;}
        #tableid .td2
        { width:90px;}
        #tableid tr
        { height:auto;}
        #tableid tr td
        { height:auto; padding:10px;}
        #txtContent,#txtResult
        { width:400px; height:60px;}
    </style>
</head>
<body>
<form id="form1" action="/createCode/create" method="post">
    <div style="width: 500px; height: 50px; color: Red; font-size: 16px; line-height: 50px;
        text-align: center;">
        生成架构代码</div>
    <div>
        生成BLL或MODEL时，只有表名填写有效<br />
        生成DAL时，可以填写其它
        <p />
        　　　　　表名：
        <select name="tableName">

            <c:forEach items="${tables}" var="t">
                <option value="${t}">${t}</option>
            </c:forEach>

        </select>

        <br /><br /><br />
        　
        　　　存放路径：
        <input type="text" id="txtSavePath" name="txtSavePath" style="width: 500"/>
        <p />
        <input type="submit" value="生成代码">
        <p />
    </div>
</form>
</body>
</html>