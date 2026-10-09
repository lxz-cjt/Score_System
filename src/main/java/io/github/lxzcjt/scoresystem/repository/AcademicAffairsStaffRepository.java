package io.github.lxzcjt.scoresystem.repository;

import io.github.lxzcjt.scoresystem.entity.AcademicAffairsStaff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AcademicAffairsStaffRepository extends JpaRepository<AcademicAffairsStaff, String> {

    Optional<AcademicAffairsStaff> findByStaffId(String staffId);

    boolean existsByStaffId(String staffId);

    Page<AcademicAffairsStaff> findByStaffNameContaining(String staffName, Pageable pageable);
}
