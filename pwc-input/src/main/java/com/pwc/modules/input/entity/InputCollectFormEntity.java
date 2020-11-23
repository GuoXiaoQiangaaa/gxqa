package com.pwc.modules.input.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pwc.common.excel.annotation.ExcelField;
import lombok.Data;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
 * 进项汇总报表
 *
 * @author fanpf
 * @date 2020/9/16
 */
@Data
@TableName("input_collect_form")
public class InputCollectFormEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 汇总报表主键
	 */
	@TableId
	private Long formId;
	/**
	 * 统计类型:0:认证; 1:转出
	 */
	private String collectType;
	/**
	 * 部门id
	 */
	private Long deptId;
	/**
	 * 部门名称
	 */
	private String deptName;
	/**
	 * 所属期
	 */
	private String authDate;
	/**
	 * 汇总类型:0:AP发票; 1:TE发票; 2:海关缴款书; 3:WHT; 4:认证总计; 5:红字转出; 6:海关免税转出; 7:福利转出; 8:其他转出; 9:转出总计
	 */
	private String itemType;
	/**
	 * 份数
	 */
	@ExcelField(title = "份数", align = 1, sort = 1)
	private Integer itemCount;
	/**
	 * 税额
	 */
	@ExcelField(title = "税额", align = 1, sort = 1)
	private BigDecimal taxPrice;
	/**
	 * 金额
	 */
	@ExcelField(title = "金额", align = 1, sort = 1)
	private BigDecimal totalPrice;
	/**
	 * 创建人
	 */
	private Integer createBy;
	/**
	 * 创建时间
	 */
	@ExcelField(title = "生成日期", align = 1, sort = 1)
	private Date createTime;

}
