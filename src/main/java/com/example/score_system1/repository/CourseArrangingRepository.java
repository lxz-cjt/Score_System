package com.example.score_system1.repository;

import com.example.score_system1.entity.CourseArranging;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 课程安排数据访问层接口
 * 提供课程安排相关的数据库操作方法
 */
@Repository
public interface CourseArrangingRepository extends JpaRepository<CourseArranging, String> {
    
    /**
     * 根据课程安排编号查找课程安排
     * @param courseArrangingId 课程安排编号
     * @return 课程安排信息
     */
    Optional<CourseArranging> findByCourseArrangingId(String courseArrangingId);
    
    /**
     * 根据课程编号查找课程安排列表
     * @param courseId 课程编号
     * @return 课程安排列表
     */
    List<CourseArranging> findByCourseId(String courseId);
    
    /**
     * 根据教师编号查找课程安排列表
     * @param teacherId 教师编号
     * @return 课程安排列表
     */
    List<CourseArranging> findByTeacherId(String teacherId);
    
    /**
     * 根据课程编号和教师编号查找课程安排
     * @param courseId 课程编号
     * @param teacherId 教师编号
     * @return 课程安排信息
     */
    Optional<CourseArranging> findByCourseIdAndTeacherId(String courseId, String teacherId);
    
    /**
     * 查询某教师教授的所有课程
     * @param teacherId 教师编号
     * @return 课程安排列表
     */
    @Query("SELECT ca FROM CourseArranging ca WHERE ca.teacherId = :teacherId")
    List<CourseArranging> findCoursesByTeacherId(@Param("teacherId") String teacherId);
} 