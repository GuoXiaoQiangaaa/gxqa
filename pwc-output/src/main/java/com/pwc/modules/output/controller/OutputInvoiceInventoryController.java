package com.pwc.modules.output.controller;

import com.pwc.common.utils.R;
import com.pwc.modules.output.entity.OutputInvoiceInventoryEntity;
import com.pwc.modules.output.service.OutputInvoiceInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;



/**
 * 发票库存
 *
 * @author fanpf
 * @date 2020/9/25
 */
@RestController
@RequestMapping("output/outputinvoiceinventory")
public class OutputInvoiceInventoryController {
    @Autowired
    private OutputInvoiceInventoryService outputInvoiceInventoryService;

    /**
     * 列表
     */
    @GetMapping("/list")
//    @RequiresPermissions("output:outputinvoiceinventory:list")
    public R list(@RequestParam Map<String, Object> params){
        List<OutputInvoiceInventoryEntity> list = outputInvoiceInventoryService.list(params);

        return R.ok().put("list", list);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{inventoryId}")
//    @RequiresPermissions("output:outputinvoiceinventory:info")
    public R info(@PathVariable("inventoryId") Long inventoryId){
        OutputInvoiceInventoryEntity outputInvoiceInventory = outputInvoiceInventoryService.getById(inventoryId);

        return R.ok().put("outputInvoiceInventory", outputInvoiceInventory);
    }

    /**
     * 保存
     */
    @PutMapping("/save")
//    @RequiresPermissions("output:outputinvoiceinventory:save")
    public R save(@RequestBody OutputInvoiceInventoryEntity outputInvoiceInventory){
        outputInvoiceInventoryService.save(outputInvoiceInventory);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
//    @RequiresPermissions("output:outputinvoiceinventory:update")
    public R update(@RequestBody OutputInvoiceInventoryEntity outputInvoiceInventory){
        outputInvoiceInventoryService.update(outputInvoiceInventory);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
//    @RequiresPermissions("output:outputinvoiceinventory:delete")
    public R delete(@RequestBody Long[] inventoryIds){
        outputInvoiceInventoryService.removeByIds(Arrays.asList(inventoryIds));

        return R.ok();
    }

    /**
     * 人工录入库存
     */
    @PostMapping("/manmade")
    public R manmade(@RequestBody List<OutputInvoiceInventoryEntity> entityList, @RequestParam Map<String, Object> params){
        outputInvoiceInventoryService.manmade(entityList, params);

        return R.ok();
    }

    /**
     * 查询发票库存是否低于临界值
     * true:低于; false:高于
     */
    @GetMapping("/isUnderCrisis")
    public R isUnderCrisis(){
        boolean result = outputInvoiceInventoryService.isUnderCrisis();

        return R.ok().put("res", result);
    }
}
