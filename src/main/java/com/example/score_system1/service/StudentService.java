package com.example.score_system1.service;


import com.example.score_system1.entity.Student;
import com.example.score_system1.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentMapper studentMapper;

    public void insertStudent(Student student) {
        studentMapper.insertStudent(student);
    }

    public Student getStudentById(String stId) {
        return studentMapper.selectStudentById(stId);
    }

    public List<Student> getAllStudents() {
        return studentMapper.selectAllStudents();
    }

    public void updateStudent(Student student) {
        studentMapper.updateStudent(student);
    }

    public void deleteStudent(String stId) {
        studentMapper.deleteStudent(stId);
    }
}