package com.pwc.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 第三方地区code
 * 
 * @author zk
 * @email 
 * @date 2020-02-03 15:54:10
 */
@Data
@TableName("filing_third_district")
public class FilingThirdDistrictEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 城市编码
	 */
	private String cityCode;
	/**
	 * 城市名
	 */
	private String cityName;

	/**
	 * 关联系统的已设置地区列表名称
	 */
	@TableField(exist = false)
	private List<String> setCityNames;

}
