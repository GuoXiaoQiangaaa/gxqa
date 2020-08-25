

package com.pwc.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 公司与菜单对应关系
 *
 * @author
 */
@Data
public class SysDeptMenuEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@TableId
	private Long id;

	/**
	 * 角色ID
	 */
	private Long deptId;

	private String deptName;

	private List<Map> meunList;

	/**
	 * 菜单ID
	 */
	private Long menuId;

	private String menuName;

	private Integer page;

	private Integer size;

	private String menuIds;
	
}
