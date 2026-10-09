package io.github.lxzcjt.scoresystem.service;

import io.github.lxzcjt.scoresystem.common.PageResult;
import io.github.lxzcjt.scoresystem.common.exception.BusinessException;
import io.github.lxzcjt.scoresystem.dto.request.TeacherSaveRequest;
import io.github.lxzcjt.scoresystem.entity.Teacher;
import io.github.lxzcjt.scoresystem.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 教师管理服务（教务侧）
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;

    public PageResult<Teacher> listTeachers(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Teacher> result = StringUtils.hasText(keyword)
                ? teacherRepository.findByTeacherNameContainingOrCollegeContaining(keyword, keyword, pageable)
                : teacherRepository.findAll(pageable);
        return PageResult.of(result);
    }

    public Teacher getTeacher(String teacherId) {
        return teacherRepository.findByTeacherId(teacherId)
                .orElseThrow(() -> BusinessException.notFound("教师不存在: " + teacherId));
    }

    @Transactional
    public Teacher createTeacher(TeacherSaveRequest request) {
        if (teacherRepository.existsByTeacherId(request.teacherId())) {
            throw BusinessException.conflict("教师工号已存在: " + request.teacherId());
        }
        if (!StringUtils.hasText(request.password())) {
            throw BusinessException.badRequest("新增教师时初始密码不能为空");
        }
        Teacher teacher = Teacher.builder()
                .teacherId(request.teacherId())
                .teacherName(request.teacherName())
                .college(request.college())
                .password(passwordEncoder.encode(request.password()))
                .build();
        log.info("新增教师: {}", teacher.getTeacherId());
        return teacherRepository.save(teacher);
    }

    @Transactional
    public Teacher updateTeacher(String teacherId, TeacherSaveRequest request) {
        Teacher teacher = getTeacher(teacherId);
        teacher.setTeacherName(request.teacherName());
        teacher.setCollege(request.college());
        if (StringUtils.hasText(request.password())) {
            teacher.setPassword(passwordEncoder.encode(request.password()));
        }
        log.info("更新教师: {}", teacherId);
        return teacherRepository.save(teacher);
    }

    @Transactional
    public void deleteTeacher(String teacherId) {
        Teacher teacher = getTeacher(teacherId);
        log.info("删除教师: {}", teacherId);
        teacherRepository.delete(teacher);
    }
}
