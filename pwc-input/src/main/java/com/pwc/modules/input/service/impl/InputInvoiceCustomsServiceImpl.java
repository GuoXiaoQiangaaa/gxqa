package com.pwc.modules.input.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fapiao.neon.client.customs.CustomsClient;
import com.fapiao.neon.client.in.BaseClient;
import com.fapiao.neon.client.in.CollectCustomsClient;
import com.fapiao.neon.client.in.DeductClient;
import com.fapiao.neon.model.CallResult;
import com.fapiao.neon.model.Entity;
import com.fapiao.neon.model.in.*;
import com.fapiao.neon.param.PaymentCertificate;
import com.fapiao.neon.param.PaymentCertificateParamBody;
import com.fapiao.neon.param.in.*;
import com.pwc.common.annotation.ApiLogFilter;
import com.pwc.common.annotation.DataFilter;
import com.pwc.common.exception.RRException;
import com.pwc.common.utils.*;
import com.pwc.common.utils.apidemo.utils.IDGenerator;
import com.pwc.modules.input.dao.InputInvoiceCustomsDao;
import com.pwc.modules.input.entity.*;
import com.pwc.modules.input.service.InputInvoiceCustomsPushService;
import com.pwc.modules.input.service.InputInvoiceCustomsService;
import com.pwc.modules.input.service.InputInvoiceSapService;
import com.pwc.modules.input.service.InputInvoiceVoucherNoService;
import com.pwc.modules.sys.entity.SysDeptEntity;
import com.pwc.modules.sys.service.SysConfigService;
import com.pwc.modules.sys.service.SysDeptService;
import com.pwc.modules.sys.shiro.ShiroUtils;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("inputInvoiceCustomsService")
public class InputInvoiceCustomsServiceImpl extends ServiceImpl<InputInvoiceCustomsDao, InputInvoiceCustomsEntity> implements InputInvoiceCustomsService {
    @Resource
    private CollectCustomsClient collectCustomsClient;
    @Resource
    private DeductClient deductClient;
    @Autowired
    private InputInvoiceCustomsPushService inputInvoiceCustomsPushService;
    @Autowired
    private InputInvoiceVoucherNoService inputInvoiceVoucherNoService;
    @Resource
    private BaseClient baseClient;
    private Log log = Log.get(this.getClass());
    @Autowired
    private InputInvoiceSapService inputInvoiceSapService;
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private InputInvoiceCustomsService customsService;
    @Autowired
    private InputInvoiceCustomsService inputInvoiceCustomsService;

    @Override
    @DataFilter(subDept = true,userId = "upload_by")
    public PageUtils queryPage(Map<String, Object> params) {
        String payNo = (String) params.get("payNo");
        String deptId = ParamsMap.findMap(params, "deptId");
        String taxCode = null;
        if(deptId != null){
            SysDeptEntity deptEntity = sysDeptService.getById(deptId);
            if(deptEntity != null){
                taxCode = deptEntity.getTaxCode();
            }
        }
        String taxNo = (String) params.get("taxNo");
        String deductible = (String) params.get("deductible");
        String entryState = (String) params.get("entryState");
        String statisticsState = (String) params.get("statisticsState");
        String batchNo = (String) params.get("batchNo");
        String deductiblePeriod = (String) params.get("deductiblePeriod");
        String billingDateArray = (String) params.get("billingDateArray");
        String billingDateStart = "";
        String billingDateEnd = "";
        if (StringUtils.isNotBlank(billingDateArray)) {
            billingDateStart = billingDateArray.split(",")[0];
            billingDateEnd = billingDateArray.split(",")[1];
        }
        String deductibleDateArray = (String) params.get("deductibleDateArray");
        String deductibleDateStart = "";
        String deductibleDateEnd = "";
        if (StringUtils.isNotBlank(deductibleDateArray)) {
            deductibleDateStart = deductibleDateArray.split(",")[0];
            deductibleDateEnd = deductibleDateArray.split(",")[1];
        }

        IPage<InputInvoiceCustomsEntity> page = this.page(
                new Query<InputInvoiceCustomsEntity>().getPage(params),
                new QueryWrapper<InputInvoiceCustomsEntity>()
                        .eq(StringUtils.isNotBlank(payNo), "pay_no", payNo)
                        .eq(StringUtils.isNotBlank(taxCode), "purchaser_tax_no", taxCode)
                        .eq(StringUtils.isNotBlank(taxNo), "purchaser_tax_no", taxNo)
                        .eq(StringUtils.isNotBlank(deductible), "deductible", deductible)
                        .eq(StringUtils.isNotBlank(entryState), "entry_state", entryState)
                        .eq(StringUtils.isNotBlank(statisticsState), "statistics_state", statisticsState)
                        .eq(StringUtils.isNotBlank(batchNo), "batch_no", batchNo)
                        .eq(StringUtils.isNotBlank(deductiblePeriod), "deductible_period", deductiblePeriod)
                        .between(StringUtils.isNotBlank(billingDateStart) && StringUtils.isNotBlank(billingDateEnd), "billing_date", billingDateStart, billingDateEnd)
                        .between(StringUtils.isNotBlank(deductibleDateStart) && StringUtils.isNotBlank(deductibleDateEnd), "deductible_date", deductibleDateStart, deductibleDateEnd)
                        .apply(params.get(Constant.SQL_FILTER) != null, (String)params.get(Constant.SQL_FILTER))
                        .orderByDesc("upload_date")
        );

        return new PageUtils(page);
    }

    @Override
    @ApiLogFilter(type = "5")
    public CallResult<CustomsInvoiceInfo> invoices(SyncInvoiceParamBody syncInvoiceParamBody) {
        CallResult<CustomsInvoiceInfo> invoices = collectCustomsClient.invoices(syncInvoiceParamBody);
        return invoices;
    }


