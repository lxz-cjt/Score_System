package com.example.score_system1.service;

import com.example.score_system1.entity.*;
import com.example.score_system1.mapper.AcademicAffairsStaffMapper;
import com.example.score_system1.mapper.StudentMapper;
import com.example.score_system1.mapper.TeacherMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private AcademicAffairsStaffMapper academicAffairsStaffMapper;

    public LoginResponse login(LoginRequest loginRequest) {
        LoginResponse response = new LoginResponse();
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        if (username == null || password == null) {
            response.setSuccess(false);
            response.setMessage("用户名和密码不能为空");
            return response;
        }

        // 尝试以学生身份登录
        Student student = studentMapper.selectStudentByUsername(username);
        if (student != null && student.getStId() != null) {
            if (student.getPassword().equals(password)) {
                response.setSuccess(true);
                response.setMessage("登录成功");
                response.setToken("student_token_" + student.getStId()); // 示例令牌
                response.setUser(new UserSimpleInfo("student", student.getStName(), "学生"));
                return response;
            } else {
                response.setSuccess(false);
                response.setMessage("密码错误");
                return response;
            }
        }

        // 尝试以教师身份登录
        Teacher teacher = teacherMapper.selectTeacherByUsername(username);
        if (teacher != null && teacher.getTeId() != null) {
            if (teacher.getPassword().equals(password)) {
                response.setSuccess(true);
                response.setMessage("登录成功");
                response.setToken("teacher_token_" + teacher.getTeId()); // 示例令牌
                response.setUser(new UserSimpleInfo("teacher", teacher.getTeName(), "教师"));
                return response;
            } else {
                response.setSuccess(false);
                response.setMessage("密码错误");
                return response;
            }
        }

        // 尝试以教务人员身份登录
        AcademicAffairsStaff staff = academicAffairsStaffMapper.selectAcademicAffairsStaffByUsername(username);
        if (staff != null && staff.getAasId() != null) {
            if (staff.getPassword().equals(password)) {
                response.setSuccess(true);
                response.setMessage("登录成功");
                response.setToken("staff_token_" + staff.getAasId()); // 示例令牌
                response.setUser(new UserSimpleInfo("staff", staff.getAasName(), "管理员"));
                return response;
            } else {
                response.setSuccess(false);
                response.setMessage("密码错误");
                return response;
            }
        }

        // 未找到用户
        response.setSuccess(false);
        response.setMessage("用户不存在");
        return response;
    }
}