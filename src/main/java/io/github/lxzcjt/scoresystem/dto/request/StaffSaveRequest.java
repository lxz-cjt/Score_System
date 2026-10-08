package io.github.lxzcjt.scoresystem.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 新增/更新教务人员请求。更新时密码留空表示不修改密码。
 */
@Schema(description = "教务人员保存请求")
public record StaffSaveRequest(

        @Schema(description = "教务工号", example = "AAS001")
        @NotBlank(message = "教务工号不能为空")
        @Size(max = 20, message = "教务工号长度不能超过20")
        String staffId,

        @Schema(description = "姓名", example = "王教务")
        @NotBlank(message = "姓名不能为空")
        @Size(max = 50, message = "姓名长度不能超过50")
        String staffName,

        @Schema(description = "初始密码（新增必填；更新时留空表示不修改）", example = "admin123")
        @Size(min = 6, max = 50, message = "密码长度需在6-50位之间")
        String password
) {
}
