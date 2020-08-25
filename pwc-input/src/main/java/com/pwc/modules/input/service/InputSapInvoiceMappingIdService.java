package com.pwc.modules.input.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.modules.input.entity.InputSapInvoiceMappingIdEntity;

import java.util.List;

public interface InputSapInvoiceMappingIdService extends IService<InputSapInvoiceMappingIdEntity> {
    InputSapInvoiceMappingIdEntity getOneBySapId(InputSapInvoiceMappingIdEntity sapInvoiceMappingIdEntity);
//    void save(InputSapInvoiceMappingIdEntity sapInvoiceMappingIdEntity);
    void update(InputSapInvoiceMappingIdEntity sapInvoiceMappingIdEntity);
    List<InputSapInvoiceMappingIdEntity> getListByTaxOrName(InputSapInvoiceMappingIdEntity sapInvoiceMappingIdEntity);
}
