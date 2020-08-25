package com.pwc.modules.input.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.modules.input.entity.InputInvoiceUnitPush;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InputInvoiceUnitPushDao extends BaseMapper<InputInvoiceUnitPush> {
    InputInvoiceUnitPush getByBasicUnits(InputInvoiceUnitPush invoiceUnitPush);
    void updateByDelete(InputInvoiceUnitPush invoiceUnitPush);
    InputInvoiceUnitPush get(InputInvoiceUnitPush invoiceUnitPush);
    void update(InputInvoiceUnitPush invoiceUnitPush);
    void save(InputInvoiceUnitPush invoiceUnitPush);
}
