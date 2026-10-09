package io.github.lxzcjt.scoresystem.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 新增/更新学生请求。更新时密码留空表示不修改密码。
 */
@Schema(description = "学生保存请求")
public record StudentSaveRequest(

        @Schema(description = "学号", example = "2021001")
        @NotBlank(message = "学号不能为空")
        @Size(max = 20, message = "学号长度不能超过20")
        String studentId,

        @Schema(description = "姓名", example = "张三")
        @NotBlank(message = "姓名不能为空")
        @Size(max = 50, message = "姓名长度不能超过50")
        String studentName,

        @Schema(description = "专业", example = "计算机科学与技术")
        @NotBlank(message = "专业不能为空")
        @Size(max = 100, message = "专业长度不能超过100")
        String major,

        @Schema(description = "已获得学分", example = "45")
        @NotNull(message = "已获得学分不能为空")
        @Min(value = 0, message = "已获得学分不能小于0")
        @Max(value = 300, message = "已获得学分不能大于300")
        Integer getCredit,

        @Schema(description = "初始密码（新增必填；更新时留空表示不修改）", example = "123456")
        @Size(min = 6, max = 50, message = "密码长度需在6-50位之间")
        String password
) {
}
