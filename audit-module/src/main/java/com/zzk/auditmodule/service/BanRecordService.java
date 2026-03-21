package com.zzk.auditmodule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzk.auditmodule.dto.BanUserRequest;
import com.zzk.auditmodule.entity.BanRecord;
import com.zzk.auditmodule.vo.BanRecordVO;

/**
* @author 周振坤
* @description 针对表【ban_record(封禁记录表)】的数据库操作Service
* @createDate 2026-03-19 23:38:26
*/
public interface BanRecordService extends IService<BanRecord> {

    BanRecordVO banUser(Long uid, BanUserRequest request);
}
