package com.pwc.modules.input.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.modules.input.entity.InputInvoiceEntity;
import com.pwc.modules.input.entity.InputInvoiceLabelRelatedEntity;

import java.util.List;


/**
 * @author Gxw
 * @date 2020/7/22 13:30
 */
public interface InputInvoiceLabelRelatedService extends IService<InputInvoiceLabelRelatedEntity> {
    void saveLable(InputInvoiceEntity invoiceEntity);

    InputInvoiceLabelRelatedEntity findByInvoiceId(Integer id);

    List<InputInvoiceEntity> findLabelByInvoiceId(List<InputInvoiceEntity> invoiceEntities);

    List<InputInvoiceLabelRelatedEntity> findByDate(String date);
}
