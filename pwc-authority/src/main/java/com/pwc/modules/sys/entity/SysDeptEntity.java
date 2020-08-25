

package com.pwc.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;


/**
 * 部门管理
 *
 * @author
 */
@Data
@TableName("sys_dept")
public class SysDeptEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 部门ID
	 */
	@TableId
	private Long deptId;
	/**
	 * 上级部门ID，一级部门为0
	 */
	private Long parentId;
	/**
	 * 部门名称
	 */
	private String name;
	/**
	 * 上级部门名称
	 */
	@TableField(exist=false)
	private String parentName;
	private Integer orderNum;
	@TableLogic
	private Integer delFlag;
	/**
	 * 地址
	 */
	private String address;

	/**
	 * 税号
	 */
	private String taxCode;

	/**
	 * 负责人
	 */
	private String owner;

	/**
	 * 联系方式
	 */
	private String contact;

	/**
	 * 省名
	 */
	private String provinceName;

	/**
	 * 市名
	 */
	private String cityName;

	/**
	 * 区名
	 */
	private String districtName;

	/**
	 * 省编号
	 */
	private String provinceCode;

	/**
	 * 市编号
	 */
	private String cityCode;

	/**
	 * 区编号
	 */
	private String districtCode;

	/**
	 * 启用状态
	 */
	private Integer status;

	/**
	 * 第三方平台企业ID
	 */
	private Long thirdOrgId;
	/**
	 * 登陆账号
	 */
	private String thirdLoginAccount;
	/**
	 * 登录方式
	 */
	private Integer thirdLoginMethod;
	/**
	 * 登录密码
	 */
	private String thirdLoginPassword;
	/**
	 * 申报地区
	 */
	private String thirdRegion;
	/**
	 * 是否可以自动修改 纳税人性质
	 */
	private String thirdCanChange;
	/**
	 * 个税申报密码
	 */
	private String thirdFilingPassowrd;
	/**
	 * 会计准则
	 */
	@NotNull(message = "会计准则不能为空")
	private String thirdAccountingStandards;
	/**
	 * 纳税人身份
	 */
	@NotNull(message = "纳税人身份不能为空")
	private String thirdVatTaxpayer;
	/**
	 * 启用年
	 */
	@NotNull(message = "纳税启用年不能为空")
	private String thirdEnabledYear;
	/**
	 * 启用月份
	 */
	@NotNull(message = "纳税启用月不能为空")
	private String thirdEnabledMonth;
	/**
	 * 企业类型 1. 总集团 2.分公司
	 */
	@NotNull(message = "企业类型不能为空")
	private Integer type;

	/**
	 * 纳税期限代码 06按期 11按次
	 */
	@NotNull(message = "纳税期限代码不能为空")
	private String thirdTaxPeriodCode;
	/**
	 * 小规模纳税人 申报期 1 月 2 季
	 */
	private Integer thirdSmallPeriod;

	/**
	 * ztree属性
	 */
	@TableField(exist=false)
	private Boolean open;
	@TableField(exist=false)
	private List<?> list;
	/**
	 * 下级部门数量
	 */
	@TableField(exist=false)
	private Integer childNum;
	/**
	 * 子公司
	 */
	@TableField(exist=false)
	private List<SysDeptEntity> children;

	@TableField(exist = false)
	private Object company;

	/**
	 * 客户总数
	 */
	@TableField(exist = false)
	private int customers;
	@TableField(exist=false)
	private List<SysMenuEntity> menus;


}
