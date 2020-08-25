package com.pwc.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 与第三方City对应关系表
 * 
 * @author zk
 * @email 
 * @date 2020-02-03 15:54:10
 */
@Data
@TableName("filing_third_city_code")
public class FilingThirdCityCodeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 系统城市编码
	 */
	private String cityCode;
	/**
	 * 第三方支持城市编码
	 */
	private String thirdCityCode;

	@TableField(exist = false)
	private List<String> cityCodeList;

}
