package com.example.score_system1.controller;

import com.example.score_system1.dto.ApiResponse;
import com.example.score_system1.dto.LoginRequest;
import com.example.score_system1.dto.LoginResponse;
import com.example.score_system1.dto.UserSimpleInfo;
import com.example.score_system1.enums.UserRole;
import com.example.score_system1.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录控制器
 * 处理用户登录、登出、角色验证等功能
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "用户认证", description = "用户登录认证相关接口")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "验证用户身份并返回登录信息")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse loginResponse = loginService.login(loginRequest);
            
            if (loginResponse != null && loginResponse.isSuccess()) {
                return ApiResponse.success("登录成功", loginResponse);
            } else {
                return ApiResponse.error("用户名或密码错误", 401);
            }
        } catch (Exception e) {
            return ApiResponse.error("登录失败: " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "用户登出", description = "用户退出登录")
    public ApiResponse<Void> logout(@RequestParam String userId) {
        try {
            // 这里可以添加登出逻辑，比如清除session、token等
            return ApiResponse.success("登出成功");
        } catch (Exception e) {
            return ApiResponse.error("登出失败: " + e.getMessage());
        }
    }

    @GetMapping("/verify")
    @Operation(summary = "验证用户身份", description = "验证用户是否有效")
    public ApiResponse<String> verifyUser(@RequestParam String userId, @RequestParam String role) {
        try {
            // 简单的用户验证逻辑
            return ApiResponse.success("用户验证功能待实现");
        } catch (Exception e) {
            return ApiResponse.error("用户验证失败: " + e.getMessage());
        }
    }

    @GetMapping("/roles")
    @Operation(summary = "获取可用角色", description = "获取系统中可用的用户角色列表")
    public ApiResponse<Map<String, String>> getRoles() {
        try {
            Map<String, String> roles = new HashMap<>();
            for (UserRole role : UserRole.values()) {
                roles.put(role.getCode(), role.getName());
            }
            return ApiResponse.success("获取角色列表成功", roles);
        } catch (Exception e) {
            return ApiResponse.error("获取角色列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/check-permissions")
    @Operation(summary = "检查用户权限", description = "检查用户是否有权限访问特定功能")
    public ApiResponse<Map<String, Boolean>> checkPermissions(@RequestBody Map<String, String> request) {
        try {
            String userId = request.get("userId");
            String role = request.get("role");
            String action = request.get("action");

            Map<String, Boolean> permissions = new HashMap<>();
            
            // 根据角色判断权限
            switch (role) {
                case "student":
                    permissions.put("view_scores", true);
                    permissions.put("submit_appeal", true);
                    permissions.put("manage_users", false);
                    permissions.put("manage_courses", false);
                    permissions.put("review_appeals", false);
                    break;
                case "teacher":
                    permissions.put("view_scores", true);
                    permissions.put("input_scores", true);
                    permissions.put("view_courses", true);
                    permissions.put("process_appeals", true);
                    permissions.put("manage_users", false);
                    permissions.put("manage_courses", false);
                    permissions.put("review_appeals", false);
                    break;
                case "staff":
                    permissions.put("view_scores", true);
                    permissions.put("input_scores", false);
                    permissions.put("manage_users", true);
                    permissions.put("manage_courses", true);
                    permissions.put("arrange_courses", true);
                    permissions.put("review_appeals", true);
                    permissions.put("view_statistics", true);
                    break;
                default:
                    permissions.put("access_denied", true);
            }

            return ApiResponse.success("权限检查完成", permissions);
        } catch (Exception e) {
            return ApiResponse.error("权限检查失败: " + e.getMessage());
        }
    }

    @GetMapping("/current-user/{userId}")
    @Operation(summary = "获取当前用户信息", description = "根据用户ID获取当前用户的详细信息")
    public ApiResponse<String> getCurrentUser(@PathVariable String userId) {
        try {
            // 简单的用户信息获取逻辑
            return ApiResponse.success("获取用户信息功能待实现");
        } catch (Exception e) {
            return ApiResponse.error("获取用户信息失败: " + e.getMessage());
        }
    }
}