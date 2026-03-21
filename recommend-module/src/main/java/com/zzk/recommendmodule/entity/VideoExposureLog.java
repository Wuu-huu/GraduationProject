package com.zzk.recommendmodule.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * @TableName video_exposure_log
 */
@TableName(value ="video_exposure_log")
@Data
public class VideoExposureLog {
    @TableId(value = "exposure_id", type = IdType.AUTO)
    private Long exposureId;

    private String requestId;

    private Long uid;

    private Long vid;

    private String scene;

    private Integer positionNo;

    private String recallSource;

    private BigDecimal score;

    private Integer isClicked;

    private Integer isPlayed;

    private Date exposeTime;

    private Date clickTime;
}
