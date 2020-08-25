package com.pwc.modules.input.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.modules.input.entity.InputInvoiceFaultTolerantEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * 
 * @author QIU
 * @email sunlightcs@gmail.com
 * @date 2018-12-13 09:31:08
 */
@Mapper
public interface InputInvoiceFaultTolerantDao extends BaseMapper<InputInvoiceFaultTolerantEntity> {
    InputInvoiceFaultTolerantEntity getOne();

    InputInvoiceFaultTolerantEntity getByName(InputInvoiceFaultTolerantEntity invoiceFaultTolerantEntity);
}
