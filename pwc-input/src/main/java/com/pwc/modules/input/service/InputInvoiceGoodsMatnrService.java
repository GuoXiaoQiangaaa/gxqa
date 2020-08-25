package com.pwc.modules.input.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.input.entity.InputInvoiceGoodsMatnr;

import java.util.List;
import java.util.Map;

public interface InputInvoiceGoodsMatnrService extends IService<InputInvoiceGoodsMatnr> {

    void updateByThreebasic(InputInvoiceGoodsMatnr invoiceGoodsMatnr);

    PageUtils findList(Map<String, Object> params);

    InputInvoiceGoodsMatnr getById(InputInvoiceGoodsMatnr invoiceGoodsMatnr);

    InputInvoiceGoodsMatnr getByThreeBasic(InputInvoiceGoodsMatnr invoiceGoodsMatnr);

    void update(InputInvoiceGoodsMatnr invoiceGoodsMatnr);

//    void save(InputInvoiceGoodsMatnr invoiceGoodsMatnr);

    void delete(InputInvoiceGoodsMatnr invoiceGoodsMatnr);

    void deleteByIds(Integer[] ids);

    List<InputInvoiceGoodsMatnr> getListByCondition(InputInvoiceGoodsMatnr invoiceGoodsMatnr);

    InputInvoiceGoodsMatnr getOneByCondition(InputInvoiceGoodsMatnr invoiceGoodsMatnr);

    List<InputInvoiceGoodsMatnr> getListAll();

    void deleteAll();

    void insertAll(List<InputInvoiceGoodsMatnr> list);
}
