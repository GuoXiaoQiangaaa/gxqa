package com.pwc.modules.filing.controller;

import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;
import com.pwc.common.validator.ValidatorUtils;
import com.pwc.modules.filing.entity.FilingOperateLogEntity;
import com.pwc.modules.filing.service.FilingOperateLogService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 申报过程操作记录
 *
 * @author zk
 * @email 
 * @date 2019-11-08 15:11:47
 */
@RestController
@RequestMapping("filing/operatelog")
public class FilingOperateLogController {
    @Autowired
    private FilingOperateLogService filingOperateLogService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("filing:operatelog:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = filingOperateLogService.queryPage(params);

        return R.ok().put("page", page);
    }


}
