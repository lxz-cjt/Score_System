package io.github.lxzcjt.scoresystem.repository;

import io.github.lxzcjt.scoresystem.entity.CourseArranging;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseArrangingRepository extends JpaRepository<CourseArranging, String> {

    Optional<CourseArranging> findByArrangingId(String arrangingId);

    List<CourseArranging> findByCourseId(String courseId);

    List<CourseArranging> findByTeacherId(String teacherId);

    Page<CourseArranging> findByCourseId(String courseId, Pageable pageable);

    Page<CourseArranging> findByTeacherId(String teacherId, Pageable pageable);

    Optional<CourseArranging> findFirstByCourseId(String courseId);

    boolean existsByCourseIdAndTeacherId(String courseId, String teacherId);
}
