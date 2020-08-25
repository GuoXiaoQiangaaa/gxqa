package com.pwc.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 公告表
 * 
 * @author zk
 * @email 
 * @date 2019-12-09 19:03:00
 */
@Data
@TableName("sys_announcement")
public class SysAnnouncementEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 公告id
	 */
	@TableId
	private Long announcementId;
	/**
	 * 标题
	 */
	@NotNull(message = "公告标题不能为空")
	private String title;
	/**
	 * 公告类型 1.申报 2.销项 3.进项 4.其他
	 */
	@NotNull(message = "公告类型不能为空")
	private Integer type;
	/**
	 * 公告内容
	 */
	@NotNull(message = "公告内容不能为空")
	private String content;
	/**
	 * 通知角色 ，多个已，分开
	 */
	private String noticeRole;
	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private Date createTime;
	/**
	 * 删除标识
	 */
	@TableLogic
	private Integer delFlag;
	/**
	 * 状态
	 */
	private Integer status;
	@TableField(fill = FieldFill.UPDATE)
	private Date updateTime;
	@TableField(fill = FieldFill.INSERT)
	private String createBy;
	@TableField(fill = FieldFill.UPDATE)
	private String updateBy;

}
