package com.pwc.modules.output.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.output.entity.OutputInvoiceRulesGoodsEntity;

import java.util.List;
import java.util.Map;

/**
 * 开票规则商品关联
 *
 * @author zk
 * @email 
 * @date 2020-06-11 17:00:13
 */
public interface OutputInvoiceRulesGoodsService extends IService<OutputInvoiceRulesGoodsEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 根据开票规则查询特殊商品规则
     * @param rulesId
     * @return
     */
    List<OutputInvoiceRulesGoodsEntity> getByRulesId(Long rulesId);
}

