package io.github.lxzcjt.scoresystem.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 图表数据项（ECharts 直接可用）
 */
@Schema(description = "图表数据项")
public record ChartItemVO(

        @Schema(description = "名称", example = "90-100分")
        String name,

        @Schema(description = "数值", example = "12")
        Object value
) {
}
