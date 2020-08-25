package com.pwc.modules.data.dao;

import com.pwc.modules.data.entity.OutputCustomerNewEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 客户信息持久层
 *
 * @author fanpf
 * @date 2020/8/24
 */
@Mapper
public interface OutputCustomerNewDao extends BaseMapper<OutputCustomerNewEntity> {
	
}
