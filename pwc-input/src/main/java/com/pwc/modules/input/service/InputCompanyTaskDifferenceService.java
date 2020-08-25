package com.pwc.modules.input.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.input.entity.InputCompanyTaskDifference;

import java.util.Map;

public interface InputCompanyTaskDifferenceService extends IService<InputCompanyTaskDifference> {

    PageUtils getDifferenceList(Map<String, Object> params);
}
