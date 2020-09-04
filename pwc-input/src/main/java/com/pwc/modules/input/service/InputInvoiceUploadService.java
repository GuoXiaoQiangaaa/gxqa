package com.pwc.modules.input.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.input.entity.InputInvoiceUploadEntity;

import java.util.Map;

/**
 * @author: Gxw
 * @create: 2020-09-03 16:16
 **/
public interface InputInvoiceUploadService extends IService<InputInvoiceUploadEntity> {
    PageUtils findUploadList(Map<String, Object> params);
}
