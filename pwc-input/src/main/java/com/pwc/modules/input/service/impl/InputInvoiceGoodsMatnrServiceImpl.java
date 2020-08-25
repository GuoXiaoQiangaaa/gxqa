package com.pwc.modules.input.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;
import com.pwc.modules.input.dao.InputInvoiceGoodsMatnrDao;
import com.pwc.modules.input.entity.InputInvoiceGoodsMatnr;
import com.pwc.modules.input.service.InputInvoiceGoodsMatnrService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("invoiceGoodsMatnrService")
public class InputInvoiceGoodsMatnrServiceImpl extends ServiceImpl<InputInvoiceGoodsMatnrDao, InputInvoiceGoodsMatnr> implements InputInvoiceGoodsMatnrService {

    @Override
    public void updateByThreebasic(InputInvoiceGoodsMatnr invoiceGoodsMatnr) {
        this.baseMapper.updateByThreebasic(invoiceGoodsMatnr);
    }

    @Override
    public PageUtils findList(Map<String, Object> params) {
        String goodsName = (String) params.get("goodsName");
        String maktx = (String) params.get("matnr");
        IPage<InputInvoiceGoodsMatnr> page = this.page(
                new Query<InputInvoiceGoodsMatnr>().getPage(params, null, true),
                new QueryWrapper<InputInvoiceGoodsMatnr>()
//                        .eq("used", "1")
                        .like(StringUtils.isNotBlank(goodsName), "goods_name", goodsName)
                        .like(StringUtils.isNotBlank(maktx), "maktx", maktx)

        );
        return new PageUtils(page);
    }



  /*  public List<InputInvoiceGoodsMatnr> findList(InputInvoiceGoodsMatnr invoiceGoodsMatnr) {
        return this.baseMapper.findList(invoiceGoodsMatnr);
    }*/

    @Override
    public InputInvoiceGoodsMatnr getById(InputInvoiceGoodsMatnr invoiceGoodsMatnr) {
        return this.baseMapper.getById(invoiceGoodsMatnr);
    }

    @Override
    public InputInvoiceGoodsMatnr getByThreeBasic(InputInvoiceGoodsMatnr invoiceGoodsMatnr) {
        return this.baseMapper.getByThreeBasic(invoiceGoodsMatnr);
    }

    @Override
    public void update(InputInvoiceGoodsMatnr invoiceGoodsMatnr) {
        this.baseMapper.update(invoiceGoodsMatnr);
    }



    @Override
    public void delete(InputInvoiceGoodsMatnr invoiceGoodsMatnr) {
        this.baseMapper.delete(invoiceGoodsMatnr);
    }

    @Override
    public void deleteByIds(Integer[] ids) {
        this.baseMapper.deleteByIds(ids);
    }

    @Override
    public List<InputInvoiceGoodsMatnr> getListByCondition(InputInvoiceGoodsMatnr invoiceGoodsMatnr) {
        return this.baseMapper.getListByCondition(invoiceGoodsMatnr);
    }

    @Override
    public InputInvoiceGoodsMatnr getOneByCondition(InputInvoiceGoodsMatnr invoiceGoodsMatnr) {
        return this.baseMapper.getOneByCondition(invoiceGoodsMatnr);
    }

    @Override
    public List<InputInvoiceGoodsMatnr> getListAll() {
        return this.baseMapper.getListAll();
    }

    /**
     * 全量删除
     * @param
     */
    @Override
    public void deleteAll(){
        this.baseMapper.deleteAll();
    }

    @Override
    public void insertAll(List<InputInvoiceGoodsMatnr> list){
        this.baseMapper.insertAll(list);
    }
}
