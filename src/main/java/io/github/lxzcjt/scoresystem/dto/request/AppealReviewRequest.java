package io.github.lxzcjt.scoresystem.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 教务审核申诉请求
 */
@Schema(description = "教务审核申诉请求")
public record AppealReviewRequest(

        @Schema(description = "教务工号", example = "AAS001")
        @NotBlank(message = "教务工号不能为空")
        String staffId,

        @Schema(description = "是否通过", example = "true")
        @NotNull(message = "审核结论不能为空")
        Boolean approved,

        @Schema(description = "审核意见", example = "同意教师复核结果")
        @NotBlank(message = "审核意见不能为空")
        @Size(max = 500, message = "审核意见不能超过500字")
        String opinion
) {
}
