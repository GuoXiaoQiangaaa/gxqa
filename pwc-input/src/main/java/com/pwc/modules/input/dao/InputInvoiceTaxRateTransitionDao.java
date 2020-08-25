package com.pwc.modules.input.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.modules.input.entity.InputInvoiceTaxRateTransition;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InputInvoiceTaxRateTransitionDao extends BaseMapper<InputInvoiceTaxRateTransition> {
    InputInvoiceTaxRateTransition get(InputInvoiceTaxRateTransition invoiceTaxRateTransition);

    void update(InputInvoiceTaxRateTransition invoiceTaxRateTransition);

    void save(InputInvoiceTaxRateTransition invoiceTaxRateTransition);

    void updateByDelete(InputInvoiceTaxRateTransition invoiceTaxRateTransition);

    InputInvoiceTaxRateTransition getByTaxRate(InputInvoiceTaxRateTransition invoiceTaxRateTransition);
}
