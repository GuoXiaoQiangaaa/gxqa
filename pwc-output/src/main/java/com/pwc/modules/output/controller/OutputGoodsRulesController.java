package com.pwc.modules.output.controller;

import java.util.Arrays;
import java.util.Map;

import com.pwc.common.validator.ValidatorUtils;
import com.pwc.modules.output.service.OutputGoodsService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pwc.modules.output.entity.OutputGoodsRulesEntity;
import com.pwc.modules.output.service.OutputGoodsRulesService;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;



/**
 * 商品规则
 *
 * @author zk
 * @email 
 * @date 2020-06-01 18:17:06
 */
@RestController
@RequestMapping("output/goodsrules")
public class OutputGoodsRulesController {
    @Autowired
    private OutputGoodsRulesService outputGoodsRulesService;
    @Autowired
    private OutputGoodsService outputGoodsService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("output:goodsrules:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = outputGoodsRulesService.queryPage(params);

        for (Object object : page.getList()) {
            OutputGoodsRulesEntity goodsRulesEntity = (OutputGoodsRulesEntity)object;
            goodsRulesEntity.setGoods(outputGoodsService.getById(goodsRulesEntity.getGoodsId()));
        }
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{rulesId}")
    @RequiresPermissions("output:goodsrules:info")
    public R info(@PathVariable("rulesId") Long rulesId){
        OutputGoodsRulesEntity outputGoodsRules = outputGoodsRulesService.getById(rulesId);

        return R.ok().put("goodsRules", outputGoodsRules);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("output:goodsrules:save")
    public R save(@RequestBody OutputGoodsRulesEntity outputGoodsRules){
        outputGoodsRulesService.save(outputGoodsRules);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("output:goodsrules:update")
    public R update(@RequestBody OutputGoodsRulesEntity outputGoodsRules){
        ValidatorUtils.validateEntity(outputGoodsRules);
        outputGoodsRulesService.updateById(outputGoodsRules);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("output:goodsrules:delete")
    public R delete(@RequestBody Long[] rulesIds){
        outputGoodsRulesService.removeByIds(Arrays.asList(rulesIds));

        return R.ok();
    }

}
