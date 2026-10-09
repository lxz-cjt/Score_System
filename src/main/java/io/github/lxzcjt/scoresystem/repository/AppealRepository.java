package io.github.lxzcjt.scoresystem.repository;

import io.github.lxzcjt.scoresystem.entity.Appeal;
import io.github.lxzcjt.scoresystem.enums.AppealStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppealRepository extends JpaRepository<Appeal, String> {

    Optional<Appeal> findByAppealId(String appealId);

    List<Appeal> findByStudentIdOrderByAppealTimeDesc(String studentId);

    Page<Appeal> findByStudentId(String studentId, Pageable pageable);

    Page<Appeal> findByTeacherId(String teacherId, Pageable pageable);

    Page<Appeal> findByTeacherIdAndAppealStatusIn(String teacherId, List<AppealStatus> statuses, Pageable pageable);

    Page<Appeal> findByAppealStatus(AppealStatus status, Pageable pageable);

    long countByAppealStatus(AppealStatus status);

    /**
     * 判断某学生对某课程是否已有进行中的申诉（防止重复提交）
     */
    boolean existsByStudentIdAndCourseIdAndAppealStatusIn(String studentId, String courseId, List<AppealStatus> statuses);
}
