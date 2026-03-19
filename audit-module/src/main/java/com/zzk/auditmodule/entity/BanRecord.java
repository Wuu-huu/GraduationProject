package com.zzk.auditmodule.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * @TableName ban_record
 */
@TableName(value ="ban_record")
@Data
public class BanRecord {
    private Long id;

    private Long uid;

    private Integer banType;

    private Date startTime;

    private Date endTime;

    private String reason;

    private Long operatorUid;

    private Integer status;

    private Date createTime;
}