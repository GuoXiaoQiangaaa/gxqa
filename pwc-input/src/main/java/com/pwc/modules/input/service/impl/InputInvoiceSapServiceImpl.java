package com.pwc.modules.input.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.exception.RRException;
import com.pwc.common.utils.*;
import com.pwc.modules.input.dao.InputInvoiceSapDao;
import com.pwc.modules.input.entity.InputExportDetailEntity;
import com.pwc.modules.input.entity.InputInvoiceEntity;
import com.pwc.modules.input.entity.InputInvoiceSapEntity;
import com.pwc.modules.input.service.InputInvoiceCustomsService;
import com.pwc.modules.input.service.InputInvoiceSapService;
import com.pwc.modules.input.service.InputInvoiceService;
import com.pwc.modules.input.service.InputRedInvoiceService;
import com.pwc.modules.sys.shiro.ShiroUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;

/**
 * @description: Sap导入进项税明细
 * @author: myz
 * @create: 2020-12-04 12:07
 **/
@Service("inputInvoiceSapService")
public class InputInvoiceSapServiceImpl extends ServiceImpl<InputInvoiceSapDao, InputInvoiceSapEntity> implements InputInvoiceSapService {
    @Autowired
    public InputInvoiceService inputInvoiceService;
    @Autowired
    public InputInvoiceCustomsService inputInvoiceCustomsService;
    @Autowired
    public InputRedInvoiceService inputRedInvoiceService;

    @Override
    public Map<String, Object> getImportBySap(MultipartFile file) throws Exception {
        Map<String, Object> resMap = new HashMap<>();
        // 数据总量
        int total = 0;
        // 数据有误条数
        int fail = 0;
        // 记录excel中的重复数据
        List<String> repeatDataList = new ArrayList<>();
        // 记录数据有误的文件
        StringBuffer buffer = new StringBuffer();
        try {
            String filename = file.getOriginalFilename();
            ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
            String[] excelHead = {"Company code", "Account", "Reference", "Document Number", "Document Type",
                    "Document Date", "Posting Date", "Amount in local currency", "Local Currency", "Amount in doc. curr.",
                    "Document currency", "User name", "Assignment", "Text", "Tax code", "Trading Partner", "Posting Key", "Year/month", "Document Header Text"};
            String[] excelHeadAlias = {"companyCode", "account", "reference", "documentNo", "type",
                    "docDate", "pstngDate", "amountLocal", "currencyLocal", "amount", "currency",
                    "userName", "assignment", "text", "taxRate", "tradingPartner", "postingKey", "yearMonth", "headerText"};
            for (int i = 0; i < excelHead.length; i++) {
                reader.addHeaderAlias(excelHead[i], excelHeadAlias[i]);
            }
            List<InputInvoiceSapEntity> dataList = reader.read(0, 1, InputInvoiceSapEntity.class);

            if (CollectionUtils.isEmpty(dataList)) {
                log.error("上传的{}Excel为空,请重新上传");
                throw new RRException("上传的Excel为空,请重新上传");
            }
            total += dataList.size();
            int count = 1;
            for (InputInvoiceSapEntity entity : dataList) {
                count++;
                // 去除Excel中重复数据
                String repeatData = entity.getCompanyCode() + entity.getDocumentNo() + entity.getPstngDate();
                if (CollectionUtil.contains(repeatDataList, repeatData)) {
                    fail += 1;
                    if (!org.apache.commons.lang3.StringUtils.contains(buffer.toString(), filename)) {
                        buffer.append("文件" + filename + "的错误行号为:");
                    }
                    buffer.append(count + ",");
                    continue;
                }
                repeatDataList.add(repeatData);

                // 数据库验重
                InputInvoiceSapEntity duplicate = super.getOne(
                        new QueryWrapper<InputInvoiceSapEntity>()
                                .eq("company_code", entity.getCompanyCode())
                                .eq("document_no", entity.getDocumentNo())
                                .eq("pstng_date", entity.getPstngDate())
                );
                if (null != duplicate) {
                    duplicate.setAccount(entity.getAccount());
                    duplicate.setReference(entity.getReference());
                    duplicate.setDocDate(entity.getDocDate());
                    duplicate.setUserName(entity.getUserName());
                    duplicate.setAssignment(entity.getAssignment());
                    duplicate.setText(entity.getText());
                    duplicate.setTradingPartner(entity.getTradingPartner());
                    duplicate.setUpdateBy(String.valueOf(ShiroUtils.getUserId()));
                    duplicate.setUpdateTime(new Date());
                    this.paraphraseParams(duplicate);
                    super.updateById(duplicate);
                } else {
                    entity.setCreateBy(String.valueOf(ShiroUtils.getUserId()));
                    entity.setCreateTime(new Date());
                    this.paraphraseParams(entity);
                    super.save(duplicate);
                }
            }
            if (buffer.toString().endsWith(",")) {
                buffer.deleteCharAt(buffer.lastIndexOf(",")).append(";");
            }

            resMap.put("total", total);
            resMap.put("fail", fail);
            resMap.put("failDetail", buffer.toString());
            return resMap;
        } catch (RRException e) {
            throw e;
        } catch (Exception e) {
            log.error("Sap导入进项税明细出错: {}", e);
            throw new RRException("Sap导入进项税明细出现异常");
        }
    }

