package com.pwc.modules.sys.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;

import com.pwc.modules.sys.dao.SysTaxDao;
import com.pwc.modules.sys.entity.SysTaxEntity;
import com.pwc.modules.sys.service.SysTaxService;


/**
 * @author zk
 */
@Service("sysTaxService")
public class SysTaxServiceImpl extends ServiceImpl<SysTaxDao, SysTaxEntity> implements SysTaxService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SysTaxEntity> page = this.page(
                new Query<SysTaxEntity>().getPage(params),
                new QueryWrapper<SysTaxEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void setup(long deptId, List<Long> taxIds) {

    }
}
