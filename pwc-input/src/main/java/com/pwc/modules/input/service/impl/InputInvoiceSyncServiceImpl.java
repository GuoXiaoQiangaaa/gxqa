package com.pwc.modules.input.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fapiao.neon.client.in.*;
import com.fapiao.neon.model.CallResult;
import com.fapiao.neon.model.Entity;
import com.fapiao.neon.model.in.*;
import com.fapiao.neon.model.in.inspect.BaseInvoice;
import com.fapiao.neon.param.in.*;
import com.pwc.common.annotation.DataFilter;
import com.pwc.common.utils.Constant;
import com.pwc.common.utils.InputConstant;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;
import com.pwc.modules.input.dao.InputInvoiceSyncDao;
import com.pwc.modules.input.entity.InputCompanyEntity;
import com.pwc.modules.input.entity.InputInvoiceEntity;
import com.pwc.modules.input.entity.InputInvoiceSyncEntity;
import com.pwc.modules.input.service.*;
import com.pwc.modules.sys.entity.SysDeptEntity;
import com.pwc.modules.sys.service.SysConfigService;
import com.pwc.modules.sys.service.SysDeptService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service("invoiceSyncService")
public class InputInvoiceSyncServiceImpl extends ServiceImpl<InputInvoiceSyncDao, InputInvoiceSyncEntity> implements InputInvoiceSyncService {

    @Autowired
    private InputInvoiceService invoiceService;

    @Resource
    private CollectClient collectClient;

    @Resource
    private CheckInvoiceClient checkInvoiceClient;

    @Resource
    private DeductClient deductClient;

    @Resource
    private StatisticsClient statisticsClient;

    @Resource
    private ConfirmClient confirmClient;

    @Autowired
    private SysDeptService sysDeptService;

    @Autowired
    private InputCompanyService companyService;

    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private InputInvoiceBatchService invoiceBatchService;
    @Autowired
    private CollectWholeClient collectWholeClient;
    @Autowired
    private InputInvoiceTaxationService inputInvoiceTaxationService;

    private Log log = Log.get(this.getClass());

    @Override
    @DataFilter(subDept = true, user = false)
    public PageUtils queryPage(Map<String, Object> params, InputInvoiceSyncEntity invoiceSyncEntity) {
        String billingDateArray = (String) params.get("billingDate");
        String deductibleDateArray = (String) params.get("deductibleDate");

        // 替换原来的公司查询数据权限
        List<String> taxCodes = null;
        if (!StrUtil.isBlankIfStr(params.get(Constant.SQL_FILTER))) {
            taxCodes = sysDeptService.getTaxCodeByIds(params);
        }
        IPage<InputInvoiceSyncEntity> page = this.page(
                new Query<InputInvoiceSyncEntity>().getPage(params, null, true),
                new QueryWrapper<InputInvoiceSyncEntity>()
                        .in(CollUtil.isNotEmpty(taxCodes), "purchaser_tax_no", taxCodes)
                        .eq(StrUtil.isNotBlank(invoiceSyncEntity.getState()), "state", invoiceSyncEntity.getState())
                        .eq(StrUtil.isNotBlank(invoiceSyncEntity.getDeductibleMode()), "deductible_mode", invoiceSyncEntity.getDeductibleMode() )
                        .eq(StrUtil.isNotBlank(invoiceSyncEntity.getInvoiceCode()), "invoice_code", invoiceSyncEntity.getInvoiceCode())
                        .eq(StrUtil.isNotBlank(invoiceSyncEntity.getDeductibleType()), "deductible_type", invoiceSyncEntity.getDeductibleType())
                .eq(invoiceSyncEntity.getInvoiceNumber() !=null && !"".equals(invoiceSyncEntity.getInvoiceNumber()),"invoice_number", invoiceSyncEntity.getInvoiceNumber())
                .eq(invoiceSyncEntity.getPurchaserName() !=null && !"".equals(invoiceSyncEntity.getPurchaserName()),"purchaser_name", invoiceSyncEntity.getPurchaserName())
                .eq(invoiceSyncEntity.getPurchaserTaxNo() !=null && !"".equals(invoiceSyncEntity.getPurchaserTaxNo()),"purchaser_tax_no", invoiceSyncEntity.getPurchaserTaxNo())
                .eq(invoiceSyncEntity.getDeductible() !=null && !"".equals(invoiceSyncEntity.getDeductible()),"deductible", invoiceSyncEntity.getDeductible())
                .eq(invoiceSyncEntity.getSalesTaxName() !=null && !"".equals(invoiceSyncEntity.getSalesTaxName()),"sales_tax_name", invoiceSyncEntity.getSalesTaxName())
                .eq(invoiceSyncEntity.getDeductible() !=null && !"".equals(invoiceSyncEntity.getDeductible()),"deductible", invoiceSyncEntity.getDeductible())
                .eq(invoiceSyncEntity.getDeductiblePeriod() !=null && !"".equals(invoiceSyncEntity.getDeductiblePeriod()),"deductible_period", invoiceSyncEntity.getDeductiblePeriod())
                .ge(StringUtils.isNotBlank(deductibleDateArray),"deductible_date",!StringUtils.isNotBlank(deductibleDateArray)?"":deductibleDateArray.split(",")[0])
                .le(StringUtils.isNotBlank(deductibleDateArray),"deductible_date",!StringUtils.isNotBlank(deductibleDateArray)?"":deductibleDateArray.split(",")[1])
                .ge(StringUtils.isNotBlank(billingDateArray),"billing_date",!StringUtils.isNotBlank(billingDateArray)?"":billingDateArray.split(",")[0])
                .le(StringUtils.isNotBlank(billingDateArray),"billing_date",!StringUtils.isNotBlank(billingDateArray)?"":billingDateArray.split(",")[1])

        );

        return new PageUtils(page);
    }

