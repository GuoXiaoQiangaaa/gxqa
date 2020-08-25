package com.pwc.modules.input.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.modules.input.entity.InputInvoiceVoucherEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 票据上传DAO接口
 * @author cj
 * @version 2018-12-05
 */
@Mapper
public interface InputInvoiceVoucherDao extends BaseMapper<InputInvoiceVoucherEntity> {
    public int save(InputInvoiceVoucherEntity invoiceVoucherEntity);
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
