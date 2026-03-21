package com.zzk.common.model.page;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

/**
 * 统一分页响应体。
 */
@Getter
@Builder
public class PageResponse<T> {

    private final List<T> records;
    private final long total;
    private final long pageNum;
    private final long pageSize;
}
