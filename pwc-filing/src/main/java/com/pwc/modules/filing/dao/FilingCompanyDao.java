package com.pwc.modules.filing.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.modules.filing.entity.FilingCompanyEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FilingCompanyDao extends BaseMapper<FilingCompanyEntity> {
}
