package com.pwc.modules.filing.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.pwc.common.validator.ValidatorUtils;
import com.pwc.modules.filing.controller.form.FilingDistrictForm;
import com.pwc.modules.filing.entity.FilingDistrictEntity;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.pwc.modules.filing.service.FilingDistrictService;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;



/**
 * 地区设置
 *
 * @author zk
 * @email 
 * @date 2019-11-13 18:47:17
 */
@RestController
@RequestMapping("filing/district")
public class FilingDistrictController {
    @Autowired
    private FilingDistrictService filingDistrictService;



    /**
     * 保存地区设置
     */
    @PutMapping("/save")
    @RequiresPermissions("filing:district:save")
    public R save(@RequestBody FilingDistrictForm form){
        filingDistrictService.saveDistrict(form.getDistricts());
        return R.ok();
    }


    /**
     * 列表所有设置过的列表树
     */
    @GetMapping("/treeList")
    @RequiresPermissions("filing:district:list")
    public R treeList(){
        List<FilingDistrictEntity> filingDistricts = filingDistrictService.getSubDistrictList(0L);

        return R.ok().put("data", filingDistricts);
    }

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("filing:district:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = filingDistrictService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{districtId}")
    @RequiresPermissions("filing:district:info")
    public R info(@PathVariable("districtId") Long districtId){
        FilingDistrictEntity filingDistrict = filingDistrictService.getById(districtId);

        return R.ok().put("filingDistrict", filingDistrict);
    }



    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("filing:district:update")
    public R update(@RequestBody FilingDistrictEntity filingDistrict){
        ValidatorUtils.validateEntity(filingDistrict);
        filingDistrictService.updateById(filingDistrict);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @RequiresPermissions("filing:district:delete")
    public R delete(@RequestBody Long[] districtIds){
        filingDistrictService.removeByIds(Arrays.asList(districtIds));

        return R.ok();
    }

}
