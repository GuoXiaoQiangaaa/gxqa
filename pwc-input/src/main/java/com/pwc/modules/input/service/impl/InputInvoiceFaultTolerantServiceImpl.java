package com.pwc.modules.input.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;
import com.pwc.modules.input.dao.InputInvoiceFaultTolerantDao;
import com.pwc.modules.input.entity.InputInvoiceFaultTolerantEntity;
import com.pwc.modules.input.service.InputInvoiceFaultTolerantService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("invoiceFaultTolerantService")
public class InputInvoiceFaultTolerantServiceImpl extends ServiceImpl<InputInvoiceFaultTolerantDao, InputInvoiceFaultTolerantEntity> implements InputInvoiceFaultTolerantService {
    @Override
    public PageUtils findList(Map<String, Object> params) {
        String key = (String) params.get("paramKey");
        IPage<InputInvoiceFaultTolerantEntity> page = this.page(
                new Query<InputInvoiceFaultTolerantEntity>().getPage(params, null, true),
                new QueryWrapper<InputInvoiceFaultTolerantEntity>()
                        .like(StringUtils.isNotBlank(key), "name", key)
        );
        return new PageUtils(page);
    }

    @Override
    public InputInvoiceFaultTolerantEntity getOne() {
        return this.baseMapper.getOne();
    }

    public InputInvoiceFaultTolerantEntity getByName(InputInvoiceFaultTolerantEntity invoiceFaultTolerantEntity){
        return this.baseMapper.getByName(invoiceFaultTolerantEntity);
    }

}
