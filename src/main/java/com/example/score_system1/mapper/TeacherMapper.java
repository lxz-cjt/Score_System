package com.example.score_system1.mapper;

import com.example.score_system1.entity.Teacher;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TeacherMapper {
    void insertTeacher(Teacher teacher);
    Teacher selectTeacherById(String teId);
    List<Teacher> selectAllTeachers();
    void updateTeacher(Teacher teacher);
    void deleteTeacher(String teId);
    Teacher selectTeacherByUsername(String username); // 新增方法
}