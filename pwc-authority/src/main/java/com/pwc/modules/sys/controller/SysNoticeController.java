package com.pwc.modules.sys.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pwc.modules.sys.service.SysNoticeService;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;



/**
 * 通知代办表
 *
 * @author zk
 * @email 
 * @date 2020-02-04 17:52:31
 */
@RestController
@RequestMapping("sys/notice")
public class SysNoticeController {
    @Autowired
    private SysNoticeService sysNoticeService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:notice:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = sysNoticeService.queryPage(params);

        return R.ok().put("page", page);
    }



}
