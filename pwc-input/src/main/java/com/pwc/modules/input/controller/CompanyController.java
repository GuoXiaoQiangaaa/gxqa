package com.pwc.modules.input.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;
import com.pwc.modules.input.entity.InputCompanyEntity;
import com.pwc.modules.input.entity.InputInvoiceBatchEntity;
import com.pwc.modules.input.entity.InputInvoiceEntity;
import com.pwc.modules.input.service.InputCompanyService;
import com.pwc.modules.sys.entity.SysDeptEntity;
import com.pwc.modules.sys.service.SysDeptService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/input/company")
public class CompanyController {

    @Autowired
    InputCompanyService inputCompanyService;
    @Autowired
    SysDeptService sysDeptService;

    /**
     * 统计列表
     */
    @RequestMapping("/statisticsList")
    @RequiresPermissions("input:company:statisticsList")
    public R statisticsList(@RequestParam Map<String, Object> params) {
        PageUtils page = sysDeptService.queryPageForStatistics(params);
        for(Object object : page.getList()){
            SysDeptEntity sysDeptEntity = (SysDeptEntity)object;
            InputCompanyEntity company= inputCompanyService.getByDeptId(sysDeptEntity.getDeptId());
            if(company!=null ){
                if(StringUtils.isBlank(company.getStatus())){
                    company.setStatus("0");
                }
            }else{
                  company=new InputCompanyEntity();
                company.setStatus("0");
            }
            sysDeptEntity.setCompany(company);
        }
        return R.ok().put("list", page);
    }
}
