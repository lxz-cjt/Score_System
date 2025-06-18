package com.example.score_system1.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

/**
 * 课程安排实体类
 * 包含课程安排的信息：课程安排编号、课程编号、教师编号、上课时间、上课地点等
 */
@Entity
@Table(name = "course_arranging",
       uniqueConstraints = @UniqueConstraint(columnNames = "ca_id"))
public class CourseArranging {
    
    @Id
    @Column(name = "ca_id", length = 50, nullable = false)
    @NotBlank(message = "课程安排编号不能为空")
    private String courseArrangingId; // 课程安排编号
    
    @Column(name = "c_id", length = 20, nullable = false)
    @NotBlank(message = "课程编号不能为空")
    private String courseId; // 课程编号
    
    @Column(name = "te_id", length = 20, nullable = false)
    @NotBlank(message = "教师编号不能为空")
    private String teacherId; // 教师编号
    
    @Column(name = "class_time", length = 50, nullable = false, columnDefinition = "NVARCHAR(50)")
    @NotBlank(message = "上课时间不能为空")
    private String classTime; // 上课时间
    
    @Column(name = "classroom", length = 50, nullable = false, columnDefinition = "NVARCHAR(50)")
    @NotBlank(message = "上课地点不能为空")
    private String classroom; // 上课地点

    // 无参构造函数 - JPA需要
    public CourseArranging() {}

    public CourseArranging(String courseId, String teacherId, String courseArrangingId, String classTime, String classroom) {
        this.courseArrangingId = courseArrangingId;
        this.courseId = courseId;
        this.teacherId = teacherId;
        this.classTime = classTime;
        this.classroom = classroom;
    }

    public String getCourseArrangingId() {
        return courseArrangingId;
    }

    public void setCourseArrangingId(String courseArrangingId) {
        this.courseArrangingId = courseArrangingId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getClassTime() {
        return classTime;
    }

    public void setClassTime(String classTime) {
        this.classTime = classTime;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    @Override
    public String toString() {
        return "CourseArranging{" +
                "courseArrangingId='" + courseArrangingId + '\'' +
                ", courseId='" + courseId + '\'' +
                ", teacherId='" + teacherId + '\'' +
                ", classTime='" + classTime + '\'' +
                ", classroom='" + classroom + '\'' +
                '}';
    }
}
