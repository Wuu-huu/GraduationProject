package com.zzk.usermodule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

/**
 * 用户资料响应。
 */
@Getter
@Builder
@Schema(description = "用户资料")
public class UserProfileVO {

    @Schema(description = "用户ID")
    private final Long uid;

    @Schema(description = "昵称")
    private final String nickname;

    @Schema(description = "头像地址")
    private final String avatarUrl;

    @Schema(description = "背景图地址")
    private final String backgroundUrl;

    @Schema(description = "性别")
    private final Integer gender;

    @Schema(description = "生日")
    private final LocalDate birthday;

    @Schema(description = "个性签名")
    private final String signature;

    @Schema(description = "省份")
    private final String province;

    @Schema(description = "城市")
    private final String city;

    @Schema(description = "认证类型")
    private final Integer authType;

    @Schema(description = "认证描述")
    private final String authDesc;
}