    /**
     * 海关缴款书采集
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String syncApply(String taxNo, String payNo, String billingDate, String totalTax) {
        CustomsApplyParamBody paramBody = new CustomsApplyParamBody();
        List<CustomsApplyInvoice> invoiceList = new ArrayList<>();
        CustomsApplyInvoice invoice = new CustomsApplyInvoice();
        invoice.setPayNo(payNo);
        invoice.setBillingDate(billingDate);
        invoice.setTotalTax(totalTax);
        paramBody.setTaxNo(taxNo);
        invoiceList.add(invoice);
        paramBody.setInvoices(invoiceList);
        paramBody.setServiceType("0");
        CallResult<Entity> aa = collectCustomsClient.apply(paramBody);
        if (aa.isSuccess()) {
            return aa.getData().getRequestId();
        } else {
            return aa.getExceptionResult().getMessage();
        }
    }

    /**
     * 海关缴款书采集结果获取
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<CustomsInvoiceResult> syncApplyResult(String requestId) {
        CommonParamBody body = new CommonParamBody();
        body.setRequestId(requestId);
        CallResult<CollectCustomsResultInfo> bb = collectCustomsClient.results(body);
        if (bb.isSuccess()) {
            List<CustomsInvoiceResult> result = bb.getData().getResults();
            return result;
        } else {
            System.out.println(bb.getExceptionResult().getMessage());
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sync(String taxNo, String startBillingDate, String endBillingDate, int page) {
        SyncInvoiceParamBody paramBody = new SyncInvoiceParamBody();
        paramBody.setTaxNo(taxNo);
        // 同步类型: 1:同步所有发票; 3:同步已勾选发票; 4:同步未勾选发票
        paramBody.setSyncType("1");
        paramBody.setStartBillingDate(startBillingDate);
        paramBody.setEndBillingDate(endBillingDate);
        paramBody.setPage(page);
        paramBody.setPageSize(200);
        CallResult<CustomsInvoiceInfo> callResult = collectCustomsClient.invoices(paramBody);
        if (null == callResult) {
            log.error("税局接口无响应");
            throw new RRException("系统繁忙,请稍后重试");
        }
        if (callResult.isSuccess()) {
            CustomsInvoiceInfo data = callResult.getData();
            if (null != data && null != data.getInvoices()) {
                List<CustomsInvoice> invoiceList = data.getInvoices();
                this.saveInvoice(invoiceList);

                // 获取全部
                int currentPage = data.getCurrentPage();
                int pages = data.getPages();
                if (currentPage < pages) {
                    this.sync(taxNo, startBillingDate, endBillingDate, currentPage + 1);
                }
            }
        } else {
            log.error("海关缴款书同步失败: {}", callResult.getExceptionResult().getMessage());
            throw new RRException("海关缴款书同步失败");
        }
    }

    /**
     * 海关缴款书入库
     */
    @Override
    public void saveInvoice(List<? extends CustomsInvoice> invoices) {
        // 生成批次号
        String dateStr = DateUtil.format(new Date(), "yyyyMMdd");
        String batchNo = dateStr + "-";
        CustomsInvoice customsInvoice = invoices.get(0);
        List<InputInvoiceCustomsEntity> customsEntityList = super.list(
                new QueryWrapper<InputInvoiceCustomsEntity>()
                        .eq("purchaser_tax_no", customsInvoice.getPurchaserTaxNo())
                        .orderByDesc("upload_date")
        );
        if (CollectionUtil.isEmpty(customsEntityList)) {
            batchNo += 1;
        } else {
            InputInvoiceCustomsEntity customsEntity = customsEntityList.get(0);
            String lastBatchNo = customsEntity.getBatchNo();
            log.debug("最后一个批次号为: {}", lastBatchNo);
            if (StringUtils.isNotBlank(lastBatchNo)) {
                String lastDateStr = lastBatchNo.split("-")[0];
                String lastBatchStr = lastBatchNo.split("-")[1];
                int res = DateUtil.compare(DateUtil.parse(dateStr, "yyyyMMdd"), DateUtil.parse(lastDateStr, "yyyyMMdd"));
                if (0 == res) {
                    batchNo += Integer.valueOf(lastBatchStr) + 1;
                } else if (1 == res) {
                    batchNo += 1;
                } else {
                    throw new RRException("批次号有误,请联系管理员");
                }
            } else {
                batchNo += 1;
            }
        }

        for (CustomsInvoice entity : invoices) {
            InputInvoiceCustomsEntity one = super.getOne(new QueryWrapper<InputInvoiceCustomsEntity>()
                    .eq("pay_no", entity.getPayNo()));

            if (null != one) {
                log.debug("已同步海关缴款书：" + entity.getPayNo());
                BeanUtils.copyProperties(entity, one);
                one.setUpdateBy(ShiroUtils.getUserId().intValue());
                one.setUpdateTime(new Date());
                one.setBatchNo(batchNo);
                super.updateById(one);
            } else {
                InputInvoiceCustomsEntity invoiceSyncEntity = new InputInvoiceCustomsEntity();
                BeanUtils.copyProperties(entity, invoiceSyncEntity);
                // 入账状态 0为入账 1已入账 2已冲销
                invoiceSyncEntity.setEntryState("0");
                // 统计状态 0 未统计确认 1 统计确认成功 2 统计确认失败
                invoiceSyncEntity.setStatisticsState("0");
                // 是否退单 0未退 1已退
                invoiceSyncEntity.setInvoiceReturn("0");
                // 缴款书来源 0同步 1手工上传
                invoiceSyncEntity.setUploadType("0");
                invoiceSyncEntity.setUploadBy(ShiroUtils.getUserId().intValue());
                invoiceSyncEntity.setUploadDate(new Date());
                invoiceSyncEntity.setBatchNo(batchNo);
                baseMapper.insert(invoiceSyncEntity);
            }
        }
    }

