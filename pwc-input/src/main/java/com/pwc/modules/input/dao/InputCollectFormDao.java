package com.pwc.modules.input.dao;

import com.pwc.modules.input.entity.InputCollectFormEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 进项汇总报表持久层
 *
 * @author fanpf
 * @date 2020/9/16
 */
@Mapper
public interface InputCollectFormDao extends BaseMapper<InputCollectFormEntity> {
	
}
