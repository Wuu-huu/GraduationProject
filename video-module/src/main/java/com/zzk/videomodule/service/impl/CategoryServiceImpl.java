package com.zzk.videomodule.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzk.videomodule.entity.Category;
import com.zzk.videomodule.service.CategoryService;
import com.zzk.videomodule.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

/**
* @author 周振坤
* @description 针对表【category(分区分类表)】的数据库操作Service实现
* @createDate 2026-03-19 23:32:03
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{

}




