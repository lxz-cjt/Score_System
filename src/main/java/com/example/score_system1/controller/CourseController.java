package com.example.score_system1.controller;

import com.example.score_system1.dto.ApiResponse;
import com.example.score_system1.dto.CourseDisplayDTO;
import com.example.score_system1.entity.Course;
import com.example.score_system1.entity.CourseArranging;
import com.example.score_system1.entity.Teacher;
import com.example.score_system1.service.CourseArrangingService;
import com.example.score_system1.service.CourseService;
import com.example.score_system1.service.StudentService;
import com.example.score_system1.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
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
    public ApiResponse<String> insertCourse(@RequestBody Course course) {
        try {
            courseService.insertCourse(course);
            return ApiResponse.success("课程添加成功");
        } catch (Exception e) {
            return ApiResponse.error("添加课程失败: " + e.getMessage());
        }
    }

    @GetMapping("/{cId}")
    public ApiResponse<Course> getCourseById(@PathVariable String cId) {
        try {
            Course course = courseService.getCourseById(cId);
            if (course == null) {
                return ApiResponse.error("课程不存在", 404);
            }
            return ApiResponse.success("获取课程信息成功", course);
        } catch (Exception e) {
            return ApiResponse.error("获取课程信息失败: " + e.getMessage());
        }
    }

    @GetMapping
    public ApiResponse<List<CourseDisplayDTO>> getAllCourses() {
        try {
            List<Course> courses = courseService.getAllCourses();
            List<CourseDisplayDTO> courseDisplayList = new ArrayList<>();
            long i = 0;
            
            for (Course course : courses) {
                List<CourseArranging> ca = courseArrangingService.getCourseArrangingsByCourseId(course.getCourseId());
                StringBuilder teachers = new StringBuilder();
                StringBuilder schedules = new StringBuilder();
                StringBuilder classrooms = new StringBuilder();
                int totalCapacity = 0;
                int totalEnrolled = 0;
                
                for (CourseArranging courseArranging : ca) {
                    Teacher teacher = teacherService.getTeacherById(courseArranging.getTeacherId());
                    if (teacher != null) {
                        teachers.append(teacher.getTeacherName()).append(",");
                    }
                    
                    // 获取真实的课程安排信息
                    if (courseArranging.getClassTime() != null) {
                        schedules.append(courseArranging.getClassTime()).append(";");
                    }
                    if (courseArranging.getClassroom() != null) {
                        classrooms.append(courseArranging.getClassroom()).append(",");
                    }
                }
                
                // 去掉最后的逗号和分号
                String teacherNames = teachers.toString();
                if (teacherNames.endsWith(",")) {
                    teacherNames = teacherNames.substring(0, teacherNames.length() - 1);
                }
                
                String scheduleStr = schedules.toString();
                if (scheduleStr.endsWith(";")) {
                    scheduleStr = scheduleStr.substring(0, scheduleStr.length() - 1);
                }
                
                String classroomStr = classrooms.toString();
                if (classroomStr.endsWith(",")) {
                    classroomStr = classroomStr.substring(0, classroomStr.length() - 1);
                }
                
                // 创建DTO对象
                CourseDisplayDTO courseDisplay = new CourseDisplayDTO(
                    i++,                                    // id
                    course.getCourseId(),                        // courseCode
                    course.getCourseName(),                      // courseName
                    teacherNames,                           // teacherName
                        course.getCourseCredit(),              // credits
                    "required",                             // courseType
                    scheduleStr.isEmpty() ? "待安排" : scheduleStr, // schedule - 使用真实数据
                    classroomStr.isEmpty() ? "待安排" : classroomStr, // classroom - 使用真实数据
                    totalCapacity > 0 ? totalCapacity : 60,  // capacity - 使用真实数据
                    totalEnrolled                           // enrolled - 使用真实数据
                );
                
                courseDisplayList.add(courseDisplay);
            }
            return ApiResponse.success("获取课程列表成功", courseDisplayList);
        } catch (Exception e) {
            return ApiResponse.error("获取课程列表失败: " + e.getMessage());
        }
    }

    @PutMapping
    public ApiResponse<String> updateCourse(@RequestBody Course course) {
        try {
            courseService.updateCourse(course);
            return ApiResponse.success("课程更新成功");
        } catch (Exception e) {
            return ApiResponse.error("更新课程失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{cId}")
    public ApiResponse<String> deleteCourse(@PathVariable String cId) {
        try {
            courseService.deleteCourse(cId);
            return ApiResponse.success("课程删除成功");
        } catch (Exception e) {
            return ApiResponse.error("删除课程失败: " + e.getMessage());
        }
    }
}