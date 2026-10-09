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
 * 成绩变更日志（新增 / 修改 / 删除均留痕）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "score_log")
public class ScoreLog {

    /** 日志编号（自增） */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Integer logId;

    /** 成绩编号 */
    @Column(name = "score_id", length = 50, nullable = false)
    private String scoreId;

    /** 学号 */
    @Column(name = "st_id", length = 20, nullable = false)
    private String studentId;

    /** 课程编号 */
    @Column(name = "c_id", length = 20, nullable = false)
    private String courseId;

    /** 操作类型：INSERT / UPDATE / DELETE */
    @Column(name = "operation_type", length = 20, nullable = false)
    private String operationType;

    /** 修改前总评 */
    @Column(name = "old_score")
    private Integer oldScore;

    /** 修改后总评 */
    @Column(name = "new_score")
    private Integer newScore;

    /** 修改前学分获得条件 */
    @Column(name = "old_credit_condition", length = 20)
    private String oldCreditCondition;

    /** 修改后学分获得条件 */
    @Column(name = "new_credit_condition", length = 20)
    private String newCreditCondition;

    /** 操作时间 */
    @Column(name = "operation_time", nullable = false)
    private Date operationTime;

    /** 操作人工号 */
    @Column(name = "operator_id", length = 50, nullable = false)
    private String operatorId;
}
