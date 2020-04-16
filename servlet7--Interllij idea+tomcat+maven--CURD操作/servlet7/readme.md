注意：我使用的是Interllij idea来编辑项目的，运行代码之前要自行下载一个tomcat，在项目中引入tomcat中lib文件夹中的jar包，这样可以使得我们不需要导入过多的包
运行项目之前，还需要安装一个MySQL数据库，并使其运行，数据库名称和表名称都可以在下面参数中修改，可以参考我的数据表
```
CREATE TABLE IF NOT EXISTS `think_test` (
  `id` int(4) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `age` int(4) NOT NULL,
  `sex` varchar(64) NOT NULL,
  `course` varchar(64) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=25 ;

-- 转存表中的数据 `think_test`

INSERT INTO `think_test` (`id`, `name`, `age`, `sex`, `course`) VALUES
(19, 'talent', 21, '1', 'julia'),
(20, 'aa', 21, '1', 'python'),
(22, 'libai', 23, '1', 'highMath');
```
还有一点，代码中与网页交互中通讯没有检测状态，只是纯粹的打印到控制台，增加学生提交数据时的格式也没有过检测
整个项目的文件结构
![](https://img2020.cnblogs.com/blog/1134658/202004/1134658-20200416213116323-549697271.png)

dao包下的dao接口， StudentDao
```
package dao;

import entity.Student;
import java.util.List;

public interface StudentDao {
    List<Student> findAll();

    void insert(Student student);

    void delete(Integer id);

    void update(Student student);

    Student find(Integer id);
}
```
接口StudentDao的实现类
```
package dao;

import db.DBUtils;
import entity.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class StudentDaoImpl implements StudentDao{

    @Override
    public List<Student> findAll(){
        List<Student> list = new ArrayList<Student>();
        Connection conn = null;
        try{
            conn = DBUtils.getConnection();
            Statement st = conn.createStatement();
            String sql = "SELECT * FROM  think_test ORDER BY id";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()){
                Integer id = rs.getInt("id");
                String name = rs.getString("name");
                Integer age = rs.getInt("age");
                String sex = rs.getString("sex");
                String course = rs.getString("course");

                Student s = new Student();
                s.setId(id);
                s.setName(name);
                s.setAge(age);
                s.setSex(sex);
                s.setCourse(course);

                list.add(s);
            }
            System.out.println("返回list");
            return list;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("获取数据失败", e);
        } finally {
            DBUtils.close(conn);
            System.out.println("归还连接");
        }
    }

    @Override
    public void insert(Student s) {
            Connection conn = null;
        try {
            conn = DBUtils.getConnection();
            String sql = "INSERT INTO think_test (name, age, sex, course) VALUES(?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, s.getName());
            ps.setInt(2, s.getAge());
            ps.setString(3, s.getSex());
            ps.setString(4, s.getCourse());

            System.out.println("ps = " + ps);

            int back = ps.executeUpdate();
            System.out.println("执行结果：" + back);

        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("保存数据错误", e);
        } finally {
            DBUtils.close(conn);
        }
    }

    @Override
    public void delete(Integer id){
        Connection conn = null;
        try {
            conn = DBUtils.getConnection();
            String sql = "DELETE FROM think_test WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            int back = ps.executeUpdate();
            System.out.println("删除 back = " + back);

        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("删除出错", e);
        } finally {
            DBUtils.close(conn);
        }
    }

    @Override
    public Student find(Integer id){
        Connection conn = null;
        try{
            conn = DBUtils.getConnection();
            String sql = "SELECT * FROM think_test WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            Student student = new Student();
            while(rs.next()){
                id = rs.getInt("id");
                String name = rs.getString("name");
                Integer age = rs.getInt("age");
                String sex = rs.getString("sex");
                String course = rs.getString("course");

                System.out.println("name = " + name);
                student.setId(id);
                student.setName(name);
                student.setAge(age);
                student.setSex(sex);
                student.setCourse(course);
            }
            return student;
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("查询数据出错");
        } finally {
            DBUtils.close(conn);
        }
    }

    @Override
    public void update(Student student){
        Connection conn = null;
        try{
            conn = DBUtils.getConnection();
            String sql = "UPDATE think_test SET name=?,age=?,sex=?,course=? WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, student.getName());
            ps.setInt(2, student.getAge());
            ps.setString(3, student.getSex());
            ps.setString(4, student.getCourse());
            ps.setInt(5, student.getId());

            int back = ps.executeUpdate();
            System.out.println("更新 back = " + back);

        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("更新数据出错");
        } finally {
            DBUtils.close(conn);
        }
    }
}
```
db包下的数据库连接池DBUtils类
```
package db;

import org.apache.commons.dbcp.BasicDataSource;

import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

public class DBUtils {
    private static String driver;
    private static String url;
    private static String username;
    private static String password;
    private static int initialSize;
    private static int maxActive;

    private static BasicDataSource ds;

    static{
        try {
            Properties cfg = new Properties();
            InputStream input = DBUtils.class.getClassLoader().getResourceAsStream("db.properties");
            cfg.load(input);
            driver = cfg.getProperty("jdbc.driver");
            url = cfg.getProperty("jdbc.url");
            username = cfg.getProperty("jdbc.username");
            password = cfg.getProperty("jdbc.password");
            initialSize = Integer.valueOf(cfg.getProperty("initialSize"));
            maxActive = Integer.valueOf(cfg.getProperty("maxActive"));

            ds = new BasicDataSource();
            ds.setDriverClassName(driver);
            ds.setUrl(url);
            ds.setUsername(username);
            ds.setPassword(password);
            ds.setInitialSize(initialSize);
            ds.setMaxActive(maxActive);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static Connection getConnection(){
        Connection conn = null;
        try{
            conn = ds.getConnection();
            return conn;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("获取错误");
        }
    }

    public static void close(Connection conn){
        if(conn != null){
            try {
                conn.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
```
实体类我放在entity包下，Student类
```
package entity;

import java.io.Serializable;

public class Student implements Serializable {
    private int id;
    private String name;
    private String sex;
    private String course;
    private int age;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
```
web包下的MainServlet，处理CURD的逻辑
```
package web;

import dao.StudentDao;
import dao.StudentDaoImpl;
import entity.Student;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.io.PrintWriter;
import java.util.*;

public class MainServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String path = req.getServletPath();
        System.out.println("path = " + path);
        if("/findStudent.do".equals(path)){
            findStudent(req, res);
        }else if("/addStudentPage.do".equals(path)){
            addStudentPage(req, res);
        }else if("/addStudent.do".equals(path)){
            addStudent(req, res);
        }else if("/deleteStudent.do".equals(path)){
            deleteStudent(req, res);
        }else if("/editStudentPage.do".equals(path)){
            editStudentPage(req, res);
        }else if("/editStudent.do".equals(path)){
            editStudent(req, res);
        } else{
            throw new RuntimeException("找不到页面");
        }
    }

    protected void findStudent(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        StudentDao studentDao = new StudentDaoImpl();
        List<Student> list = studentDao.findAll();
        req.setAttribute("list", list);
        req.getRequestDispatcher("WEB-INF/student/findStudent.jsp").forward(req, res);
    }

    protected void addStudentPage(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.getRequestDispatcher("WEB-INF/student/addStudentPage.jsp").forward(req, res);
    }

    protected void addStudent(HttpServletRequest req, HttpServletResponse res) throws IOException {
        req.setCharacterEncoding("utf-8");
        String name = req.getParameter("name");
        String age = req.getParameter("age");
        String sex = req.getParameter("sex");
        String course = req.getParameter("course");

        Student student = new Student();
        student.setName(name);
        student.setAge(Integer.valueOf(age));
        student.setSex(sex);
        student.setCourse(course);

        StudentDao studentDao = new StudentDaoImpl();
        studentDao.insert(student);

        res.sendRedirect("findStudent.do");
    }

    protected void deleteStudent(HttpServletRequest req, HttpServletResponse res) throws IOException {
        req.setCharacterEncoding("utf-8");
        String id = req.getParameter("id");
        System.out.println("id = " + id);

        StudentDao studentDao = new StudentDaoImpl();
        String data = null;
        try {
            studentDao.delete(Integer.valueOf(id));
             data = "删除成功";

        } catch (Exception e){
            data = "删除失败";
        }

        res.setContentType("text/html;charset=utf-8");
        PrintWriter out = res.getWriter();
        out.println(data);
        out.close();
    }

    protected void editStudent(HttpServletRequest req, HttpServletResponse res) throws IOException {
        req.setCharacterEncoding("utf-8");
        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String age = req.getParameter("age");
        String sex = req.getParameter("sex");
        String course = req.getParameter("course");

        Student student = new Student();
        student.setId(Integer.valueOf(id));
        student.setName(name);
        student.setAge(Integer.valueOf(age));
        student.setSex(sex);
        student.setCourse(course);

        StudentDaoImpl studentDao = new StudentDaoImpl();
        studentDao.update(student);

        res.sendRedirect("findStudent.do");
    }

    protected void editStudentPage(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Integer id = Integer.valueOf(req.getParameter("id"));
        StudentDaoImpl studentDao = new StudentDaoImpl();
        Student s = studentDao.find(id);
        req.setAttribute("student", s);
        req.getRequestDispatcher("WEB-INF/student/editStudentPage.jsp").forward(req, res);
    }
}
```
resource包下的db.properties保存数据库连接参数
```
jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost/test
jdbc.username=root
jdbc.password=root

initialSize=2
maxActive=2
```
在web.xml中我们只需要配置一个路径
```
    <servlet>
        <servlet-name>main</servlet-name>
        <servlet-class>web.MainServlet</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>main</servlet-name>
        <url-pattern>*.do</url-pattern>
    </servlet-mapping>
```
下面的就是jsp文件了
FindStudent.jsp文件
```
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
```
addStudent.jsp文件
```
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/8/23/023
  Time: 17:01
  To change this template use File | Settings | File Templates.
--%>
<%@page isELIgnored="false" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <meta charset="utf-8">
    <link rel="stylesheet" href="/static/bootstrap.css">
</head>
<body>
    <div class="container">
        <form class="form-horizontal" style="margin-top: 30px;" action="addStudent.do" method="post">
            <div class="form-group">
                <label for="name" class="col-sm-2 control-label">名称</label>
                <div class="col-sm-10">
                    <input type="text" class="form-control" id="name" placeholder="请输入名称" name="name">
                </div>
            </div>
            <div class="form-group">
                <label for="age" class="col-sm-2 control-label">年龄</label>
                <div class="col-sm-10">
                    <input type="text" class="form-control" id="age" placeholder="请输入年龄" name="age">
                </div>
            </div>
            <div class="form-group">
                <label for="sex" class="col-sm-2 control-label">性别</label>
                <div class="col-sm-10">
                    <select class="form-control" id="sex" name="sex">
                        <option value="1">男</option>
                        <option value="0">女</option>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label for="course" class="col-sm-2 control-label">课程</label>
                <div class="col-sm-10">
                    <input type="text" class="form-control" id="course" placeholder="请输入课程" name="course">
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
```
editStudent.jsp，与增加是差不多的页面
```
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
```
maven导包
```
  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.11</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>commons-dbcp</groupId>
      <artifactId>commons-dbcp</artifactId>
      <version>1.4</version>
    </dependency>
    <dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
      <version>5.1.46</version>
    </dependency>
    <dependency>
      <groupId>jstl</groupId>
      <artifactId>jstl</artifactId>
      <version>1.2</version>
    </dependency>
  </dependencies>
```
![](https://img2020.cnblogs.com/blog/1134658/202004/1134658-20200416213154214-1157181941.png)

![](https://img2020.cnblogs.com/blog/1134658/202004/1134658-20200416213214964-1711601162.png)

![](https://img2020.cnblogs.com/blog/1134658/202004/1134658-20200416213235779-1743160620.png)

![](https://img2020.cnblogs.com/blog/1134658/202004/1134658-20200416213306473-1517798288.png)

分享代码：github https://github.com/libai2017/Java-demo.git
