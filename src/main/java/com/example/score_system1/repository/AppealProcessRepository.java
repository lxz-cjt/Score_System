package com.example.score_system1.repository;

import com.example.score_system1.entity.AppealProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 申诉处理过程数据访问层接口
 * 提供申诉处理过程相关的数据库操作方法
 */
@Repository
public interface AppealProcessRepository extends JpaRepository<AppealProcess, Long> {
    
    /**
     * 根据申诉编号查找处理过程列表
     * @param apId 申诉编号
     * @return 处理过程列表
     */
    @Query("SELECT ap FROM AppealProcess ap WHERE ap.appealId = :apId ORDER BY ap.processTime ASC")
    List<AppealProcess> findByApIdOrderByProcessTime(@Param("apId") String apId);
    
    /**
     * 根据处理人编号查找处理过程列表
     * @param processorId 处理人编号
     * @return 处理过程列表
     */
    List<AppealProcess> findByProcessorId(String processorId);
    
    /**
     * 根据时间范围查询处理过程
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 处理过程列表
     */
    @Query("SELECT ap FROM AppealProcess ap WHERE ap.processTime BETWEEN :startDate AND :endDate ORDER BY ap.processTime DESC")
    List<AppealProcess> findByProcessTimeBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    
    /**
     * 获取申诉的最新处理记录
     * @param apId 申诉编号
     * @return 最新处理记录
     */
    @Query("SELECT ap FROM AppealProcess ap WHERE ap.appealId = :apId ORDER BY ap.processTime DESC LIMIT 1")
    AppealProcess findLatestByApId(@Param("apId") String apId);
} 