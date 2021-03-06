

package com.pwc.modules.sys.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pwc.common.third.TtkOrgUtil;
import com.pwc.common.utils.Constant;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;
import com.pwc.common.utils.StatusDefine;
import com.pwc.common.validator.ValidatorUtils;
import com.pwc.modules.sys.entity.*;
import com.pwc.modules.sys.service.SysDeptService;
import com.pwc.modules.sys.service.SysRoleDeptService;
import com.pwc.modules.sys.service.SysRoleService;
import com.pwc.modules.sys.service.SysUserRoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;


/**
 * 部门管理
 *
 * @author
 */
@RestController
@RequestMapping("/sys/dept")
public class SysDeptController extends AbstractController {
    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private SysRoleDeptService sysRoleDeptService;
    @Autowired
    private SysRoleService sysRoleService;

    /**
     * 部门列表(不含层级,只有子部门数量)
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        // super admin 拥有所有权限
        List<Long> roleIds = sysUserRoleService.queryRoleIdList(getUserId());
        Long roleId = roleIds.get(0);
        // 如果不是超级管理员传值parentId
        if (roleId != Constant.SUPER_ADMIN) {
            String parentId = (String) params.get("parentId");
            if (StrUtil.isBlank(parentId)) {
                params.put("parentId", getDeptId().toString());
            }
        }
        PageUtils page = sysDeptService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 关键字查询(不含层级,只有子部门数量)
     */
    @GetMapping("/search")
    public R search(@RequestParam Map<String, Object> params) {
        // 检查用户是否是超级管理员,如果不是则传parentId
        List<Long> roleIds = sysUserRoleService.queryRoleIdList(getUserId());
        Long roleId = roleIds.get(0);
        if (roleId != Constant.SUPER_ADMIN) {
            /*List<SysRoleDeptEntity> roleDeptList = sysRoleDeptService.list(
                    new QueryWrapper<SysRoleDeptEntity>()
                            .in("role_id", roleIds));

            Set<Long> deptIdList = new HashSet<>();
            for (int i = 0; i < roleDeptList.size(); i++) {
                deptIdList.add(roleDeptList.get(i).getDeptId());
            }*/
            /*String parentId = (String) params.get("parentId");
            if (StrUtil.isBlank(parentId)) {
                params.put("parentId", getDeptId().toString());
            }*/
        }

        PageUtils page = sysDeptService.search(params);
        return R.ok().put("page", page);
    }

    /**
     * 列表(含层级,部门详细信息)
     */
    @GetMapping("/treeList")
    public R list() {
        List<SysDeptEntity> deptList = sysDeptService.getTreeDeptList(0L);

        return R.ok().put("data", deptList);
    }

    /**
     * 列表(含层级,只有部门名称和id)
     */
    @GetMapping("/treeSelect")
    public R treeSelect() {
        Long userId = getUserId();
        Long deptId = getDeptId();
        if (userId == 1) {
            deptId = 0L;
        }

        List<TreeSelectVo> resultList = new ArrayList<>();
        if (deptId != 0) {
		//根据部门ID获取部门信息
			/*SysDeptEntity sysDeptEntity = sysDeptService.getById(deptId);
			if (null != sysDeptEntity) {
				TreeSelectVo treeSelectVo = new TreeSelectVo();
				treeSelectVo.setKey(sysDeptEntity.getDeptId().toString());
				treeSelectVo.setTitle(sysDeptEntity.getDeptCode()+" "+sysDeptEntity.getName());
				treeSelectVo.setChildren(deptList);
				resultList.add(treeSelectVo);
			}*/
            SysUserRoleEntity role = sysRoleService.findRoleIdByUserId(userId);
            if(role != null){
                List<Long> deptIdList = sysRoleDeptService.queryDeptIdList(new Long[]{role.getRoleId()});
                for(int i = 0;i<deptIdList.size();i++){
                    SysDeptEntity aa = sysDeptService.getById(deptIdList.get(i));
                    List<TreeSelectVo> deptList = sysDeptService.getTreeSelectList(deptIdList.get(i));
                    TreeSelectVo treeSelectVo = new TreeSelectVo();
                    treeSelectVo.setKey(aa.getDeptId().toString());
                    treeSelectVo.setTitle(aa.getDeptCode()+" "+aa.getName());
                    treeSelectVo.setChildren(deptList);
                    resultList.add(treeSelectVo);
                }
            }
		}else{
            resultList = sysDeptService.getTreeSelectList(deptId);
        }
        return R.ok().put("data", resultList);
        //return R.ok().put("data", deptList);
    }

