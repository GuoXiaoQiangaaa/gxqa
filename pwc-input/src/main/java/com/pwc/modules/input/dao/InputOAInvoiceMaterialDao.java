package com.pwc.modules.input.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.modules.input.entity.InputOAInvoiceInfo;
import com.pwc.modules.input.entity.InputOAInvoiceMaterial;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InputOAInvoiceMaterialDao extends BaseMapper<InputOAInvoiceMaterial> {
    void save(InputOAInvoiceMaterial oaInvoiceMaterial);
    List<InputOAInvoiceMaterial> getByInvoiceId(InputOAInvoiceMaterial oaInvoiceMaterial);
    void deleteByInvoiceId(InputOAInvoiceInfo oaInvoiceInfo);
}
