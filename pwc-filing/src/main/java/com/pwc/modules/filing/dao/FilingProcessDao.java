package com.pwc.modules.filing.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.pwc.modules.filing.entity.FilingNodeEntity;
import com.pwc.modules.filing.entity.FilingProcessEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 申报流程设置
 * 
 * @author zk
 * @email 
 * @date 2019-11-08 15:11:47
 */
@Mapper
public interface FilingProcessDao extends BaseMapper<FilingProcessEntity> {

    /**
     * 分页查询
     * @param page
     * @param wrapper
     * @return
     */
    List<FilingProcessEntity> queryPage(IPage<FilingProcessEntity> page, @Param(Constants.WRAPPER) Wrapper<FilingProcessEntity> wrapper);
}
