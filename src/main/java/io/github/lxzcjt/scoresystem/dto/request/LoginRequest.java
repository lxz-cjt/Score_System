package io.github.lxzcjt.scoresystem.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * 登录请求
 */
@Schema(description = "登录请求")
public record LoginRequest(

        @Schema(description = "用户名（学号/工号）", example = "2021001")
        @NotBlank(message = "用户名不能为空")
        String username,

        @Schema(description = "密码", example = "123456")
        @NotBlank(message = "密码不能为空")
        String password
) {
}
