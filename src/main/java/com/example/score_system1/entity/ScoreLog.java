package com.example.score_system1.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

/**
 * 成绩日志实体类
 * 记录成绩操作的历史信息：操作类型、操作前后的成绩、操作人、操作时间等
 */
@Entity
@Table(name = "score_log")
public class ScoreLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private int logId; // 日志编号
    
    @Column(name = "score_id", length = 50, nullable = false)
    @NotBlank(message = "成绩编号不能为空")
    private String scoreId; // 成绩编号
    
    @Column(name = "st_id", length = 20, nullable = false)
    @NotBlank(message = "学生编号不能为空")
    private String studentId; // 学生编号
    
    @Column(name = "c_id", length = 20, nullable = false)
    @NotBlank(message = "课程编号不能为空")
    private String courseId; // 课程编号
    
    @Column(name = "operation_type", length = 20, nullable = false, columnDefinition = "NVARCHAR(20)")
    @NotBlank(message = "操作类型不能为空")
    private String operationType; // 操作类型（INSERT、UPDATE、DELETE）
    
    @Column(name = "old_score")
    private int oldScore; // 修改前的成绩
    
    @Column(name = "new_score")
    private int newScore; // 修改后的成绩
    
    @Column(name = "old_credit_condition", length = 20, columnDefinition = "NVARCHAR(20)")
    private String oldCreditCondition; // 修改前的学分获得条件
    
    @Column(name = "new_credit_condition", length = 20, columnDefinition = "NVARCHAR(20)")
    private String newCreditCondition; // 修改后的学分获得条件
    
    @Column(name = "operation_time", nullable = false)
    @NotNull(message = "操作时间不能为空")
    private Date operationTime; // 操作时间
    
    @Column(name = "operator_id", length = 50, nullable = false)
    @NotBlank(message = "操作人不能为空")
    private String operatorId; // 操作人

    // 无参构造函数 - JPA需要
    public ScoreLog() {}

    public ScoreLog(int logId, String scoreId, String studentId, String courseId, String operationType, int oldScore, int newScore, String oldCreditCondition, String newCreditCondition, Date operationTime, String operatorId) {
        this.logId = logId;
        this.scoreId = scoreId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.operationType = operationType;
        this.oldScore = oldScore;
        this.newScore = newScore;
        this.oldCreditCondition = oldCreditCondition;
        this.newCreditCondition = newCreditCondition;
        this.operationTime = operationTime;
        this.operatorId = operatorId;
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public String getScoreId() {
        return scoreId;
    }

    public void setScoreId(String scoreId) {
        this.scoreId = scoreId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public int getOldScore() {
        return oldScore;
    }

    public void setOldScore(int oldScore) {
        this.oldScore = oldScore;
    }

    public int getNewScore() {
        return newScore;
    }

    public void setNewScore(int newScore) {
        this.newScore = newScore;
    }

    public String getOldCreditCondition() {
        return oldCreditCondition;
    }

    public void setOldCreditCondition(String oldCreditCondition) {
        this.oldCreditCondition = oldCreditCondition;
    }

    public String getNewCreditCondition() {
        return newCreditCondition;
    }

    public void setNewCreditCondition(String newCreditCondition) {
        this.newCreditCondition = newCreditCondition;
    }

    public Date getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(Date operationTime) {
        this.operationTime = operationTime;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    @Override
    public String toString() {
        return "ScoreLog{" +
                "logId=" + logId +
                ", scoreId='" + scoreId + '\'' +
                ", studentId='" + studentId + '\'' +
                ", courseId='" + courseId + '\'' +
                ", operationType='" + operationType + '\'' +
                ", oldScore=" + oldScore +
                ", newScore=" + newScore +
                ", oldCreditCondition='" + oldCreditCondition + '\'' +
                ", newCreditCondition='" + newCreditCondition + '\'' +
                ", operationTime=" + operationTime +
                ", operatorId='" + operatorId + '\'' +
                '}';
    }
}
