package io.github.lxzcjt.scoresystem.controller;

import io.github.lxzcjt.scoresystem.common.PageResult;
import io.github.lxzcjt.scoresystem.common.Result;
import io.github.lxzcjt.scoresystem.dto.request.TeacherSaveRequest;
import io.github.lxzcjt.scoresystem.entity.Teacher;
import io.github.lxzcjt.scoresystem.service.TeacherService;
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

/**
 * 教师管理接口（教务）
 */
@Tag(name = "教师管理", description = "教师信息的增删改查（教务侧）")
@Validated
@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @Operation(summary = "分页查询教师", description = "支持按姓名/学院关键字搜索")
    @GetMapping
    public Result<PageResult<Teacher>> listTeachers(
            @Parameter(description = "姓名/学院关键字") @RequestParam(required = false) String keyword,
            @Parameter(description = "页码，从1开始") @RequestParam(defaultValue = "1") @Min(1) int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {
        return Result.success(teacherService.listTeachers(keyword, page, size));
    }

    @Operation(summary = "查询教师详情")
    @GetMapping("/{teacherId}")
    public Result<Teacher> getTeacher(@Parameter(description = "教师工号") @PathVariable String teacherId) {
        return Result.success(teacherService.getTeacher(teacherId));
    }

    @Operation(summary = "新增教师")
    @PostMapping
    public Result<Teacher> createTeacher(@Valid @RequestBody TeacherSaveRequest request) {
        return Result.success("新增教师成功", teacherService.createTeacher(request));
    }

    @Operation(summary = "更新教师", description = "密码留空表示不修改密码")
    @PutMapping("/{teacherId}")
    public Result<Teacher> updateTeacher(
            @Parameter(description = "教师工号") @PathVariable String teacherId,
            @Valid @RequestBody TeacherSaveRequest request) {
        return Result.success("更新教师成功", teacherService.updateTeacher(teacherId, request));
    }

    @Operation(summary = "删除教师")
    @DeleteMapping("/{teacherId}")
    public Result<Void> deleteTeacher(@Parameter(description = "教师工号") @PathVariable String teacherId) {
        teacherService.deleteTeacher(teacherId);
        return Result.success();
    }
}
