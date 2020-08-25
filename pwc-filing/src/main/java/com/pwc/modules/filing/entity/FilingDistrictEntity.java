package com.pwc.modules.filing.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 地区设置
 * 
 * @author zk
 * @email 
 * @date 2019-11-13 18:47:17
 */
@Data
@TableName("filing_district")
public class FilingDistrictEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
	private Long districtId;
	/**
	 * 省名
	 */
	private String provinceName;
	/**
	 * 市名
	 */
	private String cityName;
	/**
	 * 区名
	 */
	private String districtName;
	/**
	 * 省号
	 */
	private String provinceCode;
	/**
	 * 市号
	 */
	private String cityCode;
	/**
	 * 区号
	 */
	private String districtCode;
	/**
	 * 父节点code
	 */
	private Long parentId;
	/**
	 * 状态 0 禁用 1 启用
	 */
	private Integer status;
	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private Date createTime;
	/**
	 * 创建人
	 */
	@TableField(fill = FieldFill.INSERT)
	private String createBy;
	/**
	 * 修改时间
	 */
	@TableField(fill = FieldFill.UPDATE)
	private Date updateTime;
	/**
	 * 修改人
	 */
	@TableField(fill = FieldFill.UPDATE)
	private String updateBy;
	/**
	 * 生效时间
	 */
	private Date effectTime;

	/**
	 * 子地区
	 */
	@TableField(exist = false)
	private List<FilingDistrictEntity> children;

}
