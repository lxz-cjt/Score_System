package com.example.score_system1.mapper;

import com.example.score_system1.entity.Student;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StudentMapper {
    void insertStudent(Student student);
    Student selectStudentById(String stId);
    List<Student> selectAllStudents();
    void updateStudent(Student student);
    void deleteStudent(String stId);
    Student selectStudentByUsername(String username); // 新增方法
}