package com.pwc.modules.input.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.fapiao.neon.client.in.CollectCustomsClient;
import com.fapiao.neon.model.CallResult;
import com.fapiao.neon.model.Entity;
import com.fapiao.neon.model.in.CollectCustomsResultInfo;
import com.fapiao.neon.model.in.CustomsInvoiceResult;
import com.fapiao.neon.param.in.CommonParamBody;
import com.fapiao.neon.param.in.CustomsApplyInvoice;
import com.fapiao.neon.param.in.CustomsApplyParamBody;
import com.pwc.common.exception.RRException;
import com.pwc.common.utils.DateUtils;
import com.pwc.common.utils.ParamsMap;
import com.pwc.modules.input.dao.InputInvoiceCustomsGatherDao;
import com.pwc.modules.input.entity.InputInvoiceCustomsGatherEntity;
import com.pwc.modules.input.entity.InputInvoiceWhtEntity;
import com.pwc.modules.input.service.InputInvoiceCustomsGatherService;
import com.pwc.modules.input.service.InputInvoiceCustomsService;
import com.pwc.modules.sys.entity.SysDeptEntity;
import com.pwc.modules.sys.service.SysDeptService;
import com.pwc.modules.sys.shiro.ShiroUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;


@Service("inputInvoiceCustomsGatherService")
public class InputInvoiceCustomsGatherServiceImpl extends ServiceImpl<InputInvoiceCustomsGatherDao, InputInvoiceCustomsGatherEntity> implements InputInvoiceCustomsGatherService {
    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private InputInvoiceCustomsService inputInvoiceCustomsService;
    @Resource
    private CollectCustomsClient collectCustomsClient;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        //缴款书号码
        String payNo = (String) params.get("payNo");
        String deptId = ParamsMap.findMap(params, "deptId");
        String taxCode = null;
        if (deptId != null) {
            SysDeptEntity deptEntity = sysDeptService.getById(deptId);
            if (deptEntity != null) {
                taxCode = deptEntity.getTaxCode();
            }
        }
        //填发日期
        String billingDateArray = (String) params.get("billingDateArray");
        String billingDateStart = "";
        String billingDateEnd = "";
        if (StringUtils.isNotBlank(billingDateArray)) {
            billingDateStart = billingDateArray.split(",")[0];
            billingDateEnd = billingDateArray.split(",")[1];
        }
        //采集日期
        String deductibleDateArray = (String) params.get("deductibleDateArray");
        String deductibleDateStart = "";
        String deductibleDateEnd = "";
        if (StringUtils.isNotBlank(deductibleDateArray)) {
            deductibleDateStart = deductibleDateArray.split(",")[0];
            deductibleDateEnd = deductibleDateArray.split(",")[1];
        }
        //批次号
        String batchNo = (String) params.get("batchNo");
        //采集状态
        String gatherStatus = (String) params.get("gatherStatus");
        //稽核结果
        String auditStatus = (String) params.get("auditStatus");

