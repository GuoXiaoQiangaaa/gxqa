package com.pwc.modules.filing.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 申报节点设置
 * 
 * @author zk
 * @email 
 * @date 2019-11-08 15:11:47
 */
@Data
@TableName("filing_node")
public class FilingNodeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 节点ID
	 */
	@TableId
	private Long nodeId;
	/**
	 * 公司id
	 */
	@NotNull(message = "部门不能为空")
	private Long deptId;
	/**
	 * 公司名称
	 */
	private String deptName;
	/**
	 * 申报上传截止日期
	 */
	private String uploadDate;
	/**
	 * 报告确认截止日期
	 */
	private String reportConfirmDate;
	/**
	 * 申报截止日期
	 */
	private String declareDate;
	/**
	 * 扣款截止日期
	 */
	private String deductionDate;
	/**
	 * 状态 0 未修改 1 子公司已修改
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



}
