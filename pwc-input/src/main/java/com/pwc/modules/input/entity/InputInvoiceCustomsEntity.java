package com.pwc.modules.input.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pwc.common.utils.excel.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 海关缴款书（同步）
 * 
 * @author zlb
 * @email 
 * @date 2020-08-10 18:53:50
 */
@Data
@TableName("input_invoice_customs")
public class InputInvoiceCustomsEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 海关缴款书号码
	 */
	@ExcelField(title="专用缴款书号码", align=1, sort=10)
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
	private String purchaserTaxNo;
	/**
	 * 缴款单位名称
	 */
	@ExcelField(title="所属公司", align=1, sort=50)
	private String purchaserName;
	/**
	 * 勾选状态 0:未勾选认证; 1:已勾选认证; 2:勾选中; 3:勾选认证失败; 4:撤销勾选认证中; 5:已作废
	 */
	@ExcelField(title="海关缴款书状态", align=1, sort=90,dictType = "deductible")
	private String deductible;
	/**
	 * 勾选属期 YYYYMM
	 */
	@ExcelField(title="海关缴款书勾选所属期", align=1, sort=70)
	private String deductiblePeriod;
	/**
	 * 勾选日期 YYYY-MM-DD
	 */
	@ExcelField(title="海关缴款书勾选日期", align=1, sort=60)
	private String deductibleDate;
	/**
	 * 勾选结果
	 * 1:成功; 2:无此票; 3:该票异常无法勾选; 4:该票已经勾选; 5:该票不在勾选范围内; 6:该票已经申请勾选;
	 * 7:申请勾选月份已过期; 8:其它异常; 9:勾选类型错误; 11:有效税额异常(有效税额大于实际税额);
	 * 12:当期已锁定; 13:勾选异常; 14:该缴款书已申请核查
	 */
	@ExcelField(title="认证接口返回信息", align=1, sort=80)
	private String deductibleRes;
	/**
	 * 勾选类型  1-抵扣 2-退税 3-代办退税 4 -不抵扣 6-撤销抵扣 7-撤销不抵扣
	 */
	private String deductibleMode;
	/**
	 * 管理状态 0：正常 1：异常
	 */
	private String managementStatus;
	/**
	 * 信息来源 1：系统推送 2：数据采集 3：核查转入
	 */
	private String infoSources;
	/**
	 * 有效税额 当deductibleMode=1、4、5时不为空
	 */
	private String effectiveTaxAmount;
	/**
	 * 逾期可勾选标志 0：默认值 1：可勾选逾期
	 */
	//@ExcelField(title="是否为逾期可勾选", align=1, sort=140)
	private String overdueCheckMark;
	/**
	 * 异常类型
	 */
	private String abnormalType;
	/**
	 * 稽核状态（是否录入不符项) 0 否 1 是（不允许勾选）
	 */
	private String auditStatus;
	/**
	 * 转内销证明编号
	 */
	private String resaleCertificateNumber;


	/**
	 * 请求id来请求勾选结果
	 */
	private String requestId;

	//@ExcelField(title="税款所属期", align=1, sort=150) 暂时没用
	private String period;
	/**
	 * 入账状态0 1已入账 2匹配差异
	 */
	//@ExcelField(title="海关缴款书入账状态", align=1, sort=100,dictType ="entryState")
	private String entryState;
	/**
	 * 入账类型 0 定时 1手工
	 */
	private String entryType;

	/**
	 * 入账时间
	 */
	private String  entryDate;

	/**---------------
	 * 凭证编号
	 */
	private String voucherCode;
	/**
	 * 统计状态 0 未统计确认 1 统计确认成功 2 统计确认失败
	 */
	@ExcelField(title="统计确认状态", align=1, sort=100,dictType ="statisticsState")
	private String statisticsState;

	/**
	 * 公司代码
	 */
	private String companyCode;
	/**
	 * 公司名称
	 */
	private String companyName;
	/**
	 * 进口报关单号
	 */
	private String declarationNo;

	/**
	 * 是否退单 0未退 1已退
	 */
	private String invoiceReturn;

	/**
	 * 退单原因
	 */
	private String returnReason;
    /**
     * 缴款书来源 0同步 1手工上传
     */
    private String uploadType;
	/**
	 * 发票序号（手工上传已认证清单）
	 */
	private String  serialNo;
	/**
	 * 上传日期
	 */
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
	 * 根据税号获取所属期错误时候的原因
	 */
	@TableField(exist = false)
	private String  periodErrorInfo;

	/**
	 * 批次号(格式:yyyyMMdd-1)
	 */
	@ExcelField(title="批次号", align=1, sort=20)
	private String batchNo;
	/**
	 * 年月
	 */
	private String yearAndMonth;
	/**
	 * SAP差异原因
	 */
	private String sapReason;
	/**
	 * SAP税额
	 */
	private String sapTax;
	/**
	 * SAP差异额
	 */
	private String sapCheckTax;
	/**
	 * 匹配日期
	 */
	private String matchDate;

}
