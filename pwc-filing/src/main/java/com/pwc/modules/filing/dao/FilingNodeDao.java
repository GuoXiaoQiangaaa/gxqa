package com.pwc.modules.filing.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.pwc.modules.filing.entity.FilingNodeEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.modules.filing.entity.FilingRecordEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 申报节点设置
 * 
 * @author zk
 * @email 
 * @date 2019-11-08 15:11:47
 */
@Mapper
public interface FilingNodeDao extends BaseMapper<FilingNodeEntity> {

    /**
     * 分页查询
     * @param page
     * @param wrapper
     * @return
     */
    List<FilingNodeEntity> queryPage(IPage<FilingNodeEntity> page, @Param(Constants.WRAPPER) Wrapper<FilingNodeEntity> wrapper);
	
}
