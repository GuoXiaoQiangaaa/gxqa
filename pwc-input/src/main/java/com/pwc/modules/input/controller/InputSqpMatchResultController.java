package com.pwc.modules.input.controller;

import com.pwc.common.exception.RRException;
import com.pwc.common.utils.Exceptions;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.ParamsMap;
import com.pwc.common.utils.R;
import com.pwc.modules.input.entity.InputSqpMatchResultEntity;
import com.pwc.modules.input.service.InputSqpMatchResultService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/input/matchResult")
public class InputSqpMatchResultController {
    @Autowired
    private InputSqpMatchResultService inputSqpMatchResultService;

    @RequestMapping("/matchResultlist")
    public R list(@RequestParam Map<String, Object> params){
        //参数校验
        String yearAndMonth=ParamsMap.findMap(params, "yearAndMonth");
        String deptId=ParamsMap.findMap(params, "deptId");
        if(yearAndMonth != null  && deptId != null){
            //查询当前月份是否被锁定
            params.put("status","1");
            PageUtils page = inputSqpMatchResultService.queryPage(params);
            if(page.getTotalCount() > 0){
                return R.ok().put("page", page);
            }else{
                //实时查询账票匹配数据
                List<InputSqpMatchResultEntity> matchResultList = inputSqpMatchResultService.queryMatchCurTime(params);
                return R.ok().put("page", page);
            }
        }else{
            return null;
        }
    }
}
