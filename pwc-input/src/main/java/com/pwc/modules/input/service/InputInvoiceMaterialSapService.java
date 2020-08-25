package com.pwc.modules.input.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.modules.input.entity.InputInvoiceMaterialSapEntity;

import java.util.List;
import java.util.Map;

public interface InputInvoiceMaterialSapService extends IService<InputInvoiceMaterialSapEntity> {

    List<InputInvoiceMaterialSapEntity> getListByBatchId(InputInvoiceMaterialSapEntity invoiceMaterialSapEntity);

    void deleteSapByInvoiceBatchId(InputInvoiceMaterialSapEntity invoiceMaterialSapEntity);

    List<InputInvoiceMaterialSapEntity> getListByMBLNRids(List<String> MBLNRIds);

    void deleteByBatchIdAndMblnr(InputInvoiceMaterialSapEntity invoiceMaterialSapEntity) ;

    void update(InputInvoiceMaterialSapEntity invoiceMaterialSapEntity);

    List<InputInvoiceMaterialSapEntity> getListBySapId(List<InputInvoiceMaterialSapEntity> list);

    List<InputInvoiceMaterialSapEntity> getListByLineIdAndMate(InputInvoiceMaterialSapEntity invoiceMaterialSapEntity);

    void updateLineId(List<InputInvoiceMaterialSapEntity> list);

    void updateByEnter(List<InputInvoiceMaterialSapEntity> invoiceMaterialSapEntityList);

    void updateByPostQm(List<InputInvoiceMaterialSapEntity> list);

    String getMaxQmdateByBatchId(InputInvoiceMaterialSapEntity invoiceMaterialSapEntity);

    String getMaxBudatMkpfByBatchId(InputInvoiceMaterialSapEntity invoiceMaterialSapEntity);

    InputInvoiceMaterialSapEntity get(InputInvoiceMaterialSapEntity invoiceMaterialSapEntity);
}
