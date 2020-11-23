package com.pwc.modules.input.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.pwc.common.excel.ExportExcel;
import com.pwc.common.exception.RRException;
import com.pwc.common.utils.DateUtils;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;
import com.pwc.common.validator.ValidatorUtils;
import com.pwc.modules.input.entity.InputInvoiceWhtEntity;
import com.pwc.modules.input.service.InputInvoiceWhtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;



/**
 * WHT(代扣代缴单据)
 *
 * @author fanpf
 * @date 2020/9/4
 */
@RestController
@RequestMapping("input/inputinvoicewht")
@Slf4j
public class InputInvoiceWhtController {
    @Autowired
    private InputInvoiceWhtService inputInvoiceWhtService;

    /**
     * 列表
     */
    @GetMapping("/list")
//    @RequiresPermissions("input:inputinvoicewht:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = inputInvoiceWhtService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{whtId}")
//    @RequiresPermissions("input:inputinvoicewht:info")
    public R info(@PathVariable("whtId") Long whtId){
        InputInvoiceWhtEntity inputInvoiceWht = inputInvoiceWhtService.getById(whtId);

        return R.ok().put("inputInvoiceWht", inputInvoiceWht);
    }

    /**
     * 保存
     */
    @PutMapping("/save")
//    @RequiresPermissions("input:inputinvoicewht:save")
    public R save(@RequestBody InputInvoiceWhtEntity inputInvoiceWht){
        inputInvoiceWhtService.save(inputInvoiceWht);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
//    @RequiresPermissions("input:inputinvoicewht:update")
    public R update(@RequestBody InputInvoiceWhtEntity inputInvoiceWht){
        ValidatorUtils.validateEntity(inputInvoiceWht);
        inputInvoiceWhtService.updateById(inputInvoiceWht);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
//    @RequiresPermissions("input:inputinvoicewht:delete")
    public R delete(@RequestBody Long[] whtIds){
        inputInvoiceWhtService.removeByIds(Arrays.asList(whtIds));

        return R.ok();
    }

    /**
     * 逻辑删除
     */
    @GetMapping("/remove")
    public R remove(@RequestParam Map<String, Object> params){
        inputInvoiceWhtService.remove(params);

        return R.ok();
    }

    /**
     * 模板下载
     */
    @GetMapping("/exportTemplate")
    public R exportTemplate(HttpServletResponse response){
        try {
            String fileName = "WHTTemplate" + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
            new ExportExcel("", InputInvoiceWhtEntity.class).write(response, fileName).dispose();
        } catch (Exception e) {
            log.error("下载WHTExcel模板出错: {}", e);
            throw new RRException("下载WHTExcel模板发生异常");
        }
        return null;
    }

    /**
     * 数据导入
     */
    @PostMapping("/importWht")
    public R importWht(@RequestParam("files") MultipartFile[] files){
        Map<String, Object> resMap = inputInvoiceWhtService.importWht(files);
        return R.ok().put("res", resMap);
    }

    /**
     * 数据导出
     */
    @GetMapping("/exportData")
    public R exportData(@RequestParam Map<String, Object> params, HttpServletResponse response){
        try {
            List<InputInvoiceWhtEntity> entityList = inputInvoiceWhtService.queryList(params);
            if(CollectionUtil.isEmpty(entityList)){
                log.error("根据条件未查询到数据");
                throw new RRException("未查询到数据");
            }
            String fileName = "WHTData" + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
            new ExportExcel("", InputInvoiceWhtEntity.class).setDataList(entityList) .write(response, fileName).dispose();
        } catch (RRException e) {
            log.error("未选择需要导出的数据或参数有误: {}", e);
            throw e;
        } catch (Exception e) {
            log.error("导出WHT数据出错: {}", e);
            throw new RRException("导出WHT数据发生异常");
        }
        return null;
    }
}
