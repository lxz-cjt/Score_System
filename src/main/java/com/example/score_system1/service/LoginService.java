package com.example.score_system1.service;

import com.example.score_system1.dto.LoginRequest;
import com.example.score_system1.dto.LoginResponse;
import com.example.score_system1.dto.UserSimpleInfo;
import com.example.score_system1.entity.AcademicAffairsStaff;
import com.example.score_system1.entity.Student;
import com.example.score_system1.entity.Teacher;
import com.example.score_system1.repository.AcademicAffairsStaffRepository;
import com.example.score_system1.repository.StudentRepository;
import com.example.score_system1.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 登录业务服务类
 * 提供用户登录验证的业务逻辑处理
 */
@Service
public class LoginService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private AcademicAffairsStaffRepository academicAffairsStaffRepository;

    /**
     * 用户登录验证
     * @param loginRequest 登录请求信息
     * @return 登录响应结果
     */
    public LoginResponse login(LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        if (username == null || password == null) {
            return LoginResponse.failure("用户名和密码不能为空");
        }

        // 尝试以学生身份登录
        Optional<Student> studentOpt = studentRepository.findByStudentIdAndPassword(username, password);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            String token = "student_token_" + student.getStudentId();
            UserSimpleInfo userInfo = new UserSimpleInfo("student", student.getStudentName(), "学生");
            userInfo.setId(student.getStudentId());
            return LoginResponse.success(token, userInfo);
        }

        // 尝试以教师身份登录
        Optional<Teacher> teacherOpt = teacherRepository.findByTeacherIdAndPassword(username, password);
        if (teacherOpt.isPresent()) {
            Teacher teacher = teacherOpt.get();
            String token = "teacher_token_" + teacher.getTeacherId();
            UserSimpleInfo userInfo = new UserSimpleInfo("teacher", teacher.getTeacherName(), "教师");
            userInfo.setId(teacher.getTeacherId());
            return LoginResponse.success(token, userInfo);
        }

        // 尝试以教务人员身份登录
        Optional<AcademicAffairsStaff> staffOpt = academicAffairsStaffRepository.findByAcademicAffairsStaffIdAndPassword(username, password);
        if (staffOpt.isPresent()) {
            AcademicAffairsStaff staff = staffOpt.get();
            String token = "staff_token_" + staff.getAcademicAffairsStaffId();
            UserSimpleInfo userInfo = new UserSimpleInfo("staff", staff.getAcademicAffairsStaffName(), "管理员");
            userInfo.setId(staff.getAcademicAffairsStaffId());
            return LoginResponse.success(token, userInfo);
        }

        // 未找到用户或密码错误
        return LoginResponse.failure("用户名或密码错误");
    }

    /**
     * 根据用户名和用户类型查找用户
     * @param username 用户名
     * @param userType 用户类型 (student/teacher/staff)
     * @return 用户信息，不存在则返回null
     */
    public Object findUserByUsernameAndType(String username, String userType) {
        switch (userType.toLowerCase()) {
            case "student":
                Optional<Student> student = studentRepository.findByStudentId(username);
                return student.orElse(null);
            case "teacher":
                Optional<Teacher> teacher = teacherRepository.findByTeacherId(username);
                return teacher.orElse(null);
            case "staff":
                Optional<AcademicAffairsStaff> staff = academicAffairsStaffRepository.findByAcademicAffairsStaffId(username);
                return staff.orElse(null);
            default:
                return null;
        }
    }
}