package com.example.score_system1.controller;

import com.example.score_system1.dto.ApiResponse;
import com.example.score_system1.entity.Appeal;
import com.example.score_system1.entity.AppealProcess;
import com.example.score_system1.enums.AppealStatus;
import com.example.score_system1.service.AppealProcessService;
import com.example.score_system1.service.AppealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 申诉流程管理控制器
 * 处理学生申诉的完整流程：学生提交申诉 -> 教师处理 -> 教务人员审核
 */
@RestController
@RequestMapping("/api/appeal-flow")
@Tag(name = "申诉流程管理", description = "申诉流程管理相关接口")
public class AppealFlowController {

    @Autowired
    private AppealService appealService;

    @Autowired
    private AppealProcessService appealProcessService;

    // ==================== 学生功能 ====================

    @PostMapping("/student/submit")
    @Operation(summary = "学生提交申诉", description = "学生提交成绩申诉")
    public ApiResponse<Map<String, Object>> submitAppeal(@RequestBody Map<String, String> request) {
        try {
            String apId = request.get("apId");
            String studentId = request.get("studentId");
            String studentName = request.get("studentName");
            String content = request.get("content");

            // 提交申诉处理记录
            AppealProcess process = appealProcessService.submitAppeal(apId, studentId, studentName, content);
            
            Map<String, Object> response = new HashMap<>();
            response.put("process", process);
            response.put("status", AppealStatus.PENDING.getStatus());
            response.put("message", "申诉已提交，等待教师处理");

            return ApiResponse.success("申诉提交成功", response);
        } catch (Exception e) {
            return ApiResponse.error("申诉提交失败: " + e.getMessage());
        }
    }

    @PostMapping("/student/cancel")
    @Operation(summary = "学生取消申诉", description = "学生主动取消申诉")
    public ApiResponse<AppealProcess> cancelAppeal(@RequestBody Map<String, String> request) {
        try {
            String apId = request.get("apId");
            String studentId = request.get("studentId");
            String studentName = request.get("studentName");
            String reason = request.get("reason");

            AppealProcess process = appealProcessService.cancelAppeal(apId, studentId, studentName, reason);
            return ApiResponse.success("申诉取消成功", process);
        } catch (Exception e) {
            return ApiResponse.error("申诉取消失败: " + e.getMessage());
        }
    }

    @GetMapping("/student/{studentId}/appeals")
    @Operation(summary = "查询学生的申诉列表", description = "查询指定学生的所有申诉记录")
    public ApiResponse<List<Appeal>> getStudentAppeals(
            @Parameter(description = "学生编号") @PathVariable String studentId) {
        try {
            List<Appeal> appeals = appealService.getAppealsByStudentId(studentId);
            return ApiResponse.success("查询学生申诉列表成功", appeals);
        } catch (Exception e) {
            return ApiResponse.error("查询学生申诉列表失败: " + e.getMessage());
        }
    }

    // ==================== 教师功能 ====================

    @GetMapping("/teacher/{teacherId}/pending")
    @Operation(summary = "获取教师待处理申诉", description = "获取分配给指定教师的待处理申诉")
    public ApiResponse<List<Appeal>> getPendingAppealsForTeacher(
            @Parameter(description = "教师编号") @PathVariable String teacherId) {
        try {
            List<Appeal> appeals = appealService.getAppealsByTeacherId(teacherId);
            // 过滤出待处理状态的申诉
            List<Appeal> pendingAppeals = appeals.stream()
                    .filter(appeal -> AppealStatus.PENDING.equals(appeal.getAppealStatus()) ||
                                    AppealStatus.TEACHER_PROCESSING.equals(appeal.getAppealStatus()))
                    .toList();
            return ApiResponse.success("获取待处理申诉成功", pendingAppeals);
        } catch (Exception e) {
            return ApiResponse.error("获取待处理申诉失败: " + e.getMessage());
        }
    }

    @PostMapping("/teacher/process")
    @Operation(summary = "教师处理申诉", description = "教师处理学生申诉并提交材料")
    public ApiResponse<Map<String, Object>> teacherProcessAppeal(@RequestBody Map<String, String> request) {
        try {
            String apId = request.get("apId");
            String teacherId = request.get("teacherId");
            String teacherName = request.get("teacherName");
            String content = request.get("content");
            String attachments = request.get("attachments");

            AppealProcess process = appealProcessService.teacherProcessAppeal(
                    apId, teacherId, teacherName, content, attachments);
            
            Map<String, Object> response = new HashMap<>();
            response.put("process", process);
            response.put("status", AppealStatus.SUBMITTED_TO_ADMIN.getStatus());
            response.put("message", "申诉材料已提交给教务人员，等待审核");

            return ApiResponse.success("申诉处理成功", response);
        } catch (Exception e) {
            return ApiResponse.error("申诉处理失败: " + e.getMessage());
        }
    }

