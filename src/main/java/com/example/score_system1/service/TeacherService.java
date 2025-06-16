package com.example.score_system1.service;


import com.example.score_system1.entity.Teacher;
import com.example.score_system1.mapper.TeacherMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {

    @Autowired
    private TeacherMapper teacherMapper;

    public void insertTeacher(Teacher teacher) {
        teacherMapper.insertTeacher(teacher);
    }

    public Teacher getTeacherById(String teId) {
        return teacherMapper.selectTeacherById(teId);
    }

    public List<Teacher> getAllTeachers() {
        return teacherMapper.selectAllTeachers();
    }

    public void updateTeacher(Teacher teacher) {
        teacherMapper.updateTeacher(teacher);
    }

    public void deleteTeacher(String teId) {
        teacherMapper.deleteTeacher(teId);
    }
}