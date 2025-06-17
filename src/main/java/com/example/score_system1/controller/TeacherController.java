package com.example.score_system1.controller;

import com.example.score_system1.entity.Teacher;
import com.example.score_system1.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teachers")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @PostMapping
    public ResponseEntity<String> insertTeacher(@RequestBody Teacher teacher) {
        try {
            teacherService.insertTeacher(teacher);
            return ResponseEntity.ok("教师添加成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("添加教师失败: " + e.getMessage());
        }
    }

    @GetMapping("/{teId}")
    public ResponseEntity<Teacher> getTeacherById(@PathVariable String teId) {
        try {
            Teacher teacher = teacherService.getTeacherById(teId);
            if (teacher == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null);
            }
            return ResponseEntity.ok(teacher);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<Teacher>> getAllTeachers() {
        try {
            List<Teacher> teachers = teacherService.getAllTeachers();
            return ResponseEntity.ok(teachers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @PutMapping
    public ResponseEntity<String> updateTeacher(@RequestBody Teacher teacher) {
        try {
            teacherService.updateTeacher(teacher);
            return ResponseEntity.ok("教师信息更新成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("更新教师信息失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{teId}")
    public ResponseEntity<String> deleteTeacher(@PathVariable String teId) {
        try {
            teacherService.deleteTeacher(teId);
            return ResponseEntity.ok("教师删除成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("删除教师失败: " + e.getMessage());
        }
    }
}