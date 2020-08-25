package com.pwc.modules.input.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.input.entity.InputInvoiceUnitDifferent;

import java.util.Map;

public interface InputInvoiceUnitDifferentService extends IService<InputInvoiceUnitDifferent> {
    int getList();
    InputInvoiceUnitDifferent getByUnit(String unit, String mblnr);
    InputInvoiceUnitDifferent getByThree(InputInvoiceUnitDifferent invoiceUnitDifferent);
    void updateByDelete(InputInvoiceUnitDifferent invoiceUnitDifferent);
    InputInvoiceUnitDifferent get(InputInvoiceUnitDifferent invoiceUnitDifferent);
    void update(InputInvoiceUnitDifferent invoiceUnitDifferent);
    PageUtils findList(Map<String, Object> params);
}
