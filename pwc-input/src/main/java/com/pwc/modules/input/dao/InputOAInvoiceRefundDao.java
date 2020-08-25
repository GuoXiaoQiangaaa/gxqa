package com.pwc.modules.input.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.modules.input.entity.InputOAInvoiceRefund;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InputOAInvoiceRefundDao extends BaseMapper<InputOAInvoiceRefund> {
    /**
     * @param oaInvoiceRefund
     * @return
     * 根据invoiceId查询数据
     */
    InputOAInvoiceRefund getByInvoiceId(InputOAInvoiceRefund oaInvoiceRefund);

    /**
     * @param oaInvoiceRefund
     * 根据invoiceId更新数据
     */
    void  updateByInvoiceId(InputOAInvoiceRefund oaInvoiceRefund);
}