    /**
     * 部门列表(全部部门,不含层级)
     * 选择部门(添加、修改菜单)
     */
    @GetMapping("/select")
    @RequiresPermissions("sys:dept:select")
    public R select() {
        List<SysDeptEntity> deptList = sysDeptService.queryList(new HashMap<String, Object>());

        //添加一级部门
        if (getUserId() == Constant.SUPER_ADMIN) {
            SysDeptEntity root = new SysDeptEntity();
            root.setDeptId(0L);
            root.setName("一级部门");
            root.setParentId(-1L);
            root.setOpen(true);
            deptList.add(root);
        }

        return R.ok().put("deptList", deptList);
    }

    /**
     * 上级部门Id(管理员则为0)
     */
    @GetMapping("/info")
    @RequiresPermissions("sys:dept:info")
    public R info() {
        long deptId = 0;
        if (getUserId() != Constant.SUPER_ADMIN) {
            List<SysDeptEntity> deptList = sysDeptService.queryList(new HashMap<>());
            Long parentId = null;
            for (SysDeptEntity sysDeptEntity : deptList) {
                if (parentId == null) {
                    parentId = sysDeptEntity.getParentId();
                    continue;
                }

                if (parentId > sysDeptEntity.getParentId().longValue()) {
                    parentId = sysDeptEntity.getParentId();
                }
            }
            deptId = parentId;
        }

        return R.ok().put("deptId", deptId);
    }

    /**
     * 信息
     */
    @GetMapping("/info/{deptId}")
    @RequiresPermissions("sys:dept:info")
    public R info(@PathVariable("deptId") Long deptId) {

        SysDeptEntity dept = sysDeptService.queryInfo(deptId);

        return R.ok().put("dept", dept);
    }

    /**
     * 保存
     */
    @PutMapping("/save")
    @RequiresPermissions("sys:dept:save")
    public R save(@RequestBody SysDeptEntity dept) {
//		sysDeptService.save(dept);
        ValidatorUtils.validateEntity(dept);
        sysDeptService.saveDept(dept);
        return R.ok().put("deptId", dept.getDeptId());
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("sys:dept:update")
    public R update(@RequestBody SysDeptEntity dept) {
        ValidatorUtils.validateEntity(dept);
//		sysDeptService.updateById(dept);
        sysDeptService.updateDept(dept);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @RequiresPermissions("sys:dept:delete")
    public R delete(long deptId) {
        //判断是否有子部门
        List<Long> deptList = sysDeptService.queryDetpIdList(deptId);
        if (deptList.size() > 0) {
            return R.error("请先删除子部门");
        }

        sysDeptService.removeById(deptId);

        return R.ok();
    }

    /**
     * 启用
     */
    @PostMapping("/enable")
    @RequiresPermissions("sys:dept:enable")
    public R enable(long deptId) {

        if (!sysDeptService.updateStatus(deptId, StatusDefine.DeptStatus.ENABLE.getValue())) {
            return R.error();
        }

        return R.ok();
    }

    /**
     * 禁用
     */
    @PostMapping("/disable")
    @RequiresPermissions("sys:dept:enable")
    public R disable(long deptId) {

        if (!sysDeptService.updateStatus(deptId, StatusDefine.DeptStatus.DISABLE.getValue())) {
            return R.error();
        }

        return R.ok();
    }

    @GetMapping("/thirdOrgUrl")
    @RequiresPermissions("sys:dept:info")
    public R getThirdOrgUrl(Long deptId) {
        SysDeptEntity sysDeptEntity = sysDeptService.getById(deptId);
        if (null != sysDeptEntity && null != sysDeptEntity.getThirdOrgId()) {
            return R.ok().put("data", TtkOrgUtil.getWebUrlForCompanyInformation(sysDeptEntity.getThirdOrgId()));
        }
        return R.error();
    }
}
