

package com.pwc.modules.job.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.modules.job.entity.ScheduleJobLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 定时任务日志
 *
 * @author
 */
@Mapper
public interface ScheduleJobLogDao extends BaseMapper<ScheduleJobLogEntity> {
	
}
