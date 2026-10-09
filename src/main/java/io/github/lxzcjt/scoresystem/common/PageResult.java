package io.github.lxzcjt.scoresystem.common;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

/**
 * 分页结果封装
 *
 * @param records 当前页数据
 * @param total   总记录数
 * @param page    当前页码（从 1 开始）
 * @param size    每页大小
 */
@Schema(description = "分页结果")
public record PageResult<T>(
        @Schema(description = "当前页数据") List<T> records,
        @Schema(description = "总记录数", example = "100") long total,
        @Schema(description = "当前页码（从 1 开始）", example = "1") int page,
        @Schema(description = "每页大小", example = "10") int size) {

    /**
     * 由 Spring Data 的 Page（页码从 0 开始）构建 PageResult（页码从 1 开始）
     */
    public static <T> PageResult<T> of(Page<T> page) {
        return new PageResult<>(page.getContent(), page.getTotalElements(),
                page.getNumber() + 1, page.getSize());
    }

    /**
     * 构建并映射元素类型（Entity -> VO）
     */
    public static <E, T> PageResult<T> of(Page<E> page, Function<E, T> mapper) {
        return new PageResult<>(page.getContent().stream().map(mapper).toList(),
                page.getTotalElements(), page.getNumber() + 1, page.getSize());
    }

    /**
     * 以已完成映射的内容构建（适用于需要批量组装 VO 的场景）
     */
    public static <T> PageResult<T> of(Page<?> page, List<T> mappedContent) {
        return new PageResult<>(mappedContent, page.getTotalElements(),
                page.getNumber() + 1, page.getSize());
    }
}
