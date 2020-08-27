package com.pwc.modules.data.service.impl;

import com.pwc.modules.sys.shiro.ShiroUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;

import com.pwc.modules.data.dao.OutputItemListDao;
import com.pwc.modules.data.entity.OutputItemListEntity;
import com.pwc.modules.data.service.OutputItemListService;

/**
 * 科目清单服务实现
 *
 * @author fanpf
 * @date 2020/8/27
 */
@Service("outputItemListService")
public class OutputItemListServiceImpl extends ServiceImpl<OutputItemListDao, OutputItemListEntity> implements OutputItemListService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OutputItemListEntity> page = this.page(
                new Query<OutputItemListEntity>().getPage(params),
                new QueryWrapper<OutputItemListEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 禁用/启用
     */
    @Override
    public void disableOrEnable(OutputItemListEntity reqVo) {
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

        IPage<OutputItemListEntity> page;
        if(StringUtils.isNotBlank(keyWords)){
            keyWords = keyWords.trim();
            page = this.page(
                    new Query<OutputItemListEntity>().getPage(params),
                    new QueryWrapper<OutputItemListEntity>()
                            .like("item_code", keyWords).or()
                            .like("item_type", keyWords).or()
                            .like("description", keyWords)
            );
        }else {
            page = this.page(
                    new Query<OutputItemListEntity>().getPage(params),
                    new QueryWrapper<OutputItemListEntity>()
            );
        }

        return new PageUtils(page);
    }
}
