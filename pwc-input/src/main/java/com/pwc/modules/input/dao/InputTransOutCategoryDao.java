package com.pwc.modules.input.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.modules.input.entity.InputTansOutCategoryEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InputTransOutCategoryDao extends BaseMapper<InputTansOutCategoryEntity> {

    List<InputTansOutCategoryEntity> getList();
    int save(InputTansOutCategoryEntity inputTansOutCategoryEntity);


    void inputTransOutCategoryDelete(Integer[] ints);
}
