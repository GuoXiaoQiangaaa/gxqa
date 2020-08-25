package com.pwc.modules.input.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pwc.common.utils.excel.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;

/**
 *
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2018-12-13 09:31:08
 */
@Data
@TableName("input_company")
public class InputCompanyEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	private Integer id;
	/**
	 * 企业编号
	 */
	@ExcelField(title="企业编号", align=1, sort=1)
	private String companyNumber;
	/**
	 * 企业名称
	 */
	@ExcelField(title="企业名称", align=1, sort=1)
	private String companyName;
	/**
	 * 税号
	 */
	@ExcelField(title="税号", align=1, sort=1)
	private String companyDutyParagraph;
	/**
	 * 地址电话
	 */
	@ExcelField(title="地址电话", align=1, sort=1)
	private String companyAddressPhone;
	/**
	 * 银行账号
	 */
	@ExcelField(title="银行账号", align=1, sort=1)
	private String companyBankAccount;
	/**
	 * 申请统计业务执行状态
	 */
	private String applyResult;
	/**
	 * 确认统计业务执行状态
	 */
	private String censusResult;
	/**
	 * 申请任务编码
	 */
	private String applyTaskno;

	/**
	 * 状态0: 未开始1：已获取统计结果 2：已确认统计
	 * 新状态：0: 未开始,1统计中，2，统计撤销中,3：已获取统计结果 ,4确认中，5确认撤销中6：已确认统计
	 */
	private String status;

	/**
	 * 统计结果任务编码
	 */
	private String censusTaskno;


	/**
	 * 统计月份
	 */
	private String statisticsMonth;

	/**
	 * 统计时间
	 */
	private String statisticsTime;

	/**
	 * 企业id
	 */
	private Long deptId;

	/**
	 * 地址
	 */
	@TableField(exist = false)
	private String address;

	private String requestId;

}