    /**
     * 勾选/撤销
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deductOrCancel(Map<String, Object> params) {
        // 1:抵扣(勾选); 6:撤销抵扣
        String type = (String) params.get("type");
        String idsStr = (String) params.get("ids");
        if (StringUtils.isBlank(idsStr) || StringUtils.isBlank(type)) {
            log.error("勾选/撤销参数为空");
            throw new RRException("参数不能为空");
        }
        List<String> ids = Arrays.asList(idsStr.split(","));
        Collection<InputInvoiceCustomsEntity> entityList = super.listByIds(ids);
        if (CollectionUtil.isEmpty(entityList)) {
            log.error("根据id获取到的海关缴款书为空");
            throw new RRException("参数有误");
        } else {
            for (InputInvoiceCustomsEntity entity : entityList) {
                try {
                    // 构建请求参数
                    PaymentCertificateParamBody paramBody = new PaymentCertificateParamBody();
                    paramBody.setDeductType(type);
                    paramBody.setPeriod(entity.getDeductiblePeriod());
                    paramBody.setTaxNo(entity.getPurchaserTaxNo());
                    PaymentCertificate certificate = new PaymentCertificate();
                    certificate.setPaymentCertificateNo(entity.getPayNo());
                    certificate.setIssuedDate(DateUtil.parse(entity.getBillingDate(), "yyyy-MM-dd"));
                    certificate.setTax(entity.getTotalTax());
                    certificate.setValidTax(entity.getEffectiveTaxAmount());
                    certificate.setExportRejectNo(entity.getResaleCertificateNumber());
                    List<PaymentCertificate> paymentCertificates = new ArrayList<>();
                    paymentCertificates.add(certificate);
                    paramBody.setPaymentCertificates(paymentCertificates);
                    // 调用勾选/撤销勾选接口
                    CallResult<ApplyDeductResultInfo> callResult = deductClient.deductPaymentCertificates(paramBody);
                    if (null == callResult) {
                        log.error("勾选/撤销勾选税局接口无响应");
                        throw new RRException("接口无响应,请稍后重试");
                    } else {
                        if (callResult.isSuccess()) {
                            String requestId = callResult.getData().getRequestId();
                            log.info("勾选/撤销勾选中：{}" + requestId);
                            if ("1".equals(type)) {
                                entity.setDeductible("2");
                            } else if ("6".equals(type)) {
                                entity.setDeductible("4");
                            }
                            entity.setRequestId(requestId);
                            entity.setUpdateBy(ShiroUtils.getUserId().intValue());
                            entity.setUpdateTime(new Date());
                            super.updateById(entity);
                        } else {
                            String code = callResult.getExceptionResult().getCode();
                            String message = callResult.getExceptionResult().getMessage();
                            log.info("勾选/撤销勾选失败: code:{}, message:{}", code, message);
                            if ("1".equals(type)) {
                                entity.setDeductible("3");
                                entity.setRequestId(callResult.getExceptionResult().getRequestId());
                                entity.setUpdateBy(ShiroUtils.getUserId().intValue());
                                entity.setUpdateTime(new Date());
                                super.updateById(entity);
                            }
                        }
                    }
                } catch (RRException e) {
                    log.error("勾选/撤销勾选出错: {}", e);
                    throw e;
                } catch (Exception e) {
                    log.error("勾选/撤销勾选异常: {}", e);
                    throw new RRException("操作发生异常");
                }
            }
        }
    }

    /**
     * 获取勾选/撤销结果(定时任务用)
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void getDeductOrCancelResult() {
        // 2:勾选中; 4:撤销勾选认证中
        String[] deductible = {"2", "4"};
        List<InputInvoiceCustomsEntity> entityList = super.list(
                new QueryWrapper<InputInvoiceCustomsEntity>()
                        .in("deductible", Arrays.asList(deductible))
        );

        if (CollectionUtil.isEmpty(entityList)) {
            return;
        }
        for (InputInvoiceCustomsEntity entity : entityList) {
            try {
                CommonParamBody commonParamBody = new CommonParamBody();
                commonParamBody.setRequestId(entity.getRequestId());
                CallResult<PaymentCertificateListInfo> callResult = deductClient.deductPaymentCertificateResult(commonParamBody);
                if (null == callResult) {
                    log.error("获取勾选/撤销勾选结果税局接口无响应");
                } else {
                    if (callResult.isSuccess()) {
                        List<PaymentCertificateResult> resultList = callResult.getData().getPaymentCertificateResults();
                        if (CollectionUtil.isEmpty(resultList)) {
                            log.info("获取结果为空");
                        } else {
                            for (PaymentCertificateResult result : resultList) {
                                // 1: 成功; 其余都为失败
                                if ("1".equals(result.getDeductibleResult())) {
                                    if ("1".equals(result.getDeductType())) {// 勾选
                                        entity.setDeductible("1");
                                        entity.setDeductibleRes("1");
                                        entity.setDeductibleMode("1");
                                        entity.setDeductibleDate(result.getDeductibleDate());
                                    } else if ("6".equals(result.getDeductType())) {// 撤销
                                        entity.setDeductible("0");
                                        entity.setDeductibleRes("1");
                                        entity.setDeductibleMode("6");
                                    }
                                } else {
                                    if ("1".equals(result.getDeductType())) {// 勾选
                                        entity.setDeductible("3");
                                        entity.setDeductibleRes(result.getDeductibleResult());
                                        entity.setDeductibleMode("1");
                                    }
                                }
                                entity.setUpdateBy(ShiroUtils.getUserId().intValue());
                                entity.setUpdateTime(new Date());
                                super.updateById(entity);
                            }
                        }
                    } else {
                        String code = callResult.getExceptionResult().getCode();
                        String message = callResult.getExceptionResult().getMessage();
                        String requestId = callResult.getExceptionResult().getRequestId();
                        log.error("获取勾选/撤销勾选结果失败: code:{}, message:{}, requestId:{}", code, message, requestId);
                    }
                }
            } catch (Exception e) {
                log.error("获取勾选/撤销结果出现异常: {}", e);
            }
        }
    }

    @Override
    public void deductInvoices(String invoiceIds, String type, String period) {
        String[] invoiceArray = invoiceIds.split(",");
        for (String invoiceId : invoiceArray) {
            InputInvoiceCustomsEntity invoiceEntity = getById(invoiceId);
            deductInvoices(invoiceEntity, type, period);
        }
    }

    @Override
    public void deductInvoices(List<InputInvoiceCustomsEntity> invoiceList, String deductType, String period) {
        for (InputInvoiceCustomsEntity invoice : invoiceList) {
            deductInvoices(invoice, deductType, period);
        }
    }

    @Override
    public String deductInvoices(InputInvoiceCustomsEntity invoiceEntity, String deductType, String period) {
        if (StrUtil.isBlank(deductType)) {
            deductType = "1";
        }
        String requestId = null;
        // 可配置申报期
        if ("1".equals(deductType)) {
            period = getPeriodById(String.valueOf(invoiceEntity.getId())).getDeductiblePeriod();
            invoiceEntity.setDeductiblePeriod(period);
        } else if ("6".equals(deductType)) {
            period = invoiceEntity.getDeductiblePeriod();
        }
        PaymentCertificateParamBody deductParamBody = new PaymentCertificateParamBody();
        deductParamBody.setTaxNo(invoiceEntity.getPurchaserTaxNo());
        deductParamBody.setDeductType(deductType);
        deductParamBody.setPeriod(period);
        PaymentCertificate deductInvoice = new PaymentCertificate();
        deductInvoice.setPaymentCertificateNo(invoiceEntity.getPayNo());
        deductInvoice.setIssuedDate(DateUtils.stringToDate(invoiceEntity.getBillingDate(), "yyyy-MM-dd"));
        deductInvoice.setTax(invoiceEntity.getTotalTax());

        List<PaymentCertificate> invoices = new ArrayList<>();
        invoices.add(deductInvoice);
        deductParamBody.setPaymentCertificates(invoices);
        CallResult<ApplyDeductResultInfo> results = deductPaymentCertificates(deductParamBody);

        if (null != results) {
            log.info("勾选结果：" + results.toString());
            if (results.isSuccess()) {
                requestId = results.getData().getRequestId();
                invoiceEntity.setRequestId(requestId);
                //获取勾选结果
                deductResult(invoiceEntity);
            } else {
                invoiceEntity.setDeductible("2");
                updateById(invoiceEntity);
            }
        } else {
            log.info("勾选结果：未知异常无返回结果");
            invoiceEntity.setDeductible("2");
            updateById(invoiceEntity);
        }
        //  update(invoiceEntity);
        log.info("requestId:" + requestId);
        return requestId;
    }

    @Override
    @ApiLogFilter(type = "6")
    public CallResult<ApplyDeductResultInfo> deductPaymentCertificates(PaymentCertificateParamBody paymentCertificateParamBody) {
        CallResult<ApplyDeductResultInfo> applyDeductResultInfoCallResult = deductClient.deductPaymentCertificates(paymentCertificateParamBody);

        return applyDeductResultInfoCallResult;
    }

    @Override
    public void deductResult(InputInvoiceCustomsEntity invoice) {
        CommonParamBody commonParamBody = new CommonParamBody();
        commonParamBody.setRequestId(invoice.getRequestId());
        CallResult<PaymentCertificateListInfo> result = deductClient.deductPaymentCertificateResult(commonParamBody);
        if (null != result) {
            log.info("获取勾选结果：" + JSONObject.fromObject(result).toString());
            if (result.isSuccess()) {
                List<PaymentCertificateResult> deductResultInfoList = result.getData().getPaymentCertificateResults();
                for (PaymentCertificateResult deductResultInfo : deductResultInfoList) {
                    if ("1".equals(deductResultInfo.getDeductibleResult())) {
                        // 1 勾选 6 撤销勾选
                        if ("1".equals(deductResultInfo.getDeductType())) {
                            invoice.setDeductible("1");
                            invoice.setEffectiveTaxAmount(deductResultInfo.getValidTax());
                        } else if ("6".equals(deductResultInfo.getDeductType())) {
                            invoice.setDeductible("0");
                        }
                    } else {
                        invoice.setDeductible("2");
                    }
                }

            }
        } else {
            invoice.setDeductible("2");
            log.info("获取勾选结果失败，未返回结果信息");
        }
        updateById(invoice);
        //  }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void entryInvoices(String payNos, String type) {
        String[] split = payNos.split(",");
        List<InputInvoiceCustomsEntity> list = new ArrayList<>();
        List<InputInvoiceCustomsPushEntity> list2 = new ArrayList<>();
        for (String payno : split) {
            if (payno.length() == 18) {//匹配为18位的海关缴款书编号
                InputInvoiceCustomsEntity one = getOne(new QueryWrapper<InputInvoiceCustomsEntity>()
                        .eq("pay_no", payno));
                if ("entry".equals(type)) {//入账
                    one.setEntryState("1");
                } else {
                    one.setEntryState("2");
                }
                list.add(one);
                // updateById(one);
                InputInvoiceCustomsPushEntity push = inputInvoiceCustomsPushService.getOne(new QueryWrapper<InputInvoiceCustomsPushEntity>()
                        .eq("pay_no", payno));
                push.setState("1");
                list2.add(push);
                // inputInvoiceCustomsPushService.updateById(push);

            }
        }
        updateBatchById(list, list.size());
        inputInvoiceCustomsPushService.updateBatchById(list2, list2.size());
    }


    @Override
    public int getListByShow() {
        return this.baseMapper.getListByShow();
    }

    @Override
    public PageUtils getMonthCredBeforeResult(Map<String, Object> params){
        String yearAndMonth=ParamsMap.findMap(params, "yearAndMonth");
        String deptId=ParamsMap.findMap(params, "deptId");
        String payNo=ParamsMap.findMap(params, "payNo");
        String voucherCode=ParamsMap.findMap(params, "voucherCode");
        String batchNo=ParamsMap.findMap(params, "batchNo");
        SysDeptEntity sysDeptEntity = sysDeptService.getById(deptId);
        String resultType=ParamsMap.findMap(params, "resultType");
        //1 - 前期认证本月入账 2 -本月认证未入账
        if(resultType.equals("1")){
            String newYearAndMonth = yearAndMonth.substring(0,7) + "-01";
            IPage<InputInvoiceCustomsEntity> page = this.page(
                    new Query<InputInvoiceCustomsEntity>().getPage(params),
                    new QueryWrapper<InputInvoiceCustomsEntity>()
                            .eq(StringUtils.isNotBlank(sysDeptEntity.getTaxCode()), "purchaser_tax_no", sysDeptEntity.getTaxCode())
                            .eq("entry_state", "1")
                            .lt("billing_date", newYearAndMonth)
                            .ge("entry_date", newYearAndMonth)
                            .eq("deductible", "1")
                            .like(StringUtils.isNotBlank(payNo), "pay_no", payNo)
                            .like(StringUtils.isNotBlank(voucherCode), "voucher_code", voucherCode)
                            .like(StringUtils.isNotBlank(batchNo), "batch_no", batchNo)
            );
            return new PageUtils(page);
        }else if(resultType.equals("2")){
            IPage<InputInvoiceCustomsEntity> page = this.page(
                    new Query<InputInvoiceCustomsEntity>().getPage(params),
                    new QueryWrapper<InputInvoiceCustomsEntity>()
                            .eq(StringUtils.isNotBlank(sysDeptEntity.getTaxCode()), "purchaser_tax_no", sysDeptEntity.getTaxCode())
                            .ne("entry_state", "1")
                            .eq("deductible", "1")
                            .like("billing_date", yearAndMonth)
                            .like(StringUtils.isNotBlank(payNo), "pay_no", payNo)
                            .like(StringUtils.isNotBlank(voucherCode), "voucher_code", voucherCode)
                            .like(StringUtils.isNotBlank(batchNo), "batch_no", batchNo)
            );
            return new PageUtils(page);
        }else{
            String newYearAndMonth = yearAndMonth.substring(0,7) + "-01";
            IPage<InputInvoiceCustomsEntity> page = this.page(
                    new Query<InputInvoiceCustomsEntity>().getPage(params),
                    new QueryWrapper<InputInvoiceCustomsEntity>()
                            .eq(StringUtils.isNotBlank(sysDeptEntity.getTaxCode()), "purchaser_tax_no", sysDeptEntity.getTaxCode())
                            .eq("entry_state", "1")
                            .ge("billing_date", newYearAndMonth)
                            .lt("entry_date", newYearAndMonth)
                            .eq("deductible", "1")
                            .like(StringUtils.isNotBlank(payNo), "pay_no", payNo)
                            .like(StringUtils.isNotBlank(voucherCode), "voucher_code", voucherCode)
                            .like(StringUtils.isNotBlank(batchNo), "batch_no", batchNo)
            );
            return new PageUtils(page);
        }
    }

    @Override
    public Map saveVoucherPush(List<Map<String, Object>> params) {
        String returnCode = "";
        String returnMsg = "";
        String requestId = IDGenerator.getUUID();
        List<InputInvoiceCustomsPushEntity> pushList = new ArrayList<>();
        List<Map<String, Object>> ErrorDatas = new ArrayList<>();
        Map<String, Object> errorMap = null;
        Map<String, Object> returnMap = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Pattern pattern = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}");

        if (params.size() > 200) {
            returnCode = "ExceedMaxLines";
            returnMsg = "超出最大接收行数";
            return R.result(returnCode, returnMsg, 400);
        }
        for (Map<String, Object> map : params) {
            errorMap = new HashMap<>();
            String voucherNumber = (String) map.get("voucherNumber");
            String companyId = (String) map.get("companyID");
            String voucherType = (String) map.get("voucherType");
            String accountingNumber = (String) map.get("accountingNumber");
            String postingDate = (String) map.get("postingDate");
            String currencyType = (String) map.get("currencyType");
            String amount = (String) map.get("amount");
            String accountingCode = (String) map.get("accountingCode");
            String textField = (String) map.get("wenbenText");
            String distributeField = (String) map.get("FenpeiText");
            String consultField = (String) map.get("cankaoText");
            if (!(StringUtils.isNotBlank(voucherNumber) && StringUtils.isNotBlank(companyId) && StringUtils.isNotBlank(accountingNumber) && StringUtils.isNotBlank(postingDate) && StringUtils.isNotBlank(currencyType) && StringUtils.isNotBlank(amount) && StringUtils.isNotBlank(accountingCode))) {
                errorMap.put("voucherNumber", voucherNumber);
                errorMap.put("errorMsg", "参数不合法");
                ErrorDatas.add(errorMap);
                continue;
            }
            Matcher m = pattern.matcher(postingDate);
            if (!m.matches()) {
                errorMap.put("voucherNumber", voucherNumber);
                errorMap.put("errorMsg", "postingDate参数不合法");
                ErrorDatas.add(errorMap);
                continue;
            }
            try {
                sdf.parse(postingDate);
            } catch (ParseException e) {
                e.printStackTrace();
                errorMap.put("voucherNumber", voucherNumber);
                errorMap.put("errorMsg", "postingDate参数不合法");
                ErrorDatas.add(errorMap);
                continue;
            }
            InputInvoiceCustomsPushEntity one = inputInvoiceCustomsPushService.getOne(new QueryWrapper<InputInvoiceCustomsPushEntity>()
                    .eq("voucher_number", voucherNumber)
                    .eq("company_id", companyId)
                    .eq("accounting_number", accountingNumber)
                    .likeRight("posting_date", postingDate.substring(0, 4)));
            if (one != null) {
                one.setCompanyId(companyId);
                one.setVoucherType(voucherType);
                one.setAccountingNumber(accountingNumber);
                one.setPostingDate(postingDate);
                one.setCurrencyType(currencyType);
                one.setAmount(amount);
                one.setTextField(textField);
                one.setDistributeField(distributeField);
                one.setAccountingCode(accountingCode);
                one.setConsultField(consultField);
                one.setCreateDate(DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
                one.setState("0");
                pushList.add(one);
            } else {
                InputInvoiceCustomsPushEntity entity = new InputInvoiceCustomsPushEntity();
                entity.setVoucherNumber(voucherNumber);
                entity.setCompanyId(companyId);
                entity.setVoucherType(voucherType);
                entity.setAccountingNumber(accountingNumber);
                entity.setPostingDate(postingDate);
                entity.setCurrencyType(currencyType);
                entity.setAmount(amount);
                entity.setTextField(textField);
                entity.setDistributeField(distributeField);
                entity.setAccountingCode(accountingCode);
                entity.setConsultField(consultField);
                entity.setCreateDate(DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
                entity.setState("0");
                pushList.add(entity);
            }
        }
        if (pushList != null && pushList.size() > 0) {
            inputInvoiceCustomsPushService.saveOrUpdateBatch(pushList);
        }
        if (ErrorDatas.size() > 0) {
            returnCode = "IllegalParameter";
            returnMsg = "参数不合法";
            returnMap.put("ErrorDatas", ErrorDatas);
            returnMap.put("HttpStatusCode", 400);
        } else {
            returnCode = "Success";
            returnMsg = "信息接收成功";
            returnMap.put("HttpStatusCode", 200);
        }
        returnMap.put("returnCode", returnCode);
        returnMap.put("returnMsg", returnMsg);
        returnMap.put("requestId", requestId);
        return returnMap;
    }

    /**
     * 作废(退票)
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> updateByIdReturn(Map<String, Object> params) {
        String idsStr = (String) params.get("ids");
        String returnReason = (String) params.get("returnReason");
        if (StringUtils.isBlank(idsStr) || StringUtils.isBlank(returnReason)) {
            log.error("作废海关缴款书参数为空");
            throw new RRException("参数不能为空");
        }
        Map<String, Object> resMap = new HashMap<>();
        // 作废成功的id
        List<Long> successIds = new ArrayList<>();
        // 作废失败的海关缴款书号码
        List<String> failPayNo = new ArrayList<>();
        try {
            List<String> ids = Arrays.asList(idsStr.split(","));
            // 校验海关缴款书勾选状态是否为 0:未勾选; 3:勾选失败
            Collection<InputInvoiceCustomsEntity> entityList = super.listByIds(ids);
            if (CollectionUtil.isEmpty(entityList)) {
                log.error("根据id获取到的海关缴款书为空");
                throw new RRException("参数有误");
            } else {
                for (InputInvoiceCustomsEntity entity : entityList) {
                    if (!"0".equals(entity.getDeductible()) && !"3".equals(entity.getDeductible())) {
                        log.debug("海关缴款书状态有误: {}", entity.getPayNo());
                        failPayNo.add(entity.getPayNo());
                    } else {
                        successIds.add(entity.getId());
                    }
                }
            }
            resMap.put("total", ids.size());
            resMap.put("success", successIds.size());
            resMap.put("fail", failPayNo.size());
            resMap.put("failDetail", failPayNo);
            if (CollectionUtil.isNotEmpty(successIds)) {
                Integer updateBy = ShiroUtils.getUserId().intValue();
                Date updateTime = new Date();
                this.baseMapper.updateByIdReturn(successIds, returnReason, updateBy, updateTime);
            }
            return resMap;
        } catch (Exception e) {
            log.error("作废海关缴款书出错: {}", e);
            throw new RRException("作废发生异常");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void entryByManual(String entryState, String invoiceId, String voucherNumber) {
        InputInvoiceCustomsEntity byId = this.getById(invoiceId);
        InputInvoiceVoucherNoEntity e = new InputInvoiceVoucherNoEntity();
        e.setCreateDate(DateUtils.format(new Date()));
        e.setEntryState(entryState);
        e.setEntryType("1");
        e.setInvoiceCode(byId.getPayNo());
        e.setVoucherNumber(voucherNumber);
        inputInvoiceVoucherNoService.save(e);
        if (StringUtils.isNotBlank(byId.getVoucherCode())) {
            String[] entrySuccessCode = byId.getVoucherCode().split(",");
            boolean flag = true;
            for (String code : entrySuccessCode) {
                if (code.equals(voucherNumber)) {
                    flag = false;
                }
            }
            if (flag) {
                voucherNumber = byId.getVoucherCode() + "," + voucherNumber;
            } else {
                voucherNumber = byId.getVoucherCode();
            }
        }
        byId.setVoucherCode(voucherNumber);
        byId.setEntryState(entryState);
        byId.setEntryType("1");
        byId.setEntryDate(DateUtil.format(new Date(), DatePattern.NORM_DATE_PATTERN));

        if (!"1".equals(byId.getDeductible())) {//已认证勾选
            byId.setDeductible("0");
        }

        this.updateById(byId);
    }


    @Override
    public void voucherExtract() {
        List<InputInvoiceCustomsPushEntity> pushList = inputInvoiceCustomsPushService.list(new QueryWrapper<InputInvoiceCustomsPushEntity>()
                .eq("state", "0")
                .apply(" LENGTH(distribute_field)=18 "));
        if (pushList != null && pushList.size() > 0) {
            for (InputInvoiceCustomsPushEntity e : pushList) {
                String entryState = "1";
                if (new BigDecimal(e.getAmount()).compareTo(BigDecimal.ZERO) > 0) {//根据amout 判断入账或冲销
                    entryState = "1";
                } else {
                    entryState = "2";
                }
                String voucherNumber = e.getVoucherNumber();
                InputInvoiceCustomsEntity byId = getOne(new QueryWrapper<InputInvoiceCustomsEntity>()
                        .like("pay_no", e.getDistributeField()));
                InputInvoiceVoucherNoEntity voucherNo = new InputInvoiceVoucherNoEntity();
                voucherNo.setCreateDate(DateUtils.format(new Date()));
                voucherNo.setEntryState(entryState);
                voucherNo.setEntryType("1");
                voucherNo.setInvoiceCode(byId.getPayNo());
                voucherNo.setVoucherNumber(voucherNumber);
                inputInvoiceVoucherNoService.save(voucherNo);
                if (StringUtils.isNotBlank(byId.getVoucherCode())) {
                    String[] entrySuccessCode = byId.getVoucherCode().split(",");
                    boolean flag = true;
                    for (String code : entrySuccessCode) {
                        if (code.equals(voucherNumber)) {
                            flag = false;
                        }
                    }
                    if (flag) {
                        voucherNumber = byId.getVoucherCode() + "," + voucherNumber;
                    } else {
                        voucherNumber = byId.getVoucherCode();
                    }
                }
                byId.setVoucherCode(voucherNumber);
                byId.setEntryState(entryState);
                byId.setEntryType("1");
                byId.setEntryDate(DateUtil.format(new Date(), DatePattern.NORM_DATE_PATTERN));

                if (!"1".equals(byId.getDeductible())) {//已认证勾选
                    byId.setDeductible("0");
                }

                this.updateById(byId);
            }
        }
    }

    @Override
    public InputInvoiceCustomsEntity getPeriodById(String id) {
        String taxPeriod = "";
        InputInvoiceCustomsEntity byId = getById(id);
        String taxNo = byId.getPurchaserTaxNo();
        DeclareParamBody body = new DeclareParamBody();
        body.setTaxNo(taxNo);
        CallResult<DeclareInfo> result = baseClient.declareInfo(body);
        if (result.isSuccess()) {
            taxPeriod = result.getData().getTaxPeriod();
            byId.setDeductiblePeriod(taxPeriod);
        } else {
            byId.setPeriodErrorInfo(result.getExceptionResult().getMessage());
        }
        return byId;
    }

    @Override
    public PageUtils getListBySuccess(Map<String, Object> params) {
        params.put("deductible", "1");
        params.put("entryState", InputConstant.InvoiceMatch.MATCH_YES.getValue());
        return queryPage(params);
    }

    @Override
    public PageUtils getListByNo(Map<String, Object> params) {
        params.put("deductible", "1");
        params.put("entryState", InputConstant.InvoiceMatch.MATCH_NO.getValue());
        return queryPage(params);
    }

    @Override
    public PageUtils getListByError(Map<String, Object> params) {
        params.put("deductible", "1");
        params.put("entryState", InputConstant.InvoiceMatch.MATCH_ERROR.getValue());
        return queryPage(params);
    }

    /**
     * 手工入账
     *
     * @param params
     */
    @Override
    public String manualEntry(Map<String, Object> params) {
        String ids = params.get("ids").toString();
        String yearAndMonth = params.get("yearAndMonth").toString();
        String documentNo = params.get("documentNo").toString();
        //匹配组织是否一致
        InputInvoiceCustomsEntity customsEntity = getById(ids);
        SysDeptEntity sysDept = sysDeptService.getByName(customsEntity.getPurchaserName());

        boolean flag = true;
        List<InputInvoiceSapEntity> sapEntityList = new ArrayList<>();
        String[] documentNoList = documentNo.split("/");
        if (documentNoList.length > 1) {
            //一票多账的情况
            for (int i = 0; i < documentNoList.length; i++) {
                //匹配组织及信息是否一致
                InputInvoiceSapEntity sapEntity = inputInvoiceSapService.getEntityByNo(documentNoList[i], yearAndMonth, sysDept.getSapDeptCode());
                if (sapEntity != null) {
                    sapEntityList.add(sapEntity);
                } else {
                    flag = false;
                }
            }
        } else {
            InputInvoiceSapEntity sapEntity = inputInvoiceSapService.getEntityByNo(documentNo, yearAndMonth, sysDept.getSapDeptCode());
            sapEntityList.add(sapEntity);
        }
        if (flag) {
            customsEntity.setVoucherCode(documentNo);
            updateById(customsEntity);
            String type = saveByEntry(documentNo, sapEntityList);
            return type;
        } else {
            return "0";
        }
    }

