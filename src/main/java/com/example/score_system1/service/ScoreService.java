package com.example.score_system1.service;

import com.example.score_system1.entity.Score;
import com.example.score_system1.entity.ScoreLog;
import com.example.score_system1.entity.Student;
import com.example.score_system1.repository.ScoreRepository;
import com.example.score_system1.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * 成绩业务服务类
 * 提供成绩相关的业务逻辑处理
 */
@Service
public class ScoreService {

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ScoreLogService scoreLogService;

    @Autowired
    private CourseService courseService;

    /**
     * 新增成绩
     * @param score 成绩信息
     * @return 保存后的成绩信息
     */
    public Score insertScore(Score score) {
        return scoreRepository.save(score);
    }

    /**
     * 根据成绩编号获取成绩信息
     * @param scoreId 成绩编号
     * @return 成绩信息，不存在则返回null
     */
    public Score getScoreById(String scoreId) {
        Optional<Score> score = scoreRepository.findByScoreId(scoreId);
        return score.orElse(null);
    }

    /**
     * 根据学生编号获取成绩列表
     * @param stId 学生编号
     * @return 成绩列表
     */
    public List<Score> getScoresByStudentId(String stId) {
        return scoreRepository.findByStudentId(stId);
    }

    /**
     * 根据课程编号获取成绩列表
     * @param cId 课程编号
     * @return 成绩列表
     */
    public List<Score> getScoresByCourseId(String cId) {
        return scoreRepository.findByCourseId(cId);
    }

    /**
     * 更新成绩信息
     * @param newScore 新的成绩信息
     * @return 更新后的成绩信息
     */
    public Score updateScore(Score newScore) {
        Optional<Score> oldScoreOpt = scoreRepository.findByScoreId(newScore.getScoreId());
        if (oldScoreOpt.isEmpty()) {
            throw new NoSuchElementException("成绩记录不存在");
        }
        
        Score oldScore = oldScoreOpt.get();

        // 记录变更日志
        ScoreLog log = new ScoreLog(
                0, // logId 由数据库生成
                newScore.getScoreId(),
                newScore.getStudentId(),
                newScore.getCourseId(),
                "UPDATE",
                oldScore.getTotalScore(),
                newScore.getTotalScore(),
                oldScore.getCreditGainCondition(),
                newScore.getCreditGainCondition(),
                new java.util.Date(System.currentTimeMillis()),
                "系统管理员"
        );
        scoreLogService.insertScoreLog(log);

        return scoreRepository.save(newScore);
    }

    /**
     * 根据成绩编号删除成绩
     * @param scoreId 成绩编号
     */
    public void deleteScore(String scoreId) {
        scoreRepository.deleteById(scoreId);
    }

    /**
     * 获取需要补考的成绩列表
     * @return 成绩列表
     */
    public List<Score> getMakeUpExamScores() {
        return scoreRepository.findByMakeUpExamTrue();
    }

    /**
     * 根据学分获得条件查询成绩
     * @param creditGainCondition 学分获得条件
     * @return 成绩列表
     */
    public List<Score> getScoresByCreditCondition(String creditGainCondition) {
        return scoreRepository.findByCreditGainCondition(creditGainCondition);
    }

    /**
     * 根据成绩范围查询成绩
     * @param minScore 最小成绩
     * @param maxScore 最大成绩
     * @return 成绩列表
     */
    public List<Score> getScoresByRange(int minScore, int maxScore) {
        return scoreRepository.findByTotalScoreBetween(minScore, maxScore);
    }

    /**
     * 计算学生平均成绩
     * @param stId 学生编号
     * @return 平均成绩
     */
    public Double getAverageScore(String stId) {
        return scoreRepository.findAverageScoreByStudentId(stId);
    }

    /**
     * 计算学分绩点 (GPA)
     */
    public double calculateGPA(String stId) {
        Optional<Student> studentOpt = studentRepository.findByStudentId(stId);
        if (studentOpt.isEmpty()) {
            throw new NoSuchElementException("学生不存在");
        }

        List<Score> scores = scoreRepository.findByStudentId(stId);
        double totalCredits = 0;
        double totalGPA = 0;

        for (Score score : scores) {
            double credit = getCourseCredit(score.getCourseId());
            double gradePoint = getGradePoint(score.getTotalScore());
            totalCredits += credit;
            totalGPA += credit * gradePoint;
        }

        return totalCredits == 0 ? 0 : totalGPA / totalCredits;
    }

    /**
     * 获取课程学分
     * @param cId 课程编号
     * @return 课程学分
     */
    private double getCourseCredit(String cId) {
        return courseService.getCourseById(cId).getCourseCredit();
    }

    /**
     * 根据分数计算绩点
     * @param totalScore 总分
     * @return 绩点
     */
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

    /**
     * 添加成绩记录
     * @param newScore 成绩信息
     * @return 操作结果信息
     */
    public String addScore(Score newScore) {
        try {
            // 检查该学生是否已有该课程的成绩
            List<Score> existingScores = scoreRepository.findByStudentId(newScore.getStudentId());
            boolean hasCourseScore = existingScores.stream()
                    .anyMatch(score -> score.getCourseId().equals(newScore.getCourseId()));
            
            if (hasCourseScore) {
                return "该学生已存在该课程的成绩记录";
            }

            // 生成成绩编号 (格式: SC + 时间戳)
            String scoreId = "SC" + System.currentTimeMillis();
            newScore.setScoreId(scoreId);
            
            // 保存成绩
            scoreRepository.save(newScore);
            
            // 记录操作日志
            ScoreLog log = new ScoreLog(
                0, // logId 由数据库生成
                scoreId,
                newScore.getStudentId(),
                newScore.getCourseId(),
                "INSERT",
                0, // 之前没有分数
                newScore.getTotalScore(),
                "", // 之前没有学分获得条件
                newScore.getCreditGainCondition(),
                new java.util.Date(System.currentTimeMillis()),
                "系统管理员"
            );
            scoreLogService.insertScoreLog(log);
            
            return "成绩添加成功";
        } catch (Exception e) {
            return "成绩添加失败: " + e.getMessage();
        }
    }

    public List<Score> getAllScores() {
        try {
            return scoreRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("获取所有成绩列表失败: " + e.getMessage());
        }
    }
}