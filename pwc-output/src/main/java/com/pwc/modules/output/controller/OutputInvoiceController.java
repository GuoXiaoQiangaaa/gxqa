package com.pwc.modules.output.controller;

import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;
import com.pwc.common.validator.ValidatorUtils;
import com.pwc.modules.output.entity.OutputInvoiceEntity;
import com.pwc.modules.output.service.OutputInvoiceService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
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
        //PageUtils page = outputInvoiceService.queryPage(params);
        HashMap<String, Object> p = new HashMap<>();
        p.put("invoice_id", "1");
        p.put("apply_number", "1");
        p.put("agreement","");
        p.put("apply_user","admin");
        p.put("apply_time",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        p.put("purchase_name","AAA有限公司");
        p.put("seller_name","CBC-SH");
        p.put("invoice_type","增值随专用发票");
        p.put("invoice_entity","纸质发票");
        p.put("Invoice_requisition_status","2");
        p.put("invoice_status","1");
        PageUtils p1 = outputInvoiceService.queryPage(p);
        return R.ok().put("page", p1);
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
