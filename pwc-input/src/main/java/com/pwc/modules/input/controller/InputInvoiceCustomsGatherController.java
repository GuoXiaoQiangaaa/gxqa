package com.pwc.modules.input.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fapiao.neon.model.in.CustomsInvoiceResult;
import com.pwc.common.utils.DateUtils;
import com.pwc.common.utils.ParamsMap;
import com.pwc.common.utils.excel.ExportExcel;
import com.pwc.common.validator.ValidatorUtils;
import com.pwc.modules.input.entity.InputInvoiceCustomsGatherEntity;
import com.pwc.modules.input.service.InputInvoiceCustomsGatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;


/**
 * 海关缴款书（采集）
 *
 * @author myz
 * @email 
 * @date 2020-12-16 13:26:51
 */
@RestController
@RequestMapping("data/inputInvoiceCustomsGather")
public class InputInvoiceCustomsGatherController {
    @Autowired
    private InputInvoiceCustomsGatherService inputInvoiceCustomsGatherService;

    @RequestMapping("/getImportByGather")
    public R getImportByGather(@RequestParam("files") MultipartFile file){
        try {
            inputInvoiceCustomsGatherService.getImportBySap(file);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("上传失败，请稍后");
        }
        return R.ok();
    }


    /**
     * 根据id导出
     *
     * @param params
     * @param response
     * @return
     */
    @GetMapping(value = "/exportRecordListByIds")
    public R exportRecordListByIds(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        String title = (String) params.get("title");
        String ids = String.valueOf(params.get("ids"));
        List<InputInvoiceCustomsGatherEntity> inputInvoiceCustomsEntities = (List<InputInvoiceCustomsGatherEntity>) inputInvoiceCustomsGatherService.listByIds(Arrays.asList(ids.split(",")));
        try {
            String fileName = title + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
            new ExportExcel(title, InputInvoiceCustomsGatherEntity.class).setDataList(inputInvoiceCustomsEntities).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("导出失败");
        }
    }

    /**
     * 根据条件导出
     *
     * @param params
     * @param response
     * @return
     */
    @GetMapping(value = "/exportRecordList")
    public R exportRecordList(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        String title = (String) params.get("title");
        int count = inputInvoiceCustomsGatherService.getListByShow();
        params.put("limit", count + "");

        PageUtils pageUtils = inputInvoiceCustomsGatherService.queryPage(params);
        List<InputInvoiceCustomsGatherEntity> invoiceEntityList = (List<InputInvoiceCustomsGatherEntity>) pageUtils.getList();
        try {
            String fileName = title + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
            new ExportExcel(title, InputInvoiceCustomsGatherEntity.class).setDataList(invoiceEntityList).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("导出失败");
        }
    }

    /**
     * 列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = inputInvoiceCustomsGatherService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 海关缴款书采集结果获取
     */
    @GetMapping("/syncApplyResult")
    public R syncApplyResult(@RequestParam Map<String, Object> params) {
        String id = ParamsMap.findMap(params, "id");
        if(id != null){
            inputInvoiceCustomsGatherService.customsGatherResult(id);
        }
        return R.ok();
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        InputInvoiceCustomsGatherEntity inputInvoiceCustomsGather = inputInvoiceCustomsGatherService.getById(id);

        return R.ok().put("inputInvoiceCustomsGather", inputInvoiceCustomsGather);
    }

    /**
     * 保存
     */
    @PutMapping("/save")
    public R save(@RequestBody InputInvoiceCustomsGatherEntity inputInvoiceCustomsGather){
        inputInvoiceCustomsGatherService.save(inputInvoiceCustomsGather);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public R update(@RequestBody InputInvoiceCustomsGatherEntity inputInvoiceCustomsGather){
        InputInvoiceCustomsGatherEntity newEntity = inputInvoiceCustomsGatherService.getById(inputInvoiceCustomsGather.getId());
        newEntity.setDelFlag(inputInvoiceCustomsGather.getDelFlag());
        inputInvoiceCustomsGatherService.updateById(newEntity);
        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        inputInvoiceCustomsGatherService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
