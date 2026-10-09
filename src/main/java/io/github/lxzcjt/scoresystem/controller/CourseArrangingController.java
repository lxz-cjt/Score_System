package io.github.lxzcjt.scoresystem.controller;

import io.github.lxzcjt.scoresystem.common.PageResult;
import io.github.lxzcjt.scoresystem.common.Result;
import io.github.lxzcjt.scoresystem.dto.request.CourseArrangingSaveRequest;
import io.github.lxzcjt.scoresystem.dto.response.CourseArrangingVO;
import io.github.lxzcjt.scoresystem.service.CourseArrangingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
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
 * 课程安排接口（教务排课 / 教师查课）
 */
@Tag(name = "课程安排", description = "课程安排（排课）管理")
@Validated
@RestController
@RequestMapping("/api/course-arrangings")
@RequiredArgsConstructor
public class CourseArrangingController {

    private final CourseArrangingService arrangingService;

    @Operation(summary = "分页查询课程安排", description = "支持按课程编号或教师工号过滤")
    @GetMapping
    public Result<PageResult<CourseArrangingVO>> listArrangements(
            @Parameter(description = "课程编号") @RequestParam(required = false) String courseId,
            @Parameter(description = "教师工号") @RequestParam(required = false) String teacherId,
            @Parameter(description = "页码，从1开始") @RequestParam(defaultValue = "1") @Min(1) int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {
        return Result.success(arrangingService.listArrangements(courseId, teacherId, page, size));
    }

    @Operation(summary = "教师端：我的课程", description = "查询指定教师的所有课程安排")
    @GetMapping("/teacher/{teacherId}")
    public Result<List<CourseArrangingVO>> listByTeacher(
            @Parameter(description = "教师工号") @PathVariable String teacherId) {
        return Result.success(arrangingService.listByTeacher(teacherId));
    }

    @Operation(summary = "新增课程安排")
    @PostMapping
    public Result<CourseArrangingVO> createArrangement(@Valid @RequestBody CourseArrangingSaveRequest request) {
        return Result.success("新增课程安排成功", arrangingService.createArrangement(request));
    }

    @Operation(summary = "更新课程安排")
    @PutMapping("/{arrangingId}")
    public Result<CourseArrangingVO> updateArrangement(
            @Parameter(description = "安排编号") @PathVariable String arrangingId,
            @Valid @RequestBody CourseArrangingSaveRequest request) {
        return Result.success("更新课程安排成功", arrangingService.updateArrangement(arrangingId, request));
    }

    @Operation(summary = "删除课程安排")
    @DeleteMapping("/{arrangingId}")
    public Result<Void> deleteArrangement(@Parameter(description = "安排编号") @PathVariable String arrangingId) {
        arrangingService.deleteArrangement(arrangingId);
        return Result.success();
    }
}
