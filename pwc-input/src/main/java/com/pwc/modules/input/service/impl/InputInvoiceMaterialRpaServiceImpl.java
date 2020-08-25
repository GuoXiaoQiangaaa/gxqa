package com.pwc.modules.input.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;
import com.pwc.modules.input.dao.InputInvoiceMaterialRpaDao;
import com.pwc.modules.input.entity.InputInvoiceMaterial;
import com.pwc.modules.input.entity.InputInvoiceMaterialRpaEntity;
import com.pwc.modules.input.service.InputInvoiceMaterialRpaService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@Service("invoiceMaterialRpaService")
public class InputInvoiceMaterialRpaServiceImpl extends ServiceImpl<InputInvoiceMaterialRpaDao, InputInvoiceMaterialRpaEntity> implements InputInvoiceMaterialRpaService {
    @Resource
    InputInvoiceMaterialRpaDao dao;

    @Override
    public PageUtils findList(Map<String, Object> params) {
        String key = (String) params.get("paramKey");
        IPage<InputInvoiceMaterialRpaEntity> page = this.page(
                new Query<InputInvoiceMaterialRpaEntity>().getPage(params,null,true),
                new QueryWrapper<InputInvoiceMaterialRpaEntity>()
                        .like(StringUtils.isNotBlank(key), "name", key)
        );
        return new PageUtils(page);
    }

    @Override
    public List<InputInvoiceMaterial> getListByStatus() {
        return dao.getListByStatus();
    }

    @Override
    public InputInvoiceMaterial get(int id){
        return dao.get(id);
    }

    /**
     * 更新状态为0
     * @param list
     */
    @Override
    public void updateByStatus(List<InputInvoiceMaterial> list){
        dao.updateByStatus(list);
    }

    @Override
    public InputInvoiceMaterial getOneByBatchId(InputInvoiceMaterial invoiceMaterial) {
        return this.baseMapper.getOneByBatchId(invoiceMaterial);
    }

    @Override
    public int insert(InputInvoiceMaterial invoiceMaterial) {
        return this.baseMapper.insert(invoiceMaterial);
    }

    @Override
    public void deleteByBatchId(InputInvoiceMaterial invoiceMaterial) {
        this.baseMapper.deleteByBatchId(invoiceMaterial);
    }


}
