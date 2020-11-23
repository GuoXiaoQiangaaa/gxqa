package com.pwc.modules.output.controller;

import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;
import com.pwc.common.validator.ValidatorUtils;
import com.pwc.modules.output.entity.OutputInvoiceApplyEntity;
import com.pwc.modules.output.service.OutputInvoiceApplyService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;



/**
 * 开票申请
 *
 * @author fanpf
 * @date 2020/9/24
 */
@RestController
@RequestMapping("output/apply")
public class OutputInvoiceApplyController {
    @Autowired
    private OutputInvoiceApplyService applyService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("output:apply:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = applyService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{applyId}")
    @RequiresPermissions("output:apply:info")
    public R info(@PathVariable("applyId") Long applyId){
        OutputInvoiceApplyEntity outputInvoiceApply = applyService.getById(applyId);

        return R.ok().put("outputInvoiceApply", outputInvoiceApply);
    }

    /**
     * 保存
     */
    @PutMapping("/save")
    @RequiresPermissions("output:apply:save")
    public R save(@RequestBody OutputInvoiceApplyEntity outputInvoiceApply){
        applyService.save(outputInvoiceApply);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("output:apply:update")
    public R update(@RequestBody OutputInvoiceApplyEntity outputInvoiceApply){
        ValidatorUtils.validateEntity(outputInvoiceApply);
        applyService.updateById(outputInvoiceApply);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @RequiresPermissions("output:apply:delete")
    public R delete(@RequestBody Long[] applyIds){
        applyService.removeByIds(Arrays.asList(applyIds));

        return R.ok();
    }

}
