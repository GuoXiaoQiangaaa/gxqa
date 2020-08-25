package com.pwc.modules.output.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.pwc.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.pwc.modules.output.entity.OutputGoodsEntity;
import com.pwc.modules.output.service.OutputGoodsService;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;



/**
 * 商品
 *
 * @author zk
 * @email 
 * @date 2020-06-01 18:17:05
 */
@RestController
@RequestMapping("output/goods")
public class OutputGoodsController {
    @Autowired
    private OutputGoodsService outputGoodsService;


    /**
     * 查询所有商品
     * @param keywords
     * @return
     */
    @GetMapping("/alllist")
    public R alllist(String keywords) {
        List<OutputGoodsEntity> allgoods = outputGoodsService.queryAllGoods(keywords);
        return R.ok().put("list", allgoods);
    }
    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("output:goods:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = outputGoodsService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{goodsId}")
    @RequiresPermissions("output:goods:info")
    public R info(@PathVariable("goodsId") Long goodsId){
        OutputGoodsEntity outputGoods = outputGoodsService.getById(goodsId);

        return R.ok().put("goods", outputGoods);
    }

    /**
     * 保存
     */
    @PutMapping("/save")
    @RequiresPermissions("output:goods:save")
    public R save(@RequestBody OutputGoodsEntity outputGoods){
        outputGoodsService.save(outputGoods);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("output:goods:update")
    public R update(@RequestBody OutputGoodsEntity outputGoods){
        ValidatorUtils.validateEntity(outputGoods);
        outputGoodsService.updateById(outputGoods);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @RequiresPermissions("output:goods:delete")
    public R delete(@RequestBody Long[] goodsIds){
        outputGoodsService.removeByIds(Arrays.asList(goodsIds));

        return R.ok();
    }

}
