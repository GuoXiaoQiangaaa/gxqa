package com.pwc.modules.input.controller;

import com.pwc.common.excel.ExportExcel;
import com.pwc.common.exception.RRException;
import com.pwc.common.utils.DateUtils;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;
import com.pwc.common.validator.ValidatorUtils;
import com.pwc.modules.input.entity.InputInvoiceCustomsEntity;
import com.pwc.modules.input.entity.InputRedInvoiceEntity;
import com.pwc.modules.input.service.InputRedInvoiceService;
import com.pwc.modules.sys.shiro.ShiroUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.*;


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
        if(inputRedInvoice.getRedStatus() != null && inputRedInvoice.getRedStatus().equals("2")){
            //走红字通知单作废逻辑
            inputRedInvoiceService.obsoleteEntryByRed(inputRedInvoice);
        }
        inputRedInvoice.setUpdateBy(String.valueOf(ShiroUtils.getUserId()));
        inputRedInvoice.setUpdateTime(new Date());
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
    public R importRedNotice(@RequestParam("files") MultipartFile[] files){
        Map<String, Object> resMap = inputRedInvoiceService.importRedNotice(files);
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


    /**
     * 查询账票匹配成功数据
     *
     * @param params
     * @return
     */
    @RequestMapping("/getListByMatching")
    //@RequiresPermissions("input:invoicecustoms:getListBySuccess")
    public R getListBySuccess(@RequestParam Map<String, Object> params) {
        PageUtils page = inputRedInvoiceService.getListByMatching(params);
        return R.ok().put("page", page);
    }

    /**
     * 查询账票匹配功数据(下载)
     *
     * @param params
     * @return
     */
    @RequestMapping("/getListByMatchingAndExcel")
    //@RequiresPermissions("input:invoicecustoms:getListBySuccessAndExcel")
    public R getListByMatchingAndExcel(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        String title = (String) params.get("title");
        List list = new ArrayList();
        if (StringUtils.isNotBlank((String) params.get("ids"))) {
            String[] ids = ((String) params.get("ids")).split(",");
            for (String id : ids) {
                list.add(inputRedInvoiceService.getById(id));
            }
        } else {
            int count = inputRedInvoiceService.getListByShow();
            params.put("limit", count + "");
            PageUtils page = inputRedInvoiceService.getListByMatching(params);
            list = page.getList();
        }
        try {
            String fileName = title + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
            new ExportExcel(title, InputRedInvoiceEntity.class).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("导出失败");
        }
    }

    /**
     * 红字通知单手工入账
     *
     * @param params
     * @return
     */
    @RequestMapping("/manualEntryByRed")
    public R manualEntry(@RequestParam Map<String, Object> params) {
        String sapMatch = inputRedInvoiceService.manualEntryByRed(params);
        return R.ok().put("sapMatch",sapMatch);
    }

}
