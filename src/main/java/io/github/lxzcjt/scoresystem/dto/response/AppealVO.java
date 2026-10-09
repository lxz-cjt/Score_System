package io.github.lxzcjt.scoresystem.dto.response;

import io.github.lxzcjt.scoresystem.enums.AppealStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

/**
 * 申诉视图（关联学生/课程/教师名称）
 */
@Schema(description = "申诉信息")
public record AppealVO(

        @Schema(description = "申诉编号")
        String appealId,

        @Schema(description = "学号")
        String studentId,

        @Schema(description = "学生姓名")
        String studentName,

        @Schema(description = "课程编号")
        String courseId,

        @Schema(description = "课程名称")
        String courseName,

        @Schema(description = "处理教师工号")
        String teacherId,

        @Schema(description = "处理教师姓名")
        String teacherName,

        @Schema(description = "申诉理由")
        String appealReason,

        @Schema(description = "申诉时间")
        Date appealTime,

        @Schema(description = "申诉状态")
        AppealStatus appealStatus,

        @Schema(description = "申诉状态中文名")
        String statusLabel,

        @Schema(description = "申诉结果说明")
        String appealResult,

        @Schema(description = "审核完成时间")
        Date resultTime
) {
}
