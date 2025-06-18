package com.example.score_system1.repository;

import com.example.score_system1.entity.ScoreLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 成绩日志数据访问层接口
 * 提供成绩日志相关的数据库操作方法
 */
@Repository
public interface ScoreLogRepository extends JpaRepository<ScoreLog, Integer> {
    
    /**
     * 根据成绩编号查找日志列表
     * @param scoreId 成绩编号
     * @return 日志列表
     */
    List<ScoreLog> findByScoreId(String scoreId);
    
    /**
     * 根据学生编号查找日志列表
     * @param stId 学生编号
     * @return 日志列表
     */
    List<ScoreLog> findByStudentId(String stId);
    
    /**
     * 根据课程编号查找日志列表
     * @param courseId 课程编号
     * @return 日志列表
     */
    List<ScoreLog> findByCourseId(String courseId);
    
    /**
     * 根据操作类型查找日志列表
     * @param operationType 操作类型
     * @return 日志列表
     */
    List<ScoreLog> findByOperationType(String operationType);
    
    /**
     * 根据操作人查找日志列表
     * @param operatorId 操作人ID
     * @return 日志列表
     */
    List<ScoreLog> findByOperatorId(String operatorId);
    
    /**
     * 根据操作时间范围查询日志
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 日志列表
     */
    @Query("SELECT sl FROM ScoreLog sl WHERE sl.operationTime BETWEEN :startDate AND :endDate ORDER BY sl.operationTime DESC")
    List<ScoreLog> findByOperationTimeBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    
    /**
     * 根据学生编号和课程编号查找日志列表
     * @param stId 学生编号
     * @param cId 课程编号
     * @return 日志列表
     */
    @Query("SELECT sl FROM ScoreLog sl WHERE sl.studentId = :stId AND sl.courseId = :cId ORDER BY sl.operationTime DESC")
    List<ScoreLog> findByStIdAndCId(@Param("stId") String stId, @Param("cId") String cId);
} 