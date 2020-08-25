package com.pwc.modules.input.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.input.entity.InputCompanyEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2018-12-13 09:31:08
 */
public interface InputCompanyService extends IService<InputCompanyEntity> {

    PageUtils queryPage(Map<String, Object> param);

    List<InputCompanyEntity> getList();

    void update(InputCompanyEntity companyEntity);

    /**
     * 根据企业id查询关联进项信息
     * @param deptId
     * @return
     */
    InputCompanyEntity getByDeptId(Long deptId);
}

