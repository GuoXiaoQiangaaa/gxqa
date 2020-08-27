package com.pwc.modules.data.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import com.pwc.common.validator.ValidatorUtils;
import com.pwc.modules.sys.shiro.ShiroUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.pwc.modules.data.entity.OutputSupplierEntity;
import com.pwc.modules.data.service.OutputSupplierService;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;



/**
 * 供应商信息
 *
 * @author fanpf
 * @date 2020/8/24
 */
@RestController
@RequestMapping("data/outputsupplier")
public class OutputSupplierController {
    @Autowired
    private OutputSupplierService outputSupplierService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("data:outputsupplier:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = outputSupplierService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 详情
     */
    @GetMapping("/info/{supplierId}")
    @RequiresPermissions("data:outputsupplier:info")
    public R info(@PathVariable("supplierId") Long supplierId){
        OutputSupplierEntity outputSupplier = outputSupplierService.getById(supplierId);

        return R.ok().put("outputSupplier", outputSupplier);
    }

    /**
     * 新增
     */
    @PutMapping("/save")
    @RequiresPermissions("data:outputsupplier:save")
    public R save(@RequestBody OutputSupplierEntity outputSupplier){
        outputSupplierService.save(outputSupplier);

        return R.ok();
    }

    /**
     * 编辑
     */
    @PostMapping("/update")
    @RequiresPermissions("data:outputsupplier:update")
    public R update(@RequestBody OutputSupplierEntity outputSupplier){
        ValidatorUtils.validateEntity(outputSupplier);
        outputSupplierService.updateById(outputSupplier);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @RequiresPermissions("data:outputsupplier:delete")
    public R delete(@RequestBody Long[] supplierIds){
        outputSupplierService.removeByIds(Arrays.asList(supplierIds));

        return R.ok();
    }

    /**
     * 禁用/启用
     */
    @PostMapping("/disableOrEnable")
    public R disableOrEnable(@RequestBody OutputSupplierEntity reqVo){
        outputSupplierService.disableOrEnable(reqVo);

        return R.ok();
    }

    /**
     * 关键字查询
     */
    @GetMapping("/search")
    public R search(@RequestParam Map<String, Object> params){
        PageUtils page = outputSupplierService.search(params);

        return R.ok().put("page", page);
    }

}
