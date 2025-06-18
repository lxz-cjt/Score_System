package com.example.score_system1.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Date;

/**
 * 申诉处理过程实体类
 * 记录申诉处理的详细过程：处理步骤、处理人、处理时间、处理意见等
 */
@Entity
@Table(name = "appeal_process")
public class AppealProcess {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "process_id")
    private int processId; // 流程编号
    
    @Column(name = "ap_id", length = 50, nullable = false)
    @NotBlank(message = "申诉编号不能为空")
    private String appealId; // 申诉编号
    
    @Column(name = "process_step", length = 100, nullable = false, columnDefinition = "NVARCHAR(100)")
    @NotBlank(message = "处理步骤不能为空")
    private String processStep; // 处理步骤
    
    @Column(name = "processor_id", length = 50, nullable = false)
    @NotBlank(message = "处理人编号不能为空")
    private String processorId; // 处理人编号
    
    @Column(name = "processor_name", length = 50, nullable = false, columnDefinition = "NVARCHAR(50)")
    @NotBlank(message = "处理人姓名不能为空")
    private String processorName; // 处理人姓名
    
    @Column(name = "process_time", nullable = false)
    @NotNull(message = "处理时间不能为空")
    private Date processTime; // 处理时间
    
    @Column(name = "process_opinion", length = 1000, columnDefinition = "NVARCHAR(1000)")
    private String processOpinion; // 处理意见

    // 无参构造函数 - JPA需要
    public AppealProcess() {}

    public AppealProcess(int processId, String appealId, String processStep, String processorId, String processorName, Date processTime, String processOpinion) {
        this.processId = processId;
        this.appealId = appealId;
        this.processStep = processStep;
        this.processorId = processorId;
        this.processorName = processorName;
        this.processTime = processTime;
        this.processOpinion = processOpinion;
    }

    public int getProcessId() {
        return processId;
    }

    public void setProcessId(int processId) {
        this.processId = processId;
    }

    public String getAppealId() {
        return appealId;
    }

    public void setAppealId(String appealId) {
        this.appealId = appealId;
    }

    public String getProcessStep() {
        return processStep;
    }

    public void setProcessStep(String processStep) {
        this.processStep = processStep;
    }

    public String getProcessorId() {
        return processorId;
    }

    public void setProcessorId(String processorId) {
        this.processorId = processorId;
    }

    public String getProcessorName() {
        return processorName;
    }

    public void setProcessorName(String processorName) {
        this.processorName = processorName;
    }

    public Date getProcessTime() {
        return processTime;
    }

    public void setProcessTime(Date processTime) {
        this.processTime = processTime;
    }

    public String getProcessOpinion() {
        return processOpinion;
    }

    public void setProcessOpinion(String processOpinion) {
        this.processOpinion = processOpinion;
    }

    @Override
    public String toString() {
        return "AppealProcess{" +
                "processId=" + processId +
                ", appealId='" + appealId + '\'' +
                ", processStep='" + processStep + '\'' +
                ", processorId='" + processorId + '\'' +
                ", processorName='" + processorName + '\'' +
                ", processTime=" + processTime +
                ", processOpinion='" + processOpinion + '\'' +
                '}';
    }
} 