package com.pwc.modules.output.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.output.entity.OutputInvoiceEntity;

import java.util.Map;

/**
 * 销项发票服务
 *
 * @author fanpf
 * @date 2020/9/24
 */
public interface OutputInvoiceService extends IService<OutputInvoiceEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

