package io.github.lxzcjt.scoresystem.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 学生发起成绩申诉请求
 */
@Schema(description = "申诉创建请求")
public record AppealCreateRequest(

        @Schema(description = "学号", example = "2021001")
        @NotBlank(message = "学号不能为空")
        String studentId,

        @Schema(description = "课程编号", example = "CS101")
        @NotBlank(message = "课程编号不能为空")
        String courseId,

        @Schema(description = "申诉理由", example = "对我的总评成绩有异议，申请重新核查")
        @NotBlank(message = "申诉理由不能为空")
        @Size(max = 500, message = "申诉理由不能超过500字")
        String reason
) {
}
