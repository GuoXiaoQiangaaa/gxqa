package com.pwc.modules.input.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.modules.input.dao.InputInvoiceVoucherDao;
import com.pwc.modules.input.entity.InputInvoiceVoucherEntity;
import com.pwc.modules.input.service.InputInvoiceVoucherService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("invoiceVoucherService")
public class InputInvoiceVoucherServiceImpl extends ServiceImpl<InputInvoiceVoucherDao, InputInvoiceVoucherEntity> implements InputInvoiceVoucherService {


    @Override
    public List<InputInvoiceVoucherEntity> getListByBatchId(InputInvoiceVoucherEntity invoiceVoucherEntity) {
        return this.baseMapper.getListByBatchId(invoiceVoucherEntity);
    }

    @Override
    public List<InputInvoiceVoucherEntity> getListByBatchIdAndVoucherNumber(InputInvoiceVoucherEntity invoiceVoucherEntity) {
        return this.baseMapper.getListByBatchIdAndVoucherNumber(invoiceVoucherEntity);
    }

    @Override
    public List<InputInvoiceVoucherEntity> getListGroupBy(InputInvoiceVoucherEntity invoiceVoucherEntity) {
        return this.baseMapper.getListGroupBy(invoiceVoucherEntity);
    }

    @Override
    public List<InputInvoiceVoucherEntity> getListByInvoiceNumberAndBatchNumber(InputInvoiceVoucherEntity invoiceVoucherEntity) {
        return this.baseMapper.getListByInvoiceNumberAndBatchNumber(invoiceVoucherEntity);
    }

    @Override
    public List<InputInvoiceVoucherEntity> getListByInvoiceNumberAndBatchNumberGroupBy(InputInvoiceVoucherEntity invoiceVoucherEntity) {
        return this.baseMapper.getListByInvoiceNumberAndBatchNumberGroupBy(invoiceVoucherEntity);
    }

    @Override
    public List<InputInvoiceVoucherEntity> getListByBatchIdGroupInvoiceNumber(InputInvoiceVoucherEntity invoiceVoucherEntity) {
        return this.baseMapper.getListByBatchIdGroupInvoiceNumber(invoiceVoucherEntity);
    }

    @Override
    public void deleteByBatchId(InputInvoiceVoucherEntity invoiceVoucherEntity) {
        this.baseMapper.deleteByBatchId(invoiceVoucherEntity);
    }

    @Override
    public InputInvoiceVoucherEntity getById(InputInvoiceVoucherEntity invoiceVoucherEntity) {
        return this.baseMapper.getById(invoiceVoucherEntity);
    }

    public int update(InputInvoiceVoucherEntity invoiceVoucherEntity) {
        return this.baseMapper.update(invoiceVoucherEntity);
    }
    public void delete(InputInvoiceVoucherEntity invoiceVoucherEntity) {
        this.baseMapper.delete(invoiceVoucherEntity);
    }

    @Override
    public List<InputInvoiceVoucherEntity> getListByBatchAndInvoiceNumber(InputInvoiceVoucherEntity invoiceVoucherEntity) {
        return this.baseMapper.getListByBatchAndInvoiceNumber(invoiceVoucherEntity);
    }

}
