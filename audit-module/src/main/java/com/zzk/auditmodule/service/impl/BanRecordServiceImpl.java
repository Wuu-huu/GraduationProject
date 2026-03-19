package com.zzk.auditmodule.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.auditmodule.entity.BanRecord;
import com.zzk.auditmodule.service.BanRecordService;
import com.zzk.auditmodule.mapper.BanRecordMapper;
import org.springframework.stereotype.Service;

/**
* @author 周振坤
* @description 针对表【ban_record(封禁记录表)】的数据库操作Service实现
* @createDate 2026-03-19 23:38:26
*/
@Service
public class BanRecordServiceImpl extends ServiceImpl<BanRecordMapper, BanRecord>
    implements BanRecordService{

}




