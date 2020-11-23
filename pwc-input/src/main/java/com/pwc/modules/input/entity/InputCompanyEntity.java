package com.pwc.modules.input.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pwc.common.utils.excel.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

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
	 * 统计状态：0:未统计; 1:申请统计中; 2:申请统计成功; 3:申请统计失败; 4:确认统计中; 5:确认统计成功; 6:确认统计失败; 7:统计撤销中
	 */
	private String status;

	/**
	 * 确认任务编码
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

	/**
	 * 请求唯一id，用于获取统计结果
	 */
	private String requestId;

	/**
	 * 创建人
	 */
	private String createBy;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 更新人
	 */
	private String updateBy;

	/**
	 * 更新时间
	 */
	private Date updateTime;

}
