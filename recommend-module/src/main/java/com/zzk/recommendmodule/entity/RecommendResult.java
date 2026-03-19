package com.zzk.recommendmodule.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * @TableName recommend_result
 */
@TableName(value ="recommend_result")
@Data
public class RecommendResult {
    private Long id;

    private String requestId;

    private Long uid;

    private Long vid;

    private Integer positionNo;

    private String recallSource;

    private BigDecimal rankScore;

    private BigDecimal rerankScore;

    private BigDecimal finalScore;

    private Date createTime;
}