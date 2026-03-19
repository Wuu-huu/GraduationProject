package com.zzk.recommendmodule.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * @TableName user_interest_profile
 */
@TableName(value ="user_interest_profile")
@Data
public class UserInterestProfile {
    private Long uid;

    private Object categoryWeights;

    private Object tagWeights;

    private Object creatorWeights;

    private Object activeHours;

    private BigDecimal freshnessPreference;

    private String durationPreference;

    private Date updateTime;
}