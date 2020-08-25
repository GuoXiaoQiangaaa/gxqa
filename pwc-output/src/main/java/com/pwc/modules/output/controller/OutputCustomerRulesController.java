package com.pwc.modules.output.controller;

import java.util.Arrays;
import java.util.Map;

import cn.hutool.core.convert.Convert;
import com.pwc.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.pwc.modules.output.entity.OutputCustomerRulesEntity;
import com.pwc.modules.output.service.OutputCustomerRulesService;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;



/**
 * 客户规则关联
 *
 * @author zk
 * @email 
 * @date 2020-06-11 15:41:24
 */
@RestController
@RequestMapping("output/customerrules")
public class OutputCustomerRulesController {
    @Autowired
    private OutputCustomerRulesService outputCustomerRulesService;


    /**
     * 应用规则
     * @param rulesId
     * @param customerIds
     * @return
     */
    @GetMapping("/use")
    @RequiresPermissions("output:customerrules:user")
    public R userRules(Long rulesId, String customerIds){
        String[] customerIdArray = customerIds.split(",");
        // 先删除原有应用规则，再加入新规则
        outputCustomerRulesService.delByCustomerIds(Convert.toList(Long.class, customerIdArray));
        for (String customerId : customerIdArray) {
            OutputCustomerRulesEntity customerRulesEntity = new OutputCustomerRulesEntity();
            customerRulesEntity.setRulesId(rulesId);
            customerRulesEntity.setCustomerId(Long.parseLong(customerId));
            outputCustomerRulesService.save(customerRulesEntity);
        }
        return R.ok();
    }
    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("output:customerrules:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = outputCustomerRulesService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @RequiresPermissions("output:customerrules:info")
    public R info(@PathVariable("id") Long id){
        OutputCustomerRulesEntity outputCustomerRules = outputCustomerRulesService.getById(id);

        return R.ok().put("customerRules", outputCustomerRules);
    }

    /**
     * 保存
     */
    @PutMapping("/save")
    @RequiresPermissions("output:customerrules:save")
    public R save(@RequestBody OutputCustomerRulesEntity outputCustomerRules){
        outputCustomerRulesService.save(outputCustomerRules);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("output:customerrules:update")
    public R update(@RequestBody OutputCustomerRulesEntity outputCustomerRules){
        ValidatorUtils.validateEntity(outputCustomerRules);
        outputCustomerRulesService.updateById(outputCustomerRules);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @RequiresPermissions("output:customerrules:delete")
    public R delete(@RequestBody Long[] ids){
        outputCustomerRulesService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
