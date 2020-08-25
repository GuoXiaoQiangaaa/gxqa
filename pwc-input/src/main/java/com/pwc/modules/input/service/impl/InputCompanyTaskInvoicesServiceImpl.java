package com.pwc.modules.input.service.impl;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;
import com.pwc.modules.input.dao.InputCompanyTaskInvoicesDao;
import com.pwc.modules.input.entity.InputCompanyTaskInvoices;
import com.pwc.modules.input.service.InputCompanyTaskInvoicesService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("companyTaskInvoicesService")
public class InputCompanyTaskInvoicesServiceImpl extends ServiceImpl<InputCompanyTaskInvoicesDao, InputCompanyTaskInvoices> implements InputCompanyTaskInvoicesService {


    @Override
    public PageUtils getInvoicesList(Map<String, Object> params) {
        String statisticsMonth=(String)params.get("statisticsMonth");
        String companyId=(String)params.get("companyId");
        IPage<InputCompanyTaskInvoices> page = this.page(
                new Query<InputCompanyTaskInvoices>().getPage(params, null, true),
                new QueryWrapper<InputCompanyTaskInvoices>()
                        .eq(StringUtils.isNotBlank(statisticsMonth),"statistics_month",statisticsMonth)
                        .eq(StringUtils.isNotBlank(companyId),"company_id",companyId)
        );
        return new PageUtils(page);
    }
}
