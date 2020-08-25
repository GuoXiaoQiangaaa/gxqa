package com.pwc.modules.data.controller;

import java.util.Arrays;
import java.util.Map;

import com.pwc.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.pwc.modules.data.entity.OutputCustomerNewEntity;
import com.pwc.modules.data.service.OutputCustomerNewService;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;



/**
 * 客户信息
 *
 * @author fanpf
 * @date 2020/8/24
 */
@RestController
@RequestMapping("data/outputcustomernew")
public class OutputCustomerNewController {
    @Autowired
    private OutputCustomerNewService outputCustomerNewService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("data:outputcustomernew:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = outputCustomerNewService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 详情
     */
    @GetMapping("/info/{customerId}")
    @RequiresPermissions("data:outputcustomernew:info")
    public R info(@PathVariable("customerId") Long customerId){
        OutputCustomerNewEntity outputCustomerNew = outputCustomerNewService.getById(customerId);

        return R.ok().put("outputCustomerNew", outputCustomerNew);
    }

    /**
     * 新增
     */
    @PutMapping("/save")
    @RequiresPermissions("data:outputcustomernew:save")
    public R save(@RequestBody OutputCustomerNewEntity outputCustomerNew){
        outputCustomerNewService.save(outputCustomerNew);

        return R.ok();
    }

    /**
     * 编辑
     */
    @PostMapping("/update")
    @RequiresPermissions("data:outputcustomernew:update")
    public R update(@RequestBody OutputCustomerNewEntity outputCustomerNew){
        ValidatorUtils.validateEntity(outputCustomerNew);
        outputCustomerNewService.updateById(outputCustomerNew);
        
        return R.ok();
    }

    /**
     * 停用
     */
    @DeleteMapping("/delete")
    @RequiresPermissions("data:outputcustomernew:delete")
    public R delete(@RequestBody Long[] customerIds){
        outputCustomerNewService.removeByIds(Arrays.asList(customerIds));

        return R.ok();
    }

    /**
     * 上传模板
     */


    /**
     * 下载模板
     */
}
