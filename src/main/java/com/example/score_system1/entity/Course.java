package com.example.score_system1.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

/**
 * 课程实体类
 * 包含课程的基本信息：课程编号、课程名称、学分等
 */
@Entity
@Table(name = "course",
       uniqueConstraints = @UniqueConstraint(columnNames = "c_id"))
public class Course {
    
    @Id
    @Column(name = "c_id", length = 20, nullable = false)
    @NotBlank(message = "课程编号不能为空")
    private String courseId; // 课程编号
    
    @Column(name = "c_name", length = 50, nullable = false, columnDefinition = "NVARCHAR(50)")
    @NotBlank(message = "课程名称不能为空")
    private String courseName; // 课程名称
    
    @Column(name = "c_credit", nullable = false)
    @NotNull(message = "学分不能为空")
    @Min(value = 1, message = "学分必须大于0")
    private short courseCredit; // 课程学分

    // 无参构造函数 - JPA需要
    public Course() {}

    public Course(String courseId, String courseName, short courseCredit) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseCredit = courseCredit;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public short getCourseCredit() {
        return courseCredit;
    }

    public void setCourseCredit(short courseCredit) {
        this.courseCredit = courseCredit;
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseId='" + courseId + '\'' +
                ", courseName='" + courseName + '\'' +
                ", courseCredit=" + courseCredit +
                '}';
    }
}
