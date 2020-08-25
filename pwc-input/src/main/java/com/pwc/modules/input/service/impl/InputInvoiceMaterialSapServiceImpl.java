package com.pwc.modules.input.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.modules.input.dao.InputInvoiceMaterialSapDao;
import com.pwc.modules.input.entity.InputInvoiceMaterialSapEntity;
import com.pwc.modules.input.service.InputInvoiceMaterialSapService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("InvoiceMaterialSapService")
public class InputInvoiceMaterialSapServiceImpl extends ServiceImpl<InputInvoiceMaterialSapDao, InputInvoiceMaterialSapEntity> implements InputInvoiceMaterialSapService {

    @Override
    public List<InputInvoiceMaterialSapEntity> getListByBatchId(InputInvoiceMaterialSapEntity invoiceMaterialSapEntity) {
        return this.baseMapper.getListByBatchId(invoiceMaterialSapEntity);
    }

    @Override
    public void deleteSapByInvoiceBatchId(InputInvoiceMaterialSapEntity invoiceMaterialSapEntity) {
        this.baseMapper.deleteSapByInvoiceBatchId(invoiceMaterialSapEntity);
    }

    @Override
    public List<InputInvoiceMaterialSapEntity> getListByMBLNRids(List<String> MBLNRIds){
        return this.baseMapper.getListByMBLNRids(MBLNRIds);
    }

    @Override
    public void deleteByBatchIdAndMblnr(InputInvoiceMaterialSapEntity invoiceMaterialSapEntity) {
        this.baseMapper.deleteByBatchIdAndMblnr(invoiceMaterialSapEntity);
    }

    @Override
    public void update(InputInvoiceMaterialSapEntity invoiceMaterialSapEntity) {
        this.baseMapper.update(invoiceMaterialSapEntity);
    }

    @Override
    public List<InputInvoiceMaterialSapEntity> getListBySapId(List<InputInvoiceMaterialSapEntity> list) {
        return this.baseMapper.getListBySapId(list);
    }

    @Override
    public List<InputInvoiceMaterialSapEntity> getListByLineIdAndMate(InputInvoiceMaterialSapEntity invoiceMaterialSapEntity) {
        return this.baseMapper.getListByLineIdAndMate(invoiceMaterialSapEntity);
    }

    @Override
    public void updateLineId(List<InputInvoiceMaterialSapEntity> list) {
        this.baseMapper.updateLineId(list);
    }

    @Override
    public void updateByEnter(List<InputInvoiceMaterialSapEntity> invoiceMaterialSapEntityList) {
        this.baseMapper.updateByEnter(invoiceMaterialSapEntityList);
    }

    @Override
    public void updateByPostQm(List<InputInvoiceMaterialSapEntity> list) {
        this.baseMapper.updateByPostQm(list);
    }

    @Override
    public String getMaxQmdateByBatchId(InputInvoiceMaterialSapEntity invoiceMaterialSapEntity) {
        return this.baseMapper.getMaxQmdateByBatchId(invoiceMaterialSapEntity);
    }

    @Override
    public String getMaxBudatMkpfByBatchId(InputInvoiceMaterialSapEntity invoiceMaterialSapEntity) {
        return this.baseMapper.getMaxBudatMkpfByBatchId(invoiceMaterialSapEntity);
    }

    @Override
    public InputInvoiceMaterialSapEntity get(InputInvoiceMaterialSapEntity invoiceMaterialSapEntity) {
        return this.baseMapper.get(invoiceMaterialSapEntity);
    }
}
