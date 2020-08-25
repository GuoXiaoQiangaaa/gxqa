package com.pwc.modules.input.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.input.entity.InputUnformatInvoiceEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author jojo
 * @email wangjiaojiao640801@163.com
 * @date 2019-12-29 17:15:57
 */
public interface InputUnformatInvoiceService extends
        IService<InputUnformatInvoiceEntity> {

    PageUtils queryPage(Map<String, Object> params);

    InputUnformatInvoiceEntity getById(Integer id);

    void removeByIds(Integer[] ids);

    PageUtils findListByIds(Map<String, Object> params, List<Integer> ids) ;
}



