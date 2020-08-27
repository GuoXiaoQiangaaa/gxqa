package com.pwc.modules.data.controller;

import java.util.Arrays;
import java.util.Map;

import com.pwc.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.pwc.modules.data.entity.OutputGoodsNewEntity;
import com.pwc.modules.data.service.OutputGoodsNewService;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;



/**
 * 商品信息
 *
 * @author fanpf
 * @date 2020/8/24
 */
@RestController
@RequestMapping("data/outputgoods")
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
    @RequiresPermissions("data:outputgoods:info")
    public R info(@PathVariable("goodsId") Long goodsId){
        OutputGoodsNewEntity outputGoods = outputGoodsService.getById(goodsId);

        return R.ok().put("outputGoods", outputGoods);
    }

    /**
     * 新增
     */
    @PutMapping("/save")
    @RequiresPermissions("data:outputgoods:save")
    public R save(@RequestBody OutputGoodsNewEntity outputGoods){
        outputGoodsService.save(outputGoods);

        return R.ok();
    }

    /**
     * 编辑
     */
    @PostMapping("/update")
    @RequiresPermissions("data:outputgoods:update")
    public R update(@RequestBody OutputGoodsNewEntity outputGoods){
        ValidatorUtils.validateEntity(outputGoods);
        outputGoodsService.updateById(outputGoods);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @RequiresPermissions("data:outputgoods:delete")
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

}
