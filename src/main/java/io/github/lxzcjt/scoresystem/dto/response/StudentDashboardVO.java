package io.github.lxzcjt.scoresystem.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 学生 Dashboard
 */
@Schema(description = "学生 Dashboard")
public record StudentDashboardVO(

        @Schema(description = "学号")
        String studentId,

        @Schema(description = "姓名")
        String studentName,

        @Schema(description = "专业")
        String major,

        @Schema(description = "已获得学分")
        Integer getCredit,

        @Schema(description = "成绩门数")
        Integer scoreCount,

        @Schema(description = "平均总评成绩")
        Double averageScore,

        @Schema(description = "学分绩点 GPA")
        Double gpa,

        @Schema(description = "各状态申诉数量")
        List<ChartItemVO> appealStatusCount
) {
}
