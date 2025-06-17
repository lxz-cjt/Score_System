package com.example.score_system1.controller;

import com.example.score_system1.entity.Course;
import com.example.score_system1.entity.CourseArranging;
import com.example.score_system1.entity.Teacher;
import com.example.score_system1.service.CourseArrangingService;
import com.example.score_system1.service.CourseService;
import com.example.score_system1.service.StudentService;
import com.example.score_system1.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;
    @Autowired
    private CourseArrangingService courseArrangingService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;

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
            //  id: 1,
            //      courseCode: 'CS101',
            //      courseName: '计算机程序设计基础',
            //      teacherName: '李教授',
            //      credits: 4,
            //      courseType: 'required',
            //      schedule: '周一 1-2节，周三 3-4节',
            //      classroom: '计算机楼101',
            //      capacity: 60,
            //      enrolled: 45
            String[] scheduleArr = {
                    "周一 1-2节, 周三 3-4节",
                    "周二 5-6节, 周四 7-8节",
                    "周五 1-2节, 周六 3-4节",
                    "周一 3-4节, 周三 5-6节",
                    "周二 7-8节, 周四 1-2节",
                    "周五 3-4节, 周日 5-6节",
                    "周一 5-6节, 周三 7-8节",
                    "周二 1-2节, 周四 3-4节",
                    "周五 5-6节, 周六 7-8节",
                    "周一 7-8节, 周三 1-2节",
                    "周二 3-4节, 周四 5-6节",
                    "周五 7-8节, 周日 1-2节",
                    "周一 2-3节, 周三 4-5节",
                    "周二 6-7节, 周四 8-9节",
                    "周五 2-3节, 周六 4-5节",
                    "周一 4-5节, 周三 6-7节",
                    "周二 8-9节, 周四 2-3节",
                    "周五 4-5节, 周日 6-7节",
                    "周一 6-7节, 周三 8-9节",
                    "周二 2-3节, 周四 4-5节",
                    "周五 6-7节, 周六 8-9节",
                    "周一 8-9节, 周三 2-3节",
            };
            ArrayList<Course> courseList = new ArrayList<>();
            long i = 0;
            for (Course cours : courses) {
                List<CourseArranging> ca = courseArrangingService.getCourseArrangingsByCourseId(cours.getcId());
                StringBuilder teachers = new StringBuilder();
                for (CourseArranging courseArranging : ca) {
                    Teacher teacher = teacherService.getTeacherById(courseArranging.getTeId());
                    if (teacher != null) {
                        teachers.append(teacher.getTeName()).append(",");
                    }
                }
                cours.id = i++;
                cours.courseCode = cours.getcId();
                cours.courseName = cours.getcName();
                cours.teachername = teachers.toString();
                cours.credits = cours.getcCredit();
                cours.coursetype = "required";
                cours.schedule = scheduleArr[(int)i % scheduleArr.length];
                cours.classroom = "unknown";
                cours.capacity = 60;
                cours.enrolled = 45;
                courseList.add(cours);
            }
            return ResponseEntity.ok(courseList);
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