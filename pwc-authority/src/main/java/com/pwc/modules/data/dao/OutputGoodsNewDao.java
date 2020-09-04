package com.pwc.modules.data.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pwc.modules.data.entity.OutputGoodsNewEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品信息持久层
 *
 * @author fanpf
 * @date 2020/8/24
 */
@Mapper
public interface OutputGoodsNewDao extends BaseMapper<OutputGoodsNewEntity> {

    /**
     * 商品信息列表
     *
     * @param page 分页对象
     * @return 查询结果
     */
    List<OutputGoodsNewEntity> list(IPage<OutputGoodsNewEntity> page);

    /**
     * 关键字查询
     *
     * @param page 分页对象
     * @param keyWords 关键字
     * @return 查询结果
     */
    List<OutputGoodsNewEntity> keyWordsList(IPage<OutputGoodsNewEntity> page, @Param("keyWords")String keyWords);
	
}
