package com.pwc.modules.input.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.modules.input.dao.InputCompanyTaskResultDao;
import com.pwc.modules.input.entity.InputCompanyEntity;
import com.pwc.modules.input.entity.InputCompanyTaskResult;
import com.pwc.modules.input.service.InputCompanyTaskResultService;
import com.pwc.modules.sys.entity.SysDeptEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("companyTaskResultService")
public class InputInputCompanyTaskResultServiceImpl extends ServiceImpl<InputCompanyTaskResultDao, InputCompanyTaskResult> implements InputCompanyTaskResultService {

    @Override
    public void delByCompanyId(Integer companyId) {
        this.baseMapper.delByCompanyId(companyId);
    }

    @Override
    public List<InputCompanyTaskResult> getListByType(InputCompanyTaskResult companyTaskResult) {
        return this.baseMapper.getListByType(companyTaskResult);
    }

    /**
     * 查询companyId
     * @param deptId
     * @return
     */
    @Override
    public InputCompanyEntity findConpanyIdByDeptId(Long deptId) {
        return this.baseMapper.findConpanyIdByDeptId(deptId);
    }


}
