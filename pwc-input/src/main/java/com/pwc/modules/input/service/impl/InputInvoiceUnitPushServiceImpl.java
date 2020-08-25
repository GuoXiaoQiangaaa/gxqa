package com.pwc.modules.input.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;
import com.pwc.modules.input.dao.InputInvoiceUnitPushDao;
import com.pwc.modules.input.entity.InputInvoiceUnitPush;
import com.pwc.modules.input.service.InputInvoiceUnitPushService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("invoiceUnitPushServiceImpl")
public class InputInvoiceUnitPushServiceImpl extends ServiceImpl<InputInvoiceUnitPushDao, InputInvoiceUnitPush> implements InputInvoiceUnitPushService {
    @Override
    public InputInvoiceUnitPush getByBasicUnits(InputInvoiceUnitPush invoiceUnitPush) {
        return this.baseMapper.getByBasicUnits(invoiceUnitPush);
    }

    @Override
    public void updateByDelete(InputInvoiceUnitPush invoiceUnitPush) {
        this.baseMapper.updateByDelete(invoiceUnitPush);
    }

    @Override
    public InputInvoiceUnitPush get(InputInvoiceUnitPush invoiceUnitPush) {
        return this.baseMapper.get(invoiceUnitPush);
    }

    @Override
    public void update(InputInvoiceUnitPush invoiceUnitPush) {
        this.baseMapper.update(invoiceUnitPush);
    }


    @Override
    public PageUtils findList(Map<String, Object> params) {
        String key = (String) params.get("paramKey");
        IPage<InputInvoiceUnitPush> page = this.page(
                new Query<InputInvoiceUnitPush>().getPage(params, null, true),
                new QueryWrapper<InputInvoiceUnitPush>()
                        .like(StringUtils.isNotBlank(key), "name", key)
                        .orderByDesc("id")
        );
        return new PageUtils(page);
    }
}
