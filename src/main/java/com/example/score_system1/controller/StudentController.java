package com.example.score_system1.controller;

import com.example.score_system1.dto.ApiResponse;
import com.example.score_system1.entity.Student;
import com.example.score_system1.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping
    public ApiResponse<String> insertStudent(@RequestBody Student student) {
        try {
            studentService.insertStudent(student);
            return ApiResponse.success("学生添加成功");
        } catch (Exception e) {
            return ApiResponse.error("添加学生失败: " + e.getMessage());
        }
    }

    @GetMapping("/{stId}")
    public ApiResponse<Student> getStudentById(@PathVariable String stId) {
        try {
            Student student = studentService.getStudentById(stId);
            if (student == null) {
                return ApiResponse.error("学生不存在", 404);
            }
            return ApiResponse.success("获取学生信息成功", student);
        } catch (Exception e) {
            return ApiResponse.error("获取学生信息失败: " + e.getMessage());
        }
    }

    @GetMapping
    public ApiResponse<List<Student>> getAllStudents() {
        try {
            List<Student> students = studentService.getAllStudents();
            return ApiResponse.success("获取学生列表成功", students);
        } catch (Exception e) {
            return ApiResponse.error("获取学生列表失败: " + e.getMessage());
        }
    }

    @PutMapping
    public ApiResponse<String> updateStudent(@RequestBody Student student) {
        try {
            studentService.updateStudent(student);
            return ApiResponse.success("学生信息更新成功");
        } catch (Exception e) {
            return ApiResponse.error("更新学生信息失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{stId}")
    public ApiResponse<String> deleteStudent(@PathVariable String stId) {
        try {
            studentService.deleteStudent(stId);
            return ApiResponse.success("学生删除成功");
        } catch (Exception e) {
            return ApiResponse.error("删除学生失败: " + e.getMessage());
        }
    }
}