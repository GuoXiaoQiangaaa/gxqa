package com.pwc.modules.input.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;

import com.pwc.modules.input.dao.InputInvoiceVoucherNoDao;
import com.pwc.modules.input.entity.InputInvoiceVoucherNoEntity;
import com.pwc.modules.input.service.InputInvoiceVoucherNoService;


@Service("inputInvoiceVoucherNoService")
public class InputInvoiceVoucherNoServiceImpl extends ServiceImpl<InputInvoiceVoucherNoDao, InputInvoiceVoucherNoEntity> implements InputInvoiceVoucherNoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<InputInvoiceVoucherNoEntity> page = this.page(
                new Query<InputInvoiceVoucherNoEntity>().getPage(params),
                new QueryWrapper<InputInvoiceVoucherNoEntity>()
        );

        return new PageUtils(page);
    }

}
