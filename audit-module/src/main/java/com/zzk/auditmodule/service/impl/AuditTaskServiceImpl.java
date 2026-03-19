package com.zzk.auditmodule.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.auditmodule.entity.AuditTask;
import com.zzk.auditmodule.service.AuditTaskService;
import com.zzk.auditmodule.mapper.AuditTaskMapper;
import org.springframework.stereotype.Service;

/**
* @author 周振坤
* @description 针对表【audit_task(审核任务表)】的数据库操作Service实现
* @createDate 2026-03-19 23:37:42
*/
@Service
public class AuditTaskServiceImpl extends ServiceImpl<AuditTaskMapper, AuditTask>
    implements AuditTaskService{

}




