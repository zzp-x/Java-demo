<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/8/23/023
  Time: 17:01
  To change this template use File | Settings | File Templates.
--%>
<%@page isELIgnored="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Title</title>
    <meta charset="utf-8">
    <link rel="stylesheet" href="/static/bootstrap.css">
</head>
<body>
    <div class="container">
        <form class="form-horizontal" style="margin-top: 30px;" action="editStudent.do" method="post">
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">名称</label>
                <div class="col-sm-10">
                    <input type="text" class="form-control" id="name" placeholder="请输入名称" name="name" value="${student.name}">
                    <input type="hidden" class="form-control" name="id" value="${student.id}">
                </div>
            </div>
            <div class="form-group">
                <label for="age" class="col-sm-2 control-label">年龄</label>
                <div class="col-sm-10">
                    <input type="text" class="form-control" id="age" placeholder="请输入年龄" name="age" value="${student.age}">
                </div>
            </div>
            <div class="form-group">
                <label for="sex" class="col-sm-2 control-label">性别</label>
                <div class="col-sm-10">
                    <select class="form-control" id="sex" name="sex">
                        <c:if test="${student.sex == '1'}"><option value="1" selected>男</option><option value="0">女</option></c:if>
                        <c:if test="${student.sex == '0'}"><option value="1">男</option><option value="0" selected>女</option></c:if>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label for="course" class="col-sm-2 control-label">课程</label>
                <div class="col-sm-10">
                    <input type="text" class="form-control" id="course" placeholder="请输入课程" name="course" value="${student.course}">
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <button type="submit" class="btn btn-default">提交</button>
                </div>
            </div>
        </form>
    </div>
</body>
</html>
