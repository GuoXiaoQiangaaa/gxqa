package com.pwc.modules.input.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.modules.input.dao.InputInvoiceMaterialDao;
import com.pwc.modules.input.entity.InputInvoiceMaterialEntity;
import com.pwc.modules.input.service.InputInvoiceMaterialService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("invoiceMaterialService")
public class InputInvoiceMaterialServiceImpl extends ServiceImpl<InputInvoiceMaterialDao, InputInvoiceMaterialEntity> implements InputInvoiceMaterialService {

    @Override
    public List<InputInvoiceMaterialEntity> getByInvoiceId(InputInvoiceMaterialEntity invoiceMaterialEntity){
        return this.baseMapper.getByInvoiceId(invoiceMaterialEntity);
    }

    @Override
    public List<InputInvoiceMaterialEntity> getByInvoiceIds(List<Integer> invoiceIds) {
        return this.baseMapper.getByInvoiceIds(invoiceIds);
    }

    @Override
    public void deleteInvoiceMaterialByInvoiceId(InputInvoiceMaterialEntity invoiceMaterialEntity) {
        this.baseMapper.deleteInvoiceMaterialByInvoiceId(invoiceMaterialEntity);
    }

    @Override
    public List<InputInvoiceMaterialEntity> getListByInvoiceId(InputInvoiceMaterialEntity invoiceMaterialEntity) {
        if( invoiceMaterialEntity.getInvoiceIds() ==null){
            return null;
        }

        return this.baseMapper.getListByInvoiceId(invoiceMaterialEntity);
    }

    @Override
    public void update(InputInvoiceMaterialEntity invoiceMaterialEntity) {
        this.baseMapper.update(invoiceMaterialEntity);
    }

    @Override
    public List<InputInvoiceMaterialEntity> getListByIds(InputInvoiceMaterialEntity invoiceMaterialEntity) {
        return this.baseMapper.getListByIds(invoiceMaterialEntity);
    }

    @Override
    public void updateByEnter(List<InputInvoiceMaterialEntity> invoiceMaterialEntityList) {
        this.baseMapper.updateByEnter(invoiceMaterialEntityList);
    }

    @Override
    public List<InputInvoiceMaterialEntity> getByIds(Integer[] ids) {
        return this.baseMapper.getByIds(ids);
    }
}
