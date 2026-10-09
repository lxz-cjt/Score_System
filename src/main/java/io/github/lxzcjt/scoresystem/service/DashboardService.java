package io.github.lxzcjt.scoresystem.service;

import io.github.lxzcjt.scoresystem.common.exception.BusinessException;
import io.github.lxzcjt.scoresystem.dto.response.AdminDashboardVO;
import io.github.lxzcjt.scoresystem.dto.response.ChartItemVO;
import io.github.lxzcjt.scoresystem.dto.response.StudentDashboardVO;
import io.github.lxzcjt.scoresystem.dto.response.TeacherDashboardVO;
import io.github.lxzcjt.scoresystem.entity.AcademicAffairsStaff;
import io.github.lxzcjt.scoresystem.entity.Appeal;
import io.github.lxzcjt.scoresystem.entity.Course;
import io.github.lxzcjt.scoresystem.entity.CourseArranging;
import io.github.lxzcjt.scoresystem.entity.Student;
import io.github.lxzcjt.scoresystem.entity.Teacher;
import io.github.lxzcjt.scoresystem.enums.AppealStatus;
import io.github.lxzcjt.scoresystem.repository.AcademicAffairsStaffRepository;
import io.github.lxzcjt.scoresystem.repository.AppealRepository;
import io.github.lxzcjt.scoresystem.repository.CourseArrangingRepository;
import io.github.lxzcjt.scoresystem.repository.CourseRepository;
import io.github.lxzcjt.scoresystem.repository.ScoreRepository;
import io.github.lxzcjt.scoresystem.repository.StudentRepository;
import io.github.lxzcjt.scoresystem.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 各角色 Dashboard 统计服务
 */
@Service
@RequiredArgsConstructor
public class DashboardService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final AcademicAffairsStaffRepository staffRepository;
    private final CourseRepository courseRepository;
    private final CourseArrangingRepository arrangingRepository;
    private final ScoreRepository scoreRepository;
    private final AppealRepository appealRepository;
    private final ScoreService scoreService;

    /**
     * 学生 Dashboard：基本信息 + 平均分/GPA + 申诉状态统计
     */
    public StudentDashboardVO getStudentDashboard(String studentId) {
        Student student = studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> BusinessException.notFound("学生不存在: " + studentId));

        var summary = scoreService.getStudentScoreSummary(studentId);

        Map<AppealStatus, Long> statusCount = appealRepository
                .findByStudentIdOrderByAppealTimeDesc(studentId).stream()
                .collect(Collectors.groupingBy(Appeal::getAppealStatus, Collectors.counting()));

        return new StudentDashboardVO(
                student.getStudentId(),
                student.getStudentName(),
                student.getMajor(),
                student.getGetCredit(),
                summary.scoreCount(),
                summary.averageScore(),
                summary.gpa(),
                toChartItems(statusCount));
    }

    /**
     * 教师 Dashboard：基本信息 + 授课统计 + 待处理申诉数
     */
    public TeacherDashboardVO getTeacherDashboard(String teacherId) {
        Teacher teacher = teacherRepository.findByTeacherId(teacherId)
                .orElseThrow(() -> BusinessException.notFound("教师不存在: " + teacherId));

        List<CourseArranging> arrangings = arrangingRepository.findByTeacherId(teacherId);
        List<String> courseIds = arrangings.stream().map(CourseArranging::getCourseId).distinct().toList();

        long studentCount = courseIds.isEmpty() ? 0 : scoreRepository.countByCourseIdIn(courseIds);
        long pendingAppeals = appealRepository.countByAppealStatus(AppealStatus.PENDING);

        return new TeacherDashboardVO(
                teacher.getTeacherId(),
                teacher.getTeacherName(),
                teacher.getCollege(),
                courseIds.size(),
                studentCount,
                pendingAppeals,
                courseAverages(courseIds));
    }

    /**
     * 教务 Dashboard：全局统计 + 申诉状态/成绩分布/课程均分图表
     */
    public AdminDashboardVO getAdminDashboard(String staffId) {
        AcademicAffairsStaff staff = staffRepository.findByStaffId(staffId)
                .orElseThrow(() -> BusinessException.notFound("教务人员不存在: " + staffId));

        Map<AppealStatus, Long> statusCount = appealRepository.findAll().stream()
                .collect(Collectors.groupingBy(Appeal::getAppealStatus, Collectors.counting()));

        return new AdminDashboardVO(
                studentRepository.count(),
                teacherRepository.count(),
                courseRepository.count(),
                scoreRepository.count(),
                appealRepository.count(),
                appealRepository.countByAppealStatus(AppealStatus.SUBMITTED_TO_ADMIN),
                toChartItems(statusCount),
                scoreDistribution(),
                courseAverages(null));
    }

    /**
     * 成绩分数段分布：<60 / 60-69 / 70-79 / 80-89 / 90-100
     */
    private List<ChartItemVO> scoreDistribution() {
        int[][] ranges = {{0, 59}, {60, 69}, {70, 79}, {80, 89}, {90, 100}};
        String[] labels = {"60分以下", "60-69分", "70-79分", "80-89分", "90-100分"};
        List<ChartItemVO> distribution = new ArrayList<>();
        for (int i = 0; i < ranges.length; i++) {
            distribution.add(new ChartItemVO(labels[i],
                    scoreRepository.countByTotalScoreBetween(ranges[i][0], ranges[i][1])));
        }
        return distribution;
    }

    /**
     * 课程平均成绩（courseIds 为 null 时统计全部课程）
     */
    private List<ChartItemVO> courseAverages(List<String> courseIds) {
        List<Object[]> rows = courseIds == null
                ? scoreRepository.findAverageScoreGroupByCourse()
                : (courseIds.isEmpty() ? List.of() : scoreRepository.findAverageScoreGroupByCourseIn(courseIds));

        Map<String, Course> courseMap = courseRepository.findAll().stream()
                .collect(Collectors.toMap(Course::getCourseId, Function.identity()));

        return rows.stream()
                .map(row -> {
                    String courseId = (String) row[0];
                    Double avg = (Double) row[1];
                    Course course = courseMap.get(courseId);
                    String name = course != null ? course.getCourseName() : courseId;
                    return new ChartItemVO(name, Math.round(avg * 100.0) / 100.0);
                })
                .toList();
    }

    private List<ChartItemVO> toChartItems(Map<AppealStatus, Long> statusCount) {
        return java.util.Arrays.stream(AppealStatus.values())
                .map(status -> new ChartItemVO(status.getLabel(), statusCount.getOrDefault(status, 0L)))
                .toList();
    }
}
