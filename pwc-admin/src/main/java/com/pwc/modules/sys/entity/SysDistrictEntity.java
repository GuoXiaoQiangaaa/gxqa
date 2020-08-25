package com.pwc.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 行政区字典
 * 
 * @author zk
 * @email 
 * @date 2019-11-13 18:54:25
 */
@Data
@TableName("sys_district")
public class SysDistrictEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer id;
	/**
	 * 
	 */
	private String name;
	/**
	 * 
	 */
	private Integer parentId;
	/**
	 * 
	 */
	private String initial;
	/**
	 * 
	 */
	private String initials;
	/**
	 * 
	 */
	private String pinyin;
	/**
	 * 
	 */
	private String extra;
	/**
	 * 
	 */
	private String suffix;
	/**
	 * 
	 */
	private String code;
	/**
	 * 
	 */
	private String areaCode;
	/**
	 * 
	 */
	private Integer order;


	@Transient
	private List<SysDistrictEntity> children;

}
