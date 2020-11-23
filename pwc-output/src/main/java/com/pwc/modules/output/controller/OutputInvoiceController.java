package com.pwc.modules.output.controller;

import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;
import com.pwc.common.validator.ValidatorUtils;
import com.pwc.modules.output.entity.OutputInvoiceEntity;
import com.pwc.modules.output.service.OutputInvoiceService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;



/**
 * 销项发票
 *
 * @author fanpf
 * @date 2020/9/24
 */
@RestController
@RequestMapping("output/outputinvoice")
public class OutputInvoiceController {
    @Autowired
    private OutputInvoiceService outputInvoiceService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("output:outputinvoice:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = outputInvoiceService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{invoiceId}")
    @RequiresPermissions("output:outputinvoice:info")
    public R info(@PathVariable("invoiceId") Long invoiceId){
        OutputInvoiceEntity outputInvoice = outputInvoiceService.getById(invoiceId);

        return R.ok().put("outputInvoice", outputInvoice);
    }

    /**
     * 保存
     */
    @PutMapping("/save")
    @RequiresPermissions("output:outputinvoice:save")
    public R save(@RequestBody OutputInvoiceEntity outputInvoice){
        outputInvoiceService.save(outputInvoice);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("output:outputinvoice:update")
    public R update(@RequestBody OutputInvoiceEntity outputInvoice){
        ValidatorUtils.validateEntity(outputInvoice);
        outputInvoiceService.updateById(outputInvoice);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @RequiresPermissions("output:outputinvoice:delete")
    public R delete(@RequestBody Long[] invoiceIds){
        outputInvoiceService.removeByIds(Arrays.asList(invoiceIds));

        return R.ok();
    }

}
