package com.pwc.modules.data.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

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
	private String sapCode;
	/**
	 * 公司代码
	 */
	private Long deptCode;
	/**
	 * 供应商名称
	 */
	private String name;
	/**
	 * 纳税人识别号
	 */
	private String taxCode;
	/**
	 * 地址
	 */
	private String address;
	/**
	 * 联系电话
	 */
	private String contact;
	/**
	 * 开户行
	 */
	private String bank;
	/**
	 * 银行账号
	 */
	private String bankAccount;
	/**
	 * 供应商邮箱
	 */
	private String email;
	/**
	 * 发票分类(0:NonPo Related; 1:MKRO; 2:DFU; 3:EDI; 4:R&D_外部; 5:IC_R&D; 6:IC_RRB; 7:IC_非R&D; 8:Red-letter VAT; 9:General)
	 */
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
