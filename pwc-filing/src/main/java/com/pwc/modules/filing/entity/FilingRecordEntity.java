package com.pwc.modules.filing.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pwc.modules.sys.entity.SysFileEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 申报流程信息
 * 
 * @author zk
 * @email 
 * @date 2019-11-08 15:11:47
 */
@Data
@TableName("filing_record")
public class FilingRecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long filingId;
	/**
	 * 组织
	 */
	private Long deptId;
	/**
	 * 公司名
	 */
	private String deptName;
	/**
	 * 申报上传状态
	 */
	private Integer uploadStatus;
	/**
	 * 确认报告状态
	 */
	private Integer confirmReportStatus;
	/**
	 * 报告审核描述
	 */
	private String reportAuditMemo;
	/**
	 * 申报状态
	 */
	private Integer declareStatus;
	/**
	 * 扣款状态
	 */
	private Integer deductionStatus;
	/**
	 * 申报上传Time
	 */
	private Date uploadTime;
	/**
	 * 确认报告Time
	 */
	private Date comfirmReportTime;
	/**
	 * 申报回执Time
	 */
	private Date declareTime;
	/**
	 * 扣款回执Time
	 */
	private Date deductionTime;
	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private Date createTime;
	/**
	 * 创建人
	 */
	@TableField(fill = FieldFill.INSERT)
	private String createBy;
	/**
	 * 修改时间
	 */
	@TableField(fill = FieldFill.UPDATE)
	private Date updateTime;
	/**
	 * 修改人
	 */
	@TableField(fill = FieldFill.UPDATE)
	private String updateBy;
	/**
	 * 关联节点？
	 */
	private Long nodeId;
	/**
	 * 完成状态 0未开始 1进行中 2 已完成 -1 文件已销毁
	 */
	private Integer status;
	/**
	 * 当前分公司是否支持第三方 0 否 1是
	 */
	@TableField(exist = false)
	private int third;

	/**
	 * 文件信息
	 */
	@TableField(exist = false)
	private SysFileEntity uploadFile;

	/**
	 * 文件信息
	 */
	@TableField(exist = false)
	private SysFileEntity reportFile;

	/**
	 * 文件信息
	 */
	@TableField(exist = false)
	private SysFileEntity declareFile;

	/**
	 * 文件信息
	 */
	@TableField(exist = false)
	private SysFileEntity deductionFile;

	@TableField(exist = false)
	private Long userId;

}
