package com.pwc.modules.output.controller;

import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;
import com.pwc.common.validator.ValidatorUtils;
import com.pwc.modules.output.entity.OutputBillingEntity;
import com.pwc.modules.output.service.OutputBillingService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;



/**
 * 销项Billing
 *
 * @author fanpf
 * @date 2020/9/25
 */
@RestController
@RequestMapping("output/outputbilling")
public class OutputBillingController {
    @Autowired
    private OutputBillingService outputBillingService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("output:outputbilling:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = outputBillingService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{billId}")
    @RequiresPermissions("output:outputbilling:info")
    public R info(@PathVariable("billId") Long billId){
        OutputBillingEntity outputBilling = outputBillingService.getById(billId);

        return R.ok().put("outputBilling", outputBilling);
    }

    /**
     * 保存
     */
    @PutMapping("/save")
    @RequiresPermissions("output:outputbilling:save")
    public R save(@RequestBody OutputBillingEntity outputBilling){
        outputBillingService.save(outputBilling);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("output:outputbilling:update")
    public R update(@RequestBody OutputBillingEntity outputBilling){
        ValidatorUtils.validateEntity(outputBilling);
        outputBillingService.updateById(outputBilling);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @RequiresPermissions("output:outputbilling:delete")
    public R delete(@RequestBody Long[] billIds){
        outputBillingService.removeByIds(Arrays.asList(billIds));

        return R.ok();
    }

}
