package com.pwc.modules.output.controller;

import java.util.Arrays;
import java.util.Map;

import com.pwc.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pwc.modules.output.entity.OutputGoodsCustomerEntity;
import com.pwc.modules.output.service.OutputGoodsCustomerService;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;



/**
 * 商品客户关联价格
 *
 * @author zk
 * @email 
 * @date 2020-06-01 18:17:06
 */
@RestController
@RequestMapping("output/outputgoodscustomer")
public class OutputGoodsCustomerController {
    @Autowired
    private OutputGoodsCustomerService outputGoodsCustomerService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("output:outputgoodscustomer:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = outputGoodsCustomerService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{priceId}")
    @RequiresPermissions("output:outputgoodscustomer:info")
    public R info(@PathVariable("priceId") Long priceId){
        OutputGoodsCustomerEntity outputGoodsCustomer = outputGoodsCustomerService.getById(priceId);

        return R.ok().put("outputGoodsCustomer", outputGoodsCustomer);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("output:outputgoodscustomer:save")
    public R save(@RequestBody OutputGoodsCustomerEntity outputGoodsCustomer){
        outputGoodsCustomerService.save(outputGoodsCustomer);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("output:outputgoodscustomer:update")
    public R update(@RequestBody OutputGoodsCustomerEntity outputGoodsCustomer){
        ValidatorUtils.validateEntity(outputGoodsCustomer);
        outputGoodsCustomerService.updateById(outputGoodsCustomer);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("output:outputgoodscustomer:delete")
    public R delete(@RequestBody Long[] priceIds){
        outputGoodsCustomerService.removeByIds(Arrays.asList(priceIds));

        return R.ok();
    }

}
