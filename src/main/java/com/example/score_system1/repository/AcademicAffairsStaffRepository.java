package com.example.score_system1.repository;

import com.example.score_system1.entity.AcademicAffairsStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 教务管理人员数据访问层接口
 * 提供教务管理人员相关的数据库操作方法
 */
@Repository
public interface AcademicAffairsStaffRepository extends JpaRepository<AcademicAffairsStaff, String> {
    
    /**
     * 根据工号查找教务管理人员
     * @param academicAffairsStaffId 工号
     * @return 教务管理人员信息
     */
    Optional<AcademicAffairsStaff> findByAcademicAffairsStaffId(String academicAffairsStaffId);
    
    /**
     * 根据姓名模糊查询教务管理人员
     * @param academicAffairsStaffName 姓名
     * @return 教务管理人员列表
     */
    List<AcademicAffairsStaff> findByAcademicAffairsStaffNameContaining(String academicAffairsStaffName);
    
    /**
     * 验证教务管理人员登录
     * @param academicAffairsStaffId 工号
     * @param password 密码
     * @return 教务管理人员信息
     */
    @Query("SELECT a FROM AcademicAffairsStaff a WHERE a.academicAffairsStaffId = :academicAffairsStaffId AND a.password = :password")
    Optional<AcademicAffairsStaff> findByAcademicAffairsStaffIdAndPassword(@Param("academicAffairsStaffId") String academicAffairsStaffId, @Param("password") String password);
    
    /**
     * 检查教务管理人员是否存在
     * @param academicAffairsStaffId 工号
     * @return 是否存在
     */
    boolean existsByAcademicAffairsStaffId(String academicAffairsStaffId);
} 