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
 * 课程安排（哪门课由哪位教师讲授、时间地点）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "course_arranging")
public class CourseArranging {

    /** 课程安排编号 */
    @Id
    @Column(name = "ca_id", length = 50, nullable = false)
    private String arrangingId;

    /** 课程编号 */
    @Column(name = "c_id", length = 20, nullable = false)
    private String courseId;

    /** 教师工号 */
    @Column(name = "te_id", length = 20, nullable = false)
    private String teacherId;

    /** 上课时间 */
    @Column(name = "class_time", length = 50, nullable = false)
    private String classTime;

    /** 上课地点 */
    @Column(name = "classroom", length = 50, nullable = false)
    private String classroom;
}