    @GetMapping("/teacher/{teacherId}/appeals")
    @Operation(summary = "查询教师处理的申诉列表", description = "查询指定教师处理的所有申诉记录")
    public ApiResponse<List<Appeal>> getTeacherAppeals(
            @Parameter(description = "教师编号") @PathVariable String teacherId) {
        try {
            List<Appeal> appeals = appealService.getAppealsByTeacherId(teacherId);
            return ApiResponse.success("查询教师申诉列表成功", appeals);
        } catch (Exception e) {
            return ApiResponse.error("查询教师申诉列表失败: " + e.getMessage());
        }
    }

    // ==================== 教务人员功能 ====================

    @GetMapping("/admin/pending")
    @Operation(summary = "获取待审核申诉", description = "获取提交给教务人员的待审核申诉")
    public ApiResponse<List<Appeal>> getPendingAppealsForAdmin() {
        try {
            List<Appeal> appeals = appealService.getAppealsByStatus(AppealStatus.SUBMITTED_TO_ADMIN);
            return ApiResponse.success("获取待审核申诉成功", appeals);
        } catch (Exception e) {
            return ApiResponse.error("获取待审核申诉失败: " + e.getMessage());
        }
    }

    @PostMapping("/admin/review")
    @Operation(summary = "教务人员审核申诉", description = "教务人员审核申诉并给出最终决定")
    public ApiResponse<Map<String, Object>> adminReviewAppeal(@RequestBody Map<String, Object> request) {
        try {
            String apId = (String) request.get("apId");
            String staffId = (String) request.get("staffId");
            String staffName = (String) request.get("staffName");
            String content = (String) request.get("content");
            Boolean approved = (Boolean) request.get("approved");

            AppealProcess process = appealProcessService.adminReviewAppeal(
                    apId, staffId, staffName, content, approved);
            
            Map<String, Object> response = new HashMap<>();
            response.put("process", process);
            response.put("status", approved ? AppealStatus.APPROVED.getStatus() : AppealStatus.REJECTED.getStatus());
            response.put("message", approved ? "申诉审核通过" : "申诉审核未通过");

            return ApiResponse.success("申诉审核完成", response);
        } catch (Exception e) {
            return ApiResponse.error("申诉审核失败: " + e.getMessage());
        }
    }

    @GetMapping("/admin/all")
    @Operation(summary = "获取所有申诉记录", description = "教务人员查看所有申诉记录")
    public ApiResponse<List<Appeal>> getAllAppeals() {
        try {
            List<Appeal> appeals = appealService.getAllAppeals();
            return ApiResponse.success("获取所有申诉记录成功", appeals);
        } catch (Exception e) {
            return ApiResponse.error("获取所有申诉记录失败: " + e.getMessage());
        }
    }

    // ==================== 通用功能 ====================

    @GetMapping("/process/{apId}")
    @Operation(summary = "查询申诉处理过程", description = "查询指定申诉的完整处理过程")
    public ApiResponse<List<AppealProcess>> getAppealProcess(
            @Parameter(description = "申诉编号") @PathVariable String apId) {
        try {
            List<AppealProcess> processes = appealProcessService.getAppealProcessesByApId(apId);
            return ApiResponse.success("查询申诉处理过程成功", processes);
        } catch (Exception e) {
            return ApiResponse.error("查询申诉处理过程失败: " + e.getMessage());
        }
    }

    @GetMapping("/appeal/{apId}")
    @Operation(summary = "查询申诉详情", description = "查询指定申诉的详细信息")
    public ApiResponse<Appeal> getAppealDetails(
            @Parameter(description = "申诉编号") @PathVariable String apId) {
        try {
            Appeal appeal = appealService.getAppealById(apId);
            if (appeal != null) {
                return ApiResponse.success("查询申诉详情成功", appeal);
            } else {
                return ApiResponse.error("申诉不存在", 404);
            }
        } catch (Exception e) {
            return ApiResponse.error("查询申诉详情失败: " + e.getMessage());
        }
    }

    @GetMapping("/status")
    @Operation(summary = "获取申诉状态说明", description = "获取所有申诉状态的说明信息")
    public ApiResponse<Map<String, String>> getAppealStatusInfo() {
        try {
            Map<String, String> statusInfo = new HashMap<>();
            for (AppealStatus status : AppealStatus.values()) {
                statusInfo.put(status.getStatus(), status.getDescription());
            }
            return ApiResponse.success("获取申诉状态信息成功", statusInfo);
        } catch (Exception e) {
            return ApiResponse.error("获取申诉状态信息失败: " + e.getMessage());
        }
    }
} 