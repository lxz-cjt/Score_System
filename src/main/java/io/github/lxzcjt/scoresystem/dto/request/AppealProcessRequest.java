package io.github.lxzcjt.scoresystem.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 教师处理申诉请求
 */
@Schema(description = "教师处理申诉请求")
public record AppealProcessRequest(

        @Schema(description = "教师工号", example = "T001")
        @NotBlank(message = "教师工号不能为空")
        String teacherId,

        @Schema(description = "处理意见", example = "已重新核对试卷，成绩无误")
        @NotBlank(message = "处理意见不能为空")
        @Size(max = 1000, message = "处理意见不能超过1000字")
        String opinion
) {
}
