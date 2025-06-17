package com.example.score_system1.controller;

import com.example.score_system1.entity.CourseArranging;
import com.example.score_system1.service.CourseArrangingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course-arrangements")
public class CourseArrangingController {

    @Autowired
    private CourseArrangingService courseArrangingService;

    @PostMapping
    public ResponseEntity<String> insertCourseArranging(@RequestBody CourseArranging courseArranging) {
        try {
            courseArrangingService.insertCourseArranging(courseArranging);
            return ResponseEntity.ok("课程安排添加成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("添加课程安排失败: " + e.getMessage());
        }
    }

    @GetMapping("/{caId}")
    public ResponseEntity<CourseArranging> getCourseArrangingById(@PathVariable String caId) {
        try {
            CourseArranging courseArranging = courseArrangingService.getCourseArrangingById(caId);
            if (courseArranging == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null);
            }
            return ResponseEntity.ok(courseArranging);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/course/{cId}")
    public ResponseEntity<List<CourseArranging>> getCourseArrangingsByCourseId(@PathVariable String cId) {
        try {
            List<CourseArranging> courseArrangings = courseArrangingService.getCourseArrangingsByCourseId(cId);
            return ResponseEntity.ok(courseArrangings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/teacher/{teId}")
    public ResponseEntity<List<CourseArranging>> getCourseArrangingsByTeacherId(@PathVariable String teId) {
        try {
            List<CourseArranging> courseArrangings = courseArrangingService.getCourseArrangingsByTeacherId(teId);
            return ResponseEntity.ok(courseArrangings);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @PutMapping
    public ResponseEntity<String> updateCourseArranging(@RequestBody CourseArranging courseArranging) {
        try {
            courseArrangingService.updateCourseArranging(courseArranging);
            return ResponseEntity.ok("课程安排更新成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("更新课程安排失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{caId}")
    public ResponseEntity<String> deleteCourseArranging(@PathVariable String caId) {
        try {
            courseArrangingService.deleteCourseArranging(caId);
            return ResponseEntity.ok("课程安排删除成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("删除课程安排失败: " + e.getMessage());
        }
    }
}