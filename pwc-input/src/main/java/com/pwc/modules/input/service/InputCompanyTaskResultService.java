package com.pwc.modules.input.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.modules.input.entity.InputCompanyEntity;
import com.pwc.modules.input.entity.InputCompanyTaskResult;
import com.pwc.modules.sys.entity.SysDeptEntity;

import java.util.List;

public interface InputCompanyTaskResultService extends IService<InputCompanyTaskResult> {
//    void save(InputCompanyTaskResult companyTaskResult);
    // 删除companyid下的result
    void delByCompanyId(Integer companyId);
    //
    List<InputCompanyTaskResult> getListByType(InputCompanyTaskResult companyTaskResult);

    /**
     * 查询companyId
     * @param deptId
     * @return
     */
    InputCompanyEntity findConpanyIdByDeptId(Long deptId);

}
