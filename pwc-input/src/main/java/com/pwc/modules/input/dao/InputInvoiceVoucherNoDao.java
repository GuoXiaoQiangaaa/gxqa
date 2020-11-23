package com.pwc.modules.input.dao;

import com.pwc.modules.input.entity.InputInvoiceVoucherNoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 发票入账 凭证号和发票对应表
 * 
 * @author zlb
 * @email 
 * @date 2020-08-19 12:08:28
 */
@Mapper
public interface InputInvoiceVoucherNoDao extends BaseMapper<InputInvoiceVoucherNoEntity> {
	
}