    /**
     * 查询列表
     */
    @Override
    public PageUtils getListBySap(Map<String, Object> params) {
        // 公司代码
        String companyCode = (String) params.get("companyCode");
        // 入账日期
        String pstngDate = (String) params.get("pstngDate");
        // 凭证编码
        String documentNo = (String) params.get("documentNo");
        // 科目
        String account = (String) params.get("account");
        // 参考
        String reference = (String) params.get("reference");
        // 分配
        String assignment = (String) params.get("assignment");
        //摘要
        String headerText = (String) params.get("headerText");
        // 匹配状态
        String match = ParamsMap.findMap(params, "match");
        IPage<InputInvoiceSapEntity> page = this.page(
                new Query<InputInvoiceSapEntity>().getPage(params, null, true),
                new QueryWrapper<InputInvoiceSapEntity>()
                        .eq(StringUtils.isNotBlank(companyCode), "company_code", companyCode)
                        .ge(StringUtils.isNotBlank(pstngDate), "pstng_date", StringUtils.isNotBlank(pstngDate) ? pstngDate.split(",")[0] : "")
                        .le(StringUtils.isNotBlank(pstngDate), "pstng_date", StringUtils.isNotBlank(pstngDate) ? pstngDate.split(",")[1] : "")
                        .eq(StringUtils.isNotBlank(documentNo), "document_no", documentNo)
                        .eq(StringUtils.isNotBlank(account), "account", account)
                        .like(StringUtils.isNotBlank(reference), "reference", reference)
                        .eq(StringUtils.isNotBlank(assignment), "assignment", assignment)
                        .in(StringUtils.isNotBlank(match), "sap_match", (String[]) params.get("match"))
                        .like(StringUtils.isNotBlank(headerText), "header_text", headerText)
        );
        return new PageUtils(page);

    }


    @Override
    public InputInvoiceSapEntity getEntityByNo(String documentNo) {
        InputInvoiceSapEntity sapEntity = this.getOne(
                new QueryWrapper<InputInvoiceSapEntity>()
                        .eq("document_no", documentNo)
        );
        return sapEntity;
    }

    @Override
    public List<InputInvoiceSapEntity> getEntityByDateAndStatus(String date, String status) {
        List<InputInvoiceSapEntity> sapEntitys = this.list(
                new QueryWrapper<InputInvoiceSapEntity>()
                        .like("pstng_date", date)
                        .eq("match_type", status)
        );
        return sapEntitys;
    }

    /**
     * 校验Excel中参数
     */
    private int checkExcel(InputInvoiceSapEntity entity) {
        // 非空校验
        if (org.apache.commons.lang3.StringUtils.isBlank(entity.getCompanyCode()) || org.apache.commons.lang3.StringUtils.isBlank(entity.getAccount()) ||
                org.apache.commons.lang3.StringUtils.isBlank(entity.getReference()) || org.apache.commons.lang3.StringUtils.isBlank(entity.getDocumentNo()) ||
                org.apache.commons.lang3.StringUtils.isBlank(entity.getDocumentType()) || org.apache.commons.lang3.StringUtils.isBlank(entity.getDocDate()) ||
                org.apache.commons.lang3.StringUtils.isBlank(entity.getPstngDate()) || null == entity.getAmountInLocal() ||
                org.apache.commons.lang3.StringUtils.isBlank(entity.getCurr()) || null == entity.getAmountInLocal() ||
                org.apache.commons.lang3.StringUtils.isBlank(entity.getText()) || org.apache.commons.lang3.StringUtils.isBlank(entity.getTx())) {
            return 1;
        }
        return 0;
    }

    /**
     * 枚举值转义
     */
    private InputInvoiceSapEntity paraphraseParams(InputInvoiceSapEntity entity) {
        String type = entity.getDocumentType();
        String currencyLocal = entity.getLcurr();
        String currency = entity.getCurr();
        String accout = entity.getAccount();
        String text = entity.getText();
        BigDecimal amountInDoc = entity.getAmountInDoc();
        /*// 对类型转义
        entity.setDocumentType("0");
        // 对当地币种转义
        if("CNY".equalsIgnoreCase(currencyLocal)){
            entity.setLcurr("0");
        }
        // 对币种转义
        if("CNY".equalsIgnoreCase(currency)){
            entity.setCurr("0");
        }*/
        // 对类型转义 1:发票; 2:海关通知单; 3:红字通知单
        if (org.apache.commons.lang3.StringUtils.contains(accout, "165101") && amountInDoc.compareTo(BigDecimal.ZERO) < 0) {
            inputRedInvoiceService.voluntaryEntry(entity);
            entity.setMatchType("3");
        } else if (org.apache.commons.lang3.StringUtils.contains(accout, "165102")) {
            inputInvoiceCustomsService.updateByEntry(entity);
            entity.setMatchType("2");
        } else if (org.apache.commons.lang3.StringUtils.contains(accout, "165101")) {
            entity.setMatchType("1");
            inputInvoiceService.voluntaryEntry(entity);
        }
        return entity;
    }

}
