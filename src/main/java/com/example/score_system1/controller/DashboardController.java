package com.example.score_system1.controller;

import com.example.score_system1.dto.ApiResponse;
import com.example.score_system1.entity.*;
import com.example.score_system1.enums.AppealStatus;
import com.example.score_system1.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 角色特定Dashboard控制器
 * 提供学生、教师、教务人员的专属功能面板
 */
@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "角色Dashboard", description = "各角色专属功能面板")
public class DashboardController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private AcademicAffairsStaffService academicAffairsStaffService;

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private AppealService appealService;

    @Autowired
    private CourseArrangingService courseArrangingService;

    // ==================== 学生Dashboard ====================

    @GetMapping("/student/{stId}")
    @Operation(summary = "学生Dashboard", description = "获取学生专属信息面板")
    public ApiResponse<Map<String, Object>> getStudentDashboard(
            @Parameter(description = "学号") @PathVariable String stId) {
        try {
            Map<String, Object> dashboard = new HashMap<>();

            // 获取学生基本信息
            Student student = studentService.getStudentById(stId);
            dashboard.put("studentInfo", student);

            // 获取学生成绩
            List<Score> scores = scoreService.getScoresByStudentId(stId);
            dashboard.put("scores", scores);

            // 计算平均成绩
            double averageScore = scores.stream()
                    .mapToDouble(score -> (double) score.getTotalScore())
                    .average()
                    .orElse(0.0);
            dashboard.put("averageScore", averageScore);

            // 获取学生申诉记录
            List<Appeal> appeals = appealService.getAppealsByStudentId(stId);
            dashboard.put("appeals", appeals);

            // 统计申诉状态
            Map<AppealStatus, Long> appealStatusCount = appeals.stream()
                    .collect(Collectors.groupingBy(
                            Appeal::getAppealStatus,
                            Collectors.counting()
                    ));
            dashboard.put("appealStatusCount", appealStatusCount);

            return ApiResponse.success("获取学生Dashboard成功", dashboard);
        } catch (Exception e) {
            return ApiResponse.error("获取学生Dashboard失败: " + e.getMessage());
        }
    }

    @GetMapping("/student/{stId}/recent-scores")
    @Operation(summary = "学生最近成绩", description = "获取学生最近的成绩记录")
    public ApiResponse<List<Score>> getStudentRecentScores(
            @Parameter(description = "学号") @PathVariable String stId,
            @Parameter(description = "限制数量") @RequestParam(defaultValue = "10") int limit) {
        try {
            List<Score> scores = scoreService.getScoresByStudentId(stId);
            // 取最近的几条记录
            List<Score> recentScores = scores.stream()
                    .limit(limit)
                    .toList();
            return ApiResponse.success("获取最近成绩成功", recentScores);
        } catch (Exception e) {
            return ApiResponse.error("获取最近成绩失败: " + e.getMessage());
        }
    }

    // ==================== 教师Dashboard ====================

    @GetMapping("/teacher/{teId}")
    @Operation(summary = "教师Dashboard", description = "获取教师专属信息面板")
    public ApiResponse<Map<String, Object>> getTeacherDashboard(
            @Parameter(description = "教师编号") @PathVariable String teId) {
        try {
            Map<String, Object> dashboard = new HashMap<>();

            // 获取教师基本信息
            Teacher teacher = teacherService.getTeacherById(teId);
            dashboard.put("teacherInfo", teacher);

            // 获取教师负责的课程安排
            List<CourseArranging> courseArrangings = courseArrangingService.getCourseArrangingsByTeacherId(teId);
            dashboard.put("courseArrangings", courseArrangings);

            // 获取教师处理的申诉
            List<Appeal> appeals = appealService.getAppealsByTeacherId(teId);
            dashboard.put("appeals", appeals);

            // 统计课程数量
            int totalCourses = courseArrangings.size();
            dashboard.put("totalCourses", totalCourses);

            // 统计待处理申诉数量
            long pendingAppeals = appeals.stream()
                    .filter(appeal -> AppealStatus.PENDING.equals(appeal.getAppealStatus()) ||
                                    AppealStatus.TEACHER_PROCESSING.equals(appeal.getAppealStatus()))
                    .count();
            dashboard.put("pendingAppeals", pendingAppeals);

            return ApiResponse.success("获取教师Dashboard成功", dashboard);
        } catch (Exception e) {
            return ApiResponse.error("获取教师Dashboard失败: " + e.getMessage());
        }
    }

    @GetMapping("/teacher/{teId}/my-courses")
    @Operation(summary = "教师课程", description = "获取教师负责的所有课程")
    public ApiResponse<List<CourseArranging>> getTeacherCourses(
            @Parameter(description = "教师编号") @PathVariable String teId) {
        try {
            List<CourseArranging> courseArrangings = courseArrangingService.getCourseArrangingsByTeacherId(teId);
            return ApiResponse.success("获取教师课程成功", courseArrangings);
        } catch (Exception e) {
            return ApiResponse.error("获取教师课程失败: " + e.getMessage());
        }
    }

    @GetMapping("/teacher/{teId}/pending-appeals")
    @Operation(summary = "教师待处理申诉", description = "获取教师待处理的申诉")
    public ApiResponse<List<Appeal>> getTeacherPendingAppeals(
            @Parameter(description = "教师编号") @PathVariable String teId) {
        try {
            List<Appeal> appeals = appealService.getAppealsByTeacherId(teId);
            List<Appeal> pendingAppeals = appeals.stream()
                    .filter(appeal -> AppealStatus.PENDING.equals(appeal.getAppealStatus()) ||
                                    AppealStatus.TEACHER_PROCESSING.equals(appeal.getAppealStatus()))
                    .toList();
            return ApiResponse.success("获取待处理申诉成功", pendingAppeals);
        } catch (Exception e) {
            return ApiResponse.error("获取待处理申诉失败: " + e.getMessage());
        }
    }

    // ==================== 教务人员Dashboard ====================

    @GetMapping("/admin/{aasId}")
    @Operation(summary = "教务人员Dashboard", description = "获取教务人员专属信息面板")
    public ApiResponse<Map<String, Object>> getAdminDashboard(
            @Parameter(description = "教务人员编号") @PathVariable String aasId) {
        try {
            Map<String, Object> dashboard = new HashMap<>();

            // 获取教务人员基本信息
            AcademicAffairsStaff staff = academicAffairsStaffService.getAcademicAffairsStaffById(aasId);
            dashboard.put("staffInfo", staff);

            // 获取系统统计信息
            long totalStudents = studentService.getAllStudents().size();
            long totalTeachers = teacherService.getAllTeachers().size();
            long totalCourses = courseService.getAllCourses().size();
            long totalAppeals = appealService.getAllAppeals().size();

            dashboard.put("totalStudents", totalStudents);
            dashboard.put("totalTeachers", totalTeachers);
            dashboard.put("totalCourses", totalCourses);
            dashboard.put("totalAppeals", totalAppeals);

            // 获取待审核申诉
            List<Appeal> pendingAppeals = appealService.getAppealsByStatus(AppealStatus.SUBMITTED_TO_ADMIN);
            dashboard.put("pendingAppeals", pendingAppeals);
            dashboard.put("pendingAppealsCount", pendingAppeals.size());

            // 获取申诉状态统计
            List<Appeal> allAppeals = appealService.getAllAppeals();
            Map<AppealStatus, Long> appealStatusCount = allAppeals.stream()
                    .collect(Collectors.groupingBy(
                            Appeal::getAppealStatus,
                            Collectors.counting()
                    ));
            dashboard.put("appealStatusCount", appealStatusCount);

            return ApiResponse.success("获取教务人员Dashboard成功", dashboard);
        } catch (Exception e) {
            return ApiResponse.error("获取教务人员Dashboard失败: " + e.getMessage());
        }
    }

    @GetMapping("/admin/statistics")
    @Operation(summary = "系统统计信息", description = "获取系统整体统计信息")
    public ApiResponse<Map<String, Object>> getSystemStatistics() {
        try {
            Map<String, Object> statistics = new HashMap<>();

            // 用户统计
            statistics.put("totalStudents", studentService.getAllStudents().size());
            statistics.put("totalTeachers", teacherService.getAllTeachers().size());
            statistics.put("totalStaff", academicAffairsStaffService.getAllAcademicAffairsStaffs().size());

            // 课程统计
            statistics.put("totalCourses", courseService.getAllCourses().size());
            
            // 申诉统计
            List<Appeal> allAppeals = appealService.getAllAppeals();
            statistics.put("totalAppeals", allAppeals.size());
            
            Map<AppealStatus, Long> appealStatusCount = allAppeals.stream()
                    .collect(Collectors.groupingBy(
                            Appeal::getAppealStatus,
                            Collectors.counting()
                    ));
            statistics.put("appealStatusCount", appealStatusCount);

            return ApiResponse.success("获取系统统计信息成功", statistics);
        } catch (Exception e) {
            return ApiResponse.error("获取系统统计信息失败: " + e.getMessage());
        }
    }
} 