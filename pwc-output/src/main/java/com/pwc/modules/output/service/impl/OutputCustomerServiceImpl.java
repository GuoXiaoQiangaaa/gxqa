package com.pwc.modules.output.service.impl;

import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;

import com.pwc.modules.output.dao.OutputCustomerDao;
import com.pwc.modules.output.entity.OutputCustomerEntity;
import com.pwc.modules.output.service.OutputCustomerService;


@Service("outputCustomerService")
public class OutputCustomerServiceImpl extends ServiceImpl<OutputCustomerDao, OutputCustomerEntity> implements OutputCustomerService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OutputCustomerEntity> page = this.page(
                new Query<OutputCustomerEntity>().getPage(params),
                new QueryWrapper<OutputCustomerEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<OutputCustomerEntity> getByDeptId(Long deptId, String customerName) {
        return this.list(new QueryWrapper<OutputCustomerEntity>().eq("dept_id", deptId).like(StrUtil.isNotBlank(customerName), "name", customerName));
    }
}
