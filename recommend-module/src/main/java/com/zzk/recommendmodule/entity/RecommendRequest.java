package com.zzk.recommendmodule.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * @TableName recommend_request
 */
@TableName(value ="recommend_request")
@Data
public class RecommendRequest {
    private String requestId;

    private Long uid;

    private String scene;

    private Integer pageNo;

    private String deviceType;

    private Date requestTime;
}