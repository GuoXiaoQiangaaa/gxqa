package com.pwc.modules.input.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.modules.input.entity.InputUnformatInvoiceEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * 
 * @author jojo
 * @email wangjiaojiao640801@163.com
 * @date 2019-12-29 17:15:57
 */
@Mapper
public interface InputUnformatInvoiceDao extends BaseMapper<InputUnformatInvoiceEntity> {

        int save(InputUnformatInvoiceEntity unformatInvoiceEntity);

        InputUnformatInvoiceEntity getById(Integer id);
        void removeByIds(Integer[] ids);



    }
