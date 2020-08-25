package com.pwc.modules.input.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.input.entity.InputInvoiceCategoryEntity;

import java.util.Map;

/**
 * @author zk
 */
public interface InputInvoiceCategoryService extends IService<InputInvoiceCategoryEntity> {

    /**
     * 分页查询
     * @param params
     * @param invoiceCategoryEntity
     * @return
     */
    PageUtils queryPage(Map<String, Object> params);

}

