package com.pwc.modules.input.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;
import com.pwc.modules.input.dao.InputInvoiceRefundDao;
import com.pwc.modules.input.entity.InputInvoiceRefundEntity;
import com.pwc.modules.input.service.InputInvoiceRefundService;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("invoiceRefundService")
public class InputInvoiceRefundServiceImpl extends ServiceImpl<InputInvoiceRefundDao, InputInvoiceRefundEntity> implements InputInvoiceRefundService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<InputInvoiceRefundEntity> page = this.page(
                new Query<InputInvoiceRefundEntity>().getPage(params, null, true),
                new QueryWrapper<InputInvoiceRefundEntity>()
        );

        return new PageUtils(page);
    }
}
