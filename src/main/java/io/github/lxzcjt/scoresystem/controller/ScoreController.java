package io.github.lxzcjt.scoresystem.controller;

import io.github.lxzcjt.scoresystem.common.PageResult;
import io.github.lxzcjt.scoresystem.common.Result;
import io.github.lxzcjt.scoresystem.dto.request.ScoreSaveRequest;
import io.github.lxzcjt.scoresystem.dto.request.ScoreUpdateRequest;
import io.github.lxzcjt.scoresystem.dto.response.GpaVO;
import io.github.lxzcjt.scoresystem.dto.response.ScoreVO;
import io.github.lxzcjt.scoresystem.service.ScoreService;
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
 * 成绩接口（教师录入/修改，学生查询）
 */
@Tag(name = "成绩管理", description = "成绩的录入、修改、查询，总评由服务端自动计算")
@Validated
@RestController
@RequestMapping("/api/scores")
@RequiredArgsConstructor
public class ScoreController {

    private final ScoreService scoreService;

    @Operation(summary = "分页查询成绩", description = "支持按学号/课程编号组合过滤")
    @GetMapping
    public Result<PageResult<ScoreVO>> listScores(
            @Parameter(description = "学号") @RequestParam(required = false) String studentId,
            @Parameter(description = "课程编号") @RequestParam(required = false) String courseId,
            @Parameter(description = "页码，从1开始") @RequestParam(defaultValue = "1") @Min(1) int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {
        return Result.success(scoreService.listScores(studentId, courseId, page, size));
    }

    @Operation(summary = "学生端：我的成绩", description = "查询指定学生的全部成绩")
    @GetMapping("/student/{studentId}")
    public Result<List<ScoreVO>> listStudentScores(
            @Parameter(description = "学号") @PathVariable String studentId) {
        return Result.success(scoreService.listStudentScores(studentId));
    }

    @Operation(summary = "学生端：成绩概览", description = "平均分 + GPA + 已修学分")
    @GetMapping("/student/{studentId}/summary")
    public Result<GpaVO> getStudentSummary(@Parameter(description = "学号") @PathVariable String studentId) {
        return Result.success(scoreService.getStudentScoreSummary(studentId));
    }

    @Operation(summary = "查询成绩详情")
    @GetMapping("/{scoreId}")
    public Result<ScoreVO> getScore(@Parameter(description = "成绩编号") @PathVariable String scoreId) {
        return Result.success(scoreService.getScore(scoreId));
    }

    @Operation(summary = "录入成绩", description = "总评成绩=平时30%+考试70%，服务端自动计算")
    @PostMapping
    public Result<ScoreVO> createScore(@Valid @RequestBody ScoreSaveRequest request) {
        return Result.success("成绩录入成功", scoreService.createScore(request));
    }

    @Operation(summary = "修改成绩", description = "仅允许修改分数，总评自动重算并记录日志")
    @PutMapping("/{scoreId}")
    public Result<ScoreVO> updateScore(
            @Parameter(description = "成绩编号") @PathVariable String scoreId,
            @Valid @RequestBody ScoreUpdateRequest request) {
        return Result.success("成绩修改成功", scoreService.updateScore(scoreId, request));
    }

    @Operation(summary = "删除成绩")
    @DeleteMapping("/{scoreId}")
    public Result<Void> deleteScore(
            @Parameter(description = "成绩编号") @PathVariable String scoreId,
            @Parameter(description = "操作人工号") @RequestParam String operatorId) {
        scoreService.deleteScore(scoreId, operatorId);
        return Result.success();
    }
}
