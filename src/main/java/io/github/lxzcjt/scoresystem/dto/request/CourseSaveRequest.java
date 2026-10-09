package io.github.lxzcjt.scoresystem.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 新增/更新课程请求
 */
@Schema(description = "课程保存请求")
public record CourseSaveRequest(

        @Schema(description = "课程编号", example = "CS101")
        @NotBlank(message = "课程编号不能为空")
        @Size(max = 20, message = "课程编号长度不能超过20")
        String courseId,

        @Schema(description = "课程名称", example = "数据结构与算法")
        @NotBlank(message = "课程名称不能为空")
        @Size(max = 50, message = "课程名称长度不能超过50")
        String courseName,

        @Schema(description = "学分", example = "4")
        @NotNull(message = "学分不能为空")
        @Min(value = 1, message = "学分最小为1")
        @Max(value = 30, message = "学分最大为30")
        Short courseCredit
) {
}
