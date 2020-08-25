package com.pwc.modules.input.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.input.entity.InputInvoiceConditionMap;
import com.pwc.modules.input.entity.InputInvoiceMaterialSapEntity;

import java.util.List;
import java.util.Map;

public interface InputInvoiceConditionMapService extends IService<InputInvoiceConditionMap> {
    InputInvoiceConditionMap getMaxConditionCode(InputInvoiceMaterialSapEntity invoiceMaterialSapEntity);

    List<InputInvoiceConditionMap> getAllList();

    PageUtils getList(Map<String, Object> params);

    void update(InputInvoiceConditionMap invoiceConditionMap);

    InputInvoiceConditionMap get(InputInvoiceConditionMap invoiceConditionMap);

    InputInvoiceConditionMap getByCondition(InputInvoiceConditionMap invoiceConditionMap);

    void updateByDelete(InputInvoiceConditionMap invoiceConditionMap);

    InputInvoiceConditionMap getMaxByList(List<String> condition);
}
