package io.github.lxzcjt.scoresystem.entity;

import io.github.lxzcjt.scoresystem.enums.AppealStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 成绩申诉
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "appeal")
public class Appeal {

    /** 申诉编号 */
    @Id
    @Column(name = "ap_id", length = 50, nullable = false)
    private String appealId;

    /** 学号（申诉发起人） */
    @Column(name = "st_id", length = 20, nullable = false)
    private String studentId;

    /** 被申诉的课程编号 */
    @Column(name = "c_id", length = 20, nullable = false)
    private String courseId;

    /** 处理该申诉的教师工号（按课程安排自动分配） */
    @Column(name = "te_id", length = 20)
    private String teacherId;

    /** 审核该申诉的教务工号 */
    @Column(name = "aas_id", length = 20)
    private String staffId;

    /** 申诉理由 */
    @Column(name = "appeal_reason", length = 500, nullable = false)
    private String appealReason;

    /** 申诉时间 */
    @Column(name = "appeal_time", nullable = false)
    private Date appealTime;

    /** 申诉结果说明 */
    @Column(name = "appeal_result", length = 500)
    private String appealResult;

    /** 申诉状态 */
    @Enumerated(EnumType.STRING)
    @Column(name = "appeal_status", length = 30, nullable = false)
    private AppealStatus appealStatus;

    /** 审核完成时间 */
    @Column(name = "result_time")
    private Date resultTime;
}
