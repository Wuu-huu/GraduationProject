package com.zzk.recommendmodule.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.recommendmodule.entity.SearchLog;
import com.zzk.recommendmodule.service.SearchLogService;
import com.zzk.recommendmodule.mapper.SearchLogMapper;
import org.springframework.stereotype.Service;

/**
* @author 周振坤
* @description 针对表【search_log(搜索日志表)】的数据库操作Service实现
* @createDate 2026-03-19 23:39:09
*/
@Service
public class SearchLogServiceImpl extends ServiceImpl<SearchLogMapper, SearchLog>
    implements SearchLogService{

}




