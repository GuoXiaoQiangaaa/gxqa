package com.pwc.modules.input.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fapiao.neon.client.in.*;
import com.fapiao.neon.model.CallResult;
import com.fapiao.neon.model.in.*;
import com.fapiao.neon.model.in.inspect.BaseInvoice;
import com.fapiao.neon.param.in.*;
import com.pwc.common.annotation.DataFilter;
import com.pwc.common.exception.RRException;
import com.pwc.common.utils.DateUtils;
import com.pwc.common.utils.InputConstant;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;
import com.pwc.modules.input.dao.InputInvoiceSyncDao;
import com.pwc.modules.input.entity.InputCompanyEntity;
import com.pwc.modules.input.entity.InputInvoiceCustomsEntity;
import com.pwc.modules.input.entity.InputInvoiceEntity;
import com.pwc.modules.input.entity.InputInvoiceSyncEntity;
import com.pwc.modules.input.enums.AuthCountStatusEnum;
import com.pwc.modules.input.service.*;
import com.pwc.modules.sys.entity.SysDeptEntity;
import com.pwc.modules.sys.service.SysConfigService;
import com.pwc.modules.sys.service.SysDeptService;
import com.pwc.modules.sys.shiro.ShiroUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;


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
    @Autowired
    private InputInvoiceCustomsService inputInvoiceCustomsService;
    @Autowired
    private InputInvoiceService inputInvoiceService;

    private Log log = Log.get(this.getClass());

    @Override
    @DataFilter(subDept = true, user = false)
    public PageUtils queryPage(Map<String, Object> params, InputInvoiceSyncEntity invoiceSyncEntity) {
        // 所属公司
        String originalPeriod = (String) params.get("originalPeriod"); // 认证所属期
        String invoiceNumber = (String) params.get("invoiceNumber"); // 发票号码
        String purchaserName =(String) params.get("purchaserName"); // 购方名称
        String salesTaxName =(String) params.get("salesTaxName");
        String deductible = (String) params.get("deductible"); //
        String companyId = (String) params.get("companyId");
        // 认证状态
        SysDeptEntity deptEntity = new SysDeptEntity();
        if (StringUtils.isNotBlank(companyId)) {
            deptEntity = sysDeptService.getById(Integer.valueOf(companyId));
        }
        IPage<InputInvoiceSyncEntity> page = this.page(
                new Query<InputInvoiceSyncEntity>().getPage(params, null, true),
                new QueryWrapper<InputInvoiceSyncEntity>()
                        .orderByDesc("billing_date")
                        .eq(StringUtils.isNotBlank(originalPeriod), "deductible_period", originalPeriod) //所属期
                        .like(StringUtils.isNotBlank(invoiceNumber), "invoice_number", invoiceNumber) // 发票号码
                        .like(StringUtils.isNotBlank(purchaserName), "purchaser_name", purchaserName) // 购方名称
                        .like(StringUtils.isNotBlank(salesTaxName), "sales_tax_name", salesTaxName) // 销方名称
                        .eq(StringUtils.isNotBlank(deductible), "deductible", deductible) // 认证状态
                        .eq(StringUtils.isNotBlank(deptEntity.getTaxCode()), "purchaser_tax_no", deptEntity.getTaxCode())
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
            baseMapper.insert(invoiceSyncEntity);
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
    @Transactional(rollbackFor = Exception.class)
    public void saveInvoice(List<? extends SyncInvoice> invoiceSyncList) {
        for (SyncInvoice entity: invoiceSyncList) {
            this.remove(new QueryWrapper<InputInvoiceSyncEntity>()
                    .eq("invoice_code", entity.getInvoiceCode())
                    .eq("invoice_number", entity.getInvoiceNumber()));
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
            invoiceSyncEntity.setCheckStatus(entity.getState());
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
            invoiceSyncEntity.setOriginalPeriod(entity.getOriginalPeriod());
            invoiceSyncEntity.setStatus("1");
            invoiceSyncEntity.setCreateTime(new Date());
            baseMapper.insert(invoiceSyncEntity);
        }
    }

    /**
     * 申请/撤销统计
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void statistics(Map<String, Object> params) {
        // 1:申请统计; 2:撤销统计
        String type = (String) params.get("type");
        // 税号列表
        String taxNoStrs = (String) params.get("taxNoList");
        List<String> taxNoList = Arrays.asList(taxNoStrs.split(","));
        // 参数校验
        if (StrUtil.isBlank(type)) {
            type = "1";
        }
        if (CollectionUtil.isEmpty(taxNoList)) {
            throw new RRException("税号不能为空");
        }
        for (String taxNo : taxNoList) {
            try{
                SysDeptEntity deptEntity = sysDeptService.getByTaxCode(taxNo);
                if (null == deptEntity) {
                    throw new RRException("未查询到税号为:" + taxNo +"的企业信息,请核对税号是否准确");
                }
                InputCompanyEntity companyEntity = companyService.getByDeptId(deptEntity.getDeptId());
                if (null == companyEntity) {
                    throw new RRException("未查询到税号为:" + taxNo +"的企业进项信息");
                }
                /** 测试使用开始 */

                if("1".equals(type)){
                    companyEntity.setStatus(AuthCountStatusEnum.APPLYSUCCESS.getKey());
                }else {
                    companyEntity.setStatus(AuthCountStatusEnum.NOTCOUNT.getKey());
                }
                companyEntity.setRequestId(UUID.randomUUID().toString());
                companyEntity.setUpdateBy(String.valueOf(ShiroUtils.getUserId()));
                companyEntity.setUpdateTime(new Date());
                companyService.updateById(companyEntity);

                /** 测试使用结束 */

                /*StatisticsParamBody statisticsParamBody = new StatisticsParamBody();
                statisticsParamBody.setStatisticsType(type);
                statisticsParamBody.setTaxNo(taxNo);
                // 调用申请/撤销统计接口
                CallResult<Entity> result = statisticsClient.statistics(statisticsParamBody);
                if (null != result) {
                    if(result.isSuccess()){
                        log.info("申请/撤销统计中：{}" + result.getData().getRequestId());

                        if("1".equals(type)){
                            companyEntity.setStatus(AuthCountStatusEnum.APPLYING.getKey());
                        }else {
                            companyEntity.setStatus(AuthCountStatusEnum.CANCELLING.getKey());
                        }
                        companyEntity.setRequestId(result.getData().getRequestId());
                        companyEntity.setUpdateBy(String.valueOf(ShiroUtils.getUserId()));
                        companyEntity.setUpdateTime(new Date());
                        companyService.updateById(companyEntity);
                    }else {
                        String code = result.getExceptionResult().getCode();
                        String message = result.getExceptionResult().getMessage();
                        String requestId = result.getExceptionResult().getRequestId();
                        log.info("申请/撤销统计失败: code:{}, message:{}", code, message);
                        // 当前税号正在处理中,请稍后再试
                        if(!"OperationProcessing".equalsIgnoreCase(code)){
                            if("1".equals(type)){
                                companyEntity.setStatus(AuthCountStatusEnum.APPLYFAILED.getKey());
                                companyEntity.setRequestId(requestId);
                                companyEntity.setUpdateBy(String.valueOf(ShiroUtils.getUserId()));
                                companyEntity.setUpdateTime(new Date());
                                companyService.updateById(companyEntity);
                            }
                        }
                        // throw new RRException("统计失败: " + message);
                    }
                }else {
                    log.error("申请/撤销统计税局接口无响应");
                    throw new RRException("接口无响应,请稍后重试");
                }*/
            } catch (RRException e){
                log.error("参数有误: {}", e);
                throw e;
            } catch (Exception e){
                log.error("统计出错: {}", e);
                throw new RRException("统计发生异常");
            }
        }
    }

    /**
     * 获取统计结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void statisticsResult() {
        // 1:申请统计中; 7:统计撤销中
        String[] status = {"1", "7"};
        List<InputCompanyEntity> companyEntityList = companyService.list(
                new QueryWrapper<InputCompanyEntity>()
                    .isNotNull("status")
                    .in("status", Arrays.asList(status))
        );
        if(CollectionUtil.isEmpty(companyEntityList)){
            return;
        }
        for (InputCompanyEntity companyEntity : companyEntityList) {
            CommonParamBody commonParamBody = new CommonParamBody();
            commonParamBody.setRequestId(companyEntity.getRequestId());
            // 调用获取申请/撤销统计结果接口
            CallResult<StatisticsResultInfo> callResult = statisticsClient.statisticsResult(commonParamBody);
            if (null != callResult) {
                if(callResult.isSuccess()){
                    log.info("获取申请/撤销统计结果成功：{}"+ callResult.getData().getRequestId());

                    StatisticsResultInfo resultInfo = callResult.getData();
                    if ("1".equals(resultInfo.getResult())) {
                        companyEntity.setApplyResult(resultInfo.getResultStatus());
                        companyEntity.setStatisticsMonth(resultInfo.getStatisticsMonth());
                        companyEntity.setStatisticsTime(resultInfo.getStatisticsTime());
                        // 1:申请统计; 2:撤销统计
                        if ("1".equals(resultInfo.getStatisticsType())){
                            // 申请统计成功
                            companyEntity.setStatus(AuthCountStatusEnum.APPLYSUCCESS.getKey());
                            // 统计信息
                            List<StatisticsDetailsInfo> details = resultInfo.getDetails();
                            if (CollectionUtil.isNotEmpty(details)) {
                                this.baseMapper.insertDetailsBatch(companyEntity, details);
                            }
                            // 差异发票合集
                            List<StatisticsInvoiceInfo> differenceInvoices = resultInfo.getDifferenceInvoices();
                            if (CollectionUtil.isNotEmpty(differenceInvoices)){
                                this.baseMapper.insertDifferenceBatch(companyEntity,differenceInvoices);
                            }
                            // 统计发票合集
                            List<StatisticsInvoiceInfo> invoices = resultInfo.getInvoices();
                            if (CollectionUtil.isNotEmpty(invoices)) {
                                this.baseMapper.insertInvoicesBatch(companyEntity, invoices);
                            }
                        }else if("2".equals(resultInfo.getStatisticsType())){
                            // 撤销成功status变为0，未统计
                            companyEntity.setStatus(AuthCountStatusEnum.NOTCOUNT.getKey());
                            this.baseMapper.deleteDetailsByCompanyId(companyEntity.getId());
                            this.baseMapper.deleteDifferenceByCompanyId(companyEntity.getId());
                            this.baseMapper.deleteInvoicesByCompanyId(companyEntity.getId());
                            List<StatisticsInvoiceInfo> invoices = resultInfo.getInvoices();
                            if (CollectionUtil.isNotEmpty(invoices)) {
                                for (StatisticsInvoiceInfo invoice : invoices) {
                                    // invoiceType为17, 30的是海关缴款书
                                    if("17".equals(invoice.getInvoiceType()) || "30".equals(invoice.getInvoiceType())){
                                        InputInvoiceCustomsEntity customsEntity = inputInvoiceCustomsService.getOne(
                                                new QueryWrapper<InputInvoiceCustomsEntity>()
                                                        .eq("pay_no", invoice.getPaymentCertificateNo())
                                        );
                                        // 统计状态: 0:未统计确认; 1:统计确认成功; 2:统计确认失败
                                        customsEntity.setStatisticsState("0");
                                        inputInvoiceCustomsService.updateById(customsEntity);
                                    }else {
                                        InputInvoiceEntity invoiceEntity = inputInvoiceService.getOne(
                                                new QueryWrapper<InputInvoiceEntity>()
                                                        .eq("invoice_code", invoice.getInvoiceCode())
                                                        .eq("invoice_number", invoice.getInvoiceNumber())
                                        );
                                        // 统计状态: 0:未统计确认; 1:统计确认成功; 2:统计确认失败
                                        invoiceEntity.setApplyStatus("0");
                                        invoiceEntity.setUpdateTime(new Date());
                                        inputInvoiceService.updateById(invoiceEntity);
                                    }
                                }
                            }
                        }
                        companyEntity.setUpdateTime(new Date());
                        companyService.update(companyEntity);
                    }
                }else {
                    String code = callResult.getExceptionResult().getCode();
                    String message = callResult.getExceptionResult().getMessage();
                    String requestId = callResult.getExceptionResult().getRequestId();
                    log.error("获取申请/撤销统计结果失败: code:{}, message:{}, requestId:{}", code, message, requestId);
                }
            }else {
                log.error("获取申请/撤销统计结果税局接口无响应");
            }
        }
    }

    /**
     * 确认统计
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void applyConfirm(Map<String, Object> params) {
        // 税号列表
        String taxNoStrs = (String) params.get("taxNoList");
        List<String> taxNoList = Arrays.asList(taxNoStrs.split(","));
        // 参数校验
        if (CollectionUtil.isEmpty(taxNoList)) {
            throw new RRException("税号不能为空");
        }
        for (String taxNo : taxNoList) {
            try {
                SysDeptEntity deptEntity = sysDeptService.getByTaxCode(taxNo);
                if (null == deptEntity) {
                    throw new RRException("未查询到税号为:" + taxNo +"的企业信息,请核对税号是否准确");
                }
                InputCompanyEntity companyEntity = companyService.getByDeptId(deptEntity.getDeptId());
                if (null == companyEntity) {
                    throw new RRException("未查询到税号为:" + taxNo +"的企业进项信息");
                }

                /** 测试使用开始 */

                companyEntity.setStatus(AuthCountStatusEnum.CONFIRMSUCCESS.getKey());
                companyEntity.setRequestId(UUID.randomUUID().toString());
                companyEntity.setUpdateBy(String.valueOf(ShiroUtils.getUserId()));
                companyEntity.setUpdateTime(new Date());
                companyService.updateById(companyEntity);

                /** 测试使用结束 */

                /*ConfirmParamBody confirmParamBody = new ConfirmParamBody();
                confirmParamBody.setTaxNo(taxNo);
                confirmParamBody.setStatisticsTime(companyEntity.getStatisticsTime());
                // 调用确认统计接口
                CallResult<Entity> result = confirmClient.confirm(confirmParamBody);
                if(null != result){
                    if(result.isSuccess()){
                        log.info("确认统计中：{}" + result.getData().getRequestId());
                        companyEntity.setStatus(AuthCountStatusEnum.CONFIRMING.getKey());
                        companyEntity.setRequestId(result.getData().getRequestId());
                        companyEntity.setUpdateBy(String.valueOf(ShiroUtils.getUserId()));
                        companyEntity.setUpdateTime(new Date());
                        companyService.updateById(companyEntity);
                    }else {
                        String code = result.getExceptionResult().getCode();
                        String message = result.getExceptionResult().getMessage();
                        String requestId = result.getExceptionResult().getRequestId();
                        log.info("确认统计失败: code:{}, message:{}", code, message);
                        // 当前税号正在处理中,请稍后再试
                        if(!"OperationProcessing".equalsIgnoreCase(code)){
                            companyEntity.setStatus(AuthCountStatusEnum.CONFIRMFAILED.getKey());
                            companyEntity.setRequestId(result.getExceptionResult().getRequestId());
                            companyEntity.setUpdateBy(String.valueOf(ShiroUtils.getUserId()));
                            companyEntity.setUpdateTime(new Date());
                            companyService.updateById(companyEntity);
                        }
                        throw new RRException("统计失败: " + message);
                    }
                }else {
                    log.error("确认统计税局接口无响应");
                    throw new RRException("接口无响应,请稍后重试");
                }*/
            } catch (RRException e){
                log.error("参数有误: {}", e);
                throw e;
            } catch (Exception e){
                log.error("统计出错: {}", e);
                throw new RRException("统计发生异常: {}", e);
            }
        }
    }

    /**
     * 获取确认结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmResult() {
        // 4:确认统计中
        List<InputCompanyEntity> companyEntityList = companyService.list(
                new QueryWrapper<InputCompanyEntity>()
                    .eq("status", 4)
        );
        if(CollectionUtil.isEmpty(companyEntityList)){
            return;
        }
        for (InputCompanyEntity companyEntity : companyEntityList) {
            CommonParamBody commonParamBody = new CommonParamBody();
            commonParamBody.setRequestId(companyEntity.getRequestId());
            // 调用获取确认统计结果接口
            CallResult<ConfirmResultInfo> callResult = confirmClient.confirmResult(commonParamBody);
            if (null != callResult) {
                if(callResult.isSuccess()){
                    log.info("获取确认统计结果成功：{}"+ callResult.getData().getRequestId());

                    ConfirmResultInfo resultInfo = callResult.getData();
                    if ("1".equals(resultInfo.getConfirmResult())) {
                        companyEntity.setApplyResult(resultInfo.getResultStatus());
                        companyEntity.setStatus(AuthCountStatusEnum.CONFIRMSUCCESS.getKey());
                        companyEntity.setUpdateTime(new Date());
                        companyService.update(companyEntity);

                        // 更新发票/海关缴款书的统计状态
                        List<StatisticsInvoiceInfo> confirmInvoices = resultInfo.getConfirmInvoices();
                        if(CollectionUtil.isNotEmpty(confirmInvoices)){
                            for (StatisticsInvoiceInfo invoice : confirmInvoices) {
                                // invoiceType为17, 30的是海关缴款书
                                if("17".equals(invoice.getInvoiceType()) || "30".equals(invoice.getInvoiceType())){
                                    InputInvoiceCustomsEntity customsEntity = inputInvoiceCustomsService.getOne(
                                            new QueryWrapper<InputInvoiceCustomsEntity>()
                                                    .eq("pay_no", invoice.getPaymentCertificateNo())
                                    );
                                    // 统计状态: 0:未统计确认; 1:统计确认成功; 2:统计确认失败
                                    customsEntity.setStatisticsState("1");
                                    inputInvoiceCustomsService.updateById(customsEntity);
                                }else {
                                    InputInvoiceEntity invoiceEntity = inputInvoiceService.getOne(
                                            new QueryWrapper<InputInvoiceEntity>()
                                                    .eq("invoice_code", invoice.getInvoiceCode())
                                                    .eq("invoice_number", invoice.getInvoiceNumber())
                                    );
                                    // 统计状态: 0:未统计确认; 1:统计确认成功; 2:统计确认失败
                                    invoiceEntity.setApplyStatus("1");
                                    invoiceEntity.setUpdateTime(new Date());
                                    inputInvoiceService.updateById(invoiceEntity);
                                }
                            }
                        }
                    }
                }else {
                    String code = callResult.getExceptionResult().getCode();
                    String message = callResult.getExceptionResult().getMessage();
                    String requestId = callResult.getExceptionResult().getRequestId();
                    log.error("获取确认统计结果失败: code:{}, message:{}, requestId:{}", code, message, requestId);
                }
            }else {
                log.error("获取确认统计结果税局接口无响应");
            }
        }
    }

    @Override
    public void deductInvoices(String invoiceIds, String deductType) {
        String[] invoiceArray = invoiceIds.split(",");
        for (String invoiceId : invoiceArray) {
            String period = inputInvoiceService.getPeriodById(invoiceId).getInvoiceDeductiblePeriod();
            InputInvoiceEntity invoiceEntity = invoiceService.getById(invoiceId);
            invoiceEntity.setInvoiceDeductiblePeriod(period);
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
                        if ("1".equals(deductResultInfo.getDeductibleResult()) || "4".equals(deductResultInfo.getDeductibleResult())) {
                            // 1 勾选 6 撤销勾选
                            if ("1".equals(deductResultInfo.getDeductType())) {
                                // 抵扣成功
                                invoice.setInvoiceStatus(InputConstant.InvoiceStatus.SUCCESSFUL_AUTHENTICATION.getValue());
                                invoice.setVerfy(Boolean.TRUE);
                                invoice.setInvoiceAuthDate(deductResultInfo.getDeductibleDate());
                                invoice.setInvoiceDeductiblePeriod(deductResultInfo.getDeductiblePeriod());
                                // 认证成功同步到数据库
                                inputInvoiceTaxationService.updateTaxation(invoice);
                            } else if ("6".equals(deductResultInfo.getDeductType())) {
                                invoice.setInvoiceStatus(InputConstant.InvoiceStatus.PENDING_CERTIFIED.getValue());
                                invoice.setVerfy(Boolean.FALSE);
                            }
                        } else if(!"6".equals(deductResultInfo.getDeductibleResult())) {
                            if ("1".equals(deductResultInfo.getDeductType())) {
                                invoice.setInvoiceStatus(InputConstant.InvoiceStatus.AUTHENTICATION_FAILED.getValue());
                                invoice.setInvoiceErrorDescription(deductResultInfo.getDeductibleResultMsg());
                            }else if ("6".equals(deductResultInfo.getDeductType())) {
                                invoice.setInvoiceStatus(InputConstant.InvoiceStatus.REVOKE_CERTIFICATION.getValue());
                                invoice.setInvoiceErrorDescription(deductResultInfo.getDeductibleResultMsg());
                            }
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
        String status ="";
        if (StrUtil.isBlank(deductType)) {
            deductType = "1";
            status = InputConstant.InvoiceStatus.CERTIFICATION.getValue();
        }else if("1".equals(deductType)){
            status = InputConstant.InvoiceStatus.CERTIFICATION.getValue();
        }else if("6".equals(deductType)){
            status = InputConstant.InvoiceStatus.UNDO_CERTIFICATION.getValue();
        }
        String requestId = null;
        // 可配置申报期
        String period = sysConfigService.getValue("DEDUCT_PERIOD");
        DeductParamBody deductParamBody = new DeductParamBody();
        deductParamBody.setTaxNo(invoiceEntity.getInvoicePurchaserParagraph());
        //deductParamBody.setTaxNo("911100007693505528");
        deductParamBody.setDeductType(deductType);
        deductParamBody.setPeriod(invoiceEntity.getInvoiceDeductiblePeriod());
        //deductParamBody.setPeriod("202011");
        DeductInvoice deductInvoice = new DeductInvoice();
        deductInvoice.setInvoiceCode(invoiceEntity.getInvoiceCode());
        //deductInvoice.setInvoiceCode("4400184130");
        deductInvoice.setInvoiceNumber(invoiceEntity.getInvoiceNumber());
        //deductInvoice.setInvoiceNumber("27822974");
        deductInvoice.setValidTax(invoiceEntity.getInvoiceTaxPrice().toPlainString());
        //deductInvoice.setValidTax("256.67");
        List<DeductInvoice> invoices = new ArrayList<>();
        invoices.add(deductInvoice);
        deductParamBody.setInvoices(invoices);
        CallResult<ApplyDeductResultInfo> results  = deductInvoices(deductParamBody);

/*        CommonParamBody commonParamBody = new CommonParamBody();
        commonParamBody.setRequestId("");
        CallResult<PaymentCertificateListInfo> result = deductClient.deductPaymentCertificateResult(commonParamBody);
        System.out.println(result);*/

        if (null != results) {
            log.info("勾选结果：" + results.toString());
            if (results.isSuccess()) {
                requestId = results.getData().getRequestId();
                invoiceEntity.setRequestId(requestId);
                invoiceEntity.setInvoiceStatus(status);
            } else {
                if ("1".equals(deductType)) {
                    invoiceEntity.setInvoiceStatus(InputConstant.InvoiceStatus.AUTHENTICATION_FAILED.getValue());
                }else if ("6".equals(deductType)) {
                    invoiceEntity.setInvoiceStatus(InputConstant.InvoiceStatus.REVOKE_CERTIFICATION.getValue());
                }
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
        // 触发2次验真
        invoiceService.VerificationToTwo();
//        for (InputInvoiceEntity inoice : invoiceList) {
//            InvoiceInspectionParamBody invoiceInspectionParamBody = new InvoiceInspectionParamBody();
//            String checkcode = inoice.getInvoiceCheckCode();
//            if (StrUtil.isNotBlank(checkcode)) {
//                invoiceInspectionParamBody.setCheckCode(StrUtil.subWithLength(checkcode, checkcode.length() - 6, checkcode.length()));
//            }
//            invoiceInspectionParamBody.setTotalAmount(inoice.getInvoiceFreePrice().toString());
//            invoiceInspectionParamBody.setBillingDate(inoice.getInvoiceCreateDate());
//            invoiceInspectionParamBody.setInvoiceNumber(inoice.getInvoiceNumber());
//            invoiceInspectionParamBody.setInvoiceCode(inoice.getInvoiceCode());
//
//            CallResult<BaseInvoice> invoiceCheck = this.invoiceCheck(invoiceInspectionParamBody); // 验真接口
//
//            log.info("验真返回结果  checkInvoice ：" + invoiceCheck.toString());
//            if (invoiceCheck.isSuccess()) {
//                if ("2".equals(invoiceCheck.getData().getState())) {
//                    // 异常：作废
//                    inoice.setInvoiceStatus(InputConstant.InvoiceStatus.INVALID.getValue());
//                } else if ("3".equals(invoiceCheck.getData().getState())) {
//                    // 异常：红冲
//                    inoice.setInvoiceStatus(InputConstant.InvoiceStatus.REVERSE.getValue());
//                }
//                invoiceService.update(inoice);
//            }

//            invoiceBatchService.updateStatus(inoice.getBatchNumber());
//        }
    }

    @Override
    public CallResult<Page> invoiceCollection(InvoiceCollectionParamBody invoiceCollectionParamBody) {
        return collectWholeClient.invoiceCollection(invoiceCollectionParamBody);
    }
    @Override
    public void syncInvoice(int page, String startDate, String endDate, String taxNo) {
        if(page==0){
            page =1;
        }
        if(startDate==null||"".equals(startDate)){
            startDate = DateUtils.format(DateUtils.addDateYears(new Date(),-1),"yyyy-MM")+"-01";
        }
        if(endDate==null||"".equals(endDate)){
            endDate = DateUtils.format(new Date());
        }
        SyncInvoiceParamBody syncInvoiceParamBody = new SyncInvoiceParamBody();
        syncInvoiceParamBody.setTaxNo(taxNo);
        syncInvoiceParamBody.setSyncType("1");
        syncInvoiceParamBody.setStartBillingDate(startDate);
        syncInvoiceParamBody.setEndBillingDate(endDate);
        syncInvoiceParamBody.setPage(page);
        syncInvoiceParamBody.setPageSize(200);
        CallResult<SyncInvoiceInfo> results = this.invoiceSync(syncInvoiceParamBody);
        int currentPage = results.getData().getCurrentPage();
        int totalPage = results.getData().getPages();
        if(results.getData() != null && results.getData().getInvoices() != null) {
            this.saveInvoice(results.getData().getInvoices());
            System.out.println("==save==" + results.getData().getInvoices().toString());
        }
        if(currentPage < totalPage) {
            System.out.println("==nextPage==" + (currentPage+1));
            this.syncInvoice(currentPage + 1, startDate, endDate, taxNo);

        }

    }

    /**
     * 根据开票日期查询数据
     * @param date
     * @return
     */
    @Override
    public List<InputInvoiceSyncEntity>  findByBillingDate(String date) {
        return this.list(
                new QueryWrapper<InputInvoiceSyncEntity>().like("billing_date", date)
        );
    }
}
