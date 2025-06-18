package com.example.score_system1.service;

import com.example.score_system1.entity.ScoreLog;
import com.example.score_system1.repository.ScoreLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 成绩日志业务服务类
 * 提供成绩日志相关的业务逻辑处理
 */
@Service
public class ScoreLogService {

    @Autowired
    private ScoreLogRepository scoreLogRepository;

    /**
     * 新增成绩日志
     * @param scoreLog 成绩日志信息
     * @return 保存后的成绩日志信息
     */
    public ScoreLog insertScoreLog(ScoreLog scoreLog) {
        return scoreLogRepository.save(scoreLog);
    }

    /**
     * 根据日志编号获取成绩日志信息
     * @param logId 日志编号
     * @return 成绩日志信息，不存在则返回null
     */
    public ScoreLog getScoreLogById(int logId) {
        Optional<ScoreLog> scoreLog = scoreLogRepository.findById(logId);
        return scoreLog.orElse(null);
    }

    /**
     * 根据成绩编号获取成绩日志列表
     * @param scoreId 成绩编号
     * @return 成绩日志列表
     */
    public List<ScoreLog> getScoreLogsByScoreId(String scoreId) {
        return scoreLogRepository.findByScoreId(scoreId);
    }

    /**
     * 根据学生编号获取成绩日志列表
     * @param stId 学生编号
     * @return 成绩日志列表
     */
    public List<ScoreLog> getScoreLogsByStudentId(String stId) {
        return scoreLogRepository.findByStudentId(stId);
    }

    /**
     * 根据课程编号获取成绩日志列表
     * @param cId 课程编号
     * @return 成绩日志列表
     */
    public List<ScoreLog> getScoreLogsByCourseId(String cId) {
        return scoreLogRepository.findByCourseId(cId);
    }

    /**
     * 获取所有成绩日志列表
     * @return 成绩日志列表
     */
    public List<ScoreLog> getAllScoreLogs() {
        return scoreLogRepository.findAll();
    }

    /**
     * 根据操作类型获取成绩日志列表
     * @param operationType 操作类型
     * @return 成绩日志列表
     */
    public List<ScoreLog> getScoreLogsByOperationType(String operationType) {
        return scoreLogRepository.findByOperationType(operationType);
    }

    /**
     * 根据操作用户获取成绩日志列表
     * @return 成绩日志列表
     */
    public List<ScoreLog> getScoreLogsByOperatorId(String operatorId) {
        return scoreLogRepository.findByOperatorId(operatorId);
    }

    /**
     * 根据时间范围查询成绩日志
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 成绩日志列表
     */
    public List<ScoreLog> getScoreLogsByTimeRange(Date startDate, Date endDate) {
        return scoreLogRepository.findByOperationTimeBetween(startDate, endDate);
    }

    /**
     * 根据学生编号和课程编号查找成绩日志列表
     * @param stId 学生编号
     * @param cId 课程编号
     * @return 成绩日志列表
     */
    public List<ScoreLog> getScoreLogsByStudentAndCourse(String stId, String cId) {
        return scoreLogRepository.findByStIdAndCId(stId, cId);
    }
}