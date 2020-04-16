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
