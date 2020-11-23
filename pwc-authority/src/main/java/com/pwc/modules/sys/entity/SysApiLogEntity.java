package com.pwc.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 请求接口日志
 * 
 * @author zlb
 * @email 
 * @date 2020-08-28 18:07:21
 */
@Data
@TableName("sys_api_log")
public class SysApiLogEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 操作人
	 */
	private String createBy;
	/**
	 * 请求时间
	 */
	private String requestTime;
	/**
	 * 请求报文
	 */
	private String requestText;
	/**
	 * 返回时间
	 */
	private String responseTime;
	/**
	 * 返回报文
	 */
	private String responseText;
	/**
	 * 分类：1发票传入接口，2发票入账接口，3发票查验接口，4发票勾选接口，5海关缴款书同步，6海关缴款书勾选，7统计接口
	 */
	private String type;

}
