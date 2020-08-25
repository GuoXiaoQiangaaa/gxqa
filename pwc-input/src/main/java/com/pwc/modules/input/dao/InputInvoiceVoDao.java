package com.pwc.modules.input.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.modules.input.entity.InputInvoiceVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InputInvoiceVoDao extends BaseMapper<InputInvoiceVo> {
    List<InputInvoiceVo> getListById(InputInvoiceVo invoiceVo);
}
