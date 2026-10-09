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
 * 教务人员
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "academic_affairs_staff")
public class AcademicAffairsStaff {

    /** 教务工号 */
    @Id
    @Column(name = "aas_id", length = 20, nullable = false)
    private String staffId;

    /** 姓名 */
    @Column(name = "aas_name", length = 50, nullable = false)
    private String staffName;

    /** 登录密码（BCrypt 散列） */
    @Column(name = "password", length = 100, nullable = false)
    private String password;
}
