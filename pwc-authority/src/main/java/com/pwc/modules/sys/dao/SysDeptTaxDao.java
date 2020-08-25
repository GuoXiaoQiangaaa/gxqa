package com.pwc.modules.sys.dao;

import com.pwc.modules.sys.entity.SysDeptTaxEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 企业设置税种关联表
 * 
 * @author zk
 * @email 
 * @date 2019-12-31 17:21:04
 */
@Mapper
public interface SysDeptTaxDao extends BaseMapper<SysDeptTaxEntity> {
	
}
