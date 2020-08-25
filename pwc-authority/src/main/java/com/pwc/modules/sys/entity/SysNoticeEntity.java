package com.pwc.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 通知代办表
 * 
 * @author zk
 * @email 
 * @date 2020-02-04 17:52:31
 */
@Data
@TableName("sys_notice")
public class SysNoticeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 通知代办id
	 */
	@TableId
	private Long noticeId;
	/**
	 * 申报流程id
	 */
	private Long filingId;
	/**
	 * 企业id
	 */
	private Long deptId;
	/**
	 * 通知内容
	 */
	private String descr;
	/**
	 * 通知时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private Date createTime;
	/**
	 * 用户id
	 */
	private Long userId;
	/**
	 * 通知的角色id
	 */
	private Long roleId;

	@TableField(exist = false)
	private String deptName;

	@TableField(exist = false)
	private String taxCode;

}
