package io.github.lxzcjt.scoresystem.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 登录用户信息
 */
@Schema(description = "登录用户信息")
public record UserInfoVO(

        @Schema(description = "用户编号（学号/工号）", example = "2021001")
        String id,

        @Schema(description = "姓名", example = "张三")
        String name,

        @Schema(description = "角色代码：student/teacher/staff", example = "student")
        String role,

        @Schema(description = "角色名称", example = "学生")
        String roleName
) {
}
