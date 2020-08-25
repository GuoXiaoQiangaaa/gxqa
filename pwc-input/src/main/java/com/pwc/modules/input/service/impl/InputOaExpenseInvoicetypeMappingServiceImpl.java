package com.pwc.modules.input.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;
import com.pwc.modules.input.dao.InputOaExpenseInvoicetypeMappingDao;
import com.pwc.modules.input.entity.InputOaExpenseInvoicetypeMappingEntity;
import com.pwc.modules.input.service.InputOaExpenseInvoicetypeMappingService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("oaExpenseInvoicetypeMappingService")
public class InputOaExpenseInvoicetypeMappingServiceImpl extends ServiceImpl<InputOaExpenseInvoicetypeMappingDao, InputOaExpenseInvoicetypeMappingEntity>
    implements
        InputOaExpenseInvoicetypeMappingService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<InputOaExpenseInvoicetypeMappingEntity> page = this.page(
            new Query<InputOaExpenseInvoicetypeMappingEntity>().getPage(params, null, true),
            new QueryWrapper<InputOaExpenseInvoicetypeMappingEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public InputOaExpenseInvoicetypeMappingEntity getById(Integer id){
        return this.baseMapper.getById(id);

    }
    @Override
    public void removeByIds(Integer[] ids){
        this.baseMapper.removeByIds(ids);
    }

    @Override
    public List<InputOaExpenseInvoicetypeMappingEntity> getOaExpenseInvoicetypeMappingEntitiesByExpenseNo(Integer expenseNo)
    {

            return this.baseMapper.getOaExpenseInvoicetypeMappingEntitiesByExpenseNo(expenseNo);
    }



}