package com.pwc.modules.input.dao;

import com.pwc.modules.input.entity.InputInvoiceCustomsPushEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 海关缴款书SAP入账或冲销凭证信息推送表
 * 
 * @author zlb
 * @email 
 * @date 2020-08-12 17:01:42
 */
@Mapper
public interface InputInvoiceCustomsPushDao extends BaseMapper<InputInvoiceCustomsPushEntity> {
	
}
