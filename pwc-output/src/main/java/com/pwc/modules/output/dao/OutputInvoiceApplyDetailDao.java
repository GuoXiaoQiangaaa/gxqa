package com.pwc.modules.output.dao;

import com.pwc.modules.output.entity.OutputInvoiceApplyDetailEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 开票申请明细持久层
 *
 * @author fanpf
 * @date 2020/9/24
 */
@Mapper
public interface OutputInvoiceApplyDetailDao extends BaseMapper<OutputInvoiceApplyDetailEntity> {
	
}
