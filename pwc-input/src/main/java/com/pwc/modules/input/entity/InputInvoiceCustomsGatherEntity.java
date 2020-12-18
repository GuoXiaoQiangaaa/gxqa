package com.pwc.modules.input.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pwc.common.utils.excel.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 海关缴款书（同步）
 * 
 * @author zk
 * @email 
 * @date 2020-12-16 13:26:51
 */
@Data
@TableName("input_invoice_customs_gather")
public class InputInvoiceCustomsGatherEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 当前税款所属期
	 */
	@ExcelField(title="专用缴款书号码", align=1, sort=20)
	private String period;
	/**
	 * 海关缴款书号码
	 */
	private String payNo;
	/**
	 * 填发日期
	 */
	@ExcelField(title="填发日期", align=1, sort=30)
	private String billingDate;
	/**
	 * 税款金额
	 */
	@ExcelField(title="税额", align=1, sort=40)
	private String totalTax;
	/**
	 * 发票类型 17 海关缴款书
	 */
	private String invoiceType;
	/**
	 * 缴款单位税号
	 */
	@ExcelField(title="申报公司税号", align=1, sort=60)
	private String purchaserTaxNo;
	/**
	 * 缴款单位名称
	 */
	@ExcelField(title="所属公司", align=1, sort=50)
	private String purchaserName;
	/**
	 * 信息来源 1：系统推送 2：数据采集 3：核查转入
	 */
	private String infoSources;
	/**
	 * 有效税额 当deductibleMode=1、4、5时不为空
	 */
	private String effectiveTaxAmount;
	/**
	 * 异常类型，金税异常描述
	 * 1-成功
	 * 2-缴款书号码不合法
	 * 3-无此税号
	 * 4-填发日期已逾期
	 * 5-税款金额不合法（不能为负数）
	 * 6-已采集（已经做过采集） 7-已存在（未认证或已认证）
	 * 8-其它异常
	 * 9-不符合不符录入条件
	 */
	@ExcelField(title="金税返回信息", align=1, sort=110, dictType = "abnormalType")
	private String abnormalType;
	/**
	 * 缴款书来源 0同步 1手工上传
	 */
	private String uploadType;
	/**
	 * 稽核结果0-稽核中
	 * 1-相符
	 * 2-不符
	 * 3-缺联
	 * 4-重号
	 * 5-历史相符(缴款书不可勾选)
	 * 99-已撤销（不符录入后做了撤销操作返回此结果）
	 */
	@ExcelField(title="稽核结果", align=1, sort=90 , dictType = "auditStatus")
	private String auditStatus;
	/**
	 * 采集日期
	 */
	@ExcelField(title="采集日期", align=1, sort=100)
	private Date gatherDate;
	/**
	 * 采集状态(1-采集中 2-采集成功 3-采集失败 4-申请采集失败)
	 */
	@ExcelField(title="采集状态", align=1, sort=80 , dictType = "gatherStatus")
	private String gatherStatus;
	/**
	 * 上传日期
	 */
	@ExcelField(title="导入日期", align=1, sort=70)
	private Date uploadDate;
	/**
	 * 上传人
	 */
	private Integer uploadBy;
	/**
	 * 更新时间
	 */
	private Date updateTime;
	/**
	 * 更新人
	 */
	private Integer updateBy;
	/**
	 * 批次号(格式:yyyyMMdd-1)
	 */
	@ExcelField(title="批次号", align=1, sort=10)
	private String batchNo;
	/**
	 * 是否删除（0-否 1-是）
	 */
	private Integer delFlag;
	/**
	 * 请求金税获取的request_id，用于获取结果集
	 */
	private String requestId;
	/**
	 * 部门ID
	 */
	private Integer deptId;

}
