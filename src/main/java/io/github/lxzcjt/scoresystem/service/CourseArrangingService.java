package io.github.lxzcjt.scoresystem.service;

import io.github.lxzcjt.scoresystem.common.PageResult;
import io.github.lxzcjt.scoresystem.common.exception.BusinessException;
import io.github.lxzcjt.scoresystem.dto.request.CourseArrangingSaveRequest;
import io.github.lxzcjt.scoresystem.dto.response.CourseArrangingVO;
import io.github.lxzcjt.scoresystem.entity.Course;
import io.github.lxzcjt.scoresystem.entity.CourseArranging;
import io.github.lxzcjt.scoresystem.entity.Teacher;
import io.github.lxzcjt.scoresystem.repository.CourseArrangingRepository;
import io.github.lxzcjt.scoresystem.repository.CourseRepository;
import io.github.lxzcjt.scoresystem.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 课程安排服务（教务排课、教师查课）
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CourseArrangingService {

    private final CourseArrangingRepository arrangingRepository;
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;

    /**
     * 分页查询课程安排，支持按课程/教师过滤
     */
    public PageResult<CourseArrangingVO> listArrangements(String courseId, String teacherId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<CourseArranging> result;
        if (StringUtils.hasText(courseId)) {
            result = arrangingRepository.findByCourseId(courseId, pageable);
        } else if (StringUtils.hasText(teacherId)) {
            result = arrangingRepository.findByTeacherId(teacherId, pageable);
        } else {
            result = arrangingRepository.findAll(pageable);
        }
        return PageResult.of(result, enrichBatch(result.getContent()));
    }

    /**
     * 查询教师的所有课程安排（教师端"我的课程"）
     */
    public List<CourseArrangingVO> listByTeacher(String teacherId) {
        if (!teacherRepository.existsByTeacherId(teacherId)) {
            throw BusinessException.notFound("教师不存在: " + teacherId);
        }
        return enrichBatch(arrangingRepository.findByTeacherId(teacherId));
    }

    @Transactional
    public CourseArrangingVO createArrangement(CourseArrangingSaveRequest request) {
        validateCourseAndTeacher(request.courseId(), request.teacherId());
        if (arrangingRepository.existsByCourseIdAndTeacherId(request.courseId(), request.teacherId())) {
            throw BusinessException.conflict("该课程已由此教师授课，请勿重复安排");
        }
        CourseArranging arranging = CourseArranging.builder()
                .arrangingId(generateId())
                .courseId(request.courseId())
                .teacherId(request.teacherId())
                .classTime(request.classTime())
                .classroom(request.classroom())
                .build();
        log.info("新增课程安排: course={}, teacher={}", request.courseId(), request.teacherId());
        return toVO(arrangingRepository.save(arranging));
    }

    @Transactional
    public CourseArrangingVO updateArrangement(String arrangingId, CourseArrangingSaveRequest request) {
        CourseArranging arranging = getArranging(arrangingId);
        validateCourseAndTeacher(request.courseId(), request.teacherId());
        arranging.setCourseId(request.courseId());
        arranging.setTeacherId(request.teacherId());
        arranging.setClassTime(request.classTime());
        arranging.setClassroom(request.classroom());
        log.info("更新课程安排: {}", arrangingId);
        return toVO(arrangingRepository.save(arranging));
    }

    @Transactional
    public void deleteArrangement(String arrangingId) {
        CourseArranging arranging = getArranging(arrangingId);
        log.info("删除课程安排: {}", arrangingId);
        arrangingRepository.delete(arranging);
    }

    public CourseArranging getArranging(String arrangingId) {
        return arrangingRepository.findByArrangingId(arrangingId)
                .orElseThrow(() -> BusinessException.notFound("课程安排不存在: " + arrangingId));
    }

    private void validateCourseAndTeacher(String courseId, String teacherId) {
        if (!courseRepository.existsByCourseId(courseId)) {
            throw BusinessException.notFound("课程不存在: " + courseId);
        }
        if (!teacherRepository.existsByTeacherId(teacherId)) {
            throw BusinessException.notFound("教师不存在: " + teacherId);
        }
    }

    /**
     * 组装单个 VO
     */
    private CourseArrangingVO toVO(CourseArranging arranging) {
        return enrichBatch(List.of(arranging)).get(0);
    }

    /**
     * 批量组装 VO：一次性查出关联的课程/教师，避免 N+1 查询
     */
    private List<CourseArrangingVO> enrichBatch(List<CourseArranging> arrangings) {
        Set<String> courseIds = arrangings.stream().map(CourseArranging::getCourseId).collect(Collectors.toSet());
        Set<String> teacherIds = arrangings.stream().map(CourseArranging::getTeacherId).collect(Collectors.toSet());
        Map<String, Course> courseMap = courseRepository.findAllById(courseIds).stream()
                .collect(Collectors.toMap(Course::getCourseId, Function.identity()));
        Map<String, Teacher> teacherMap = teacherRepository.findAllById(teacherIds).stream()
                .collect(Collectors.toMap(Teacher::getTeacherId, Function.identity()));

        return arrangings.stream().map(arranging -> {
            Course course = courseMap.get(arranging.getCourseId());
            Teacher teacher = teacherMap.get(arranging.getTeacherId());
            return new CourseArrangingVO(
                    arranging.getArrangingId(),
                    arranging.getCourseId(),
                    course != null ? course.getCourseName() : null,
                    course != null ? course.getCourseCredit() : null,
                    arranging.getTeacherId(),
                    teacher != null ? teacher.getTeacherName() : null,
                    arranging.getClassTime(),
                    arranging.getClassroom());
        }).toList();
    }

    private String generateId() {
        return "CA" + UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
    }
}
