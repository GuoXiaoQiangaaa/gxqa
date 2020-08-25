package com.pwc.modules.filing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.modules.filing.entity.FilingCompanyEntity;
import com.pwc.modules.sys.entity.SysDeptEntity;

/**
 *
 */
public interface FilingCompanyService extends IService<FilingCompanyEntity> {

    FilingCompanyEntity getByDeptId(Long deptId);

    /**
     * 保存
     * @param companyEntity
     * @return
     */
    boolean saveCompany(FilingCompanyEntity companyEntity);

    /**
     * 修改
     * @param companyEntity
     * @return
     */
    boolean updateCompany(FilingCompanyEntity companyEntity);
}
