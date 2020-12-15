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
import com.pwc.modules.sys.entity.SysDeptEntity;
import com.pwc.modules.sys.service.SysDeptService;
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
    @Autowired
    private SysDeptService sysDeptService;

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
            String[] excelHead = {"Company Code", "Account", "Reference", "Document Number", "Document Type",
                    "Document Date", "Posting Date", "Amount in local currency", "Local Currency", "Amount in doc. curr.",
                    "Document currency", "User name", "Assignment", "Text", "Tax code", "Trading Partner", "Posting Key", "Year/month", "Document Header Text"};
            String[] excelHeadAlias = {"companyCode", "account", "reference", "documentNo", "documentType",
                    "docDate", "pstngDate", "amountInLocal", "lcurr", "amountInDoc", "curr",
                    "userName", "assignment", "text", "tx", "tradingPartner", "postingKey", "yearAndMonth", "headerText"};
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
                    duplicate.setUpdateBy(ShiroUtils.getUserId().intValue());
                    duplicate.setUpdateTime(new Date());
                    this.paraphraseParams(duplicate);
                    super.updateById(duplicate);
                } else {
                    entity.setCreateBy(ShiroUtils.getUserId().intValue());
                    entity.setCreateTime(new Date());
                    this.paraphraseParams(entity);
                    super.save(entity);
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
        String companyCode = ParamsMap.findMap(params, "companyCode");
        // 入账日期
        String pstngDate = ParamsMap.findMap(params, "pstngDate");
        // 凭证编码
        String documentNo = ParamsMap.findMap(params, "documentNo");
        // 匹配类型 1 发票 2 海关 3 红字通知单
        String matchType = ParamsMap.findMap(params, "matchType");
        // 科目
        String account = ParamsMap.findMap(params, "account");
        // 参考
        String reference = ParamsMap.findMap(params, "reference");
        // 分配
        String assignment = ParamsMap.findMap(params, "assignment");
        //摘要
        String headerText = ParamsMap.findMap(params, "headerText");
        // 匹配状态
        String[] match = null;
        if(params.containsKey("match") && params.get("match")!= null){
            match = (String[])params.get("match");
        }else{
            match = new String[]{"0","1","2"};
        }
        IPage<InputInvoiceSapEntity> page = this.page(
                new Query<InputInvoiceSapEntity>().getPage(params),
                new QueryWrapper<InputInvoiceSapEntity>()
                        .eq(StringUtils.isNotBlank(companyCode), "dept_id", companyCode)
                        .ge(StringUtils.isNotBlank(pstngDate), "pstng_date", StringUtils.isNotBlank(pstngDate) ? pstngDate.split(",")[0] : "")
                        .le(StringUtils.isNotBlank(pstngDate), "pstng_date", StringUtils.isNotBlank(pstngDate) ? pstngDate.split(",")[1] : "")
                        .eq(StringUtils.isNotBlank(documentNo), "document_no", documentNo)
                        .eq(StringUtils.isNotBlank(account), "account", account)
                        .eq(StringUtils.isNotBlank(matchType), "match_type", matchType)
                        .like(StringUtils.isNotBlank(reference), "reference", reference)
                        .eq(StringUtils.isNotBlank(assignment), "assignment", assignment)
                        .in("sap_match", match)
                        .like(StringUtils.isNotBlank(headerText), "header_text", headerText)
        );
        return new PageUtils(page);
    }

    @Override
    public boolean updateListByDeptId(Map<String, Object> params) {
        List<InputInvoiceSapEntity> sapEntitys = this.list(
                new QueryWrapper<InputInvoiceSapEntity>()
        );
        for (int i = 0;i < sapEntitys.size();i++){
            this.paraphraseParams(sapEntitys.get(i));
            super.updateById(sapEntitys.get(i));
        }
        return true;
    }

    @Override
    public int getListByShow() {
        return this.baseMapper.getListByShow();
    }


    @Override
    public InputInvoiceSapEntity getEntityByNo(String documentNo, String yearAndMonth, String deptCode) {
        InputInvoiceSapEntity sapEntity = this.getOne(
                new QueryWrapper<InputInvoiceSapEntity>()
                        .eq("document_no", documentNo)
                        .like("year_and_month", yearAndMonth)
                        .eq("company_code", deptCode)
        );
        return sapEntity;
    }

    @Override
    public List<InputInvoiceSapEntity> getEntityByDateAndStatus(String date, String status,String deptId) {
        List<InputInvoiceSapEntity> sapEntitys = this.list(
                new QueryWrapper<InputInvoiceSapEntity>()
                        .like("pstng_date", date)
                        .eq("match_type", status)
                        .eq("dept_id", deptId)
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
     * 进项税导入
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
            entity = inputRedInvoiceService.voluntaryEntry(entity);
            entity.setMatchType("3");
            SysDeptEntity deptEntity = sysDeptService.getByDeptCode("CBC");
            entity.setDeptId(String.valueOf(deptEntity.getDeptId()));
            //查询部门ID
        } else if (org.apache.commons.lang3.StringUtils.contains(accout, "165102")) {
            entity = inputInvoiceCustomsService.updateByEntry(entity);
            entity.setMatchType("2");
            //查询部门ID
            if (entity.getReference() != null) {
                if (entity.getReference().contains("IMPORT HQ") || entity.getReference().contains("Z04 IMPORT HQ")) {
                    SysDeptEntity deptEntity = sysDeptService.getByDeptCode("CBC");
                    entity.setDeptId(String.valueOf(deptEntity.getDeptId()));
                }else if(entity.getReference().contains("IMPORT GZ")){
                    SysDeptEntity deptEntity = sysDeptService.getByDeptCode("CBC-GZ");
                    entity.setDeptId(String.valueOf(deptEntity.getDeptId()));
                }
            }
        } else if (org.apache.commons.lang3.StringUtils.contains(accout, "165101")) {
            entity.setMatchType("1");
            entity = inputInvoiceService.voluntaryEntry(entity);
            //查询部门ID
            SysDeptEntity deptEntity = sysDeptService.getByDeptCode("CBC");
            entity.setDeptId(String.valueOf(deptEntity.getDeptId()));
        }else if (org.apache.commons.lang3.StringUtils.contains(accout, "165103")) {
            entity.setMatchType("1");
            if (entity.getText().contains("CBC-DL")) {
                SysDeptEntity deptEntity = sysDeptService.getByDeptCode("CBC-DL");
                entity.setDeptId(String.valueOf(deptEntity.getDeptId()));
            }else if(entity.getText().contains("CBC-SH")){
                SysDeptEntity deptEntity = sysDeptService.getByDeptCode("CBC-SH");
                entity.setDeptId(String.valueOf(deptEntity.getDeptId()));
            }else{
                SysDeptEntity deptEntity = sysDeptService.getByDeptCode("CBC");
                entity.setDeptId(String.valueOf(deptEntity.getDeptId()));
            }
        }
        return entity;
    }

}
