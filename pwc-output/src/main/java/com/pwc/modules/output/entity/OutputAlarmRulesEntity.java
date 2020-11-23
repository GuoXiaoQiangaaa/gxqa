package com.pwc.modules.output.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 报警规则
 *
 * @author fanpf
 * @date 2020/9/25
 */
@Data
@TableName("output_alarm_rules")
public class OutputAlarmRulesEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
	private Long alarmId;
	/**
	 * 企业id
	 */
	@NotNull(message = "所属部门id不能为空")
	private Long deptId;
	/**
	 * 库存id，以此为标准发出库存报警通知
	 */
	@NotNull(message = "库存id不能为空")
	private Long inventoryId;
	/**
	 * 报警内容: 1:库存不足; 2:库存过多
	 */
	@NotBlank(message = "报警内容不能为空")
	private String alarmDesc;
	/**
	 * 报警数量
	 */
	@NotBlank(message = "报警临界数量不能为空")
	private String quantity;
	/**
	 * 运算 LT:小于; LTE:小于等于; GT:大于; GTE:大于等于;
	 */
	@NotBlank(message = "报警运算参数不能为空")
	private String operation;
	/**
	 * 
	 */
	private Long createBy;
	/**
	 * 
	 */
	private Date createTime;
	/**
	 * 
	 */
	private Long updateBy;
	/**
	 * 
	 */
	private Date updateTime;

}
