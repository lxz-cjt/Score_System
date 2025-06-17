package com.example.score_system1.service;

import com.example.score_system1.entity.Score;
import com.example.score_system1.entity.ScoreLog;
import com.example.score_system1.entity.Student;
import com.example.score_system1.mapper.ScoreMapper;
import com.example.score_system1.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ScoreService {

    @Autowired
    private ScoreMapper scoreMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private ScoreLogService scoreLogService;

    @Autowired
    private CourseService courseService;

    public void insertScore(Score score) {
        scoreMapper.insertScore(score);
    }

    public Score getScoreById(String scoreId) {
        return scoreMapper.selectScoreById(scoreId);
    }

    public List<Score> getScoresByStudentId(String stId) {
        return scoreMapper.selectScoresByStudentId(stId);
    }

    public List<Score> getScoresByCourseId(String cId) {
        return scoreMapper.selectScoresByCourseId(cId);
    }

    public void updateScore(Score newScore) {
        Score oldScore = scoreMapper.selectScoreById(newScore.getScoreId());
        if (oldScore == null) {
            throw new NoSuchElementException("成绩记录不存在");
        }

        // 记录变更日志
        ScoreLog log = new ScoreLog(
                0, // logId 由数据库生成
                newScore.getScoreId(),
                newScore.getStId(),
                newScore.getcId(),
                "UPDATE",
                oldScore.getTotalScore(),
                newScore.getTotalScore(),
                oldScore.getCreditGainCondition(),
                newScore.getCreditGainCondition(),
                new java.sql.Date(System.currentTimeMillis()),
                "系统管理员"
        );
        scoreLogService.insertScoreLog(log);

        scoreMapper.updateScore(newScore);
    }

    public void deleteScore(String scoreId) {
        scoreMapper.deleteScore(scoreId);
    }

    /**
     * 计算学分绩点 (GPA)
     */
    public double calculateGPA(String stId) {
        Student student = studentMapper.selectStudentById(stId);
        if (student == null) {
            throw new NoSuchElementException("学生不存在");
        }

        List<Score> scores = scoreMapper.selectScoresByStudentId(stId);
        double totalCredits = 0;
        double totalGPA = 0;

        for (Score score : scores) {
            double credit = getCourseCredit(score.getcId());
            double gradePoint = getGradePoint(score.getTotalScore());
            totalCredits += credit;
            totalGPA += credit * gradePoint;
        }

        return totalCredits == 0 ? 0 : totalGPA / totalCredits;
    }

    private double getCourseCredit(String cId) {
        return courseService.getCourseById(cId).getcCredit();
    }

    private double getGradePoint(int totalScore) {
        if (totalScore >= 90) return 4.0;
        if (totalScore >= 85) return 3.7;
        if (totalScore >= 82) return 3.3;
        if (totalScore >= 78) return 3.0;
        if (totalScore >= 75) return 2.7;
        if (totalScore >= 72) return 2.3;
        if (totalScore >= 68) return 2.0;
        if (totalScore >= 64) return 1.5;
        return 0.0;
    }
}