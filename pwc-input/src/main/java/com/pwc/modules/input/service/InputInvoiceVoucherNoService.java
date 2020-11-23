package com.pwc.modules.input.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.input.entity.InputInvoiceVoucherNoEntity;

import java.util.Map;

/**
 * 发票入账 凭证号和发票对应表
 *
 * @author zlb
 * @email 
 * @date 2020-08-19 12:08:28
 */
public interface InputInvoiceVoucherNoService extends IService<InputInvoiceVoucherNoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

