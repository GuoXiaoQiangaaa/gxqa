package com.pwc.modules.data.dao;

import com.pwc.modules.data.entity.OutputSapTaxListEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * SAP税码清单持久层
 *
 * @author fanpf
 * @date 2020/8/27
 */
@Mapper
public interface OutputSapTaxListDao extends BaseMapper<OutputSapTaxListEntity> {
	
}
