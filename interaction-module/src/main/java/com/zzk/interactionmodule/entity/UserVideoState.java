package com.zzk.interactionmodule.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * @TableName user_video_state
 */
@TableName(value ="user_video_state")
@Data
public class UserVideoState {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long uid;

    private Long vid;

    private Integer liked;

    private Integer disliked;

    private Integer coinCount;

    private Integer favorited;

    private Integer watchLater;

    private Date lastPlayTime;

    private Integer lastPlayProgressSec;

    private Integer totalWatchSec;

    private Integer playTimes;
}
