package com.pwc.modules.input.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.modules.input.entity.InputInvoiceEntity;
import com.pwc.modules.input.entity.InputInvoiceTaxationEntity;

import java.util.List;
import java.util.Map;


/**
 * @author gxw
 * @date 2020/6/23 19:07
 */
public interface InputInvoiceTaxationService extends IService<InputInvoiceTaxationEntity> {
    InputInvoiceTaxationEntity findDataByCreateDate(Integer invoiceId,String[] flag,String Date);
    void saveTaxation(InputInvoiceEntity invoice,String flag);
    void updateTaxation(InputInvoiceEntity invoice);
    List<InputInvoiceTaxationEntity> findByFlag(String[] ids,String Date);
    void saveReason(Map<String, Object> params);
    List<InputInvoiceTaxationEntity> findByFlagNoData(String[] ids, String Date);
    void saveOldData(String Date);

    List<InputInvoiceTaxationEntity> findByEntity(String invoiceNumber, String invoiceCode, String voucherNumber);

}
