package com.pwc.modules.input.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;
import com.pwc.modules.input.dao.InputInvoiceUnitDetailsDao;
import com.pwc.modules.input.entity.InputInvoiceUnitDetailsEntity;
import com.pwc.modules.input.service.InputInvoiceUnitDetailsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("invoiceUnitDetailsService")
public class InputInvoiceUnitDetailsServiceImpl extends ServiceImpl<InputInvoiceUnitDetailsDao, InputInvoiceUnitDetailsEntity> implements InputInvoiceUnitDetailsService {

    @Override
    public InputInvoiceUnitDetailsEntity getByUnitName(String unitName) {
        return this.baseMapper.getByUnitName(unitName);
    }

    @Override
    public InputInvoiceUnitDetailsEntity getOneByNameOrCode(InputInvoiceUnitDetailsEntity invoiceUnitDetailsEntity) {
        return this.baseMapper.getOneByNameOrCode(invoiceUnitDetailsEntity);
    }

    @Override
    public InputInvoiceUnitDetailsEntity getOneByUnitAndNameOrCode(InputInvoiceUnitDetailsEntity invoiceUnitDetailsEntity) {
        return this.baseMapper.getOneByUnitAndNameOrCode(invoiceUnitDetailsEntity);
    }

    @Override
    public PageUtils findList(Map<String, Object> params) {
        String key = (String) params.get("paramKey");
        String invoiceUnit = (String) params.get("invoiceUnit");
        IPage<InputInvoiceUnitDetailsEntity> page = this.page(
                new Query<InputInvoiceUnitDetailsEntity>().getPage(params, null, true),
                new QueryWrapper<InputInvoiceUnitDetailsEntity>()
                        .or().apply("(details_delete='0')")
                        .like(StringUtils.isNotBlank(key), "name", key)
                        .eq(StringUtils.isNotBlank(invoiceUnit),"invoice_unit",invoiceUnit)
        );
        return new PageUtils(page);
    }

    @Override
    public InputInvoiceUnitDetailsEntity get(InputInvoiceUnitDetailsEntity invoiceUnitDetailsEntity) {
        return this.baseMapper.get(invoiceUnitDetailsEntity);
    }

    @Override
    public List<InputInvoiceUnitDetailsEntity> getUnitList(InputInvoiceUnitDetailsEntity invoiceUnitDetailsEntity) {
        return this.baseMapper.getUnitList(invoiceUnitDetailsEntity);
    }

    @Override
    public List<InputInvoiceUnitDetailsEntity> getUnit(InputInvoiceUnitDetailsEntity invoiceUnitDetailsEntity) {
        return this.baseMapper.getUnit(invoiceUnitDetailsEntity);
    }

    @Override
    public InputInvoiceUnitDetailsEntity getFind(InputInvoiceUnitDetailsEntity invoiceUnitDetailsEntity) {
        return this.baseMapper.getFind(invoiceUnitDetailsEntity);
    }

    @Override
    public void detailsDelete(InputInvoiceUnitDetailsEntity invoiceUnitDetailsEntity) {
        this.baseMapper.detailsDelete(invoiceUnitDetailsEntity);
    }

    @Override
    public List<InputInvoiceUnitDetailsEntity> getByDetailsNames(InputInvoiceUnitDetailsEntity invoiceUnitDetailsEntity) {
        return this.baseMapper.getByDetailsNames(invoiceUnitDetailsEntity);
    }
}
