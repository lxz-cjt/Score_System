package io.github.lxzcjt.scoresystem.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 成绩录入请求。总评成绩由服务端按 平时30% + 考试70% 自动计算。
 */
@Schema(description = "成绩录入请求")
public record ScoreSaveRequest(

        @Schema(description = "学号", example = "2021001")
        @NotBlank(message = "学号不能为空")
        String studentId,

        @Schema(description = "课程编号", example = "CS101")
        @NotBlank(message = "课程编号不能为空")
        String courseId,

        @Schema(description = "平时成绩（0-100）", example = "85")
        @NotNull(message = "平时成绩不能为空")
        @Min(value = 0, message = "平时成绩不能小于0")
        @Max(value = 100, message = "平时成绩不能大于100")
        Integer dailyScore,

        @Schema(description = "考试成绩（0-100）", example = "88")
        @NotNull(message = "考试成绩不能为空")
        @Min(value = 0, message = "考试成绩不能小于0")
        @Max(value = 100, message = "考试成绩不能大于100")
        Integer examScore,

        @Schema(description = "操作人工号（成绩日志留痕）", example = "T001")
        @NotBlank(message = "操作人不能为空")
        String operatorId
) {
}
