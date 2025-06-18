package com.example.score_system1.dto;

/**
 * 登录响应DTO
 * 用于返回登录结果、令牌和用户信息
 */
public class LoginResponse {
    
    private String token; // 登录令牌
    private String message; // 响应消息
    private boolean success; // 登录是否成功
    private UserSimpleInfo user; // 用户简单信息

    // 无参构造函数
    public LoginResponse() {}

    // 带参构造函数
    public LoginResponse(boolean success, String message, String token, UserSimpleInfo user) {
        this.success = success;
        this.message = message;
        this.token = token;
        this.user = user;
    }

    // 静态工厂方法 - 成功响应
    public static LoginResponse success(String token, UserSimpleInfo user) {
        return new LoginResponse(true, "登录成功", token, user);
    }

    // 静态工厂方法 - 失败响应
    public static LoginResponse failure(String message) {
        return new LoginResponse(false, message, null, null);
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public UserSimpleInfo getUser() {
        return user;
    }

    public void setUser(UserSimpleInfo user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "token='" + (token != null ? "[PRESENT]" : "null") + '\'' +
                ", message='" + message + '\'' +
                ", success=" + success +
                ", user=" + user +
                '}';
    }
} 