    /**
     * 自动入账
     *
     * @param sapEntitys
     */
    @Override
    public InputInvoiceSapEntity updateByEntry(InputInvoiceSapEntity sapEntitys) {
        int updateNum = 0;
        if (sapEntitys.getText() != null) {
            List<String> list = disposeText(sapEntitys.getText());
            if (list.size() > 0) {
                updateNum = this.baseMapper.updateByVoucherCode(sapEntitys.getDocumentNo(), list);
            }
        }
        if (updateNum != 0) {
            List<InputInvoiceSapEntity> sapEntityList = new ArrayList<>();
            sapEntityList.add(sapEntitys);
            String type = saveByEntry(sapEntitys.getDocumentNo(), sapEntityList);
            sapEntitys.setSapMatch(type);
        } else {
            sapEntitys.setSapMatch("0");
        }
        return sapEntitys;
    }

    //导入text格式处理
    private List<String> disposeText(String text) {
        List<String> list = new ArrayList<String>();
        String arr[] = text.split(",");
        if (arr.length > 1) {
            for (int i = 1; i < arr.length; i++) {
                String arr2[] = arr[1].split("-");
                if (arr[i].length() == 3) {
                    list.add(arr2[0] + "-" + arr[i]);
                } else {
                    list.add(arr[i]);
                }
            }
        }
        return list;
    }

