package com.example.score_system1.controller;

import com.example.score_system1.dto.ApiResponse;
import com.example.score_system1.entity.CourseArranging;
import com.example.score_system1.service.CourseArrangingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course-arrangements")
public class CourseArrangingController {

    @Autowired
    private CourseArrangingService courseArrangingService;

    @PostMapping
    public ApiResponse<String> insertCourseArranging(@RequestBody CourseArranging courseArranging) {
        try {
            courseArrangingService.insertCourseArranging(courseArranging);
            return ApiResponse.success("课程安排添加成功");
        } catch (Exception e) {
            return ApiResponse.error("添加课程安排失败: " + e.getMessage());
        }
    }

    @GetMapping
    public ApiResponse<List<CourseArranging>> getAllCourseArrangings() {
        try {
            List<CourseArranging> courseArrangings = courseArrangingService.getAllCourseArrangings();
            return ApiResponse.success("获取所有课程安排列表成功", courseArrangings);
        } catch (Exception e) {
            return ApiResponse.error("获取所有课程安排列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/{caId}")
    public ApiResponse<CourseArranging> getCourseArrangingById(@PathVariable String caId) {
        try {
            CourseArranging courseArranging = courseArrangingService.getCourseArrangingById(caId);
            if (courseArranging == null) {
                return ApiResponse.error("课程安排不存在", 404);
            }
            return ApiResponse.success("获取课程安排信息成功", courseArranging);
        } catch (Exception e) {
            return ApiResponse.error("获取课程安排信息失败: " + e.getMessage());
        }
    }

    @GetMapping("/course/{cId}")
    public ApiResponse<List<CourseArranging>> getCourseArrangingsByCourseId(@PathVariable String cId) {
        try {
            List<CourseArranging> courseArrangings = courseArrangingService.getCourseArrangingsByCourseId(cId);
            return ApiResponse.success("获取课程安排列表成功", courseArrangings);
        } catch (Exception e) {
            return ApiResponse.error("获取课程安排列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/teacher/{teId}")
    public ApiResponse<List<CourseArranging>> getCourseArrangingsByTeacherId(@PathVariable String teId) {
        try {
            List<CourseArranging> courseArrangings = courseArrangingService.getCourseArrangingsByTeacherId(teId);
            return ApiResponse.success("获取教师课程安排列表成功", courseArrangings);
        } catch (Exception e) {
            return ApiResponse.error("获取教师课程安排列表失败: " + e.getMessage());
        }
    }

    @PutMapping
    public ApiResponse<String> updateCourseArranging(@RequestBody CourseArranging courseArranging) {
        try {
            courseArrangingService.updateCourseArranging(courseArranging);
            return ApiResponse.success("课程安排更新成功");
        } catch (Exception e) {
            return ApiResponse.error("更新课程安排失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{caId}")
    public ApiResponse<String> deleteCourseArranging(@PathVariable String caId) {
        try {
            courseArrangingService.deleteCourseArranging(caId);
            return ApiResponse.success("课程安排删除成功");
        } catch (Exception e) {
            return ApiResponse.error("删除课程安排失败: " + e.getMessage());
        }
    }
}