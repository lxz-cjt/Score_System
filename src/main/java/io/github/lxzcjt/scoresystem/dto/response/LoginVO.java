package io.github.lxzcjt.scoresystem.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 登录响应
 */
@Schema(description = "登录响应")
public record LoginVO(

        @Schema(description = "登录令牌")
        String token,

        @Schema(description = "用户信息")
        UserInfoVO user
) {
}
