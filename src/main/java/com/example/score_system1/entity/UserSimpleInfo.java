package com.example.score_system1.entity;

public class UserSimpleInfo {
    public String role; // admin teacher student
    public String name;
    public String type; // 教师 学生

    public UserSimpleInfo(String role, String name, String type) {
        this.role = role;
        this.name = name;
        this.type = type;
    }
}
