package com.zzk.recommendmodule.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * @TableName recommend_item_similarity
 */
@TableName(value ="recommend_item_similarity")
@Data
public class RecommendItemSimilarity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long vid;

    @TableField("related_vid")
    private Long relatedVid;

    private BigDecimal score;

    private String source;

    private Date updateTime;
}
