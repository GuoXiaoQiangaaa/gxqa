package com.pwc.modules.input.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.input.entity.InputCompanyTaskDetail;


import java.util.Map;

public interface InputCompanyTaskDetailService extends IService<InputCompanyTaskDetail> {

    PageUtils getDetailList(Map<String, Object> params);
}
