package io.github.lxzcjt.scoresystem.repository;

import io.github.lxzcjt.scoresystem.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {

    Optional<Course> findByCourseId(String courseId);

    boolean existsByCourseId(String courseId);

    Page<Course> findByCourseNameContaining(String courseName, Pageable pageable);
}
