package com.pwc.modules.data.controller;

import com.pwc.common.excel.ExportExcel;
import com.pwc.common.exception.RRException;
import com.pwc.common.utils.DateUtils;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;
import com.pwc.common.validator.ValidatorUtils;
import com.pwc.modules.data.entity.OutputSupplierEntity;
import com.pwc.modules.data.service.OutputSupplierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;


/**
 * 供应商信息
 *
 * @author fanpf
 * @date 2020/8/24
 */
@RestController
@RequestMapping("data/outputsupplier")
@Slf4j
public class OutputSupplierController {
    @Autowired
    private OutputSupplierService outputSupplierService;

    /**
     * 列表
     */
    @GetMapping("/list")
//    @RequiresPermissions("data:outputsupplier:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = outputSupplierService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 详情
     */
    @GetMapping("/info/{supplierId}")
//    @RequiresPermissions("data:outputsupplier:info")
    public R info(@PathVariable("supplierId") Long supplierId){
        OutputSupplierEntity outputSupplier = outputSupplierService.getById(supplierId);

        return R.ok().put("outputSupplier", outputSupplier);
    }

    /**
     * 新增
     */
    @PutMapping("/save")
//    @RequiresPermissions("data:outputsupplier:save")
    public R save(@RequestBody OutputSupplierEntity outputSupplier){
        ValidatorUtils.validateEntity(outputSupplier);
        outputSupplierService.save(outputSupplier);

        return R.ok();
    }

    /**
     * 编辑
     */
    @PostMapping("/update")
//    @RequiresPermissions("data:outputsupplier:update")
    public R update(@RequestBody OutputSupplierEntity outputSupplier){
        ValidatorUtils.validateEntity(outputSupplier);
        outputSupplierService.updateById(outputSupplier);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
//    @RequiresPermissions("data:outputsupplier:delete")
    public R delete(@RequestBody Long[] supplierIds){
        outputSupplierService.removeByIds(Arrays.asList(supplierIds));

        return R.ok();
    }

    /**
     * 禁用/启用
     */
    @PostMapping("/disableOrEnable")
    public R disableOrEnable(@RequestBody OutputSupplierEntity reqVo){
        outputSupplierService.disableOrEnable(reqVo);

        return R.ok();
    }

    /**
     * 关键字查询
     */
    @GetMapping("/search")
    public R search(@RequestParam Map<String, Object> params){
        PageUtils page = outputSupplierService.search(params);

        return R.ok().put("page", page);
    }

    /**
     * 数据导入
     */
    @PostMapping("/importSupplier")
    public R importSupplier(@RequestParam("files") MultipartFile[] files){
        Map<String, Object> resMap = outputSupplierService.importSupplier(files);

        return R.ok().put("res", resMap);
    }

    /**
     * 下载模板
     */
    @GetMapping("/exportTemplate")
    public R exportTemplate(HttpServletResponse response){
        try {
            String fileName = "SupplierInfo" + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
            new ExportExcel("", OutputSupplierEntity.class).write(response, fileName).dispose();
        } catch (Exception e) {
            log.error("下载供应商Excel模板出错: {}", e);
            throw new RRException("下载供应商Excel模板发生异常");
        }
        return null;
    }

}
