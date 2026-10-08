package io.github.lxzcjt.scoresystem.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 成绩（一名学生一门课程一条记录）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "score")
public class Score {

    /** 成绩编号 */
    @Id
    @Column(name = "score_id", length = 50, nullable = false)
    private String scoreId;

    /** 平时成绩（0-100） */
    @Column(name = "daily_score", nullable = false)
    private Integer dailyScore;

    /** 考试成绩（0-100） */
    @Column(name = "exam_score", nullable = false)
    private Integer examScore;

    /** 总评成绩（平时 30% + 考试 70%，服务端计算） */
    @Column(name = "total_score", nullable = false)
    private Integer totalScore;

    /** 学分获得条件：优秀 / 通过 / 不通过 */
    @Column(name = "credit_gain_condition", length = 20, nullable = false)
    private String creditGainCondition;

    /** 是否需要补考（总评 < 60） */
    @Column(name = "make_up_exam", nullable = false)
    private Boolean makeUpExam;

    /** 学号 */
    @Column(name = "st_id", length = 20, nullable = false)
    private String studentId;

    /** 课程编号 */
    @Column(name = "c_id", length = 20, nullable = false)
    private String courseId;
}
