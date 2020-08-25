package com.pwc.modules.input.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;
import com.pwc.modules.input.dao.InputInvoiceConditionMapDao;
import com.pwc.modules.input.entity.InputInvoiceConditionMap;
import com.pwc.modules.input.entity.InputInvoiceMaterialSapEntity;
import com.pwc.modules.input.service.InputInvoiceConditionMapService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("InvoiceConditionMapServiceImpl")
public class InputInvoiceConditionMapServiceImpl extends ServiceImpl<InputInvoiceConditionMapDao, InputInvoiceConditionMap> implements InputInvoiceConditionMapService {
    @Override
    public InputInvoiceConditionMap getMaxConditionCode(InputInvoiceMaterialSapEntity invoiceMaterialSapEntity) {
        return this.baseMapper.getMaxConditionCode(invoiceMaterialSapEntity);
    }

    @Override
    public List<InputInvoiceConditionMap> getAllList() {
        return this.baseMapper.getAllList();
    }

    @Override
    public PageUtils getList(Map<String, Object> params) {
        String key = (String) params.get("paramKey");
        IPage<InputInvoiceConditionMap> page = this.page(
                new Query<InputInvoiceConditionMap>().getPage(params, null, true),
                new QueryWrapper<InputInvoiceConditionMap>()
                        .like(StringUtils.isNotBlank(key), "name", key)
                        .orderByDesc("id")
        );
        return new PageUtils(page);
    }

    @Override
    public void update(InputInvoiceConditionMap invoiceTaxRateTransition) {
        this.baseMapper.update(invoiceTaxRateTransition);
    }

    @Override
    public InputInvoiceConditionMap get(InputInvoiceConditionMap invoiceConditionMap) {
        return this.baseMapper.get(invoiceConditionMap);
    }

    @Override
    public InputInvoiceConditionMap getByCondition(InputInvoiceConditionMap invoiceConditionMap) {
        return this.baseMapper.getByCondition(invoiceConditionMap);
    }

    @Override
    public void updateByDelete(InputInvoiceConditionMap invoiceConditionMap) {
        this.baseMapper.updateByDelete(invoiceConditionMap);
    }

    @Override
    public InputInvoiceConditionMap getMaxByList(List<String> condition) {
        return this.baseMapper.getMaxByList(condition);
    }


}
