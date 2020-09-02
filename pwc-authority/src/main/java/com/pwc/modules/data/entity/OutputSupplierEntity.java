package com.pwc.modules.data.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pwc.common.excel.annotation.ExcelField;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * 供应商信息
 *
 * @author fanpf
 * @date 2020/8/24
 */
@Data
@TableName("output_supplier")
public class OutputSupplierEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 供应商主键
	 */
	@TableId
	private Long supplierId;
	/**
	 * 供应商SAP代码
	 */
	@ExcelField(title = "SAP代码（必填）", align = 1, sort = 1)
	@NotBlank(message = "供应商SAP代码不能为空")
	private String sapCode;
	/**
	 * 公司代码
	 */
	@ExcelField(title = "公司代码", align = 1, sort = 1)
	private String deptCode;
	/**
	 * 供应商名称
	 */
	@ExcelField(title = "供应商名称（必填）", align = 1, sort = 1)
	@NotBlank(message = "供应商名称不能为空")
	private String name;
	/**
	 * 纳税人识别号
	 */
	@ExcelField(title = "纳税人识别号（必填）", align = 1, sort = 1)
	@NotBlank(message = "纳税人识别号不能为空")
	private String taxCode;
	/**
	 * 地址
	 */
	@ExcelField(title = "地址", align = 1, sort = 1)
	private String address;
	/**
	 * 联系电话
	 */
	@ExcelField(title = "电话号码", align = 1, sort = 1)
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
	 * 供应商邮箱
	 */
	@ExcelField(title = "供应商邮箱", align = 1, sort = 1)
	private String email;
	/**
	 * 发票分类(0:NonPo Related; 1:MKRO; 2:DFU; 3:EDI; 4:R&D_外部; 5:IC_R&D; 6:IC_RRB; 7:IC_非R&D; 8:Red-letter VAT; 9:General)
	 */
	@ExcelField(title = "分类", align = 1, sort = 1)
	private String invoiceType;
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
