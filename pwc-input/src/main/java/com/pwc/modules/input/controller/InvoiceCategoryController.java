package com.pwc.modules.input.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;
import com.pwc.common.validator.ValidatorUtils;
import com.pwc.modules.input.entity.InputInvoiceCategoryEntity;
import com.pwc.modules.input.service.InputInvoiceCategoryService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/input/invoice/category")
public class InvoiceCategoryController {

    @Autowired
    private InputInvoiceCategoryService invoiceCategoryService;
    /**
     * 保存
     */
    @PutMapping("/save")
    @RequiresPermissions("input:category:save")
    public R save(@RequestBody InputInvoiceCategoryEntity invoiceCategoryEntity){
        invoiceCategoryEntity.setDelFlag(0);
        invoiceCategoryService.save(invoiceCategoryEntity);
        return R.ok();
    }

    /**
     * 列表
     */
    @GetMapping("/alllist")
    @RequiresPermissions("input:category:alllist")
    public R list(){
        List<InputInvoiceCategoryEntity> invoiceCategoryEntityList = invoiceCategoryService.list();
        List<Map<String, String>> result = new ArrayList<>();
        for (InputInvoiceCategoryEntity category : invoiceCategoryEntityList) {
            Map<String, String> map = new HashMap<>();
            map.put("label", category.getCategory());
            map.put("value", category.getCategory());
            result.add(map);
        }
        return R.ok().put("list", result);
    }


    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("input:category:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = invoiceCategoryService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @GetMapping("/info/{categoryId}")
    @RequiresPermissions("input:category:info")
    public R info(@PathVariable("categoryId") Long categoryId){
        InputInvoiceCategoryEntity invoiceCategoryEntity = invoiceCategoryService.getById(categoryId);

        return R.ok().put("invoiceCategory", invoiceCategoryEntity);
    }



    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("input:category:update")
    public R update(@RequestBody InputInvoiceCategoryEntity invoiceCategoryEntity){
        ValidatorUtils.validateEntity(invoiceCategoryEntity);
        invoiceCategoryService.updateById(invoiceCategoryEntity);

        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @RequiresPermissions("input:category:delete")
    public R delete(String categoryIds){
        String[] ids = categoryIds.split(",");

        invoiceCategoryService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }
}
