package com.pwc.modules.input.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.modules.input.entity.InputCompanyEntity;
import com.pwc.modules.input.entity.InputCompanyTaskResult;
import com.pwc.modules.sys.entity.SysDeptEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InputCompanyTaskResultDao extends BaseMapper<InputCompanyTaskResult> {
    void save(InputCompanyTaskResult companyTaskResult);

    void delByCompanyId(@Param("companyId") Integer companyId);

    List<InputCompanyTaskResult> getListByType(InputCompanyTaskResult companyTaskResult);

    /**
     * 查询companyId
     * @param deptId
     * @return
     */
    InputCompanyEntity findConpanyIdByDeptId(@Param("deptId") Long deptId);
}
