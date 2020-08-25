package com.pwc.modules.input.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;
import com.pwc.modules.input.dao.InputInvoiceCategoryDao;
import com.pwc.modules.input.entity.InputInvoiceCategoryEntity;
import com.pwc.modules.input.service.InputInvoiceCategoryService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("invoiceCategoryService")
public class InputInvoiceCategoryServiceImpl extends ServiceImpl<InputInvoiceCategoryDao, InputInvoiceCategoryEntity> implements InputInvoiceCategoryService  {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String category = (String) params.get("category");
        IPage<InputInvoiceCategoryEntity> page = this.page(
                new Query<InputInvoiceCategoryEntity>().getPage(params),
                new QueryWrapper<InputInvoiceCategoryEntity>().like(StrUtil.isNotBlank(category), "category", category)
        );

        return new PageUtils(page);
    }
}
