package com.example.score_system1.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

/**
 * 学生实体类
 * 包含学生的基本信息：学号、姓名、专业、获得学分、密码
 */
@Entity
@Table(name = "student",
       uniqueConstraints = @UniqueConstraint(columnNames = "st_id"))
public class Student {
    
    @Id
    @Column(name = "st_id", length = 20, nullable = false)
    @NotBlank(message = "学号不能为空")
    private String studentId; // 学号
    
    @Column(name = "st_name", length = 50, nullable = false, columnDefinition = "NVARCHAR(50)")
    @NotBlank(message = "学生姓名不能为空")
    private String studentName; // 学生姓名
    
    @Column(name = "st_major", length = 100, nullable = false, columnDefinition = "NVARCHAR(100)")
    @NotBlank(message = "专业不能为空")
    private String major; // 专业
    
    @Column(name = "st_get_credit", nullable = false)
    @NotNull(message = "获得学分不能为空")
    @Min(value = 0, message = "获得学分不能小于0")
    private int getCredit; // 获得学分
    
    @Column(name = "password", length = 100, nullable = false)
    @NotBlank(message = "密码不能为空")
    private String password; // 密码

    // 无参构造函数 - JPA需要
    public Student() {}

    public Student(String studentId, String studentName, String major, int getCredit, String password) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.major = major;
        this.getCredit = getCredit;
        this.password = password;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public int getGetCredit() {
        return getCredit;
    }

    public void setGetCredit(int getCredit) {
        this.getCredit = getCredit;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", studentName='" + studentName + '\'' +
                ", major='" + major + '\'' +
                ", getCredit=" + getCredit +
                ", password='" + password + '\'' +
                '}';
    }
}