        IPage<InputInvoiceCustomsGatherEntity> page = this.page(
                new Query<InputInvoiceCustomsGatherEntity>().getPage(params),
                new QueryWrapper<InputInvoiceCustomsGatherEntity>()
                        .eq(StringUtils.isNotBlank(payNo), "pay_no", payNo)
                        .eq(StringUtils.isNotBlank(taxCode), "purchaser_tax_no", taxCode)
                        .eq(StringUtils.isNotBlank(gatherStatus), "gather_status", gatherStatus)
                        .eq(StringUtils.isNotBlank(auditStatus), "audit_status", auditStatus)
                        .eq(StringUtils.isNotBlank(batchNo), "batch_no", batchNo)
                        .eq("del_flag", 0)
                        .between(StringUtils.isNotBlank(billingDateStart) && StringUtils.isNotBlank(billingDateEnd), "billing_date", billingDateStart, billingDateEnd)
                        .between(StringUtils.isNotBlank(deductibleDateStart) && StringUtils.isNotBlank(deductibleDateEnd), "gather_date", deductibleDateStart, deductibleDateEnd)
                        .orderByDesc("upload_date")
        );
        return new PageUtils(page);
    }

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
            String[] excelHead = {"专用交款书编号", "申报公司税号", "填发报期", "税额"};
            String[] excelHeadAlias = {"payNo", "purchaserTaxNo", "billingDate", "totalTax",};
            for (int i = 0; i < excelHead.length; i++) {
                reader.addHeaderAlias(excelHead[i], excelHeadAlias[i]);
            }
            List<InputInvoiceCustomsGatherEntity> dataList = reader.read(0, 1, InputInvoiceCustomsGatherEntity.class);

            if (CollectionUtils.isEmpty(dataList)) {
                log.error("上传的{}Excel为空,请重新上传");
                throw new RRException("上传的Excel为空,请重新上传");
            }
            total += dataList.size();
            int count = 1;
            for (InputInvoiceCustomsGatherEntity entity : dataList) {
                count++;
                // 去除Excel中重复数据
                String repeatData = entity.getPayNo() + entity.getPurchaserTaxNo();
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
                InputInvoiceCustomsGatherEntity duplicate = super.getOne(
                        new QueryWrapper<InputInvoiceCustomsGatherEntity>()
                                .eq("pay_no", entity.getPayNo())
                                .eq("purchaser_tax_no", entity.getPurchaserTaxNo())
                                .eq("del_flag", 0)
                );
                //根据税号获取所属公司名称
                if (null != duplicate) {
                    duplicate.setUpdateBy(ShiroUtils.getUserId().intValue());
                    duplicate.setUpdateTime(new Date());
                    this.paraphraseParams(duplicate);
                    super.updateById(duplicate);
                } else {
                    entity.setUploadBy(ShiroUtils.getUserId().intValue());
                    entity.setUploadDate(new Date());
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
            log.error("海关缴款书采集导入明细出错: {}", e);
            throw new RRException("海关缴款书采集导入明细出现异常");
        }
    }

    private InputInvoiceCustomsGatherEntity paraphraseParams(InputInvoiceCustomsGatherEntity entity) {
        SysDeptEntity dept = sysDeptService.getByTaxCode(entity.getPurchaserTaxNo());
        entity.setPurchaserName(dept.getName());
        String strMsg = entity.getBillingDate().replace("/", "-");
        Date entryDate = DateUtils.stringToDate(strMsg.substring(0, 10), "yyyy-MM-dd");

        //申请金税采集接口
        CustomsApplyParamBody paramBody = new CustomsApplyParamBody();
        List<CustomsApplyInvoice> invoiceList = new ArrayList<>();
        CustomsApplyInvoice invoice = new CustomsApplyInvoice();
        invoice.setPayNo(entity.getPayNo());
        invoice.setBillingDate(entryDate.toString());
        invoice.setTotalTax(entity.getTotalTax());
        paramBody.setTaxNo(entity.getPurchaserTaxNo());
        invoiceList.add(invoice);
        paramBody.setInvoices(invoiceList);
        paramBody.setServiceType("0");
        CallResult<Entity> aa = collectCustomsClient.apply(paramBody);
        if (aa.isSuccess()) {
            entity.setGatherStatus("1");
            entity.setRequestId(aa.getData().getRequestId());
        } else {
            entity.setGatherStatus("4");
        }
        entity.setInfoSources("2");
        entity.setInvoiceType("17");
        entity.setUploadType("1");

        // 生成批次号
        String dateStr = DateUtil.format(new Date(), "yyyyMMdd");
        String batchNo = dateStr + "-";
        List<InputInvoiceCustomsGatherEntity> customsEntityList = super.list(
                new QueryWrapper<InputInvoiceCustomsGatherEntity>()
                        .eq("purchaser_tax_no", entity.getPurchaserTaxNo())
                        .orderByDesc("upload_date")
        );
        if (CollectionUtil.isEmpty(customsEntityList)) {
            batchNo += 1;
        } else {
            InputInvoiceCustomsGatherEntity customsEntity = customsEntityList.get(0);
            String lastBatchNo = customsEntity.getBatchNo();
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
        entity.setBatchNo(batchNo);
        return entity;
    }

    @Override
    public int getListByShow() {
        return this.baseMapper.getListByShow();
    }

    @Override
    public void customsGatherResultByTime() {
        List<InputInvoiceCustomsGatherEntity> entityList = super.list(
                new QueryWrapper<InputInvoiceCustomsGatherEntity>()
                        .eq("del_flag", 0)
                        .in("gather_status", new String[]{"1", "3"})
        );
        if (entityList.size() > 0) {
            for (int i = 0; i < entityList.size(); i++) {
                InputInvoiceCustomsGatherEntity entity = entityList.get(i);
                if (entity.getRequestId() != null) {
                    CommonParamBody body = new CommonParamBody();
                    body.setRequestId(entity.getRequestId());
                    CallResult<CollectCustomsResultInfo> bb = collectCustomsClient.results(body);
                    if (bb.isSuccess()) {
                        List<CustomsInvoiceResult> resultList = bb.getData().getResults();
                        if (resultList.size() > 0) {
                            if (resultList.get(0).getAuditResult() != null) {
                                if (resultList.get(0).getAuditResult().equals("1") || resultList.get(0).getAuditResult().equals("6") || resultList.get(0).getAuditResult().equals("7")) {
                                    entity.setGatherStatus("2");
                                } else {
                                    entity.setGatherStatus("3");
                                }
                            } else {
                                entity.setGatherStatus("3");
                            }
                            entity.setAuditStatus(resultList.get(0).getAuditResult());
                            entity.setGatherDate(new Date());
                            entity.setAbnormalType(resultList.get(0).getApplyResult());
                            super.updateById(entity);
                        }
                    } else {
                        log.error("获取采集结果税局接口无响应");
                        throw new RRException("接口无响应,请稍后重试");
                    }
                }
            }
        }
    }

    @Override
    public void customsGatherResult(String id) {
        InputInvoiceCustomsGatherEntity entity = super.getById(id);
        if (entity.getRequestId() != null) {
            CommonParamBody body = new CommonParamBody();
            body.setRequestId(entity.getRequestId());
            CallResult<CollectCustomsResultInfo> bb = collectCustomsClient.results(body);
            if (bb.isSuccess()) {
                List<CustomsInvoiceResult> resultList = bb.getData().getResults();
                if (resultList.size() > 0) {
                    if (resultList.get(0).getAuditResult() != null) {
                        if (resultList.get(0).getAuditResult().equals("1") || resultList.get(0).getAuditResult().equals("6") || resultList.get(0).getAuditResult().equals("7")) {
                            entity.setGatherStatus("2");
                        } else {
                            entity.setGatherStatus("3");
                        }
                    } else {
                        entity.setGatherStatus("3");
                    }
                    entity.setAuditStatus(resultList.get(0).getAuditResult());
                    entity.setGatherDate(new Date());
                    entity.setAbnormalType(resultList.get(0).getApplyResult());
                    super.updateById(entity);
                }
            } else {
                log.error("获取采集结果税局接口无响应");
                throw new RRException("接口无响应,请稍后重试");
            }
        }
    }
}
