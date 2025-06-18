package com.example.score_system1.repository;

import com.example.score_system1.entity.Appeal;
import com.example.score_system1.enums.AppealStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 申诉数据访问层接口
 * 提供申诉相关的数据库操作方法
 */
@Repository
public interface AppealRepository extends JpaRepository<Appeal, String> {
    
    /**
     * 根据申诉编号查找申诉
     * @param apId 申诉编号
     * @return 申诉信息
     */
    Optional<Appeal> findByAppealId(String apId);
    
    /**
     * 根据学生编号查找申诉列表
     * @param stId 学生编号
     * @return 申诉列表
     */
    List<Appeal> findByStudentId(String stId);
    
    /**
     * 根据教务管理人员编号查找申诉列表
     * @param aasId 教务管理人员编号
     * @return 申诉列表
     */
    List<Appeal> findByAcademicAffairsStaffId(String aasId);
    
    /**
     * 根据申诉状态查找申诉列表
     * @param apStatus 申诉状态
     * @return 申诉列表
     */
    List<Appeal> findByAppealStatus(AppealStatus apStatus);
    
    /**
     * 根据申诉时间范围查询申诉
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 申诉列表
     */
    @Query("SELECT a FROM Appeal a WHERE a.appealTime BETWEEN :startDate AND :endDate")
    List<Appeal> findByApTimeBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    
    /**
     * 查找待处理的申诉
     * @return 申诉列表
     */
    @Query("SELECT a FROM Appeal a WHERE a.appealStatus = '待处理' ORDER BY a.appealTime ASC")
    List<Appeal> findPendingAppeals();
    
    /**
     * 根据教师编号查找申诉列表
     * @param teacherId 教师编号
     * @return 申诉列表
     */
    List<Appeal> findByTeacherId(String teacherId);
} 