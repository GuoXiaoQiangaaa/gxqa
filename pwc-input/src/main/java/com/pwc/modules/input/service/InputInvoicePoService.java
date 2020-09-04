package com.pwc.modules.input.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.input.entity.InputInvoicePoEntity;
import net.sf.json.JSONArray;

import java.util.Map;

/**
 * @author: Gxw
 * @create: 2020-09-02 18:43
 **/
public interface InputInvoicePoService extends IService<InputInvoicePoEntity> {
    InputInvoicePoEntity saveInputInvoicePoEntity(JSONArray data, String image);
    PageUtils getPoEntity(Map<String, Object> params);
}
