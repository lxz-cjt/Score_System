package io.github.lxzcjt.scoresystem.controller;

import io.github.lxzcjt.scoresystem.common.PageResult;
import io.github.lxzcjt.scoresystem.common.Result;
import io.github.lxzcjt.scoresystem.dto.request.StaffSaveRequest;
import io.github.lxzcjt.scoresystem.entity.AcademicAffairsStaff;
import io.github.lxzcjt.scoresystem.service.StaffService;
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
 * 教务人员管理接口（教务）
 */
@Tag(name = "教务人员管理", description = "教务人员信息的增删改查（教务侧）")
@Validated
@RestController
@RequestMapping("/api/staff")
@RequiredArgsConstructor
public class StaffController {

    private final StaffService staffService;

    @Operation(summary = "分页查询教务人员")
    @GetMapping
    public Result<PageResult<AcademicAffairsStaff>> listStaff(
            @Parameter(description = "姓名关键字") @RequestParam(required = false) String keyword,
            @Parameter(description = "页码，从1开始") @RequestParam(defaultValue = "1") @Min(1) int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {
        return Result.success(staffService.listStaff(keyword, page, size));
    }

    @Operation(summary = "查询教务人员详情")
    @GetMapping("/{staffId}")
    public Result<AcademicAffairsStaff> getStaff(@Parameter(description = "教务工号") @PathVariable String staffId) {
        return Result.success(staffService.getStaff(staffId));
    }

    @Operation(summary = "新增教务人员")
    @PostMapping
    public Result<AcademicAffairsStaff> createStaff(@Valid @RequestBody StaffSaveRequest request) {
        return Result.success("新增教务人员成功", staffService.createStaff(request));
    }

    @Operation(summary = "更新教务人员", description = "密码留空表示不修改密码")
    @PutMapping("/{staffId}")
    public Result<AcademicAffairsStaff> updateStaff(
            @Parameter(description = "教务工号") @PathVariable String staffId,
            @Valid @RequestBody StaffSaveRequest request) {
        return Result.success("更新教务人员成功", staffService.updateStaff(staffId, request));
    }

    @Operation(summary = "删除教务人员")
    @DeleteMapping("/{staffId}")
    public Result<Void> deleteStaff(@Parameter(description = "教务工号") @PathVariable String staffId) {
        staffService.deleteStaff(staffId);
        return Result.success();
    }
}
