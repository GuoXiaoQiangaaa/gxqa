package com.pwc.modules.input.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.annotation.DataFilter;
import com.pwc.common.exception.RRException;
import com.pwc.common.utils.Constant;
import com.pwc.common.utils.InputConstant;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;
import com.pwc.modules.input.dao.InputInvoicePoDao;
import com.pwc.modules.input.entity.InputExportDetailEntity;
import com.pwc.modules.input.entity.InputInvoiceEntity;
import com.pwc.modules.input.entity.InputInvoicePoEntity;
import com.pwc.modules.input.entity.InputInvoiceUploadEntity;
import com.pwc.modules.input.service.InputInvoicePoService;
import com.pwc.modules.input.service.InputInvoiceService;
import com.pwc.modules.input.service.InputInvoiceUploadService;
import com.pwc.modules.sys.entity.SysDeptEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.NumberFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    @Autowired
    private InputInvoicePoService inputInvoicePoService;

    // OCR识别成功保存
    @Override
    @DataFilter(subDept = true, userId = "create_by")
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
        if(page.getTotal() > 0){
            for(InputInvoicePoEntity poEntity : page.getRecords()){
                if(poEntity.getCompanyName() == null){
                    if(poEntity.getInvoiceNumber() != null){
                        List<InputInvoiceEntity> invoiceEntities = inputInvoiceService
                                .list(new QueryWrapper<InputInvoiceEntity>()
                                        .eq("invoice_number", poEntity.getInvoiceNumber())
                                        // 是否退票 0:未退票; 1:已退票
                                        .eq("invoice_return", "0")
                                        // 是否失效 0:否; 1:是
                                        .eq("invoice_delete", "0")
                                        .notIn("invoice_status", status)
                                );
                        if (invoiceEntities.size() > 0) {
                            poEntity.setCompanyName(invoiceEntities.get(0).getInvoicePurchaserCompany());
                            updateById(poEntity);
                        }
                    }
                }
            }
        }
        return new PageUtils(page);
    }

    @Override
    public InputInvoicePoEntity uploadPo(InputInvoicePoEntity poEntity) {
        InputInvoicePoEntity poEn = inputInvoicePoService.getById(poEntity);
        //先清除原来的po关联信息
        List<InputInvoiceEntity> invoicePoEntities = inputInvoiceService
                .list(new QueryWrapper<InputInvoiceEntity>()
                        .eq("po_number", poEn.getPoNumber())
                );
        if (invoicePoEntities.size() > 0) {
            for (InputInvoiceEntity entity : invoicePoEntities) {
                entity.setPoNumber("");
                inputInvoiceService.updateById(entity);
            }
        }
        List<InputInvoicePoEntity> poEntitys = inputInvoicePoService.getByPoNumber(poEntity.getPoNumber());
        InputInvoiceUploadEntity uploadEntity = inputInvoiceUploadService.getById(poEntity.getUploadId());
        if (poEntitys.size() > 1) {
            //标记识别重复
            poEntity.setStatus(InputConstant.InvoicePo.REPEAT.getValue());
            uploadEntity.setStatus(InputConstant.UpdoldState.REPEAT.getValue());
        } else if (poEntity.getPoNumber() == null || poEntity.getPoNumber().equals("") || poEntity.getInvoiceNumber() == null || poEntity.getInvoiceNumber().equals("")) {
            //标记识别失败
            poEntity.setStatus(InputConstant.InvoicePo.FAIL.getValue());
            uploadEntity.setStatus(InputConstant.UpdoldState.RECOGNITION_FAILED.getValue());
        } else {
            boolean isPoNum = true;
            Pattern pattern = Pattern.compile("\\d{8}");
            Pattern pattern2 = Pattern.compile("\\d{10}");
            String[] poNumArray3 = poEntity.getPoNumber().split("/");
            //验证是否识别成功
            for (int i = 0; i < poNumArray3.length; i++) {
                Matcher isPoNum2 = pattern2.matcher(poNumArray3[i]);
                isPoNum = isPoNum2.matches();
                if (!isPoNum) {
                    i = poNumArray3.length;
                }
            }
            Matcher isInvoiceNum = pattern.matcher(poEntity.getInvoiceNumber());
            if (!isInvoiceNum.matches() || !isPoNum) {
                //正则校验不通过，置为识别失败
                poEntity.setStatus(InputConstant.InvoicePo.FAIL.getValue());
                uploadEntity.setStatus(InputConstant.UpdoldState.RECOGNITION_FAILED.getValue());
            } else {
                List<String> status = Arrays.asList("-2", "-7");
                List<InputInvoiceEntity> invoiceEntities = inputInvoiceService
                        .list(new QueryWrapper<InputInvoiceEntity>()
                                .eq("invoice_number", poEntity.getInvoiceNumber())
                                // 是否退票 0:未退票; 1:已退票
                                .eq("invoice_return", "0")
                                // 是否失效 0:否; 1:是
                                .eq("invoice_delete", "0")
                                .notIn("invoice_status", status)
                        );
                if (invoiceEntities.size() > 0) {
                    //标记已匹配
                    poEntity.setStatus(InputConstant.InvoicePo.MATCH.getValue());
                    poEntity.setCompanyName(invoiceEntities.get(0).getInvoicePurchaserCompany());
                    uploadEntity.setStatus(InputConstant.UpdoldState.PENDING_VERIFICATION.getValue());
                    for (InputInvoiceEntity entity : invoiceEntities) {
                        entity.setPoNumber(poEntity.getPoNumber());
                        inputInvoiceService.mainProcess(entity);
                    }
                } else {
                    //标记识别成功
                    poEntity.setStatus(InputConstant.InvoicePo.SUCCESS.getValue());
                    uploadEntity.setStatus(InputConstant.UpdoldState.PENDING_VERIFICATION.getValue());
                }
            }
        }
        if (poEntity.getPoId() != null) {
            updateById(poEntity);
        } else {
            save(poEntity);
        }
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
    public List<InputInvoicePoEntity> getByPoNumber(String poNumber) {
        return this.list(
                new QueryWrapper<InputInvoicePoEntity>()
                        .eq(StringUtils.isNotBlank(poNumber), "po_number", poNumber)
                        .eq("status", InputConstant.InvoicePo.SUCCESS.getValue())
        );

    }


}
