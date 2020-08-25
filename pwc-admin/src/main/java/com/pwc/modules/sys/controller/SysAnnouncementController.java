package com.pwc.modules.sys.controller;

import java.util.Arrays;
import java.util.Map;

import com.pwc.common.validator.ValidatorUtils;
import com.pwc.modules.sys.entity.SysAnnouncementEntity;
import com.pwc.modules.sys.service.SysAnnouncementService;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;



/**
 * 公告表
 *
 * @author zk
 * @email 
 * @date 2019-12-09 19:03:00
 */
@RestController
@RequestMapping("sys/announcement")
public class SysAnnouncementController {
    @Autowired
    private SysAnnouncementService sysAnnouncementService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("sys:announcement:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = sysAnnouncementService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{announcementId}")
    @RequiresPermissions("sys:announcement:info")
    public R info(@PathVariable("announcementId") Long announcementId){
        SysAnnouncementEntity sysAnnouncement = sysAnnouncementService.getById(announcementId);

        return R.ok().put("announcement", sysAnnouncement);
    }

    /**
     * 保存
     */
    @PutMapping("/save")
    @RequiresPermissions("sys:announcement:save")
    public R save(@RequestBody SysAnnouncementEntity sysAnnouncement){
        ValidatorUtils.validateEntity(sysAnnouncement);
        sysAnnouncementService.save(sysAnnouncement);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("sys:announcement:update")
    public R update(@RequestBody SysAnnouncementEntity sysAnnouncement){
        ValidatorUtils.validateEntity(sysAnnouncement);
        sysAnnouncementService.updateById(sysAnnouncement);
        
        return R.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @RequiresPermissions("sys:announcement:delete")
    public R delete(@RequestParam String announcementIds){
        Long[] ids = (Long[]) ConvertUtils.convert(announcementIds.split(","), Long.class);
        sysAnnouncementService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
