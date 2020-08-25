package com.pwc.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 通知角色关联
 * 
 * @author zk
 * @email 
 * @date 2020-02-11 18:06:10
 */
@Data
@TableName("sys_notice_role")
public class SysNoticeRoleEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 通知id
	 */
	private Long noticeId;
	/**
	 * 角色id
	 */
	private Long roleId;

}
