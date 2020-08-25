package com.pwc.modules.input.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;
import com.pwc.modules.input.dao.InputInvoiceResponsibleDao;
import com.pwc.modules.input.entity.InputInvoiceResponsibleEntity;
import com.pwc.modules.input.service.InputInvoiceResponsibleService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("invoiceResponsibleService")
public class InputInvoiceResponsibleServiceImpl extends ServiceImpl<InputInvoiceResponsibleDao, InputInvoiceResponsibleEntity> implements InputInvoiceResponsibleService {
    @Override
    public PageUtils findList(Map<String, Object> params) {
        String key = (String) params.get("paramKey");
        String invoiceResponsible = (String) params.get("invoiceResponsible");
        String goodsCategory = (String) params.get("goodsCategory");
        IPage<InputInvoiceResponsibleEntity> page = this.page(
                new Query<InputInvoiceResponsibleEntity>().getPage(params, null, true),
                new QueryWrapper<InputInvoiceResponsibleEntity>()
                        .like(StringUtils.isNotBlank(key), "name", key)
//                        .or("(responsible_delete='0')")
                        .orderByDesc("id")
                        .like(StringUtils.isNotBlank(invoiceResponsible),"invoice_responsible",invoiceResponsible)
                        .like(StringUtils.isNotBlank(goodsCategory),"goods_category",goodsCategory)
        );
        return new PageUtils(page);
    }

    @Override
    public InputInvoiceResponsibleEntity get(InputInvoiceResponsibleEntity invoiceResponsibleEntity) {
        return this.baseMapper.get(invoiceResponsibleEntity);
    }

    @Override
    public List<InputInvoiceResponsibleEntity> getList(){
        return this.baseMapper.getList();
    }

    @Override
    public InputInvoiceResponsibleEntity getByResponsibleAndCategory(InputInvoiceResponsibleEntity invoiceResponsibleEntity) {
        return this.baseMapper.getByResponsibleAndCategory(invoiceResponsibleEntity);
    }

    @Override
    public void updateByResponsibleAndCategory(InputInvoiceResponsibleEntity invoiceResponsibleEntity) {
        this.baseMapper.updateByResponsibleAndCategory(invoiceResponsibleEntity);
    }

    public void responsibleDelete(Integer[] ids){
        this.baseMapper.responsibleDelete(ids);
    }

    @Override
    public InputInvoiceResponsibleEntity getOneByCondition(InputInvoiceResponsibleEntity invoiceResponsibleEntity) {
        return this.baseMapper.getOneByCondition(invoiceResponsibleEntity);
    }

    @Override
    public InputInvoiceResponsibleEntity getOneById(InputInvoiceResponsibleEntity invoiceResponsibleEntity) {
        return this.baseMapper.getOneById(invoiceResponsibleEntity);
    }

    @Override
    public void updateResponsibleDelete(InputInvoiceResponsibleEntity invoiceResponsibleEntity) {
        this.baseMapper.updateResponsibleDelete(invoiceResponsibleEntity);
    }
    @Override
    public void deleteAll(){
        this.baseMapper.deleteAll();
    }

    /**
     * 批量插入
     * @param list
     */
    @Override
    public void insertAll(List<InputInvoiceResponsibleEntity> list){
        this.baseMapper.insertAll(list);
    }
}
