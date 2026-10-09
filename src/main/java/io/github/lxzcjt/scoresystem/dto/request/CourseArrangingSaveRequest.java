package io.github.lxzcjt.scoresystem.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 新增/更新课程安排请求，安排编号由服务端生成
 */
@Schema(description = "课程安排保存请求")
public record CourseArrangingSaveRequest(

        @Schema(description = "课程编号", example = "CS101")
        @NotBlank(message = "课程编号不能为空")
        String courseId,

        @Schema(description = "教师工号", example = "T001")
        @NotBlank(message = "教师工号不能为空")
        String teacherId,

        @Schema(description = "上课时间", example = "周一 1-2节, 周三 3-4节")
        @NotBlank(message = "上课时间不能为空")
        @Size(max = 50, message = "上课时间长度不能超过50")
        String classTime,

        @Schema(description = "上课地点", example = "计算机楼101")
        @NotBlank(message = "上课地点不能为空")
        @Size(max = 50, message = "上课地点长度不能超过50")
        String classroom
) {
}
