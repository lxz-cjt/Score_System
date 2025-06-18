package com.example.score_system1.service;

import com.example.score_system1.entity.Appeal;
import com.example.score_system1.entity.AppealProcess;
import com.example.score_system1.enums.AppealStatus;
import com.example.score_system1.repository.AppealProcessRepository;
import com.example.score_system1.repository.AppealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 申诉处理过程业务服务类
 * 提供申诉流程管理的业务逻辑处理
 */
@Service
public class AppealProcessService {

    @Autowired
    private AppealProcessRepository appealProcessRepository;
    
    @Autowired
    private AppealRepository appealRepository;

    /**
     * 新增申诉处理记录
     * @param appealProcess 申诉处理信息
     * @return 保存后的申诉处理信息
     */
    public AppealProcess insertAppealProcess(AppealProcess appealProcess) {
        return appealProcessRepository.save(appealProcess);
    }

    /**
     * 根据申诉编号获取处理过程列表
     * @param apId 申诉编号
     * @return 处理过程列表
     */
    public List<AppealProcess> getAppealProcessesByApId(String apId) {
        return appealProcessRepository.findByApIdOrderByProcessTime(apId);
    }

    /**
     * 根据处理人编号获取处理过程列表
     * @param processorId 处理人编号
     * @return 处理过程列表
     */
    public List<AppealProcess> getAppealProcessesByProcessorId(String processorId) {
        return appealProcessRepository.findByProcessorId(processorId);
    }

    /**
     * 学生提交申诉
     * @param apId 申诉编号
     * @param studentId 学生编号
     * @param studentName 学生姓名
     * @param content 申诉内容
     * @return 处理记录
     */
    @Transactional
    public AppealProcess submitAppeal(String apId, String studentId, String studentName, String content) {
        // 更新申诉状态
        Optional<Appeal> appealOpt = appealRepository.findByAppealId(apId);
        if (appealOpt.isPresent()) {
            Appeal appeal = appealOpt.get();
            appeal.setAppealStatus(AppealStatus.PENDING);
            appealRepository.save(appeal);
        }

        // 创建处理记录
        AppealProcess process = new AppealProcess();
        process.setAppealId(apId);
        process.setProcessOpinion("学生提交: " + content);
        process.setProcessorId(studentId);
        process.setProcessorName(studentName);
        process.setProcessTime(new Date());
        process.setProcessStep(AppealStatus.PENDING.getStatus());

        return appealProcessRepository.save(process);
    }

    /**
     * 教师处理申诉
     * @param apId 申诉编号
     * @param teacherId 教师编号
     * @param teacherName 教师姓名
     * @param content 处理内容
     * @param attachments 附件信息
     * @return 处理记录
     */
    @Transactional
    public AppealProcess teacherProcessAppeal(String apId, String teacherId, String teacherName, 
                                             String content, String attachments) {
        // 获取当前状态
        Optional<Appeal> appealOpt = appealRepository.findByAppealId(apId);
        if (appealOpt.isEmpty()) {
            throw new RuntimeException("申诉不存在");
        }
        
        Appeal appeal = appealOpt.get();
        AppealStatus oldStatus = appeal.getAppealStatus();
        
        // 更新申诉状态
        appeal.setAppealStatus(AppealStatus.SUBMITTED_TO_ADMIN);
        appealRepository.save(appeal);

        // 创建处理记录
        AppealProcess process = new AppealProcess();
        process.setAppealId(apId);
        process.setProcessOpinion("教师处理: " + content + (attachments != null ? " [附件: " + attachments + "]" : ""));
        process.setProcessorId(teacherId);
        process.setProcessorName(teacherName);
        process.setProcessTime(new Date());
        process.setProcessStep(AppealStatus.SUBMITTED_TO_ADMIN.getStatus());

        return appealProcessRepository.save(process);
    }

    /**
     * 教务人员审核申诉
     * @param apId 申诉编号
     * @param staffId 教务人员编号
     * @param staffName 教务人员姓名
     * @param content 审核意见
     * @param approved 是否通过
     * @return 处理记录
     */
    @Transactional
    public AppealProcess adminReviewAppeal(String apId, String staffId, String staffName, 
                                          String content, boolean approved) {
        // 获取当前状态
        Optional<Appeal> appealOpt = appealRepository.findByAppealId(apId);
        if (appealOpt.isEmpty()) {
            throw new RuntimeException("申诉不存在");
        }
        
        Appeal appeal = appealOpt.get();
        AppealStatus oldStatus = appeal.getAppealStatus();
        
        // 更新申诉状态
        AppealStatus newStatus = approved ? AppealStatus.APPROVED : AppealStatus.REJECTED;
        appeal.setAppealStatus(newStatus);
        appealRepository.save(appeal);

        // 创建处理记录
        AppealProcess process = new AppealProcess();
        process.setAppealId(apId);
        process.setProcessOpinion("教务审核: " + content);
        process.setProcessorId(staffId);
        process.setProcessorName(staffName);
        process.setProcessTime(new Date());
        process.setProcessStep(newStatus.getStatus());

        return appealProcessRepository.save(process);
    }

    /**
     * 学生取消申诉
     * @param apId 申诉编号
     * @param studentId 学生编号
     * @param studentName 学生姓名
     * @param reason 取消原因
     * @return 处理记录
     */
    @Transactional
    public AppealProcess cancelAppeal(String apId, String studentId, String studentName, String reason) {
        // 获取当前状态
        Optional<Appeal> appealOpt = appealRepository.findByAppealId(apId);
        if (appealOpt.isEmpty()) {
            throw new RuntimeException("申诉不存在");
        }
        
        Appeal appeal = appealOpt.get();
        AppealStatus oldStatus = appeal.getAppealStatus();
        
        // 更新申诉状态
        appeal.setAppealStatus(AppealStatus.CANCELLED);
        appealRepository.save(appeal);

        // 创建处理记录
        AppealProcess process = new AppealProcess();
        process.setAppealId(apId);
        process.setProcessOpinion("学生取消: " + reason);
        process.setProcessorId(studentId);
        process.setProcessorName(studentName);
        process.setProcessTime(new Date());
        process.setProcessStep(AppealStatus.CANCELLED.getStatus());

        return appealProcessRepository.save(process);
    }

    /**
     * 获取申诉的最新处理记录
     * @param apId 申诉编号
     * @return 最新处理记录
     */
    public AppealProcess getLatestProcessByApId(String apId) {
        return appealProcessRepository.findLatestByApId(apId);
    }

    /**
     * 根据时间范围查询处理记录
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 处理记录列表
     */
    public List<AppealProcess> getAppealProcessesByTimeRange(Date startDate, Date endDate) {
        return appealProcessRepository.findByProcessTimeBetween(startDate, endDate);
    }
} 