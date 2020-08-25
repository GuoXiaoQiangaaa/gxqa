package com.pwc.modules.sys.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pwc.common.annotation.DataFilter;
import com.pwc.common.utils.Constant;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;
import com.pwc.modules.sys.entity.SysDeptEntity;
import com.pwc.modules.sys.entity.SysTaxEntity;
import com.pwc.modules.sys.service.SysDeptService;
import com.pwc.modules.sys.service.SysTaxService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.pwc.modules.sys.dao.SysDeptTaxDao;
import com.pwc.modules.sys.entity.SysDeptTaxEntity;
import com.pwc.modules.sys.service.SysDeptTaxService;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author zk
 */
@Service("sysDeptTaxService")
public class SysDeptTaxServiceImpl extends ServiceImpl<SysDeptTaxDao, SysDeptTaxEntity> implements SysDeptTaxService {

    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private SysTaxService sysTaxService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(Long deptId, List<Long> taxIdList) {
        List<Long> subDeptIds = sysDeptService.getSubDeptIdList(deptId);
        if (CollUtil.isEmpty(subDeptIds)) {
            subDeptIds = new ArrayList<>();
        }
        subDeptIds.add(deptId);
        baseMapper.delete(new QueryWrapper<SysDeptTaxEntity>().in("dept_id", subDeptIds));
        if(taxIdList.size() == 0){
            return ;
        }

        //保存
        List<SysDeptEntity> depts = (List<SysDeptEntity>) sysDeptService.listByIds(subDeptIds);
        depts.forEach(f -> {
            for(Long taxId : taxIdList){
                    SysDeptTaxEntity sysDeptTaxEntity = new SysDeptTaxEntity();
                sysDeptTaxEntity.setDeptId(f.getDeptId());
                sysDeptTaxEntity.setTaxId(taxId);

                this.save(sysDeptTaxEntity);
            }
        });

    }

    @Override
    @DataFilter(subDept = true, user = false)
    public PageUtils queryPage(Map<String, Object> params) {
        String deptName = (String)params.get("deptName");

        //曲线救国，分页查询
        List<Object> deptIds = sysDeptService.listObjs(
                new QueryWrapper<SysDeptEntity>().select("dept_id").like(StringUtils.isNotBlank(deptName),"name", deptName));
        IPage<SysDeptTaxEntity> page = this.page(
                new Query<SysDeptTaxEntity>().getPage(params),
                new QueryWrapper<SysDeptTaxEntity>()
                        .in(CollUtil.isNotEmpty(deptIds),"dept_id", deptIds)
                        .groupBy("dept_id")
                        .apply(params.get(Constant.SQL_FILTER) != null, (String)params.get(Constant.SQL_FILTER))
        );

        for(SysDeptTaxEntity sysDeptTaxEntity : page.getRecords()){
            SysDeptEntity sysDeptEntity = sysDeptService.getById(sysDeptTaxEntity.getDeptId());
            if(sysDeptEntity != null){
                sysDeptTaxEntity.setDeptName(sysDeptEntity.getName());
                //税种
                List<Object> ids = this.listObjs(new QueryWrapper<SysDeptTaxEntity>().select("tax_id").eq("dept_id", sysDeptEntity.getDeptId()));
                List<Long> longList = Convert.toList(Long.class, ids);
                List<SysTaxEntity> taxs = (List<SysTaxEntity>) sysTaxService.listByIds(longList);
                sysDeptTaxEntity.setTaxList(taxs);
            }
        }

        return new PageUtils(page);
    }

    @Override
    public List<Long> getTaxIdByDeptId(Long deptId) {
        return Convert.toList(Long.class, this.listObjs(new QueryWrapper<SysDeptTaxEntity>().select("tax_id").eq("dept_id", deptId)));
    }
}