    @Override
    public InputInvoiceSyncEntity queryInvoiceSync(Map<String, Object> params) {
        String invoiceEntity = (String) params.get("invoiceEntity");
        String invoiceNumber = (String) params.get("invoiceNumber");
        String invoiceCode = (String) params.get("invoiceCode");
        String invoiceCreateDate = (String) params.get("invoiceCreateDate");
        String invoiceFreePrice = (String) params.get("invoiceFreePrice");
        String invoiceCheckCode = (String) params.get("invoiceCheckCode");
        InputInvoiceSyncEntity invoiceSyncEntity = this.getOne(
                new QueryWrapper<InputInvoiceSyncEntity>()
//                        .eq(StringUtils.isNotBlank(invoiceEntity), "invoice_type", invoiceEntity)
                        .eq(StringUtils.isNotBlank(invoiceNumber), "invoice_number", invoiceNumber)
                        .eq(StringUtils.isNotBlank(invoiceCode), "invoice_code", invoiceCode)
                        .eq(StringUtils.isNotBlank(invoiceCreateDate), "billing_date", invoiceCreateDate)
                        .eq(StringUtils.isNotBlank(invoiceFreePrice), "total_amount", invoiceFreePrice)
                        .eq(StringUtils.isNotBlank(invoiceCheckCode), "check_code", invoiceCheckCode)
        );

        return invoiceSyncEntity;
    }

    @Override
    public void sync() {
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat(
                "yyyy-MM");//设置日期格式
        String dateString = df.format(date);
        String nsrsbh = "91310000607378229C";
        List<InputInvoiceSyncEntity> invoiceSyncEntities = invoiceService.getSkssqEntity(nsrsbh,dateString);
        for ( InputInvoiceSyncEntity entity: invoiceSyncEntities) {
            baseMapper.insert(entity);
        }
    }

