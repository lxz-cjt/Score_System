package com.example.score_system1.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

/**
 * 成绩实体类
 * 包含学生某门课程的成绩信息：平时成绩、考试成绩、总成绩、学分获得条件、补考标记等
 */
@Entity
@Table(name = "score",
       uniqueConstraints = @UniqueConstraint(columnNames = "score_id"))
public class Score {
    
    @Id
    @Column(name = "score_id", length = 50, nullable = false)
    @NotBlank(message = "成绩编号不能为空")
    private String scoreId; // 成绩编号
    
    @Column(name = "daily_score", nullable = false)
    @NotNull(message = "平时成绩不能为空")
    @Min(value = 0, message = "平时成绩不能小于0")
    @Max(value = 100, message = "平时成绩不能大于100")
    private int dailyScore; // 平时成绩
    
    @Column(name = "exam_score", nullable = false)
    @NotNull(message = "考试成绩不能为空")
    @Min(value = 0, message = "考试成绩不能小于0")
    @Max(value = 100, message = "考试成绩不能大于100")
    private int examScore; // 考试成绩
    
    @Column(name = "total_score", nullable = false)
    @NotNull(message = "总成绩不能为空")
    @Min(value = 0, message = "总成绩不能小于0")
    @Max(value = 100, message = "总成绩不能大于100")
    private int totalScore; // 总成绩
    
    @Column(name = "credit_gain_condition", length = 20, nullable = false, columnDefinition = "NVARCHAR(20)")
    @NotBlank(message = "学分获得条件不能为空")
    private String creditGainCondition; // 学分获得条件（如：通过、不通过、优秀等）
    
    @Column(name = "make_up_exam", nullable = false)
    private boolean makeUpExam; // 是否需要补考
    
    @Column(name = "st_id", length = 20, nullable = false)
    @NotBlank(message = "学生编号不能为空")
    private String studentId; // 学生编号
    
    @Column(name = "c_id", length = 20, nullable = false)
    @NotBlank(message = "课程编号不能为空")
    private String courseId; // 课程编号

    // 无参构造函数 - JPA需要
    public Score() {}

    public Score(int dailyScore, int examScore, int totalScore, String creditGainCondition, boolean makeUpExam, String scoreId, String studentId, String courseId) {
        this.dailyScore = dailyScore;
        this.examScore = examScore;
        this.totalScore = totalScore;
        this.creditGainCondition = creditGainCondition;
        this.makeUpExam = makeUpExam;
        this.scoreId = scoreId;
        this.studentId = studentId;
        this.courseId = courseId;
    }

    public int getDailyScore() {
        return dailyScore;
    }

    public void setDailyScore(int dailyScore) {
        this.dailyScore = dailyScore;
    }

    public int getExamScore() {
        return examScore;
    }

    public void setExamScore(int examScore) {
        this.examScore = examScore;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public String getCreditGainCondition() {
        return creditGainCondition;
    }

    public void setCreditGainCondition(String creditGainCondition) {
        this.creditGainCondition = creditGainCondition;
    }

    public boolean isMakeUpExam() {
        return makeUpExam;
    }

    public void setMakeUpExam(boolean makeUpExam) {
        this.makeUpExam = makeUpExam;
    }

    public String getScoreId() {
        return scoreId;
    }

    public void setScoreId(String scoreId) {
        this.scoreId = scoreId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    @Override
    public String toString() {
        return "Score{" +
                "dailyScore=" + dailyScore +
                ", examScore=" + examScore +
                ", totalScore=" + totalScore +
                ", creditGainCondition='" + creditGainCondition + '\'' +
                ", makeUpExam=" + makeUpExam +
                ", scoreId='" + scoreId + '\'' +
                ", studentId='" + studentId + '\'' +
                ", courseId='" + courseId + '\'' +
                '}';
    }
}
