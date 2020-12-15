package com.pwc.modules.input.service.impl;

import com.pwc.common.utils.*;
import com.pwc.modules.input.dao.InputSqpMatchResultDao;
import com.pwc.modules.input.entity.*;
import com.pwc.modules.input.entity.vo.InvoiceCustomsDifferenceMatch;
import com.pwc.modules.input.entity.vo.InvoiceDifferenceMatch;
import com.pwc.modules.input.entity.vo.MatchResultVo;
import com.pwc.modules.input.entity.vo.RedInvoiceDifferenceMatch;
import com.pwc.modules.input.service.*;
import com.pwc.modules.sys.entity.SysDeptEntity;
import com.pwc.modules.sys.service.SysDeptService;
import com.pwc.modules.sys.shiro.ShiroUtils;
import com.sun.org.apache.regexp.internal.RE;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


@Service("inputSqpMatchResultService")
public class InputSapMatchResultServiceImpl extends ServiceImpl<InputSqpMatchResultDao, InputSapMatchResultEntity> implements InputSqpMatchResultService {

    @Autowired
    private InputInvoiceSapService inputInvoiceSapService;
    @Autowired
    private InputInvoiceCustomsService inputInvoiceCustomsService;
    @Autowired
    private InputRedInvoiceService inputRedInvoiceService;
    @Autowired
    private InputInvoiceService inputInvoiceService;
    @Autowired
    private InputSqpMatchResultDao inputSqpMatchResultDao;
    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private InputInvoiceService invoiceService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        String sort = ParamsMap.findMap(params, "sort");
        String deptId = ParamsMap.findMap(params, "deptId");
        String status = ParamsMap.findMap(params, "status");
        String yearAndMonth = ParamsMap.findMap(params, "yearAndMonth");

