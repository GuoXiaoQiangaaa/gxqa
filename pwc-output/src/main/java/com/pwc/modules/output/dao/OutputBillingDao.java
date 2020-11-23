package com.pwc.modules.output.dao;

import com.pwc.modules.output.entity.OutputBillingEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 销项Billing表持久层
 *
 * @author fanpf
 * @date 2020/9/25
 */
@Mapper
public interface OutputBillingDao extends BaseMapper<OutputBillingEntity> {
	
}
