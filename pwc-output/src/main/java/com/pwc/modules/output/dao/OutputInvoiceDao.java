package com.pwc.modules.output.dao;

import com.pwc.modules.output.entity.OutputInvoiceEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 销项发票持久层
 *
 * @author fanpf
 * @date 2020/9/24
 */
@Mapper
public interface OutputInvoiceDao extends BaseMapper<OutputInvoiceEntity> {
	
}
