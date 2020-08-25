package com.pwc.modules.filing.dao;

import com.pwc.modules.filing.entity.FilingOperateLogEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 申报过程操作记录
 * 
 * @author zk
 * @email 
 * @date 2019-11-08 15:11:47
 */
@Mapper
public interface FilingOperateLogDao extends BaseMapper<FilingOperateLogEntity> {
	
}