    /**
     * 发票同步接口
     */
    @Override
    public CallResult<SyncInvoiceInfo> invoiceSync(SyncInvoiceParamBody syncInvoiceParamBody) {
        CallResult<SyncInvoiceInfo> results = collectClient.invoiceSync(syncInvoiceParamBody);
        List<? extends SyncInvoice> invoiceSyncList = results.getData().getInvoices();
        for (SyncInvoice entity: invoiceSyncList) {
            InputInvoiceSyncEntity invoiceSyncEntity = new InputInvoiceSyncEntity();
            invoiceSyncEntity.setAbnormalType(entity.getAbnormalType());
            invoiceSyncEntity.setAgencyDrawback(entity.getAgencyDrawback());
            invoiceSyncEntity.setBillingDate(entity.getBillingDate());
            invoiceSyncEntity.setCheckCode(entity.getCheckCode());
            invoiceSyncEntity.setCheckDate(entity.getDeductibleDate());
            invoiceSyncEntity.setDeductible(entity.getDeductible());
            invoiceSyncEntity.setDeductibleDate(entity.getDeductibleDate());
            invoiceSyncEntity.setDeductibleMode(entity.getDeductibleMode());
            invoiceSyncEntity.setDeductiblePeriod(entity.getDeductiblePeriod());
            invoiceSyncEntity.setDeductibleType(entity.getDeductibleType());
            invoiceSyncEntity.setCheckStatus(entity.getDeductible());
            invoiceSyncEntity.setEffectiveTaxAmount(entity.getEffectiveTaxAmount());
            invoiceSyncEntity.setInfoSources(entity.getInfoSources());
            invoiceSyncEntity.setInvoiceCode(entity.getInvoiceCode());
            invoiceSyncEntity.setInvoiceNumber(entity.getInvoiceNumber());
            invoiceSyncEntity.setInvoiceType(entity.getInvoiceType());
            invoiceSyncEntity.setManagementStatus(entity.getManagementStatus());
            invoiceSyncEntity.setOverdueCheckMark(entity.getOverdueCheckMark());
            invoiceSyncEntity.setPurchaserName(entity.getPurchaserName());
            invoiceSyncEntity.setPurchaserTaxNo(entity.getPurchaserTaxNo());
            invoiceSyncEntity.setResaleCertificateNumber(entity.getResaleCertificateNumber());
            invoiceSyncEntity.setSalesTaxName(entity.getSalesTaxName());
            invoiceSyncEntity.setSalesTaxNo(entity.getSalesTaxNo());
            invoiceSyncEntity.setState(entity.getState());
            invoiceSyncEntity.setTotalAmount(entity.getTotalAmount());
            invoiceSyncEntity.setTotalTax(entity.getTotalTax());
            invoiceSyncEntity.setValidTax(entity.getEffectiveTaxAmount());
//            baseMapper.insert(invoiceSyncEntity);
        }
        return results;
    }

    /**
     * 底账库接口
     */
    @Override
    public InputInvoiceSyncEntity findInvoiceSync(InputInvoiceEntity invoiceEntity) {
        InputInvoiceSyncEntity invoiceSyncEntity = this.getOne(
                new QueryWrapper<InputInvoiceSyncEntity>()
//                        .eq(StringUtils.isNotBlank(invoiceEntity.getInvoiceEntity()), "invoice_type", invoiceEntity.getInvoiceEntity())
                        .eq(StringUtils.isNotBlank(invoiceEntity.getInvoiceNumber()), "invoice_number", invoiceEntity.getInvoiceNumber())
                        .eq(StringUtils.isNotBlank(invoiceEntity.getInvoiceCode()), "invoice_code", invoiceEntity.getInvoiceCode())
                        .eq(StringUtils.isNotBlank(invoiceEntity.getInvoiceCreateDate()), "billing_date", invoiceEntity.getInvoiceCreateDate())
                        .eq(StringUtils.isNotBlank(null != invoiceEntity.getInvoiceFreePrice() ? invoiceEntity.getInvoiceFreePrice().toString() : null), "total_amount", invoiceEntity.getInvoiceFreePrice())
                        .eq(StringUtils.isNotBlank(invoiceEntity.getInvoiceCheckCode()), "check_code", invoiceEntity.getInvoiceCheckCode())
        );
        return invoiceSyncEntity;
    }

    /**
     * 单票查验接口
     */
    @Override
    public CallResult<BaseInvoice> invoiceCheck(InvoiceInspectionParamBody invoiceInspectionParamBody) {
        CallResult<BaseInvoice> results = checkInvoiceClient.check(invoiceInspectionParamBody);
        return results;
    }

