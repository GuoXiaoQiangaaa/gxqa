package com.pwc.modules.input.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pwc.common.excel.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author myz
 * @email 
 * @date 2020-12-09 11:35:43
 */
@Data
@TableName("input_sap_match_result")
public class InputSapMatchResultEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
	private Integer resultId;
	/**
	 * 年份月份（202011）
	 */
	@ExcelField(title = "年月", align = 1, sort = 1)
	private String yearAndMonth;
	/**
	 * 分类(0-发票 1-海关缴款书 2-红字通知单)
	 */
	@ExcelField(title = "分类", align = 1, sort = 1,dictType = "sapSort")
	private Integer sort;
	/**
	 * 入账税额
	 */
	@ExcelField(title = "入账税额", align = 1, sort = 1)
	private String credittax;
	/**
	 * 认证税额
	 */
	@ExcelField(title = "认证税额", align = 1, sort = 1)
	private String certificationtax;
	/**
	 * 本月入账未认证
	 */
	@ExcelField(title = "本月入账未认证", align = 1, sort = 1)
	private String monthcredtax;
	/**
	 * 本月认证未入账
	 */
	@ExcelField(title = "本月认证未入账", align = 1, sort = 1)
	private String monthcerttax;
	/**
	 * 前期认证本月入账
	 */
	@ExcelField(title = "前期认证本月入账", align = 1, sort = 1)
	private String monthcredbeforetax;
	/**
	 * 前期入账本月认证
	 */
	@ExcelField(title = "前期入账本月认证", align = 1, sort = 1)
	private String monthcertbeforetax;
	/**
	 * 税会差异
	 */
	@ExcelField(title = "税会差异", align = 1, sort = 1)
	private String differencetax;
	/**
	 * 差异调整合计
	 */
	@ExcelField(title = "差异调整合计", align = 1, sort = 1)
	private String adjustmenttax;
	/**
	 * 核对
	 */
	@ExcelField(title = "核对", align = 1, sort = 1)
	private String checktax;
	/**
	 * 数据状态（0-未锁定 1-被锁定）
	 */
	private Integer status;
	/**
	 * 手动输入校准值
	 */
	@ExcelField(title = "手动输入校准值", align = 1, sort = 1)
	private String calibration;
	/**
	 * 校准值说明
	 */
	@ExcelField(title = "校准值说明", align = 1, sort = 1)
	private String calibrationExplain;
	/**
	 * 创建人
	 */
	private String createBy;
	/**
	 * 创建时间
	 */
	@ExcelField(title = "创建时间", align = 1, sort = 1)
	private Date createTime;
	/**
	 * 修改人
	 */
	private String updateBy;
	/**
	 * 更新时间
	 */
	private Date updateTime;
	/**
	 * 部门id
	 */
	private int deptId;

}
