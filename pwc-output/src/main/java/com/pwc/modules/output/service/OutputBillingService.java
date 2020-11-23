package com.pwc.modules.output.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.output.entity.OutputBillingEntity;

import java.util.Map;

/**
 * 销项Billing服务
 *
 * @author fanpf
 * @date 2020/9/25
 */
public interface OutputBillingService extends IService<OutputBillingEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

