package com.zzk.auditmodule.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * @TableName audit_task
 */
@TableName(value ="audit_task")
@Data
public class AuditTask {
    @TableId(value = "audit_id", type = IdType.AUTO)
    private Long auditId;

    private Integer bizType;

    private Long bizId;

    private Integer auditStatus;

    private String reason;

    private Long auditorUid;

    private Date submitTime;

    private Date auditTime;
}
