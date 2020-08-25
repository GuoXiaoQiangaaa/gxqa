package com.pwc.modules.input.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.modules.input.entity.InputInvoiceUnitDifferent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface InputInvoiceUnitDifferentDao extends BaseMapper<InputInvoiceUnitDifferent> {
    InputInvoiceUnitDifferent getByUnit(@Param("unit") String unit, @Param("mblnr") String mblnr);
    InputInvoiceUnitDifferent getByThree(InputInvoiceUnitDifferent invoiceUnitDifferent);
    void updateByDelete(InputInvoiceUnitDifferent invoiceUnitDifferent);
    InputInvoiceUnitDifferent get(InputInvoiceUnitDifferent invoiceUnitDifferent);
    void update(InputInvoiceUnitDifferent invoiceUnitDifferent);
    int getList();
    void save(InputInvoiceUnitDifferent invoiceUnitDifferent);
}
