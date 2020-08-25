package com.pwc.modules.input.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.input.entity.InputInvoiceRefundEntity;

import java.util.Map;

/**
 * 发票退票表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2018-12-26 16:58:47
 */
public interface InputInvoiceRefundService extends IService<InputInvoiceRefundEntity> {
    PageUtils queryPage(Map<String, Object> params);
}

