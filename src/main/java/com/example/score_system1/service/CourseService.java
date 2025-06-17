package com.example.score_system1.service;


import com.example.score_system1.entity.Course;
import com.example.score_system1.mapper.CourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseMapper courseMapper;

    public void insertCourse(Course course) {
        courseMapper.insertCourse(course);
    }

    public Course getCourseById(String cId) {
        return courseMapper.selectCourseById(cId);
    }

    public List<Course> getAllCourses() {
        return courseMapper.selectAllCourses();
    }

    public void updateCourse(Course course) {
        courseMapper.updateCourse(course);
    }

    public void deleteCourse(String cId) {
        courseMapper.deleteCourse(cId);
    }
}