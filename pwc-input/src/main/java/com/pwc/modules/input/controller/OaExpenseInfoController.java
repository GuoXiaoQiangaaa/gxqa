package com.pwc.modules.input.controller;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;
import com.pwc.modules.input.entity.InputOaExpenseInfoEntity;
import com.pwc.modules.input.service.InputOaExpenseInfoService;
import com.pwc.modules.sys.shiro.ShiroUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;


/**
 * 
 *
 * @author jojo
 * @email wangjiaojiao640801@163.com
 * @date 2019-12-23 15:15:03
 */
@RestController
@RequestMapping("input/oaexpenseinfo")
public class OaExpenseInfoController {
    @Autowired
    private InputOaExpenseInfoService oaExpenseInfoService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("input:oaexpenseinfo:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = oaExpenseInfoService.queryPage(params);

        return R.ok().put("page", page);
    }

//    public R specialList() {
//
//    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("input:oaexpenseinfo:info")
    public R info(@PathVariable("id") Integer id){
		InputOaExpenseInfoEntity oaExpenseInfo = oaExpenseInfoService.getById(id);

        return R.ok().put("oaExpenseInfo", oaExpenseInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("input:oaexpenseinfo:save")
    public R save(@RequestBody InputOaExpenseInfoEntity oaExpenseInfo){
        oaExpenseInfo = new InputOaExpenseInfoEntity();
        oaExpenseInfo.setExpenseStatus(1);
        oaExpenseInfo.setExpenseNumber(IdUtil.simpleUUID());
        oaExpenseInfo.setApplyTime(DateUtil.format(DateUtil.date(), DatePattern.NORM_DATETIME_PATTERN));
        oaExpenseInfo.setApplyUser(ShiroUtils.getUserEntity().getUsername());
        oaExpenseInfo.setEndUser(ShiroUtils.getUserEntity().getUsername());
        oaExpenseInfo.setAmount(new BigDecimal("100.00"));
        oaExpenseInfo.setCompanyId(ShiroUtils.getUserEntity().getDeptId());
        oaExpenseInfoService.save(oaExpenseInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("input:oaexpenseinfo:update")
    public R update(@RequestBody InputOaExpenseInfoEntity oaExpenseInfo){
		    String applyTime = oaExpenseInfo.getApplyTime().replace("T"," ");
		    applyTime = applyTime.replace("Z","");
        oaExpenseInfo.setApplyTime(applyTime);

        oaExpenseInfoService.updateById(oaExpenseInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("input:oaexpenseinfo:delete")
    public R delete(@RequestBody Integer[] ids){
		    oaExpenseInfoService.removeByIds(ids);

        return R.ok();
    }

}
