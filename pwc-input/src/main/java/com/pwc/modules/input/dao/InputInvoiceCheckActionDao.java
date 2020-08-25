package com.pwc.modules.input.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.modules.input.entity.InputInvoiceCheckAction;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InputInvoiceCheckActionDao extends BaseMapper<InputInvoiceCheckAction> {
    void updateByAction(InputInvoiceCheckAction invoiceCheckAction);
    List<InputInvoiceCheckAction> findList();
    InputInvoiceCheckAction getByItem(InputInvoiceCheckAction invoiceCheckAction);
}
