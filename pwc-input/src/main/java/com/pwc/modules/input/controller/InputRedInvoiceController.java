package com.pwc.modules.input.controller;

import java.util.Arrays;
import java.util.Map;

import com.pwc.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.pwc.modules.input.entity.InputRedInvoiceEntity;
import com.pwc.modules.input.service.InputRedInvoiceService;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;
import org.springframework.web.multipart.MultipartFile;


/**
 * 红字发票管理
 *
 * @author fanpf
 * @date 2020/8/25
 */
@RestController
@RequestMapping("input/inputredinvoice")
public class InputRedInvoiceController {
    @Autowired
    private InputRedInvoiceService inputRedInvoiceService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("input:inputredinvoice:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = inputRedInvoiceService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{redId}")
    @RequiresPermissions("input:inputredinvoice:info")
    public R info(@PathVariable("redId") Long redId){
        InputRedInvoiceEntity inputRedInvoice = inputRedInvoiceService.getById(redId);

        return R.ok().put("inputRedInvoice", inputRedInvoice);
    }

    /**
     * 保存
     */
    @PutMapping("/save")
    @RequiresPermissions("input:inputredinvoice:save")
    public R save(@RequestBody InputRedInvoiceEntity inputRedInvoice){
        inputRedInvoiceService.save(inputRedInvoice);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("input:inputredinvoice:update")
    public R update(@RequestBody InputRedInvoiceEntity inputRedInvoice){
        ValidatorUtils.validateEntity(inputRedInvoice);
        inputRedInvoiceService.updateById(inputRedInvoice);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @RequiresPermissions("input:inputredinvoice:delete")
    public R delete(@RequestBody Long[] redIds){
        inputRedInvoiceService.removeByIds(Arrays.asList(redIds));

        return R.ok();
    }

    /**
     * 条件查询
     */
    @GetMapping("/conditionList")
    public R conditionList(@RequestParam Map<String, Object> params, InputRedInvoiceEntity redInvoiceEntity){
        R r = new R();
        PageUtils page = inputRedInvoiceService.conditionList(params, redInvoiceEntity);
        r.put("page", page);
        return r;
    }

    /**
     * 导入红字通知单
     */
    @PostMapping("/importRedNotice")
    public R importRedNotice(@RequestParam("file") MultipartFile file){
        R r = new R();
        inputRedInvoiceService.importRedNotice(file);
        return r;
    }

    /**
     * 导入红字发票并更新红字通知单状态
     */
    @PostMapping("/importRedInvoice")
    public R importRedInvoice(@RequestParam("file") MultipartFile file){
        R r = new R();
        inputRedInvoiceService.importRedInvoice(file);
        return r;
    }
}
