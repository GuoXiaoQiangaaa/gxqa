package com.pwc.modules.data.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 科目清单表
 *
 * @author fanpf
 * @date 2020/8/27
 */
@Data
@TableName("output_item_list")
public class OutputItemListEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 科目主键
	 */
	@TableId
	private Long itemId;
	/**
	 * 科目编码
	 */
	private String itemCode;
	/**
	 * 科目类型
	 */
	private String itemType;
	/**
	 * 科目描述
	 */
	private String description;
	/**
	 * 是否停用(0:停用;1:正常)
	 */
	private String delFlag;
	/**
	 * 创建人
	 */
	private String createBy;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 修改人
	 */
	private String updateBy;
	/**
	 * 更新时间
	 */
	private Date updateTime;

}
