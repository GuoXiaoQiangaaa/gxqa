package com.pwc.modules.data.controller;

import java.util.Arrays;
import java.util.Map;

import com.pwc.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.pwc.modules.data.entity.OutputSapTaxListEntity;
import com.pwc.modules.data.service.OutputSapTaxListService;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;



/**
 * SAP税码清单
 *
 * @author fanpf
 * @date 2020/8/27
 */
@RestController
@RequestMapping("data/outputsaptaxlist")
public class OutputSapTaxListController {
    @Autowired
    private OutputSapTaxListService outputSapTaxListService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("data:outputsaptaxlist:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = outputSapTaxListService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{taxId}")
    @RequiresPermissions("data:outputsaptaxlist:info")
    public R info(@PathVariable("taxId") Long taxId){
        OutputSapTaxListEntity outputSapTaxList = outputSapTaxListService.getById(taxId);

        return R.ok().put("outputSapTaxList", outputSapTaxList);
    }

    /**
     * 保存
     */
    @PutMapping("/save")
    @RequiresPermissions("data:outputsaptaxlist:save")
    public R save(@RequestBody OutputSapTaxListEntity outputSapTaxList){
        outputSapTaxListService.save(outputSapTaxList);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("data:outputsaptaxlist:update")
    public R update(@RequestBody OutputSapTaxListEntity outputSapTaxList){
        ValidatorUtils.validateEntity(outputSapTaxList);
        outputSapTaxListService.updateById(outputSapTaxList);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @RequiresPermissions("data:outputsaptaxlist:delete")
    public R delete(@RequestBody Long[] taxIds){
        outputSapTaxListService.removeByIds(Arrays.asList(taxIds));

        return R.ok();
    }


    /**
     * 禁用/启用
     */
    @PostMapping("/disableOrEnable")
    public R disableOrEnable(@RequestBody OutputSapTaxListEntity reqVo){
        outputSapTaxListService.disableOrEnable(reqVo);

        return R.ok();
    }

    /**
     * 关键字查询
     */
    @GetMapping("/search")
    public R search(@RequestParam Map<String, Object> params){
        PageUtils page = outputSapTaxListService.search(params);

        return R.ok().put("page", page);
    }

}
