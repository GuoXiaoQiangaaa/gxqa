package com.pwc.modules.input.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.annotation.DataFilter;
import com.pwc.modules.input.dao.InputInvoiceLabelDao;
import com.pwc.modules.input.dao.InputInvoiceLabelRelatedDao;
import com.pwc.modules.input.entity.InputInvoiceEntity;
import com.pwc.modules.input.entity.InputInvoiceLabelEntity;
import com.pwc.modules.input.entity.InputInvoiceLabelRelatedEntity;
import com.pwc.modules.input.entity.InputInvoiceMaterialEntity;
import com.pwc.modules.input.service.InputInvoiceLabelRelatedService;
import com.pwc.modules.input.service.InputInvoiceLabelService;
import com.pwc.modules.input.service.InputInvoiceMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Gxw
 * @date 2020/7/22 13:33
 */
@Service("inputInvoiceLabelRelatedService")
public class InputInvoiceLabelRelatedServiceImpl extends ServiceImpl<InputInvoiceLabelRelatedDao, InputInvoiceLabelRelatedEntity> implements InputInvoiceLabelRelatedService {
    @Autowired
    private InputInvoiceLabelService inputInvoiceLabelService;
    @Autowired
    private InputInvoiceMaterialService invoiceMaterialService;
    @Override
    public void  saveLable(InputInvoiceEntity invoiceEntity){
        Integer labelid =null;
        String labelName = null;
        Integer subjectid= null;
        String subjectName = null;
        List<Integer> idList = new ArrayList<>();
        idList.add(invoiceEntity.getId());
        List<InputInvoiceMaterialEntity> invoiceMaterialEntityList = invoiceMaterialService.getByInvoiceIds(idList);
        for (InputInvoiceMaterialEntity entity:invoiceMaterialEntityList) {
            InputInvoiceLabelEntity labelEntity = new InputInvoiceLabelEntity();
            if (entity.getSphSpmc()!=null){ // 科目不为空
                // 转出科目
                subjectid=labelEntity.getId();
                subjectName= labelEntity.getSubjectName();

            }else{
                labelEntity = inputInvoiceLabelService.findRule(entity.getSphSpmc());
                labelid = labelEntity.getLabelAttribution();
                labelName = labelEntity.getAttributionName();
            }

        }
        InputInvoiceLabelRelatedEntity relatedEntity = this.findByInvoiceId(invoiceEntity.getId());
        if(relatedEntity!=null){
            relatedEntity.setUpdateTime(new Date());
            relatedEntity.setLevelId(labelid);
            relatedEntity.setSubjectId(subjectid);
            relatedEntity.setCompanyId(invoiceEntity.getCompanyId());
            this.updateById(relatedEntity);
        }else{
            relatedEntity.setInvoiceId(invoiceEntity.getId());
            relatedEntity.setLevelId(labelid);
            relatedEntity.setSubjectId(subjectid);
            relatedEntity.setCompanyId(invoiceEntity.getCompanyId());
            relatedEntity.setUpdateTime(new Date());
            this.save(relatedEntity);
        }
    }
    @Override
    @DataFilter(deptId = "company_id",subDept = true,user = false)
    public InputInvoiceLabelRelatedEntity findByInvoiceId(Integer id ){
        return this.getOne(new QueryWrapper<InputInvoiceLabelRelatedEntity>()
                .eq("invoice_id", id)
                .isNotNull("subject_id")
        );
    }

    @Override
    public List<InputInvoiceEntity> findLabelByInvoiceId(List<InputInvoiceEntity> invoiceEntities) {
        List list = new ArrayList();
        for(InputInvoiceEntity entity:invoiceEntities){
            InputInvoiceLabelRelatedEntity relatedEntity= this.findByInvoiceId(entity.getId());
            if(relatedEntity!=null){
                InputInvoiceLabelEntity  labelEntity =inputInvoiceLabelService.getById(relatedEntity.getSubjectId());
                entity.setObjectName(labelEntity.getLabelName());
                entity.setOutRatio(relatedEntity.getOutRatio());
                entity.setOutFlag((relatedEntity.getOutRatio()).equals("100")?1:0);
                list.add(entity);
            }

        }
        return list;
    }
    @Override
    public List<InputInvoiceLabelRelatedEntity> findByDate(String date){
        return this.list(new QueryWrapper<InputInvoiceLabelRelatedEntity>()
                .likeRight("create_time", date)
                .isNotNull("level_id")
        );
    }
}
