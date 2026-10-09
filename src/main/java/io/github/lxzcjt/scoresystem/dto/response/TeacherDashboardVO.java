package io.github.lxzcjt.scoresystem.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 教师 Dashboard
 */
@Schema(description = "教师 Dashboard")
public record TeacherDashboardVO(

        @Schema(description = "教师工号")
        String teacherId,

        @Schema(description = "姓名")
        String teacherName,

        @Schema(description = "所属学院")
        String college,

        @Schema(description = "授课数量")
        Integer courseCount,

        @Schema(description = "授课学生人次")
        Long studentCount,

        @Schema(description = "待处理申诉数")
        Long pendingAppealCount,

        @Schema(description = "各课程平均成绩")
        List<ChartItemVO> courseAverages
) {
}
