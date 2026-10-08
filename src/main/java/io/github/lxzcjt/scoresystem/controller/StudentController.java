package io.github.lxzcjt.scoresystem.controller;

import io.github.lxzcjt.scoresystem.common.PageResult;
import io.github.lxzcjt.scoresystem.common.Result;
import io.github.lxzcjt.scoresystem.dto.request.StudentSaveRequest;
import io.github.lxzcjt.scoresystem.entity.Student;
import io.github.lxzcjt.scoresystem.service.StudentService;
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
 * 学生管理接口（教务）
 */
@Tag(name = "学生管理", description = "学生信息的增删改查（教务侧）")
@Validated
@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @Operation(summary = "分页查询学生", description = "支持按姓名/专业关键字搜索")
    @GetMapping
    public Result<PageResult<Student>> listStudents(
            @Parameter(description = "姓名/专业关键字") @RequestParam(required = false) String keyword,
            @Parameter(description = "页码，从1开始") @RequestParam(defaultValue = "1") @Min(1) int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {
        return Result.success(studentService.listStudents(keyword, page, size));
    }

    @Operation(summary = "查询学生详情")
    @GetMapping("/{studentId}")
    public Result<Student> getStudent(@Parameter(description = "学号") @PathVariable String studentId) {
        return Result.success(studentService.getStudent(studentId));
    }

    @Operation(summary = "新增学生")
    @PostMapping
    public Result<Student> createStudent(@Valid @RequestBody StudentSaveRequest request) {
        return Result.success("新增学生成功", studentService.createStudent(request));
    }

    @Operation(summary = "更新学生", description = "密码留空表示不修改密码")
    @PutMapping("/{studentId}")
    public Result<Student> updateStudent(
            @Parameter(description = "学号") @PathVariable String studentId,
            @Valid @RequestBody StudentSaveRequest request) {
        return Result.success("更新学生成功", studentService.updateStudent(studentId, request));
    }

    @Operation(summary = "删除学生")
    @DeleteMapping("/{studentId}")
    public Result<Void> deleteStudent(@Parameter(description = "学号") @PathVariable String studentId) {
        studentService.deleteStudent(studentId);
        return Result.success();
    }
}
