package com.pwc.modules.data.controller;

import com.pwc.common.excel.ExportExcel;
import com.pwc.common.exception.RRException;
import com.pwc.common.utils.DateUtils;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;
import com.pwc.common.validator.ValidatorUtils;
import com.pwc.modules.data.entity.OutputGoodsNewEntity;
import com.pwc.modules.data.service.OutputGoodsNewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;


/**
 * 商品信息
 *
 * @author fanpf
 * @date 2020/8/24
 */
@RestController
@RequestMapping("data/outputgoods")
@Slf4j
public class OutputGoodsNewController {
    @Autowired
    private OutputGoodsNewService outputGoodsService;

    /**
     * 列表
     */
    @GetMapping("/list")
    //@RequiresPermissions("data:outputgoods:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = outputGoodsService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 详情
     */
    @GetMapping("/info/{goodsId}")
//    @RequiresPermissions("data:outputgoods:info")
    public R info(@PathVariable("goodsId") Long goodsId){
        OutputGoodsNewEntity outputGoods = outputGoodsService.getById(goodsId);

        return R.ok().put("outputGoods", outputGoods);
    }

    /**
     * 新增
     */
    @PutMapping("/save")
//    @RequiresPermissions("data:outputgoods:save")
    public R save(@RequestBody OutputGoodsNewEntity outputGoods){
        ValidatorUtils.validateEntity(outputGoods);
        outputGoodsService.save(outputGoods);

        return R.ok();
    }

    /**
     * 编辑
     */
    @PostMapping("/update")
//    @RequiresPermissions("data:outputgoods:update")
    public R update(@RequestBody OutputGoodsNewEntity outputGoods){
        ValidatorUtils.validateEntity(outputGoods);
        outputGoodsService.updateById(outputGoods);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
//    @RequiresPermissions("data:outputgoods:delete")
    public R delete(@RequestBody Long[] goodsIds){
        outputGoodsService.removeByIds(Arrays.asList(goodsIds));

        return R.ok();
    }

    /**
     * 禁用/启用
     */
    @PostMapping("/disableOrEnable")
    public R disableOrEnable(@RequestBody OutputGoodsNewEntity reqVo){
        outputGoodsService.disableOrEnable(reqVo);

        return R.ok();
    }

    /**
     * 关键字查询
     */
    @GetMapping("/search")
    public R search(@RequestParam Map<String, Object> params){
        PageUtils page = outputGoodsService.search(params);

        return R.ok().put("page", page);
    }

    /**
     * 数据导入
     */
    @PostMapping("/importGoods")
    public R importGoods(@RequestParam("files") MultipartFile[] files){
        Map<String, Object> resMap = outputGoodsService.importGoods(files);
        return R.ok().put("res", resMap);
    }

    /**
     * 下载模板
     */
    @GetMapping("/exportTemplate")
    public R exportTemplate(HttpServletResponse response){
        try {
            String fileName = "GoodsInfo" + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
            new ExportExcel("", OutputGoodsNewEntity.class).write(response, fileName).dispose();
        }catch (Exception e){
            log.error("下载商品Excel模板出错: {}", e);
            throw new RRException("下载商品Excel模板发生异常");
        }
        return null;
    }
}
