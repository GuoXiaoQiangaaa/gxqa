package com.pwc.modules.input.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.input.entity.InputInvoiceFaultTolerantEntity;

import java.util.Map;

public interface InputInvoiceFaultTolerantService extends IService<InputInvoiceFaultTolerantEntity> {
    PageUtils findList(Map<String, Object> params);
    InputInvoiceFaultTolerantEntity getOne();
    InputInvoiceFaultTolerantEntity getByName(InputInvoiceFaultTolerantEntity invoiceFaultTolerantEntity);
}
