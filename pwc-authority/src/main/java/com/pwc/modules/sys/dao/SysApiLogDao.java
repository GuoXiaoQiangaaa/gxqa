package com.pwc.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.modules.sys.entity.SysApiLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 请求接口日志
 * 
 * @author zlb
 * @email 
 * @date 2020-08-28 18:07:21
 */
@Mapper
public interface SysApiLogDao extends BaseMapper<SysApiLogEntity> {
	
}
