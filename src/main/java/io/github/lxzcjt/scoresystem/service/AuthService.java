package io.github.lxzcjt.scoresystem.service;

import io.github.lxzcjt.scoresystem.common.exception.BusinessException;
import io.github.lxzcjt.scoresystem.dto.request.LoginRequest;
import io.github.lxzcjt.scoresystem.dto.response.LoginVO;
import io.github.lxzcjt.scoresystem.dto.response.UserInfoVO;
import io.github.lxzcjt.scoresystem.entity.AcademicAffairsStaff;
import io.github.lxzcjt.scoresystem.entity.Student;
import io.github.lxzcjt.scoresystem.entity.Teacher;
import io.github.lxzcjt.scoresystem.enums.UserRole;
import io.github.lxzcjt.scoresystem.repository.AcademicAffairsStaffRepository;
import io.github.lxzcjt.scoresystem.repository.StudentRepository;
import io.github.lxzcjt.scoresystem.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * 认证服务：三类用户统一登录，密码使用 BCrypt 校验
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final AcademicAffairsStaffRepository staffRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 统一登录：依次尝试学生、教师、教务人员身份
     *
     * @throws BusinessException 用户名或密码错误时抛出 401
     */
    public LoginVO login(LoginRequest request) {
        String username = request.username();
        String rawPassword = request.password();

        Optional<Student> student = studentRepository.findByStudentId(username);
        if (student.isPresent() && passwordEncoder.matches(rawPassword, student.get().getPassword())) {
            log.info("学生登录成功: {}", username);
            return buildLoginVO(student.get().getStudentId(), student.get().getStudentName(), UserRole.STUDENT);
        }

        Optional<Teacher> teacher = teacherRepository.findByTeacherId(username);
        if (teacher.isPresent() && passwordEncoder.matches(rawPassword, teacher.get().getPassword())) {
            log.info("教师登录成功: {}", username);
            return buildLoginVO(teacher.get().getTeacherId(), teacher.get().getTeacherName(), UserRole.TEACHER);
        }

        Optional<AcademicAffairsStaff> staff = staffRepository.findByStaffId(username);
        if (staff.isPresent() && passwordEncoder.matches(rawPassword, staff.get().getPassword())) {
            log.info("教务人员登录成功: {}", username);
            return buildLoginVO(staff.get().getStaffId(), staff.get().getStaffName(), UserRole.STAFF);
        }

        log.warn("登录失败: username={}", username);
        throw BusinessException.unauthorized("用户名或密码错误");
    }

    private LoginVO buildLoginVO(String id, String name, UserRole role) {
        // 演示环境的简易令牌（生产环境建议替换为 JWT，见 README Roadmap）
        String token = UUID.randomUUID().toString().replace("-", "");
        UserInfoVO user = new UserInfoVO(id, name, role.getCode(), role.getLabel());
        return new LoginVO(token, user);
    }
}
