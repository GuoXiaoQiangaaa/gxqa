package com.pwc.modules.output.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.output.entity.OutputInvoiceRulesEntity;

import java.util.List;
import java.util.Map;

/**
 * 开票规则
 *
 * @author zk
 * @email 
 * @date 2020-06-01 18:17:06
 */
public interface OutputInvoiceRulesService extends IService<OutputInvoiceRulesEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 根据客户ID
     * @param customerId
     * @return
     */
    OutputInvoiceRulesEntity getByCustomerId(Long customerId);
}

