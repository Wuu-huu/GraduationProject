package com.zzk.recommendmodule.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * @TableName search_log
 */
@TableName(value ="search_log")
@Data
public class SearchLog {
    private Long id;

    private Long uid;

    private String keyword;

    private String searchScene;

    private Integer resultCount;

    private Long clickVid;

    private Date createTime;
}