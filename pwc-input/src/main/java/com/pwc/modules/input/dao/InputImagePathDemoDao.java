package com.pwc.modules.input.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.modules.input.entity.InputImagePathDemo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InputImagePathDemoDao extends BaseMapper<InputImagePathDemo> {
    void saveDemo(InputImagePathDemo imagePathDemo);
}
