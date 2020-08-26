package com.pwc.modules.data.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;

import com.pwc.modules.data.dao.OutputCustomerNewDao;
import com.pwc.modules.data.entity.OutputCustomerNewEntity;
import com.pwc.modules.data.service.OutputCustomerNewService;

/**
 * 客户信息服务实现
 *
 * @author fanpf
 * @date 2020/8/24
 */
@Service("outputCustomerNewService")
public class OutputCustomerNewServiceImpl extends ServiceImpl<OutputCustomerNewDao, OutputCustomerNewEntity> implements OutputCustomerNewService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OutputCustomerNewEntity> page = this.page(
                new Query<OutputCustomerNewEntity>().getPage(params),
                new QueryWrapper<OutputCustomerNewEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 禁用/启用
     */
    @Override
    public void disableOrEnable(OutputCustomerNewEntity reqVo) {
        super.updateById(reqVo);
    }

    /**
     * 关键字查询
     */
    @Override
    public PageUtils search(Map<String, Object> params) {
        String keyWords = (String) params.get("keyWords");
        IPage<OutputCustomerNewEntity> page = this.page(
                new Query<OutputCustomerNewEntity>().getPage(params),
                new QueryWrapper<OutputCustomerNewEntity>()
        );
        return null;
    }
}
