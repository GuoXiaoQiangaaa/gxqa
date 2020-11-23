package com.pwc.modules.input.controller;

import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;
import com.pwc.modules.input.entity.InputCompanyEntity;
import com.pwc.modules.input.service.InputCompanyService;
import com.pwc.modules.sys.entity.SysDeptEntity;
import com.pwc.modules.sys.service.SysDeptService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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

    /**
     * 统计情况列表
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = inputCompanyService.list(params);

        return R.ok().put("page", page);
    }

    /**
     * 统计详情列表
     */
    @GetMapping("/details/{companyId}")
    public R details(@PathVariable("companyId") Integer companyId, @RequestParam Map<String, Object> params){
        PageUtils page = inputCompanyService.details(companyId, params);

        return R.ok().put("page", page);
    }

    /**
     * 统计成功发票列表
     */
    @GetMapping("/invoices/{companyId}")
    public R invoices(@PathVariable("companyId") Integer companyId, @RequestParam Map<String, Object> params){
        PageUtils page = inputCompanyService.invoices(companyId, params);

        return R.ok().put("page", page);
    }

    /**
     * 统计差异发票列表
     */
    @GetMapping("/differenceInvoices/{companyId}")
    public R differenceInvoices(@PathVariable("companyId") Integer companyId, @RequestParam Map<String, Object> params){
        PageUtils page = inputCompanyService.differenceInvoices(companyId, params);

        return R.ok().put("page", page);
    }

    /**
     * 导出数据
     */
    @GetMapping("/exportData")
    public R exportData(@RequestParam Map<String, Object> params, HttpServletResponse response){

        return null;
    }
}
