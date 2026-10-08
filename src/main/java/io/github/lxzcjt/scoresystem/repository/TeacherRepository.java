package io.github.lxzcjt.scoresystem.repository;

import io.github.lxzcjt.scoresystem.entity.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, String> {

    Optional<Teacher> findByTeacherId(String teacherId);

    boolean existsByTeacherId(String teacherId);

    /**
     * 按姓名或学院关键字分页搜索
     */
    Page<Teacher> findByTeacherNameContainingOrCollegeContaining(String name, String college, Pageable pageable);
}
