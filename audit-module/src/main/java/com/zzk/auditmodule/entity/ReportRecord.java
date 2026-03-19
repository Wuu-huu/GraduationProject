package com.zzk.auditmodule.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * @TableName report_record
 */
@TableName(value ="report_record")
@Data
public class ReportRecord {
    private Long reportId;

    private Long reporterUid;

    private Integer targetType;

    private Long targetId;

    private Integer reasonType;

    private String reasonText;

    private Integer status;

    private Date createTime;

    private Date handleTime;
}