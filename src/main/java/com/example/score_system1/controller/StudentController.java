package com.example.score_system1.controller;

import com.example.score_system1.entity.Student;
import com.example.score_system1.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping
    public ResponseEntity<String> insertStudent(@RequestBody Student student) {
        try {
            studentService.insertStudent(student);
            return ResponseEntity.ok("学生添加成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("添加学生失败: " + e.getMessage());
        }
    }

    @GetMapping("/{stId}")
    public ResponseEntity<Student> getStudentById(@PathVariable String stId) {
        try {
            Student student = studentService.getStudentById(stId);
            if (student == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null);
            }
            return ResponseEntity.ok(student);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        try {
            List<Student> students = studentService.getAllStudents();
            return ResponseEntity.ok(students);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @PutMapping
    public ResponseEntity<String> updateStudent(@RequestBody Student student) {
        try {
            studentService.updateStudent(student);
            return ResponseEntity.ok("学生信息更新成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("更新学生信息失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{stId}")
    public ResponseEntity<String> deleteStudent(@PathVariable String stId) {
        try {
            studentService.deleteStudent(stId);
            return ResponseEntity.ok("学生删除成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("删除学生失败: " + e.getMessage());
        }
    }
}