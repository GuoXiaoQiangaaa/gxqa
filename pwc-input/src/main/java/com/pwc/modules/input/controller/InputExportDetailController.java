package com.pwc.modules.input.controller;

import com.pwc.common.excel.ExportExcel;
import com.pwc.common.exception.RRException;
import com.pwc.common.utils.DateUtils;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;
import com.pwc.common.validator.ValidatorUtils;
import com.pwc.modules.input.entity.InputExportDetailEntity;
import com.pwc.modules.input.service.InputExportDetailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;



/**
 * 进项转出明细
 *
 * @author fanpf
 * @date 2020/9/17
 */
@RestController
@RequestMapping("input/inputexportdetail")
@Slf4j
public class InputExportDetailController {
    @Autowired
    private InputExportDetailService inputExportDetailService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("input:inputexportdetail:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = inputExportDetailService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{exportId}")
    @RequiresPermissions("input:inputexportdetail:info")
    public R info(@PathVariable("exportId") Long exportId){
        InputExportDetailEntity inputExportDetail = inputExportDetailService.getById(exportId);

        return R.ok().put("inputExportDetail", inputExportDetail);
    }

    /**
     * 保存
     */
    @PutMapping("/save")
    @RequiresPermissions("input:inputexportdetail:save")
    public R save(@RequestBody InputExportDetailEntity inputExportDetail){
        inputExportDetailService.save(inputExportDetail);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("input:inputexportdetail:update")
    public R update(@RequestBody InputExportDetailEntity inputExportDetail){
        ValidatorUtils.validateEntity(inputExportDetail);
        inputExportDetailService.updateById(inputExportDetail);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @RequiresPermissions("input:inputexportdetail:delete")
    public R delete(@RequestBody Long[] exportIds){
        inputExportDetailService.removeByIds(Arrays.asList(exportIds));

        return R.ok();
    }

    /**
     * 进项转出明细导入
     */
    @PostMapping("/importData")
    public R importData(@RequestParam("files") MultipartFile[] files){
        Map<String, Object> resMap = inputExportDetailService.importData(files);

        return R.ok().put("res", resMap);
    }

    /**
     * 进项转出明细模板下载
     */
    @GetMapping("/exportTemplate")
    public R exportTemplate(HttpServletResponse response){
        try {
            String fileName = "ExportDetailTemplate" + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
            new ExportExcel("", InputExportDetailEntity.class).write(response, fileName).dispose();
        } catch (Exception e) {
            log.error("下载进项转出明细Excel模板出错: {}", e);
            throw new RRException("下载进项转出明细Excel模板发生异常");
        }
        return null;
    }

}
