package com.pwc.modules.data.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pwc.common.excel.annotation.ExcelField;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * 客户信息
 *
 * @author fanpf
 * @date 2020/8/24
 */
@Data
@TableName("output_customer_new")
public class OutputCustomerNewEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 客户主键
	 */
	@TableId
	private Long customerId;
	/**
	 * 客户SAP代码
	 */
	@ExcelField(title = "客户SAP代码（必填）", align = 1, sort = 1)
	@NotBlank(message = "客户SAP代码不能为空")
	private String sapCode;
	/**
	 * 公司代码
	 */
	@ExcelField(title = "公司代码", align = 1, sort = 1)
	private String deptCode;
	/**
	 * 英文客户名称
	 */
	@ExcelField(title = "英文客户名称", align = 1, sort = 1)
	private String name;
	/**
	 * 中文客户名称
	 */
	@ExcelField(title = "中文客户名称（必填）", align = 1, sort = 1)
	@NotBlank(message = "中文客户名称不能为空")
	private String nameCn;
	/**
	 * 纳税人识别号
	 */
	@ExcelField(title = "纳税人识别号（必填）", align = 1, sort = 1)
	@NotBlank(message = "纳税人识别号不能为空")
	private String taxCode;
	/**
	 * 地址
	 */
	@ExcelField(title = "地址（必填）", align = 1, sort = 1)
	@NotBlank(message = "客户地址不能为空")
	private String address;
	/**
	 * 联系电话
	 */
	@ExcelField(title = "电话号码（必填）", align = 1, sort = 1)
	@NotBlank(message = "客户电话不能为空")
	private String contact;
	/**
	 * 开户行
	 */
	@ExcelField(title = "开户行", align = 1, sort = 1)
	private String bank;
	/**
	 * 银行账号
	 */
	@ExcelField(title = "银行账号", align = 1, sort = 1)
	private String bankAccount;
	/**
	 * 客户邮箱
	 */
	@ExcelField(title = "客户邮箱", align = 1, sort = 1)
	private String email;
	/**
	 * 是否停用(0:停用;1:正常)
	 */
	private String delFlag;
	/**
	 * 创建人
	 */
	@TableField(fill = FieldFill.DEFAULT)
	private String createBy;
	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.DEFAULT)
	private Date createTime;
	/**
	 * 修改人
	 */
	@TableField(fill = FieldFill.DEFAULT)
	private String updateBy;
	/**
	 * 更新时间
	 */
	@TableField(fill = FieldFill.DEFAULT)
	private Date updateTime;

}
