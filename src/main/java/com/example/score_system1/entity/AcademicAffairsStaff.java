package com.example.score_system1.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

/**
 * 教务管理人员实体类
 * 包含教务管理人员的基本信息：工号、姓名、密码等
 */
@Entity
@Table(name = "academic_affairs_staff",
       uniqueConstraints = @UniqueConstraint(columnNames = "aas_id"))
public class AcademicAffairsStaff {
    
    @Id
    @Column(name = "aas_id", length = 20, nullable = false)
    @NotBlank(message = "教务管理人员编号不能为空")
    private String academicAffairsStaffId; // 教务管理人员工号
    
    @Column(name = "aas_name", length = 50, nullable = false, columnDefinition = "NVARCHAR(50)")
    @NotBlank(message = "教务管理人员姓名不能为空")
    private String academicAffairsStaffName; // 教务管理人员姓名
    
    @Column(name = "password", length = 100, nullable = false)
    @NotBlank(message = "密码不能为空")
    private String password; // 密码

    // 无参构造函数 - JPA需要
    public AcademicAffairsStaff() {}

    public AcademicAffairsStaff(String academicAffairsStaffId, String academicAffairsStaffName, String password) {
        this.academicAffairsStaffId = academicAffairsStaffId;
        this.academicAffairsStaffName = academicAffairsStaffName;
        this.password = password;
    }

    public String getAcademicAffairsStaffId() {
        return academicAffairsStaffId;
    }

    public void setAcademicAffairsStaffId(String academicAffairsStaffId) {
        this.academicAffairsStaffId = academicAffairsStaffId;
    }

    public String getAcademicAffairsStaffName() {
        return academicAffairsStaffName;
    }

    public void setAcademicAffairsStaffName(String academicAffairsStaffName) {
        this.academicAffairsStaffName = academicAffairsStaffName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "AcademicAffairsStaff{" +
                "academicAffairsStaffId='" + academicAffairsStaffId + '\'' +
                ", academicAffairsStaffName='" + academicAffairsStaffName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
