package io.github.lxzcjt.scoresystem.controller;

import io.github.lxzcjt.scoresystem.common.Result;
import io.github.lxzcjt.scoresystem.dto.response.AdminDashboardVO;
import io.github.lxzcjt.scoresystem.dto.response.StudentDashboardVO;
import io.github.lxzcjt.scoresystem.dto.response.TeacherDashboardVO;
import io.github.lxzcjt.scoresystem.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 各角色 Dashboard 统计接口
 */
@Tag(name = "Dashboard", description = "学生/教师/教务三角色统计面板")
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @Operation(summary = "学生 Dashboard", description = "基本信息 + 平均分/GPA + 申诉状态统计")
    @GetMapping("/student/{studentId}")
    public Result<StudentDashboardVO> getStudentDashboard(
            @Parameter(description = "学号") @PathVariable String studentId) {
        return Result.success(dashboardService.getStudentDashboard(studentId));
    }

    @Operation(summary = "教师 Dashboard", description = "基本信息 + 授课统计 + 待处理申诉数")
    @GetMapping("/teacher/{teacherId}")
    public Result<TeacherDashboardVO> getTeacherDashboard(
            @Parameter(description = "教师工号") @PathVariable String teacherId) {
        return Result.success(dashboardService.getTeacherDashboard(teacherId));
    }

    @Operation(summary = "教务 Dashboard", description = "全局统计 + 申诉状态/成绩分布/课程均分图表")
    @GetMapping("/admin/{staffId}")
    public Result<AdminDashboardVO> getAdminDashboard(
            @Parameter(description = "教务工号") @PathVariable String staffId) {
        return Result.success(dashboardService.getAdminDashboard(staffId));
    }
}
