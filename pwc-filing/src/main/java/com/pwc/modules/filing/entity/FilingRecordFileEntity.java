package com.pwc.modules.filing.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 申报附件表
 * 
 * @author zk
 * @email 
 * @date 2020-01-07 16:27:18
 */
@Data
@TableName("filing_record_file")
public class FilingRecordFileEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
	private Long id;
	/**
	 * 申报ID
	 */
	private Long filingId;
	/**
	 * 文件ID
	 */
	private Long fileId;
	/**
	 * 类型：1.申报上传 2.报告确认 3.申报 4.扣款
	 */
	private Integer type;
	/**
	 * 
	 */
	private Date createTime;
	/**
	 * 文件报税类型 VAT
	 */
	private String fileType;

}
