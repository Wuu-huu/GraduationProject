package com.zzk.videomodule.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.videomodule.entity.Tag;
import com.zzk.videomodule.service.TagService;
import com.zzk.videomodule.mapper.TagMapper;
import org.springframework.stereotype.Service;

/**
* @author 周振坤
* @description 针对表【tag(标签表)】的数据库操作Service实现
* @createDate 2026-03-19 23:32:28
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService{

}




