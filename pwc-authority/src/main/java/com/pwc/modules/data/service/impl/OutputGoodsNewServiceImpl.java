package com.pwc.modules.data.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pwc.common.exception.RRException;
import com.pwc.common.utils.Constant;
import com.pwc.modules.sys.shiro.ShiroUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;

import com.pwc.modules.data.dao.OutputGoodsNewDao;
import com.pwc.modules.data.entity.OutputGoodsNewEntity;
import com.pwc.modules.data.service.OutputGoodsNewService;

import javax.annotation.Resource;

/**
 * 商品信息服务实现
 *
 * @author fanpf
 * @date 2020/8/24
 */
@Service("outputGoodsNewService")
public class OutputGoodsNewServiceImpl extends ServiceImpl<OutputGoodsNewDao, OutputGoodsNewEntity> implements OutputGoodsNewService {

    @Resource
    private OutputGoodsNewDao goodsNewDao;

    /**
     * 列表
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OutputGoodsNewEntity> page = new Query<OutputGoodsNewEntity>().getPage(params);
        List<OutputGoodsNewEntity> list = goodsNewDao.list(page);
        PageUtils pageUtils = new PageUtils(page);
        pageUtils.setList(list);
        return pageUtils;
    }

    /**
     * 新增
     */
    @Override
    public boolean save(OutputGoodsNewEntity outputGoods) {
        // 参数校验
        this.checkParams(outputGoods);

        outputGoods.setCreateBy(String.valueOf(ShiroUtils.getUserId()));
        outputGoods.setCreateTime(new Date());
        return super.save(outputGoods);
    }

    /**
     * 编辑
     */
    @Override
    public boolean updateById(OutputGoodsNewEntity outputGoods) {
        // 参数校验
        this.checkParams(outputGoods);

        outputGoods.setUpdateBy(String.valueOf(ShiroUtils.getUserId()));
        outputGoods.setUpdateTime(new Date());
        return super.updateById(outputGoods);
    }

    /**
     * 禁用/启用
     */
    @Override
    public void disableOrEnable(OutputGoodsNewEntity reqVo) {
        reqVo.setUpdateBy(String.valueOf(ShiroUtils.getUserId()));
        reqVo.setUpdateTime(new Date());
        super.updateById(reqVo);
    }

    /**
     * 关键字查询
     */
    @Override
    public PageUtils search(Map<String, Object> params) {
        String keyWords = (String) params.get("keyWords");

        if(StringUtils.isNotBlank(keyWords)){
            keyWords = keyWords.trim();
            keyWords = "%" + keyWords + "%";
            IPage<OutputGoodsNewEntity> page = new Query<OutputGoodsNewEntity>().getPage(params);
            List<OutputGoodsNewEntity> list = goodsNewDao.keyWordsList(page, keyWords);
            PageUtils pageUtils = new PageUtils(page);
            pageUtils.setList(list);
            return pageUtils;
        }else {
            return this.queryPage(params);
        }
    }

    /**
     * 参数校验
     */
    private void checkParams(OutputGoodsNewEntity outputGoods){
        if(StringUtils.isBlank(outputGoods.getGoodsNumber())){
            throw new RRException("商品编码不能为空");
        }
        if(StringUtils.isBlank(outputGoods.getGoodsName())){
            throw new RRException("商品名称不能为空");
        }
        if(StringUtils.isBlank(outputGoods.getTaxCategoryCode())){
            throw new RRException("税收分类编码不能为空");
        }
        if(StringUtils.isBlank(outputGoods.getTaxCategoryName())){
            throw new RRException("税收分类名称不能为空");
        }
        if(StringUtils.isBlank(outputGoods.getTaxRate())){
            throw new RRException("商品税率不能为空");
        }
    }
}
