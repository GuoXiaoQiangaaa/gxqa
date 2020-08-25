package com.pwc.modules.input.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.input.entity.InputInvoiceResponsibleEntity;

import java.util.List;
import java.util.Map;

public interface InputInvoiceResponsibleService extends IService<InputInvoiceResponsibleEntity> {
    InputInvoiceResponsibleEntity getByResponsibleAndCategory(InputInvoiceResponsibleEntity invoiceResponsibleEntity);

    void updateByResponsibleAndCategory(InputInvoiceResponsibleEntity invoiceResponsibleEntity);

    PageUtils findList(Map<String, Object> params);

    InputInvoiceResponsibleEntity get(InputInvoiceResponsibleEntity invoiceResponsibleEntity);

    List<InputInvoiceResponsibleEntity> getList();

    void responsibleDelete(Integer[] ids);

    InputInvoiceResponsibleEntity getOneByCondition(InputInvoiceResponsibleEntity invoiceResponsibleEntity);

    InputInvoiceResponsibleEntity getOneById(InputInvoiceResponsibleEntity invoiceResponsibleEntity);

    void updateResponsibleDelete(InputInvoiceResponsibleEntity invoiceResponsibleEntity);

    void deleteAll();

    void insertAll(List<InputInvoiceResponsibleEntity> list);
}
