package com.example.score_system1.service;

import com.example.score_system1.entity.Student;
import com.example.score_system1.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 学生业务服务类
 * 提供学生相关的业务逻辑处理
 */
@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    /**
     * 新增学生
     * @param student 学生信息
     * @return 保存后的学生信息
     */
    public Student insertStudent(Student student) {
        return studentRepository.save(student);
    }

    /**
     * 根据学号获取学生信息
     * @param stId 学号
     * @return 学生信息，不存在则返回null
     */
    public Student getStudentById(String stId) {
        Optional<Student> student = studentRepository.findByStudentId(stId);
        return student.orElse(null);
    }

    /**
     * 获取所有学生列表
     * @return 学生列表
     */
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    /**
     * 更新学生信息
     * @param student 学生信息
     * @return 更新后的学生信息
     */
    public Student updateStudent(Student student) {
        return studentRepository.save(student);
    }

    /**
     * 根据学号删除学生
     * @param stId 学号
     */
    public void deleteStudent(String stId) {
        studentRepository.deleteById(stId);
    }

    /**
     * 根据专业查询学生列表
     * @param stMajor 专业
     * @return 学生列表
     */
    public List<Student> getStudentsByMajor(String stMajor) {
        return studentRepository.findByMajor(stMajor);
    }

    /**
     * 根据姓名模糊查询学生
     * @param stName 学生姓名
     * @return 学生列表
     */
    public List<Student> getStudentsByNameContaining(String stName) {
        return studentRepository.findByStudentNameContaining(stName);
    }

    /**
     * 验证学生登录
     * @param stId 学号
     * @param password 密码
     * @return 学生信息，验证失败返回null
     */
    public Student validateStudent(String stId, String password) {
        Optional<Student> student = studentRepository.findByStudentIdAndPassword(stId, password);
        return student.orElse(null);
    }
}