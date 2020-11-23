package com.pwc.modules.output.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.output.entity.OutputInvoiceApplyEntity;

import java.util.Map;

/**
 * 开票申请服务
 *
 * @author fanpf
 * @date 2020/9/24
 */
public interface OutputInvoiceApplyService extends IService<OutputInvoiceApplyEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

