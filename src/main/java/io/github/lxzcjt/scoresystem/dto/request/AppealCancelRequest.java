package io.github.lxzcjt.scoresystem.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 学生取消申诉请求
 */
@Schema(description = "取消申诉请求")
public record AppealCancelRequest(

        @Schema(description = "学号", example = "2021001")
        @NotBlank(message = "学号不能为空")
        String studentId,

        @Schema(description = "取消原因", example = "已与教师沟通，无需继续申诉")
        @NotBlank(message = "取消原因不能为空")
        @Size(max = 500, message = "取消原因不能超过500字")
        String reason
) {
}
