package com.example.score_system1.enums;

/**
 * 申诉状态枚举
 */
public enum AppealStatus {
    PENDING("待处理", "学生已提交申诉，等待教师处理"),
    TEACHER_PROCESSING("教师处理中", "教师正在处理申诉，准备提交材料"),
    SUBMITTED_TO_ADMIN("已提交教务", "教师已提交材料，等待教务人员审核"),
    APPROVED("已通过", "教务人员审核通过"),
    REJECTED("已拒绝", "教务人员审核拒绝"),
    CANCELLED("已取消", "学生主动取消申诉");

    private final String status;
    private final String description;

    AppealStatus(String status, String description) {
        this.status = status;
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public static AppealStatus fromStatus(String status) {
        for (AppealStatus appealStatus : AppealStatus.values()) {
            if (appealStatus.getStatus().equals(status)) {
                return appealStatus;
            }
        }
        throw new IllegalArgumentException("未知的申诉状态: " + status);
    }
} 