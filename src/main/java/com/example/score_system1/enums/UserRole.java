package com.example.score_system1.enums;

/**
 * 用户角色枚举
 */
public enum UserRole {
    STUDENT("student", "学生"),
    TEACHER("teacher", "教师"),
    ACADEMIC_AFFAIRS_STAFF("staff", "教务人员");

    private final String code;
    private final String name;

    UserRole(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static UserRole fromCode(String code) {
        for (UserRole role : UserRole.values()) {
            if (role.getCode().equals(code)) {
                return role;
            }
        }
        throw new IllegalArgumentException("未知的用户角色代码: " + code);
    }
} 