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

    /**
     * 新增
     */
    boolean save(OutputCustomerNewEntity outputCustomerNew);

    /**
     * 编辑
     */
    boolean updateById(OutputCustomerNewEntity outputCustomerNew);

    /**
     * 禁用/启用
     */
    void disableOrEnable(OutputCustomerNewEntity reqVo);

    /**
     * 关键字查询
     */
    PageUtils search(Map<String, Object> params);
}

