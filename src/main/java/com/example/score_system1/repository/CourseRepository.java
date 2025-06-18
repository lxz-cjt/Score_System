package com.example.score_system1.repository;

import com.example.score_system1.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 课程数据访问层接口
 * 提供课程相关的数据库操作方法
 */
@Repository
public interface CourseRepository extends JpaRepository<Course, String> {
    
    /**
     * 根据课程编号查找课程
     * @param courseId 课程编号
     * @return 课程信息
     */
    Optional<Course> findByCourseId(String courseId);
    
    /**
     * 检查课程是否存在
     * @param courseId 课程编号
     * @return 是否存在
     */
    boolean existsByCourseId(String courseId);
    
    /**
     * 根据课程名称模糊查询
     * @param courseName 课程名称
     * @return 课程列表
     */
    List<Course> findByCourseNameContaining(String courseName);
    
    /**
     * 根据学分查询课程
     * @param courseCredit 学分
     * @return 课程列表
     */
    List<Course> findByCourseCredit(short courseCredit);
    
    /**
     * 根据学分范围查询课程
     * @param minCredit 最小学分
     * @param maxCredit 最大学分
     * @return 课程列表
     */
    @Query("SELECT c FROM Course c WHERE c.courseCredit BETWEEN :minCredit AND :maxCredit")
    List<Course> findByCourseCreditBetween(@Param("minCredit") short minCredit, @Param("maxCredit") short maxCredit);
} 