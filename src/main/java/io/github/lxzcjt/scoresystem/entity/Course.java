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
 * 课程
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "course")
public class Course {

    /** 课程编号 */
    @Id
    @Column(name = "c_id", length = 20, nullable = false)
    private String courseId;

    /** 课程名称 */
    @Column(name = "c_name", length = 50, nullable = false)
    private String courseName;

    /** 学分 */
    @Column(name = "c_credit", nullable = false)
    private Short courseCredit;
}
