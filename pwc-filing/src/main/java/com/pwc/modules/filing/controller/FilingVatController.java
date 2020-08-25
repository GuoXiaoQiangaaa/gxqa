package com.pwc.modules.filing.controller;

import java.util.Arrays;
import java.util.Map;

import com.pwc.common.validator.ValidatorUtils;
import com.pwc.modules.sys.controller.AbstractController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.pwc.modules.filing.entity.FilingVatEntity;
import com.pwc.modules.filing.service.FilingVatService;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;
import org.springframework.web.multipart.MultipartFile;


/**
 * 
 *
 * @author zk
 * @email 
 * @date 2019-11-18 18:34:05
 */
@RestController
@RequestMapping("filing/vat")
public class FilingVatController extends AbstractController {
    @Autowired
    private FilingVatService filingVatService;

//    private

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("filing:vat:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = filingVatService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{vatId}")
    @RequiresPermissions("filing:vat:info")
    public R info(@PathVariable("vatId") Long vatId){
        FilingVatEntity filingVat = filingVatService.getById(vatId);

        return R.ok().put("filingVat", filingVat);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("filing:vat:save")
    public R save(@RequestBody FilingVatEntity filingVat){
        filingVatService.save(filingVat);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("filing:vat:update")
    public R update(@RequestBody FilingVatEntity filingVat){
        ValidatorUtils.validateEntity(filingVat);
        filingVatService.updateById(filingVat);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("filing:vat:delete")
    public R delete(@RequestBody Long[] vatIds){
        filingVatService.removeByIds(Arrays.asList(vatIds));

        return R.ok();
    }


    /**
     * 上传初始文件
     */
    @PostMapping(value = "/initUpload" ,consumes = "multipart/*",headers="content-type=multipart/form-data")
    @RequiresPermissions("filing:record:initUpload")
    public R initVat(@RequestParam MultipartFile file) {
        filingVatService.initUpload(file, getDeptId());
        return R.ok();
    }
}
