package io.github.lxzcjt.scoresystem.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 新增/更新教师请求。更新时密码留空表示不修改密码。
 */
@Schema(description = "教师保存请求")
public record TeacherSaveRequest(

        @Schema(description = "教师工号", example = "T001")
        @NotBlank(message = "教师工号不能为空")
        @Size(max = 20, message = "教师工号长度不能超过20")
        String teacherId,

        @Schema(description = "姓名", example = "李教授")
        @NotBlank(message = "姓名不能为空")
        @Size(max = 50, message = "姓名长度不能超过50")
        String teacherName,

        @Schema(description = "所属学院", example = "计算机学院")
        @NotBlank(message = "所属学院不能为空")
        @Size(max = 100, message = "学院长度不能超过100")
        String college,

        @Schema(description = "初始密码（新增必填；更新时留空表示不修改）", example = "teacher123")
        @Size(min = 6, max = 50, message = "密码长度需在6-50位之间")
        String password
) {
}
