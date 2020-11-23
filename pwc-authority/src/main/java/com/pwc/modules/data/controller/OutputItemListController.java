package com.pwc.modules.data.controller;

import com.pwc.common.annotation.SysLog;
import com.pwc.common.excel.ExportExcel;
import com.pwc.common.exception.RRException;
import com.pwc.common.utils.DateUtils;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;
import com.pwc.common.validator.ValidatorUtils;
import com.pwc.modules.data.entity.OutputItemListEntity;
import com.pwc.modules.data.service.OutputItemListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;


/**
 * 科目清单
 *
 * @author fanpf
 * @date 2020/8/27
 */
@RestController
@RequestMapping("data/outputitemlist")
@Slf4j
public class OutputItemListController {
    @Autowired
    private OutputItemListService outputItemListService;

    /**
     * 列表
     */
    @GetMapping("/list")
//    @RequiresPermissions("data:outputitemlist:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = outputItemListService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{itemId}")
//    @RequiresPermissions("data:outputitemlist:info")
    public R info(@PathVariable("itemId") Long itemId){
        OutputItemListEntity outputItemList = outputItemListService.getById(itemId);

        return R.ok().put("outputItemList", outputItemList);
    }

    /**
     * 保存
     */
    @SysLog("添加科目")
    @PutMapping("/save")
//    @RequiresPermissions("data:outputitemlist:save")
    public R save(@RequestBody OutputItemListEntity outputItemList){
        ValidatorUtils.validateEntity(outputItemList);

        outputItemListService.save(outputItemList);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
//    @RequiresPermissions("data:outputitemlist:update")
    public R update(@RequestBody OutputItemListEntity outputItemList){
        ValidatorUtils.validateEntity(outputItemList);

        outputItemListService.updateById(outputItemList);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
//    @RequiresPermissions("data:outputitemlist:delete")
    public R delete(@RequestBody Long[] itemIds){
        outputItemListService.removeByIds(Arrays.asList(itemIds));

        return R.ok();
    }

    /**
     * 禁用/启用
     */
    @PostMapping("/disableOrEnable")
    public R disableOrEnable(@RequestBody OutputItemListEntity reqVo){
        outputItemListService.disableOrEnable(reqVo);

        return R.ok();
    }

    /**
     * 关键字查询
     */
    @GetMapping("/search")
    public R search(@RequestParam Map<String, Object> params){
        PageUtils page = outputItemListService.search(params);

        return R.ok().put("page", page);
    }

    /**
     * 数据导入
     */
    @PostMapping("/importItem")
    public R importItem(@RequestParam("files") MultipartFile[] files){
        Map<String, Object> resMap = outputItemListService.importItem(files);

        return R.ok().put("res", resMap);
    }


    /**
     * 下载模板
     */
    @GetMapping("/exportTemplate")
    public R exportTemplate(HttpServletResponse response){
        try {
            String fileName = "ItemInfo" + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
            new ExportExcel("", OutputItemListEntity.class).write(response, fileName).dispose();
        }catch (Exception e){
            log.error("下载科目清单Excel模板出错: {}", e);
            throw new RRException("下载科目清单Excel模板发生异常");
        }
        return null;
    }
}
