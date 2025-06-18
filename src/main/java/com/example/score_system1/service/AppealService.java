package com.example.score_system1.service;

import com.example.score_system1.entity.Appeal;
import com.example.score_system1.enums.AppealStatus;
import com.example.score_system1.repository.AppealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 申诉业务服务类
 * 提供申诉相关的业务逻辑处理
 */
@Service
public class AppealService {

    @Autowired
    private AppealRepository appealRepository;

    /**
     * 新增申诉
     * @param appeal 申诉信息
     * @return 保存后的申诉信息
     */
    public Appeal insertAppeal(Appeal appeal) {
        return appealRepository.save(appeal);
    }

    /**
     * 根据申诉编号获取申诉信息
     * @param apId 申诉编号
     * @return 申诉信息，不存在则返回null
     */
    public Appeal getAppealById(String apId) {
        Optional<Appeal> appeal = appealRepository.findByAppealId(apId);
        return appeal.orElse(null);
    }

    /**
     * 根据学生编号获取申诉列表
     * @param stId 学生编号
     * @return 申诉列表
     */
    public List<Appeal> getAppealsByStudentId(String stId) {
        return appealRepository.findByStudentId(stId);
    }

    /**
     * 更新申诉信息
     * @param appeal 申诉信息
     * @return 更新后的申诉信息
     */
    public Appeal updateAppeal(Appeal appeal) {
        return appealRepository.save(appeal);
    }

    /**
     * 根据申诉编号删除申诉
     * @param apId 申诉编号
     */
    public void deleteAppeal(String apId) {
        appealRepository.deleteById(apId);
    }

    /**
     * 根据教务管理人员编号获取申诉列表
     * @param aasId 教务管理人员编号
     * @return 申诉列表
     */
    public List<Appeal> getAppealsByAcademicAffairsStaffId(String aasId) {
        return appealRepository.findByAcademicAffairsStaffId(aasId);
    }

    /**
     * 根据申诉状态获取申诉列表
     * @param apStatus 申诉状态
     * @return 申诉列表
     */
    public List<Appeal> getAppealsByStatus(AppealStatus apStatus) {
        return appealRepository.findByAppealStatus(apStatus);
    }

    /**
     * 获取待处理的申诉列表
     * @return 申诉列表
     */
    public List<Appeal> getPendingAppeals() {
        return appealRepository.findPendingAppeals();
    }

    /**
     * 根据时间范围查询申诉
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 申诉列表
     */
    public List<Appeal> getAppealsByTimeRange(Date startDate, Date endDate) {
        return appealRepository.findByApTimeBetween(startDate, endDate);
    }

    /**
     * 根据教师编号获取申诉列表
     * @param teId 教师编号
     * @return 申诉列表
     */
    public List<Appeal> getAppealsByTeacherId(String teId) {
        return appealRepository.findByTeacherId(teId);
    }

    /**
     * 获取所有申诉记录
     * @return 申诉列表
     */
    public List<Appeal> getAllAppeals() {
        return appealRepository.findAll();
    }
}