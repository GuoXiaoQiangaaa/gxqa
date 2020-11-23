package com.pwc.modules.output.dao;

import com.pwc.modules.output.entity.OutputInvoiceApplyEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 开票申请持久层
 *
 * @author fanpf
 * @date 2020/9/24
 */
@Mapper
public interface OutputInvoiceApplyDao extends BaseMapper<OutputInvoiceApplyEntity> {
	
}
