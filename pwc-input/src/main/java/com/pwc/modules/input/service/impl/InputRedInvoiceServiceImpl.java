package com.pwc.modules.input.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fapiao.neon.client.in.CheckInvoiceClient;
import com.fapiao.neon.model.CallResult;
import com.fapiao.neon.model.in.inspect.*;
import com.fapiao.neon.param.in.InvoiceInspectionParamBody;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pwc.common.exception.RRException;
import com.pwc.common.utils.*;
import com.pwc.modules.input.dao.InputRedInvoiceDao;
import com.pwc.modules.input.entity.*;
import com.pwc.modules.input.service.InputInvoiceSapService;
import com.pwc.modules.input.service.InputInvoiceService;
import com.pwc.modules.input.service.InputRedInvoiceService;
import com.pwc.modules.input.service.InputSapConfEntityService;
import com.pwc.modules.sys.entity.SysDeptEntity;
import com.pwc.modules.sys.service.SysConfigService;
import com.pwc.modules.sys.service.SysDeptService;
import com.pwc.modules.sys.shiro.ShiroUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 红字发票服务实现
 *
 * @author fanpf
 * @date 2020/8/25
 */
@Service("inputRedInvoiceService")
@Slf4j
public class InputRedInvoiceServiceImpl extends ServiceImpl<InputRedInvoiceDao, InputRedInvoiceEntity> implements InputRedInvoiceService {

    @Autowired
    private InputSapConfEntityService inputSapConfEntityService;
    @Autowired
    private InputInvoiceService inputInvoiceService;
    @Autowired
    private HttpUploadFile httpUploadFile;
    @Autowired
    private SysDeptService deptService;
    @Resource
    private CheckInvoiceClient checkInvoiceClient;
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private InputInvoiceSapService inputInvoiceSapService;
    @Autowired
    private SysDeptService sysDeptService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Long deptId = (Long) params.get("deptId");
        String redNoticeNumber = (String) params.get("redNoticeNumber");
        String blueInvoiceNumber = (String) params.get("blueInvoiceNumber");
        String redStatus = (String) params.get("redStatus");
        String entryState = (String) params.get("entryState");
        // 根据部门id获取税号,关联红字通知单
        this.linkDept(deptId);

