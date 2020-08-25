

package com.pwc.modules.job.controller;

import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;
import com.pwc.common.validator.ValidatorUtils;
import com.pwc.modules.job.entity.ScheduleJobEntity;
import com.pwc.modules.job.service.ScheduleJobService;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * 定时任务
 *
 * @author
 */
@RestController
@RequestMapping("/sys/schedule")
public class ScheduleJobController {
	@Autowired
	private ScheduleJobService scheduleJobService;
	
	/**
	 * 定时任务列表
	 */
	@GetMapping("/list")
	@RequiresPermissions("sys:schedule:list")
	public R list(@RequestParam Map<String, Object> params){
		PageUtils page = scheduleJobService.queryPage(params);

		return R.ok().put("page", page);
	}
	
	/**
	 * 定时任务信息
	 */
	@GetMapping("/info/{jobId}")
	@RequiresPermissions("sys:schedule:info")
	public R info(@PathVariable("jobId") Long jobId){
		ScheduleJobEntity schedule = scheduleJobService.getById(jobId);
		
		return R.ok().put("schedule", schedule);
	}
	
	/**
	 * 保存定时任务
	 */
	@PutMapping("/save")
	@RequiresPermissions("sys:schedule:save")
	public R save(@RequestBody ScheduleJobEntity scheduleJob){
		ValidatorUtils.validateEntity(scheduleJob);
		
		scheduleJobService.saveJob(scheduleJob);
		
		return R.ok();
	}
	
	/**
	 * 修改定时任务
	 */
	@PostMapping("/update")
	@RequiresPermissions("sys:schedule:update")
	public R update(@RequestBody ScheduleJobEntity scheduleJob){
		ValidatorUtils.validateEntity(scheduleJob);
				
		scheduleJobService.update(scheduleJob);
		
		return R.ok();
	}
	
	/**
	 * 删除定时任务
	 */
	@DeleteMapping("/delete")
	@RequiresPermissions("sys:schedule:delete")
	public R delete(String jobIds){
		Long[] ids = (Long[]) ConvertUtils.convert(jobIds.split(","), Long.class);
		scheduleJobService.deleteBatch(Arrays.asList(ids));
		
		return R.ok();
	}
	
	/**
	 * 立即执行任务
	 */
	@PostMapping("/run")
	@RequiresPermissions("sys:schedule:run")
	public R run(String jobIds){
		Long[] ids = (Long[]) ConvertUtils.convert(jobIds.split(","), Long.class);
		scheduleJobService.run(Arrays.asList(ids));
		
		return R.ok();
	}
	
	/**
	 * 暂停定时任务
	 */
	@PostMapping("/pause")
	@RequiresPermissions("sys:schedule:pause")
	public R pause(String jobIds){
		Long[] ids = (Long[]) ConvertUtils.convert(jobIds.split(","), Long.class);
		scheduleJobService.pause(Arrays.asList(ids));
		
		return R.ok();
	}
	
	/**
	 * 恢复定时任务
	 */
	@PostMapping("/resume")
	@RequiresPermissions("sys:schedule:resume")
	public R resume(String jobIds){
		Long[] ids = (Long[]) ConvertUtils.convert(jobIds.split(","), Long.class);
		scheduleJobService.resume(Arrays.asList(ids));
		
		return R.ok();
	}

}
