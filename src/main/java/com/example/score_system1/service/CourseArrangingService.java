package com.example.score_system1.service;

import com.example.score_system1.entity.CourseArranging;
import com.example.score_system1.repository.CourseArrangingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 课程安排业务服务类
 * 提供课程安排相关的业务逻辑处理
 */
@Service
public class CourseArrangingService {

    @Autowired
    private CourseArrangingRepository courseArrangingRepository;

    /**
     * 新增课程安排
     * @param courseArranging 课程安排信息
     * @return 保存后的课程安排信息
     */
    public CourseArranging insertCourseArranging(CourseArranging courseArranging) {
        return courseArrangingRepository.save(courseArranging);
    }

    /**
     * 根据课程安排编号获取课程安排信息
     * @param courseArrangingId 课程安排编号
     * @return 课程安排信息，不存在则返回null
     */
    public CourseArranging getCourseArrangingById(String courseArrangingId) {
        Optional<CourseArranging> courseArranging = courseArrangingRepository.findByCourseArrangingId(courseArrangingId);
        return courseArranging.orElse(null);
    }

    /**
     * 根据课程编号获取课程安排列表
     * @param courseId 课程编号
     * @return 课程安排列表
     */
    public List<CourseArranging> getCourseArrangingsByCourseId(String courseId) {
        return courseArrangingRepository.findByCourseId(courseId);
    }

    /**
     * 根据教师编号获取课程安排列表
     * @param teacherId 教师编号
     * @return 课程安排列表
     */
    public List<CourseArranging> getCourseArrangingsByTeacherId(String teacherId) {
        return courseArrangingRepository.findByTeacherId(teacherId);
    }

    /**
     * 更新课程安排信息
     * @param courseArranging 课程安排信息
     * @return 更新后的课程安排信息
     */
    public CourseArranging updateCourseArranging(CourseArranging courseArranging) {
        return courseArrangingRepository.save(courseArranging);
    }

    /**
     * 根据课程安排编号删除课程安排
     * @param courseArrangingId 课程安排编号
     */
    public void deleteCourseArranging(String courseArrangingId) {
        courseArrangingRepository.deleteById(courseArrangingId);
    }

    /**
     * 获取所有课程安排列表
     * @return 课程安排列表
     */
    public List<CourseArranging> getAllCourseArrangings() {
        return courseArrangingRepository.findAll();
    }

    /**
     * 根据课程编号和教师编号查找课程安排
     * @param courseId 课程编号
     * @param teacherId 教师编号
     * @return 课程安排信息，不存在则返回null
     */
    public CourseArranging getCourseArrangingByCourseIdAndTeacherId(String courseId, String teacherId) {
        Optional<CourseArranging> courseArranging = courseArrangingRepository.findByCourseIdAndTeacherId(courseId, teacherId);
        return courseArranging.orElse(null);
    }

    /**
     * 查询某教师教授的所有课程
     * @param teacherId 教师编号
     * @return 课程安排列表
     */
    public List<CourseArranging> getCoursesByTeacherId(String teacherId) {
        return courseArrangingRepository.findCoursesByTeacherId(teacherId);
    }
}