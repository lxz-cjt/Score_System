package com.example.score_system1.service;

import com.example.score_system1.entity.Course;
import com.example.score_system1.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 课程业务服务类
 * 提供课程相关的业务逻辑处理
 */
@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    /**
     * 新增课程
     * @param course 课程信息
     * @return 保存后的课程信息
     */
    public Course insertCourse(Course course) {
        return courseRepository.save(course);
    }

    /**
     * 根据课程编号获取课程信息
     * @param cId 课程编号
     * @return 课程信息，不存在则返回null
     */
    public Course getCourseById(String cId) {
        Optional<Course> course = courseRepository.findByCourseId(cId);
        return course.orElse(null);
    }

    /**
     * 获取所有课程列表
     * @return 课程列表
     */
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    /**
     * 更新课程信息
     * @param course 课程信息
     * @return 更新后的课程信息
     */
    public Course updateCourse(Course course) {
        return courseRepository.save(course);
    }

    /**
     * 根据课程编号删除课程
     * @param cId 课程编号
     */
    public void deleteCourse(String cId) {
        courseRepository.deleteById(cId);
    }

    /**
     * 根据课程名称模糊查询课程
     * @param courseName 课程名称
     * @return 课程列表
     */
    public List<Course> getCoursesByNameContaining(String courseName) {
        return courseRepository.findByCourseNameContaining(courseName);
    }

    /**
     * 根据学分查询课程
     * @param courseCredit 学分
     * @return 课程列表
     */
    public List<Course> getCoursesByCredit(short courseCredit) {
        return courseRepository.findByCourseCredit(courseCredit);
    }

    /**
     * 根据学分范围查询课程
     * @param minCredit 最小学分
     * @param maxCredit 最大学分
     * @return 课程列表
     */
    public List<Course> getCoursesByCreditRange(short minCredit, short maxCredit) {
        return courseRepository.findByCourseCreditBetween(minCredit, maxCredit);
    }
}