<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/8/23/023
  Time: 10:09
  To change this template use File | Settings | File Templates.
--%>
<%@page isELIgnored="false"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Title</title>
    <meta charset="utf-8">
    <link rel="stylesheet" href="/static/bootstrap.css">
    <script src="/static/jquery-3.3.1.min.js"></script>
    <script src="/static/bootstrap.js"></script>
</head>
<body>

    <div class="container">
        <a type="button" class="btn btn-info" style="float: right;margin: 10px;" href="addStudentPage.do">增加学生</a>
        <table class="table table-striped table-bordered" style="margin: 30px auto; text-align: center;">
            <tr>
                <td>编号</td>
                <td>名称</td>
                <td>年龄</td>
                <td>性别</td>
                <td>课程</td>
                <td>操作</td>
            </tr>
            <c:forEach items="${list}" var="l">
            <tr>
                <td>${l.id}</td>
                <td>${l.name}</td>
                <td>${l.age}</td>
                <td>
                    <c:if test="${l.sex == '1'}">男</c:if>
                    <c:if test="${l.sex == '0'}">女</c:if>
                </td>
                <td>${l.course}</td>
                <td name="${l.id}">
                    <a type="button" class="btn btn-warning edit-btn" href="editStudentPage.do?id=${l.id}">修改</a>
                    <button type="button" class="btn btn-danger delete-btn" data-toggle="modal"  data-target="#deleteModal">删除</button>
                </td>
            </tr>
            </c:forEach>
        </table>
    </div>

    <!-- Modal -->
    <div class="modal fade" id="deleteModal" tabindex="-1" role="dialog" aria-labelledby="deleteModal">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel"><span style="color: red;">注意</span></h4>
                </div>
                <div class="modal-body">
                    <p>是否确定删除编号为 <span id="id" style="color: red;"></span> 、名称为 <span id="name" style="color: red;"></span> 的学生??</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal" id="delete-cancel">取消</button>
                    <button type="button" class="btn btn-primary" id="delete-certain">确定</button>
                </div>
            </div>
        </div>
    </div>


    <script>
        $(".delete-btn").on("click", function () {
            var id = $(this).parent().attr("name");
            var name = $(this).parent().parent().children().eq(1).text();
            $("#id").text(id);
            $("#name").text(name);
        });

        $("#delete-certain").on("click", function () {
            var id = $("#id").text();
            $.ajax({
                url: "deleteStudent.do",
                type: "POST",
                data: {'id' : id},
                success: function (result) {
                    console.log(result);

                    setTimeout(function (args) { $('#delete-cancel').click();$('td[name=' + id + ']').parent()[0].remove(); }, 600);
                },
                error:function (result) {
                    console.log(result);
                }
            })
        })

    </script>
</body>
</html>
