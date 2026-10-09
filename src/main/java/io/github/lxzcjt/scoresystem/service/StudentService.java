package io.github.lxzcjt.scoresystem.service;

import io.github.lxzcjt.scoresystem.common.PageResult;
import io.github.lxzcjt.scoresystem.common.exception.BusinessException;
import io.github.lxzcjt.scoresystem.dto.request.StudentSaveRequest;
import io.github.lxzcjt.scoresystem.entity.Student;
import io.github.lxzcjt.scoresystem.repository.StudentRepository;
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
 * 学生管理服务（教务侧）
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 分页查询学生，支持姓名/专业关键字
     */
    public PageResult<Student> listStudents(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Student> result = StringUtils.hasText(keyword)
                ? studentRepository.findByStudentNameContainingOrMajorContaining(keyword, keyword, pageable)
                : studentRepository.findAll(pageable);
        return PageResult.of(result);
    }

    /**
     * 按学号查询
     */
    public Student getStudent(String studentId) {
        return studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> BusinessException.notFound("学生不存在: " + studentId));
    }

    /**
     * 新增学生（密码 BCrypt 加密存储）
     */
    @Transactional
    public Student createStudent(StudentSaveRequest request) {
        if (studentRepository.existsByStudentId(request.studentId())) {
            throw BusinessException.conflict("学号已存在: " + request.studentId());
        }
        if (!StringUtils.hasText(request.password())) {
            throw BusinessException.badRequest("新增学生时初始密码不能为空");
        }
        Student student = Student.builder()
                .studentId(request.studentId())
                .studentName(request.studentName())
                .major(request.major())
                .getCredit(request.getCredit())
                .password(passwordEncoder.encode(request.password()))
                .build();
        log.info("新增学生: {}", student.getStudentId());
        return studentRepository.save(student);
    }

    /**
     * 更新学生（密码留空则不修改）
     */
    @Transactional
    public Student updateStudent(String studentId, StudentSaveRequest request) {
        Student student = getStudent(studentId);
        student.setStudentName(request.studentName());
        student.setMajor(request.major());
        student.setGetCredit(request.getCredit());
        if (StringUtils.hasText(request.password())) {
            student.setPassword(passwordEncoder.encode(request.password()));
        }
        log.info("更新学生: {}", studentId);
        return studentRepository.save(student);
    }

    /**
     * 删除学生（存在成绩等关联数据时将因外键约束失败，由全局异常处理返回 409）
     */
    @Transactional
    public void deleteStudent(String studentId) {
        Student student = getStudent(studentId);
        log.info("删除学生: {}", studentId);
        studentRepository.delete(student);
    }
}
