package com.example.score_system1.mapper;


import com.example.score_system1.entity.Course;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CourseMapper {
    void insertCourse(Course course);
    Course selectCourseById(String cId);
    List<Course> selectAllCourses();
    void updateCourse(Course course);
    void deleteCourse(String cId);
}