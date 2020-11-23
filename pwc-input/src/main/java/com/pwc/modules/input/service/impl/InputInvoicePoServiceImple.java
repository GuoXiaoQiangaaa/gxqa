package com.pwc.modules.input.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.annotation.DataFilter;
import com.pwc.common.utils.Constant;
import com.pwc.common.utils.InputConstant;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;
import com.pwc.modules.input.dao.InputInvoicePoDao;
import com.pwc.modules.input.entity.InputInvoiceEntity;
import com.pwc.modules.input.entity.InputInvoicePoEntity;
import com.pwc.modules.input.entity.InputInvoiceUploadEntity;
import com.pwc.modules.input.service.InputInvoicePoService;
import com.pwc.modules.input.service.InputInvoiceService;
import com.pwc.modules.input.service.InputInvoiceUploadService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @description: po票据
 * @author: Gxw
 * @create: 2020-09-02 18:44
 **/
@Service("inputInvoicePoService")
public class InputInvoicePoServiceImple extends ServiceImpl<InputInvoicePoDao, InputInvoicePoEntity> implements InputInvoicePoService {
    @Autowired
    private InputInvoiceUploadService inputInvoiceUploadService;
    @Autowired
    private InputInvoiceService inputInvoiceService;

    // OCR识别成功保存
    @Override
    @DataFilter(deptId = "dept_id", subDept = true, user = false)
    public PageUtils getPoEntity(Map<String, Object> params) {
        // 已匹配
        List<InputInvoicePoEntity> poEntitys = getListByNumber("");
        for (InputInvoicePoEntity poEntity : poEntitys) {
            List<InputInvoiceEntity> entitys = inputInvoiceService.findByInvoicNumber(poEntity.getInvoiceNumber());
            if (entitys.size() > 0) {
                poEntity.setStatus(InputConstant.InvoicePo.MATCH.getValue());
                poEntity.setCompanyName(entitys.get(0).getInvoicePurchaserCompany());
                updateById(poEntity);
            }
            for (InputInvoiceEntity entity : entitys) {
                entity.setPoNumber(poEntity.getPoNumber());
                inputInvoiceService.updateById(entity);
            }
        }
        String poNumber = (String) params.get("poNumber");
        String companyName = (String) params.get("companyName");
        String invoiceNumber = (String) params.get("invoiceNumber");
        String status = (String) params.get("status");
        IPage<InputInvoicePoEntity> page = this.page(
                new Query<InputInvoicePoEntity>().getPage(params, null, true),
                new QueryWrapper<InputInvoicePoEntity>()
                        .orderByDesc("update_time")
                        .like(StringUtils.isNotBlank(companyName), "company_name", companyName)
                        .like(StringUtils.isNotBlank(poNumber), "po_number", poNumber)
                        .like(StringUtils.isNotBlank(invoiceNumber), "invoice_number", invoiceNumber)
                        .eq(StringUtils.isNotBlank(status), "status", status)
                        .apply(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER))
        );
        return new PageUtils(page);
    }

    @Override
    public InputInvoicePoEntity uploadPo(InputInvoicePoEntity poEntity) {
        List<InputInvoicePoEntity> poEntitys = this.getListByNumber(poEntity.getInvoiceNumber());
        InputInvoiceUploadEntity uploadEntity = inputInvoiceUploadService.getById(poEntity.getUploadId());
        List<InputInvoiceEntity> invoiceEntities = inputInvoiceService
                .list(new QueryWrapper<InputInvoiceEntity>()
                        .eq("invoice_number", poEntity.getInvoiceNumber())
                );
        if (poEntity.getPoId() != null) {
            updateById(poEntity);
        } else {
            save(poEntity);
        }
        if (poEntitys.size() > 0) {
            poEntity.setStatus(InputConstant.InvoicePo.REPEAT.getValue());
            uploadEntity.setStatus(InputConstant.UpdoldState.REPEAT.getValue());
        } else {
            poEntity.setStatus(InputConstant.InvoicePo.SUCCESS.getValue());
            uploadEntity.setStatus(InputConstant.UpdoldState.PENDING_VERIFICATION.getValue());
            for (InputInvoiceEntity entity : invoiceEntities) {
                inputInvoiceService.mainProcess(entity);
            }
        }
//        if (poEntity.getPoId() != null) {
//            updateById(poEntity);
//        } else {
//            save(poEntity);
//        }
        uploadEntity.setUploadType(InputConstant.InvoiceStyle.PO.getValue());
        inputInvoiceUploadService.updateById(uploadEntity);
        return poEntity;
    }

    @Override
    public int getListByShow() {
        return this.count(
                new QueryWrapper<InputInvoicePoEntity>()
        );

    }

    @Override
    public List<InputInvoicePoEntity> getListByNumber(String invoiceNumber) {
        return this.list(
                new QueryWrapper<InputInvoicePoEntity>()
                        .eq(StringUtils.isNotBlank(invoiceNumber), "invoice_number", invoiceNumber)
                        .eq("status", InputConstant.InvoicePo.SUCCESS.getValue())
        );

    }

    @Override
    public InputInvoicePoEntity findByuploadId(String uploadId) {
        InputInvoicePoEntity poEntity = this.getOne(
                new QueryWrapper<InputInvoicePoEntity>()
                        .eq("upload_id", uploadId)
        );
        return poEntity;
    }

    @Override
    public InputInvoicePoEntity getByNumber(String invoiceNumber) {
        return this.getOne(
                new QueryWrapper<InputInvoicePoEntity>()
                        .eq(StringUtils.isNotBlank(invoiceNumber), "invoice_number", invoiceNumber)
                        .eq("status", InputConstant.InvoicePo.SUCCESS.getValue())
        );

    }
}
