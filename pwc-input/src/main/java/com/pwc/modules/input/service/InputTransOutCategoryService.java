package com.pwc.modules.input.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.input.entity.InputTansOutCategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * @author zk
 */
public interface InputTransOutCategoryService extends IService<InputTansOutCategoryEntity> {

    List<InputTansOutCategoryEntity> getList();


    void inputTransOutCategoryDelete(Integer[] ints);

    PageUtils findList(Map<String, Object> params);
}
