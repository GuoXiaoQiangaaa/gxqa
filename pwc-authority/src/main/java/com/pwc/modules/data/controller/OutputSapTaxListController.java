package com.pwc.modules.data.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import com.pwc.common.excel.ExportExcel;
import com.pwc.common.exception.RRException;
import com.pwc.common.utils.DateUtils;
import com.pwc.common.validator.ValidatorUtils;
import com.pwc.modules.sys.shiro.ShiroUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.pwc.modules.data.entity.OutputSapTaxListEntity;
import com.pwc.modules.data.service.OutputSapTaxListService;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;


/**
 * SAP税码清单
 *
 * @author fanpf
 * @date 2020/8/27
 */
@RestController
@RequestMapping("data/outputsaptaxlist")
@Slf4j
public class OutputSapTaxListController {
    @Autowired
    private OutputSapTaxListService outputSapTaxListService;

    /**
     * 列表
     */
    @GetMapping("/list")
//    @RequiresPermissions("data:outputsaptaxlist:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = outputSapTaxListService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{taxId}")
//    @RequiresPermissions("data:outputsaptaxlist:info")
    public R info(@PathVariable("taxId") Long taxId){
        OutputSapTaxListEntity outputSapTaxList = outputSapTaxListService.getById(taxId);

        return R.ok().put("outputSapTaxList", outputSapTaxList);
    }

    /**
     * 保存
     */
    @PutMapping("/save")
//    @RequiresPermissions("data:outputsaptaxlist:save")
    public R save(@RequestBody OutputSapTaxListEntity outputSapTaxList){
        outputSapTaxList.setCreateBy(String.valueOf(ShiroUtils.getUserId()));
        outputSapTaxList.setCreateTime(new Date());
        outputSapTaxListService.save(outputSapTaxList);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
//    @RequiresPermissions("data:outputsaptaxlist:update")
    public R update(@RequestBody OutputSapTaxListEntity outputSapTaxList){
        ValidatorUtils.validateEntity(outputSapTaxList);
        outputSapTaxList.setUpdateBy(String.valueOf(ShiroUtils.getUserId()));
        outputSapTaxList.setUpdateTime(new Date());
        outputSapTaxListService.updateById(outputSapTaxList);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
//    @RequiresPermissions("data:outputsaptaxlist:delete")
    public R delete(@RequestBody Long[] taxIds){
        outputSapTaxListService.removeByIds(Arrays.asList(taxIds));

        return R.ok();
    }


    /**
     * 禁用/启用
     */
    @PostMapping("/disableOrEnable")
    public R disableOrEnable(@RequestBody OutputSapTaxListEntity reqVo){
        outputSapTaxListService.disableOrEnable(reqVo);

        return R.ok();
    }

    /**
     * 关键字查询
     */
    @GetMapping("/search")
    public R search(@RequestParam Map<String, Object> params){
        PageUtils page = outputSapTaxListService.search(params);

        return R.ok().put("page", page);
    }

    /**
     * 数据导入
     */
    @PostMapping("/importSapTax")
    public R importSapTax(@RequestParam("file") MultipartFile file){
        Map<String, Object> resMap = outputSapTaxListService.importSapTax(file);

        return R.ok().put("res", resMap);
    }


    /**
     * 下载模板
     */
    @GetMapping("/exportTemplate")
    public R exportTemplate(HttpServletResponse response){
        try {
            String fileName = "SAPTaxInfo" + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
            new ExportExcel("", OutputSapTaxListEntity.class).write(response, fileName).dispose();
        }catch (Exception e){
            log.error("下载SAP税码清单Excel模板出错: {}", e);
            throw new RRException("下载SAP税码清单Excel模板发生异常");
        }
        return null;
    }

}
