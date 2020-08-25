package com.pwc.modules.output.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.pwc.common.validator.ValidatorUtils;
import com.pwc.modules.output.entity.OutputGoodsEntity;
import com.pwc.modules.output.entity.OutputInvoiceRulesGoodsEntity;
import com.pwc.modules.output.service.OutputGoodsService;
import com.pwc.modules.output.service.OutputInvoiceRulesGoodsService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.pwc.modules.output.entity.OutputInvoiceRulesEntity;
import com.pwc.modules.output.service.OutputInvoiceRulesService;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;



/**
 * 开票规则
 *
 * @author zk
 * @email 
 * @date 2020-06-01 18:17:06
 */
@RestController
@RequestMapping("output/invoicerules")
public class OutputInvoiceRulesController {
    @Autowired
    private OutputInvoiceRulesService outputInvoiceRulesService;
    @Autowired
    private OutputGoodsService outputGoodsService;
    @Autowired
    private OutputInvoiceRulesGoodsService outputInvoiceRulesGoodsService;


    /**
     * 查询客户关联的发票规则
     * @param customerId
     * @return
     */
    @GetMapping("/getRules/{customerId}")
    public R getRules(@PathVariable("customerId") Long customerId) {
        return R.ok().put("data", outputInvoiceRulesService.getByCustomerId(customerId));
    }


    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("output:invoicerules:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = outputInvoiceRulesService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{rulesId}")
    @RequiresPermissions("output:invoicerules:info")
    public R info(@PathVariable("rulesId") Long rulesId){
        OutputInvoiceRulesEntity outputInvoiceRules = outputInvoiceRulesService.getById(rulesId);

        return R.ok().put("invoiceRules", outputInvoiceRules);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("output:invoicerules:save")
    public R save(@RequestBody OutputInvoiceRulesEntity outputInvoiceRules){
        outputInvoiceRulesService.save(outputInvoiceRules);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("output:invoicerules:update")
    public R update(@RequestBody OutputInvoiceRulesEntity outputInvoiceRules){
        ValidatorUtils.validateEntity(outputInvoiceRules);
        outputInvoiceRulesService.updateById(outputInvoiceRules);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("output:invoicerules:delete")
    public R delete(@RequestBody Long[] rulesIds){
        outputInvoiceRulesService.removeByIds(Arrays.asList(rulesIds));

        return R.ok();
    }

}
