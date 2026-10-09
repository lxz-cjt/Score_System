package io.github.lxzcjt.scoresystem.enums;

import lombok.Getter;

/**
 * 用户角色
 */
@Getter
public enum UserRole {

    STUDENT("student", "学生"),
    TEACHER("teacher", "教师"),
    STAFF("staff", "教务人员");

    private final String code;
    private final String label;

    UserRole(String code, String label) {
        this.code = code;
        this.label = label;
    }
}
