package com.pwc.modules.input.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.modules.input.entity.InputSapConfEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface InputSapConfEntityDao extends BaseMapper<InputSapConfEntity> {
    InputSapConfEntity getOneById(@Param("id") Integer id);
}
