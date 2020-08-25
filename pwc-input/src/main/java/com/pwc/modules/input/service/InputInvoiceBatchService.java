package com.pwc.modules.input.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.input.entity.InputInvoiceBatchEntity;
import com.pwc.modules.input.entity.InputInvoiceEntity;
import com.pwc.modules.input.entity.InputInvoiceMaterialEntity;

import java.util.List;
import java.util.Map;

/**
 * 票据上传Service
 * @author cj
 * @version 2018-12-05
 */
public interface InputInvoiceBatchService extends IService<InputInvoiceBatchEntity> {
    List<InputInvoiceBatchEntity> getList();

    InputInvoiceBatchEntity getBatchNumber(InputInvoiceEntity invoiceEntity);

    PageUtils findListForThree(Map<String, Object> params);

    InputInvoiceBatchEntity getLastOne();

    /**
     * 计算批次号
     * @return
     */
    String calculateBatchNumber();

    void delBatch(Integer id);

    void update(InputInvoiceBatchEntity invoiceBatchEntity);

    InputInvoiceBatchEntity get(InputInvoiceBatchEntity invoiceBatchEntity);

    void updateInvocieStatus(List<InputInvoiceMaterialEntity> invoiceMaterialEntityList);

    void setStatus(List<InputInvoiceEntity> invoiceEntityList);

    /**
     * 比较状态值更新状态
     * @param invoiceStatus
     * @return
     */
    boolean updateStatus(String batchId);
}
