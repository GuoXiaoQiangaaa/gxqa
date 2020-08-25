package com.pwc.modules.data.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.data.entity.OutputCustomerNewEntity;

import java.util.Map;

/**
 * 客户信息服务
 *
 * @author fanpf
 * @date 2020/8/24
 */
public interface OutputCustomerNewService extends IService<OutputCustomerNewEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

