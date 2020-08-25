package com.pwc.modules.input.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.input.entity.InputInvoiceUnitPush;

import java.util.Map;

public interface InputInvoiceUnitPushService extends IService<InputInvoiceUnitPush> {
    InputInvoiceUnitPush getByBasicUnits(InputInvoiceUnitPush invoiceUnitPush);
    void updateByDelete(InputInvoiceUnitPush invoiceUnitPush);
    InputInvoiceUnitPush get(InputInvoiceUnitPush invoiceUnitPush);
    void update(InputInvoiceUnitPush invoiceUnitPush);
    PageUtils findList(Map<String, Object> params);
}
