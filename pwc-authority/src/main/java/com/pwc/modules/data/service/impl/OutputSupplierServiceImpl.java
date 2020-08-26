package com.pwc.modules.data.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;

import com.pwc.modules.data.dao.OutputSupplierDao;
import com.pwc.modules.data.entity.OutputSupplierEntity;
import com.pwc.modules.data.service.OutputSupplierService;

/**
 * 供应商信息服务实现
 *
 * @author fanpf
 * @date 2020/8/24
 */
@Service("outputSupplierService")
public class OutputSupplierServiceImpl extends ServiceImpl<OutputSupplierDao, OutputSupplierEntity> implements OutputSupplierService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OutputSupplierEntity> page = this.page(
                new Query<OutputSupplierEntity>().getPage(params),
                new QueryWrapper<OutputSupplierEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 禁用/启用
     */
    @Override
    public void disableOrEnable(OutputSupplierEntity reqVo) {
        super.updateById(reqVo);
    }

}
