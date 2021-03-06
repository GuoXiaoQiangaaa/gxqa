package com.pwc.modules.input.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.fapiao.neon.model.in.inspect.item.NormalItem;
import com.fapiao.neon.model.in.inspect.item.SpecialItem;
import com.pwc.modules.input.entity.InputInvoiceMaterialEntity;

import java.util.List;


/**
 * 发票物料Service
 * @author zk
 */
public interface InputInvoiceMaterialService extends IService<InputInvoiceMaterialEntity> {

    List<InputInvoiceMaterialEntity> getByInvoiceId(InputInvoiceMaterialEntity invoiceMaterialEntity);

    List<InputInvoiceMaterialEntity> getByInvoiceIds(List<Integer> invoiceIds);

    void deleteInvoiceMaterialByInvoiceId(InputInvoiceMaterialEntity invoiceMaterialEntity);

    List<InputInvoiceMaterialEntity> getListByInvoiceId(InputInvoiceMaterialEntity invoiceMaterialEntity);

    void update(InputInvoiceMaterialEntity invoiceMaterialEntity);

    List<InputInvoiceMaterialEntity> getListByIds(InputInvoiceMaterialEntity invoiceMaterialEntity);

    void updateByEnter(List<InputInvoiceMaterialEntity> invoiceMaterialEntityList);

    List<InputInvoiceMaterialEntity> getByIds(Integer[] ids);

    void saveMaterialEntityByNormalItem(List<NormalItem> items,Integer invoiceId);

    void saveMaterialEntityBySpecialItem(List<SpecialItem> items, Integer invoiceId);

}
