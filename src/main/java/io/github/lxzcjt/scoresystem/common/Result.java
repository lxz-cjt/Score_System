package io.github.lxzcjt.scoresystem.common;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 统一 API 响应体
 *
 * @param code    业务状态码，200 表示成功，其余与 HTTP 状态码对齐
 * @param message 提示信息
 * @param data    业务数据
 */
@Schema(description = "统一响应体")
public record Result<T>(
        @Schema(description = "业务状态码", example = "200") int code,
        @Schema(description = "提示信息", example = "操作成功") String message,
        @Schema(description = "业务数据") T data) {

    public static <T> Result<T> success(T data) {
        return new Result<>(ErrorCode.SUCCESS.getCode(), "操作成功", data);
    }

    public static <T> Result<T> success(String message, T data) {
        return new Result<>(ErrorCode.SUCCESS.getCode(), message, data);
    }

    public static Result<Void> success() {
        return new Result<>(ErrorCode.SUCCESS.getCode(), "操作成功", null);
    }

    public static <T> Result<T> error(ErrorCode errorCode, String message) {
        return new Result<>(errorCode.getCode(), message, null);
    }
}
