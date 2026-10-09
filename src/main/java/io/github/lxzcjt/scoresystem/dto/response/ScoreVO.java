package io.github.lxzcjt.scoresystem.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 成绩视图（关联学生姓名与课程名称）
 */
@Schema(description = "成绩信息")
public record ScoreVO(

        @Schema(description = "成绩编号")
        String scoreId,

        @Schema(description = "学号")
        String studentId,

        @Schema(description = "学生姓名")
        String studentName,

        @Schema(description = "课程编号")
        String courseId,

        @Schema(description = "课程名称")
        String courseName,

        @Schema(description = "课程学分")
        Short courseCredit,

        @Schema(description = "平时成绩")
        Integer dailyScore,

        @Schema(description = "考试成绩")
        Integer examScore,

        @Schema(description = "总评成绩")
        Integer totalScore,

        @Schema(description = "学分获得条件：优秀/通过/不通过")
        String creditGainCondition,

        @Schema(description = "是否需要补考")
        Boolean makeUpExam
) {
}
