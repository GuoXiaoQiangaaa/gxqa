package com.pwc.modules.output.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.output.entity.OutputCustomerEntity;

import java.util.List;
import java.util.Map;

/**
 * 客户基本信息
 *
 * @author zk
 * @email 
 * @date 2020-06-01 18:17:06
 */
public interface OutputCustomerService extends IService<OutputCustomerEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<OutputCustomerEntity> getByDeptId(Long deptId, String customerName);
}

