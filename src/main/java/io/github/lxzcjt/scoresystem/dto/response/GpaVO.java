package io.github.lxzcjt.scoresystem.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 学生成绩概览（平均分 + GPA）
 */
@Schema(description = "学生成绩概览")
public record GpaVO(

        @Schema(description = "学号")
        String studentId,

        @Schema(description = "成绩门数")
        Integer scoreCount,

        @Schema(description = "平均总评成绩")
        Double averageScore,

        @Schema(description = "学分绩点 GPA")
        Double gpa,

        @Schema(description = "已修总学分")
        Double totalCredits
) {
}
