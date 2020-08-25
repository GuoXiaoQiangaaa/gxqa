package com.pwc.modules.input.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.input.entity.InputInvoiceTaxRateTransition;

import java.util.Map;

public interface InputInvoiceTaxRateTransitionService extends IService<InputInvoiceTaxRateTransition> {
    PageUtils getList(Map<String, Object> params);

    void update(InputInvoiceTaxRateTransition invoiceTaxRateTransition);

    InputInvoiceTaxRateTransition get(InputInvoiceTaxRateTransition invoiceTaxRateTransition);

    InputInvoiceTaxRateTransition getByTaxRate(InputInvoiceTaxRateTransition invoiceTaxRateTransition);

    void updateByDelete(InputInvoiceTaxRateTransition invoiceTaxRateTransition);
}
