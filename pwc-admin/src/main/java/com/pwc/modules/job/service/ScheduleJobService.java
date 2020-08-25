

package com.pwc.modules.job.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.modules.job.entity.ScheduleJobEntity;
import com.pwc.common.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * 定时任务
 *
 * @author
 */
public interface ScheduleJobService extends IService<ScheduleJobEntity> {

	PageUtils queryPage(Map<String, Object> params);

	/**
	 * 保存定时任务
	 */
	void saveJob(ScheduleJobEntity scheduleJob);
	
	/**
	 * 更新定时任务
	 */
	void update(ScheduleJobEntity scheduleJob);
	
	/**
	 * 批量删除定时任务
	 */
	void deleteBatch(List<Long> jobIds);
	
	/**
	 * 批量更新定时任务状态
	 */
	int updateBatch(List<Long>jobIds, int status);
	
	/**
	 * 立即执行
	 */
	void run(List<Long> jobIds);
	
	/**
	 * 暂停运行
	 */
	void pause(List<Long> jobIds);
	
	/**
	 * 恢复运行
	 */
	void resume(List<Long> jobIds);
}
