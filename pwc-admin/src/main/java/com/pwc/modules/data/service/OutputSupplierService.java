package com.pwc.modules.data.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.data.entity.OutputSupplierEntity;

import java.util.Map;

/**
 * 供应商信息服务
 *
 * @author fanpf
 * @date 2020/8/24
 */
public interface OutputSupplierService extends IService<OutputSupplierEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

