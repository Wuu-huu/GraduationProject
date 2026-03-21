package com.zzk.usermodule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 关注列表分页查询参数。
 */
@Data
@Schema(description = "关注列表分页查询")
public class UserFollowPageQuery {

    @Min(value = 1, message = "页码必须大于等于1")
    @Schema(description = "页码", example = "1", defaultValue = "1")
    private long pageNum = 1;

    @Min(value = 1, message = "每页数量必须大于等于1")
    @Schema(description = "每页数量", example = "10", defaultValue = "10")
    private long pageSize = 10;
}
