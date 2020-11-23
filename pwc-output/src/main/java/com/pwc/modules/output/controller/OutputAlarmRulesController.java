package com.pwc.modules.output.controller;

import com.pwc.common.utils.R;
import com.pwc.common.validator.ValidatorUtils;
import com.pwc.modules.output.entity.OutputAlarmRulesEntity;
import com.pwc.modules.output.service.OutputAlarmRulesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;



/**
 * 报警规则
 *
 * @author fanpf
 * @date 2020/9/25
 */
@RestController
@RequestMapping("output/outputalarmrules")
public class OutputAlarmRulesController {
    @Autowired
    private OutputAlarmRulesService outputAlarmRulesService;

    /**
     * 列表
     */
    @GetMapping("/list")
//    @RequiresPermissions("output:outputalarmrules:list")
    public R list(@RequestParam Map<String, Object> params){
        List<OutputAlarmRulesEntity> list = outputAlarmRulesService.list(params);

        return R.ok().put("list", list);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{alarmId}")
//    @RequiresPermissions("output:outputalarmrules:info")
    public R info(@PathVariable("alarmId") Long alarmId){
        OutputAlarmRulesEntity outputAlarmRules = outputAlarmRulesService.getById(alarmId);

        return R.ok().put("outputAlarmRules", outputAlarmRules);
    }

    /**
     * 保存/编辑
     */
    @PostMapping("/saveOrUpdate")
//    @RequiresPermissions("output:outputalarmrules:save")
    public R saveOrUpdate(@RequestBody List<OutputAlarmRulesEntity> rulesEntityList, @RequestParam Map<String, Object> params){

        outputAlarmRulesService.saveOrUpdate(rulesEntityList, params);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
//    @RequiresPermissions("output:outputalarmrules:update")
    public R update(@RequestBody OutputAlarmRulesEntity outputAlarmRules){
        ValidatorUtils.validateEntity(outputAlarmRules);

        outputAlarmRulesService.updateById(outputAlarmRules);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
//    @RequiresPermissions("output:outputalarmrules:delete")
    public R delete(@RequestBody Long[] alarmIds){
        outputAlarmRulesService.removeByIds(Arrays.asList(alarmIds));

        return R.ok();
    }

}
