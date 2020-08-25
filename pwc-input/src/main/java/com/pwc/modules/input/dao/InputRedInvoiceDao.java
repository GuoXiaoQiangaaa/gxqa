package com.pwc.modules.input.dao;

import com.pwc.modules.input.entity.InputRedInvoiceEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 红字发票持久层
 *
 * @author fanpf
 * @date 2020/8/25
 */
@Mapper
public interface InputRedInvoiceDao extends BaseMapper<InputRedInvoiceEntity> {
	
}
