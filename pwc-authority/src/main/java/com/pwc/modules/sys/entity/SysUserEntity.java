

package com.pwc.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pwc.common.validator.group.AddGroup;
import com.pwc.common.validator.group.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 系统用户
 *
 * @author
 */
@Data
@TableName("sys_user")
public class SysUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 用户ID
	 */
	@TableId
	private Long userId;

	/**
	 * 用户名
	 */
	@NotBlank(message="用户名不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String username;

	/**
	 * 密码
	 */
	@NotBlank(message="密码不能为空", groups = AddGroup.class)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;

	/**
	 * 盐
	 */
	private String salt;

	/**
	 * 邮箱
	 */
	@NotBlank(message="邮箱不能为空", groups = {AddGroup.class, UpdateGroup.class})
	@Email(message="邮箱格式不正确", groups = {AddGroup.class, UpdateGroup.class})
	private String email;

	/**
	 * 手机号
	 */
	private String mobile;

	/**
	 * 状态  0：禁用   1：正常
	 */
	private Integer status;
	
	/**
	 * 角色ID列表
	 */
	@TableField(exist=false)
	private List<Long> roleIdList;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 部门ID
	 */
	@NotNull(message="所属公司不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private Long deptId;

	/**
	 * 部门名称
	 */
//	@TableField(exist=false)
	private String deptName;
	/**
	 * 角色名
	 */
	@TableField(exist=false)
	private String roleName;
	/**
	 * 部分类型 1 总公司 2分公司
	 */
	@TableField(exist=false)
	private Integer deptType;

	@TableLogic
	private Integer delFlag;
	/**
	 * 有效期
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Date expireDate;

	/**
	 * 所能浏览的公司,逗号分隔  input test
	 */
	@TableField(exist=false)
	private String browseCompaniesId;

	/**
	 * 是否是管理人员，0:普通用户，1：管理员 input test
	 */
	@TableField(exist=false)
	private Integer isNormalUser;
	/**
	 * 所属企业 input test
	 */
	@TableField(exist=false)
	private Integer companyId;

}
