package io.github.lxzcjt.scoresystem.controller;

import io.github.lxzcjt.scoresystem.common.PageResult;
import io.github.lxzcjt.scoresystem.common.Result;
import io.github.lxzcjt.scoresystem.entity.ScoreLog;
import io.github.lxzcjt.scoresystem.service.ScoreLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 成绩日志接口
 */
@Tag(name = "成绩日志", description = "成绩变更历史查询")
@Validated
@RestController
@RequestMapping("/api/score-logs")
@RequiredArgsConstructor
public class ScoreLogController {

    private final ScoreLogService scoreLogService;

    @Operation(summary = "分页查询成绩日志", description = "支持按成绩编号或操作人工号过滤")
    @GetMapping
    public Result<PageResult<ScoreLog>> listLogs(
            @Parameter(description = "成绩编号") @RequestParam(required = false) String scoreId,
            @Parameter(description = "操作人工号") @RequestParam(required = false) String operatorId,
            @Parameter(description = "页码，从1开始") @RequestParam(defaultValue = "1") @Min(1) int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {
        return Result.success(scoreLogService.listLogs(scoreId, operatorId, page, size));
    }
}
