package com.pwc.modules.input.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;
import com.pwc.modules.input.dao.InputInvoiceUnitDifferentDao;
import com.pwc.modules.input.entity.InputInvoiceUnitDifferent;
import com.pwc.modules.input.service.InputInvoiceUnitDifferentService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("invoiceUnitDifferentService")
public class InputInvoiceUnitDifferentServiceImpl extends ServiceImpl<InputInvoiceUnitDifferentDao, InputInvoiceUnitDifferent> implements InputInvoiceUnitDifferentService {

    @Override
    public InputInvoiceUnitDifferent getByUnit(String unit, String mblnr) {
        return this.baseMapper.getByUnit(unit,mblnr);
    }

    @Override
    public InputInvoiceUnitDifferent getByThree(InputInvoiceUnitDifferent invoiceUnitDifferent) {
        return this.baseMapper.getByThree(invoiceUnitDifferent);
    }
    @Override
    public int getList() {
        return this.baseMapper.getList();
    }

    @Override
    public void updateByDelete(InputInvoiceUnitDifferent invoiceUnitDifferent) {
        this.baseMapper.updateByDelete(invoiceUnitDifferent);
    }

    @Override
    public InputInvoiceUnitDifferent get(InputInvoiceUnitDifferent invoiceUnitDifferent) {
        return this.baseMapper.get(invoiceUnitDifferent);
    }

    @Override
    public void update(InputInvoiceUnitDifferent invoiceUnitDifferent) {
        this.baseMapper.update(invoiceUnitDifferent);
    }


    @Override
    public PageUtils findList(Map<String, Object> params) {
        String key = (String) params.get("paramKey");
        String sapmblnr = (String)params.get("sapMblnr");
        IPage<InputInvoiceUnitDifferent> page = this.page(
                new Query<InputInvoiceUnitDifferent>().getPage(params, null, true),
                new QueryWrapper<InputInvoiceUnitDifferent>()
                        .like(StringUtils.isNotBlank(key), "name", key)
                        .eq(StringUtils.isNotBlank(sapmblnr),"sap_mblnr",sapmblnr)
                        .orderByDesc("id")

        );
        return new PageUtils(page);
    }
}
