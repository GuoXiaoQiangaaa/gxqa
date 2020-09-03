package com.pwc.modules.input.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import com.pwc.common.excel.ExportExcel;
import com.pwc.common.exception.RRException;
import com.pwc.common.utils.DateUtils;
import com.pwc.common.validator.ValidatorUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.pwc.modules.input.entity.InputRedInvoiceEntity;
import com.pwc.modules.input.service.InputRedInvoiceService;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;


/**
 * 红字发票管理
 *
 * @author fanpf
 * @date 2020/8/25
 */
@RestController
@RequestMapping("input/inputredinvoice")
@Slf4j
public class InputRedInvoiceController {
    @Autowired
    private InputRedInvoiceService inputRedInvoiceService;

    /**
     * 列表
     */
    @GetMapping("/list")
//    @RequiresPermissions("input:inputredinvoice:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = inputRedInvoiceService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{redId}")
//    @RequiresPermissions("input:inputredinvoice:info")
    public R info(@PathVariable("redId") Long redId){
        InputRedInvoiceEntity inputRedInvoice = inputRedInvoiceService.getById(redId);

        return R.ok().put("inputRedInvoice", inputRedInvoice);
    }

    /**
     * 保存
     */
    @PutMapping("/save")
//    @RequiresPermissions("input:inputredinvoice:save")
    public R save(@RequestBody InputRedInvoiceEntity inputRedInvoice){
        ValidatorUtils.validateEntity(inputRedInvoice);
        inputRedInvoiceService.save(inputRedInvoice);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
//    @RequiresPermissions("input:inputredinvoice:update")
    public R update(@RequestBody InputRedInvoiceEntity inputRedInvoice){
        ValidatorUtils.validateEntity(inputRedInvoice);
        inputRedInvoiceService.updateById(inputRedInvoice);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
//    @RequiresPermissions("input:inputredinvoice:delete")
    public R delete(@RequestBody Long[] redIds){
        inputRedInvoiceService.removeByIds(Arrays.asList(redIds));

        return R.ok();
    }

    /**
     * 红字通知单条件查询
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
        Map<String, Object> resMap = inputRedInvoiceService.importRedNotice(file);
        return R.ok().put("res", resMap);
    }

    /**
     * 下载Excel模板
     */
    @GetMapping("/exportTemplate")
    public R exportTemplate(HttpServletResponse response){
        try {
            String fileName = "RedNoticeTemplate" + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
            new ExportExcel("", InputRedInvoiceEntity.class).write(response, fileName).dispose();
        } catch (Exception e) {
            log.error("下载红字通知单Excel模板出错: {}", e);
            throw new RRException("下载红字通知单Excel模板发生异常");
        }
        return null;
    }

    /**
     * 接收红字发票并更新红字通知单状态
     */
    @PostMapping("/receiveRedInvoice")
    public R receiveRedInvoice(@RequestParam("file") MultipartFile file){
        inputRedInvoiceService.receiveRedInvoice(file);
        return R.ok();
    }

    /**
     * 红字发票监控条件查询
     */
    @GetMapping("/redList")
    public R redList(@RequestParam Map<String, Object> params, InputRedInvoiceEntity redInvoiceEntity){
        R r = new R();
        PageUtils page = inputRedInvoiceService.redList(params, redInvoiceEntity);
        r.put("page", page);
        return r;
    }

    /**
     * 关联红字发票
     */
    @GetMapping("/link/{redId}")
    public R link(@PathVariable("redId") Long redId, @RequestParam Map<String, Object> params){
        boolean link = inputRedInvoiceService.link(redId, params);
        if(link){
            return R.ok();
        }else {
            return R.error("未查询到红字发票信息,关联失败");
        }
    }
}
