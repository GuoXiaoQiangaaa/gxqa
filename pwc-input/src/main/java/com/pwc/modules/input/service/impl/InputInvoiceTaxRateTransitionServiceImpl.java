package com.pwc.modules.input.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;
import com.pwc.modules.input.dao.InputInvoiceTaxRateTransitionDao;
import com.pwc.modules.input.entity.InputInvoiceTaxRateTransition;
import com.pwc.modules.input.service.InputInvoiceTaxRateTransitionService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("InvoiceTaxRateTransitionServiceImpl")
public class InputInvoiceTaxRateTransitionServiceImpl extends ServiceImpl<InputInvoiceTaxRateTransitionDao, InputInvoiceTaxRateTransition> implements InputInvoiceTaxRateTransitionService {

    @Override
    public PageUtils getList(Map<String, Object> params) {
        String key = (String) params.get("paramKey");
        IPage<InputInvoiceTaxRateTransition> page = this.page(
                new Query<InputInvoiceTaxRateTransition>().getPage(params, null, true),
                new QueryWrapper<InputInvoiceTaxRateTransition>()
                        .like(StringUtils.isNotBlank(key), "name", key)
                        .orderByDesc("id")
        );
        return new PageUtils(page);
    }

    @Override
    public void update(InputInvoiceTaxRateTransition invoiceTaxRateTransition) {
        this.baseMapper.update(invoiceTaxRateTransition);
    }



    @Override
    public InputInvoiceTaxRateTransition get(InputInvoiceTaxRateTransition invoiceTaxRateTransition) {
        return this.baseMapper.get(invoiceTaxRateTransition);
    }

    @Override
    public InputInvoiceTaxRateTransition getByTaxRate(InputInvoiceTaxRateTransition invoiceTaxRateTransition) {
        return this.baseMapper.getByTaxRate(invoiceTaxRateTransition);
    }

    @Override
    public void updateByDelete(InputInvoiceTaxRateTransition invoiceTaxRateTransition) {
        this.baseMapper.updateByDelete(invoiceTaxRateTransition);
    }
}
