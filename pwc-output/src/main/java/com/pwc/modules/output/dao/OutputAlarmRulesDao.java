package com.pwc.modules.output.dao;

import com.pwc.modules.output.entity.OutputAlarmRulesEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 报警规则持久层
 *
 * @author fanpf
 * @date 2020/9/25
 */
@Mapper
public interface OutputAlarmRulesDao extends BaseMapper<OutputAlarmRulesEntity> {
	
}
