package io.github.lxzcjt.scoresystem.service;

import io.github.lxzcjt.scoresystem.common.exception.BusinessException;
import io.github.lxzcjt.scoresystem.dto.request.LoginRequest;
import io.github.lxzcjt.scoresystem.dto.response.LoginVO;
import io.github.lxzcjt.scoresystem.entity.AcademicAffairsStaff;
import io.github.lxzcjt.scoresystem.entity.Student;
import io.github.lxzcjt.scoresystem.entity.Teacher;
import io.github.lxzcjt.scoresystem.repository.AcademicAffairsStaffRepository;
import io.github.lxzcjt.scoresystem.repository.StudentRepository;
import io.github.lxzcjt.scoresystem.repository.TeacherRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

/**
 * AuthService 单元测试
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private StudentRepository studentRepository;
    @Mock
    private TeacherRepository teacherRepository;
    @Mock
    private AcademicAffairsStaffRepository staffRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("学生登录成功：返回令牌与学生角色")
    void studentLoginSuccess() {
        when(studentRepository.findByStudentId("2021001")).thenReturn(Optional.of(
                Student.builder().studentId("2021001").studentName("张三").password("hashed").build()));
        when(passwordEncoder.matches("123456", "hashed")).thenReturn(true);

        LoginVO result = authService.login(new LoginRequest("2021001", "123456"));

        assertNotNull(result.token());
        assertEquals("student", result.user().role());
        assertEquals("张三", result.user().name());
    }

    @Test
    @DisplayName("教师登录成功")
    void teacherLoginSuccess() {
        lenient().when(studentRepository.findByStudentId("T001")).thenReturn(Optional.empty());
        when(teacherRepository.findByTeacherId("T001")).thenReturn(Optional.of(
                Teacher.builder().teacherId("T001").teacherName("李教授").password("hashed").build()));
        when(passwordEncoder.matches("teacher123", "hashed")).thenReturn(true);

        LoginVO result = authService.login(new LoginRequest("T001", "teacher123"));

        assertEquals("teacher", result.user().role());
        assertEquals("李教授", result.user().name());
    }

    @Test
    @DisplayName("教务人员登录成功")
    void staffLoginSuccess() {
        lenient().when(studentRepository.findByStudentId("AAS001")).thenReturn(Optional.empty());
        lenient().when(teacherRepository.findByTeacherId("AAS001")).thenReturn(Optional.empty());
        when(staffRepository.findByStaffId("AAS001")).thenReturn(Optional.of(
                AcademicAffairsStaff.builder().staffId("AAS001").staffName("管理员一").password("hashed").build()));
        when(passwordEncoder.matches("admin123", "hashed")).thenReturn(true);

        LoginVO result = authService.login(new LoginRequest("AAS001", "admin123"));

        assertEquals("staff", result.user().role());
    }

    @Test
    @DisplayName("密码错误 -> 401")
    void wrongPassword() {
        lenient().when(studentRepository.findByStudentId("2021001")).thenReturn(Optional.of(
                Student.builder().studentId("2021001").studentName("张三").password("hashed").build()));
        lenient().when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
        lenient().when(teacherRepository.findByTeacherId("2021001")).thenReturn(Optional.empty());
        lenient().when(staffRepository.findByStaffId("2021001")).thenReturn(Optional.empty());

        BusinessException e = assertThrows(BusinessException.class,
                () -> authService.login(new LoginRequest("2021001", "wrong")));
        assertEquals(401, e.getErrorCode().getCode());
    }

    @Test
    @DisplayName("用户不存在 -> 401")
    void userNotFound() {
        lenient().when(studentRepository.findByStudentId("nobody")).thenReturn(Optional.empty());
        lenient().when(teacherRepository.findByTeacherId("nobody")).thenReturn(Optional.empty());
        lenient().when(staffRepository.findByStaffId("nobody")).thenReturn(Optional.empty());

        BusinessException e = assertThrows(BusinessException.class,
                () -> authService.login(new LoginRequest("nobody", "123456")));
        assertEquals(401, e.getErrorCode().getCode());
    }
}
