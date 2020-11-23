package com.pwc.modules.input.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fapiao.neon.model.in.inspect.item.NormalItem;
import com.fapiao.neon.model.in.inspect.item.SpecialItem;
import com.pwc.modules.input.dao.InputInvoiceMaterialDao;
import com.pwc.modules.input.entity.InputInvoiceMaterialEntity;
import com.pwc.modules.input.service.InputInvoiceMaterialService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service("invoiceMaterialService")
public class InputInvoiceMaterialServiceImpl extends ServiceImpl<InputInvoiceMaterialDao, InputInvoiceMaterialEntity> implements InputInvoiceMaterialService {

    @Override
    public List<InputInvoiceMaterialEntity> getByInvoiceId(InputInvoiceMaterialEntity invoiceMaterialEntity){
        return this.baseMapper.getByInvoiceId(invoiceMaterialEntity);
    }

    @Override
    public List<InputInvoiceMaterialEntity> getByInvoiceIds(List<Integer> invoiceIds) {
        return this.baseMapper.getByInvoiceIds(invoiceIds);
    }

    @Override
    public void deleteInvoiceMaterialByInvoiceId(InputInvoiceMaterialEntity invoiceMaterialEntity) {
        this.baseMapper.deleteInvoiceMaterialByInvoiceId(invoiceMaterialEntity);
    }

    @Override
    public List<InputInvoiceMaterialEntity> getListByInvoiceId(InputInvoiceMaterialEntity invoiceMaterialEntity) {
        if( invoiceMaterialEntity.getInvoiceIds() ==null){
            return null;
        }

        return this.baseMapper.getListByInvoiceId(invoiceMaterialEntity);
    }

    @Override
    public void update(InputInvoiceMaterialEntity invoiceMaterialEntity) {
        this.baseMapper.update(invoiceMaterialEntity);
    }

    @Override
    public List<InputInvoiceMaterialEntity> getListByIds(InputInvoiceMaterialEntity invoiceMaterialEntity) {
        return this.baseMapper.getListByIds(invoiceMaterialEntity);
    }

    @Override
    public void updateByEnter(List<InputInvoiceMaterialEntity> invoiceMaterialEntityList) {
        this.baseMapper.updateByEnter(invoiceMaterialEntityList);
    }

    @Override
    public List<InputInvoiceMaterialEntity> getByIds(Integer[] ids) {
        return this.baseMapper.getByIds(ids);
    }

    @Override
    public void saveMaterialEntityByNormalItem(List<NormalItem> items,Integer invoiceId){
        for(NormalItem item:items){
            InputInvoiceMaterialEntity  Entity = new InputInvoiceMaterialEntity();
            Entity.setInvoiceId(invoiceId);
            // 序号
            Entity.setSphXh(item.getRowNo());
           // 商品名称
            Entity.setSphSpmc(item.getCommodityName());
            // 不含税金额 // amount
            if(StringUtils.isNotBlank(item.getAmount())){
                Entity.setSphJe(new BigDecimal(item.getAmount()));
            }
            //规格序号  // specificationModel
            Entity.setSphGgxh(item.getSpecificationModel());
            // 税收分类代码
            Entity.setTaxClassyCode(item.getTaxClassifyCode());
            //单位 // unit
            if(StringUtils.isNotBlank(item.getUnit())){
            Entity.setSphJldw(item.getUnit());
            }
            // 数量 // quantity
            if(StringUtils.isNotBlank(item.getQuantity())) {
                Entity.setSphSl(new BigDecimal(item.getQuantity()));
            }
            // 单价 // unitPrice
            if(StringUtils.isNotBlank(item.getUnitPrice())) {
                Entity.setSphDj(new BigDecimal(item.getUnitPrice()));
            }
            //税率 // taxRate
            Entity.setSphSlv(item.getTaxRate());
            //税额 // tax
            if(StringUtils.isNotBlank(item.getTax()) && (item.getTax()).matches("^(\\-|\\+)?\\d+(\\.\\d+)?$")) {
                Entity.setSphSe(new BigDecimal(item.getTax()));
            }
            // specialPolicySign  特殊政策标识
            // realTaxRate 实际税率
            // realTax 实际税额
            Entity.setSpecialPolicySign(item.getSpecialPolicySign());
            Entity.setRealTaxRate(item.getRealTaxRate());
            Entity.setRealTax(item.getRealTax());
            save(Entity);
        }

    }


    @Override
    public void saveMaterialEntityBySpecialItem(List<SpecialItem> items,Integer invoiceId){
            for(SpecialItem item:items){
                InputInvoiceMaterialEntity  Entity = new InputInvoiceMaterialEntity();
                Entity.setInvoiceId(invoiceId);
                // 序号
                Entity.setSphXh(item.getRowNo());
                // 商品名称
                Entity.setSphSpmc(item.getCommodityName());
                // 不含税金额 // amount
                if(StringUtils.isNotBlank(item.getAmount())){
                    Entity.setSphJe(new BigDecimal(item.getAmount()));
                }
                //规格序号  // specificationModel
                Entity.setSphGgxh(item.getSpecificationModel());
                // 税收分类代码
                Entity.setTaxClassyCode(item.getTaxClassifyCode());
                //单位 // unit
                if(StringUtils.isNotBlank(item.getUnit())){
                    Entity.setSphJldw(item.getUnit());
                }
                // 数量 // quantity
                if(StringUtils.isNotBlank(item.getQuantity())) {
                    Entity.setSphSl(new BigDecimal(item.getQuantity()));
                }
                // 单价 // unitPrice
                if(StringUtils.isNotBlank(item.getUnitPrice())) {
                    Entity.setSphDj(new BigDecimal(item.getUnitPrice()));
                }
                //税率 // taxRate
                Entity.setSphSlv(item.getTaxRate());
                //税额 // tax
                if(StringUtils.isNotBlank(item.getTax()) && item.getTax().matches("^(\\-|\\+)?\\d+(\\.\\d+)?$")) {
                    Entity.setSphSe(new BigDecimal(item.getTax()));
                }
                save(Entity);

        }

    }

    public static void main(String[] args) {
        String str = "免税";
        Pattern pattern = Pattern.compile("^(\\-|\\+)?\\d+(\\.\\d+)?$");
        Matcher isNum = pattern.matcher(str);
        if (isNum.matches()) {
            System.out.println("true");
        }else{
            System.out.println("fa");
        }
        if(str.matches("^(\\-|\\+)?\\d+(\\.\\d+)?$")){
            System.out.println("true");
        }else{
            System.out.println("fa");
        }
    }
}
