package com.pwc.modules.input.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.input.entity.InputInvoiceEntity;

import java.util.Map;

public interface InputInvoiceListOfDiffService extends IService<InputInvoiceEntity> {

    PageUtils listPage(Map<String, Object> params);

}
