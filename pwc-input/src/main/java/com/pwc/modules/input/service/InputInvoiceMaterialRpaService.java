package com.pwc.modules.input.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.input.entity.InputInvoiceMaterial;
import com.pwc.modules.input.entity.InputInvoiceMaterialRpaEntity;

import java.util.List;
import java.util.Map;

public interface InputInvoiceMaterialRpaService extends IService<InputInvoiceMaterialRpaEntity> {
    PageUtils findList(Map<String, Object> params);
    InputInvoiceMaterial get(int id);
    List<InputInvoiceMaterial> getListByStatus();

    /**
     * 更新状态为0
     * @param list
     */
    void updateByStatus(List<InputInvoiceMaterial> list);
    InputInvoiceMaterial getOneByBatchId(InputInvoiceMaterial invoiceMaterial);
    int insert(InputInvoiceMaterial invoiceMaterial);
    void deleteByBatchId(InputInvoiceMaterial invoiceMaterial);
}
