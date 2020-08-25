package com.pwc.modules.input.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.modules.input.entity.InputInvoiceVoucherEntity;

import java.util.List;

/**
 * ocr物料凭证Service
 * @author cj
 * @version 2018-1-17
 */
public interface InputInvoiceVoucherService extends IService<InputInvoiceVoucherEntity> {
    public List<InputInvoiceVoucherEntity> getListByBatchId(InputInvoiceVoucherEntity invoiceVoucherEntity);
    public List<InputInvoiceVoucherEntity> getListByBatchIdAndVoucherNumber(InputInvoiceVoucherEntity invoiceVoucherEntity);
    List<InputInvoiceVoucherEntity> getListGroupBy(InputInvoiceVoucherEntity invoiceVoucherEntity);
    List<InputInvoiceVoucherEntity> getListByInvoiceNumberAndBatchNumber(InputInvoiceVoucherEntity invoiceVoucherEntity);
    List<InputInvoiceVoucherEntity> getListByInvoiceNumberAndBatchNumberGroupBy(InputInvoiceVoucherEntity invoiceVoucherEntity);
    List<InputInvoiceVoucherEntity> getListByBatchIdGroupInvoiceNumber(InputInvoiceVoucherEntity invoiceVoucherEntity);
    void deleteByBatchId(InputInvoiceVoucherEntity invoiceVoucherEntity);
    InputInvoiceVoucherEntity getById(InputInvoiceVoucherEntity invoiceVoucherEntity);
    public int update(InputInvoiceVoucherEntity invoiceVoucherEntity);
    void delete(InputInvoiceVoucherEntity invoiceVoucherEntity);
    List<InputInvoiceVoucherEntity> getListByBatchAndInvoiceNumber(InputInvoiceVoucherEntity invoiceVoucherEntity);
}
