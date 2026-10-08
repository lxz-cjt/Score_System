package io.github.lxzcjt.scoresystem.controller;

import io.github.lxzcjt.scoresystem.common.PageResult;
import io.github.lxzcjt.scoresystem.common.Result;
import io.github.lxzcjt.scoresystem.dto.request.CourseSaveRequest;
import io.github.lxzcjt.scoresystem.entity.Course;
import io.github.lxzcjt.scoresystem.service.CourseService;
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
 * 课程管理接口（教务）
 */
@Tag(name = "课程管理", description = "课程信息的增删改查（教务侧）")
@Validated
@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @Operation(summary = "分页查询课程", description = "支持按课程名称关键字搜索")
    @GetMapping
    public Result<PageResult<Course>> listCourses(
            @Parameter(description = "课程名称关键字") @RequestParam(required = false) String keyword,
            @Parameter(description = "页码，从1开始") @RequestParam(defaultValue = "1") @Min(1) int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {
        return Result.success(courseService.listCourses(keyword, page, size));
    }

    @Operation(summary = "查询课程详情")
    @GetMapping("/{courseId}")
    public Result<Course> getCourse(@Parameter(description = "课程编号") @PathVariable String courseId) {
        return Result.success(courseService.getCourse(courseId));
    }

    @Operation(summary = "新增课程")
    @PostMapping
    public Result<Course> createCourse(@Valid @RequestBody CourseSaveRequest request) {
        return Result.success("新增课程成功", courseService.createCourse(request));
    }

    @Operation(summary = "更新课程")
    @PutMapping("/{courseId}")
    public Result<Course> updateCourse(
            @Parameter(description = "课程编号") @PathVariable String courseId,
            @Valid @RequestBody CourseSaveRequest request) {
        return Result.success("更新课程成功", courseService.updateCourse(courseId, request));
    }

    @Operation(summary = "删除课程")
    @DeleteMapping("/{courseId}")
    public Result<Void> deleteCourse(@Parameter(description = "课程编号") @PathVariable String courseId) {
        courseService.deleteCourse(courseId);
        return Result.success();
    }
}
