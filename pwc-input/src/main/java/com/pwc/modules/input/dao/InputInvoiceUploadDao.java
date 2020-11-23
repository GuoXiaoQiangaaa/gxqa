package com.pwc.modules.input.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.modules.input.entity.InputInvoiceUploadEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: Gxw
 * @create: 2020-09-03 16:18
 **/
@Mapper
public interface InputInvoiceUploadDao extends BaseMapper<InputInvoiceUploadEntity>  {
    List<InputInvoiceUploadEntity> getListAndCreateName(@Param("list") List<InputInvoiceUploadEntity> list, @Param("createUserName") String createUserName);

}
