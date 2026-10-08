package io.github.lxzcjt.scoresystem.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 申诉处理记录（流程留痕，前端时间线展示）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "appeal_process")
public class AppealProcess {

    /** 处理记录编号（自增） */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "process_id")
    private Integer processId;

    /** 申诉编号 */
    @Column(name = "ap_id", length = 50, nullable = false)
    private String appealId;

    /** 处理步骤（如：学生提交 / 教师处理 / 教务审核） */
    @Column(name = "process_step", length = 100, nullable = false)
    private String processStep;

    /** 处理人编号 */
    @Column(name = "processor_id", length = 50, nullable = false)
    private String processorId;

    /** 处理人姓名 */
    @Column(name = "processor_name", length = 50, nullable = false)
    private String processorName;

    /** 处理时间 */
    @Column(name = "process_time", nullable = false)
    private Date processTime;

    /** 处理意见 */
    @Column(name = "process_opinion", length = 1000)
    private String processOpinion;
}
