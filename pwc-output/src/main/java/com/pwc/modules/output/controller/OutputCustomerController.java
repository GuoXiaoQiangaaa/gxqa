package com.pwc.modules.output.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cn.hutool.core.collection.CollUtil;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;
import com.pwc.common.validator.ValidatorUtils;
import com.pwc.modules.sys.entity.SysDeptEntity;
import com.pwc.modules.sys.service.SysDeptService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pwc.modules.output.entity.OutputCustomerEntity;
import com.pwc.modules.output.service.OutputCustomerService;







/**
 * 客户基本信息
 *
 * @author zk
 * @email 
 * @date 2020-06-01 18:17:06
 */
@RestController
@RequestMapping("output/customer")
public class OutputCustomerController {
    @Autowired
    private OutputCustomerService outputCustomerService;

    @Autowired
    private SysDeptService sysDeptService;



    /**
     * 管理员查看列表
     */
    @RequestMapping("/deptlist")
//    @RequiresPermissions("output:customer:deptlist")
    public R deptlist(@RequestParam Map<String, Object> params){

        PageUtils page = sysDeptService.queryPage(params);
        for (Object obj : page.getList()) {
            SysDeptEntity deptEntity = (SysDeptEntity)obj;
            List<OutputCustomerEntity> customerList = outputCustomerService.getByDeptId(deptEntity.getDeptId(),"");
            deptEntity.setCustomers(CollUtil.isNotEmpty(customerList) ? customerList.size() : 0);
        }
        return R.ok().put("page", page);
    }



    /**
     * 列表
     */
    @RequestMapping("/list")
//    @RequiresPermissions("output:customer:list")
    public R list(@RequestParam Long deptId, @RequestParam(required = false) String customerName){
        List<OutputCustomerEntity> customerList = outputCustomerService.getByDeptId(deptId, customerName);
        return R.ok().put("list", customerList);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{customerId}")
//    @RequiresPermissions("output:customer:info")
    public R info(@PathVariable("customerId") Long customerId){
        OutputCustomerEntity outputCustomer = outputCustomerService.getById(customerId);

        return R.ok().put("customer", outputCustomer);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//    @RequiresPermissions("output:customer:save")
    public R save(@RequestBody OutputCustomerEntity outputCustomer){
        outputCustomerService.save(outputCustomer);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//    @RequiresPermissions("output:customer:update")
    public R update(@RequestBody OutputCustomerEntity outputCustomer){
        ValidatorUtils.validateEntity(outputCustomer);
        outputCustomerService.updateById(outputCustomer);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//    @RequiresPermissions("output:customer:delete")
    public R delete(@RequestBody Long[] customerIds){
        outputCustomerService.removeByIds(Arrays.asList(customerIds));

        return R.ok();
    }

}
