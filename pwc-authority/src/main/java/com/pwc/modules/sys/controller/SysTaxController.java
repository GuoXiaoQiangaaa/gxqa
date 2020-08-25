package com.pwc.modules.sys.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pwc.common.validator.ValidatorUtils;
import com.pwc.modules.sys.entity.SysDeptTaxEntity;
import com.pwc.modules.sys.service.SysDeptTaxService;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pwc.modules.sys.entity.SysTaxEntity;
import com.pwc.modules.sys.service.SysTaxService;
import com.pwc.common.utils.R;



/**
 * 税种表
 *
 * @author zk
 * @email 
 * @date 2019-12-31 17:21:04
 */
@RestController
@RequestMapping("sys/tax")
public class SysTaxController extends AbstractController {
    @Autowired
    private SysTaxService sysTaxService;
    @Autowired
    private SysDeptTaxService sysDeptTaxService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:tax:list")
    public R list(){

        return R.ok().put("list", sysTaxService.list());
    }


    /**
     * 列表
     */
    @RequestMapping("/page")
    @RequiresPermissions("sys:tax:list")
    public R list(@RequestParam Map<String, Object> params){
        return R.ok().put("page", sysDeptTaxService.queryPage(params));
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{taxId}")
    @RequiresPermissions("sys:tax:info")
    public R info(@PathVariable("taxId") Long taxId){
        SysTaxEntity sysTax = sysTaxService.getById(taxId);

        return R.ok().put("tax", sysTax);
    }

    /**
     * 信息
     */
    @RequestMapping("/info")
    @RequiresPermissions("sys:tax:info")
    public R info(){

        List<Object> ids = sysDeptTaxService.listObjs(new QueryWrapper<SysDeptTaxEntity>().select("tax_id").eq("dept_id", getDeptId()));
        List<Long> longList = Convert.toList(Long.class, ids);
        List<SysTaxEntity> taxs = CollUtil.newArrayList();
        if (CollUtil.isNotEmpty(longList)) {
            taxs = (List<SysTaxEntity>) sysTaxService.listByIds(longList);
        }

        return R.ok().put("tax", taxs);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("sys:tax:save")
    public R save(@RequestBody SysTaxEntity sysTax){
        sysTaxService.save(sysTax);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("sys:tax:update")
    public R update(String taxIds, long deptId ){
        sysDeptTaxService.remove(new QueryWrapper<SysDeptTaxEntity>().eq("dept_id", deptId));
        Long[] ids = (Long[]) ConvertUtils.convert(taxIds.split(","), Long.class);
        for(Long taxId : ids){
            SysDeptTaxEntity sysDeptTaxEntity = new SysDeptTaxEntity();
            sysDeptTaxEntity.setDeptId(deptId);
            sysDeptTaxEntity.setTaxId(taxId);

            sysDeptTaxService.save(sysDeptTaxEntity);
        }
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("sys:tax:delete")
    public R delete(@RequestBody Long[] taxIds){
        sysTaxService.removeByIds(Arrays.asList(taxIds));

        return R.ok();
    }

    /**
     * 保存
     */
    @RequestMapping("/setup")
    @RequiresPermissions("sys:tax:setup")
    public R save(String taxIds, long deptId){
        Long[] ids = (Long[]) ConvertUtils.convert(taxIds.split(","), Long.class);
        sysDeptTaxService.saveOrUpdate(deptId, Arrays.asList(ids));
        return R.ok();
    }
}
