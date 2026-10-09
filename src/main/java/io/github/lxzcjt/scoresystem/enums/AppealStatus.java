package io.github.lxzcjt.scoresystem.enums;

import lombok.Getter;

/**
 * 申诉状态：学生提交 -> 教师处理 -> 教务审核（通过/拒绝），学生可主动取消
 */
@Getter
public enum AppealStatus {

    PENDING("待处理", "学生已提交申诉，等待教师处理"),
    SUBMITTED_TO_ADMIN("待教务审核", "教师已提交处理意见，等待教务人员审核"),
    APPROVED("已通过", "教务人员审核通过"),
    REJECTED("已拒绝", "教务人员审核拒绝"),
    CANCELLED("已取消", "学生主动取消申诉");

    private final String label;
    private final String description;

    AppealStatus(String label, String description) {
        this.label = label;
        this.description = description;
    }
}
