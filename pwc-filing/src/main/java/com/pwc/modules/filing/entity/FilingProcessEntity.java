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
 * 申报流程设置
 * 
 * @author zk
 * @email 
 * @date 2019-11-08 15:11:47
 */
@Data
@TableName("filing_process")
public class FilingProcessEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long processId;
	/**
	 * 组织id
	 */
	@NotNull
	private Long deptId;
	/**
	 * 公司名称
	 */
	private String deptName;
	/**
	 * 申报上传状态 此步骤是否开启
	 */
	private Integer uploadStatus;
	/**
	 * 确认报告步骤 是否开启 0 关闭 1 开启 
	 */
	private Integer confirmReportStatus;
	/**
	 * 确认步骤是否需要上传 草稿截图
	 */
	private Integer reportUploadStatus;
	/**
	 * 申报步骤是否开启
	 */
	private Integer declareStatus;
	/**
	 * 扣款步骤设置是否开启
	 */
	private Integer deductionStatus;
	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private Date createTime;
	/**
	 * 状态 0 未修改 1 子公司已修改
	 */
	private Integer status;
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
