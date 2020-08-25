package com.pwc.modules.filing.controller;

import java.util.Arrays;
import java.util.Map;

import com.pwc.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.pwc.modules.filing.entity.FilingBelleVatEntity;
import com.pwc.modules.filing.service.FilingBelleVatService;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;
import org.springframework.web.multipart.MultipartFile;


/**
 * 
 *
 * @author zk
 * @email 
 * @date 2020-01-13 18:32:25
 */
@RestController
@RequestMapping("filing/belle/vat")
public class FilingBelleVatController {
    @Autowired
    private FilingBelleVatService filingBelleVatService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("filing:filingbellevat:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = filingBelleVatService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{vatId}")
    @RequiresPermissions("filing:filingbellevat:info")
    public R info(@PathVariable("vatId") Long vatId){
        FilingBelleVatEntity filingBelleVat = filingBelleVatService.getById(vatId);

        return R.ok().put("filingBelleVat", filingBelleVat);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("filing:filingbellevat:save")
    public R save(@RequestBody FilingBelleVatEntity filingBelleVat){
        filingBelleVatService.save(filingBelleVat);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("filing:filingbellevat:update")
    public R update(@RequestBody FilingBelleVatEntity filingBelleVat){
        ValidatorUtils.validateEntity(filingBelleVat);
        filingBelleVatService.updateById(filingBelleVat);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("filing:filingbellevat:delete")
    public R delete(@RequestBody Long[] vatIds){
        filingBelleVatService.removeByIds(Arrays.asList(vatIds));

        return R.ok();
    }

    /**
     * 上传初始文件
     */
    @PostMapping(value = "/initUpload" ,consumes = "multipart/*",headers="content-type=multipart/form-data")
    @RequiresPermissions("filing:record:initUpload")
    public R initVat(@RequestParam MultipartFile file) {
        filingBelleVatService.initUpload(file);
        return R.ok();
    }
}
