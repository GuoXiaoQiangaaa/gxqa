package com.pwc.modules.output.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.output.entity.OutputCustomerRulesEntity;

import java.util.List;
import java.util.Map;

/**
 * 客户规则关联
 *
 * @author zk
 * @email 
 * @date 2020-06-11 15:41:24
 */
public interface OutputCustomerRulesService extends IService<OutputCustomerRulesEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 删除客户以应用规则
     * @param customerIds
     * @return
     */
    boolean delByCustomerIds(List<Long> customerIds);
}

