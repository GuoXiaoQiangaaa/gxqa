package com.pwc.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 税种表
 * 
 * @author zk
 * @email 
 * @date 2019-12-31 17:21:04
 */
@Data
@TableName("sys_tax")
public class SysTaxEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long taxId;
	/**
	 * 税种名称
	 */
	private String name;

}