    /**
     * 异步底账勾选、勾选撤销接口
     */
    @Override
    public CallResult<ApplyDeductResultInfo> deductInvoices(DeductParamBody deductParamBody) {
        CallResult<ApplyDeductResultInfo> results = deductClient.deductInvoices(deductParamBody);
        return results;
    }

    /**
     * 获取勾选结果接口
     */
    @Override
    public CallResult<DeductResultListInfo> deductResult(CommonParamBody commonParamBody) {
        CallResult<DeductResultListInfo> results = deductClient.deductResult(commonParamBody);
        return results;
    }

    /**
     * 保存发票同步
     */
    @Override
    public void saveInvoice(List<? extends SyncInvoice> invoiceSyncList) {
        for (SyncInvoice entity: invoiceSyncList) {
            List<InputInvoiceSyncEntity> list = this.list(new QueryWrapper<InputInvoiceSyncEntity>()
                    .eq("invoice_code", entity.getInvoiceCode())
                    .eq("invoice_number", entity.getInvoiceNumber()));

            if (CollUtil.isNotEmpty(list)) {
                log.info("已同步发票：" + entity.getInvoiceCode() + "---" + entity.getInvoiceNumber());
                continue;
            }
            InputInvoiceSyncEntity invoiceSyncEntity = new InputInvoiceSyncEntity();
            invoiceSyncEntity.setAbnormalType(entity.getAbnormalType());
            invoiceSyncEntity.setAgencyDrawback(entity.getAgencyDrawback());
            invoiceSyncEntity.setBillingDate(entity.getBillingDate());
            invoiceSyncEntity.setCheckCode(entity.getCheckCode());
            invoiceSyncEntity.setCheckDate(entity.getDeductibleDate());
            invoiceSyncEntity.setDeductible(entity.getDeductible());
            invoiceSyncEntity.setDeductibleDate(entity.getDeductibleDate());
            invoiceSyncEntity.setDeductibleMode(entity.getDeductibleMode());
            invoiceSyncEntity.setDeductiblePeriod(entity.getDeductiblePeriod());
            invoiceSyncEntity.setDeductibleType(entity.getDeductibleType());
            invoiceSyncEntity.setCheckStatus(entity.getDeductible());
            invoiceSyncEntity.setEffectiveTaxAmount(entity.getEffectiveTaxAmount());
            invoiceSyncEntity.setInfoSources(entity.getInfoSources());
            invoiceSyncEntity.setInvoiceCode(entity.getInvoiceCode());
            invoiceSyncEntity.setInvoiceNumber(entity.getInvoiceNumber());
            invoiceSyncEntity.setInvoiceType(entity.getInvoiceType());
            invoiceSyncEntity.setManagementStatus(entity.getManagementStatus());
            invoiceSyncEntity.setOverdueCheckMark(entity.getOverdueCheckMark());
            invoiceSyncEntity.setPurchaserName(entity.getPurchaserName());
            invoiceSyncEntity.setPurchaserTaxNo(entity.getPurchaserTaxNo());
            invoiceSyncEntity.setResaleCertificateNumber(entity.getResaleCertificateNumber());
            invoiceSyncEntity.setSalesTaxName(entity.getSalesTaxName());
            invoiceSyncEntity.setSalesTaxNo(entity.getSalesTaxNo());
            invoiceSyncEntity.setState(entity.getState());
            invoiceSyncEntity.setTotalAmount(entity.getTotalAmount());
            invoiceSyncEntity.setTotalTax(entity.getTotalTax());
            invoiceSyncEntity.setValidTax(entity.getEffectiveTaxAmount());
            baseMapper.insert(invoiceSyncEntity);
        }
    }

