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
