package com.pwc.modules.filing.controller;

import com.pwc.common.utils.R;
import com.pwc.common.validator.ValidatorUtils;
import com.pwc.modules.filing.entity.FilingCompanyEntity;
import com.pwc.modules.filing.service.FilingCompanyService;
import com.pwc.modules.sys.entity.SysDeptEntity;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("filing/company")
public class FilingCompanyController {

    @Autowired
    private FilingCompanyService filingCompanyService;


    /**
     * 信息
     */
    @GetMapping("/info/{companyId}")
    public R info(@PathVariable("companyId") Long companyId){
        FilingCompanyEntity companyEntity = filingCompanyService.getById(companyId);

        return R.ok().put("filingCompany", companyEntity);
    }

    /**
     * 信息
     */
    @GetMapping("/get/{deptId}")
    public R getByDeptId(@PathVariable("deptId") Long deptId){
        FilingCompanyEntity companyEntity = filingCompanyService.getByDeptId(deptId);

        return R.ok().put("filingCompany", companyEntity);
    }

    /**
     * 保存
     */
    @PutMapping("/save")
    public R save(@RequestBody FilingCompanyEntity company) {
        ValidatorUtils.validateEntity(company);
        filingCompanyService.saveCompany(company);
        return R.ok().put("deptId", company.getDeptId());
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public R update(@RequestBody FilingCompanyEntity company){
        ValidatorUtils.validateEntity(company);
        filingCompanyService.updateCompany(company);

        return R.ok();
    }
}
