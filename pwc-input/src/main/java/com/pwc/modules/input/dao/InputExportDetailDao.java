package com.pwc.modules.input.dao;

import com.pwc.modules.input.entity.InputExportDetailEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 进项转出明细表持久层
 *
 * @author fanpf
 * @date 2020/9/17
 */
@Mapper
public interface InputExportDetailDao extends BaseMapper<InputExportDetailEntity> {
	
}
