package com.pwc.modules.filing.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.pwc.modules.filing.entity.FilingRecordEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 申报流程信息
 * 
 * @author zk
 * @email 
 * @date 2019-11-08 15:11:47
 */
@Mapper
public interface FilingRecordDao extends BaseMapper<FilingRecordEntity> {

    List<FilingRecordEntity> queryPage(IPage<FilingRecordEntity> page, @Param(Constants.WRAPPER) Wrapper<FilingRecordEntity> wrapper);
}
