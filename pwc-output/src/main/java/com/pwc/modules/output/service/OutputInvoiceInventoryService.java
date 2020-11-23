package com.pwc.modules.output.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.modules.output.entity.OutputInvoiceInventoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 发票库存服务
 *
 * @author fanpf
 * @date 2020/9/25
 */
public interface OutputInvoiceInventoryService extends IService<OutputInvoiceInventoryEntity> {

    /**
     * 列表
     */
    List<OutputInvoiceInventoryEntity> list(Map<String, Object> params);

    /**
     * 人工录入
     */
    void manmade(List<OutputInvoiceInventoryEntity> entityList, Map<String, Object> params);

    /**
     * 编辑
     */
    void update(OutputInvoiceInventoryEntity outputInvoiceInventory);

    /**
     * 查询发票库存是否低于临界值
     * true:低于; false:高于
     */
    boolean isUnderCrisis();
}

