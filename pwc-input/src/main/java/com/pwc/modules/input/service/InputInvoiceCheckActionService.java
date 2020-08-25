package com.pwc.modules.input.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.modules.input.entity.InputInvoiceCheckAction;

import java.math.BigDecimal;
import java.util.List;

public interface InputInvoiceCheckActionService extends IService<InputInvoiceCheckAction> {
    String checkSubtract(BigDecimal a, BigDecimal b);
    void updateByAction(InputInvoiceCheckAction invoiceCheckAction);
    List<InputInvoiceCheckAction> findList();
    InputInvoiceCheckAction getByItem(InputInvoiceCheckAction invoiceCheckAction);
    String checkAction(String data);
}
