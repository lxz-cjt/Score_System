package com.example.score_system1.repository;

import com.example.score_system1.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 成绩数据访问层接口
 * 提供成绩相关的数据库操作方法
 */
@Repository
public interface ScoreRepository extends JpaRepository<Score, String> {
    
    /**
     * 根据成绩编号查找成绩
     * @param scoreId 成绩编号
     * @return 成绩信息
     */
    Optional<Score> findByScoreId(String scoreId);
    
    /**
     * 根据学生编号查找成绩列表
     * @param studentId 学生编号
     * @return 成绩列表
     */
    List<Score> findByStudentId(String studentId);
    
    /**
     * 根据课程编号查找成绩列表
     * @param courseId 课程编号
     * @return 成绩列表
     */
    List<Score> findByCourseId(String courseId);
    
    /**
     * 根据学生编号和课程编号查找成绩
     * @param studentId 学生编号
     * @param courseId 课程编号
     * @return 成绩信息
     */
    Optional<Score> findByStudentIdAndCourseId(String studentId, String courseId);
    
    /**
     * 查找需要补考的成绩
     * @return 成绩列表
     */
    List<Score> findByMakeUpExamTrue();
    
    /**
     * 根据学分获得条件查找成绩
     * @param creditGainCondition 学分获得条件
     * @return 成绩列表
     */
    List<Score> findByCreditGainCondition(String creditGainCondition);
    
    /**
     * 根据总成绩范围查询成绩
     * @param minScore 最小总成绩
     * @param maxScore 最大总成绩
     * @return 成绩列表
     */
    @Query("SELECT s FROM Score s WHERE s.totalScore BETWEEN :minScore AND :maxScore")
    List<Score> findByTotalScoreBetween(@Param("minScore") int minScore, @Param("maxScore") int maxScore);
    
    /**
     * 查询某学生的平均成绩
     * @param studentId 学生编号
     * @return 平均成绩
     */
    @Query("SELECT AVG(s.totalScore) FROM Score s WHERE s.studentId = :studentId")
    Double findAverageScoreByStudentId(@Param("studentId") String studentId);
} 