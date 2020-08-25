package com.pwc.modules.input.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;
import com.pwc.modules.input.dao.InputSapConfEntityDao;
import com.pwc.modules.input.entity.InputSapConfEntity;
import com.pwc.modules.input.service.InputSapConfEntityService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("sapConfEntityService")
public class InputSapConfEntityServiceImpl extends ServiceImpl<InputSapConfEntityDao, InputSapConfEntity> implements InputSapConfEntityService {
    @Override
    public PageUtils findList(Map<String, Object> params) {
        String key = (String) params.get("paramKey");
        IPage<InputSapConfEntity> page = this.page(
                new Query<InputSapConfEntity>().getPage(params,null,true),
                new QueryWrapper<InputSapConfEntity>()
                        .like(StringUtils.isNotBlank(key), "name", key)
                        .orderByDesc("id")
        );
        return new PageUtils(page);
    }

    @Override
    public InputSapConfEntity getOneById(Integer id) {
        return this.baseMapper.getOneById(id);
    }


}
