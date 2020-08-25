package com.pwc.modules.input.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.input.entity.InputOaExpenseInfoEntity;

import java.util.Map;

/**
 * 
 *
 * @author jojo
 * @email wangjiaojiao640801@163.com
 * @date 2019-12-23 15:15:03
 */
public interface InputOaExpenseInfoService extends IService<InputOaExpenseInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    InputOaExpenseInfoEntity getById(Integer id);

    void removeByIds(Integer[] ids);

    int update(InputOaExpenseInfoEntity oaExpenseInfoEntity);






}

