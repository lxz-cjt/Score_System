package com.example.score_system1.service;

import com.example.score_system1.entity.Teacher;
import com.example.score_system1.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 教师业务服务类
 * 提供教师相关的业务逻辑处理
 */
@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    /**
     * 新增教师
     * @param teacher 教师信息
     * @return 保存后的教师信息
     */
    public Teacher insertTeacher(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    /**
     * 根据教师编号获取教师信息
     * @param teId 教师编号
     * @return 教师信息，不存在则返回null
     */
    public Teacher getTeacherById(String teId) {
        Optional<Teacher> teacher = teacherRepository.findByTeacherId(teId);
        return teacher.orElse(null);
    }

    /**
     * 获取所有教师列表
     * @return 教师列表
     */
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    /**
     * 更新教师信息
     * @param teacher 教师信息
     * @return 更新后的教师信息
     */
    public Teacher updateTeacher(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    /**
     * 根据教师编号删除教师
     * @param teId 教师编号
     */
    public void deleteTeacher(String teId) {
        teacherRepository.deleteById(teId);
    }

    /**
     * 根据专业查询教师列表
     * @param teMajor 专业
     * @return 教师列表
     */
    public List<Teacher> getTeachersByMajor(String teMajor) {
        return teacherRepository.findByCollege(teMajor);
    }

    /**
     * 根据姓名模糊查询教师
     * @param teName 教师姓名
     * @return 教师列表
     */
    public List<Teacher> getTeachersByNameContaining(String teName) {
        return teacherRepository.findByTeacherNameContaining(teName);
    }

    /**
     * 验证教师登录
     * @param teId 教师编号
     * @param password 密码
     * @return 教师信息，验证失败返回null
     */
    public Teacher validateTeacher(String teId, String password) {
        Optional<Teacher> teacher = teacherRepository.findByTeacherIdAndPassword(teId, password);
        return teacher.orElse(null);
    }
}