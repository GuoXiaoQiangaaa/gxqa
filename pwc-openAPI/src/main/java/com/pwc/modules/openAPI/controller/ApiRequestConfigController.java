package com.pwc.modules.openAPI.controller;

import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;
import com.pwc.modules.input.entity.InputTansOutCategoryEntity;
import com.pwc.modules.openAPI.entity.ApiRequestConfig;
import com.pwc.modules.openAPI.service.ApiRequestConfigService;
import com.pwc.modules.openAPI.service.ApiRequestCountService;
import com.pwc.modules.openAPI.service.ApiService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * 企业可请求接口配置
 */
@RestController
@RequestMapping("/apiRequestConfig")
public class ApiRequestConfigController {


    @Autowired
    private ApiRequestConfigService apiRequestConfigService;

    /**
     * 获取企业配置列表
     */
    @RequestMapping("/list")
    public R getRequestCount(@RequestParam Map<String, Object> params){
        PageUtils page = apiRequestConfigService.queryPage(params);
        return R.ok().put("list",page);
    }
    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody ApiRequestConfig apiRequestConfig) {
        try {
            apiRequestConfigService.save(apiRequestConfig);
            return R.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("添加失败");
        }
    }
    /**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public R delete(String ids) {
        String[] strings = ids.split(",");
        apiRequestConfigService.removeByIds(Arrays.asList(strings));
        return R.ok();
    }
}
