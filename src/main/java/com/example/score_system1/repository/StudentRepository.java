package com.example.score_system1.repository;

import com.example.score_system1.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 学生数据访问层接口
 * 提供学生相关的数据库操作方法
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
    
    /**
     * 根据学号查找学生
     * @param studentId 学号
     * @return 学生信息
     */
    Optional<Student> findByStudentId(String studentId);
    
    /**
     * 检查学生是否存在
     * @param studentId 学号
     * @return 是否存在
     */
    boolean existsByStudentId(String studentId);
    
    /**
     * 根据专业查找学生列表
     * @param major 专业
     * @return 学生列表
     */
    List<Student> findByMajor(String major);
    
    /**
     * 根据姓名模糊查询学生
     * @param studentName 学生姓名
     * @return 学生列表
     */
    List<Student> findByStudentNameContaining(String studentName);
    
    /**
     * 验证学生登录
     * @param studentId 学号
     * @param password 密码
     * @return 学生信息
     */
    @Query("SELECT s FROM Student s WHERE s.studentId = :studentId AND s.password = :password")
    Optional<Student> findByStudentIdAndPassword(@Param("studentId") String studentId, @Param("password") String password);
    
    /**
     * 根据获得学分范围查询学生
     * @param minCredit 最小学分
     * @param maxCredit 最大学分
     * @return 学生列表
     */
    @Query("SELECT s FROM Student s WHERE s.getCredit BETWEEN :minCredit AND :maxCredit")
    List<Student> findByGetCreditBetween(@Param("minCredit") int minCredit, @Param("maxCredit") int maxCredit);
} 