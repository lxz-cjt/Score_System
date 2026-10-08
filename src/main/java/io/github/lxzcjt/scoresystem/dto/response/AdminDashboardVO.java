package io.github.lxzcjt.scoresystem.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 教务 Dashboard（统计图表数据）
 */
@Schema(description = "教务 Dashboard")
public record AdminDashboardVO(

        @Schema(description = "学生总数")
        Long totalStudents,

        @Schema(description = "教师总数")
        Long totalTeachers,

        @Schema(description = "课程总数")
        Long totalCourses,

        @Schema(description = "成绩记录总数")
        Long totalScores,

        @Schema(description = "申诉总数")
        Long totalAppeals,

        @Schema(description = "待审核申诉数")
        Long pendingReviewCount,

        @Schema(description = "各状态申诉数量")
        List<ChartItemVO> appealStatusCount,

        @Schema(description = "成绩分数段分布")
        List<ChartItemVO> scoreDistribution,

        @Schema(description = "各课程平均成绩")
        List<ChartItemVO> courseAverages
) {
}
