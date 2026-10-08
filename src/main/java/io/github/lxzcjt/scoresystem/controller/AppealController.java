package io.github.lxzcjt.scoresystem.controller;

import io.github.lxzcjt.scoresystem.common.PageResult;
import io.github.lxzcjt.scoresystem.common.Result;
import io.github.lxzcjt.scoresystem.dto.request.AppealCancelRequest;
import io.github.lxzcjt.scoresystem.dto.request.AppealCreateRequest;
import io.github.lxzcjt.scoresystem.dto.request.AppealProcessRequest;
import io.github.lxzcjt.scoresystem.dto.request.AppealReviewRequest;
import io.github.lxzcjt.scoresystem.dto.response.AppealVO;
import io.github.lxzcjt.scoresystem.entity.AppealProcess;
import io.github.lxzcjt.scoresystem.enums.AppealStatus;
import io.github.lxzcjt.scoresystem.service.AppealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 申诉接口（学生提交/取消，教师处理，教务审核）
 */
@Tag(name = "成绩申诉", description = "申诉全流程：学生提交 -> 教师处理 -> 教务审核")
@Validated
@RestController
@RequestMapping("/api/appeals")
@RequiredArgsConstructor
public class AppealController {

    private final AppealService appealService;

    @Operation(summary = "学生端：我的申诉列表")
    @GetMapping("/my")
    public Result<List<AppealVO>> listStudentAppeals(@Parameter(description = "学号") @RequestParam String studentId) {
        return Result.success(appealService.listStudentAppeals(studentId));
    }

    @Operation(summary = "教师端：分配给我的申诉", description = "pendingOnly=true 时只看待处理")
    @GetMapping("/teacher")
    public Result<PageResult<AppealVO>> listTeacherAppeals(
            @Parameter(description = "教师工号") @RequestParam String teacherId,
            @Parameter(description = "只看待处理") @RequestParam(defaultValue = "false") boolean pendingOnly,
            @Parameter(description = "页码，从1开始") @RequestParam(defaultValue = "1") @Min(1) int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {
        return Result.success(appealService.listTeacherAppeals(teacherId, pendingOnly, page, size));
    }

    @Operation(summary = "教务端：全部申诉", description = "支持按状态过滤")
    @GetMapping
    public Result<PageResult<AppealVO>> listAppeals(
            @Parameter(description = "申诉状态") @RequestParam(required = false) AppealStatus status,
            @Parameter(description = "页码，从1开始") @RequestParam(defaultValue = "1") @Min(1) int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {
        return Result.success(appealService.listAppeals(status, page, size));
    }

    @Operation(summary = "查询申诉详情")
    @GetMapping("/{appealId}")
    public Result<AppealVO> getAppeal(@Parameter(description = "申诉编号") @PathVariable String appealId) {
        return Result.success(appealService.getAppeal(appealId));
    }

    @Operation(summary = "查询申诉处理过程", description = "按时间正序的处理记录，用于时间线展示")
    @GetMapping("/{appealId}/processes")
    public Result<List<AppealProcess>> getAppealProcesses(
            @Parameter(description = "申诉编号") @PathVariable String appealId) {
        return Result.success(appealService.getAppealProcesses(appealId));
    }

    @Operation(summary = "学生端：发起申诉", description = "按课程安排自动分配授课教师处理")
    @PostMapping
    public Result<AppealVO> createAppeal(@Valid @RequestBody AppealCreateRequest request) {
        return Result.success("申诉提交成功，等待教师处理", appealService.createAppeal(request));
    }

    @Operation(summary = "学生端：取消申诉", description = "仅限本人且教师尚未处理")
    @PutMapping("/{appealId}/cancel")
    public Result<AppealVO> cancelAppeal(
            @Parameter(description = "申诉编号") @PathVariable String appealId,
            @Valid @RequestBody AppealCancelRequest request) {
        return Result.success("申诉已取消", appealService.cancelAppeal(appealId, request));
    }

    @Operation(summary = "教师端：处理申诉", description = "提交处理意见后流转至教务审核")
    @PutMapping("/{appealId}/process")
    public Result<AppealVO> processAppeal(
            @Parameter(description = "申诉编号") @PathVariable String appealId,
            @Valid @RequestBody AppealProcessRequest request) {
        return Result.success("处理成功，已提交教务审核", appealService.processAppeal(appealId, request));
    }

    @Operation(summary = "教务端：审核申诉", description = "给出最终审核结论")
    @PutMapping("/{appealId}/review")
    public Result<AppealVO> reviewAppeal(
            @Parameter(description = "申诉编号") @PathVariable String appealId,
            @Valid @RequestBody AppealReviewRequest request) {
        return Result.success("审核完成", appealService.reviewAppeal(appealId, request));
    }
}
