package com.pwc.modules.input.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.DateUtils;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;
import com.pwc.modules.input.dao.InputInvoiceLabelDao;
import com.pwc.modules.input.entity.InputInvoiceEntity;
import com.pwc.modules.input.entity.InputInvoiceLabelEntity;
import com.pwc.modules.input.entity.InputInvoiceMaterialEntity;
import com.pwc.modules.input.service.InputInvoiceLabelService;
import com.pwc.modules.input.service.InputInvoiceMaterialService;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.EAN;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Gxw
 * @date 2020/7/15 16:48
 */
@Service("inputInvoiceLabelService")
public class InputInvoiceLabelServiceImpl extends ServiceImpl<InputInvoiceLabelDao,InputInvoiceLabelEntity> implements InputInvoiceLabelService {
    @Autowired
    private InputInvoiceMaterialService invoiceMaterialService;

    /*
    * 查询列表
    * */
    @Override
    public List<InputInvoiceLabelEntity> findLabelList(Map<String,Object> params) {
        String inquireFalg =null;
        String id = null;
        String[] ids = null;
        String labelName = null;
        String subjectName = null;
        if(params.containsKey("inquireFalg")){
            inquireFalg = params.get("inquireFalg").toString();
        }
        if(params.containsKey("ids")){
            id =params.get("ids").toString();
            ids = StringUtils.isNotBlank(id) ? params.get("ids").toString().split(",") : null;
        }
        if(params.containsKey("labelName")){
            labelName = params.get("labelName").toString();
        }
        if(params.containsKey("subjectName")){
            subjectName = params.get("subjectName").toString();
        }
        return this.list(new QueryWrapper<InputInvoiceLabelEntity>()
                .in(StringUtils.isNotBlank(id),"id", ids)
                .eq(StringUtils.isNotBlank(inquireFalg),"inquire_falg", inquireFalg)
                .eq(StringUtils.isNotBlank(labelName),"label_name", labelName)
                .eq(StringUtils.isNotBlank(subjectName),"subject_name", subjectName)
                .eq("delete_falg", 1));
    }

    /*
    * 查询标签
    * */
    @Override
    public PageUtils findLabel(Map<String,Object> params){
        String numbering = null;
        String labelName = null;
        String labelAttribution = null;
        String rangeId = null;
        String createTimeBegin = null;
        String createTimeEnd = null;
        String subjectName = null;
        String inquireFalg = params.get("inquireFalg").toString();
        if(inquireFalg!=null) {
            if(params.containsKey("numbering")){
                 numbering = params.get("numbering").toString();
            }
            if(params.containsKey("labelName")){
                 labelName = params.get("labelName").toString();
            }
            if(params.containsKey("labelAttribution")){
                 labelAttribution = params.get("labelAttribution").toString();
            }
            if(params.containsKey("rangeId")){
                 rangeId = params.get("rangeId").toString();
            }
            if(params.containsKey("createTimeArray")){
                String createTimeArray = params.get("createTimeArray").toString();
                createTimeBegin = StringUtils.isNotBlank(createTimeArray)? createTimeArray.split(",")[0]+" 00:00:00":null;
                createTimeEnd = StringUtils.isNotBlank(createTimeArray)?createTimeArray.split(",")[1]+" 23:59:59":null;
            }
            if(params.containsKey("subjectName")){
                subjectName = params.get("subjectName").toString();
            }
            IPage<InputInvoiceLabelEntity> page = this.page(
                    new Query<InputInvoiceLabelEntity>().getPage(params,null,true),
                    new QueryWrapper<InputInvoiceLabelEntity>()
                    .like(StringUtils.isNotBlank(labelName),"label_name",labelName)
                    .eq(StringUtils.isNotBlank(labelAttribution),"label_attribution",labelAttribution)
                    .eq(StringUtils.isNotBlank(rangeId),"range_id",rangeId)
                    .eq(StringUtils.isNotBlank(numbering),"numbering",numbering)
                    .eq(StringUtils.isNotBlank(subjectName),"subject_name",subjectName)
                    .eq(StringUtils.isNotBlank(inquireFalg),"inquire_falg", inquireFalg)
//                    .apply(StringUtils.isNotBlank(createTimeArray),"())")
                    .apply(StringUtils.isNotBlank(createTimeBegin),"UNIX_TIMESTAMP(create_time)>=UNIX_TIMESTAMP({0})",createTimeBegin)
                    .apply(StringUtils.isNotBlank(createTimeEnd),"UNIX_TIMESTAMP(create_time)<=UNIX_TIMESTAMP({0})", createTimeEnd)
                    .eq("delete_falg", 1)
            );
            return new PageUtils(page);
        }
        return new PageUtils(null);

    }
    /*
    * 按照名称查询
    * */
    @Override
    public InputInvoiceLabelEntity findRule(String labelName){
        return this.getOne(new QueryWrapper<InputInvoiceLabelEntity>()
                .eq( "label_name",labelName)
                .eq("label_level",1)
        );
    }

    public Integer getLabel(InputInvoiceEntity invoiceEntity){
        Integer label  =null;
        List<Integer> idList = new ArrayList<>();
        idList.add(invoiceEntity.getId());
        List<InputInvoiceMaterialEntity> invoiceMaterialEntityList = invoiceMaterialService.getByInvoiceIds(idList);
        for (InputInvoiceMaterialEntity entity:invoiceMaterialEntityList) {
            InputInvoiceLabelEntity labelEntity = new InputInvoiceLabelEntity();
            if (entity.getSphSpmc()!=null){ // 科目不为空

            }else{
                 labelEntity = this.findRule(entity.getSphSpmc());
            }
            label = labelEntity.getLabelAttribution();
        }
        return label;
    }



}
