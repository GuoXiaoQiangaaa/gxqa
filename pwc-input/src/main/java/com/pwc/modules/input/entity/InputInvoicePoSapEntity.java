package com.pwc.modules.input.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author myz
 * @email 
 * @date 2020-12-03 16:28:38
 */
@Data
@TableName("input_invoice_po_sap")
public class InputInvoicePoSapEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
	private Integer poSapId;
	/**
	 * 
	 */
	private String outlineAgreement;
	/**
	 * 
	 */
	private String purchasingDocument;
	/**
	 * 
	 */
	private String item;
	/**
	 * 
	 */
	private String documentDate;
	/**
	 * 
	 */
	private String purchOrganization;
	/**
	 * 
	 */
	private String plant;
	/**
	 * 
	 */
	private String material;
	/**
	 * 
	 */
	private String nameOfVendor;
	/**
	 * 
	 */
	private String shortText;
	/**
	 * 
	 */
	private String orderQuantity;
	/**
	 * 
	 */
	private String orderUnit;
	/**
	 * 
	 */
	private BigDecimal netPrice;
	/**
	 * 
	 */
	private String currency;
	/**
	 * 
	 */
	private String netOrderValue;
	/**
	 * 
	 */
	private String qtyDelivered;
	/**
	 * 
	 */
	private String valDelivered;
	/**
	 * 
	 */
	private String qtyInvoiced;
	/**
	 * 
	 */
	private String valInvoiced;
	/**
	 * 
	 */
	private String assignmentCat;
	/**
	 * 
	 */
	private String purchasingGroup;
	/**
	 * 
	 */
	private String materialGroup;
	/**
	 * 
	 */
	private String trackingNumber;
	/**
	 * 
	 */
	private String deletionIndicator;
	/**
	 * 
	 */
	private String itemCategory;
	/**
	 * 插入日期
	 */
	private Date createTime;
	/**
	 * 更新日期
	 */
	private Date updateTime;
	/**
	 * 上传人
	 */
	private String createBy;
	/**
	 * 操作人
	 */
	private String updateBy;
	/**
	 * 关联上传id
	 */
	private Integer uploadId;
	/**
	 * 公司id
	 */
	private Integer deptId;

}
