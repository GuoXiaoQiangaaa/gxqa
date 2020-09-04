package com.pwc.modules.input.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.annotation.DataFilter;
import com.pwc.common.utils.Constant;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;
import com.pwc.modules.input.dao.InputInvoicePoDao;
import com.pwc.modules.input.entity.InputInvoicePoEntity;
import com.pwc.modules.input.service.InputInvoicePoService;
import net.sf.json.JSONArray;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @description: po票据
 * @author: Gxw
 * @create: 2020-09-02 18:44
 **/
@Service("inputInvoicePoService")
public class InputInvoicePoServiceImple extends ServiceImpl<InputInvoicePoDao, InputInvoicePoEntity> implements InputInvoicePoService {
    // OCR识别成功保存
    @Override
    public InputInvoicePoEntity saveInputInvoicePoEntity(JSONArray data,String image){
        String invoiceNumber = null;
        String poNumber = null;
        for(int k =0; k < 10;k++  ){
            if (data.getJSONObject(k).get("invoice_number") != null && !"".equals(data.getJSONObject(k).get("vat_type"))) {
                invoiceNumber = data.getJSONObject(k).get("invoice_number").toString();
            }
            if(data.getJSONObject(k).get("po_number") != null && !"".equals(data.getJSONObject(k).get("vat_type"))){
                poNumber = data.getJSONObject(k).get("po_number").toString();
            }
        }
        if(StringUtils.isNotEmpty(poNumber)){
            String[]  poNumbers = poNumber.split(",");
            for (String po:poNumbers) {
                InputInvoicePoEntity poEntity = new InputInvoicePoEntity();
                poEntity.setInvoiceNumber(invoiceNumber);
                poEntity.setPoNumber(po);
                save(poEntity);
            }
        }
        return  null;
    }
    @Override
    @DataFilter(subDept = true, user = false)
    public PageUtils getPoEntity(Map<String, Object> params) {
        IPage<InputInvoicePoEntity> page = this.page(
                new Query<InputInvoicePoEntity>().getPage(params, null, true),
                new QueryWrapper<InputInvoicePoEntity>()
                        .orderByAsc("update_time")
                        .apply(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER))
        );

        return new PageUtils(page);
    }

}