        IPage<InputSapMatchResultEntity> page = this.page(
                new Query<InputSapMatchResultEntity>().getPage(params),
                new QueryWrapper<InputSapMatchResultEntity>()
                        .eq(StringUtils.isNotBlank(sort), "sort", sort)
                        .eq(StringUtils.isNotBlank(deptId), "dept_id", deptId)
                        .eq(StringUtils.isNotBlank(status), "status", status)
                        .eq(StringUtils.isNotBlank(yearAndMonth), "year_and_month", yearAndMonth)
                        .orderByDesc("create_time") //根据创建排序
        );
        return new PageUtils(page);
    }

    @Override
    public PageUtils getMonthCredBeforeResult(Map<String, Object> params) {
        String yearAndMonth=ParamsMap.findMap(params, "yearAndMonth");
        String newYearAndMonth = yearAndMonth.substring(0,7) + "-01";
        String deptId=ParamsMap.findMap(params, "deptId");
        String invoiceCode=ParamsMap.findMap(params, "invoiceCode");
        String invoiceNumber=ParamsMap.findMap(params, "invoiceNumber");
        String invoiceClass=ParamsMap.findMap(params, "invoiceClass");
        String entrySuccessCode=ParamsMap.findMap(params, "entrySuccessCode");
        SysDeptEntity sysDeptEntity = sysDeptService.getById(deptId);
        String resultType=ParamsMap.findMap(params, "resultType");
        if(resultType.equals("1")){
            IPage<InputInvoiceEntity> page = invoiceService.page(
                    new Query<InputInvoiceEntity>().getPage(params),
                    new QueryWrapper<InputInvoiceEntity>()
                            .eq(StringUtils.isNotBlank(sysDeptEntity.getTaxCode()), "invoice_purchaser_paragraph", sysDeptEntity.getTaxCode())
                            // 是否退票 0:未退票; 1:已退票
                            .eq("invoice_return", "0")
                            // 是否失效 0:否; 1:是
                            .eq("invoice_delete", "0")
                            // 认证结果 1:成功; 0:失败
                            .eq("verfy", "1")
                            .eq("invoice_match", "1")
                            .lt("invoice_auth_date", newYearAndMonth)
                            .ge("entry_date", newYearAndMonth)
                            .like(StringUtils.isNotBlank(invoiceNumber), "invoice_number", invoiceNumber)
                            .like(StringUtils.isNotBlank(invoiceCode), "invoice_code", invoiceCode)
                            .like(StringUtils.isNotBlank(invoiceClass), "invoice_class", invoiceClass)
                            .like(StringUtils.isNotBlank(entrySuccessCode), "entry_success_code", entrySuccessCode)
            );
            return new PageUtils(page);
        }else if(resultType.equals("2")){
            IPage<InputInvoiceEntity> page = invoiceService.page(
                    new Query<InputInvoiceEntity>().getPage(params),
                    new QueryWrapper<InputInvoiceEntity>()
                            .eq(StringUtils.isNotBlank(sysDeptEntity.getTaxCode()), "invoice_purchaser_paragraph", sysDeptEntity.getTaxCode())
                            // 是否退票 0:未退票; 1:已退票
                            .eq("invoice_return", "0")
                            // 是否失效 0:否; 1:是
                            .eq("invoice_delete", "0")
                            // 认证结果 1:成功; 0:失败
                            .eq("verfy", "1")
                            .ne("invoice_match", "1")
                            .ge("invoice_auth_date", newYearAndMonth)
                            .like(StringUtils.isNotBlank(invoiceNumber), "invoice_number", invoiceNumber)
                            .like(StringUtils.isNotBlank(invoiceCode), "invoice_code", invoiceCode)
                            .like(StringUtils.isNotBlank(invoiceClass), "invoice_class", invoiceClass)
            );
            return new PageUtils(page);
        }else{
            IPage<InputInvoiceEntity> page = invoiceService.page(
                    new Query<InputInvoiceEntity>().getPage(params),
                    new QueryWrapper<InputInvoiceEntity>()
                            .eq(StringUtils.isNotBlank(sysDeptEntity.getTaxCode()), "invoice_purchaser_paragraph", sysDeptEntity.getTaxCode())
                            // 是否退票 0:未退票; 1:已退票
                            .eq("invoice_return", "0")
                            // 是否失效 0:否; 1:是
                            .eq("invoice_delete", "0")
                            // 认证结果 1:成功; 0:失败
                            .eq("verfy", "1")
                            .eq("invoice_match", "1")
                            .ge("invoice_auth_date", newYearAndMonth)
                            .lt("entry_date", newYearAndMonth)
                            .like(StringUtils.isNotBlank(invoiceNumber), "invoice_number", invoiceNumber)
                            .like(StringUtils.isNotBlank(invoiceCode), "invoice_code", invoiceCode)
                            .like(StringUtils.isNotBlank(invoiceClass), "invoice_class", invoiceClass)
            );
            return new PageUtils(page);
        }
    }

    @Override
    public Map<String, Object> getDifferenceMatchResult(Map<String, Object> params) {
        int offset = Integer.parseInt(params.get("offset").toString());
        int limit = Integer.parseInt(params.get("limit").toString());
        List<InvoiceDifferenceMatch> matchResultList = new ArrayList<>();
        Map<String, String> documentNoMap = new HashMap<>();
        Map<String, String> invoiceNumMap = new HashMap<>();
        //入账税额合计
        BigDecimal sunFee = BigDecimal.ZERO;
        //发票税额合计
        BigDecimal invoiceFee = BigDecimal.ZERO;
        //票账差异金额合计
        BigDecimal differenceFee = BigDecimal.ZERO;
        String yearAndMonth = ParamsMap.findMap(params, "yearAndMonth");
        String deptId = ParamsMap.findMap(params, "deptId");
        matchResultList = inputSqpMatchResultDao.getDifferenceMatchInvoice(offset, limit, deptId, yearAndMonth);
        int count = inputSqpMatchResultDao.getDifferenceMatchInvoiceCount(offset, limit, deptId, yearAndMonth);
        if (matchResultList.size() > 0) {
            for (int i = 0; i < matchResultList.size(); i++) {
                String doNum = matchResultList.get(i).getDocumentNo();
                String doNo = matchResultList.get(i).getInvoiceNumber();
                if (!documentNoMap.containsKey(doNum) && !invoiceNumMap.containsKey(doNo)) {
                    String fee = matchResultList.get(i).getDifferenceFee();
                    if (fee != null) {
                        differenceFee = differenceFee.add(new BigDecimal(fee));
                    }
                }
                if (!documentNoMap.containsKey(doNum)) {
                    String amountInLocal = matchResultList.get(i).getAmountInLocal();
                    documentNoMap.put(doNum, amountInLocal);
                    sunFee = sunFee.add(new BigDecimal(amountInLocal));
                }
                if (!invoiceNumMap.containsKey(doNo)) {
                    String amountInLocal = matchResultList.get(i).getInvoiceTaxPrice();
                    if (amountInLocal != null) {
                        invoiceNumMap.put(doNum, amountInLocal);
                        invoiceFee = invoiceFee.add(new BigDecimal(amountInLocal));
                    }
                }
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("page", new PageUtils(matchResultList, count, limit, offset));
        result.put("sunFee", sunFee);
        result.put("invoiceFee", invoiceFee);
        result.put("differenceFee", differenceFee);
        return result;
    }

    @Override
    public Map<String, Object> getRedDifferenceMatchResult(Map<String, Object> params) {
        int offset = Integer.parseInt(params.get("offset").toString());
        int limit = Integer.parseInt(params.get("limit").toString());
        List<RedInvoiceDifferenceMatch> matchResultList = new ArrayList<>();
        Map<String, String> documentNoMap = new HashMap<>();
        Map<String, String> invoiceNumMap = new HashMap<>();
        //入账税额合计
        BigDecimal sunFee = BigDecimal.ZERO;
        //发票税额合计
        BigDecimal invoiceFee = BigDecimal.ZERO;
        //票账差异金额合计
        BigDecimal differenceFee = BigDecimal.ZERO;
        String yearAndMonth = ParamsMap.findMap(params, "yearAndMonth");
        String deptId = ParamsMap.findMap(params, "deptId");

        matchResultList = inputSqpMatchResultDao.getRedDifferenceMatchInvoice(offset, limit, deptId, yearAndMonth);

        int count = inputSqpMatchResultDao.getRedDifferenceMatchInvoiceCount(offset, limit, deptId, yearAndMonth);
        if (matchResultList.size() > 0) {
            for (int i = 0; i < matchResultList.size(); i++) {
                String doNum = matchResultList.get(i).getDocumentNo();
                String doNo = matchResultList.get(i).getRedNoticeNumber();
                if (!documentNoMap.containsKey(doNum) && !invoiceNumMap.containsKey(doNo)) {
                    String fee = matchResultList.get(i).getDifferenceFee();
                    if (fee != null) {
                        differenceFee = differenceFee.add(new BigDecimal(fee));
                    }
                }
                if (!documentNoMap.containsKey(doNum)) {
                    String amountInLocal = matchResultList.get(i).getAmountInLocal();
                    documentNoMap.put(doNum, amountInLocal);
                    sunFee = sunFee.add(new BigDecimal(amountInLocal));
                }
                if (!invoiceNumMap.containsKey(doNo)) {
                    String amountInLocal = String.valueOf(matchResultList.get(i).getTaxPrice());
                    if (amountInLocal != null && !amountInLocal.equals("null")) {
                        invoiceNumMap.put(doNum, amountInLocal);
                        invoiceFee = invoiceFee.add(new BigDecimal(amountInLocal));
                    }
                }
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("page", new PageUtils(matchResultList, count, limit, offset));
        result.put("sunFee", sunFee);
        result.put("invoiceFee", invoiceFee);
        result.put("differenceFee", differenceFee);
        return result;
    }

    @Override
    public List getDifferenceMatchResultExcel(Map<String, Object> params) {
        int type = Integer.parseInt(params.get("type").toString());
        Map<String, String> documentNoMap = new HashMap<>();
        Map<String, String> invoiceNumMap = new HashMap<>();
        //入账税额合计
        BigDecimal sunFee = BigDecimal.ZERO;
        //发票税额合计
        BigDecimal invoiceFee = BigDecimal.ZERO;
        //票账差异金额合计
        BigDecimal differenceFee = BigDecimal.ZERO;
        String yearAndMonth = ParamsMap.findMap(params, "yearAndMonth");
        String deptId = ParamsMap.findMap(params, "deptId");
        if (type == 1) {
            //发票下载
            List<InvoiceDifferenceMatch> matchResultList = inputSqpMatchResultDao.getDifferenceMatchInvoiceExcel(deptId, yearAndMonth);
            if (matchResultList.size() > 0) {
                for (int i = 0; i < matchResultList.size(); i++) {
                    String doNum = matchResultList.get(i).getDocumentNo();
                    String doNo = matchResultList.get(i).getInvoiceNumber();
                    if (!documentNoMap.containsKey(doNum) && !invoiceNumMap.containsKey(doNo)) {
                        String fee = matchResultList.get(i).getDifferenceFee();
                        if (fee != null) {
                            differenceFee = differenceFee.add(new BigDecimal(fee));
                        }
                    }
                    if (!documentNoMap.containsKey(doNum)) {
                        String amountInLocal = matchResultList.get(i).getAmountInLocal();
                        documentNoMap.put(doNum, amountInLocal);
                        sunFee = sunFee.add(new BigDecimal(amountInLocal));
                    }
                    if (!invoiceNumMap.containsKey(doNo)) {
                        String amountInLocal = matchResultList.get(i).getInvoiceTaxPrice();
                        if (amountInLocal != null) {
                            invoiceNumMap.put(doNum, amountInLocal);
                            invoiceFee = invoiceFee.add(new BigDecimal(amountInLocal));
                        }
                    }
                }
            }
            InvoiceDifferenceMatch match = new InvoiceDifferenceMatch();
            match.setMouth("合计");
            match.setAmountInLocal(sunFee.toString());
            match.setInvoiceTaxPrice(invoiceFee.toString());
            match.setDifferenceFee(differenceFee.toString());
            matchResultList.add(match);
            return matchResultList;
        } else if (type == 2) {
            //海关通知单下载
            List<InvoiceCustomsDifferenceMatch> matchResultList = inputSqpMatchResultDao.getCustomsDifferenceMatchResultExcel(deptId, yearAndMonth);
            if (matchResultList.size() > 0) {
                for (int i = 0; i < matchResultList.size(); i++) {
                    String doNum = matchResultList.get(i).getDocumentNo();
                    String doNo = matchResultList.get(i).getPayNo();
                    if (!documentNoMap.containsKey(doNum) && !invoiceNumMap.containsKey(doNo)) {
                        String fee = matchResultList.get(i).getDifferenceFee();
                        if (fee != null) {
                            differenceFee = differenceFee.add(new BigDecimal(fee));
                        }
                    }
                    if (!documentNoMap.containsKey(doNum)) {
                        String amountInLocal = matchResultList.get(i).getAmountInLocal();
                        documentNoMap.put(doNum, amountInLocal);
                        sunFee = sunFee.add(new BigDecimal(amountInLocal));
                    }
                    if (!invoiceNumMap.containsKey(doNo)) {
                        String amountInLocal = matchResultList.get(i).getTotalTax();
                        if (amountInLocal != null) {
                            invoiceNumMap.put(doNum, amountInLocal);
                            invoiceFee = invoiceFee.add(new BigDecimal(amountInLocal));
                        }
                    }
                }
            }
            InvoiceCustomsDifferenceMatch match = new InvoiceCustomsDifferenceMatch();
            match.setMouth("合计");
            match.setAmountInLocal(sunFee.toString());
            match.setTotalTax(invoiceFee.toString());
            match.setDifferenceFee(differenceFee.toString());
            matchResultList.add(match);
            return matchResultList;
        } else {
            //红字通知单下载
            List<RedInvoiceDifferenceMatch> matchResultList = inputSqpMatchResultDao.getRedDifferenceMatchInvoiceExcel(deptId, yearAndMonth);
            if (matchResultList.size() > 0) {
                for (int i = 0; i < matchResultList.size(); i++) {
                    String doNum = matchResultList.get(i).getDocumentNo();
                    String doNo = matchResultList.get(i).getRedNoticeNumber();
                    if (!documentNoMap.containsKey(doNum) && !invoiceNumMap.containsKey(doNo)) {
                        String fee = matchResultList.get(i).getDifferenceFee();
                        if (fee != null) {
                            differenceFee = differenceFee.add(new BigDecimal(fee));
                        }
                    }
                    if (!documentNoMap.containsKey(doNum)) {
                        String amountInLocal = matchResultList.get(i).getAmountInLocal();
                        documentNoMap.put(doNum, amountInLocal);
                        sunFee = sunFee.add(new BigDecimal(amountInLocal));
                    }
                    if (!invoiceNumMap.containsKey(doNo)) {
                        String amountInLocal = String.valueOf(matchResultList.get(i).getTaxPrice());
                        if (amountInLocal != null && !amountInLocal.equals("null")) {
                            invoiceNumMap.put(doNum, amountInLocal);
                            invoiceFee = invoiceFee.add(new BigDecimal(amountInLocal));
                        }
                    }
                }
            }
            RedInvoiceDifferenceMatch match = new RedInvoiceDifferenceMatch();
            match.setMouth("合计");
            match.setAmountInLocal(sunFee.toString());
            match.setTaxPrice(invoiceFee);
            match.setDifferenceFee(differenceFee.toString());
            matchResultList.add(match);
            return matchResultList;
        }
    }

    @Override
    public Map<String, Object> getCustomsDifferenceMatchResult(Map<String, Object> params) {
        int offset = Integer.parseInt(params.get("offset").toString());
        int limit = Integer.parseInt(params.get("limit").toString());
        List<InvoiceCustomsDifferenceMatch> matchResultList = new ArrayList<>();
        Map<String, String> documentNoMap = new HashMap<>();
        Map<String, String> invoiceNumMap = new HashMap<>();
        //入账税额合计
        BigDecimal sunFee = BigDecimal.ZERO;
        //发票税额合计
        BigDecimal invoiceFee = BigDecimal.ZERO;
        //票账差异金额合计
        BigDecimal differenceFee = BigDecimal.ZERO;
        String yearAndMonth = ParamsMap.findMap(params, "yearAndMonth");
        String deptId = ParamsMap.findMap(params, "deptId");

        matchResultList = inputSqpMatchResultDao.getCustomsDifferenceMatchResult(offset, limit, deptId, yearAndMonth);
        int count = inputSqpMatchResultDao.getCustomsDifferenceMatchResultCount(offset, limit, deptId, yearAndMonth);
        if (matchResultList.size() > 0) {
            for (int i = 0; i < matchResultList.size(); i++) {
                String doNum = matchResultList.get(i).getDocumentNo();
                String doNo = matchResultList.get(i).getPayNo();
                if (!documentNoMap.containsKey(doNum) && !invoiceNumMap.containsKey(doNo)) {
                    String fee = matchResultList.get(i).getDifferenceFee();
                    if (fee != null) {
                        differenceFee = differenceFee.add(new BigDecimal(fee));
                    }
                }
                if (!documentNoMap.containsKey(doNum)) {
                    String amountInLocal = matchResultList.get(i).getAmountInLocal();
                    documentNoMap.put(doNum, amountInLocal);
                    sunFee = sunFee.add(new BigDecimal(amountInLocal));
                }
                if (!invoiceNumMap.containsKey(doNo)) {
                    String amountInLocal = matchResultList.get(i).getTotalTax();
                    if (amountInLocal != null) {
                        invoiceNumMap.put(doNum, amountInLocal);
                        invoiceFee = invoiceFee.add(new BigDecimal(amountInLocal));
                    }
                }
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("page", new PageUtils(matchResultList, count, limit, offset));
        result.put("sunFee", sunFee);
        result.put("invoiceFee", invoiceFee);
        result.put("differenceFee", differenceFee);
        return result;
    }

    public List<InputSapMatchResultEntity> queryMatchCurTime(Map<String, Object> params) {
        List<InputSapMatchResultEntity> matchResultList = new ArrayList<>();
        InputSapMatchResultEntity redInvoiceVo = getRedInvoiceVo(params);
        InputSapMatchResultEntity customsVo = getCustomsVo(params);
        InputSapMatchResultEntity invoiceVo = getInvoiceVo(params);
        matchResultList.add(redInvoiceVo);
        matchResultList.add(customsVo);
        matchResultList.add(invoiceVo);
        return matchResultList;
    }

    public Collection<InputSapMatchResultEntity> getMatchResultByIds(Map<String, Object> params) {
        String ids = ParamsMap.findMap(params, "ids");
        List<String> idList = Arrays.asList(ids.split(","));
        return super.list(
                new QueryWrapper<InputSapMatchResultEntity>()
                        .in("result_id", idList)
                        .orderByDesc("create_time")
        );
    }

    // 红字通知单账票匹配结果
    public InputSapMatchResultEntity getRedInvoiceVo(Map<String, Object> params) {
        BigDecimal certificationTax = BigDecimal.ZERO;  // 认证税额
        BigDecimal monthCertTax = BigDecimal.ZERO;  //本月认证未入账税额
        BigDecimal monthCertBeforeTax = BigDecimal.ZERO; // 本月认证前月入账
        BigDecimal creditTax = BigDecimal.ZERO; // 入账税额
        BigDecimal monthCredTax = BigDecimal.ZERO; //本月入账未认证税额
        BigDecimal monthCredBeforeTax = BigDecimal.ZERO; // 本月入账前月认证
        InputSapMatchResultEntity resultVo = new InputSapMatchResultEntity();
        String deptId = params.get("deptId").toString();
        //红字通知单根据填开日期查询
        List<InputRedInvoiceEntity> redInvoiceList = inputRedInvoiceService.getCertification(params);
        Date dates = DateUtils.stringToDate(((String) params.get("yearAndMonth")).substring(0, 7) + "-01", "yyyy-MM-dd");
        for (InputRedInvoiceEntity redInvoice : redInvoiceList) {
            certificationTax = certificationTax.add(new BigDecimal(redInvoice.getTaxPrice().toString()));
            if ((redInvoice.getEntryStatus()).equals(InputConstant.InvoiceMatch.MATCH_NO.getValue())) {
                monthCertTax = monthCertTax.add(new BigDecimal(redInvoice.getTaxPrice().toString()));
            } else {
                if (redInvoice.getWriteDate().before(dates)) {
                    monthCertBeforeTax = monthCertBeforeTax.add(new BigDecimal(redInvoice.getTaxPrice().toString()));
                }
            }
        }
        // 入账
        List<InputInvoiceSapEntity> sapEntitys = inputInvoiceSapService.getEntityByDateAndStatus(((String) params.get("yearAndMonth")).substring(0, 7), "3", params.get("deptId").toString());
        for (InputInvoiceSapEntity sapEntity : sapEntitys) {
            creditTax = creditTax.add(sapEntity.getAmountInLocal());
            if (sapEntity.getSapMatch().equals(InputConstant.InvoiceMatch.MATCH_NO.getValue())) {
                monthCredTax = monthCredTax.add(sapEntity.getAmountInLocal());
            } else {
                //获取小于本月入账日期的单号
                Date datex = DateUtils.stringToDate(sapEntity.getPstngDate().substring(0, 7) + "-01 00:00:00", DateUtils.DATE_TIME_PATTERN);
                List<InputRedInvoiceEntity> redInvoiceListSap = inputRedInvoiceService.list(
                        new QueryWrapper<InputRedInvoiceEntity>()
                                .in("entry_status", new String[]{"1", "2"})
                                .le("write_date", datex)
                                .eq("dept_id", deptId)
                );
                for (InputRedInvoiceEntity redInvoice : redInvoiceListSap) {
                    monthCredBeforeTax = monthCredBeforeTax.add(new BigDecimal(redInvoice.getTaxPrice().toString()));
                }
            }
        }
        resultVo.setYearAndMonth((String) params.get("date"));
        //分类(0-发票 1-海关缴款书 2-红字通知单)
        resultVo.setSort(2);
        resultVo.setCredittax(creditTax.toString());
        resultVo.setCertificationtax(certificationTax.toString());
        BigDecimal differenceTax = creditTax.subtract(certificationTax);
        resultVo.setDifferencetax(differenceTax.toString());
        BigDecimal adjustmentTax = monthCredTax.add(monthCredBeforeTax).add(monthCertBeforeTax).add(monthCertTax);
        resultVo.setAdjustmenttax(adjustmentTax.toString());
        resultVo.setChecktax((differenceTax.subtract(adjustmentTax)).toString());
        resultVo.setMonthcredtax(monthCredTax.toString());
        resultVo.setMonthcerttax(monthCertTax.toString());
        resultVo.setMonthcredbeforetax(monthCredBeforeTax.toString());
        resultVo.setMonthcertbeforetax(monthCertBeforeTax.toString());
        resultVo.setYearAndMonth((String) params.get("yearAndMonth"));
        resultVo.setDeptId(Integer.parseInt(params.get("deptId").toString()));
        resultVo.setCreateBy(String.valueOf(ShiroUtils.getUserId()));
        resultVo.setCreateTime(new Date());
        super.save(resultVo);
        return resultVo;
    }

    //海关通知单账票匹配结果
    public InputSapMatchResultEntity getCustomsVo(Map<String, Object> params) {
        BigDecimal certificationTax = BigDecimal.ZERO;  // 认证税额
        BigDecimal monthCertTax = BigDecimal.ZERO;  //本月认证未入账税额
        BigDecimal monthCertBeforeTax = BigDecimal.ZERO; // 本月认证前月入账
        BigDecimal creditTax = BigDecimal.ZERO; // 入账税额
        BigDecimal monthCredTax = BigDecimal.ZERO; //本月入账未认证税额
        BigDecimal monthCredBeforeTax = BigDecimal.ZERO; // 本月入账前月认证
        InputSapMatchResultEntity resultVo = new InputSapMatchResultEntity();
        List<InputInvoiceCustomsEntity> customsEntitys = inputInvoiceCustomsService.getCertification(params);
        for (InputInvoiceCustomsEntity customsEntity : customsEntitys) {
            certificationTax = certificationTax.add(new BigDecimal(customsEntity.getTotalTax()));
            if ((customsEntity.getEntryState()).equals(InputConstant.InvoiceMatch.MATCH_NO.getValue())) {
                monthCertTax = monthCertTax.add(new BigDecimal(customsEntity.getTotalTax()));
            } else {
                Date date = DateUtils.stringToDate(customsEntity.getEntryDate(), "yyyy-MM-dd");
                Date dates = DateUtils.stringToDate(((String) params.get("yearAndMonth")).substring(0, 7) + "-01", "yyyy-MM-dd");
                if (date.before(dates)) {
                    monthCertBeforeTax = monthCertBeforeTax.add(new BigDecimal(customsEntity.getTotalTax()));
                }
            }
        }
        // 入账
        List<InputInvoiceSapEntity> sapEntitys = inputInvoiceSapService.getEntityByDateAndStatus(((String) params.get("yearAndMonth")).substring(0, 7), "2", params.get("deptId").toString());
        for (InputInvoiceSapEntity sapEntity : sapEntitys) {
            creditTax = creditTax.add(sapEntity.getAmountInLocal());
            if (sapEntity.getSapMatch().equals(InputConstant.InvoiceMatch.MATCH_NO.getValue())) {
                monthCredTax = monthCredTax.add(sapEntity.getAmountInLocal());
            } else {
                //获取小于本月入账日期的单号
                Date datex = DateUtils.stringToDate(sapEntity.getPstngDate().substring(0, 7) + "-01", DateUtils.DATE_PATTERN);
                String deptId = params.get("deptId").toString();
                SysDeptEntity deptEntity = sysDeptService.getById(deptId);
                List<InputInvoiceCustomsEntity> redInvoiceListSap = inputInvoiceCustomsService.list(
                        new QueryWrapper<InputInvoiceCustomsEntity>()
                                .in("deductible", new String[]{"1", "4"})
                                .le("deductible_date", datex)
                                .eq("purchaser_name", deptEntity.getName())
                );
                for (InputInvoiceCustomsEntity customsEntity : redInvoiceListSap) {
                    monthCredBeforeTax = monthCredBeforeTax.add(new BigDecimal(customsEntity.getTotalTax()));
                }
            }
        }
        resultVo.setYearAndMonth((String) params.get("date"));
        //分类(0-发票 1-海关缴款书 2-红字通知单)
        resultVo.setSort(1);
        resultVo.setCredittax(creditTax.toString());
        resultVo.setCertificationtax(certificationTax.toString());
        BigDecimal differenceTax = creditTax.subtract(certificationTax);
        resultVo.setDifferencetax(differenceTax.toString());
        BigDecimal adjustmentTax = monthCredTax.add(monthCredBeforeTax).add(monthCertBeforeTax).add(monthCertTax);
        resultVo.setAdjustmenttax(adjustmentTax.toString());
        resultVo.setChecktax((differenceTax.subtract(adjustmentTax)).toString());
        resultVo.setMonthcredtax(monthCredTax.toString());
        resultVo.setMonthcerttax(monthCertTax.toString());
        resultVo.setMonthcredbeforetax(monthCredBeforeTax.toString());
        resultVo.setMonthcertbeforetax(monthCertBeforeTax.toString());
        resultVo.setYearAndMonth((String) params.get("yearAndMonth"));
        resultVo.setDeptId(Integer.parseInt(params.get("deptId").toString()));
        resultVo.setCreateBy(String.valueOf(ShiroUtils.getUserId()));
        resultVo.setCreateTime(new Date());
        super.save(resultVo);
        return resultVo;
    }


    //发票账票匹配结果
    public InputSapMatchResultEntity getInvoiceVo(Map<String, Object> params) {
        BigDecimal certificationTax = BigDecimal.ZERO;  // 认证税额
        BigDecimal monthCertTax = BigDecimal.ZERO;  //本月认证未入账税额
        BigDecimal monthCertBeforeTax = BigDecimal.ZERO; // 本月认证前月入账
        BigDecimal creditTax = BigDecimal.ZERO; // 入账税额
        BigDecimal monthCredTax = BigDecimal.ZERO; //本月入账未认证税额
        BigDecimal monthCredBeforeTax = BigDecimal.ZERO; // 本月入账前月认证
        InputSapMatchResultEntity resultVo = new InputSapMatchResultEntity();
        List<InputInvoiceEntity> customsEntitys = inputInvoiceService.getCertificationList(params);
        for (InputInvoiceEntity invoice : customsEntitys) {
            certificationTax = certificationTax.add(new BigDecimal(invoice.getInvoiceTaxPrice().toString()));
            if ((invoice.getApplyStatus()).equals(InputConstant.InvoiceMatch.MATCH_NO.getValue())) {
                monthCertTax = monthCertTax.add(new BigDecimal(invoice.getInvoiceTaxPrice().toString()));
            } else {
                Date date = DateUtils.stringToDate(invoice.getEntryDate(), "yyyy-MM-dd");
                Date dates = DateUtils.stringToDate(((String) params.get("yearAndMonth")).substring(0, 7) + "-01", "yyyy-MM-dd");
                if (date.before(dates)) {
                    monthCertBeforeTax = monthCertBeforeTax.add(new BigDecimal(invoice.getInvoiceTaxPrice().toString()));
                }
            }
        }
        // 入账
        List<InputInvoiceSapEntity> sapEntitys = inputInvoiceSapService.getEntityByDateAndStatus(((String) params.get("yearAndMonth")).substring(0, 7), "1", params.get("deptId").toString());
        for (InputInvoiceSapEntity sapEntity : sapEntitys) {
            creditTax = creditTax.add(sapEntity.getAmountInLocal());
            if (sapEntity.getSapMatch().equals(InputConstant.InvoiceMatch.MATCH_NO.getValue())) {
                monthCredTax = monthCredTax.add(sapEntity.getAmountInLocal());
            } else {
                //获取小于本月入账日期的单号
                Date datex = DateUtils.stringToDate(sapEntity.getPstngDate().substring(0, 7) + "-01", DateUtils.DATE_PATTERN);
                String deptId = params.get("deptId").toString();
                SysDeptEntity deptEntity = sysDeptService.getById(deptId);
                List<InputInvoiceEntity> invoiceListSap = inputInvoiceService.list(
                        new QueryWrapper<InputInvoiceEntity>()
                                .in("invoice_status", new String[]{"12", "14"})
                                .le("invoice_auth_date", datex)
                                .eq("invoice_purchaser_company", deptEntity.getName())
                );
                for (InputInvoiceEntity invoice : invoiceListSap) {
                    monthCredBeforeTax = monthCredBeforeTax.add(invoice.getInvoiceTaxPrice());
                }
            }
        }
        resultVo.setYearAndMonth((String) params.get("date"));
        //分类(0-发票 1-海关缴款书 2-红字通知单)
        resultVo.setSort(0);
        resultVo.setCredittax(creditTax.toString());
        resultVo.setCertificationtax(certificationTax.toString());
        BigDecimal differenceTax = creditTax.subtract(certificationTax);
        resultVo.setDifferencetax(differenceTax.toString());
        /*BigDecimal adjustmentTax = (monthCredTax.add(monthCredBeforeTax)).subtract(monthCertTax.add(monthCertBeforeTax));*/
        BigDecimal adjustmentTax = monthCredTax.add(monthCredBeforeTax).add(monthCertBeforeTax).add(monthCertTax);
        resultVo.setAdjustmenttax(adjustmentTax.toString());
        resultVo.setChecktax((differenceTax.subtract(adjustmentTax)).toString());
        resultVo.setMonthcredtax(monthCredTax.toString());
        resultVo.setMonthcerttax(monthCertTax.toString());
        resultVo.setMonthcredbeforetax(monthCredBeforeTax.toString());
        resultVo.setMonthcertbeforetax(monthCertBeforeTax.toString());
        resultVo.setYearAndMonth((String) params.get("yearAndMonth"));
        resultVo.setDeptId(Integer.parseInt(params.get("deptId").toString()));
        resultVo.setCreateBy(String.valueOf(ShiroUtils.getUserId()));
        resultVo.setCreateTime(new Date());
        super.save(resultVo);
        return resultVo;
    }

    @Override
    public boolean updateMatchResult(Map<String, Object> params) {
        String ids = ParamsMap.findMap(params, "ids");
        boolean flag = true;
        String status = ParamsMap.findMap(params, "status");
        String calibration = ParamsMap.findMap(params, "calibration");
        String calibrationExplain = ParamsMap.findMap(params, "calibrationExplain");
        if (ids != null) {
            String[] idList = ids.split(",");
            for (int i = 0; i < idList.length; i++) {
                String id = idList[i];
                InputSapMatchResultEntity matchResult = super.getById(id);
                if (status != null) {
                    matchResult.setStatus(Integer.parseInt(status));
                }
                if (calibration != null) {
                    matchResult.setCalibration(calibration);
                    matchResult.setAdjustmenttax((new BigDecimal(matchResult.getAdjustmenttax()).add(new BigDecimal(calibration))).toString());
                    matchResult.setChecktax((new BigDecimal(matchResult.getDifferencetax()).subtract(new BigDecimal(matchResult.getAdjustmenttax())).toString()));
                }
                if (calibrationExplain != null) {
                    matchResult.setCalibrationExplain(calibrationExplain);
                }
                matchResult.setUpdateBy(String.valueOf(ShiroUtils.getUserId()));
                matchResult.setUpdateTime(new Date());
                boolean aa = super.updateById(matchResult);
                if (!aa) {
                    flag = aa;
                }
            }
        }
        return flag;
    }

}
