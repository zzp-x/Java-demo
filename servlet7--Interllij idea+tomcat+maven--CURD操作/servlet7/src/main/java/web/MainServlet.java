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
