package com.zzk.usermodule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 更新当前用户资料请求。
 */
@Data
@Schema(description = "更新用户资料请求")
public class UpdateUserProfileRequest {

    @NotBlank(message = "昵称不能为空")
    @Size(max = 50, message = "昵称长度不能超过50个字符")
    @Schema(description = "昵称", example = "小电视同学")
    private String nickname;

    @Size(max = 500, message = "头像地址长度不能超过500个字符")
    @Schema(description = "头像地址")
    private String avatarUrl;

    @Size(max = 500, message = "背景图地址长度不能超过500个字符")
    @Schema(description = "背景图地址")
    private String backgroundUrl;

    @Schema(description = "性别 0未知 1男 2女", example = "0")
    private Integer gender;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Schema(description = "生日", example = "2000-01-01")
    private LocalDate birthday;

    @Size(max = 255, message = "个性签名长度不能超过255个字符")
    @Schema(description = "个性签名")
    private String signature;

    @Size(max = 50, message = "省份长度不能超过50个字符")
    @Schema(description = "省份")
    private String province;

    @Size(max = 50, message = "城市长度不能超过50个字符")
    @Schema(description = "城市")
    private String city;
}
