package com.pwc.modules.data.dao;

import com.pwc.modules.data.entity.OutputItemListEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 科目清单持久层
 *
 * @author fanpf
 * @date 2020/8/27
 */
@Mapper
public interface OutputItemListDao extends BaseMapper<OutputItemListEntity> {
	
}
