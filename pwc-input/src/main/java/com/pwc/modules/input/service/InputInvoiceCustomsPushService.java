package com.pwc.modules.input.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.input.entity.InputInvoiceCustomsPushEntity;

import java.util.List;
import java.util.Map;

/**
 * 海关缴款书SAP入账或冲销凭证信息推送表
 *
 * @author zlb
 * @email 
 * @date 2020-08-12 17:01:42
 */
public interface InputInvoiceCustomsPushService extends IService<InputInvoiceCustomsPushEntity> {

    PageUtils queryPage(Map<String, Object> params);

}

