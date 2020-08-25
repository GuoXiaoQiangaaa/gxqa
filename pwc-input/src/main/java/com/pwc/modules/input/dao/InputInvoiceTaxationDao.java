package com.pwc.modules.input.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.modules.input.entity.InputInvoiceTaxationEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author gxw
 * @date 2020/6/23 19:06
 */
@Mapper
public interface InputInvoiceTaxationDao  extends BaseMapper<InputInvoiceTaxationEntity> {
    void updateByFlag(Integer invoiceId,String[] ids);
}
