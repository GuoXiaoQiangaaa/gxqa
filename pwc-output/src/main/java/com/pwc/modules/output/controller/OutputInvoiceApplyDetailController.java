package com.pwc.modules.output.controller;

import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;
import com.pwc.common.validator.ValidatorUtils;
import com.pwc.modules.output.entity.OutputInvoiceApplyDetailEntity;
import com.pwc.modules.output.service.OutputInvoiceApplyDetailService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;



/**
 * 开票申请明细
 *
 * @author fanpf
 * @date 2020/9/24
 */
@RestController
@RequestMapping("output/applydetail")
public class OutputInvoiceApplyDetailController {
    @Autowired
    private OutputInvoiceApplyDetailService applyDetailService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("output:applydetail:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = applyDetailService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{detailId}")
    @RequiresPermissions("output:applydetail:info")
    public R info(@PathVariable("detailId") Long detailId){
        OutputInvoiceApplyDetailEntity outputInvoiceApplyDetail = applyDetailService.getById(detailId);

        return R.ok().put("outputInvoiceApplyDetail", outputInvoiceApplyDetail);
    }

    /**
     * 保存
     */
    @PutMapping("/save")
    @RequiresPermissions("output:applydetail:save")
    public R save(@RequestBody OutputInvoiceApplyDetailEntity outputInvoiceApplyDetail){
        applyDetailService.save(outputInvoiceApplyDetail);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("output:applydetail:update")
    public R update(@RequestBody OutputInvoiceApplyDetailEntity outputInvoiceApplyDetail){
        ValidatorUtils.validateEntity(outputInvoiceApplyDetail);
        applyDetailService.updateById(outputInvoiceApplyDetail);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @RequiresPermissions("output:applydetail:delete")
    public R delete(@RequestBody Long[] detailIds){
        applyDetailService.removeByIds(Arrays.asList(detailIds));

        return R.ok();
    }

}
