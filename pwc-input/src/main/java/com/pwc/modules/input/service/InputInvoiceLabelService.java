package com.pwc.modules.input.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.input.entity.InputInvoiceLabelEntity;

import java.util.List;
import java.util.Map;

/**
 * @author Gxw
 * @date 2020/7/15 16:48
 */
public interface InputInvoiceLabelService extends IService<InputInvoiceLabelEntity> {
    PageUtils findLabel(Map<String,Object> params);

    List<InputInvoiceLabelEntity> findLabelList(Map<String, Object> params);
    InputInvoiceLabelEntity findRule(String labelName);
}
