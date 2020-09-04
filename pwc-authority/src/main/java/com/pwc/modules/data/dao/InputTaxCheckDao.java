package com.pwc.modules.data.dao;

import com.pwc.modules.data.entity.InputTaxCheckEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 进项税率校验持久层
 *
 * @author fanpf
 * @date 2020/8/29
 */
@Mapper
public interface InputTaxCheckDao extends BaseMapper<InputTaxCheckEntity> {
	
}
