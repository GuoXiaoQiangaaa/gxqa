package com.pwc.modules.input.service.impl;

import com.pwc.common.utils.apidemo.utils.IDGenerator;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;

import com.pwc.modules.input.dao.InputInvoiceCustomsPushDao;
import com.pwc.modules.input.entity.InputInvoiceCustomsPushEntity;
import com.pwc.modules.input.service.InputInvoiceCustomsPushService;


@Service("inputInvoiceCustomsPushService")
public class InputInvoiceCustomsPushServiceImpl extends ServiceImpl<InputInvoiceCustomsPushDao, InputInvoiceCustomsPushEntity> implements InputInvoiceCustomsPushService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String voucherNumber=(String)params.get("voucherNumber");
        String companyId=(String)params.get("companyId");
        String accountingNumber=(String)params.get("accountingNumber");
        String postingDateArray=(String)params.get("postingDateArray");
        IPage<InputInvoiceCustomsPushEntity> page = this.page(
                new Query<InputInvoiceCustomsPushEntity>().getPage(params),
                new QueryWrapper<InputInvoiceCustomsPushEntity>()
                .eq(StringUtils.isNotBlank(voucherNumber),"voucher_number",voucherNumber)
                .eq(StringUtils.isNotBlank(companyId),"company_id",companyId)
                .eq(StringUtils.isNotBlank(accountingNumber),"accounting_number",accountingNumber)
                .ge(StringUtils.isNotBlank(postingDateArray), "posting_date", !StringUtils.isNotBlank(postingDateArray) ? "" : postingDateArray.split(",")[0])
                .le(StringUtils.isNotBlank(postingDateArray), "posting_date", !StringUtils.isNotBlank(postingDateArray) ? "" : postingDateArray.split(",")[1])


        );

        return new PageUtils(page);
    }


}
