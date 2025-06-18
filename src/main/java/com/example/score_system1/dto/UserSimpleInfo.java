package com.example.score_system1.dto;

/**
 * 用户简单信息DTO
 * 用于在登录响应中返回用户的基本信息
 */
public class UserSimpleInfo {
    
    private String role; // 用户角色：admin、teacher、student
    private String name; // 用户姓名
    private String type; // 用户类型：教师、学生、管理员
    private String id;

    // 无参构造函数
    public UserSimpleInfo() {}

    // 带参构造函数
    public UserSimpleInfo(String role, String name, String type) {
        this.role = role;
        this.name = name;
        this.type = type;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "UserSimpleInfo{" +
                "role='" + role + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}