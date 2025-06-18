package com.example.score_system1.controller;

import com.example.score_system1.dto.ApiResponse;
import com.example.score_system1.entity.Teacher;
import com.example.score_system1.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @PostMapping
    public ApiResponse<String> insertTeacher(@RequestBody Teacher teacher) {
        try {
            teacherService.insertTeacher(teacher);
            return ApiResponse.success("教师添加成功");
        } catch (Exception e) {
            return ApiResponse.error("添加教师失败: " + e.getMessage());
        }
    }

    @GetMapping("/{teId}")
    public ApiResponse<Teacher> getTeacherById(@PathVariable String teId) {
        try {
            Teacher teacher = teacherService.getTeacherById(teId);
            if (teacher == null) {
                return ApiResponse.error("教师不存在", 404);
            }
            return ApiResponse.success("获取教师信息成功", teacher);
        } catch (Exception e) {
            return ApiResponse.error("获取教师信息失败: " + e.getMessage());
        }
    }

    @GetMapping
    public ApiResponse<List<Teacher>> getAllTeachers() {
        try {
            List<Teacher> teachers = teacherService.getAllTeachers();
            return ApiResponse.success("获取教师列表成功", teachers);
        } catch (Exception e) {
            return ApiResponse.error("获取教师列表失败: " + e.getMessage());
        }
    }

    @PutMapping
    public ApiResponse<String> updateTeacher(@RequestBody Teacher teacher) {
        try {
            teacherService.updateTeacher(teacher);
            return ApiResponse.success("教师信息更新成功");
        } catch (Exception e) {
            return ApiResponse.error("更新教师信息失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{teId}")
    public ApiResponse<String> deleteTeacher(@PathVariable String teId) {
        try {
            teacherService.deleteTeacher(teId);
            return ApiResponse.success("教师删除成功");
        } catch (Exception e) {
            return ApiResponse.error("删除教师失败: " + e.getMessage());
        }
    }
}