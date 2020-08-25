package com.pwc.modules.input.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.input.entity.InputInvoiceUnitDetailsEntity;

import java.util.List;
import java.util.Map;

public interface InputInvoiceUnitDetailsService extends IService<InputInvoiceUnitDetailsEntity> {
    InputInvoiceUnitDetailsEntity getByUnitName(String unitName);
    InputInvoiceUnitDetailsEntity getOneByNameOrCode(InputInvoiceUnitDetailsEntity invoiceUnitDetailsEntity);
    InputInvoiceUnitDetailsEntity getOneByUnitAndNameOrCode(InputInvoiceUnitDetailsEntity invoiceUnitDetailsEntity);
    PageUtils findList(Map<String, Object> params);
    InputInvoiceUnitDetailsEntity get(InputInvoiceUnitDetailsEntity invoiceUnitDetailsEntity);
    List<InputInvoiceUnitDetailsEntity> getUnitList(InputInvoiceUnitDetailsEntity invoiceUnitDetailsEntity);
    List<InputInvoiceUnitDetailsEntity> getUnit(InputInvoiceUnitDetailsEntity invoiceUnitDetailsEntity);
    InputInvoiceUnitDetailsEntity getFind(InputInvoiceUnitDetailsEntity invoiceUnitDetailsEntity);
    void detailsDelete(InputInvoiceUnitDetailsEntity invoiceUnitDetailsEntity);
    List<InputInvoiceUnitDetailsEntity> getByDetailsNames(InputInvoiceUnitDetailsEntity invoiceUnitDetailsEntity);
}
