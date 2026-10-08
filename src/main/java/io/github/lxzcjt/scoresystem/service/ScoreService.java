package io.github.lxzcjt.scoresystem.service;

import io.github.lxzcjt.scoresystem.common.PageResult;
import io.github.lxzcjt.scoresystem.common.exception.BusinessException;
import io.github.lxzcjt.scoresystem.dto.request.ScoreSaveRequest;
import io.github.lxzcjt.scoresystem.dto.request.ScoreUpdateRequest;
import io.github.lxzcjt.scoresystem.dto.response.GpaVO;
import io.github.lxzcjt.scoresystem.dto.response.ScoreVO;
import io.github.lxzcjt.scoresystem.entity.Course;
import io.github.lxzcjt.scoresystem.entity.Score;
import io.github.lxzcjt.scoresystem.entity.ScoreLog;
import io.github.lxzcjt.scoresystem.entity.Student;
import io.github.lxzcjt.scoresystem.repository.CourseRepository;
import io.github.lxzcjt.scoresystem.repository.ScoreLogRepository;
import io.github.lxzcjt.scoresystem.repository.ScoreRepository;
import io.github.lxzcjt.scoresystem.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 成绩核心服务
 *
 * 业务规则：
 * - 总评成绩 = 平时成绩 * 30% + 考试成绩 * 70%（四舍五入），由服务端统一计算
 * - 学分获得条件：>=90 优秀，>=60 通过，否则不通过
 * - 总评 < 60 标记为需要补考
 * - 新增 / 修改 / 删除成绩均写入成绩日志（score_log）留痕
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ScoreService {

    /** 平时成绩权重 */
    public static final double DAILY_WEIGHT = 0.3;
    /** 考试成绩权重 */
    public static final double EXAM_WEIGHT = 0.7;
    /** 及格线 */
    public static final int PASS_LINE = 60;
    /** 优秀线 */
    public static final int EXCELLENT_LINE = 90;

    private final ScoreRepository scoreRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final ScoreLogRepository scoreLogRepository;

    /**
     * 分页查询成绩，支持按学生/课程组合过滤
     */
    public PageResult<ScoreVO> listScores(String studentId, String courseId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Score> result;
        boolean hasStudent = StringUtils.hasText(studentId);
        boolean hasCourse = StringUtils.hasText(courseId);
        if (hasStudent && hasCourse) {
            result = scoreRepository.findByStudentIdAndCourseId(studentId, courseId, pageable);
        } else if (hasStudent) {
            result = scoreRepository.findByStudentId(studentId, pageable);
        } else if (hasCourse) {
            result = scoreRepository.findByCourseId(courseId, pageable);
        } else {
            result = scoreRepository.findAll(pageable);
        }
        return PageResult.of(result, enrichBatch(result.getContent()));
    }

    /**
     * 学生端：查询自己的全部成绩
     */
    public List<ScoreVO> listStudentScores(String studentId) {
        if (!studentRepository.existsByStudentId(studentId)) {
            throw BusinessException.notFound("学生不存在: " + studentId);
        }
        return enrichBatch(scoreRepository.findByStudentId(studentId));
    }

    public ScoreVO getScore(String scoreId) {
        return toVO(findScore(scoreId));
    }

    /**
     * 录入成绩
     */
    @Transactional
    public ScoreVO createScore(ScoreSaveRequest request) {
        if (!studentRepository.existsByStudentId(request.studentId())) {
            throw BusinessException.notFound("学生不存在: " + request.studentId());
        }
        if (!courseRepository.existsByCourseId(request.courseId())) {
            throw BusinessException.notFound("课程不存在: " + request.courseId());
        }
        if (scoreRepository.existsByStudentIdAndCourseId(request.studentId(), request.courseId())) {
            throw BusinessException.conflict("该学生此课程的成绩已存在，请勿重复录入");
        }

        int totalScore = calculateTotalScore(request.dailyScore(), request.examScore());
        Score score = Score.builder()
                .scoreId(generateId())
                .studentId(request.studentId())
                .courseId(request.courseId())
                .dailyScore(request.dailyScore())
                .examScore(request.examScore())
                .totalScore(totalScore)
                .creditGainCondition(resolveCreditCondition(totalScore))
                .makeUpExam(needMakeUpExam(totalScore))
                .build();
        scoreRepository.save(score);

        writeLog(score, "INSERT", null, request.operatorId());
        log.info("录入成绩: scoreId={}, student={}, course={}, total={}",
                score.getScoreId(), score.getStudentId(), score.getCourseId(), totalScore);
        return toVO(score);
    }

    /**
     * 修改成绩（只允许修改分数，总评重新计算）
     */
    @Transactional
    public ScoreVO updateScore(String scoreId, ScoreUpdateRequest request) {
        Score score = findScore(scoreId);
        Integer oldTotal = score.getTotalScore();
        String oldCondition = score.getCreditGainCondition();

        int totalScore = calculateTotalScore(request.dailyScore(), request.examScore());
        score.setDailyScore(request.dailyScore());
        score.setExamScore(request.examScore());
        score.setTotalScore(totalScore);
        score.setCreditGainCondition(resolveCreditCondition(totalScore));
        score.setMakeUpExam(needMakeUpExam(totalScore));
        scoreRepository.save(score);

        writeLog(score, "UPDATE", new Object[]{oldTotal, oldCondition}, request.operatorId());
        log.info("修改成绩: scoreId={}, oldTotal={}, newTotal={}, operator={}",
                scoreId, oldTotal, totalScore, request.operatorId());
        return toVO(score);
    }

    /**
     * 删除成绩
     */
    @Transactional
    public void deleteScore(String scoreId, String operatorId) {
        Score score = findScore(scoreId);
        writeLog(score, "DELETE", new Object[]{score.getTotalScore(), score.getCreditGainCondition()}, operatorId);
        scoreRepository.delete(score);
        log.info("删除成绩: scoreId={}, operator={}", scoreId, operatorId);
    }

    /**
     * 学生成绩概览：平均分 + GPA + 已修学分
     */
    public GpaVO getStudentScoreSummary(String studentId) {
        if (!studentRepository.existsByStudentId(studentId)) {
            throw BusinessException.notFound("学生不存在: " + studentId);
        }
        List<Score> scores = scoreRepository.findByStudentId(studentId);
        Map<String, Course> courseMap = loadCourses(scores);

        double totalCredits = 0;
        double weightedPoints = 0;
        for (Score score : scores) {
            Course course = courseMap.get(score.getCourseId());
            if (course == null) {
                continue;
            }
            double credit = course.getCourseCredit();
            totalCredits += credit;
            weightedPoints += credit * gradePointOf(score.getTotalScore());
        }
        double gpa = totalCredits == 0 ? 0 : weightedPoints / totalCredits;
        Double average = scoreRepository.findAverageScoreByStudentId(studentId);
        return new GpaVO(studentId, scores.size(),
                round2(average != null ? average : 0), round2(gpa), totalCredits);
    }

    // ==================== 业务计算（静态方法，便于单元测试与复用） ====================

    /**
     * 计算总评成绩：平时 30% + 考试 70%，四舍五入取整
     */
    public static int calculateTotalScore(int dailyScore, int examScore) {
        return (int) Math.round(dailyScore * DAILY_WEIGHT + examScore * EXAM_WEIGHT);
    }

    /**
     * 学分获得条件：>=90 优秀，>=60 通过，否则不通过
     */
    public static String resolveCreditCondition(int totalScore) {
        if (totalScore >= EXCELLENT_LINE) {
            return "优秀";
        }
        if (totalScore >= PASS_LINE) {
            return "通过";
        }
        return "不通过";
    }

    /**
     * 是否需要补考：总评 < 60
     */
    public static boolean needMakeUpExam(int totalScore) {
        return totalScore < PASS_LINE;
    }

    /**
     * 分数段对应的绩点（常见 4.0 制换算）
     */
    public static double gradePointOf(int totalScore) {
        if (totalScore >= 90) return 4.0;
        if (totalScore >= 85) return 3.7;
        if (totalScore >= 82) return 3.3;
        if (totalScore >= 78) return 3.0;
        if (totalScore >= 75) return 2.7;
        if (totalScore >= 72) return 2.3;
        if (totalScore >= 68) return 2.0;
        if (totalScore >= 64) return 1.5;
        if (totalScore >= 60) return 1.0;
        return 0.0;
    }

    // ==================== 内部方法 ====================

    private Score findScore(String scoreId) {
        return scoreRepository.findByScoreId(scoreId)
                .orElseThrow(() -> BusinessException.notFound("成绩记录不存在: " + scoreId));
    }

    /**
     * 写成绩日志。oldValues 为 [旧总评, 旧学分条件]，新增时为 null。
     */
    private void writeLog(Score score, String operationType, Object[] oldValues, String operatorId) {
        ScoreLog logEntry = ScoreLog.builder()
                .scoreId(score.getScoreId())
                .studentId(score.getStudentId())
                .courseId(score.getCourseId())
                .operationType(operationType)
                .oldScore(oldValues != null ? (Integer) oldValues[0] : null)
                .newScore("DELETE".equals(operationType) ? null : score.getTotalScore())
                .oldCreditCondition(oldValues != null ? (String) oldValues[1] : null)
                .newCreditCondition("DELETE".equals(operationType) ? null : score.getCreditGainCondition())
                .operationTime(new Date())
                .operatorId(operatorId)
                .build();
        scoreLogRepository.save(logEntry);
    }

    /**
     * 组装单个 VO
     */
    private ScoreVO toVO(Score score) {
        return enrichBatch(List.of(score)).get(0);
    }

    /**
     * 批量组装 VO：一次性查出关联的学生/课程，避免 N+1 查询
     */
    private List<ScoreVO> enrichBatch(List<Score> scores) {
        Set<String> studentIds = scores.stream().map(Score::getStudentId).collect(Collectors.toSet());
        Set<String> courseIds = scores.stream().map(Score::getCourseId).collect(Collectors.toSet());
        Map<String, Student> studentMap = studentRepository.findAllById(studentIds).stream()
                .collect(Collectors.toMap(Student::getStudentId, Function.identity()));
        Map<String, Course> courseMap = courseRepository.findAllById(courseIds).stream()
                .collect(Collectors.toMap(Course::getCourseId, Function.identity()));

        return scores.stream().map(score -> {
            Student student = studentMap.get(score.getStudentId());
            Course course = courseMap.get(score.getCourseId());
            return new ScoreVO(
                    score.getScoreId(),
                    score.getStudentId(),
                    student != null ? student.getStudentName() : null,
                    score.getCourseId(),
                    course != null ? course.getCourseName() : null,
                    course != null ? course.getCourseCredit() : null,
                    score.getDailyScore(),
                    score.getExamScore(),
                    score.getTotalScore(),
                    score.getCreditGainCondition(),
                    score.getMakeUpExam());
        }).toList();
    }

    private Map<String, Course> loadCourses(List<Score> scores) {
        Set<String> courseIds = scores.stream().map(Score::getCourseId).collect(Collectors.toSet());
        return courseRepository.findAllById(courseIds).stream()
                .collect(Collectors.toMap(Course::getCourseId, Function.identity()));
    }

    private static double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    private String generateId() {
        return "SC" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
    }
}
