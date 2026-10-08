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
 * 学生
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "student")
public class Student {

    /** 学号 */
    @Id
    @Column(name = "st_id", length = 20, nullable = false)
    private String studentId;

    /** 姓名 */
    @Column(name = "st_name", length = 50, nullable = false)
    private String studentName;

    /** 专业 */
    @Column(name = "st_major", length = 100, nullable = false)
    private String major;

    /** 已获得学分 */
    @Column(name = "st_get_credit", nullable = false)
    private Integer getCredit;

    /** 登录密码（BCrypt 散列） */
    @Column(name = "password", length = 100, nullable = false)
    private String password;
}
