package com.pwc.modules.filing.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 申报过程操作记录
 * 
 * @author zk
 * @email 
 * @date 2019-11-08 15:11:47
 */
@Data
@TableName("filing_operate_log")
public class FilingOperateLogEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long logId;
	/**
	 * 操作人ID
	 */
	private Long userId;
	/**
	 * 关联申请ID
	 */
	private Long filingId;
	/**
	 * 操作描述
	 */
	private String descr;
	/**
	 * 
	 */
	@TableField(fill = FieldFill.INSERT)
	private Date createTime;
	/**
	 * 附件地址
	 */
	private String url;
	/**
	 * 企业id
	 */
	private Long deptId;
	/**
	 * 文件名
	 */
	private String fileName;
	/**
	 * 操作人姓名
	 */
	@TableField(exist = false)
	private String userName;
	/**
	 * 企业名
	 */
	@TableField(exist = false)
	private String deptName;
	/**
	 * 税号
	 */
	@TableField(exist = false)
	private String taxCode;

}
