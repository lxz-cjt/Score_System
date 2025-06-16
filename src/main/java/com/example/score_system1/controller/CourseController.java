package com.example.score_system1.controller;

import com.example.score_system1.entity.Course;
import com.example.score_system1.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping
    public ResponseEntity<String> insertCourse(@RequestBody Course course) {
        try {
            courseService.insertCourse(course);
            return ResponseEntity.ok("课程添加成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("添加课程失败: " + e.getMessage());
        }
    }

    @GetMapping("/{cId}")
    public ResponseEntity<Course> getCourseById(@PathVariable String cId) {
        try {
            Course course = courseService.getCourseById(cId);
            if (course == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null);
            }
            return ResponseEntity.ok(course);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        try {
            List<Course> courses = courseService.getAllCourses();
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @PutMapping
    public ResponseEntity<String> updateCourse(@RequestBody Course course) {
        try {
            courseService.updateCourse(course);
            return ResponseEntity.ok("课程更新成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("更新课程失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{cId}")
    public ResponseEntity<String> deleteCourse(@PathVariable String cId) {
        try {
            courseService.deleteCourse(cId);
            return ResponseEntity.ok("课程删除成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("删除课程失败: " + e.getMessage());
        }
    }
}