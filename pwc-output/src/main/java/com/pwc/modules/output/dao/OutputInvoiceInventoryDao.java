package com.pwc.modules.output.dao;

import com.pwc.modules.output.entity.OutputInvoiceInventoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 发票库存持久层
 *
 * @author fanpf
 * @date 2020/9/25
 */
@Mapper
public interface OutputInvoiceInventoryDao extends BaseMapper<OutputInvoiceInventoryEntity> {
	
}