    // 入账
    public String saveByEntry(String documentNo, List<InputInvoiceSapEntity> sapEntityList) {
        //获取容差
        String value = sysConfigService.getValue("TOLERANCE_VALUE");
        BigDecimal valueTax = value != null ? new BigDecimal(value) : BigDecimal.ZERO;
        BigDecimal amountInLocal = BigDecimal.ZERO;
        for (int i = 0; i < sapEntityList.size(); i++) {
            amountInLocal = amountInLocal.add(sapEntityList.get(i).getAmountInLocal());
        }
        String totalTax = this.baseMapper.getCountByVoucherCode(documentNo);
        String type = InputConstant.InvoiceMatch.MATCH_NO.getValue();
        InputInvoiceCustomsEntity customsEntity = new InputInvoiceCustomsEntity();
        if (totalTax != null && (new BigDecimal(totalTax)).compareTo(BigDecimal.ZERO) == 0) {
            for (int i = 0; i < sapEntityList.size(); i++) {
                InputInvoiceSapEntity sapEntity = sapEntityList.get(i);
                sapEntity.setSapMatch(InputConstant.InvoiceMatch.MATCH_NO.getValue());
                inputInvoiceSapService.updateById(sapEntity);
            }
        } else if (totalTax != null && ((new BigDecimal(totalTax).subtract(valueTax)).compareTo(amountInLocal) == 0
                || (new BigDecimal(totalTax)).compareTo(amountInLocal.subtract(valueTax)) == 0
        )) {
            for (int i = 0; i < sapEntityList.size(); i++) {
                InputInvoiceSapEntity sapEntity = sapEntityList.get(i);
                sapEntity.setSapMatch(InputConstant.InvoiceMatch.MATCH_YES.getValue());
                inputInvoiceSapService.updateById(sapEntity);
            }
            type = InputConstant.InvoiceMatch.MATCH_YES.getValue();
        } else{
            for (int i = 0; i < sapEntityList.size(); i++) {
                InputInvoiceSapEntity sapEntity = sapEntityList.get(i);
                sapEntity.setSapMatch(InputConstant.InvoiceMatch.MATCH_ERROR.getValue());
                inputInvoiceSapService.updateById(sapEntity);
            }
            type = InputConstant.InvoiceMatch.MATCH_ERROR.getValue();
            if(totalTax != null){
                customsEntity.setSapCheckTax(((new BigDecimal(totalTax).subtract(valueTax)).subtract(amountInLocal).toString()));
            }
        }
        customsEntity.setEntryState(type);
        customsEntity.setMatchDate(DateUtils.format(new Date()));
        customsEntity.setEntryDate(sapEntityList.get(0).getPstngDate());
        customsEntity.setYearAndMonth(sapEntityList.get(0).getYearAndMonth());
        customsEntity.setSapTax(amountInLocal.toString());
        UpdateWrapper<InputInvoiceCustomsEntity> updateQueryWrapper = new UpdateWrapper();
        updateQueryWrapper.eq("voucher_code", documentNo);
        baseMapper.update(customsEntity, updateQueryWrapper);
        return type;
    }

    /**
     * 获取查询月份认证完成的数据
     *
     * @param params
     * @return
     */
    @Override
    public List<InputInvoiceCustomsEntity> getCertification(Map<String, Object> params) {
        String date = params.get("yearAndMonth").toString();
        String deptId = params.get("deptId").toString();
        SysDeptEntity deptEntity = sysDeptService.getById(deptId);
        return this.list(
                new QueryWrapper<InputInvoiceCustomsEntity>()
                        .like("deductible_date", date)
                        .in("deductible", 1)
                        .eq("purchaser_name", deptEntity.getName())
        );
    }

    public static void main(String[] args) {
/*        List<String> list = new ArrayList<String>();
        String text = "Import,010120201000514950-A01,L02,97500431-00100Xi";
        String arr[]=text.split(",");
        if(arr.length > 1){
            for (int i = 1;i < arr.length;i++){
                String arr2[]=arr[1].split("-");
                if(arr[i].length() == 3){
                    list.add(arr2[0] +"-"+ arr[i]);
                }else{
                    list.add(arr[i]);
                }
            }
        }
        System.out.println(list.toString());*/


    }
}

