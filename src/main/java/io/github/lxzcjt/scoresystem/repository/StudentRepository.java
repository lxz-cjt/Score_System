package io.github.lxzcjt.scoresystem.repository;

import io.github.lxzcjt.scoresystem.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {

    Optional<Student> findByStudentId(String studentId);

    boolean existsByStudentId(String studentId);

    /**
     * 按姓名或专业关键字分页搜索
     */
    Page<Student> findByStudentNameContainingOrMajorContaining(String name, String major, Pageable pageable);
}
