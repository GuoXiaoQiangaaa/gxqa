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

import com.pwc.modules.data.entity.OutputItemListEntity;
import com.pwc.modules.data.service.OutputItemListService;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;


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
        outputItemList.setUpdateBy(String.valueOf(ShiroUtils.getUserId()));
        outputItemList.setUpdateTime(new Date());
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
    public R importItem(@RequestParam("file") MultipartFile file){
        Map<String, Object> resMap = outputItemListService.importItem(file);

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
