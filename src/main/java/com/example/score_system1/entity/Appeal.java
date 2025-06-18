package com.example.score_system1.entity;

import com.example.score_system1.enums.AppealStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Date;

/**
 * 申诉实体类
 * 包含学生对成绩的申诉信息：申诉编号、学生编号、申诉原因、处理状态等
 */
@Entity
@Table(name = "appeal",
       uniqueConstraints = @UniqueConstraint(columnNames = "ap_id"))
public class Appeal {
    
    @Id
    @Column(name = "ap_id", length = 50, nullable = false)
    @NotBlank(message = "申诉编号不能为空")
    private String appealId; // 申诉编号
    
    @Column(name = "st_id", length = 20, nullable = false)
    @NotBlank(message = "学生编号不能为空")
    private String studentId; // 学生编号
    
    @Column(name = "aas_id", length = 20)
    private String academicAffairsStaffId; // 教务管理人员编号
    
    @Column(name = "te_id", length = 20)
    private String teacherId; // 教师编号（处理申诉的教师）
    
    @Column(name = "appeal_reason", length = 500, nullable = false, columnDefinition = "NVARCHAR(500)")
    @NotBlank(message = "申诉原因不能为空")
    private String appealReason; // 申诉原因
    
    @Column(name = "appeal_time", nullable = false)
    @NotNull(message = "申诉时间不能为空")
    private Date appealTime; // 申诉时间
    
    @Column(name = "appeal_result", length = 500, columnDefinition = "NVARCHAR(500)")
    private String appealResult; // 申诉结果
    
    @Enumerated(EnumType.STRING)
    @Column(name = "appeal_status", nullable = false)
    @NotNull(message = "申诉状态不能为空")
    private AppealStatus appealStatus; // 申诉状态
    
    @Column(name = "result_time")
    private Date resultTime; // 结果时间

    // 无参构造函数 - JPA需要
    public Appeal() {}

    public Appeal(String studentId, String academicAffairsStaffId, String teacherId, String appealId, Date appealTime, String appealReason) {
        this.appealId = appealId;
        this.studentId = studentId;
        this.academicAffairsStaffId = academicAffairsStaffId;
        this.teacherId = teacherId;
        this.appealReason = appealReason;
        this.appealTime = appealTime;
        this.appealResult = "";
        this.appealStatus = AppealStatus.PENDING;
        this.resultTime = new Date(System.currentTimeMillis());
    }

    public String getAppealId() {
        return appealId;
    }

    public void setAppealId(String appealId) {
        this.appealId = appealId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getAcademicAffairsStaffId() {
        return academicAffairsStaffId;
    }

    public void setAcademicAffairsStaffId(String academicAffairsStaffId) {
        this.academicAffairsStaffId = academicAffairsStaffId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getAppealReason() {
        return appealReason;
    }

    public void setAppealReason(String appealReason) {
        this.appealReason = appealReason;
    }

    public Date getAppealTime() {
        return appealTime;
    }

    public void setAppealTime(Date appealTime) {
        this.appealTime = appealTime;
    }

    public String getAppealResult() {
        return appealResult;
    }

    public void setAppealResult(String appealResult) {
        this.appealResult = appealResult;
    }

    public AppealStatus getAppealStatus() {
        return appealStatus;
    }

    public void setAppealStatus(AppealStatus appealStatus) {
        this.appealStatus = appealStatus;
    }

    public Date getResultTime() {
        return resultTime;
    }

    public void setResultTime(Date resultTime) {
        this.resultTime = resultTime;
    }

    @Override
    public String toString() {
        return "Appeal{" +
                "appealId='" + appealId + '\'' +
                ", studentId='" + studentId + '\'' +
                ", academicAffairsStaffId='" + academicAffairsStaffId + '\'' +
                ", teacherId='" + teacherId + '\'' +
                ", appealReason='" + appealReason + '\'' +
                ", appealTime=" + appealTime +
                ", appealResult='" + appealResult + '\'' +
                ", appealStatus=" + appealStatus +
                ", resultTime=" + resultTime +
                '}';
    }
}
