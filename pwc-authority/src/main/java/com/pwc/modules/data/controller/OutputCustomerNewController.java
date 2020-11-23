package com.pwc.modules.data.controller;

import com.pwc.common.excel.ExportExcel;
import com.pwc.common.exception.RRException;
import com.pwc.common.utils.DateUtils;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;
import com.pwc.common.validator.ValidatorUtils;
import com.pwc.modules.data.entity.OutputCustomerNewEntity;
import com.pwc.modules.data.service.OutputCustomerNewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;


/**
 * 客户信息
 *
 * @author fanpf
 * @date 2020/8/24
 */
@RestController
@RequestMapping("data/outputcustomernew")
@Slf4j
public class OutputCustomerNewController {
    @Autowired
    private OutputCustomerNewService outputCustomerNewService;

    /**
     * 列表
     */
    @GetMapping("/list")
//    @RequiresPermissions("data:outputcustomernew:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = outputCustomerNewService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 详情
     */
    @GetMapping("/info/{customerId}")
//    @RequiresPermissions("data:outputcustomernew:info")
    public R info(@PathVariable("customerId") Long customerId){
        OutputCustomerNewEntity outputCustomerNew = outputCustomerNewService.getById(customerId);

        return R.ok().put("outputCustomerNew", outputCustomerNew);
    }

    /**
     * 新增
     */
    @PutMapping("/save")
//    @RequiresPermissions("data:outputcustomernew:save")
    public R save(@RequestBody OutputCustomerNewEntity outputCustomerNew){
        ValidatorUtils.validateEntity(outputCustomerNew);
        outputCustomerNewService.save(outputCustomerNew);

        return R.ok();
    }

    /**
     * 编辑
     */
    @PostMapping("/update")
//    @RequiresPermissions("data:outputcustomernew:update")
    public R update(@RequestBody OutputCustomerNewEntity outputCustomerNew){
        ValidatorUtils.validateEntity(outputCustomerNew);
        outputCustomerNewService.updateById(outputCustomerNew);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
//    @RequiresPermissions("data:outputcustomernew:delete")
    public R delete(@RequestBody Long[] customerIds){
        outputCustomerNewService.removeByIds(Arrays.asList(customerIds));

        return R.ok();
    }

    /**
     * 禁用/启用
     */
    @PostMapping("/disableOrEnable")
    public R disableOrEnable(@RequestBody OutputCustomerNewEntity reqVo){
        outputCustomerNewService.disableOrEnable(reqVo);

        return R.ok();
    }

    /**
     * 关键字查询
     */
    @GetMapping("/search")
    public R search(@RequestParam Map<String, Object> params){
        PageUtils page = outputCustomerNewService.search(params);

        return R.ok().put("page", page);
    }

    /**
     * 数据导入
     */
    @PostMapping("/importCustomer")
    public R importCustomer(@RequestParam("files") MultipartFile[] files){
        Map<String, Object> resMap = outputCustomerNewService.importCustomer(files);

        return R.ok().put("res", resMap);
    }


    /**
     * 下载模板
     */
    @GetMapping("/exportTemplate")
    public R exportTemplate(HttpServletResponse response){
        try {
            String fileName = "CustomerInfo" + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
            new ExportExcel("", OutputCustomerNewEntity.class).write(response, fileName).dispose();
        }catch (Exception e){
            log.error("下载客户Excel模板出错: {}", e);
            throw new RRException("下载客户Excel模板发生异常");
        }
        return null;
    }
}
