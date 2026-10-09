package io.github.lxzcjt.scoresystem.controller;

import io.github.lxzcjt.scoresystem.common.Result;
import io.github.lxzcjt.scoresystem.dto.request.LoginRequest;
import io.github.lxzcjt.scoresystem.dto.response.LoginVO;
import io.github.lxzcjt.scoresystem.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证接口
 */
@Tag(name = "认证", description = "用户登录认证")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "用户登录", description = "学生/教师/教务人员统一登录，返回令牌与用户信息")
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginRequest request) {
        return Result.success("登录成功", authService.login(request));
    }
}
