package com.pwc.modules.input.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.input.entity.InputCompanyTaskInvoices;

import java.util.Map;

public interface InputCompanyTaskInvoicesService extends IService<InputCompanyTaskInvoices> {

    PageUtils getInvoicesList(Map<String, Object> params);
}
