package com.pwc.modules.sys.service.impl;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.modules.sys.dao.SysDeptMenuDao;
import com.pwc.modules.sys.entity.SysDeptMenuEntity;
import com.pwc.modules.sys.entity.SysMenuEntity;
import com.pwc.modules.sys.service.SysDeptMenuService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("SysDeptMenuService")
public class SysDeptMenuServiceImpl extends ServiceImpl<SysDeptMenuDao,SysDeptMenuEntity> implements SysDeptMenuService {


    /**
     * 公司管理模块列表
     * @param sysDeptMenuEntityPage
     * @param sysDeptMenuEntity
     * @return
     */
    @Override
    public Page<SysDeptMenuEntity> selectByPage(Page<SysDeptMenuEntity> sysDeptMenuEntityPage, SysDeptMenuEntity sysDeptMenuEntity) {

        List<SysDeptMenuEntity> list =  baseMapper.selectByPage(sysDeptMenuEntityPage,sysDeptMenuEntity);
        sysDeptMenuEntityPage.setRecords(list);
        return sysDeptMenuEntityPage;
    }

    /**
     * 菜单一级列表
     * @return
     */
    @Override
    public List<SysMenuEntity> getMenuList() {

        return baseMapper.getMenuList();
    }

    /**
     * 模块管理编辑回显
     * @param deptId
     * @return
     */
    @Override
    public List getDeptMenuById(Long deptId) {
        return baseMapper.getDeptMenuById(deptId);
    }



    /**
     * 修改模块管理
     * @param sysDeptMenuEntity
     */
    @Override
    public void update(SysDeptMenuEntity sysDeptMenuEntity,Long[] ids) {

        //1.先将该公司下的菜单清空
        baseMapper.clearMenuIdByDeptId(sysDeptMenuEntity);
        //2.重新选择公司对应一级菜单权限
        if (ids!=null) {
            for (Long id : ids) {
                baseMapper.update(sysDeptMenuEntity.getDeptId(),id);
            }

        }



    }
}
