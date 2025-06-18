package com.example.score_system1.repository;

import com.example.score_system1.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 教师数据访问层接口
 * 提供教师相关的数据库操作方法
 */
@Repository
public interface TeacherRepository extends JpaRepository<Teacher, String> {
    
    /**
     * 根据教师编号查找教师
     * @param teacherId 教师编号
     * @return 教师信息
     */
    Optional<Teacher> findByTeacherId(String teacherId);
    
    /**
     * 根据学院查找教师列表
     * @param college 学院
     * @return 教师列表
     */
    List<Teacher> findByCollege(String college);
    
    /**
     * 根据姓名模糊查询教师
     * @param teacherName 教师姓名
     * @return 教师列表
     */
    List<Teacher> findByTeacherNameContaining(String teacherName);
    
    /**
     * 验证教师登录
     * @param teacherId 教师编号
     * @param password 密码
     * @return 教师信息
     */
    @Query("SELECT t FROM Teacher t WHERE t.teacherId = :teacherId AND t.password = :password")
    Optional<Teacher> findByTeacherIdAndPassword(@Param("teacherId") String teacherId, @Param("password") String password);

    /**
     * 检查教师是否存在
     * @param teacherId 教师编号
     * @return 是否存在
     */
    boolean existsByTeacherId(String teacherId);
} 