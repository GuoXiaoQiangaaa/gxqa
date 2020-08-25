package com.pwc.modules.input.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.input.entity.InputSapConfEntity;

import java.util.Map;

public interface InputSapConfEntityService extends IService<InputSapConfEntity> {
    PageUtils findList(Map<String, Object> params);

    InputSapConfEntity getOneById(Integer id);
}
