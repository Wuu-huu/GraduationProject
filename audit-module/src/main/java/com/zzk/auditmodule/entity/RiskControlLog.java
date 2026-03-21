package com.zzk.auditmodule.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * @TableName risk_control_log
 */
@TableName(value ="risk_control_log")
@Data
public class RiskControlLog {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long uid;

    private String riskType;

    private Integer bizType;

    private Long bizId;

    private String detail;

    private Integer riskLevel;

    private Date createTime;
}
