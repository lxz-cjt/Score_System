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
 * 教师
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "teacher")
public class Teacher {

    /** 教师工号 */
    @Id
    @Column(name = "te_id", length = 20, nullable = false)
    private String teacherId;

    /** 姓名 */
    @Column(name = "te_name", length = 50, nullable = false)
    private String teacherName;

    /** 所属学院 */
    @Column(name = "te_college", length = 100, nullable = false)
    private String college;

    /** 登录密码（BCrypt 散列） */
    @Column(name = "password", length = 100, nullable = false)
    private String password;
}
