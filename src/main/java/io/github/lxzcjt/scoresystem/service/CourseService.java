package io.github.lxzcjt.scoresystem.service;

import io.github.lxzcjt.scoresystem.common.PageResult;
import io.github.lxzcjt.scoresystem.common.exception.BusinessException;
import io.github.lxzcjt.scoresystem.dto.request.CourseSaveRequest;
import io.github.lxzcjt.scoresystem.entity.Course;
import io.github.lxzcjt.scoresystem.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 课程管理服务（教务侧）
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    public PageResult<Course> listCourses(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Course> result = StringUtils.hasText(keyword)
                ? courseRepository.findByCourseNameContaining(keyword, pageable)
                : courseRepository.findAll(pageable);
        return PageResult.of(result);
    }

    public Course getCourse(String courseId) {
        return courseRepository.findByCourseId(courseId)
                .orElseThrow(() -> BusinessException.notFound("课程不存在: " + courseId));
    }

    @Transactional
    public Course createCourse(CourseSaveRequest request) {
        if (courseRepository.existsByCourseId(request.courseId())) {
            throw BusinessException.conflict("课程编号已存在: " + request.courseId());
        }
        Course course = Course.builder()
                .courseId(request.courseId())
                .courseName(request.courseName())
                .courseCredit(request.courseCredit())
                .build();
        log.info("新增课程: {}", course.getCourseId());
        return courseRepository.save(course);
    }

    @Transactional
    public Course updateCourse(String courseId, CourseSaveRequest request) {
        Course course = getCourse(courseId);
        course.setCourseName(request.courseName());
        course.setCourseCredit(request.courseCredit());
        log.info("更新课程: {}", courseId);
        return courseRepository.save(course);
    }

    @Transactional
    public void deleteCourse(String courseId) {
        Course course = getCourse(courseId);
        log.info("删除课程: {}", courseId);
        courseRepository.delete(course);
    }
}
