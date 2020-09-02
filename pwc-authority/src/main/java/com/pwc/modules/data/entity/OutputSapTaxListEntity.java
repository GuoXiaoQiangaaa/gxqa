package com.pwc.modules.data.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pwc.common.excel.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * SAP税码清单表
 *
 * @author fanpf
 * @date 2020/8/27
 */
@Data
@TableName("output_sap_tax_list")
public class OutputSapTaxListEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * sap税码清单主键
	 */
	@TableId
	private Long taxId;
	/**
	 * 税码
	 */
	@ExcelField(title = "税码", align = 1, sort = 1)
	private String taxCode;
	/**
	 * 税种
	 */
	@ExcelField(title = "税种", align = 1, sort = 1)
	private String taxType;
	/**
	 * 描述
	 */
	@ExcelField(title = "描述", align = 1, sort = 1)
	private String description;
	/**
	 * 税率
	 */
	@ExcelField(title = "税率", align = 1, sort = 1)
	private String taxRate;
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