        redStatus = StringUtils.isNotBlank(redStatus) ? redStatus : "0";
        IPage<InputRedInvoiceEntity> page = this.page(
                new Query<InputRedInvoiceEntity>().getPage(params),
                new QueryWrapper<InputRedInvoiceEntity>()
                        .eq(null != deptId, "dept_id", deptId)
                        .eq(StringUtils.isNotBlank(redNoticeNumber), "red_notice_number", redNoticeNumber)
                        .eq(StringUtils.isNotBlank(blueInvoiceNumber), "blue_invoice_number", blueInvoiceNumber)
                        .eq(StringUtils.isNotBlank(redStatus), "red_status", redStatus)
                        .eq(StringUtils.isNotBlank(entryState), "entry_state", entryState)
                        .orderByDesc("create_time")
        );
        return new PageUtils(page);
    }

    public PageUtils queryPageForMatching(Map<String, Object> params) {
        Long deptId = null;
        if(params.containsKey("deptId") && !params.get("deptId").equals("")){
            deptId =Long.parseLong(params.get("deptId").toString());
        }

        String redNoticeNumber = (String) params.get("redNoticeNumber");
        String blueInvoiceNumber = (String) params.get("blueInvoiceNumber");
        String documentNo = (String) params.get("documentNo");
        String[] redStatus = (String[]) params.get("redStatus");
        String entryState = (String) params.get("entryState");
        // 根据部门id获取税号,关联红字通知单
        this.linkDept(deptId);

        IPage<InputRedInvoiceEntity> page = this.page(
                new Query<InputRedInvoiceEntity>().getPage(params),
                new QueryWrapper<InputRedInvoiceEntity>()
                        .eq(null != deptId, "dept_id", deptId)
                        .eq(StringUtils.isNotBlank(redNoticeNumber), "red_notice_number", redNoticeNumber)
                        .eq(StringUtils.isNotBlank(documentNo), "document_no", documentNo)
                        .eq(StringUtils.isNotBlank(blueInvoiceNumber), "blue_invoice_number", blueInvoiceNumber)
                        .in( "red_status", redStatus)
                        .eq(StringUtils.isNotBlank(entryState), "entry_status", entryState)
                        .orderByDesc("create_time")
        );
        return new PageUtils(page);
    }

    /**
     * 红字通知单条件查询
     *
     * @param params           分页参数
     * @param redInvoiceEntity 条件参数
     * @return 查询结果
     */
    @Override
    public PageUtils conditionList(Map<String, Object> params, InputRedInvoiceEntity redInvoiceEntity) {
        this.linkDept(redInvoiceEntity.getDeptId());
        String freePrice = String.valueOf(redInvoiceEntity.getFreePrice());
        String freePriceBegin = ((String) params.get("freePriceBegin"));
        String freePriceEnd = ((String) params.get("freePriceEnd"));
        IPage<InputRedInvoiceEntity> page = this.page(
                new Query<InputRedInvoiceEntity>().getPage(params, null, true),
                new QueryWrapper<InputRedInvoiceEntity>()
                        .orderByDesc("create_time")
                        .eq(null != redInvoiceEntity.getDeptId(), "dept_id", redInvoiceEntity.getDeptId())
                        .like(StringUtils.isNotBlank(redInvoiceEntity.getRedNoticeNumber()), "red_notice_number", redInvoiceEntity.getRedNoticeNumber())
                        .ge(freePriceBegin != null && !"".equals(freePriceBegin), "free_price", freePriceBegin)
                        .le(freePriceEnd != null && !"".equals(freePriceEnd), "free_price", freePriceEnd)
                        .like(StringUtils.isNotBlank(redInvoiceEntity.getBlueInvoiceNumber()), "blue_invoice_number", redInvoiceEntity.getBlueInvoiceNumber())
                        .eq(StringUtils.isNotBlank(redInvoiceEntity.getRedStatus()), "red_status", StringUtils.isBlank(redInvoiceEntity.getRedStatus()) ? "0" : redInvoiceEntity.getRedStatus())
        );
        return new PageUtils(page);
    }

    /**
     * 导入红字通知单
     *
     * @param files 待导入文件
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importRedNotice(MultipartFile[] files) {
        Map<String, Object> resMap = new HashMap<>();
        // 数据校验正确的集合
        List<InputRedInvoiceEntity> entityList = new ArrayList<>();
        // 数据重复的集合
        List<InputRedInvoiceEntity> duplicateList = new ArrayList<>();
        // 数据总量
        int total = 0;
        // 数据有误条数
        int fail = 0;
        // 记录excel中的重复数据
        List<String> repeatDataList = new ArrayList<>();
        // 记录数据有误的文件
        StringBuffer sb = new StringBuffer();
        try {
            for (MultipartFile file : files) {
                String filename = file.getOriginalFilename();
                ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
                String[] excelHead = {"红字通知单编号", "填开日期", "购方名称", "购方纳税人识别号（税号）", "销方名称",
                        "销方纳税人识别号（税号）", "发票总额（CNY）", "不含税金额（CNY）", "税额（CNY）", "税率", "蓝字发票号码", "蓝字发票代码"};
                String[] excelHeadAlias = {"redNoticeNumber", "writeDate", "purchaserCompany", "purchaserTaxCode",
                        "sellCompany", "sellTaxCode", "totalPrice", "freePrice", "taxPrice", "taxRate", "blueInvoiceNumber", "blueInvoiceCode"};
                for (int i = 0; i < excelHead.length; i++) {
                    reader.addHeaderAlias(excelHead[i], excelHeadAlias[i]);
                }
                List<InputRedInvoiceEntity> dataList = reader.read(0, 1, InputRedInvoiceEntity.class);

                if (CollectionUtils.isEmpty(dataList)) {
                    log.error("上传的{}Excel为空,请重新上传", filename);
//                    throw new RRException("上传的Excel为空,请重新上传");
                    continue;
                }
                total += dataList.size();
                int count = 1;
                for (InputRedInvoiceEntity redInvoiceEntity : dataList) {
                    count++;
                    // 参数校验
                    if (1 == checkExcel(redInvoiceEntity)) {
                        // 参数有误
                        fail += 1;
                        if (!StringUtils.contains(sb.toString(), filename)) {
                            sb.append("文件" + filename + "的错误行号为:");
                        }
                        sb.append(count + ",");
                    } else {
                        // 税率获取到的为小数类型,转为百分数
                        String taxRate = redInvoiceEntity.getTaxRate();
                        NumberFormat nf = NumberFormat.getPercentInstance();
                        redInvoiceEntity.setTaxRate(nf.format(Double.valueOf(taxRate)));
                        // 去除Excel中重复数据
                        String repeatData = redInvoiceEntity.toString();
                        if (CollectionUtil.contains(repeatDataList, repeatData)) {
                            fail += 1;
                            if (!StringUtils.contains(sb.toString(), filename)) {
                                sb.append("文件" + filename + "的错误行号为:");
                            }
                            sb.append(count + ",");
                            continue;
                        }
                        repeatDataList.add(repeatData);
                        // 根据购方税号获取部门id
                        SysDeptEntity deptEntity = deptService.getByTaxCode(redInvoiceEntity.getPurchaserTaxCode());
                        if (null != deptEntity) {
                            redInvoiceEntity.setDeptId(deptEntity.getDeptId());
                        }

                        // 数据库验重
                        // TODO duplicate check red notice number
                        InputRedInvoiceEntity duplicate = super.getOne(
                                new QueryWrapper<InputRedInvoiceEntity>()
                                        .eq("red_notice_number", redInvoiceEntity.getRedNoticeNumber())
                                        //过滤作废红字通知单
                                        .ne("red_status", "2")
                                //根据bug477，只判断红字通知单号
                                      /*.eq("write_date", redInvoiceEntity.getWriteDate())
                                        .eq("purchaser_company", redInvoiceEntity.getPurchaserCompany())
                                        .eq("purchaser_tax_code", redInvoiceEntity.getPurchaserTaxCode())
                                        .eq("sell_company", redInvoiceEntity.getSellCompany())
                                        .eq("sell_tax_code", redInvoiceEntity.getSellTaxCode())
                                        .eq("total_price", redInvoiceEntity.getTotalPrice())
                                        .eq("free_price", redInvoiceEntity.getFreePrice())
                                        .eq("tax_price", redInvoiceEntity.getTaxPrice())
                                        .eq("tax_rate", redInvoiceEntity.getTaxRate())
                                        .eq("blue_invoice_number", redInvoiceEntity.getBlueInvoiceNumber())
                                        .eq("blue_invoice_code", redInvoiceEntity.getBlueInvoiceCode())*/
                        );

                        //查询是否有关联红字通知单的订单信息，如果有将红字通知单置为红票已开
                        List<String> status = Arrays.asList("0", "1", "3", "4");
                        InputInvoiceEntity invoiceEntity = inputInvoiceService.getOne(
                                new QueryWrapper<InputInvoiceEntity>()
                                        .like("red_notice_number", redInvoiceEntity.getRedNoticeNumber()).or().like("invoice_remarks", redInvoiceEntity.getRedNoticeNumber())
                                        .eq("invoice_return", "0")
                                        .eq("invoice_delete", "0")
                                        .notIn("invoice_status", status)
                        );
                        if (null != duplicate) {
                            duplicate.setUpdateBy(String.valueOf(ShiroUtils.getUserId()));
                            duplicate.setUpdateTime(new Date());
                            if (invoiceEntity != null) {
                                duplicate.setRedStatus("1");
                                duplicate.setRedInvoiceCode(invoiceEntity.getInvoiceCode());
                                duplicate.setRedInvoiceNumber(invoiceEntity.getInvoiceNumber());
                            }
                        } else {
                            redInvoiceEntity.setRedStatus("0");
                            if (invoiceEntity != null) {
                                redInvoiceEntity.setRedInvoiceCode(invoiceEntity.getInvoiceCode());
                                redInvoiceEntity.setRedInvoiceNumber(invoiceEntity.getInvoiceNumber());
                                redInvoiceEntity.setRedStatus("1");
                            }
                            redInvoiceEntity.setCreateBy(String.valueOf(ShiroUtils.getUserId()));
                            redInvoiceEntity.setCreateTime(new Date());
                            entityList.add(redInvoiceEntity);
                        }
                    }
                }
                if (sb.toString().endsWith(",")) {
                    sb.deleteCharAt(sb.lastIndexOf(",")).append(";");
                }
            }
            if (sb.toString().endsWith(";")) {
                sb.deleteCharAt(sb.lastIndexOf(";")).append("。");
            }
            resMap.put("total", total);
            resMap.put("success", duplicateList.size() + entityList.size());
            resMap.put("fail", fail);
            resMap.put("failDetail", sb.toString());

            if (CollectionUtil.isNotEmpty(duplicateList)) {
                super.updateBatchById(duplicateList);
            }
            if (CollectionUtil.isNotEmpty(entityList)) {
                super.saveBatch(entityList);
            }

            return resMap;
        } catch (RRException e) {
            throw e;
        } catch (Exception e) {
            log.error("导入红字通知单出错: {}", e);
            throw new RRException("导入红字通知单出现异常");
        }
    }


    /**
     * 接收红字发票并更新红字通知单状态
     *
     * @param file 待导入红字发票
     */
    @Override
    public void receiveRedInvoice(MultipartFile file) {
        // 获取文件类型
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        // 文件后缀校验
        if (!("jpg".equalsIgnoreCase(suffix) || "jpeg".equalsIgnoreCase(suffix) || "png".equalsIgnoreCase(suffix))) {
            throw new RRException("上传文件只支持jpg,jpeg,png类型");
        }

        try {
            // 将文件保存在项目根路径
            String filePath = "statics/red/pic";
            UploadFileEntity fileEntity = UploadKitUtil.uploadFile(file, filePath, true, false);
            String fileName = fileEntity.getServerPath();
            // 调用OCR接口
            InputSapConfEntity sapConfEntity = inputSapConfEntityService.getOneById(7);
            String url = sapConfEntity.getPostUrl();
            String result = httpUploadFile.connectionOCR(url, fileName);
            // 接收到OCR返回后删除图片
            File file1 = new File(ClassUtil.getClassPath() + fileName);
            file1.delete();
            // 解析OCR返回内容
            JsonObject jsonObject = new Gson().fromJson(result, JsonObject.class);
            if (StringUtils.isNotBlank(result)) {
                if (result.contains("scan_result")) {
                    JsonElement jsonElement = jsonObject.get("scan_result");
                    JsonObject content = jsonElement.getAsJsonObject();
                    String code = content.get("code").getAsString();
                    String number = content.get("number").getAsString();
                    String date = content.get("date").getAsString();
                    String checkCode = content.get("check_code").getAsString();
                    if (StringUtils.isNotBlank(checkCode) && checkCode.length() > 6) {
                        checkCode = checkCode.substring(checkCode.length() - 6);
                    }
                    String totalAmount = content.get("total_amount_without_tax").getAsString();
                    // 发票验真获取备注的红字通知单编号
                    String redNoticeNumber = this.checkInvoice(code, number, date, totalAmount, checkCode);
                    // 根据红字通知单编号关联红票
                    InputRedInvoiceEntity redInvoiceEntity = super.getOne(
                            new QueryWrapper<InputRedInvoiceEntity>()
                                    .likeRight("red_notice_number", redNoticeNumber)
                                    .eq("red_status", 0)
                    );
                    // 更改红字通知单的状态为: 红票已开
                    redInvoiceEntity.setRedStatus("1");
                    redInvoiceEntity.setRedInvoiceCode(code);
                    redInvoiceEntity.setRedInvoiceNumber(number);
                    redInvoiceEntity.setUpdateBy(String.valueOf(ShiroUtils.getUserId()));
                    redInvoiceEntity.setUpdateTime(new Date());
                    super.updateById(redInvoiceEntity);
                } else {
                    String errorMsg = jsonObject.get("errorMsg").getAsString();
                    String errorCode = jsonObject.get("errorCode").getAsString();
                    throw new RRException(errorMsg, Integer.valueOf(errorCode));
                }
            } else {
                log.error("OCR接口异常");
                throw new RRException("OCR接口异常");
            }
        } catch (Exception e) {
            log.error("导入红字发票出错: {}", e);
            throw new RRException("导入红字发票发生异常");
        }
    }

    /**
     * 红字发票监控条件查询
     *
     * @param params           分页参数
     * @param redInvoiceEntity 条件参数
     * @return 查询结果
     */
    @Override
    public PageUtils redList(Map<String, Object> params, InputRedInvoiceEntity redInvoiceEntity) {
        IPage<InputRedInvoiceEntity> page = this.page(
                new Query<InputRedInvoiceEntity>().getPage(params, null, true),
                new QueryWrapper<InputRedInvoiceEntity>()
                        .orderByDesc("red_id")
                        .eq(StringUtils.isNotBlank(redInvoiceEntity.getRedNoticeNumber()), "red_notice_number", redInvoiceEntity.getRedNoticeNumber())
                        .eq(StringUtils.isNotBlank(redInvoiceEntity.getRedInvoiceNumber()), "red_invoice_number", redInvoiceEntity.getRedInvoiceNumber())
                        .eq(StringUtils.isNotBlank(redInvoiceEntity.getRedStatus()), "red_status", !StringUtils.isNotBlank(redInvoiceEntity.getRedStatus()) ? "0" : redInvoiceEntity.getRedStatus())
                        .orderByDesc("create_time")
        );
        return new PageUtils(page);
    }

    /**
     * 关联红字发票
     */
    @Override
    public boolean link(Long redId, Map<String, Object> params) {
        String redInvoiceNumber = (String) params.get("redInvoiceNumber");
        String redInvoiceCode = (String) params.get("redInvoiceCode");
        if (StringUtils.isBlank(redInvoiceNumber)) {
            throw new RRException("红字发票号码不能为空");
        }
        if (StringUtils.isBlank(redInvoiceCode)) {
            throw new RRException("红字发票代码不能为空");
        }
        InputRedInvoiceEntity entity = super.getById(redId);
        // 根据参数查询符合条件的红票信息
        try {
            // 识别失败的发票状态 0:识别异常; 1:未识别; 3:部分识别; 4:识别失败
            List<String> status = Arrays.asList("0", "1", "3", "4");
            InputInvoiceEntity invoiceEntity = inputInvoiceService.getOne(
                    new QueryWrapper<InputInvoiceEntity>()
                            .eq("invoice_number", redInvoiceNumber)
                            .eq("invoice_code", redInvoiceCode)
                            .eq("invoice_return", "0")
                            .eq("invoice_delete", "0")
                            .notIn("invoice_status", status)
            );
            if (null == invoiceEntity) {
                throw new RRException("未查询到红字发票信息,请核对数据后再关联");
            }
            /*String invoiceRemarks = invoiceEntity.getInvoiceRemarks();
            if (StringUtils.isBlank(invoiceRemarks)) {
                log.error("红票备注信息为空,无法获取红字通知单编号");
                throw new RRException("数据有误,请核对数据后再关联");
            }
            // 截取备注中的红字通知单编号
            String regex = "[^0-9]";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(invoiceRemarks);
            String noticeNumber = StrUtil.sub(matcher.replaceAll("").trim(), 0, 16);
            if (!entity.getRedNoticeNumber().trim().equals(noticeNumber.trim())) {
                log.error("发票备注信息: {}与红字通知单编号: {}不一致", noticeNumber, entity.getRedNoticeNumber());
                throw new RRException("数据有误,请核对数据后再关联");
            }*/
            // 更新红字通知单的状态及信息
            entity.setRedInvoiceNumber(redInvoiceNumber);
            entity.setRedInvoiceCode(redInvoiceCode);
            entity.setRedStatus("1");
            entity.setUpdateBy(String.valueOf(ShiroUtils.getUserId()));
            entity.setUpdateTime(new Date());
            super.updateById(entity);

            //更新发票红字通知单号信息
            invoiceEntity.setRedNoticeNumber(entity.getRedNoticeNumber());
            inputInvoiceService.updateById(invoiceEntity);
        } catch (RRException e) {
            throw e;
        } catch (Exception e) {
            log.error("查询红字发票信息出错: {}", e);
            throw new RRException("数据有误,请核对数据后再关联");
        }
        return true;
    }

    /**
     * 发票验真获取发票备注信息
     *
     * @param invoiceCode   发票代码
     * @param invoiceNumber 发票号码
     * @param billingDate   开票日期
     * @param totalAmount   发票金额
     * @param checkCode     校验码
     * @return 发票备注信息
     */
    private String checkInvoice(String invoiceCode, String invoiceNumber, String billingDate, String totalAmount, String checkCode) {
        InvoiceInspectionParamBody paramBody = new InvoiceInspectionParamBody();
        paramBody.setInvoiceCode(invoiceCode);
        paramBody.setInvoiceNumber(invoiceNumber);
        paramBody.setBillingDate(billingDate);
        paramBody.setTotalAmount(totalAmount);
        paramBody.setCheckCode(checkCode);

        // 初始化备注信息
        String remarks = "";
        try {
            CallResult<BaseInvoice> result = checkInvoiceClient.check(paramBody);
            BaseInvoice data = result.getData();
            // 根据发票类型,基类强转获取子类
            String invoiceType = data.getInvoiceType();
            if ("01".equals(invoiceCode)) {
                SpecialInvoiceInfo specialInvoiceInfo = (SpecialInvoiceInfo) data;
                remarks = specialInvoiceInfo.getRemarks();
            } else if ("02".equals(invoiceCode)) {
                TransportInvoiceInfo transportInvoiceInfo = (TransportInvoiceInfo) data;
                remarks = transportInvoiceInfo.getRemarks();
            } else if ("03".equals(invoiceCode)) {
                VehicleInvoiceInfo vehicleInvoiceInfo = (VehicleInvoiceInfo) data;
                log.info("此类发票没有备注字段,需进行手动关联");
            } else if ("04".equals(invoiceCode)) {
                NormalInvoiceInfo normalInvoiceInfo = (NormalInvoiceInfo) data;
                remarks = normalInvoiceInfo.getRemarks();
            } else if ("10".equals(invoiceCode)) {
                ENormalInvoiceInfo eNormalInvoiceInfo = (ENormalInvoiceInfo) data;
                remarks = eNormalInvoiceInfo.getRemarks();
            } else if ("11".equals(invoiceCode)) {
                RollInvoiceInfo rollInvoiceInfo = (RollInvoiceInfo) data;
                remarks = rollInvoiceInfo.getRemarks();
            } else if ("14".equals(invoiceCode)) {
                TollInvoiceInfo tollInvoiceInfo = (TollInvoiceInfo) data;
                remarks = tollInvoiceInfo.getRemarks();
            } else if ("15".equals(invoiceCode)) {
                UsedCarInvoiceInfo usedCarInvoiceInfo = (UsedCarInvoiceInfo) data;
                remarks = usedCarInvoiceInfo.getRemarks();
            } else if ("20".equals(invoiceCode)) {
                ESpecialInvoiceInfo eSpecialInvoiceInfo = (ESpecialInvoiceInfo) data;
                remarks = eSpecialInvoiceInfo.getRemarks();
            }
            // TODO 截取前16位为红字通知单编号
        } catch (Exception e) {
            log.error("发票验真出错: {}", e);
            throw new RRException("发票验真发生异常");
        }
        return remarks;
    }

    /**
     * 校验Excel中参数
     */
    private int checkExcel(InputRedInvoiceEntity entity) {
        if (StringUtils.isBlank(entity.getRedNoticeNumber()) || null == entity.getWriteDate() ||
                StringUtils.isBlank(entity.getPurchaserCompany()) || StringUtils.isBlank(entity.getPurchaserTaxCode()) ||
                StringUtils.isBlank(entity.getSellCompany()) || StringUtils.isBlank(entity.getSellTaxCode()) ||
                null == entity.getTotalPrice() || null == entity.getFreePrice() || null == entity.getTaxPrice() ||
                StringUtils.isBlank(entity.getTaxRate()) || StringUtils.isBlank(entity.getBlueInvoiceNumber()) ||
                StringUtils.isBlank(entity.getBlueInvoiceCode())) {
            return 1;
        }
        return 0;
    }

    /**
     * 根据部门id获取税号,关联红字通知单
     */
    private void linkDept(Long deptId) {
        if (null == deptId) {
            return;
        }
        String taxCode = deptService.queryTaxCodeById(deptId);
        if (StrUtil.isBlank(taxCode)) {
            return;
        }
        List<InputRedInvoiceEntity> entityList = super.list(
                new QueryWrapper<InputRedInvoiceEntity>()
                        .eq("purchaser_tax_code", taxCode)
                        .isNull("dept_id")
        );
        if (CollectionUtil.isEmpty(entityList)) {
            return;
        }
        entityList.forEach(
                entity -> entity.setDeptId(deptId)
        );
        super.updateBatchById(entityList);
    }

    /**
     * 根据红字编号 查询未关联的通知单
     *
     * @param redNoticeNumber
     * @return
     */
    @Override
    public InputRedInvoiceEntity findRedNoticeNumber(String redNoticeNumber) {
        InputRedInvoiceEntity invoiceEntity = this.getOne(
                new QueryWrapper<InputRedInvoiceEntity>()
                        .eq("red_notice_number", redNoticeNumber)
                        .isNull("red_invoice_number")
                        .isNull("red_invoice_code")
                        .eq("red_status", 0)
        );
        return invoiceEntity;
    }

    /**
     * 红字通知单手工入账
     *
     * @param params
     */
    @Override
    public String manualEntryByRed(Map<String, Object> params) {
        String ids =  params.get("ids").toString();
        String yearAndMonth =  params.get("yearAndMonth").toString();
        String documentNo = params.get("documentNo").toString();
        InputRedInvoiceEntity redInvoiceEntity = getById(ids);
        SysDeptEntity sysDept = sysDeptService.getByName(redInvoiceEntity.getPurchaserCompany());
        boolean flag = true;
        List<InputInvoiceSapEntity> sapEntityList = new ArrayList<>();
        String[] documentNoList = documentNo.split("/");
        if(documentNoList.length >1){
            //一票多账的情况
            for(int i = 0;i<documentNoList.length;i++){
                //匹配组织及信息是否一致
                InputInvoiceSapEntity sapEntity=inputInvoiceSapService.getEntityByNo(documentNoList[i],yearAndMonth,sysDept.getSapDeptCode());
                if (sapEntity != null) {
                    sapEntityList.add(sapEntity);
                } else {
                    flag = false;
                }
            }
        }else{
            InputInvoiceSapEntity sapEntity=inputInvoiceSapService.getEntityByNo(documentNo,yearAndMonth,sysDept.getSapDeptCode());
            sapEntityList.add(sapEntity);
        }
        if (flag) {
            redInvoiceEntity.setDocumentNo(documentNo);
            updateById(redInvoiceEntity);
            String  type= saveEntry(documentNo,sapEntityList);
            return  type;
        } else {
            return  "0";
        }
    }

    @Override
    public void obsoleteEntryByRed(InputRedInvoiceEntity inputRedInvoice) {
        //删除红字通知单与发票关联关系
        InputRedInvoiceEntity oldInputRedInvoiceEntity = super.getById(inputRedInvoice);
        if (oldInputRedInvoiceEntity.getRedInvoiceNumber() != null && oldInputRedInvoiceEntity.getRedInvoiceCode() != null) {
            // 识别失败的发票状态 0:识别异常; 1:未识别; 3:部分识别; 4:识别失败
            List<String> status = Arrays.asList("0", "1", "3", "4");
            InputInvoiceEntity invoiceEntity = inputInvoiceService.getOne(
                    new QueryWrapper<InputInvoiceEntity>()
                            .eq("invoice_number", inputRedInvoice.getRedInvoiceNumber())
                            .eq("invoice_code", inputRedInvoice.getRedInvoiceCode())
                            .eq("invoice_return", "0")
                            .eq("invoice_delete", "0")
                            .notIn("invoice_status", status)
            );
            if (invoiceEntity != null) {
                //删除红票关联关系
                invoiceEntity.setRedNoticeNumber("");
                inputInvoiceService.updateById(invoiceEntity);
            }
        }
    }

    /**
     * 自动入账
     *
     * @param sapEntity
     * @return
     */
    @Override
    public InputInvoiceSapEntity voluntaryEntry(InputInvoiceSapEntity sapEntity) {
        String[] numbers = sapEntity.getReference().split("/");
        int count = 0;
        if (numbers.length > 0) {
            InputRedInvoiceEntity redInvoiceEntity = new InputRedInvoiceEntity();
            redInvoiceEntity.setDocumentNo(sapEntity.getDocumentNo());
            UpdateWrapper<InputRedInvoiceEntity> updateQueryWrapper = new UpdateWrapper<>();
            updateQueryWrapper.in("blue_invoice_number", numbers);
            updateQueryWrapper.ne("red_status", "2");
            count = baseMapper.update(redInvoiceEntity, updateQueryWrapper);
            if (count != 0) {
                List<InputInvoiceSapEntity> sapEntityList = new ArrayList<>();
                sapEntityList.add(sapEntity);
                String type  = saveEntry(sapEntity.getDocumentNo(),sapEntityList);
                sapEntity.setSapMatch(type);
            }
        } else {
            sapEntity.setSapMatch("0");
        }
        return sapEntity;
    }


    /**
     * 账票匹配
     *
     * @param documentNo
     */
    public String saveEntry(String documentNo,List<InputInvoiceSapEntity> sapEntityList) {
        //获取容差
        String value = sysConfigService.getValue("TOLERANCE_VALUE");
        BigDecimal valueTax = value != null ? new BigDecimal(value) : BigDecimal.ZERO;
        BigDecimal amountInLocal = BigDecimal.ZERO;
        for(int i = 0;i<sapEntityList.size();i++){
            amountInLocal = amountInLocal.add(sapEntityList.get(i).getAmountInLocal()) ;
        }
        String totalTax = baseMapper.getSumByTaxPrice(documentNo);
        String type = InputConstant.InvoiceMatch.MATCH_NO.getValue();
        InputRedInvoiceEntity redInvoiceEntity = new InputRedInvoiceEntity();
        if (totalTax != null && (new BigDecimal(totalTax)).compareTo(BigDecimal.ZERO) == 0) {
            for(int i = 0;i<sapEntityList.size();i++){
                InputInvoiceSapEntity sapEntity=sapEntityList.get(i);
                sapEntity.setSapMatch(InputConstant.InvoiceMatch.MATCH_NO.getValue());
                inputInvoiceSapService.updateById(sapEntity);
            }
        } else if (totalTax != null && ((new BigDecimal(totalTax).subtract(valueTax)).compareTo(amountInLocal) == 0
                || (new BigDecimal(totalTax)).compareTo(amountInLocal.subtract(valueTax)) == 0
        )) {
            for(int i = 0;i<sapEntityList.size();i++){
                InputInvoiceSapEntity sapEntity=sapEntityList.get(i);
                sapEntity.setSapMatch(InputConstant.InvoiceMatch.MATCH_YES.getValue());
                inputInvoiceSapService.updateById(sapEntity);
            }
            type = InputConstant.InvoiceMatch.MATCH_YES.getValue();
        } else {
            for(int i = 0;i<sapEntityList.size();i++){
                InputInvoiceSapEntity sapEntity=sapEntityList.get(i);
                sapEntity.setSapMatch(InputConstant.InvoiceMatch.MATCH_ERROR.getValue());
                inputInvoiceSapService.updateById(sapEntity);
            }
            type = InputConstant.InvoiceMatch.MATCH_ERROR.getValue();
            if(totalTax != null){
                redInvoiceEntity.setSapCheckTax(((new BigDecimal(totalTax).subtract(valueTax)).subtract(amountInLocal).toString()));
            }
        }
        redInvoiceEntity.setEntryStatus(type);
        redInvoiceEntity.setMatchDate(DateUtils.format(new Date()));
        redInvoiceEntity.setEntryDate(sapEntityList.get(0).getPstngDate());
        redInvoiceEntity.setYearAndMonth(sapEntityList.get(0).getYearAndMonth());
        redInvoiceEntity.setSapTax(amountInLocal.toString());
        UpdateWrapper<InputRedInvoiceEntity> updateQueryWrapper = new UpdateWrapper<>();
        updateQueryWrapper.eq("document_no", documentNo);
        return type;
    }

    public List<InputRedInvoiceEntity> getRedInvoicByDate(Map<String, Object> params) {
        return this.list(
                new QueryWrapper<InputRedInvoiceEntity>().like("write_date", params.get("date"))
        );

    }

    @Override
    public PageUtils getListByMatching(Map<String, Object> params){
        String[] redStatus=new String[]{"0","1"};
        params.put("redStatus",redStatus);
        //params.put("entryState", InputConstant.InvoiceMatch.MATCH_YES.getValue());
        return  queryPageForMatching(params);
    }

    @Override
    public int getListByShow() {
        return this.baseMapper.getListByShow();
    }

    /**
     * 获取查询月份认证完成的数据
     * @param params
     * @return
     */
    @Override
    public List<InputRedInvoiceEntity> getCertification(Map<String, Object> params){
        String date = params.get("yearAndMonth").toString();
        String deptId = params.get("deptId").toString();
        return  this.list(
                new QueryWrapper<InputRedInvoiceEntity>()
                        .like("write_date",date)
                        .eq("dept_id",deptId)
        );
    }


    @Override
    public PageUtils getMonthCredBeforeResult(Map<String, Object> params){
        String yearAndMonth=ParamsMap.findMap(params, "yearAndMonth");
        String newYearAndMonth = yearAndMonth.substring(0,7) + "-01";
        String deptId=ParamsMap.findMap(params, "deptId");
        String redNoticeNumber=ParamsMap.findMap(params, "redNoticeNumber");
        String blueInvoiceNumber=ParamsMap.findMap(params, "blueInvoiceNumber");
        String documentNo=ParamsMap.findMap(params, "documentNo");
        SysDeptEntity sysDeptEntity = sysDeptService.getById(deptId);
        String resultType=ParamsMap.findMap(params, "resultType");
        if(resultType.equals("1")){
            IPage<InputRedInvoiceEntity> page = this.page(
                    new Query<InputRedInvoiceEntity>().getPage(params),
                    new QueryWrapper<InputRedInvoiceEntity>()
                            .eq(org.apache.commons.lang.StringUtils.isNotBlank(sysDeptEntity.getTaxCode()), "purchaser_tax_code", sysDeptEntity.getTaxCode())
                            .eq("entry_status", "1")
                            .lt("write_date", newYearAndMonth)
                            .ge("entry_date", newYearAndMonth)
                            .like(org.apache.commons.lang.StringUtils.isNotBlank(redNoticeNumber), "red_notice_number", redNoticeNumber)
                            .like(org.apache.commons.lang.StringUtils.isNotBlank(blueInvoiceNumber), "blue_invoice_number", blueInvoiceNumber)
                            .like(org.apache.commons.lang.StringUtils.isNotBlank(documentNo), "document_no", documentNo)
            );
            return new PageUtils(page);
        }else if(resultType.equals("2")){
            IPage<InputRedInvoiceEntity> page = this.page(
                    new Query<InputRedInvoiceEntity>().getPage(params),
                    new QueryWrapper<InputRedInvoiceEntity>()
                            .eq(org.apache.commons.lang.StringUtils.isNotBlank(sysDeptEntity.getTaxCode()), "purchaser_tax_code", sysDeptEntity.getTaxCode())
                            .ne("entry_status", "1")
                            .ge("write_date", newYearAndMonth)
                            .like(org.apache.commons.lang.StringUtils.isNotBlank(redNoticeNumber), "red_notice_number", redNoticeNumber)
                            .like(org.apache.commons.lang.StringUtils.isNotBlank(blueInvoiceNumber), "blue_invoice_number", blueInvoiceNumber)
                            .like(org.apache.commons.lang.StringUtils.isNotBlank(documentNo), "document_no", documentNo)
            );
            return new PageUtils(page);
        }else{

            IPage<InputRedInvoiceEntity> page = this.page(
                    new Query<InputRedInvoiceEntity>().getPage(params),
                    new QueryWrapper<InputRedInvoiceEntity>()
                            .eq(org.apache.commons.lang.StringUtils.isNotBlank(sysDeptEntity.getTaxCode()), "purchaser_tax_code", sysDeptEntity.getTaxCode())
                            .eq("entry_status", "1")
                            .ge("write_date", newYearAndMonth)
                            .lt("entry_date", newYearAndMonth)
                            .like(org.apache.commons.lang.StringUtils.isNotBlank(redNoticeNumber), "red_notice_number", redNoticeNumber)
                            .like(org.apache.commons.lang.StringUtils.isNotBlank(blueInvoiceNumber), "blue_invoice_number", blueInvoiceNumber)
                            .like(org.apache.commons.lang.StringUtils.isNotBlank(documentNo), "document_no", documentNo)
            );
            return new PageUtils(page);
        }
    }
}