    @Override
    public String statistics(StatisticsParamBody statisticsParamBody) {
        String requestId = null;
        CallResult<Entity> result = statisticsClient.statistics(statisticsParamBody);
        String statisticsType = statisticsParamBody.getStatisticsType();//1 申请统计 2 撤销统计
        if (null != result && result.isSuccess()) {
            log.info("statistics 申请统计："+ result.toString());
            requestId = result.getData().getRequestId();

            SysDeptEntity deptEntity = sysDeptService.getByTaxCode(statisticsParamBody.getTaxNo());
            InputCompanyEntity companyEntity = companyService.getByDeptId(deptEntity.getDeptId());
            if (null != companyEntity) {
                companyEntity.setRequestId(requestId);
                companyEntity.setApplyResult(statisticsType);
                companyEntity.setStatus(statisticsType);
                companyService.update(companyEntity);
            }
        }
        log.info("requestId:" + requestId);
        return requestId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void statisticsResult() {
        String[] status = {"1", "2"};
        List<InputCompanyEntity> companyEntityList = companyService.list(new QueryWrapper<InputCompanyEntity>()
                //.eq("apply_result", 0))
                .isNotNull("status")
                .in("status",status));
        for (InputCompanyEntity companyEntity : companyEntityList) {
            CommonParamBody commonParamBody = new CommonParamBody();
            commonParamBody.setRequestId(companyEntity.getRequestId());
            CallResult<StatisticsResultInfo> callResult = statisticsClient.statisticsResult(commonParamBody);
            if (null != callResult && callResult.isSuccess()) {
                log.info("方法statisticsResult 统计结果："+ callResult.toString());
                StatisticsResultInfo resultInfo = callResult.getData();
                if ("1".equals(resultInfo.getResult())) {
                    companyEntity.setApplyResult(resultInfo.getResultStatus());
                    companyEntity.setStatisticsMonth(resultInfo.getStatisticsMonth());
                    companyEntity.setStatisticsTime(resultInfo.getStatisticsTime());
                    if ("1".equals(resultInfo.getStatisticsType())){//申请统计
                        companyEntity.setStatus("3");//以获取统计结果
                        //统计信息
                        List<StatisticsDetailsInfo> details = resultInfo.getDetails();
                        if (details!=null&&details.size()>0) {
                            this.baseMapper.insertDetailsBatch(companyEntity, details);
                        }
                        //差异发票合集
                        List<StatisticsInvoiceInfo> differenceInvoices = resultInfo.getDifferenceInvoices();
                        if (differenceInvoices!=null&&differenceInvoices.size()>0){
                            this.baseMapper.insertDifferenceBatch(companyEntity,differenceInvoices);
                        }
                        //统计发票合集
                        List<StatisticsInvoiceInfo> invoices=resultInfo.getInvoices();
                        if (invoices!=null&&invoices.size()>0) {
                            this.baseMapper.insertInvoicesBatch(companyEntity, invoices);
                        }
                    }else if("2".equals(resultInfo.getStatisticsType())){//撤销统计
                        companyEntity.setStatus("0");//撤销完成status变为0，未开始状态
                        this.baseMapper.deleteDetailsByCompanyId(companyEntity.getId());
                        this.baseMapper.deleteDifferenceByCompanyId(companyEntity.getId());
                        this.baseMapper.deleteInvoicesByCompanyId(companyEntity.getId());
                    }
                    companyService.update(companyEntity);
                }
            }
        }
    }

    @Override
    public String applyConfirm(ConfirmParamBody confirmParamBody) {
        String requestId = null;
        CallResult<Entity> result = confirmClient.confirm(confirmParamBody);
        if (null != result && result.isSuccess()) {
            log.info("申请统计确认："+ result.toString());
            requestId = result.getData().getRequestId();
            SysDeptEntity deptEntity = sysDeptService.getByTaxCode(confirmParamBody.getTaxNo());
            InputCompanyEntity companyEntity = companyService.getByDeptId(deptEntity.getDeptId());
            if (null != companyEntity) {
                companyEntity.setRequestId(requestId);
                companyEntity.setStatus("4");
                companyService.update(companyEntity);
            }
        }
        log.info("reqeustId:"+ requestId);
        return requestId;
    }

    @Override
    public void confirmResult() {
        List<InputCompanyEntity> companyEntityList = companyService.list(new QueryWrapper<InputCompanyEntity>()
                //.eq("apply_result", 0)
                .eq("status", 4));
        for (InputCompanyEntity companyEntity : companyEntityList) {
            CommonParamBody commonParamBody = new CommonParamBody();
            commonParamBody.setRequestId(companyEntity.getRequestId());
            CallResult<ConfirmResultInfo> callResult = confirmClient.confirmResult(commonParamBody);
            if (null != callResult && callResult.isSuccess()) {
                ConfirmResultInfo resultInfo = callResult.getData();
                if ("1".equals(resultInfo.getConfirmResult())) {
                    companyEntity.setApplyResult(resultInfo.getResultStatus());
                    companyEntity.setStatus("6");
                    companyService.update(companyEntity);
                }
            }
        }
    }

    @Override
    public void deductInvoices(String invoiceIds, String deductType) {
        String[] invoiceArray = invoiceIds.split(",");
        for (String invoiceId : invoiceArray) {
            InputInvoiceEntity invoiceEntity = invoiceService.getById(invoiceId);
            deductInvoices(invoiceEntity,deductType);
        }
    }

    @Override
    public void deductInvoices(List<InputInvoiceEntity> invoiceList, String deductType) {
        for (InputInvoiceEntity invoice : invoiceList) {
            deductInvoices(invoice, deductType);
        }
    }

    @Override
    public void deductResult() {
        String status = InputConstant.InvoiceStatus.CERTIFICATION.getValue();
        List<InputInvoiceEntity> invoiceEntityList = invoiceService.list(new QueryWrapper<InputInvoiceEntity>().isNotNull("request_id").eq("invoice_status", status));
        for (InputInvoiceEntity invoice:invoiceEntityList) {
            CommonParamBody commonParamBody = new CommonParamBody();
            commonParamBody.setRequestId(invoice.getRequestId());
            CallResult<DeductResultListInfo> result = deductClient.deductResult(commonParamBody);
            if (null != result) {
                log.info("获取勾选结果：{0}" + result.toString());
                if (result.isSuccess()) {
                    List<DeductResultInfo> deductResultInfoList = result.getData().getInvoices();
                    for (DeductResultInfo deductResultInfo : deductResultInfoList) {
                        if ("1".equals(deductResultInfo.getDeductibleResult())) {
                            // 1 勾选 6 撤销勾选
                            if ("1".equals(deductResultInfo.getDeductType())) {
                                invoice.setInvoiceStatus(InputConstant.InvoiceStatus.SUCCESSFUL_AUTHENTICATION.getValue());
                                invoice.setVerfy(Boolean.TRUE);
                                // 认证成功同步到数据库
                                inputInvoiceTaxationService.updateTaxation(invoice);
                            } else if ("6".equals(deductResultInfo.getDeductType())) {
                                invoice.setInvoiceStatus(InputConstant.InvoiceStatus.UNDO_CERTIFICATION.getValue());
                                invoice.setVerfy(Boolean.FALSE);
                            }
                        } else {
                            invoice.setInvoiceStatus(InputConstant.InvoiceStatus.AUTHENTICATION_FAILED.getValue());
                            invoice.setInvoiceErrorDescription(deductResultInfo.getDeductibleResultMsg());
                        }
                    }
                    invoiceService.update(invoice);
                }
            } else {
                invoice.setInvoiceStatus(InputConstant.InvoiceStatus.AUTHENTICATION_FAILED.getValue());
                invoice.setInvoiceErrorDescription("获取勾选结果未知异常");
                log.info("获取勾选结果失败，未返回结果信息");
            }
            // 更新批次状态
            invoiceBatchService.updateStatus(invoice.getBatchNumber());
        }
    }

    /**
     * 勾选
     * @param invoiceEntity
     * @param deductType 1 勾选 6 撤销勾选
     * @return
     */
    @Override
    public String deductInvoices(InputInvoiceEntity invoiceEntity, String deductType) {
        if (StrUtil.isBlank(deductType)) {
            deductType = "1";
        }
//        String invoiceStatus;
//        if ("1".equals(deductType)) {
//            invoiceStatus = "40";
//        } else {
//            invoiceStatus = "50";
//        }
        String requestId = null;
        // 可配置申报期
        String period = sysConfigService.getValue("DEDUCT_PERIOD");
        DeductParamBody deductParamBody = new DeductParamBody();
        deductParamBody.setTaxNo(invoiceEntity.getInvoicePurchaserParagraph());
        deductParamBody.setDeductType(deductType);
        deductParamBody.setPeriod(period);
        DeductInvoice deductInvoice = new DeductInvoice();
        deductInvoice.setInvoiceCode(invoiceEntity.getInvoiceCode());
        deductInvoice.setInvoiceNumber(invoiceEntity.getInvoiceNumber());
        deductInvoice.setValidTax(invoiceEntity.getInvoiceTaxPrice().toPlainString());
        List<DeductInvoice> invoices = new ArrayList<>();
        invoices.add(deductInvoice);
        deductParamBody.setInvoices(invoices);
        CallResult<ApplyDeductResultInfo> results = deductInvoices(deductParamBody);

        if (null != results) {
            log.info("勾选结果：" + results.toString());
            if (results.isSuccess()) {
                requestId = results.getData().getRequestId();
                invoiceEntity.setRequestId(requestId);
                invoiceEntity.setInvoiceStatus(InputConstant.InvoiceStatus.CERTIFICATION.getValue());
            } else {
                invoiceEntity.setInvoiceStatus(InputConstant.InvoiceStatus.AUTHENTICATION_FAILED.getValue());
                invoiceEntity.setInvoiceErrorDescription(results.getExceptionResult().getMessage());
            }
        } else {
            log.info("勾选结果：未知异常无返回结果" );
            invoiceEntity.setInvoiceStatus(InputConstant.InvoiceStatus.AUTHENTICATION_FAILED.getValue());
            invoiceEntity.setInvoiceErrorDescription("未知异常");
        }
        invoiceService.update(invoiceEntity);
        invoiceBatchService.updateStatus(invoiceEntity.getBatchNumber());
        log.info("requestId:" + requestId);
        return requestId;
    }


    @Override
    public void checkInvoice() {
        // 验真成功发票
        List<InputInvoiceEntity> invoiceList = invoiceService.getByStatus(InputConstant.InvoiceStatus.PENDING_MATCHED.getValue());
        for (InputInvoiceEntity inoice : invoiceList) {
            InvoiceInspectionParamBody invoiceInspectionParamBody = new InvoiceInspectionParamBody();
            String checkcode = inoice.getInvoiceCheckCode();
            if (StrUtil.isNotBlank(checkcode)) {
                invoiceInspectionParamBody.setCheckCode(StrUtil.subWithLength(checkcode, checkcode.length() - 6, checkcode.length()));
            }
            invoiceInspectionParamBody.setTotalAmount(inoice.getInvoiceFreePrice().toString());
            invoiceInspectionParamBody.setBillingDate(inoice.getInvoiceCreateDate());
            invoiceInspectionParamBody.setInvoiceNumber(inoice.getInvoiceNumber());
            invoiceInspectionParamBody.setInvoiceCode(inoice.getInvoiceCode());

            CallResult<BaseInvoice> invoiceCheck = this.invoiceCheck(invoiceInspectionParamBody); // 验真接口

            log.info("验真返回结果  checkInvoice ：" + invoiceCheck.toString());
            if (invoiceCheck.isSuccess()) {
                if ("2".equals(invoiceCheck.getData().getState())) {
                    // 异常：作废
                    inoice.setInvoiceStatus(InputConstant.InvoiceStatus.INVALID.getValue());
                } else if ("3".equals(invoiceCheck.getData().getState())) {
                    // 异常：红冲
                    inoice.setInvoiceStatus(InputConstant.InvoiceStatus.REVERSE.getValue());
                }
                invoiceService.update(inoice);
            }

            invoiceBatchService.updateStatus(inoice.getBatchNumber());
        }
    }

    @Override
    public CallResult<Page> invoiceCollection(InvoiceCollectionParamBody invoiceCollectionParamBody) {
        return collectWholeClient.invoiceCollection(invoiceCollectionParamBody);
    }
}
