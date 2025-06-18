package com.example.score_system1.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

/**
 * 教师实体类
 * 包含教师的基本信息：工号、姓名、学院、密码等
 */
@Entity
@Table(name = "teacher",
       uniqueConstraints = @UniqueConstraint(columnNames = "te_id"))
public class Teacher {
    
    @Id
    @Column(name = "te_id", length = 20, nullable = false)
    @NotBlank(message = "教师编号不能为空")
    private String teacherId; // 教师编号
    
    @Column(name = "te_name", length = 50, nullable = false, columnDefinition = "NVARCHAR(50)")
    @NotBlank(message = "教师姓名不能为空")
    private String teacherName; // 教师姓名
    
    @Column(name = "te_college", length = 100, nullable = false, columnDefinition = "NVARCHAR(100)")
    @NotBlank(message = "学院不能为空")
    private String college; // 学院
    
    @Column(name = "password", length = 100, nullable = false)
    @NotBlank(message = "密码不能为空")
    private String password; // 密码

    // 无参构造函数 - JPA需要
    public Teacher() {}

    public Teacher(String teacherId, String teacherName, String college, String password) {
        this.teacherId = teacherId;
        this.teacherName = teacherName;
        this.college = college;
        this.password = password;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "teacherId='" + teacherId + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", college='" + college + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
