package com.pwc.modules.filing.controller;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;
import com.pwc.common.validator.ValidatorUtils;
import com.pwc.modules.filing.entity.FilingProcessEntity;
import com.pwc.modules.filing.service.FilingProcessService;
import com.pwc.modules.sys.controller.AbstractController;
//import oracle.jdbc.proxy.annotation.Post;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 申报流程设置
 *
 * @author zk
 * @email 
 * @date 2019-11-08 15:11:47
 */
@RestController
@RequestMapping("filing/process")
public class FilingProcessController extends AbstractController {
    @Autowired
    private FilingProcessService filingProcessService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("filing:process:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = filingProcessService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 当前登录公司的流程展示
     */
    @GetMapping("/info")
    @RequiresPermissions("filing:process:info")
    public R info(){

        String datePattern = "yyyy-MM";
        String currentDateStr = DateUtil.format(DateUtil.date(), datePattern);
        FilingProcessEntity filingProcess = filingProcessService.queryByDeptAndCreateTime(getDeptId(), currentDateStr);

        return R.ok().put("filingProcess", filingProcess);
    }

    /**
     * 信息
     */
    @GetMapping("/info/{processId}")
    @RequiresPermissions("filing:process:info")
    public R info(@PathVariable("processId") Long processId){
        FilingProcessEntity filingProcess = filingProcessService.getById(processId);

        return R.ok().put("filingProcess", filingProcess);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("filing:process:save")
    public R save(@RequestBody FilingProcessEntity filingProcess){
        ValidatorUtils.validateEntity(filingProcess);
        String datePattern = "yyyy-MM";
        String currentDateStr = DateUtil.format(DateUtil.date(), datePattern);

        if(null != filingProcessService.queryByDeptAndCreateTime(getDeptId(),currentDateStr)) {
            return R.error("每个月只能设置一次");
        }
        filingProcess.setDeptId(getDeptId());
        filingProcessService.saveProcess(filingProcess);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("filing:process:update")
    public R update(@RequestBody FilingProcessEntity filingProcess){
        ValidatorUtils.validateEntity(filingProcess);
        filingProcessService.updateById(filingProcess);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @RequiresPermissions("filing:process:delete")
    public R delete(@RequestBody Long[] processIds){
        filingProcessService.removeByIds(Arrays.asList(processIds));

        return R.ok();
    }

}
