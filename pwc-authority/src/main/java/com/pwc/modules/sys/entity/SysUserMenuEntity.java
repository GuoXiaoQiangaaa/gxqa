package com.pwc.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * 
 * @author zk
 * @email 
 * @date 2020-08-19 16:06:55
 */
@Data
@TableName("sys_user_menu")
public class SysUserMenuEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 模块id
	 */
	private Long menuId;
	/**
	 * 企业id
	 */
	private Long deptId;
	/**
	 * 用户id
	 */
	private Long userId;
	/**
	 * 只看自己数据
	 */
	private Integer justOwn;

}
