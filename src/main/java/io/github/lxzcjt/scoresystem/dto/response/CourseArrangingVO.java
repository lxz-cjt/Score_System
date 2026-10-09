package io.github.lxzcjt.scoresystem.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 课程安排视图（关联课程名称与教师姓名）
 */
@Schema(description = "课程安排信息")
public record CourseArrangingVO(

        @Schema(description = "安排编号")
        String arrangingId,

        @Schema(description = "课程编号")
        String courseId,

        @Schema(description = "课程名称")
        String courseName,

        @Schema(description = "课程学分")
        Short courseCredit,

        @Schema(description = "教师工号")
        String teacherId,

        @Schema(description = "教师姓名")
        String teacherName,

        @Schema(description = "上课时间")
        String classTime,

        @Schema(description = "上课地点")
        String classroom
) {
}
