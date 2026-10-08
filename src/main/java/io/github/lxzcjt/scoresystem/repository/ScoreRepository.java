package io.github.lxzcjt.scoresystem.repository;

import io.github.lxzcjt.scoresystem.entity.Score;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScoreRepository extends JpaRepository<Score, String> {

    Optional<Score> findByScoreId(String scoreId);

    boolean existsByStudentIdAndCourseId(String studentId, String courseId);

    List<Score> findByStudentId(String studentId);

    Page<Score> findByStudentId(String studentId, Pageable pageable);

    Page<Score> findByCourseId(String courseId, Pageable pageable);

    Page<Score> findByStudentIdAndCourseId(String studentId, String courseId, Pageable pageable);

    /**
     * 查询某学生的平均总评成绩
     */
    @Query("SELECT AVG(s.totalScore) FROM Score s WHERE s.studentId = :studentId")
    Double findAverageScoreByStudentId(@Param("studentId") String studentId);

    /**
     * 统计指定分数段内的成绩数量（用于成绩分布统计）
     */
    long countByTotalScoreBetween(int minScore, int maxScore);

    /**
     * 统计每门课程的平均成绩（用于 Dashboard 图表）
     */
    @Query("SELECT s.courseId, AVG(s.totalScore) FROM Score s GROUP BY s.courseId")
    List<Object[]> findAverageScoreGroupByCourse();

    /**
     * 统计指定课程集合中每门课程的平均成绩（教师 Dashboard）
     */
    @Query("SELECT s.courseId, AVG(s.totalScore) FROM Score s WHERE s.courseId IN :courseIds GROUP BY s.courseId")
    List<Object[]> findAverageScoreGroupByCourseIn(@Param("courseIds") List<String> courseIds);

    /**
     * 统计指定课程集合下的成绩记录数（教师授课学生人次）
     */
    long countByCourseIdIn(List<String> courseIds);
}
