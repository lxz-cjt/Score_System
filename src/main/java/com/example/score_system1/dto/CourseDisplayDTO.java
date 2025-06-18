package com.example.score_system1.dto;

/**
 * 课程展示DTO
 * 用于向前端返回课程的详细展示信息
 */
public class CourseDisplayDTO {
    private long id; // 展示用的序号
    private String courseCode; // 课程代码
    private String courseName; // 课程名称
    private String teacherName; // 教师姓名
    private int credits; // 学分
    private String courseType; // 课程类型
    private String schedule; // 上课时间
    private String classroom; // 教室
    private int capacity; // 容量
    private int enrolled; // 已选人数

    // 无参构造函数
    public CourseDisplayDTO() {}

    // 全参构造函数
    public CourseDisplayDTO(long id, String courseCode, String courseName, String teacherName, 
                           int credits, String courseType, String schedule, String classroom, 
                           int capacity, int enrolled) {
        this.id = id;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.teacherName = teacherName;
        this.credits = credits;
        this.courseType = courseType;
        this.schedule = schedule;
        this.classroom = classroom;
        this.capacity = capacity;
        this.enrolled = enrolled;
    }

    // Getter和Setter方法
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getEnrolled() {
        return enrolled;
    }

    public void setEnrolled(int enrolled) {
        this.enrolled = enrolled;
    }

    @Override
    public String toString() {
        return "CourseDisplayDTO{" +
                "id=" + id +
                ", courseCode='" + courseCode + '\'' +
                ", courseName='" + courseName + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", credits=" + credits +
                ", courseType='" + courseType + '\'' +
                ", schedule='" + schedule + '\'' +
                ", classroom='" + classroom + '\'' +
                ", capacity=" + capacity +
                ", enrolled=" + enrolled +
                '}';
    }
} 