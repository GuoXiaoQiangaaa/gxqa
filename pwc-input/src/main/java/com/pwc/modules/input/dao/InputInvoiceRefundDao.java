package com.pwc.modules.input.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.modules.input.entity.InputInvoiceRefundEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 发票退票表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2018-12-26 16:58:47
 */
@Mapper
public interface InputInvoiceRefundDao extends BaseMapper<InputInvoiceRefundEntity> {
	
}
