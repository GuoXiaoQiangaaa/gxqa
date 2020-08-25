package com.pwc.modules.input.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fapiao.neon.model.CallResult;
import com.fapiao.neon.model.in.inspect.BaseInvoice;
import com.fapiao.neon.model.in.inspect.ENormalInvoiceInfo;
import com.fapiao.neon.model.in.inspect.NormalInvoiceInfo;
import com.fapiao.neon.model.in.inspect.SpecialInvoiceInfo;
import com.fapiao.neon.param.in.InvoiceInspectionParamBody;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.pwc.common.annotation.DataFilter;
import com.pwc.common.utils.*;
import com.pwc.common.utils.apidemo.utils.Base64Util;
import com.pwc.common.utils.apidemo.utils.HttpUtil;
import com.pwc.common.utils.apidemo.utils.SignUtil;
import com.pwc.modules.input.dao.InputInvoiceDao;
import com.pwc.modules.input.dao.InputWhiteBlackDao;
import com.pwc.modules.input.dao.InputWhiteBlackListDao;
import com.pwc.modules.input.entity.*;
import com.pwc.modules.input.entity.rpa.ZInvoiceAccount;
import com.pwc.modules.input.entity.rpa.ZInvoiceDetailInfo;
import com.pwc.modules.input.entity.rpa.ZInvoiceInfo;
import com.pwc.modules.input.service.*;
import com.pwc.modules.sys.entity.SysDeptEntity;
import com.pwc.modules.sys.service.SysDeptService;
import com.pwc.modules.sys.shiro.ShiroUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.pwc.common.utils.FdfsUtil.createLog;

@Service("invoiceService")
public class InputInvoiceServiceImpl extends ServiceImpl<InputInvoiceDao, InputInvoiceEntity> implements InputInvoiceService {


    private static final String jtnsrsbh = "91310000607378229C";//集团 、 合作伙伴 税号
    //    private static final String jtnsrsbh = "91350000611528672E";//集团 、 合作伙伴 税号
    private static final String appid = "5d206b9d5b12";// appid
    private static final String appsecret = "3638a4cda4468ad7fab1";//appsecret
    //    private static final String url = "https://pubapi.holytax.com/pre/api";//接口地址
    private static final String url = "http://218.94.72.202:19090/mars/";//接口地址

    @Autowired
    private InputInvoiceBatchService invoiceBatchService;
    @Autowired
    private InputSapConfEntityService sapConfEntityService;
    @Autowired
    private HttpUploadFile httpUploadFile;
    @Autowired
    private InputInvoiceVoucherService invoiceVoucherService;
    @Autowired
    private InputInvoiceSyncService invoiceSyncService;
    @Autowired
    private InputInvoiceMaterialService invoiceMaterialService;
    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private InputInvoiceMaterialSapService invoiceMaterialSapService;
    @Autowired
    private InputWhiteBlackDao whiteBlackDao;
    @Autowired
    private InputWhiteBlackListDao whiteBlackListDao;
    @Autowired
    private InputSapInvoiceMappingIdService sapInvoiceMappingIdService;
    @Autowired
    private InputInvoiceResponsibleService invoiceResponsibleService;
    @Autowired
    private InputInvoiceGoodsMatnrService invoiceGoodsMatnrService;
    @Autowired
    private InputTransOutCategoryService inputTransOutCateogryService;
    @Autowired
    private InputInvoiceTaxRateTransitionService invoiceTaxRateTransitionService;
    @Autowired
    private InputInvoiceConditionMapService invoiceConditionMapService;
    @Autowired
    private InputInvoiceCheckActionService invoiceCheckActionService;
    @Autowired
    private InputInvoiceFaultTolerantService invoiceFaultTolerantService;
    @Autowired
    private InputInvoiceUnitDetailsService invoiceUnitDetailsService;
    @Autowired
    private InputInvoiceUnitPushService invoiceUnitPushService;
    @Autowired
    private InputInvoiceUnitDifferentService invoiceUnitDifferentService;
    @Autowired
    private InputInvoiceTaxationService inputInvoiceTaxationService;


    /**
     * 票据总览页面方法
     *
     * @param params
     * @return
     */
    @Override
    @DataFilter(deptId = "company_id",subDept = true, user = false)
    public PageUtils queryPage(Map<String, Object> params, InputInvoiceEntity invoiceEntity) {
        String invoiceUploadDateArray = (String) params.get("invoiceUploadDateArray");
        String invoiceCreateDateArray = (String) params.get("invoiceCreateDateArray");
        String entryDateArray = (String) params.get("entryDateArray");
        String[] batchNumbers = ((String) params.get("invoiceBatchNumberList")).split(",");
//        String invoiceReturn = (String) params.get("invoiceReturn");
//        String invoiceDelete = (String) params.get("invoiceDelete");
        Integer createBy = invoiceEntity.getCreateBy();
        String invoiceStatus = (String) params.get("invoiceStatus");
        // 是否是认证列表
        String certification = (String) params.get("certification");
        // 认证勾选状态
        List<String> statusList = new ArrayList<String>(){{
            add("9");
            add("10");
            add("12");
            add("13");
        }};
        IPage<InputInvoiceEntity> page = this.page(
                new Query<InputInvoiceEntity>().getPage(params, null, true),
                new QueryWrapper<InputInvoiceEntity>()
                        .orderByDesc("invoice_upload_date")
                        .eq(StringUtils.isNotBlank(invoiceStatus),"invoice_status",invoiceStatus)
                        // 如果为空证明没查状态，直接查所有勾选认证的状态
                        .in(StringUtils.isBlank(invoiceStatus) && "0".equals(certification),"invoice_status", statusList)
                        .in(batchNumbers[0] != null && !"".equals(batchNumbers[0]),"invoice_batch_number",batchNumbers)
                        .eq(createBy!=0,"create_by",createBy)
                        .eq(invoiceEntity.getInvoiceCode() !=null && !"".equals(invoiceEntity.getInvoiceCode()),"invoice_code",invoiceEntity.getInvoiceCode())
                        .eq(invoiceEntity.getInvoiceNumber() !=null && !"".equals(invoiceEntity.getInvoiceNumber()),"invoice_number",invoiceEntity.getInvoiceNumber())
                        .eq(invoiceEntity.getExpressNumber() !=null && !"".equals(invoiceEntity.getExpressNumber()),"express_number",invoiceEntity.getExpressNumber())
                        .eq(invoiceEntity.getEntrySuccessCode() !=null && !"".equals(invoiceEntity.getEntrySuccessCode()),"entry_success_code",invoiceEntity.getEntrySuccessCode())
                        .eq(invoiceEntity.getBelnr() !=null && !"".equals(invoiceEntity.getBelnr()),"belnr",invoiceEntity.getBelnr())
                        .eq(invoiceEntity.getInvoiceSapType() !=null && !"".equals(invoiceEntity.getInvoiceSapType()),"invoice_sap_type",invoiceEntity.getInvoiceSapType())
                        .like(invoiceEntity.getInvoicePurchaserCompany() !=null && !"".equals(invoiceEntity.getInvoicePurchaserCompany()),"invoice_purchaser_company",invoiceEntity.getInvoicePurchaserCompany())
                        .like(invoiceEntity.getInvoiceSellCompany() !=null && !"".equals(invoiceEntity.getInvoiceSellCompany()),"invoice_sell_company",invoiceEntity.getInvoiceSellCompany())
                        .eq(invoiceEntity.getInvoiceType()!=null && !"".equals(invoiceEntity.getInvoiceType()),"invoice_type",invoiceEntity.getInvoiceType())
                        .eq(invoiceEntity.getInvoiceEntity()!=null && !"".equals(invoiceEntity.getInvoiceEntity()),"invoice_entity",invoiceEntity.getInvoiceEntity())
                        .eq(invoiceEntity.getInvoiceFromto()!=null && !"".equals(invoiceEntity.getInvoiceFromto()),"invoice_fromto",invoiceEntity.getInvoiceFromto())
                        .eq(invoiceEntity.getInvoiceRecognition()!=null && !"".equals(invoiceEntity.getInvoiceRecognition()),"invoice_recognition",invoiceEntity.getInvoiceRecognition())
                        .ge(StringUtils.isNotBlank(entryDateArray),"entry_date",!StringUtils.isNotBlank(entryDateArray)?"":entryDateArray.split(",")[0])
                        .le(StringUtils.isNotBlank(entryDateArray),"entry_date",!StringUtils.isNotBlank(entryDateArray)?"":entryDateArray.split(",")[1])
                        .ge(StringUtils.isNotBlank(invoiceUploadDateArray),"invoice_upload_date",!StringUtils.isNotBlank(invoiceUploadDateArray)?"":invoiceUploadDateArray.split(",")[0])
                        .le(StringUtils.isNotBlank(invoiceUploadDateArray),"invoice_upload_date",!StringUtils.isNotBlank(invoiceUploadDateArray)?"":invoiceUploadDateArray.split(",")[1])
                        .ge(StringUtils.isNotBlank(invoiceCreateDateArray),"invoice_create_date",!StringUtils.isNotBlank(invoiceCreateDateArray)?"":invoiceCreateDateArray.split(",")[0])
                        .le(StringUtils.isNotBlank(invoiceCreateDateArray),"invoice_create_date",!StringUtils.isNotBlank(invoiceCreateDateArray)?"":invoiceCreateDateArray.split(",")[1])
                        .ge(invoiceEntity.getInvoiceTotalPriceBegin()!=null &&!"".equals(invoiceEntity.getInvoiceTotalPriceBegin()),"invoice_total_price",invoiceEntity.getInvoiceTotalPriceBegin())
                        .le(invoiceEntity.getInvoiceTotalPriceEnd()!=null &&!"".equals(invoiceEntity.getInvoiceTotalPriceEnd()),"invoice_total_price",invoiceEntity.getInvoiceTotalPriceEnd())
                        .eq(StringUtils.isNotBlank(invoiceStatus) &&!"undefined".equals(invoiceStatus),"invoice_status",invoiceStatus)
                        .in(batchNumbers[0] != null && !"".equals(batchNumbers[0]),"invoice_batch_number",batchNumbers)
                        .apply(params.get(Constant.SQL_FILTER) != null, (String)params.get(Constant.SQL_FILTER))
        );
        return new PageUtils(page);
    }

    @Override
    public List<InputInvoiceEntity> getListAndCreateName(List<InputInvoiceEntity> list, String createUserName) {
        return baseMapper.getListAndCreateName(list, createUserName);
    }

    /**
     * 为集合插入批次号
     *
     * @param invoiceEntities
     */
    @Override
    public void setBatchNumber(List<InputInvoiceEntity> invoiceEntities) {
        for (int i = 0; i < invoiceEntities.size(); i++) {
            InputInvoiceBatchEntity invoiceBatchEntity = invoiceBatchService.getBatchNumber(invoiceEntities.get(i));
            if (invoiceBatchEntity != null) {
                invoiceEntities.get(i).setBatchNumber(invoiceBatchEntity.getInvoiceBatchNumber());
            } else {
                invoiceEntities.get(i).setBatchNumber(null);
            }
        }
    }

    @Override
    @DataFilter(deptId = "company_id",subDept = true, user = false)
    public PageUtils findBillList(Map<String, Object> params, InputInvoiceEntity invoiceEntity) {
        String invoiceUploadDateArray = (String) params.get("invoiceUploadDateArray");
        String invoiceCreateDateArray = (String) params.get("invoiceCreateDateArray");
        String ientryDateArray = (String) params.get("entryDate");
        Integer createBy = invoiceEntity.getCreateBy();
        String invoiceStatus = (String) params.get("invoiceStatus");

        String key = (String) params.get("paramKey");

        // 根据菜单类型查询不同的数据
        String menuType = (String) params.get("menuType");
        List<String> statusList = InvoiceUtils.invoiceStatusList(menuType,invoiceStatus);

        IPage<InputInvoiceEntity> page = this.page(
                new Query<InputInvoiceEntity>().getPage(params, null, true),
                new QueryWrapper<InputInvoiceEntity>()
                        .orderByDesc("id")
                        .eq("invoice_fromto", "单票查询")
                        .like(StringUtils.isNotBlank(key), "name", key)
                        .eq(StrUtil.isNotBlank(invoiceStatus), "invoice_status", invoiceStatus)
                        .in(CollUtil.isNotEmpty(statusList) && StrUtil.isBlank(invoiceStatus), "invoice_status", statusList)
                        .eq(createBy != 0, "create_by", createBy)
                        .eq(invoiceEntity.getInvoiceCode() != null && !"".equals(invoiceEntity.getInvoiceCode()), "invoice_code", invoiceEntity.getInvoiceCode())
                        .eq(invoiceEntity.getInvoiceNumber() != null && !"".equals(invoiceEntity.getInvoiceNumber()), "invoice_number", invoiceEntity.getInvoiceNumber())
                        .like(invoiceEntity.getInvoicePurchaserCompany() != null && !"".equals(invoiceEntity.getInvoicePurchaserCompany()), "invoice_purchaser_company", invoiceEntity.getInvoicePurchaserCompany())
                        .like(invoiceEntity.getInvoiceSellCompany() != null && !"".equals(invoiceEntity.getInvoiceSellCompany()), "invoice_sell_company", invoiceEntity.getInvoiceSellCompany())
                        .eq(invoiceEntity.getInvoiceType() != null && !"".equals(invoiceEntity.getInvoiceType()), "invoice_type", invoiceEntity.getInvoiceType())
                        .eq(invoiceEntity.getInvoiceEntity() != null && !"".equals(invoiceEntity.getInvoiceEntity()), "invoice_entity", invoiceEntity.getInvoiceEntity())
                        .ge(StringUtils.isNotBlank(invoiceUploadDateArray), "invoice_upload_date", !StringUtils.isNotBlank(invoiceUploadDateArray) ? "" : invoiceUploadDateArray.split(",")[0])
                        .le(StringUtils.isNotBlank(invoiceUploadDateArray), "invoice_upload_date", !StringUtils.isNotBlank(invoiceUploadDateArray) ? "" : invoiceUploadDateArray.split(",")[1])
                        .ge(StringUtils.isNotBlank(invoiceCreateDateArray), "invoice_create_date", !StringUtils.isNotBlank(invoiceCreateDateArray) ? "" : invoiceCreateDateArray.split(",")[0])
                        .le(StringUtils.isNotBlank(invoiceCreateDateArray), "invoice_create_date", !StringUtils.isNotBlank(invoiceCreateDateArray) ? "" : invoiceCreateDateArray.split(",")[1])
                        .ge(StringUtils.isNotBlank(ientryDateArray), "entry_date", !StringUtils.isNotBlank(ientryDateArray) ? "" : ientryDateArray.split(",")[0])
                        .le(StringUtils.isNotBlank(ientryDateArray), "entry_date", !StringUtils.isNotBlank(ientryDateArray) ? "" : ientryDateArray.split(",")[1])
                        .ge(invoiceEntity.getInvoiceTotalPriceBegin() != null && !"".equals(invoiceEntity.getInvoiceTotalPriceBegin()), "invoice_total_price", invoiceEntity.getInvoiceTotalPriceBegin())
                        .le(invoiceEntity.getInvoiceTotalPriceEnd() != null && !"".equals(invoiceEntity.getInvoiceTotalPriceEnd()), "invoice_total_price", invoiceEntity.getInvoiceTotalPriceEnd())
                        .apply(params.get(Constant.SQL_FILTER) != null, (String)params.get(Constant.SQL_FILTER))
        );
        return new PageUtils(page);
    }

    @Override
    public List<InputInvoiceEntity> getListByBatchId(InputInvoiceEntity invoiceEntity) {
        return this.baseMapper.getListByBatchId(invoiceEntity);
    }

    @Override
    public List<InputInvoiceEntity> getListByBatchIds(InputInvoiceEntity invoiceEntity) {
        return this.baseMapper.getListByBatchIds(invoiceEntity);
    }

    //传入发票识别
    //TODO
    @Override
    public String functionSaveInvoice(InputInvoiceEntity invoiceEntity) {
        SimpleDateFormat sdf_result = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fileName = "/data" + invoiceEntity.getInvoiceImage();
        String content = "";

        content = getInvoiceOCRresult(invoiceEntity);
        invoiceEntity.setCreateBy(ShiroUtils.getUserId().intValue());
        invoiceEntity.setCompanyId(ShiroUtils.getUserEntity().getDeptId().intValue());

        if (content.startsWith("<h1>Server Error (500)</h1>")) {
            invoiceEntity.setInvoiceErrorDescription("OCR接口识别失败!");
            invoiceEntity.setInvoiceStatus(InputConstant.InvoiceStatus.RECOGNITION_FAILED.getValue());
            invoiceEntity.setInvoiceUploadType("1");
            invoiceEntity.setInvoiceReturn("0");
            invoiceEntity.setInvoiceDelete("0");
            invoiceEntity.setInvoiceCreateDate(sdf_result.format(new Date()));
            if (invoiceEntity.getId() == null) {
                save(invoiceEntity);
            } else {
                update(invoiceEntity);
            }
            //将该接口的日志生成到服务器中
            Map<String, String> map = new HashMap<>();
            map.put("fileName", fileName);
            String inParam = map.toString();
            String outParam = content;
            createLog(inParam, outParam, "functionSaveInvoice");
            return "500_";  //ocr 500
        }

        if (content.equals("2")) {
            //将该接口的日志生成到服务器中
            Map<String, String> map = new HashMap<>();
            map.put("invoiceEntity", invoiceEntity.toString());
            String inParam = map.toString();
            String outParam = content;
            createLog(inParam, outParam, "functionSaveInvoice");
            return "501_";  //方法异常
        }
        JSONArray jsonArray = null;
        jsonArray = JSONArray.fromObject("[" + content + "]");

        System.out.println(content);
        System.out.println(jsonArray);
        Pattern pattern = Pattern.compile("[0-9]*");
        if (jsonArray.size() > 0) {
            for (int j = 0; j < jsonArray.size(); j++) {
                JSONObject jsb = jsonArray.getJSONObject(0);
                Object error = jsb.get("errorMsg");

                if (error != null) {
                    invoiceEntity.setInvoiceErrorDescription(error.toString());
//
                    invoiceEntity.setInvoiceStatus(InputConstant.InvoiceStatus.RECOGNITION_FAILED.getValue());
                    invoiceEntity.setInvoiceUploadType("1");
                    invoiceEntity.setInvoiceReturn("0");
                    invoiceEntity.setInvoiceDelete("0");
                    invoiceEntity.setInvoiceCreateDate(sdf2.format(new Date()));
                    if (invoiceEntity.getId() == null) {
                        save(invoiceEntity);
                    } else {
                        update(invoiceEntity);
                    }
                    //将该接口的日志生成到服务器中
                    Map<String, String> map = new HashMap<>();
                    map.put("invoiceEntity", invoiceEntity.toString());
                    String inParam = map.toString();
                    String outParam = content;
                    createLog(inParam, outParam, "functionSaveInvoice");
                    return "400_";   //返回结果400，无数据
                } else {
                    JSONArray data = JSONArray.fromObject(jsb.get("scan_result"));
                    System.out.println(data);
                    if (data.size() > 0) {
                        for (int k = 0; k < data.size(); k++) {
                            if (data.getJSONObject(k).get("vat_type") != null && !"".equals(data.getJSONObject(k).get("vat_type"))) {
                                invoiceEntity.setInvoiceEntity((String) data.getJSONObject(k).get("vat_type"));
                                if (data.getJSONObject(k).get("vat_type").equals("电子发票")) {
                                    invoiceEntity.setInvoiceType("电子");
                                } else {
                                    if (data.getJSONObject(k).get("printed_number") != null && !"".equals(data.getJSONObject(k).get("printed_number"))) {
                                        invoiceEntity.setInvoicePrintedNumber((String) data.getJSONObject(k).get("printed_number"));
                                    }
                                    if (data.getJSONObject(k).get("printed_code") != null && !"".equals(data.getJSONObject(k).get("printed_code"))) {
                                        invoiceEntity.setInvoicePrintedCode((String) data.getJSONObject(k).get("printed_code"));
                                    }
                                    invoiceEntity.setInvoiceType("纸质");
                                }
                            }
                            if (data.getJSONObject(k).get("number") != null && !"".equals(data.getJSONObject(k).get("number"))) {
                                invoiceEntity.setInvoiceNumber((String) data.getJSONObject(k).get("number"));

                            }
                            if (data.getJSONObject(k).get("code") != null && !"".equals(data.getJSONObject(k).get("code"))) {
                                invoiceEntity.setInvoiceCode((String) data.getJSONObject(k).get("code"));
                            }
                            if (data.getJSONObject(k).get("check_code") != null && !"".equals(data.getJSONObject(k).get("check_code"))) {
                                invoiceEntity.setInvoiceCheckCode((String) data.getJSONObject(k).get("check_code"));
                            }
                            if (data.getJSONObject(k).get("date") != null && !"".equals(data.getJSONObject(k).get("date"))) {
                                String createDate = (String) data.getJSONObject(k).get("date");
                                try {
                                    if (FormatUtil.formatDate(createDate)) {
                                        invoiceEntity.setInvoiceCreateDate(DateUtil.format(DateUtil.parseDate(createDate), "yyyy-MM-dd"));
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            if (data.getJSONObject(k).get("total_amount_without_tax") != null && !"".equals(data.getJSONObject(k).get("total_amount_without_tax"))) {
                                if (FormatUtil.formatPrice((String) data.getJSONObject(k).get("total_amount_without_tax"))) {
                                    invoiceEntity.setInvoiceFreePrice(new BigDecimal((String) data.getJSONObject(k).get("total_amount_without_tax")));
                                }
                            }
                        }
                    }
                }

            }
        }
        invoiceEntity.setInvoiceUploadType("1");
        invoiceEntity.setInvoiceReturn("0");
        invoiceEntity.setInvoiceDelete("0");

        invoiceEntity.setInvoiceUploadDate(sdf_result.format(new Date()));
        //判断解析的5要素全部不为空，设置invoiceStatus 已识别
        if (("专用发票".equals(invoiceEntity.getInvoiceEntity()) && (invoiceEntity.getInvoiceEntity() != null && !"".equals(invoiceEntity.getInvoiceEntity())) &&
                (invoiceEntity.getInvoiceNumber() != null && !"".equals(invoiceEntity.getInvoiceNumber())) &&
                (invoiceEntity.getInvoiceCode() != null && !"".equals(invoiceEntity.getInvoiceCode())) &&
                (invoiceEntity.getInvoiceCreateDate() != null && !"".equals(invoiceEntity.getInvoiceCreateDate())) &&
                (invoiceEntity.getInvoiceFreePrice() != null && !"".equals(invoiceEntity.getInvoiceFreePrice()))) ||
                ("普通发票".equals(invoiceEntity.getInvoiceEntity()) && (invoiceEntity.getInvoiceEntity() != null && !"".equals(invoiceEntity.getInvoiceEntity())) &&
                        (invoiceEntity.getInvoiceNumber() != null && !"".equals(invoiceEntity.getInvoiceNumber())) &&
                        (invoiceEntity.getInvoiceCode() != null && !"".equals(invoiceEntity.getInvoiceCode())) &&
                        (invoiceEntity.getInvoiceCreateDate() != null && !"".equals(invoiceEntity.getInvoiceCreateDate())) &&
                        (invoiceEntity.getInvoiceCheckCode() != null && !"".equals(invoiceEntity.getInvoiceCheckCode()))) ||
                ("电子发票".equals(invoiceEntity.getInvoiceEntity()) && (invoiceEntity.getInvoiceEntity() != null && !"".equals(invoiceEntity.getInvoiceEntity())) &&
                        (invoiceEntity.getInvoiceNumber() != null && !"".equals(invoiceEntity.getInvoiceNumber())) &&
                        (invoiceEntity.getInvoiceCode() != null && !"".equals(invoiceEntity.getInvoiceCode())) &&
                        (invoiceEntity.getInvoiceCreateDate() != null && !"".equals(invoiceEntity.getInvoiceCreateDate())) &&
                        (invoiceEntity.getInvoiceCheckCode() != null && !"".equals(invoiceEntity.getInvoiceCheckCode())))) {
            //纸质发票需判断小号是否与发票号匹配，不匹配即为串号
            if (invoiceEntity.getInvoiceEntity().equals("专用发票") || invoiceEntity.getInvoiceEntity().equals("普通发票")) {
                if (invoiceEntity.getInvoicePrintedCode() != null && !"".equals(invoiceEntity.getInvoicePrintedCode())) {
                    if (!invoiceEntity.getInvoicePrintedCode().equals(invoiceEntity.getInvoiceCode())) {
                        invoiceEntity.setInvoiceStatus(InputConstant.InvoiceStatus.DIFFERENCE.getValue());
                        invoiceEntity.setInvoiceErrorDescription("发票代码串号!");
                    } else {
                        if (invoiceEntity.getInvoicePrintedNumber() != null && !"".equals(invoiceEntity.getInvoicePrintedNumber())) {
                            if (invoiceEntity.getInvoicePrintedNumber() != null && !"".equals(invoiceEntity.getInvoicePrintedNumber())) {
                                if (!invoiceEntity.getInvoicePrintedNumber().equals(invoiceEntity.getInvoiceNumber())) {
                                    invoiceEntity.setInvoiceStatus(InputConstant.InvoiceStatus.DIFFERENCE.getValue());
                                    invoiceEntity.setInvoiceErrorDescription("发票号码串号!");
                                } else {
                                    invoiceEntity.setInvoiceStatus(InputConstant.InvoiceStatus.PENDING_VERIFICATION.getValue());
                                }
                            } else {
                                invoiceEntity.setInvoiceStatus(InputConstant.InvoiceStatus.DIFFERENCE.getValue());
                                invoiceEntity.setInvoiceErrorDescription("发票号码串号!");
                            }
                        }
                    }
                } else {
                    invoiceEntity.setInvoiceStatus(InputConstant.InvoiceStatus.DIFFERENCE.getValue());
                    invoiceEntity.setInvoiceErrorDescription("发票代码串号!");
                }
            } else {
                if (invoiceEntity.getInvoiceType().equals("电子") && invoiceEntity.getInvoiceEntity().equals("电子发票")) {
                    invoiceEntity.setInvoiceStatus(InputConstant.InvoiceStatus.PENDING_VERIFICATION.getValue());
                } else {
                    invoiceEntity.setInvoiceStatus(InputConstant.InvoiceStatus.RECOGNITION_FAILED.getValue());
                }
            }
        } else if (invoiceEntity.getInvoiceNumber() == null || "".equals(invoiceEntity.getInvoiceNumber())) {
            invoiceEntity.setInvoiceStatus(InputConstant.InvoiceStatus.RECOGNITION_FAILED.getValue());
            invoiceEntity.setInvoiceErrorDescription("发票号码识别失败！");
        } else if (invoiceEntity.getInvoiceCode() == null || "".equals(invoiceEntity.getInvoiceCode())) {
            invoiceEntity.setInvoiceStatus(InputConstant.InvoiceStatus.RECOGNITION_FAILED.getValue());
            invoiceEntity.setInvoiceErrorDescription("发票代码识别失败！");
        } else if (invoiceEntity.getInvoiceCreateDate() == null || "".equals(invoiceEntity.getInvoiceCreateDate())) {
            invoiceEntity.setInvoiceStatus(InputConstant.InvoiceStatus.RECOGNITION_FAILED.getValue());
            invoiceEntity.setInvoiceErrorDescription("开票日期识别失败！");
        } else if (invoiceEntity.getInvoiceFreePrice() == null || "".equals(invoiceEntity.getInvoiceFreePrice())) {
            invoiceEntity.setInvoiceStatus(InputConstant.InvoiceStatus.RECOGNITION_FAILED.getValue());
            invoiceEntity.setInvoiceErrorDescription("不含税价格识别失败！");
        } else if (invoiceEntity.getInvoiceCheckCode() == null || "".equals(invoiceEntity.getInvoiceCheckCode())) {
            invoiceEntity.setInvoiceStatus(InputConstant.InvoiceStatus.RECOGNITION_FAILED.getValue());
            invoiceEntity.setInvoiceErrorDescription("校验码识别失败！");
        }
        if (invoiceEntity.getId() == null) {
            save(invoiceEntity);
        } else {
            update(invoiceEntity);
        }
        Integer num = this.getOAInvoiceList(invoiceEntity.getInvoiceCode(), invoiceEntity.getInvoiceNumber());

        if (num >= 2) {
            //根据发票号码和发票代码更新发票重复状态
            this.updateRepeat(invoiceEntity.getInvoiceCode(), invoiceEntity.getInvoiceNumber(), "1");
            invoiceEntity.setRepeatBill("1");
            // 新发票状态 重复也写进状态字段里 author zk
            invoiceEntity.setInvoiceStatus(InputConstant.InvoiceStatus.REPEAT.getValue());
            invoiceEntity.setInvoiceErrorDescription("发票重复！");
        } else {
            //根据发票号码和发票代码更新发票重复状态
            this.updateRepeat(invoiceEntity.getInvoiceCode(), invoiceEntity.getInvoiceNumber(), "0");
            invoiceEntity.setRepeatBill("0");
        }
        update(invoiceEntity);
        //将该接口的日志生成到服务器中
        Map<String, String> map = new HashMap<>();
        map.put("invoiceEntity", invoiceEntity.toString());
        String inParam = map.toString();
        String outParam = content;
        createLog(inParam, outParam, "functionSaveInvoice");
        return "0";
    }


    /**
     * 根据发票代码和发票号码更新重复发票状态
     *
     * @param invoiceCode
     * @param invoiceNumber
     */
    @Override
    public void updateRepeat(String invoiceCode, String invoiceNumber, String repeatBill) {
        this.baseMapper.updateRepeat(invoiceCode, invoiceNumber, repeatBill);
    }

    /**
     * 根据发票代码和发票号码判断是否重复
     *
     * @param invoiceCode
     * @param invoiceNumber
     * @return
     */
    @Override
    public Integer getOAInvoiceList(String invoiceCode, String invoiceNumber) {
        return this.baseMapper.getOAInvoiceList(invoiceCode, invoiceNumber);
    }

    @Override
    public void update(InputInvoiceEntity invoiceEntity) {
        this.baseMapper.update(invoiceEntity);
    }

    @Override
    public void deleteInvoiceByInvoiceBatchNumber(InputInvoiceEntity invoiceEntity) {
        this.baseMapper.deleteInvoiceByInvoiceBatchNumber(invoiceEntity);
    }

    @Override
    public List<InputInvoiceEntity> findInvoiceEntityById(InputInvoiceEntity invoiceEntity) {

        List<InputInvoiceEntity> invoiceSyncEntity = this.list(
                new QueryWrapper<InputInvoiceEntity>()
                        .eq(StringUtils.isNotBlank(invoiceEntity.getInvoiceEntity()), "invoice_entity", invoiceEntity.getInvoiceEntity())
                        .eq(StringUtils.isNotBlank(invoiceEntity.getInvoiceNumber()), "invoice_number", invoiceEntity.getInvoiceNumber())
                        .eq(StringUtils.isNotBlank(invoiceEntity.getInvoiceCode()), "invoice_code", invoiceEntity.getInvoiceCode())
                        .eq(StringUtils.isNotBlank(invoiceEntity.getInvoiceCreateDate()), "invoice_create_date", invoiceEntity.getInvoiceCreateDate())
                        .eq("invoice_free_price", invoiceEntity.getInvoiceFreePrice())
                        .like(StringUtils.isNotBlank(invoiceEntity.getInvoiceCheckCode()), "invoice_check_code", invoiceEntity.getInvoiceCheckCode())
                        .apply("id != " + invoiceEntity.getId())
                .eq(null !=invoiceEntity.getCompanyId(), "company_id", invoiceEntity.getCompanyId())
        );
        return invoiceSyncEntity;
    }

    @Override
    public String functionCheckTrue(InputInvoiceEntity invoiceEntity) {
        String msg = "";
        InvoiceInspectionParamBody invoiceInspectionParamBody = new InvoiceInspectionParamBody();
        String checkcode = invoiceEntity.getInvoiceCheckCode();
        if (StrUtil.isNotBlank(checkcode)) {
            invoiceInspectionParamBody.setCheckCode(StrUtil.subWithLength(checkcode, checkcode.length() - 6, checkcode.length()));
        }
        if (null != invoiceEntity.getInvoiceFreePrice()) {
            invoiceInspectionParamBody.setTotalAmount(invoiceEntity.getInvoiceFreePrice().toString());
        }
        invoiceInspectionParamBody.setBillingDate(invoiceEntity.getInvoiceCreateDate());
        invoiceInspectionParamBody.setInvoiceNumber(invoiceEntity.getInvoiceNumber());
        invoiceInspectionParamBody.setInvoiceCode(invoiceEntity.getInvoiceCode());

        List<InputInvoiceEntity> findEntity = this.findInvoiceEntityById(invoiceEntity);

        CallResult<BaseInvoice> invoiceCheck = invoiceSyncService.invoiceCheck(invoiceInspectionParamBody); // 验真接口


        InputInvoiceSyncEntity invoiceSyncEntity = invoiceSyncService.findInvoiceSync(invoiceEntity); // 底账库

        if(invoiceSyncEntity != null || (invoiceCheck != null && invoiceCheck.getData() != null)) {
            System.out.println("验真结果：" + invoiceCheck != null ? invoiceCheck.toString() : "未返回结果");

            if ("单票查询".equals(invoiceEntity.getInvoiceFromto())) {
                invoiceEntity.setInvoiceStatus(InputConstant.InvoiceStatus.PENDING_CERTIFIED.getValue());
            } else {
                invoiceEntity.setInvoiceStatus(InputConstant.InvoiceStatus.PENDING_MATCHED.getValue());
                // TODO: 47 使用 暂时改为待认证
//            invoiceEntity.setInvoiceStatus(InputConstant.InvoiceStatus.PENDING_CERTIFIED.getValue());
            }
            // 临时使用
            String purchaseTaxNo="";
            if(invoiceSyncEntity != null) {
                BigDecimal invoiceFreePrice = new BigDecimal((String) invoiceSyncEntity.getTotalAmount());
                BigDecimal totalTax = new BigDecimal((String) invoiceSyncEntity.getTotalTax());
                invoiceEntity.setInvoiceTotalPrice(invoiceFreePrice.add(totalTax));
                invoiceEntity.setInvoicePurchaserParagraph(invoiceSyncEntity.getPurchaserTaxNo());
                invoiceEntity.setInvoicePurchaserCompany(invoiceSyncEntity.getPurchaserName());
                invoiceEntity.setInvoiceSellParagraph(invoiceSyncEntity.getSalesTaxNo());
                invoiceEntity.setInvoiceSellCompany(invoiceSyncEntity.getSalesTaxName());
                invoiceEntity.setInvoiceTaxPrice(totalTax);
                invoiceEntity.setInvoiceCheckCode(invoiceSyncEntity.getCheckCode());
                purchaseTaxNo = invoiceSyncEntity.getPurchaserTaxNo();
                if(invoiceSyncEntity.getDeductible().equals("1")) { //已勾选
                    invoiceEntity.setInvoiceStatus(InputConstant.InvoiceStatus.SUCCESSFUL_AUTHENTICATION.getValue());
                    inputInvoiceTaxationService.updateTaxation(invoiceEntity);
                }
            }

            if(invoiceCheck.getData() != null) {
                purchaseTaxNo = invoiceCheck.getData().getPurchaserTaxNo();
                setInvoiceCheckEntity(invoiceCheck, invoiceEntity);
            }

            msg = "验真成功";
            if(CollUtil.isNotEmpty(findEntity)) {
                invoiceEntity.setInvoiceStatus(InputConstant.InvoiceStatus.REPEAT.getValue()); //重复上传
                msg = "异常：重复";
            }
            if ("验真成功".equals(msg)) {
                if ("报销单管理".equals(invoiceEntity.getInvoiceFromto())) {
                    if ("专用发票".equals(invoiceEntity.getInvoiceEntity())) {
                        invoiceEntity.setInvoiceStatus(InputConstant.InvoiceStatus.PENDING_CERTIFIED.getValue());

                    } else if ("普通发票".equals(invoiceEntity.getInvoiceEntity())) {
                        invoiceEntity.setInvoiceStatus(InputConstant.InvoiceStatus.FINISHED.getValue());
                    }
                }
            }
        } else {
            // 验真失败
            invoiceEntity.setInvoiceStatus(InputConstant.InvoiceStatus.VERIFICATION_FAILED.getValue());
            msg = "验真失败";
        }
        update(invoiceEntity);
        // 更新批次状态
        invoiceBatchService.updateStatus(invoiceEntity.getInvoiceBatchNumber());
        if ("验真成功".equals(msg)) {

//            ScheduleJobEntity scheduleJobEntity = scheduleJobService.getJob("autoDeductInvoiceTask");
//            // 验真成功是否自动勾选
//            if (null != scheduleJobEntity && 0 == scheduleJobEntity.getStatus()) {
//                invoiceSyncService.deductInvoices(invoiceEntity, "1");
//            }
        }
        return msg;
    }



    @Override
    public String forceCheckTrue(InputInvoiceEntity invoiceEntity) {
        String msg = "";
        InvoiceInspectionParamBody invoiceInspectionParamBody = new InvoiceInspectionParamBody();
        invoiceInspectionParamBody.setCheckCode(invoiceEntity.getInvoiceCheckCode());
        invoiceInspectionParamBody.setTotalAmount(invoiceEntity.getInvoiceFreePrice().toString());
        invoiceInspectionParamBody.setBillingDate(invoiceEntity.getInvoiceCreateDate());
        invoiceInspectionParamBody.setInvoiceNumber(invoiceEntity.getInvoiceNumber());
        invoiceInspectionParamBody.setInvoiceCode(invoiceEntity.getInvoiceCode());

        List<InputInvoiceEntity> findEntity = this.findInvoiceEntityById(invoiceEntity);

        CallResult<BaseInvoice> invoiceCheck = invoiceSyncService.invoiceCheck(invoiceInspectionParamBody);

        if(invoiceCheck != null && invoiceCheck.getData() != null) {
            //如果不在这个list中，就异常
//            if (!Constant.TAX_CODE.contains(invoiceCheck.getData().getPurchaserTaxNo())) {
//                // 验真失败
//                invoiceEntity.setInvoiceStatus("2");
//                invoiceEntity.setInvoiceErrorDescription("购方税号不匹配");
//                msg = "验真失败";
//                update(invoiceEntity);
//                return msg;
//            }
            setInvoiceCheckEntity(invoiceCheck, invoiceEntity);
            invoiceEntity.setInvoiceStatus(InputConstant.InvoiceStatus.PENDING_MATCHED.getValue());

            msg = "验真成功";
            if(CollUtil.isNotEmpty(findEntity)) {
                invoiceEntity.setInvoiceStatus(InputConstant.InvoiceStatus.REPEAT.getValue()); //重复上传
                msg = "异常：重复";
            }

        } else {
            // 验真失败
            invoiceEntity.setInvoiceStatus(InputConstant.InvoiceStatus.VERIFICATION_FAILED.getValue());
            msg = "验真失败";
        }
        update(invoiceEntity);

        if ("验真成功".equals(msg)) {
            // 更新批次状态
            invoiceBatchService.updateStatus(invoiceEntity.getBatchNumber());
//            ScheduleJobEntity scheduleJobEntity = scheduleJobService.getJob("autoDeductInvoiceTask");
//            // 验真成功是否自动勾选
//            if (null != scheduleJobEntity && 0 == scheduleJobEntity.getStatus()) {
//                invoiceSyncService.deductInvoices(invoiceEntity, "1");
//            }
        }
        return msg;
    }
    
    /**
     * 数据同步
     */
    @Override
    public List<InputInvoiceSyncEntity> getSkssqEntity(String nsrsbh, String createDate) {
        return this.getSkssqEntity(nsrsbh, createDate, "02");
    }

    @Override
    public void updateInvoiceGroup(InputInvoiceEntity invoiceEntity) {
        this.baseMapper.updateInvoiceGroup(invoiceEntity);
    }

    /**
     * 传入识别成功的发票与当前批次号进行分组
     *
     * @param invoiceEntityList
     * @param invoiceVoucherEntity
     * @return
     */
    @Override
    public Boolean functionGroupByInvoice(List<InputInvoiceEntity> invoiceEntityList, InputInvoiceVoucherEntity invoiceVoucherEntity) {
        List<InputInvoiceVoucherEntity> invoiceVoucherEntityList = new ArrayList<>();
        List<String> voucherNumberList = new ArrayList<>();
        try {
            //获取当前批次物料清单数据
//            invoiceVoucherEntityList = invoiceVoucherService.getListByBatchId(invoiceVoucherEntity);
            List<InputInvoiceVoucherEntity> invoiceVoucherEntities = invoiceVoucherService.getListByBatchId(invoiceVoucherEntity);
            for (int i = 0; i < invoiceVoucherEntities.size(); i++) {
                if (!voucherNumberList.contains(invoiceVoucherEntities.get(i).getInvoiceNumber() + invoiceVoucherEntities.get(i).getVoucherNumber())) {
                    voucherNumberList.add(invoiceVoucherEntities.get(i).getInvoiceNumber() + invoiceVoucherEntities.get(i).getVoucherNumber());
                    invoiceVoucherEntityList.add(invoiceVoucherEntities.get(i));
                }
            }
            int groupNumber = 0;
            int size = invoiceEntityList.size();
            int loopStop = 0;

            while (invoiceEntityList.size() > 0) {
                if (size < loopStop) {
                    return true;

                }
                loopStop = loopStop + 1;
                List<String> invoiceNumber = new ArrayList<>();
                InputInvoiceVoucherEntity vocherObj = new InputInvoiceVoucherEntity();
                int invoiceSum = 0;

                int i = invoiceEntityList.size() - 1;
                for (int j = 0; j < invoiceVoucherEntityList.size(); j++) {
                    System.out.println(invoiceEntityList.get(i).getInvoiceNumber());
                    System.out.println(invoiceVoucherEntityList.get(j).getInvoiceNumber());
                    if (!"".equals(invoiceEntityList.get(i).getInvoiceNumber()) && invoiceEntityList.get(i).getInvoiceNumber() != null) {
                        if (invoiceEntityList.get(i).getInvoiceNumber().equals(invoiceVoucherEntityList.get(j).getInvoiceNumber())) {
                            invoiceSum += 1;
                            vocherObj = invoiceVoucherEntityList.get(j);
                        }
                    }
                }
                //当前发票存在于清单里面
                if (invoiceSum > 1) {
                    groupNumber += 1;
                    invoiceEntityList.get(i).setInvoiceGroup(groupNumber);
                    this.updateInvoiceGroup(invoiceEntityList.get(i));
                    invoiceNumber.add(invoiceEntityList.get(i).getInvoiceNumber());
                    invoiceEntityList = deleteInvoiceList(invoiceEntityList, invoiceNumber);
                } else if (invoiceSum == 1) {
                    int voucherSum = 0;
                    for (int k = 0; k < invoiceVoucherEntityList.size(); k++) {
                        if (vocherObj.getVoucherNumber().equals(invoiceVoucherEntityList.get(k).getVoucherNumber())) {
                            voucherSum += 1;
                        }
                    }
                    List<InputInvoiceVoucherEntity> voucherEntityList = new ArrayList<>();
                    if (voucherSum > 1) {
                        voucherEntityList = invoiceVoucherService.getListByBatchIdAndVoucherNumber(vocherObj);
                        groupNumber += 1;
                        for (int p = 0; p < voucherEntityList.size(); p++) {
                            InputInvoiceEntity invoiceEntity = new InputInvoiceEntity();
                            invoiceEntity.setInvoiceBatchNumber(invoiceEntityList.get(i).getInvoiceBatchNumber());
                            invoiceEntity.setInvoiceNumber(voucherEntityList.get(p).getInvoiceNumber());
                            invoiceEntity.setInvoiceGroup(groupNumber);
                            this.updateInvoiceGroup(invoiceEntity);
                            invoiceNumber.add(voucherEntityList.get(p).getInvoiceNumber());
                        }
                        invoiceEntityList = deleteInvoiceList(invoiceEntityList, invoiceNumber);
                    } else {
                        groupNumber += 1;
                        invoiceEntityList.get(i).setInvoiceGroup(groupNumber);
                        this.updateInvoiceGroup(invoiceEntityList.get(i));
                        invoiceNumber.add(invoiceEntityList.get(i).getInvoiceNumber());
                        invoiceEntityList = deleteInvoiceList(invoiceEntityList, invoiceNumber);
                    }
                } else {
                    invoiceEntityList.get(i).setInvoiceErrorDescription("发票号码和清单上号码识别不一致");
//                    invoiceEntityList.get(i).setInvoiceStatus("3");
                    this.updateGroup(invoiceEntityList.get(i));
                    invoiceNumber.add(invoiceEntityList.get(i).getInvoiceNumber());
                    deleteInvoiceList(invoiceEntityList, invoiceNumber);
                }
            }
            InputInvoiceEntity invoiceEntity = new InputInvoiceEntity();
            invoiceEntity.setInvoiceBatchNumber(String.valueOf(invoiceVoucherEntity.getInvoiceBatchNumber()));
            List<InputInvoiceEntity> invoiceEntityList1 = this.getListByBatchId(invoiceEntity);
            List<Integer> groupList = new ArrayList<>();
            for (int i = 0; i < invoiceEntityList1.size(); i++) {
                if (invoiceEntityList1.get(i).getInvoiceGroup() != null) {
                    if (!groupList.contains(invoiceEntityList1.get(i).getInvoiceGroup())) {
                        groupList.add(invoiceEntityList1.get(i).getInvoiceGroup());
                    }
                }
            }
            Collections.sort(groupList);
            //按组号去查询每个组的发票
            if (groupList.size() > 0) {
                for (int i = 0; i < groupList.size(); i++) {
                    invoiceEntity.setInvoiceGroup(groupList.get(i));
                    List<InputInvoiceEntity> invoiceEntityList2 = this.getListByBatchIdAndInvoiceGroup(invoiceEntity);
                    //一个组有2张以上的发票，那么2张发票只能查到一个物料记录，否则为多对多
                    if (invoiceEntityList2.size() > 1) {
                        InputInvoiceVoucherEntity invoiceVoucherEntity1 = new InputInvoiceVoucherEntity();
                        invoiceVoucherEntity1.setInvoiceBatchNumber(invoiceVoucherEntity.getInvoiceBatchNumber());
                        List<String> invoiceId = new ArrayList<>();
                        for (int j = 0; j < invoiceEntityList2.size(); j++) {
                            invoiceId.add(invoiceEntityList2.get(j).getInvoiceNumber());
                        }
                        invoiceVoucherEntity1.setInvoiceIds(invoiceId);
                        List<InputInvoiceVoucherEntity> invoiceVoucherEntityList1 = invoiceVoucherService.getListGroupBy(invoiceVoucherEntity1);
                        if (invoiceVoucherEntityList1.size() > 1) {
                            this.updateGroupByBatchId(invoiceEntity);
                            return true;
                        }
                    }
                }
            }
            for (int i = 0; i < invoiceVoucherEntities.size(); i++) {
                List<String> invoiceList = new ArrayList<>();
                invoiceList.add(invoiceVoucherEntities.get(i).getInvoiceNumber());
                for (int j = 0; j < invoiceVoucherEntities.size(); j++) {
                    if (i != j) {
                        if (invoiceVoucherEntities.get(i).getVoucherNumber().equals(invoiceVoucherEntities.get(j).getVoucherNumber())) {
                            invoiceList.add(invoiceVoucherEntities.get(j).getInvoiceNumber());
                        }
                    }
                }
                if (invoiceList.size() > 1) {
                    List<String> list = removeString(invoiceList);
                    InputInvoiceEntity invoiceEntity1 = new InputInvoiceEntity();
                    invoiceEntity1.setInvoices(list);
                    invoiceEntity1.setInvoiceBatchNumber(String.valueOf(invoiceVoucherEntities.get(0).getInvoiceBatchNumber()));
                    List<InputInvoiceEntity> invoiceEntityList2 = this.getListByInvoiceNumberAndBatchId(invoiceEntity1);
                    for (int k = 0; k < invoiceEntityList2.size(); k++) {
                        for (int p = 0; p < invoiceEntityList2.size(); p++) {
                            if (invoiceEntityList2.get(k).getInvoiceGroup() != invoiceEntityList2.get(p).getInvoiceGroup()) {
                                this.updateGroupByBatchId(invoiceEntity);
                                return true;
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public List<InputInvoiceEntity> getListByBatchIdAndInvoiceGroup(InputInvoiceEntity invoiceEntity) {
        return this.baseMapper.getListByBatchIdAndInvoiceGroup(invoiceEntity);
    }

    @Override
    public List<InputInvoiceEntity> getListByInvoiceNumberAndBatchId(InputInvoiceEntity invoiceEntity) {
        return this.baseMapper.getListByInvoiceNumberAndBatchId(invoiceEntity);
    }

    @Override
    public void updateGroupByBatchId(InputInvoiceEntity invoiceEntity) {
        this.baseMapper.updateGroupByBatchId(invoiceEntity);
    }

    @Override
    public void updateGroup(InputInvoiceEntity invoiceEntity) {
        this.baseMapper.updateGroup(invoiceEntity);
    }


    //传入invoice对象验真
    //TODO
    @Override
    public int functionCheckTrue(InputInvoiceEntity invoiceEntity, String flag) {
        int data = 0;
        if (InputConstant.InvoiceStatus.PENDING_VERIFICATION.getValue().equals(invoiceEntity.getInvoiceStatus())
                || InputConstant.InvoiceStatus.VERIFICATION_FAILED.getValue().equals(invoiceEntity.getInvoiceStatus())) {
            Map<String, String> result = invoiceCheck(invoiceEntity);
            try {
                String value = result.get("response");
                JSONObject json = JSONObject.fromObject(value);
                System.out.println(json);
                String uuid = (String) json.get("uuid");
                String code = (String) json.get("code");
                String msg = (String) json.get("msg");
                String contentValue = (String) json.get("content");
                if ("0000".equals(code)) {
                    //Base64解密
                    invoiceEntity.setInvoiceStatus(InputConstant.InvoiceStatus.PENDING_MATCHED.getValue());
                    String val = Base64Util.decode(contentValue);
                    Gson gson = new Gson();
                    Map<String, Object> mapValue = new HashMap<String, Object>();
                    mapValue = gson.fromJson(val, new TypeToken<Map<String, Object>>() {
                    }.getType());
                    System.out.println(mapValue.toString());
                    String jfbz = (String) mapValue.get("jfbz");
                    Map<String, Object> fpxx = (Map<String, Object>) mapValue.get("fpxx");
                    // 作废标志
                    String zfbz = (String) fpxx.get("zfbz");
                    //备注
                    invoiceEntity.setInvoiceDescription((String) fpxx.get("bz"));
                    //购方名称
                    invoiceEntity.setInvoicePurchaserCompany((String) fpxx.get("gfmc"));
                    //购方税号
                    invoiceEntity.setInvoicePurchaserParagraph((String) fpxx.get("gfsh"));
                    //购方银行账号
                    invoiceEntity.setInvoicePurchaserBankAccount((String) fpxx.get("gfyhzh"));
                    //销方名称
                    invoiceEntity.setInvoiceSellCompany((String) fpxx.get("xfmc"));
                    //销方税号
                    invoiceEntity.setInvoiceSellParagraph((String) fpxx.get("xfsh"));
                    //销方银行账户
                    invoiceEntity.setInvoiceSellBankAccount((String) fpxx.get("xfyhzh"));
                    //价税合计
                    invoiceEntity.setInvoiceTotalPrice(new BigDecimal((String) fpxx.get("jshj")));
                    //校验码
                    invoiceEntity.setInvoiceCheckCodeDetails((String) fpxx.get("jym"));
                    //税额
                    invoiceEntity.setInvoiceTaxPrice(new BigDecimal((String) fpxx.get("se")));
                    //购方地址电话
                    invoiceEntity.setInvoicePurchaserAddress((String) fpxx.get("gfdzdh"));
                    //销方地址电话
                    invoiceEntity.setInvoiceSellAddress((String) fpxx.get("xfdzdh"));
                    String companyName = "";
                    //查询购方企业
                    String num = invoiceEntity.getInvoicePurchaserParagraph();
                    String com = invoiceEntity.getInvoicePurchaserCompany();
                    InputCompanyEntity companyEntity2 = new InputCompanyEntity();
                    if (num != null && !"".equals(num)) {
                        if (com != null) {
                            if (com.contains("(")) {
//                                InputCompanyEntity companyEntity1 = new InputCompanyEntity();
//                                companyEntity1.setCompanyName(com);
//                                companyEntity1.setCompanyDutyParagraph(num);
                                Integer count = sysDeptService.getCountByTaxCodAndName(num, com);
                                if (count == 0) {
                                    String companyName0 = com.replace("(", "（");
                                    companyName = companyName0.replace(")", "）");
//                                    companyEntity2.setCompanyName(companyName);
//                                    companyEntity2.setCompanyDutyParagraph(num);
                                    Integer count1 = sysDeptService.getCountByTaxCodAndName(num, companyName);
                                    if (count1 == 0) {
                                        InputOAInvoiceInfo oaInvoiceInfo2 = new InputOAInvoiceInfo();
                                        oaInvoiceInfo2.setWorning("0");
                                        oaInvoiceInfo2.setInvoicePurchaserParagraph(num);
                                        oaInvoiceInfo2.setInvoicePurchaserCompany(com);
                                        this.baseMapper.setWorn(oaInvoiceInfo2);
                                    } else {
                                        InputOAInvoiceInfo oaInvoiceInfo2 = new InputOAInvoiceInfo();
                                        oaInvoiceInfo2.setWorning("1");
                                        oaInvoiceInfo2.setInvoicePurchaserParagraph(num);
                                        oaInvoiceInfo2.setInvoicePurchaserCompany(com);
                                        this.baseMapper.setWorn(oaInvoiceInfo2);
                                    }
                                } else {
                                    InputOAInvoiceInfo oaInvoiceInfo2 = new InputOAInvoiceInfo();
                                    oaInvoiceInfo2.setWorning("1");
                                    oaInvoiceInfo2.setInvoicePurchaserParagraph(num);
                                    oaInvoiceInfo2.setInvoicePurchaserCompany(com);
                                    this.baseMapper.setWorn(oaInvoiceInfo2);
                                }
                            } else if (com.contains("（")) {
//                                InputCompanyEntity companyEntity1 = new InputCompanyEntity();
//                                companyEntity1.setCompanyName(com);
//                                companyEntity1.setCompanyDutyParagraph(num);
                                Integer count = sysDeptService.getCountByTaxCodAndName(num, com);
                                if (count == 0) {
                                    String companyName0 = com.replace("（", "(");
                                    companyName = companyName0.replace("）", ")");
//                                    companyEntity2.setCompanyName(companyName);
//                                    companyEntity2.setCompanyDutyParagraph(num);
                                    Integer count1 = sysDeptService.getCountByTaxCodAndName(num, companyName);
                                    if (count1 == 0) {
                                        InputOAInvoiceInfo oaInvoiceInfo2 = new InputOAInvoiceInfo();
                                        oaInvoiceInfo2.setWorning("0");
                                        oaInvoiceInfo2.setInvoicePurchaserParagraph(num);
                                        oaInvoiceInfo2.setInvoicePurchaserCompany(com);
                                        this.baseMapper.setWorn(oaInvoiceInfo2);
                                    } else {
                                        InputOAInvoiceInfo oaInvoiceInfo2 = new InputOAInvoiceInfo();
                                        oaInvoiceInfo2.setWorning("1");
                                        oaInvoiceInfo2.setInvoicePurchaserParagraph(num);
                                        oaInvoiceInfo2.setInvoicePurchaserCompany(com);
                                        this.baseMapper.setWorn(oaInvoiceInfo2);
                                    }
                                } else {
                                    InputOAInvoiceInfo oaInvoiceInfo2 = new InputOAInvoiceInfo();
                                    oaInvoiceInfo2.setWorning("1");
                                    oaInvoiceInfo2.setInvoicePurchaserParagraph(num);
                                    oaInvoiceInfo2.setInvoicePurchaserCompany(com);
                                    this.baseMapper.setWorn(oaInvoiceInfo2);
                                }
                            } else {
                                companyEntity2.setCompanyName(com);
                                companyEntity2.setCompanyDutyParagraph(num);
                                Integer count1 = sysDeptService.getCountByTaxCodAndName(num, com);
                                if (count1 == 0) {
                                    InputOAInvoiceInfo oaInvoiceInfo2 = new InputOAInvoiceInfo();
                                    oaInvoiceInfo2.setWorning("0");
                                    oaInvoiceInfo2.setInvoicePurchaserParagraph(num);
                                    oaInvoiceInfo2.setInvoicePurchaserCompany(com);
                                    this.baseMapper.setWorn(oaInvoiceInfo2);
                                } else {
                                    InputOAInvoiceInfo oaInvoiceInfo2 = new InputOAInvoiceInfo();
                                    oaInvoiceInfo2.setWorning("1");
                                    oaInvoiceInfo2.setInvoicePurchaserParagraph(num);
                                    oaInvoiceInfo2.setInvoicePurchaserCompany(com);
                                    this.baseMapper.setWorn(oaInvoiceInfo2);
                                }
                            }
                        }
                    } else {
                        if (com != null && com != "") {
                            InputOAInvoiceInfo oaInvoiceInfo2 = new InputOAInvoiceInfo();
                            oaInvoiceInfo2.setInvoicePurchaserCompany(com);
                            oaInvoiceInfo2.setWorning("0");
                            this.baseMapper.setWorn2(oaInvoiceInfo2);
                        }
                    }
                    //设置自动验真
                    if ("0".equals(flag)) {
                        invoiceEntity.setInvoiceVerifyTruth("0");
                    } else {
                        invoiceEntity.setInvoiceVerifyTruth("1");
                    }
                    //设置成功原因
//                    invoiceEntity.setInvoiceErrorDescription(msg);
                    //更新验真结果
                    System.out.println("===============发票验真数据");
                    System.out.println(invoiceEntity);
                    // 根据验真得到的企业名称查询企业信息表
//                    InputCompanyEntity companyEntity = companyService.getByName(invoiceEntity.getInvoicePurchaserParagraph());
                    SysDeptEntity deptEntity = sysDeptService.getByName(invoiceEntity.getInvoicePurchaserParagraph());
                    if (deptEntity != null) {
                        invoiceEntity.setCompanyId(deptEntity.getDeptId().intValue());
                    }
                    if (zfbz.equals("Y")) {
                        invoiceEntity.setInvoiceStatus(InputConstant.InvoiceStatus.VERIFICATION_FAILED.getValue());
                        invoiceEntity.setInvoiceErrorDescription("查验结果：该发票已作废");
                    }
                    update(invoiceEntity);
                    InputInvoiceMaterialEntity invoiceMaterialEntity = new InputInvoiceMaterialEntity();
                    List<String> invoiceIdsList = new ArrayList<>();
                    invoiceIdsList.add(String.valueOf(invoiceEntity.getId()));
                    invoiceMaterialEntity.setInvoiceIds(invoiceIdsList);
                    invoiceMaterialService.deleteInvoiceMaterialByInvoiceId(invoiceMaterialEntity);
                    List<InputInvoiceEntity> invoiceEntityList = new ArrayList<>();
                    invoiceEntityList = getListByBatchId(invoiceEntity);
                    // 分组计数
                    int groupSum = 0;
                    // 状态计数
                    int statusSum = 0;
                    for (int i = 0; i < invoiceEntityList.size(); i++) {
                        if (invoiceEntity.getInvoiceGroup() == invoiceEntityList.get(i).getInvoiceGroup()) {
                            groupSum += 1;
                            if (InputConstant.InvoiceStatus.PENDING_MATCHED.getValue().equals(invoiceEntityList.get(i).getInvoiceStatus())) {
                                statusSum += 1;
                            }
                        }
                    }
                    //当前分组计数和状态计数相等，可证明该分组下所有发票都已经认证
                    if (groupSum == statusSum) {
                        data = 1;
                    }
//                                //查验次数
//                                String cycs=(String)fpxx.get("cycs");
//                                //查验时间
//                                String cysj=(String)fpxx.get("cysj");
//                                //发票代码
//                                String fpdm=(String)fpxx.get("fpdm");
//                                //发票号码
//                                String fphm=(String)fpxx.get("fphm");
//                                //发票类型
//                                String fplx=(String)fpxx.get("fplx");
//                                //不含税金额
//                                String je=(String)fpxx.get("je");
//                                //机器编码
//                                String jpbm=(String)fpxx.get("jpbm");
//                                //开票日期
//                                String kprq=(String)fpxx.get("kprq");
//                                //作废标志
//                                String zfbz=(String)fpxx.get("zfbz");
//                                Map<String,Object> sphMap=new HashMap<String,Object>();
                    List<Object> sphList = new ArrayList<Object>();
                    sphList = (List<Object>) fpxx.get("sph");
                    for (int k = 0; k < sphList.size(); k++) {
                        LinkedTreeMap<String, String> sphMap = (LinkedTreeMap<String, String>) sphList.get(k);
                        InputInvoiceMaterialEntity invoiceMaterial = new InputInvoiceMaterialEntity();
                        invoiceMaterial.setInvoiceId(invoiceEntity.getId());
                        String spmc = sphMap.get("spmc");
                        if (spmc.contains("折扣额合计") || spmc.contains("原价合计")) {
                            continue;
                        }
                        //序号
                        invoiceMaterial.setSphXh(sphMap.get("xh"));
                        //商品名称
                        invoiceMaterial.setSphSpmc(sphMap.get("spmc"));
                        //规格型号
                        invoiceMaterial.setSphGgxh(sphMap.get("ggxh"));
                        //计量单位
                        if (StringUtils.isNotBlank(sphMap.get("jldw"))) {
                            invoiceMaterial.setSphJldw(sphMap.get("jldw"));
                        }
                        //数量
                        if (StringUtils.isNotBlank(sphMap.get("sl"))) {
                            invoiceMaterial.setSphSl(new BigDecimal(sphMap.get("sl")));
                        }
                        //不含税单价
                        if (StringUtils.isNotBlank(sphMap.get("dj"))) {
                            invoiceMaterial.setSphDj(getBigDecimal(sphMap.get("dj")));
                        }
                        //金额
                        if (StringUtils.isNotBlank(sphMap.get("je"))) {
                            invoiceMaterial.setSphJe(new BigDecimal(sphMap.get("je")));
                        }
                        //税率
                        invoiceMaterial.setSphSlv(sphMap.get("slv"));
                        //税额
                        invoiceMaterial.setSphSe(new BigDecimal(sphMap.get("se")));
                        invoiceMaterial.setStatus("0");
                        System.out.println("====================发票明细验真返回结果");
                        System.out.println(invoiceMaterial);
                        invoiceMaterialService.save(invoiceMaterial);
                    }
                } else if (
//                        "CY001".equals(code) ||
                        "CY002".equals(code) ||
                                "CY003".equals(code) ||
                                "CY004".equals(code) ||
                                "CY005".equals(code) ||
                                "CY006".equals(code) ||
                                "CY007".equals(code) ||
                                "CY008".equals(code) ||
                                "CY009".equals(code) ||
                                "CY0010".equals(code) ||
                                "CY0017".equals(code) ||
                                "CY0021".equals(code) ||
                                "CY0022".equals(code) ||
                                "CY0023".equals(code) ||
                                "CY0024".equals(code) ||
                                "CY0025".equals(code)) {
                    invoiceEntity.setInvoiceStatus(InputConstant.InvoiceStatus.VERIFICATION_FAILED.getValue());
                    invoiceEntity.setInvoiceErrorDescription(msg);
                    invoiceEntity.setInvoiceVerifyTruth(flag);
                    update(invoiceEntity);
                } else if ("CY0018".equals(code)) {
                    invoiceEntity.setInvoiceErrorDescription("查验失败");
                    invoiceEntity.setInvoiceStatus(InputConstant.InvoiceStatus.VERIFICATION_FAILED.getValue());
                    invoiceEntity.setInvoiceVerifyTruth(flag);
                    update(invoiceEntity);
                } else {
                    //验真失败
                    invoiceEntity.setInvoiceStatus(InputConstant.InvoiceStatus.VERIFICATION_FAILED.getValue());
                    //设置自动验真
                    invoiceEntity.setInvoiceVerifyTruth(flag);
                    //设置验真失败原因
                    invoiceEntity.setInvoiceErrorDescription(msg);
                    //更新验真结果
                    update(invoiceEntity);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        //将该接口的日志生成到服务器中
//        Map<String, String> map = new HashMap<>();
//        map.put("invoiceEntity", invoiceEntity.toString());
//        String inParam = map.toString();
//        Integer integer = data;
//        String outParam = integer.toString();
//        createLog(inParam, outParam, "invoiceCheck");
        return data;
    }

    @Override
    public String checkWhiteBlackEntity(List<String> bsartList) {
        // 获取红黑名单中采购订单
        InputWhiteBlackEntity whiteBlackEntity = whiteBlackDao.selectById(1);
        // 判断采购订单的校验模式
        // 获取对应模式下的采购订单名单
        InputWhiteBlackListEntity whiteBlackListEntity = new InputWhiteBlackListEntity();
        whiteBlackListEntity.setParentId(1);
        whiteBlackListEntity.setApprive(whiteBlackEntity.getApprove());
        List<InputWhiteBlackListEntity> whiteBlackListEntityList = whiteBlackListDao.getListByParentId(whiteBlackListEntity);
        // 状态为0时为红名单，1为黑名单
        if (whiteBlackEntity.getApprove().equals("0")) {
            // 红名单模式下判断当前SAP返回的订单类型名称是否包含在采购订单列表名单中
            for (int i = 0; i < whiteBlackListEntityList.size(); i++) {
                // 如果红名单中包含当前SAP中任意一条，返回true 允许进入匹配流程
                if (bsartList.contains(whiteBlackListEntityList.get(i).getOrderType())) {
                    return "1";
                }
            }
            // 循环执行完毕仍未发现匹配的红名单记录则返回false
            return "2";
        } else {
            // 黑名单模式下判断当前SAP返回的订单类型名称是否包含在采购订单列表名单中
            for (int i = 0; i < whiteBlackListEntityList.size(); i++) {
                // 如果黒名单中包含当前SAP中任意一条，返回false 不允许进入匹配流程
                if (bsartList.contains(whiteBlackListEntityList.get(i).getOrderType())) {
                    return whiteBlackListEntityList.get(i).getOrderType();
                }
            }
            // 循环执行完毕仍未发现匹配的黒名单记录则返回true
            return "4";
        }
    }

    @Override
    public String checkWhiteBlackByMatnrList(List<String> matnrList) {
        // 获取红黑名单中物料信息
        InputWhiteBlackEntity whiteBlackEntity = whiteBlackDao.selectById(3);
        // 判断物料的校验模式
        // 获取对应模式下的物料名单
        InputWhiteBlackListEntity whiteBlackListEntity = new InputWhiteBlackListEntity();
        whiteBlackListEntity.setParentId(whiteBlackEntity.getId());
        whiteBlackListEntity.setApprive(whiteBlackEntity.getApprove());
        List<InputWhiteBlackListEntity> whiteBlackListEntityList = whiteBlackListDao.getListByParentId(whiteBlackListEntity);
        // 状态为0时为红名单，1为黑名单
        if (whiteBlackEntity.getApprove().equals("0")) {
            for (int i = 0; i < whiteBlackListEntityList.size(); i++) {
                if (matnrList.contains(whiteBlackListEntityList.get(i).getMaterialNumber())) {
                    return "1";
                }
            }
            return "2";
        } else {
            for (int i = 0; i < whiteBlackListEntityList.size(); i++) {
                if (matnrList.contains(whiteBlackListEntityList.get(i).getMaterialNumber())) {
                    return "3";
                }
            }
            return "4";
        }
    }

    @Override
    public void updateByEnter(InputInvoiceEntity invoiceEntity) {
        this.baseMapper.updateByEnter(invoiceEntity);
    }

    @Override
    public Boolean any(String code, String number) {
        return this.baseMapper.getOAInvoiceList(code, number) > 0;
    }

    @Override
    public List<InputInvoiceEntity> getListByIds(List<Integer> invoiceIds) {
        return this.baseMapper.getListByIds(invoiceIds);
    }

    @Override
    public String checkWhiteBlackByEntry(List<String> lifnrList) {
        // 获取红黑名单中供应商信息
        InputWhiteBlackEntity whiteBlackEntity = whiteBlackDao.selectById(2);
        // 判断供应商的校验模式
        InputWhiteBlackListEntity whiteBlackListEntity = new InputWhiteBlackListEntity();
        // 获取对应模式下的供应商名单
        whiteBlackListEntity.setParentId(whiteBlackEntity.getId());
        whiteBlackListEntity.setApprive(whiteBlackEntity.getApprove());
        List<InputWhiteBlackListEntity> whiteBlackListEntityList = whiteBlackListDao.getListByParentId(whiteBlackListEntity);
        // 状态为0时为红名单，1为黑名单
        if (whiteBlackEntity.getApprove().equals("0")) {
            for (int i = 0; i < whiteBlackListEntityList.size(); i++) {
                if (lifnrList.contains(whiteBlackListEntityList.get(i).getSupplierCode())) {
                    return "1";
                }
            }
            return "2";
        } else {
            for (int i = 0; i < whiteBlackListEntityList.size(); i++) {
                if (lifnrList.contains(whiteBlackListEntityList.get(i).getSupplierCode())) {
                    return whiteBlackListEntityList.get(i).getSupplierCode();
                }
            }
            return "4";
        }
    }

    @Override
    public void updateList(List<InputInvoiceEntity> invoiceEntityList) {
        this.baseMapper.updateList(invoiceEntityList);
    }


    //每个分组 对应的发票，物料明细，物料清单，sap物料凭证 对应的集合
    //TODO
    @Override
    public String mate(InputInvoiceEntity invoiceEntity) {
        // 进入三单匹配，给批插入所属公司Id
        SysDeptEntity companyEntity = new SysDeptEntity();
//        companyEntity.setCompanyDutyParagraph(invoiceEntity.getInvoicePurchaserParagraph());
        companyEntity = sysDeptService.getByTaxCode(invoiceEntity.getInvoicePurchaserParagraph());
        InputInvoiceBatchEntity batchEntity = new InputInvoiceBatchEntity();
        batchEntity.setId(Integer.valueOf(invoiceEntity.getInvoiceBatchNumber()));
        if (companyEntity != null) {
            batchEntity.setCompanyId(companyEntity.getDeptId().intValue());
            invoiceBatchService.update(batchEntity);
        }
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        String result = "";
        //发票集合
        List<InputInvoiceEntity> invoiceList = new ArrayList<>();
        //发票物料集合
        List<InputInvoiceMaterialEntity> invoiceMaterialEntityList = new ArrayList<>();
        //sap物料集合
        List<InputInvoiceMaterialSapEntity> invoiceMaterialSapEntityList = new ArrayList<>();
        //物料清单集合
        List<InputInvoiceVoucherEntity> invoiceVoucherEntityList = new ArrayList<>();
        List<String> invoiceIds = new ArrayList<>();
        List<String> invoiceNumbers = new ArrayList<>();
        InputInvoiceMaterialEntity invoiceMaterialEntity = new InputInvoiceMaterialEntity();
        InputInvoiceVoucherEntity invoiceVoucherEntity = new InputInvoiceVoucherEntity();
        //物料清单的凭证号码集合
        List<String> mblnrList = new ArrayList<>();
        //根据批次id和发票分组，查询该分组下所有invoice
        invoiceList = this.getListByBatchIdAndInvoiceGroup(invoiceEntity);
        for (int i = 0; i < invoiceList.size(); i++) {
            invoiceIds.add(String.valueOf(invoiceList.get(i).getId()));
            invoiceNumbers.add(invoiceList.get(i).getInvoiceNumber());
        }

        invoiceMaterialEntity.setInvoiceIds(invoiceIds);
        //根据invoice_id 查询所有物料明细记录
        invoiceMaterialEntityList = invoiceMaterialService.getListByInvoiceId(invoiceMaterialEntity);
        invoiceVoucherEntity.setInvoiceIds(invoiceNumbers);
        invoiceVoucherEntity.setInvoiceBatchNumber(Integer.parseInt(invoiceEntity.getInvoiceBatchNumber()));
        //根据批次id 发票号码集合 查询对应的物料清单集合。
        invoiceVoucherEntityList = invoiceVoucherService.getListGroupBy(invoiceVoucherEntity);
        if (invoiceVoucherEntityList.size() > 0) {
            for (int i = 0; i < invoiceVoucherEntityList.size(); i++) {
                mblnrList.add(invoiceVoucherEntityList.get(i).getVoucherNumber());
            }
        } else {
            return "12_物料清单数据有误";
        }
        List<InputInvoiceMaterialSapEntity> invoiceMaterialSapEntityList1 = new ArrayList<>();
        List<String> mblnrs = new ArrayList<>();
        // SAP接口返回的订单类型名称
        List<String> bsartList = new ArrayList<>();
        try {
//            invoiceMaterialSapEntityList1 = StepClient.rfcCall(mblnrList);
            invoiceMaterialSapEntityList1 = invoiceMaterialSapService.getListByMBLNRids(mblnrList);
            if (invoiceMaterialSapEntityList1.size() > 0) {
                for (int i = 0; i < invoiceMaterialSapEntityList1.size(); i++) {
                    mblnrs.add(invoiceMaterialSapEntityList1.get(i).getMblnr());
                    if (StringUtils.isNotBlank(invoiceMaterialSapEntityList1.get(i).getBsart())) {
                        if (!bsartList.contains(invoiceMaterialSapEntityList1.get(i).getBsart())) {
                            bsartList.add(invoiceMaterialSapEntityList1.get(i).getBsart());
                        }
                    }
                }
                // 红黑名单校验,通过则进入三单匹配流程
                String data = this.checkWhiteBlackEntity(bsartList);
                if (!data.equals("1") && !data.equals("4")) {
                    if (data.equals("2")) {
                        if (bsartList.size() > 3) {
                            batchEntity.setThreeErrorDescription("采购订单类型：[" + bsartList.get(0) + "," + bsartList.get(1) + "," + bsartList.get(2) + "...] 被红黑名单过滤，不能进行三单匹配");
                        } else if (bsartList.size() == 3) {
                            batchEntity.setThreeErrorDescription("采购订单类型：[" + bsartList.get(0) + "," + bsartList.get(1) + "," + bsartList.get(2) + "] 被红黑名单过滤，不能进行三单匹配");
                        } else if (bsartList.size() == 2) {
                            batchEntity.setThreeErrorDescription("采购订单类型：[" + bsartList.get(0) + "," + bsartList.get(1) + "] 被红黑名单过滤，不能进行三单匹配");
                        } else if (bsartList.size() == 1) {
                            batchEntity.setThreeErrorDescription("采购订单类型：[" + bsartList.get(0) + "] 被红黑名单过滤，不能进行三单匹配");
                        } else {
                            batchEntity.setThreeErrorDescription("采购订单类型为空，不能进行三单匹配");
                        }
                    } else {
                        batchEntity.setThreeErrorDescription("采购订单类型：[" + data + "] 被红黑名单过滤，不能进行三单匹配");
                    }
                    batchEntity.setInvoiceBatchStatus("3");
                    batchEntity.setCheckWhiteBlackStatus("1");
                    invoiceBatchService.update(batchEntity);
                    return "15";
                } else {
                    // 通过红黑名单校验
                    batchEntity.setInvoiceBatchStatus("3");
                    batchEntity.setThreeErrorDescription("");
                    batchEntity.setCheckWhiteBlackStatus("");
                    invoiceBatchService.update(batchEntity);
                }
                InputInvoiceMaterialSapEntity invoiceMaterialSapEntity1 = new InputInvoiceMaterialSapEntity();
                invoiceMaterialSapEntity1.setMblnrs(mblnrs);
                invoiceMaterialSapEntity1.setBatchId(invoiceEntity.getInvoiceBatchNumber());
                invoiceMaterialSapService.deleteByBatchIdAndMblnr(invoiceMaterialSapEntity1);
                System.out.println(invoiceMaterialSapEntityList1);
                for (int i = 0; i < invoiceMaterialSapEntityList1.size(); i++) {

                    invoiceMaterialSapEntityList1.get(i).setMate("0");
                    invoiceMaterialSapEntityList1.get(i).setBatchId(invoiceEntity.getInvoiceBatchNumber());
                    invoiceMaterialSapEntityList1.get(i).setCreateTime(new Date());
                    String materialids = "";
                    for (int j = 0; j < invoiceMaterialEntityList.size(); j++) {
                        invoiceMaterialEntityList.get(j).setStatus("0");
                        invoiceMaterialEntityList.get(j).setMatchStatus("");
                        if (j < invoiceMaterialEntityList.size() - 1) {
                            materialids += invoiceMaterialEntityList.get(j).getId() + ",";
                        } else {
                            materialids += String.valueOf(invoiceMaterialEntityList.get(j).getId());
                        }
                    }
                    if (invoiceMaterialSapEntityList1.get(i).getId() == null) {
                        invoiceMaterialSapEntityList1.get(i).setMaterialIds(materialids);
                        invoiceMaterialSapService.save(invoiceMaterialSapEntityList1.get(i));
                    } else {
                        invoiceMaterialSapEntityList1.get(i).setMaterialIds(materialids);
                        invoiceMaterialSapService.update(invoiceMaterialSapEntityList1.get(i));

                    }
                }
                for (int i = 0; i < invoiceList.size(); i++) {
                    invoiceList.get(i).setInvoiceSapType(invoiceMaterialSapEntityList1.get(0).getBsart());
                    System.out.println("问题定位:mate");
                    this.update(invoiceList.get(i));
                }
            } else {
//                //将该接口的日志生成到服务器中
//                Map<String, String> map = new HashMap<>();
//                map.put("invoiceEntity", invoiceEntity.toString());
//                String inParam = map.toString();
//                String outParam = result;
//                createLog(inParam, outParam, "mate");
                return result;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        invoiceEntity.setInvoices(invoiceIds);
        //红黑名单设置
        InputInvoiceBatchEntity invoiceBatchEntity = new InputInvoiceBatchEntity();
        invoiceBatchEntity.setId(Integer.parseInt(invoiceEntity.getInvoiceBatchNumber()));
        InputInvoiceMaterialSapEntity invoiceMaterialSapEntity = new InputInvoiceMaterialSapEntity();
        invoiceMaterialSapEntity.setBatchId(invoiceEntity.getInvoiceBatchNumber());
        invoiceMaterialSapEntity.setMblnrs(mblnrs);


//        invoiceMaterialSapEntityList1 = invoiceMaterialSapService.getListByBatchId2(invoiceMaterialSapEntity);
        for (int j = 0; j < invoiceMaterialSapEntityList1.size(); j++) {
            invoiceMaterialSapEntityList1.get(j).setMate("0");
            invoiceMaterialSapEntityList1.get(j).setMatchStatus("");
        }
        String taxGroupRusult = matchTaxGroup(invoiceMaterialEntityList, invoiceMaterialSapEntityList1);
        if ("1".equals(taxGroupRusult)) {
            System.out.println("D通过税收分类");
            String groupResult = groupByDetails(invoiceMaterialEntityList, invoiceMaterialSapEntityList1);
            if ("1".equals(groupResult)) {
                invoiceMaterialSapEntityList1 = invoiceMaterialSapService.getListBySapId(invoiceMaterialSapEntityList1);
                System.out.println("E通过物料明细与开票商品名称");
                matchRcode(invoiceMaterialEntityList, invoiceMaterialSapEntityList1);
                System.out.println("A通过RCODE");
                matchPurchaser(invoiceMaterialEntityList, invoiceMaterialSapEntityList1);
                System.out.println("B通过购方名称和税号校验");
                matchSell(invoiceMaterialEntityList, invoiceMaterialSapEntityList1);
                System.out.println("C通过销方名称校验");
                matchTax(invoiceMaterialEntityList, invoiceMaterialSapEntityList1);
                System.out.println("F通过税率校验");
                matchCount(invoiceMaterialEntityList, invoiceMaterialSapEntityList1);
                System.out.println("G通过数量校验");
                matchFreeTaxPrice(invoiceMaterialEntityList, invoiceMaterialSapEntityList1);
                System.out.println("H通过不含税总价校验");
                matchTotalPrice(invoiceMaterialEntityList, invoiceMaterialSapEntityList1);
                System.out.println("I通过含税总价校验");

            }
        }
        // 批次下所有sap物料
        List<InputInvoiceMaterialSapEntity> invoiceMaterialSapEntityList2 = invoiceMaterialSapService.getListByBatchId(invoiceMaterialSapEntity);
        setMatchStatus(invoiceMaterialSapEntityList2, invoiceMaterialEntityList);
        invoiceMaterialEntityList = invoiceMaterialService.getListByInvoiceId(invoiceMaterialEntity);
        invoiceBatchService.updateInvocieStatus(invoiceMaterialEntityList);
        // 获取批次下所有发票
        List<InputInvoiceEntity> invoiceEntities = this.getListByBatchId(invoiceEntity);
        invoiceBatchService.setStatus(invoiceEntities);
        // 获取批次中的所有发票
        invoiceEntity.setInvoiceBatchNumber(invoiceMaterialSapEntityList1.get(0).getBatchId());
        List<InputInvoiceEntity> invoiceEntities2 = this.getListByBatchId(invoiceEntity);
        int count = 0;
        for (int i = 0; i < invoiceEntities2.size(); i++) {
            if (invoiceEntities2.get(i).getInvoiceStatus().equals("8") || invoiceEntities2.get(i).getInvoiceStatus().equals("11") ||
                    invoiceEntities2.get(i).getInvoiceStatus().equals("12") || invoiceEntities2.get(i).getInvoiceStatus().equals("15")) {
                count++;
            }
        }
        if (count == invoiceEntities2.size()) {
            List<Integer> integers = new ArrayList<>();
            for (int i = 0; i < invoiceEntities2.size(); i++) {
                integers.add(invoiceEntities2.get(i).getId());
            }
            // 获取批次中所有的发票明细
            List<InputInvoiceMaterialEntity> invoiceMaterialEntities = invoiceMaterialService.getByInvoiceIds(integers);
            // 获取批次中所有的sap明细
            InputInvoiceMaterialSapEntity invoiceMaterialSapEntity1 = new InputInvoiceMaterialSapEntity();
            invoiceMaterialSapEntity1.setBatchId(invoiceMaterialSapEntityList1.get(0).getBatchId());
            List<InputInvoiceMaterialSapEntity> invoiceMaterialSapEntities = invoiceMaterialSapService.getListByBatchId(invoiceMaterialSapEntity1);
            // 如果批次中的明细全部匹配成功，则进入入账环节
            if (checkMate(invoiceMaterialEntities, invoiceMaterialSapEntities)) {
                List<String> lifnrList = new ArrayList<>();
                List<String> matnrList = new ArrayList<>();
                for (int i = 0; i < invoiceMaterialSapEntities.size(); i++) {
                    if (!lifnrList.contains(invoiceMaterialSapEntities.get(i).getLifnr())) {
                        if (!lifnrList.contains(invoiceMaterialSapEntities.get(i).getLifnr())) {
                            lifnrList.add(invoiceMaterialSapEntities.get(i).getLifnr());
                        }
                        if (!matnrList.contains(invoiceMaterialSapEntities.get(i).getMatnr())) {
                            matnrList.add(invoiceMaterialSapEntities.get(i).getMatnr());
                        }
                    }
                }
                String data = this.checkWhiteBlackByEntry(lifnrList);
                if (data.equals("2") || data.equals("3")) {
                    invoiceBatchEntity.setCheckWhiteBlackStatus("2");
                    if (data.equals("2")) {
                        if (lifnrList.size() > 3) {
                            invoiceBatchEntity.setThreeErrorDescription("供应商（编号：" + lifnrList.get(0) + "," + lifnrList.get(1) + "," + lifnrList.get(2) + "..." + "）被红黑名单过滤，需手工入账");
                        } else if (lifnrList.size() == 3) {
                            invoiceBatchEntity.setThreeErrorDescription("供应商（编号：" + lifnrList.get(0) + "," + lifnrList.get(1) + "," + lifnrList.get(2) + "）被红黑名单过滤，需手工入账");
                        } else if (lifnrList.size() == 2) {
                            invoiceBatchEntity.setThreeErrorDescription("供应商（编号：" + lifnrList.get(0) + "," + lifnrList.get(1) + "）被红黑名单过滤，需手工入账");
                        } else {
                            invoiceBatchEntity.setThreeErrorDescription("供应商（编号：" + lifnrList.get(0) + "）被红黑名单过滤，需手工入账");
                        }
                    } else {
                        invoiceBatchEntity.setThreeErrorDescription("供应商（编号：" + data + "）被红黑名单过滤，需手工入账");
                    }
                    invoiceBatchService.update(invoiceBatchEntity);
                } else {
                    String r = this.checkWhiteBlackByMatnrList(matnrList);
                    if (r.equals("2") || r.equals("3")) {
                        invoiceBatchEntity.setCheckWhiteBlackStatus("3");
                        if (data.equals("2")) {
                            if (matnrList.size() > 3) {
                                invoiceBatchEntity.setThreeErrorDescription("物料（编码：" + matnrList.get(0) + "," + matnrList.get(1) + "," + matnrList.get(2) + "..." + "）被红黑名单过滤，需手工入账");
                            } else if (matnrList.size() == 3) {
                                invoiceBatchEntity.setThreeErrorDescription("物料（编码：" + matnrList.get(0) + "," + matnrList.get(1) + "," + matnrList.get(2) + "）被红黑名单过滤，需手工入账");
                            } else if (matnrList.size() == 2) {
                                invoiceBatchEntity.setThreeErrorDescription("物料（编码：" + matnrList.get(0) + "," + matnrList.get(1) + "）被红黑名单过滤，需手工入账");
                            } else {
                                invoiceBatchEntity.setThreeErrorDescription("物料（编码：" + matnrList.get(0) + "）被红黑名单过滤，需手工入账");
                            }
                        } else {
                            invoiceBatchEntity.setThreeErrorDescription("物料（编码：" + data + "）被红黑名单过滤，需手工入账");
                        }
                        invoiceBatchService.update(invoiceBatchEntity);
                    } else {
                        groupByGroupId(invoiceEntities2, null);
                    }
                }
            }
        }

        return result;
    }


    @Override
    public InputInvoiceEntity get(InputInvoiceEntity invoiceEntity) {
        return this.baseMapper.get(invoiceEntity);
    }

    @Override
    public void updateByIdPendingRefund(List<String> ids) {

        this.baseMapper.updateByIdPendingRefund(ids);
    }


    /**
     * 异常发票页面方法
     *
     * @param params
     * @return
     */
    @Override
    @DataFilter(subDept = true, user = false, deptId = "company_id")
    public PageUtils invoiceException(Map<String, Object> params) {
//        InputInvoiceEntity invoiceEntity = new InputInvoiceEntity();
        String key = (String) params.get("paramKey");
        String[] batchNumbers = ((String) params.get("invoiceBatchNumberList")).split(",");
        String invoiceCode = (String) params.get("invoiceCode");
        String invoiceNumber = (String) params.get("invoiceNumber");
        String invoicePurchaserCompany = (String) params.get("invoicePurchaserCompany");
        String invoiceSellCompany = (String) params.get("invoiceSellCompany");
        String invoiceType = (String) params.get("invoiceType");
        String invoiceFromto = (String) params.get("invoiceFromto");
        String invoiceStatus = (String) params.get("invoiceStatus");
        String invoiceTotalPrice = (String) params.get("invoiceTotalPrice");
        String invoiceUploadDateArray = (String) params.get("invoiceUploadDateArray");
        String invoiceCreateDateArray = (String) params.get("invoiceCreateDateArray");
//        String companyIds = ((String) params.get("companyIds"));
        String invoiceTotalPriceBegin = ((String) params.get("invoiceTotalPriceBegin"));
        String invoiceTotalPriceEnd = ((String) params.get("invoiceTotalPriceEnd"));
        // 替换原来的公司查询数据权限
//        List<String> taxCodes = null;
//        if (!StrUtil.isBlankIfStr(params.get(Constant.SQL_FILTER))) {
//            taxCodes = sysDeptService.getTaxCodeByIds(params);
//
//        }
        IPage<InputInvoiceEntity> page = this.page(
                new Query<InputInvoiceEntity>().getPage(params, null, true),
                new QueryWrapper<InputInvoiceEntity>()
                        .like(StringUtils.isNotBlank(key), "name", key)
                        .orderByDesc("id")
//                        .in(CollUtil.isNotEmpty(taxCodes), "invoice_purchaser_paragraph", taxCodes)
                        .apply("(invoice_status < 0) ")
                        .in(batchNumbers[0] != null && !"".equals(batchNumbers[0]), "invoice_batch_number", batchNumbers)
                        .eq(StringUtils.isNotBlank(invoiceCode), "invoice_code", invoiceCode)
                        .eq(StringUtils.isNotBlank(invoicePurchaserCompany), "invoice_purchaser_company", invoicePurchaserCompany)
                        .eq(StringUtils.isNotBlank(invoiceSellCompany), "invoice_sell_company", invoiceSellCompany)
                        .eq(StringUtils.isNotBlank(invoiceNumber), "invoice_number", invoiceNumber)
                        // FIXME type与entity反了
                        .eq(StringUtils.isNotBlank(invoiceType), "invoice_entity", invoiceType)
                        .eq(StringUtils.isNotBlank(invoiceFromto), "invoice_fromto", invoiceFromto)
                        .eq(StringUtils.isNotBlank(invoiceStatus), "invoice_status", invoiceStatus)
                        .eq(StringUtils.isNotBlank(invoiceTotalPrice), "invoice_totalPrice", invoiceTotalPrice)
                        .ge(StringUtils.isNotBlank(invoiceUploadDateArray), "invoice_upload_date", !StringUtils.isNotBlank(invoiceUploadDateArray) ? "" : invoiceUploadDateArray.split(",")[0])
                        .le(StringUtils.isNotBlank(invoiceUploadDateArray), "invoice_upload_date", !StringUtils.isNotBlank(invoiceUploadDateArray) ? "" : invoiceUploadDateArray.split(",")[1])
                        .ge(StringUtils.isNotBlank(invoiceCreateDateArray), "invoice_create_date", !StringUtils.isNotBlank(invoiceCreateDateArray) ? "" : invoiceCreateDateArray.split(",")[0])
                        .le(StringUtils.isNotBlank(invoiceCreateDateArray), "invoice_create_date", !StringUtils.isNotBlank(invoiceCreateDateArray) ? "" : invoiceCreateDateArray.split(",")[1])
                        .ge(invoiceTotalPriceBegin != null && !"".equals(invoiceTotalPriceBegin), "invoice_total_price", invoiceTotalPriceBegin)
                        .le(invoiceTotalPriceEnd != null && !"".equals(invoiceTotalPriceEnd), "invoice_total_price", invoiceTotalPriceEnd)
                        .apply(params.get(Constant.SQL_FILTER) != null, (String)params.get(Constant.SQL_FILTER))
        );
        return new PageUtils(page);

    }


    @Override
    public Integer getGroupSizeByBatchId(InputInvoiceEntity invoiceEntity) {
        return this.baseMapper.getGroupSizeByBatchId(invoiceEntity);
    }

    @Override
    public List<InputInvoiceVo> getVoListById(InputInvoiceVo invoiceVo) {
        return this.baseMapper.getVoListById(invoiceVo);
    }

    @Override
    @DataFilter(subDept = true, deptId = "company_id", user = false)
    public PageUtils findListByInvoiceIds(Map<String, Object> params, List<Integer> ids) {
        String invoiceCode = (String) params.get("invoiceCode");
        String invoiceNumber = (String) params.get("invoiceNumber");
        String invoicePurchaserCompany = (String) params.get("invoicePurchaserCompany");
        String invoiceSellCompany = (String) params.get("invoiceSellCompany");
        String invoiceType = (String) params.get("invoiceType");
        String invoiceFromto = (String) params.get("invoiceFromto");
        String invoiceStatus = (String) params.get("invoiceStatus");
        String invoiceTotalPrice = (String) params.get("invoiceTotalPrice");
        String invoiceUploadDateArray = (String) params.get("invoiceUploadDateArray");
        String invoiceCreateDateArray = (String) params.get("invoiceCreateDateArray");
        String invoiceTotalPriceBegin = ((String) params.get("invoiceTotalPriceBegin"));
        String invoiceTotalPriceEnd = ((String) params.get("invoiceTotalPriceEnd"));
        String invoiceEntity = (String) params.get("invoiceEntity");
//        String invoiceDelete = "5";
        if (CollUtil.isEmpty(ids)) {
           ids.add(-1);
        }
        IPage<InputInvoiceEntity> page = this.page(
                new Query<InputInvoiceEntity>().getPage(params, null, true),
                new QueryWrapper<InputInvoiceEntity>()
//                        .eq("invoice_delete", invoiceDelete)
                        .orderByDesc("id")
                        .in("id", ids)
                        .eq(StringUtils.isNotBlank(invoiceCode), "invoice_code", invoiceCode)
                        .eq(StringUtils.isNotBlank(invoicePurchaserCompany), "invoice_purchaser_company", invoicePurchaserCompany)
                        .eq(StringUtils.isNotBlank(invoiceSellCompany), "invoice_sell_company", invoiceSellCompany)
                        .eq(StringUtils.isNotBlank(invoiceNumber), "invoice_number", invoiceNumber)
//                            .eq(StringUtils.isNotBlank(invoiceType),"invoice_type",invoiceType)
                        .eq(StringUtils.isNotBlank(invoiceEntity), "invoice_entity", invoiceEntity)
                        .eq(StringUtils.isNotBlank(invoiceFromto), "invoice_fromto", invoiceFromto)
                        .eq(StringUtils.isNotBlank(invoiceStatus), "invoice_status", invoiceStatus)
                        .eq(StringUtils.isNotBlank(invoiceTotalPrice), "invoice_totalPrice", invoiceTotalPrice)
                        .ge(StringUtils.isNotBlank(invoiceUploadDateArray), "invoice_upload_date", !StringUtils.isNotBlank(invoiceUploadDateArray) ? "" : invoiceUploadDateArray.split(",")[0])
                        .le(StringUtils.isNotBlank(invoiceUploadDateArray), "invoice_upload_date", !StringUtils.isNotBlank(invoiceUploadDateArray) ? "" : invoiceUploadDateArray.split(",")[1])
                        .ge(StringUtils.isNotBlank(invoiceCreateDateArray), "invoice_create_date", !StringUtils.isNotBlank(invoiceCreateDateArray) ? "" : invoiceCreateDateArray.split(",")[0])
                        .le(StringUtils.isNotBlank(invoiceCreateDateArray), "invoice_create_date", !StringUtils.isNotBlank(invoiceCreateDateArray) ? "" : invoiceCreateDateArray.split(",")[1])
                        .ge(invoiceTotalPriceBegin != null && !"".equals(invoiceTotalPriceBegin), "invoice_total_price", invoiceTotalPriceBegin)
                        .le(invoiceTotalPriceEnd != null && !"".equals(invoiceTotalPriceEnd), "invoice_total_price", invoiceTotalPriceEnd)
                        .apply(params.get(Constant.SQL_FILTER) != null, (String)params.get(Constant.SQL_FILTER))

        );
        return new PageUtils(page);
    }

    /**
     * 获取 抵扣统计的申请、撤销结果
     *
     * @param nsrsbh
     * @param businessType
     * @param taskNo
     * @return
     */
    @Override
    public R getApplyResule(String nsrsbh, String businessType, String taskNo) {
        Map<String, String> data = applyResule(nsrsbh, businessType, taskNo);
        String dataValue = data.get("response");
        JSONObject json = JSONObject.fromObject(dataValue);
        System.out.println(json);
        String uuid = (String) json.get("uuid");
        String code = (String) json.get("code");
        String msg = (String) json.get("msg");
        String contentValue = (String) json.get("content");
        if (code.equals("0000")) {
            String val = Base64Util.decode(contentValue);
            Gson gson = new Gson();
            Map<String, Object> mapValue = new HashMap<String, Object>();
            mapValue = gson.fromJson(val, new TypeToken<Map<String, Object>>() {

            }.getType());
            System.out.println(mapValue);
            Map<String, Object> taskResult = (Map<String, Object>) mapValue.get("taskResult");
            if (businessType.equals("2")) {
                String taskStatus = (String) mapValue.get("taskStatus");
                return R.ok().put("taskStatus", taskStatus);
            } else {
                String businessStatus = (String) taskResult.get("businessStatus");
                // 申请统计 并且处理成功
                if (((String) mapValue.get("businessType")).equals("1")) {
                    Map<String, Object> businessData = (Map<String, Object>) taskResult.get("businessData");
                    if (businessData == null) {
                        businessStatus = "9";
                    }
                    return R.ok().put("businessData", businessData).put("businessStatus", businessStatus);
                    // 申请撤销 处理成功
                } else {
                    return R.ok().put("businessStatus", businessStatus);
                }
            }

        } else {
            return R.error(555, msg);
        }
    }

    @Override
    public void updateApply(InputInvoiceEntity invoiceEntity) {
        baseMapper.updateApply(invoiceEntity);


    }

    /**
     * 发起 抵扣统计的申请、撤销
     *
     * @param nsrsbh
     * @param businessType (1-申请统计2-撤销统计)
     * @return
     */
    @Override
    public R getApply(String nsrsbh, String businessType) {
        //获取结果集
        Map<String, String> data = apply(nsrsbh, businessType);
        String dataValue = data.get("response");
        JSONObject json = JSONObject.fromObject(dataValue);
        System.out.println(json);
        String uuid = (String) json.get("uuid");
        String code = (String) json.get("code");
        String msg = (String) json.get("msg");
        String contentValue = (String) json.get("content");

        if ("0000".equals(code)) {
            String val = Base64Util.decode(contentValue);
            Gson gson = new Gson();
            Map<String, Object> mapValue = new HashMap<String, Object>();
            mapValue = gson.fromJson(val, new TypeToken<Map<String, Object>>() {

            }.getType());
            System.out.println(mapValue);
            R r = new R();
            r.put("code", 0);
            r.put("msg", "success");
            if (msg.contains("存在未处理完的任务")) {
                r.put("msg", msg);
            }
            String taskNo = (String) mapValue.get("taskNo");
            System.out.println(taskNo);
            return r.put("taskNo", taskNo);
        } else {
            return R.error(Integer.valueOf(code), msg);
        }
    }


    /**
     * 确认申请
     *
     * @param nsrsbh
     * @param taskNo
     * @return
     */
    @Override
    public R getCensusResult(String nsrsbh, String taskNo) {
        //获取结果集
        Map<String, String> data = censusResult(nsrsbh, taskNo);
        String dataValue = data.get("response");
        JSONObject json = JSONObject.fromObject(dataValue);
        System.out.println(json);
        String uuid = (String) json.get("uuid");
        String code = (String) json.get("code");
        String msg = (String) json.get("msg");
        String contentValue = (String) json.get("content");
        if ("0000".equals(code)) {
            String val = Base64Util.decode(contentValue);
            Gson gson = new Gson();
            Map<String, Object> mapValue = new HashMap<String, Object>();
            mapValue = gson.fromJson(val, new TypeToken<Map<String, Object>>() {

            }.getType());
            System.out.println(mapValue);
            Map<String, String> taskResult = (Map<String, String>) mapValue.get("taskResult");
            String taskStatus = (String) mapValue.get("taskStatus");
            return R.ok().put("taskResult", taskResult).put("taskStatus", taskStatus);
        } else {
            return R.error(555, msg);
        }
    }

    /**
     * 申请确认统计
     *
     * @param nsrsbh
     * @param statisticsTime
     * @return
     */
    @Override
    public R getAffirmCensus(String nsrsbh, String statisticsTime) {
        //获取结果集
        Map<String, String> data = affirmCensus(nsrsbh, statisticsTime);
        String dataValue = data.get("response");
        JSONObject json = JSONObject.fromObject(dataValue);
        System.out.println(json);
        String uuid = (String) json.get("uuid");
        String code = (String) json.get("code");
        String msg = (String) json.get("msg");
        String contentValue = (String) json.get("content");
        if ("0000".equals(code)) {
            String val = Base64Util.decode(contentValue);
            Gson gson = new Gson();
            Map<String, Object> mapValue = new HashMap<String, Object>();
            mapValue = gson.fromJson(val, new TypeToken<Map<String, Object>>() {

            }.getType());
            System.out.println(mapValue);
            String taskNo = (String) mapValue.get("taskNo");
            return R.ok().put("taskNo", taskNo);
        } else {
            return R.error(Integer.valueOf(code), msg);
        }
    }

    @Override
    public List<InputInvoiceEntity> getByInvoiceIds(List<Integer> ids) {
        List<InputInvoiceEntity> results = new ArrayList<>();
        if (CollUtil.isNotEmpty(ids)) {
            results = baseMapper.selectList(new QueryWrapper<InputInvoiceEntity>().in("id", ids));
        }
        return results;
    }

    /**
     * 根据指定id获取发票集合
     *
     * @param invoiceEntity
     * @return
     */
    @Override
    public List<InputInvoiceEntity> getListById(InputInvoiceEntity invoiceEntity) {
        String[] arr = invoiceEntity.getInvoiceIds().split(",");
        int[] invoiceIds = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            invoiceIds[i] = Integer.parseInt(arr[i]);
        }
        return this.baseMapper.getListById(invoiceIds);
    }

    @Override
    public void checkStstus(List<InputInvoiceEntity> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getInvoiceStatus() != null && !"".equals(list.get(i).getInvoiceStatus())) {
                switch (list.get(i).getInvoiceStatus()) {
                    case "0":
//                        list.get(i).setInvoiceStatus("识别异常");
                        continue;
                    case "1":
                        list.get(i).setInvoiceStatus("");
                        continue;
                    case "2":
                        list.get(i).setInvoiceStatus("识别失败");
                        continue;
                    case "3":
                        list.get(i).setInvoiceStatus("待验真");
                        continue;
                    case "4":
                        list.get(i).setInvoiceStatus("验真失败");
                        continue;
                    case "5":
                        list.get(i).setInvoiceStatus("待匹配");
                        continue;
                    case "6":
                        list.get(i).setInvoiceStatus("匹配失败");
                        continue;
                    case "7":
                        list.get(i).setInvoiceStatus("待入账");
                        continue;
                    case "8":
                        list.get(i).setInvoiceStatus("入账失败");
                        continue;
                    case "9":
                        list.get(i).setInvoiceStatus("待认证");
                        continue;
                    case "10":
                        list.get(i).setInvoiceStatus("认证中");
                        continue;
                    case "11":
                        list.get(i).setInvoiceStatus("撤销认证");
                        continue;
                    case "12":
                        list.get(i).setInvoiceStatus("认证成功");
                        continue;
                    case "13":
                        list.get(i).setInvoiceStatus("认证失败");
                        continue;
                    case "14":
                        list.get(i).setInvoiceStatus("待统计");
                        continue;
                    case "15":
                        list.get(i).setInvoiceStatus("统计中");
                        continue;
                    case "16":
                        list.get(i).setInvoiceStatus("统计成功");
                        continue;
                    case "17":
                        list.get(i).setInvoiceStatus("统计失败");
                        continue;
                    case "18":
                        list.get(i).setInvoiceStatus("完成");
                        continue;
                    case "-1":
                        list.get(i).setInvoiceStatus("异常-重复");
                        continue;
                    case "-2":
                        list.get(i).setInvoiceStatus("异常-退票");
                        continue;
                    case "-3":
                        list.get(i).setInvoiceStatus("异常-删除");
                        continue;
                    case "-4":
                        list.get(i).setInvoiceStatus("异常-冲销");
                        continue;
                    case "-5":
                        list.get(i).setInvoiceStatus("异常-串号");
                        continue;
                }
            }
        }
    }

    @Override
    public int getListByShow() {
        return this.baseMapper.getListByShow();
    }

    @Override
    public List<InputInvoiceEntity> getListByItems(List<InputInvoiceEntity> list) {
        return this.baseMapper.getListByItems(list);
    }

    @Override
    public void updateByIdArray(Integer[] ids) {
        this.baseMapper.updateByIdArray(ids);
    }

    @Override
    public void relatedBatch(String invoiceIds, String batchId) {
        String[] invoiceId = invoiceIds.split(",");

        List<InputInvoiceEntity> invoiceEntityList = this.getListByIds(Convert.toList(Integer.class, invoiceId));
        for (InputInvoiceEntity invoice : invoiceEntityList) {
            invoice.setInvoiceBatchNumber(batchId);
            this.updateById(invoice);
        }
        // 关联后更新采购单状态
        invoiceBatchService.updateStatus(batchId);
    }

    @Override
    public void manualEntry(String invoiceIds) {
        String[] invoiceId = invoiceIds.split(",");

        List<InputInvoiceEntity> invoiceEntityList = this.getListByIds(Convert.toList(Integer.class, invoiceId));
        for (InputInvoiceEntity invoice : invoiceEntityList) {
            invoice.setInvoiceStatus(InputConstant.InvoiceStatus.PENDING_CERTIFIED.getValue());
            this.updateById(invoice);
            // 关联到计税模块表
            inputInvoiceTaxationService.saveTaxation(invoice,InputConstant.TaxationStats.BENRU_WEIREN.getValue());
            // 关联后更新采购单状态
            invoiceBatchService.updateStatus(invoice.getInvoiceBatchNumber());
        }
    }

    @Override
    public void manualMatch(String invoiceIds) {
        String[] invoiceId = invoiceIds.split(",");

        List<InputInvoiceEntity> invoiceEntityList = this.getListByIds(Convert.toList(Integer.class, invoiceId));
        for (InputInvoiceEntity invoice : invoiceEntityList) {
            invoice.setInvoiceStatus(InputConstant.InvoiceStatus.PENDING_ENTRY.getValue());
            this.updateById(invoice);
            // 关联到计税模块表
            inputInvoiceTaxationService.saveTaxation(invoice,InputConstant.TaxationStats.BENREN_WEIRU.getValue());
            // 关联后更新采购单状态
            invoiceBatchService.updateStatus(invoice.getInvoiceBatchNumber());
        }
    }

    @Override
    public boolean functionVerfy(InputInvoiceEntity invoiceEntity) {
        invoiceEntity.setVerfy(Boolean.TRUE);
        invoiceEntity.setInvoiceStatus(InputConstant.InvoiceStatus.SUCCESSFUL_AUTHENTICATION.getValue());
        update(invoiceEntity);
        // 关联后更新采购单状态
        invoiceBatchService.updateStatus(invoiceEntity.getInvoiceBatchNumber());
        return true;
    }

    @Override
    public List<InputInvoiceEntity> getByStatus(String status) {
        return this.list(new QueryWrapper<InputInvoiceEntity>().eq("invoice_status", status));
    }

    @Override
    public List<InputInvoiceEntity> getListByBatchIdAndStatus(String batchId,  List<String> status) {
        return this.list(new QueryWrapper<InputInvoiceEntity>()
                .eq("invoice_batch_number", batchId)
                .in(CollUtil.isNotEmpty(status),"invoice_status",  status));
    }

    @Override
    public String getLastSeq(String invoiceSeq) {
        return this.baseMapper.getLastSeq(invoiceSeq);
    }

    /*-------------------------------------------------------------------------------------private method------------------------------------------------------------------*/


    /**
     * 2.2.6申请确认统计
     * 申请统计后，需要进行确认统计，确认统计后，该统计表的数据才可以作为申报的依据 进行当期的抵扣申报工作
     * S000517
     *
     * @param nsrsbh
     * @param statisticsTime
     * @return
     */
    private Map<String, String> affirmCensus(String nsrsbh, String statisticsTime) {
        Map<String, String> querys = new HashMap<>();
        querys.put("appid", appid);// appid
        querys.put("serviceid", "S000517");
        querys.put("jtnsrsbh", jtnsrsbh);//集团税号
        querys.put("nsrsbh", nsrsbh);//集团下企业税号
        Map<String, Object> contentMap = new HashMap<String, Object>();
        contentMap.put("statisticsTime", statisticsTime);
        String content = Base64Util.encode(com.alibaba.fastjson.JSONObject.toJSONString(contentMap));
        querys.put("content", content);
        String signR = SignUtil.sign(appsecret, querys);
        querys.put("signature", signR);
        System.out.println("querys====" + querys);

        Map<String, String> result = HttpUtil.post(url, querys);
        System.out.println("result===" + result);
        return result;
    }


    /**
     * 获取 确认统计的结果
     * 根据任务编号查询该企业之前发起的 确认统计的结果。
     * S000518
     */
    private Map<String, String> censusResult(String nsrsbh, String taskNo) {
        Map<String, String> querys = new HashMap<>();
        querys.put("appid", appid);// appid
        querys.put("serviceid", "S000518");
        querys.put("jtnsrsbh", jtnsrsbh);//集团税号
        querys.put("nsrsbh", nsrsbh);//集团下企业税号
        Map<String, Object> contentMap = new HashMap<String, Object>();
        contentMap.put("taskNo", taskNo);
        String content = Base64Util.encode(com.alibaba.fastjson.JSONObject.toJSONString(contentMap));
        querys.put("content", content);
        String signR = SignUtil.sign(appsecret, querys);
        querys.put("signature", signR);
        System.out.println("querys====" + querys);

        Map<String, String> result = HttpUtil.post(url, querys);
        System.out.println("result===" + result);
        return result;
    }


    /**
     * 2.2.4 发起 抵扣统计的申请、撤销
     * 申请统计：
     * 对当前税款所属期所认证数据进行统计申请，查看当前属期认证数据的统计信息。（在申报期内 “申请统计”后，将锁定当期抵扣勾选操作，如果需要继续勾选操作，需要撤销统计）
     * 撤销统计：
     * 撤销当前税款所属期的统计信息，解锁当期抵扣勾选操作。
     *
     * @param nsrsbh       纳税人识别号
     * @param businessType 业务类型 (1-申请统计,2-撤销统计)
     * @return
     */
    private Map<String, String> apply(String nsrsbh, String businessType) {
        Map<String, String> querys = new HashMap<>();
        querys.put("appid", appid);// appid
        querys.put("serviceid", "S000515");
        querys.put("jtnsrsbh", jtnsrsbh);//集团税号
        querys.put("nsrsbh", nsrsbh);//集团下企业税号
        Map<String, Object> contentMap = new HashMap<String, Object>();
        contentMap.put("businessType", businessType);
        String content = Base64Util.encode(com.alibaba.fastjson.JSONObject.toJSONString(contentMap));
        querys.put("content", content);
        String signR = SignUtil.sign(appsecret, querys);
        querys.put("signature", signR);
        System.out.println("querys====" + querys);

        Map<String, String> result = HttpUtil.post(url, querys);
        System.out.println("result===" + result);
        return result;
    }

    /**
     * 2.2.5 获取 抵扣统计的申请、撤销结果
     * 根据任务编号查询该企业之前发起的 抵扣统计的申请、撤销 结果。
     *
     * @param nsrsbh
     * @param businessType
     * @return
     */
    private Map<String, String> applyResule(String nsrsbh, String businessType, String taskNo) {
        Map<String, String> querys = new HashMap<>();
        querys.put("appid", appid);// appid
        querys.put("serviceid", "S000516");
        querys.put("jtnsrsbh", jtnsrsbh);//集团税号
        querys.put("nsrsbh", nsrsbh);//集团下企业税号
        Map<String, Object> contentMap = new HashMap<String, Object>();
        contentMap.put("businessType", businessType);
        contentMap.put("taskNo", taskNo);
        String content = Base64Util.encode(com.alibaba.fastjson.JSONObject.toJSONString(contentMap));
        querys.put("content", content);
        String signR = SignUtil.sign(appsecret, querys);
        querys.put("signature", signR);
        System.out.println("querys====" + querys);

        Map<String, String> result = HttpUtil.post(url, querys);
        System.out.println("result===" + result);
        return result;
    }


    //防止sapList未全部执行所有校验就中断，补全match_status 字段至10位
    private void setMatchStatus(List<InputInvoiceMaterialSapEntity> invoiceMaterialSapEntityList1, List<InputInvoiceMaterialEntity> invoiceMaterialEntityList) {
        for (int i = 0; i < invoiceMaterialSapEntityList1.size(); i++) {
            if (10 - invoiceMaterialSapEntityList1.get(i).getMatchStatus().length() > 0) {
                int sum = 10 - invoiceMaterialSapEntityList1.get(i).getMatchStatus().length();
                for (int j = 0; j < sum; j++) {
                    invoiceMaterialSapEntityList1.get(i).setMatchStatus(invoiceMaterialSapEntityList1.get(i).getMatchStatus() + "0");
                }
                invoiceMaterialSapService.update(invoiceMaterialSapEntityList1.get(i));
            }
        }
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < invoiceMaterialEntityList.size(); i++) {
            ids.add(invoiceMaterialEntityList.get(i).getId());
        }
        InputInvoiceMaterialEntity invoiceMaterialEntity = new InputInvoiceMaterialEntity();
        invoiceMaterialEntity.setIds(ids);
        List<InputInvoiceMaterialEntity> materialEntityList = invoiceMaterialService.getListByIds(invoiceMaterialEntity);
        for (int i = 0; i < materialEntityList.size(); i++) {
            if (10 - materialEntityList.get(i).getMatchStatus().length() > 0) {
                int sum = 10 - materialEntityList.get(i).getMatchStatus().length();
                for (int j = 0; j < sum; j++) {
                    materialEntityList.get(i).setMatchStatus(materialEntityList.get(i).getMatchStatus() + "0");
                }
                invoiceMaterialService.update(materialEntityList.get(i));
            }
        }

    }

    // C 销方信息校验 （11111）
    private String matchSell(List<InputInvoiceMaterialEntity> invoiceMaterialEntityList, List<InputInvoiceMaterialSapEntity> invoiceMaterialSap) {
        String result = "1";
        // 将sap分组
        List<List<InputInvoiceMaterialSapEntity>> allList = byGroup(invoiceMaterialSap);
        for (int i = 0; i < allList.size(); i++) {
            List<InputInvoiceMaterialSapEntity> sapEntityList = allList.get(i);
            String materialArr[] = sapEntityList.get(0).getMaterialLineId().split(",");
            List<Integer> listList = new ArrayList<>();
            for (int j = 0; j < materialArr.length; j++) {
                listList.add(Integer.parseInt(materialArr[j]));
            }
            InputInvoiceMaterialEntity invoiceMaterialEntity = new InputInvoiceMaterialEntity();
            invoiceMaterialEntity.setIds(listList);
            List<InputInvoiceMaterialEntity> materialEntityList = invoiceMaterialService.getListByIds(invoiceMaterialEntity);
            String flag = checkSell(materialEntityList, sapEntityList);
            if (flag.equals("1")) {
                updateMatchPurchaser3(materialEntityList, sapEntityList, "1", "");
            } else {
                updateMatchPurchaser3(materialEntityList, sapEntityList, "0", "发票销方名称与SAP物料凭证销方名称不一致");
            }
        }
//        updateMatchPurchaser2(invoiceMaterialEntityList, invoiceMaterialSapEntityList, 4);
        return result;
    }

    private String checkSell(List<InputInvoiceMaterialEntity> invoiceMaterialEntityList, List<InputInvoiceMaterialSapEntity> invoiceMaterialSapEntityList) {
        String result = "";
        List<Integer> mIds = new ArrayList<>();
        for (int i = 0; i < invoiceMaterialEntityList.size(); i++) {
            mIds.add(invoiceMaterialEntityList.get(i).getInvoiceId());
        }
        List<InputInvoiceEntity> invoiceEntityList = this.getListByIds(mIds);
        int count = 0;
        for (int i = 0; i < invoiceEntityList.size(); i++) {
            int countS = 0;
            String invoiceSellCompany = invoiceEntityList.get(i).getInvoiceSellCompany().replace("（", "(").replace("）", ")").replace(" ", "");
            for (int j = 0; j < invoiceMaterialSapEntityList.size(); j++) {
                if (invoiceSellCompany.equals(invoiceMaterialSapEntityList.get(j).getName1().replace("（", "(").replace("）", ")").replace(" ", ""))) {
                    countS += 1;
                }
            }
            if (countS == invoiceMaterialSapEntityList.size()) {
                count += countS;
            }
        }
        if (invoiceEntityList.size() == 1) {
            if (count == invoiceMaterialSapEntityList.size()) {
                result = "1";
            } else {
                result = invoiceCheckActionService.checkAction("SELL");
            }
        } else {
            if (count == invoiceEntityList.size() * invoiceMaterialSapEntityList.size()) {
                result = "1";
            } else {
                result = invoiceCheckActionService.checkAction("SELL");
            }
        }

        return result;
    }


    //F校验税率 （111111）
    private String matchTax(List<InputInvoiceMaterialEntity> invoiceMaterialEntityList, List<InputInvoiceMaterialSapEntity> invoiceMaterialSap) {
        String result = "1";
        // 将sap分组
        List<List<InputInvoiceMaterialSapEntity>> allList = byGroup(invoiceMaterialSap);
        for (int i = 0; i < allList.size(); i++) {
            List<InputInvoiceMaterialSapEntity> sapEntityList = allList.get(i);
            String materialArr[] = sapEntityList.get(0).getMaterialLineId().split(",");
            List<Integer> listList = new ArrayList<>();
            for (int j = 0; j < materialArr.length; j++) {
                listList.add(Integer.parseInt(materialArr[j]));
            }
            InputInvoiceMaterialEntity invoiceMaterialEntity = new InputInvoiceMaterialEntity();
            invoiceMaterialEntity.setIds(listList);
            List<InputInvoiceMaterialEntity> materialEntityList = invoiceMaterialService.getListByIds(invoiceMaterialEntity);
            int count = 1;
            for (int k = 0; k < sapEntityList.size(); k++) {
                String kbetr = new BigDecimal(sapEntityList.get(k).getKbetr()).multiply(new BigDecimal(100)).toString();
                Integer kettr2 = Integer.valueOf(kbetr.substring(0, kbetr.indexOf(".")));
                for (int j = 0; j < materialEntityList.size(); j++) {
                    Float sl = Float.parseFloat(materialEntityList.get(j).getSphSlv().replace("%", ""));
                    Integer mslv = sl.intValue();
                    if (kettr2 == mslv) {
                        count += 1;
                    }
                }
            }
            if (count == (sapEntityList.size() * materialEntityList.size()) + 1) {
                updateMatchPurchaser3(materialEntityList, sapEntityList, "1", "");
            } else {
                updateMatchPurchaser3(materialEntityList, sapEntityList, "0", "税率不等");
            }
        }
        return result;
    }

    // G 校验sap和发票货品数量 （1111111）
    private String matchCount(List<InputInvoiceMaterialEntity> invoiceMaterialEntityList,
                              List<InputInvoiceMaterialSapEntity> invoiceMaterialSap) {
        String result = "1";
        // 将sap分组
        List<List<InputInvoiceMaterialSapEntity>> allList = byGroup(invoiceMaterialSap);

        for (int i = 0; i < allList.size(); i++) {
            List<InputInvoiceMaterialSapEntity> sapEntityList = allList.get(i);
            String materialArr[] = sapEntityList.get(0).getMaterialLineId().split(",");
            List<Integer> listList = new ArrayList<>();
            for (int j = 0; j < materialArr.length; j++) {
                listList.add(Integer.parseInt(materialArr[j]));
            }
            InputInvoiceMaterialEntity invoiceMaterialEntity = new InputInvoiceMaterialEntity();
            invoiceMaterialEntity.setIds(listList);
            List<InputInvoiceMaterialEntity> materialEntityList = invoiceMaterialService.getListByIds(invoiceMaterialEntity);
            Map<String, BigDecimal> map = getDiffer(materialEntityList, sapEntityList);
            //发票物料明细总数量
            BigDecimal sphSl = map.get("sphSl");
            //sap物料明细总数量
            BigDecimal mengeQm = map.get("mengeQm");

            BigDecimal sphSl2 = new BigDecimal(0);
            //sap物料明细总数量
            if (sphSl.compareTo(sphSl2) != 0 && mengeQm.compareTo(sphSl2) != 0) {
                if (sphSl.compareTo(mengeQm) == 0) {
                    updateMatchPurchaser3(materialEntityList, sapEntityList, "1", "");
                } else {
                    updateMatchPurchaser3(materialEntityList, sapEntityList, "0", "SAP物料数量与发票明细货品数量校验不通过");
                }
            } else {
                updateMatchPurchaser3(materialEntityList, sapEntityList, "0", "SAP物料数量与发票明细货品数量校验不通过");
            }

        }
        return result;
    }


    private String matchFreeTaxPrice(List<InputInvoiceMaterialEntity> invoiceMaterialEntityList, List<InputInvoiceMaterialSapEntity> invoiceMaterialSapEntityList) {
        String result = "1";
        List<List<InputInvoiceMaterialSapEntity>> allList = byGroup(invoiceMaterialSapEntityList);
        for (int i = 0; i < allList.size(); i++) {
            List<InputInvoiceMaterialSapEntity> sapEntityList = allList.get(i);
            String materialArr[] = sapEntityList.get(0).getMaterialLineId().split(",");
            List<Integer> listList = new ArrayList<>();
            for (int j = 0; j < materialArr.length; j++) {
                listList.add(Integer.parseInt(materialArr[j]));
            }
            InputInvoiceMaterialEntity invoiceMaterialEntity = new InputInvoiceMaterialEntity();
            invoiceMaterialEntity.setIds(listList);
            List<InputInvoiceMaterialEntity> materialEntityList = invoiceMaterialService.getListByIds(invoiceMaterialEntity);
            Map<String, BigDecimal> map = getDiffer(materialEntityList, sapEntityList);
            //发票物料不含税总金额
            BigDecimal mFreePrice = map.get("mFreePrice");
            //sap物料不含税总金额
            BigDecimal sapFreePrice = map.get("sapFreePrice");
            //获取金额容差范围
            BigDecimal fault = map.get("fault");
            String freeDiffer = (mFreePrice.subtract(sapFreePrice) + "").replace("-", "");
            if (Float.parseFloat(freeDiffer) <= fault.floatValue()) {
                updateMatchPurchaser3(materialEntityList, sapEntityList, "1", "");
            } else {
                updateMatchPurchaser3(materialEntityList, sapEntityList, "0", "发票与物料凭证的不含税总金额超出允差范围");
            }
        }
        return result;
    }

    // I 校验含税总价(111111111)
    private String matchTotalPrice(List<InputInvoiceMaterialEntity> invoiceMaterialEntityList, List<InputInvoiceMaterialSapEntity> invoiceMaterialSapEntityList) {
        String result = "1";
        List<List<InputInvoiceMaterialSapEntity>> allList = byGroup(invoiceMaterialSapEntityList);
        for (int i = 0; i < allList.size(); i++) {
            List<InputInvoiceMaterialSapEntity> sapEntityList = allList.get(i);
            String materialArr[] = sapEntityList.get(0).getMaterialLineId().split(",");
            List<Integer> listList = new ArrayList<>();
            for (int j = 0; j < materialArr.length; j++) {
                listList.add(Integer.parseInt(materialArr[j]));
            }
            InputInvoiceMaterialEntity invoiceMaterialEntity = new InputInvoiceMaterialEntity();
            invoiceMaterialEntity.setIds(listList);
            List<InputInvoiceMaterialEntity> materialEntityList = invoiceMaterialService.getListByIds(invoiceMaterialEntity);
            Map<String, BigDecimal> map = getDiffer(materialEntityList, sapEntityList);
            //发票物料含税总金额
            BigDecimal mTotalPrice = map.get("mTotalPrice");
            //sap物料含税总金额
            BigDecimal sapTotalPrice = map.get("sapTotalPrice");
            //获取金额容差范围
            BigDecimal fault = map.get("fault");
            String totalDiffer = (mTotalPrice.subtract(sapTotalPrice) + "").replace("-", "");
            if (Float.parseFloat(totalDiffer) <= fault.floatValue()) {
                updateMatchPurchaser3(materialEntityList, sapEntityList, "1", "");
            } else {
                updateMatchPurchaser3(materialEntityList, sapEntityList, "0", "发票与物料凭证的含税总金额超出允差范围");
            }
        }
        for (int i = 0; i < invoiceMaterialSapEntityList.size(); i++) {
            updateInvoice(invoiceMaterialSapEntityList.get(i));
        }
        return result;
    }

    /**
     * 获取数量和金额差
     *
     * @return
     */
    public Map<String, BigDecimal> getDiffer(List<InputInvoiceMaterialEntity> invoiceMaterialEntityList, List<InputInvoiceMaterialSapEntity> invoiceMaterialSapEntityList) {
        Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
        //发票物料明细总数量
        BigDecimal sphSl = new BigDecimal(0);
        //sap物料明细总数量
        BigDecimal mengeQm = new BigDecimal(0);
        //发票物料含税总金额
        BigDecimal mTotalPrice = new BigDecimal(0);
        //sap物料含税总金额
        BigDecimal sapTotalPrice = new BigDecimal(0);
        //发票物料不含税总金额
        BigDecimal mFreePrice = new BigDecimal(0);
        //sap物料不含税总金额
        BigDecimal sapFreePrice = new BigDecimal(0);
        //发票物料税额
        BigDecimal sphse = new BigDecimal(0);
        //sap物料税额
        BigDecimal wmwst = new BigDecimal(0);
        //发票单位
        String invoiceDW = "";
        //物料单位
        String sapDW = "";
        //获取金额容差范围
        InputInvoiceFaultTolerantEntity invoiceFaultTolerantEntity = new InputInvoiceFaultTolerantEntity();
        invoiceFaultTolerantEntity.setUnit("元");
        invoiceFaultTolerantEntity = invoiceFaultTolerantService.getByName(invoiceFaultTolerantEntity);
        boolean invoiceFlag = true;
        List<String> dwNameList = new ArrayList<>();
        for (int i = 0; i < invoiceMaterialEntityList.size(); i++) {
            String dw = invoiceMaterialEntityList.get(i).getSphJldw();
            if (dw != null && !"".equals(dw)) {
                dwNameList.add(dw);
                if (!invoiceMaterialEntityList.get(0).getSphJldw().equals(dw)) {
                    invoiceFlag = false;
                    continue;
                }
            }
        }
        for (int i = 0; i < invoiceMaterialEntityList.size(); i++) {
            mTotalPrice = mTotalPrice.add(invoiceMaterialEntityList.get(i).getSphJe().add(invoiceMaterialEntityList.get(i).getSphSe()));
            mFreePrice = mFreePrice.add(invoiceMaterialEntityList.get(i).getSphJe());
            sphse = sphse.add(invoiceMaterialEntityList.get(i).getSphSe());
        }
        if (invoiceFlag) {
            //TODO invoiceFlag判断是否所有发票行的 单位都一致，一致直接相加
            for (int i = 0; i < invoiceMaterialEntityList.size(); i++) {
                if (invoiceMaterialEntityList.get(i).getSphJldw() != null && !"".equals(invoiceMaterialEntityList.get(i).getSphJldw())) {
                    invoiceDW = invoiceMaterialEntityList.get(i).getSphJldw();
                    continue;
                }
            }
            for (int i = 0; i < invoiceMaterialEntityList.size(); i++) {
                BigDecimal sl = invoiceMaterialEntityList.get(i).getSphSl();
                sphSl = sphSl.add(sl == null ? new BigDecimal("0") : sl);
            }

        } else {
            //TODO，如不一致，需要查新单位转换关系，统计数量
            //将所有发票行的单位获取到以后去掉重复的单位
            List<String> listWithoutDuplicates = dwNameList.stream().distinct().collect(Collectors.toList());
            InputInvoiceUnitDetailsEntity invoiceUnitDetailsEntity = new InputInvoiceUnitDetailsEntity();
            invoiceUnitDetailsEntity.setDetailsNames(listWithoutDuplicates);
            //使用去重以后的单位去明细表，查出所有记录
            List<InputInvoiceUnitDetailsEntity> invoiceUnitDetailsEntityList = invoiceUnitDetailsService.getByDetailsNames(invoiceUnitDetailsEntity);
            //查出的所有记录的父级单位是否为同一个。如是，可以利用利率转换数量，如不是，不同父级单位的子单位无法转换数量
            boolean isSameParentDW = true;
            for (int i = 0; i < invoiceUnitDetailsEntityList.size(); i++) {
                if (!invoiceUnitDetailsEntityList.get(0).getInvoiceUnit().equals(invoiceUnitDetailsEntityList.get(i).getInvoiceUnit())) {
                    isSameParentDW = false;
                    continue;
                }
            }
            if (invoiceUnitDetailsEntityList.size() > 0) {
                invoiceDW = invoiceUnitDetailsEntityList.get(0).getUnitName();
            }
            if (isSameParentDW) {
                //如果是同一大类的单位，根据各自对大类的比例来统计数量
                for (int i = 0; i < invoiceMaterialEntityList.size(); i++) {
                    for (int j = 0; j < invoiceUnitDetailsEntityList.size(); j++) {
                        if (invoiceMaterialEntityList.get(i).getSphJldw().equals(invoiceUnitDetailsEntityList.get(j).getDetailsName())) {
                            BigDecimal sl = invoiceMaterialEntityList.get(i).getSphSl().divide(invoiceUnitDetailsEntityList.get(j).getDetailsVal());
                            sphSl = sphSl.add(sl == null ? new BigDecimal("0") : sl);
                        }
                    }
                }
            } else {
                //非同一大类单位，发票行无法统计数量
                for (int i = 0; i < invoiceMaterialEntityList.size(); i++) {
                    for (int j = 0; j < invoiceUnitDetailsEntityList.size(); j++) {
                        if (invoiceMaterialEntityList.get(i).getSphJldw().equals(invoiceUnitDetailsEntityList.get(j).getDetailsName())) {
                            BigDecimal sl = invoiceMaterialEntityList.get(i).getSphSl().divide(invoiceUnitDetailsEntityList.get(j).getDetailsVal());
                            sphSl = sphSl.add(sl == null ? new BigDecimal("0") : sl);
                        }
                    }
                }
            }
        }


        boolean sapFlag = true;
        List<String> dwSapList = new ArrayList<>();
        for (int i = 0; i < invoiceMaterialSapEntityList.size(); i++) {
            String dw = invoiceMaterialSapEntityList.get(i).getMeins();
            if (dw != null && !"".equals(dw)) {
                dwSapList.add(dw);
                if (!invoiceMaterialSapEntityList.get(0).getMeins().equals(dw)) {
                    sapFlag = false;
                    break;
                }
            }
        }
        for (int i = 0; i < invoiceMaterialSapEntityList.size(); i++) {
            sapTotalPrice = sapTotalPrice.add(invoiceMaterialSapEntityList.get(i).getWrbtr());
            sapFreePrice = sapFreePrice.add(invoiceMaterialSapEntityList.get(i).getZzkkbhs());
            wmwst = wmwst.add(invoiceMaterialSapEntityList.get(i).getWmwst());
        }

        if (sapFlag) {
            for (int i = 0; i < invoiceMaterialSapEntityList.size(); i++) {
                if (invoiceMaterialSapEntityList.get(i).getMeins() != null && !"".equals(invoiceMaterialSapEntityList.get(i).getMeins())) {
                    sapDW = invoiceMaterialSapEntityList.get(i).getMeins();
                    break;
                }
            }
            // sapFlag 判断是否所有物料行的 单位都一致，一致直接相加
            for (int i = 0; i < invoiceMaterialSapEntityList.size(); i++) {
                mengeQm = mengeQm.add(invoiceMaterialSapEntityList.get(i).getMengeQm());
            }
        } else {
            //TODO，如不一致，需要查新单位转换关系，统计数量
            //将所有发票行的单位获取到以后去掉重复的单位
            List<String> listWithoutDuplicates = dwSapList.stream().distinct().collect(Collectors.toList());
            InputInvoiceUnitDetailsEntity invoiceUnitDetailsEntity = new InputInvoiceUnitDetailsEntity();
            invoiceUnitDetailsEntity.setDetailsNames(listWithoutDuplicates);
            //使用去重以后的单位去明细表，查出所有记录
            List<InputInvoiceUnitDetailsEntity> invoiceUnitDetailsEntityList = invoiceUnitDetailsService.getByDetailsNames(invoiceUnitDetailsEntity);
            //查出的所有记录的父级单位是否为同一个。如是，可以利用利率转换数量，如不是，不同父级单位的子单位无法转换数量
            boolean isSameParentDW = true;
            for (int i = 0; i < invoiceUnitDetailsEntityList.size(); i++) {
                if (!invoiceUnitDetailsEntityList.get(0).getInvoiceUnit().equals(invoiceUnitDetailsEntityList.get(i).getInvoiceUnit())) {
                    isSameParentDW = false;
                    continue;
                }
            }
            if (invoiceUnitDetailsEntityList.size() > 0) {
                sapDW = invoiceUnitDetailsEntityList.get(0).getUnitName();
            }
            if (isSameParentDW) {
                for (int i = 0; i < invoiceMaterialSapEntityList.size(); i++) {
                    for (int j = 0; j < invoiceUnitDetailsEntityList.size(); j++) {
                        if (invoiceMaterialSapEntityList.get(i).getMeins().equals(invoiceUnitDetailsEntityList.get(j).getDetailsName())) {
                            BigDecimal sl = invoiceMaterialSapEntityList.get(i).getMengeQm().divide(invoiceUnitDetailsEntityList.get(j).getDetailsVal());
                            mengeQm = mengeQm.add(sl == null ? new BigDecimal("0") : sl);
                        }
                    }
                }
            } else {
                for (int i = 0; i < invoiceMaterialSapEntityList.size(); i++) {
                    for (int j = 0; j < invoiceUnitDetailsEntityList.size(); j++) {
                        if (invoiceMaterialSapEntityList.get(i).getMeins().equals(invoiceUnitDetailsEntityList.get(j).getDetailsName())) {
                            BigDecimal sl = invoiceMaterialSapEntityList.get(i).getMengeQm().divide(invoiceUnitDetailsEntityList.get(j).getDetailsVal());
                            mengeQm = mengeQm.add(sl == null ? new BigDecimal("0") : sl);
                        }
                    }
                }
            }
        }

        if (invoiceDW.equals(sapDW)) {
            map.put("sphSl", sphSl);
            map.put("mengeQm", mengeQm);
        } else {
            List<String> dwList1 = new ArrayList<>();
            List<String> dwList2 = new ArrayList<>();
            dwList1.add(invoiceDW);
            dwList2.add(sapDW);
            InputInvoiceUnitDetailsEntity invoiceUnitDetailsEntity = new InputInvoiceUnitDetailsEntity();
            invoiceUnitDetailsEntity.setDetailsNames(dwList1);
            List<InputInvoiceUnitDetailsEntity> invoiceUnitDetailsEntityList1 = invoiceUnitDetailsService.getByDetailsNames(invoiceUnitDetailsEntity);
            invoiceUnitDetailsEntity.setDetailsNames(dwList2);
            List<InputInvoiceUnitDetailsEntity> invoiceUnitDetailsEntityList2 = invoiceUnitDetailsService.getByDetailsNames(invoiceUnitDetailsEntity);
            if (invoiceUnitDetailsEntityList1.size() > 0 && invoiceUnitDetailsEntityList2.size() > 0) {
                if (invoiceUnitDetailsEntityList1.get(0).getInvoiceUnit() == invoiceUnitDetailsEntityList2.get(0).getInvoiceUnit()) {
                    sphSl = sphSl.divide(invoiceUnitDetailsEntityList1.get(0).getDetailsVal());
                    mengeQm = mengeQm.divide(invoiceUnitDetailsEntityList2.get(0).getDetailsVal());
                    map.put("sphSl", sphSl);
                    map.put("mengeQm", mengeQm);
                } else {
                    InputInvoiceUnitPush invoiceUnitPush = new InputInvoiceUnitPush();
                    invoiceUnitPush.setBasicUnit1(invoiceUnitDetailsEntityList1.get(0).getUnitName());
                    invoiceUnitPush.setBasicUnit2(invoiceUnitDetailsEntityList2.get(0).getUnitName());
                    invoiceUnitPush.setIsDelete("0");
                    invoiceUnitPush = invoiceUnitPushService.getByBasicUnits(invoiceUnitPush);
                    if (invoiceUnitPush != null) {
                        sphSl = sphSl.divide(invoiceUnitDetailsEntityList1.get(0).getDetailsVal());
                        mengeQm = mengeQm.divide(invoiceUnitDetailsEntityList2.get(0).getDetailsVal());
                        if (invoiceUnitDetailsEntityList1.get(0).getUnitName().equals(invoiceUnitPush.getBasicUnit1())) {
                            sphSl = sphSl.divide(invoiceUnitPush.getQuantityUnit1());
                            mengeQm = mengeQm.divide(invoiceUnitPush.getQuantityUnit2());
                        } else if (invoiceUnitDetailsEntityList2.get(0).getUnitName().equals(invoiceUnitPush.getBasicUnit1())) {
                            sphSl.divide(invoiceUnitPush.getQuantityUnit2());
                            mengeQm = mengeQm.divide(invoiceUnitPush.getQuantityUnit1());
                        }
                        map.put("sphSl", sphSl);
                        map.put("mengeQm", mengeQm);
                    } else {
                        BigDecimal sphSl2 = new BigDecimal(0);
                        BigDecimal mengeQm2 = new BigDecimal(0);
                        List<String> sapMatnrList = new ArrayList<>();
                        for (int i = 0; i < invoiceMaterialSapEntityList.size(); i++) {
                            if (!sapMatnrList.contains(invoiceMaterialSapEntityList.get(i).getMatnr())) {
                                sapMatnrList.add(invoiceMaterialSapEntityList.get(i).getMatnr());
                            }
                            for (int j = 0; j < invoiceMaterialEntityList.size(); j++) {
                                InputInvoiceUnitDifferent invoiceUnitDifferent = new InputInvoiceUnitDifferent();
                                invoiceUnitDifferent.setSapMblnr(invoiceMaterialSapEntityList.get(i).getMatnr());
                                invoiceUnitDifferent.setUnitClassification1(invoiceMaterialSapEntityList.get(i).getMeins());
                                invoiceUnitDifferent.setUnitClassification2(invoiceMaterialEntityList.get(j).getSphJldw());
                                invoiceUnitDifferent = invoiceUnitDifferentService.getByThree(invoiceUnitDifferent);
                                if (invoiceUnitDifferent != null) {
                                    if (invoiceMaterialSapEntityList.get(i).getMeins().equals(invoiceUnitDifferent.getUnitClassification1())) {
                                        BigDecimal sl = invoiceMaterialSapEntityList.get(i).getMengeQm().divide(invoiceUnitDifferent.getQuantityUnit1());
                                        mengeQm2 = mengeQm2.add(sl == null ? new BigDecimal("0") : sl);
                                        BigDecimal sl2 = invoiceMaterialEntityList.get(j).getSphSl().divide(invoiceUnitDifferent.getQuantityUnit2());
                                        sphSl2 = sphSl2.add(sl == null ? new BigDecimal("0") : sl2);
                                    } else {
                                        BigDecimal sl = invoiceMaterialSapEntityList.get(i).getMengeQm().divide(invoiceUnitDifferent.getQuantityUnit2());
                                        mengeQm2 = mengeQm2.add(sl == null ? new BigDecimal("0") : sl);
                                        BigDecimal sl2 = invoiceMaterialEntityList.get(j).getSphSl().divide(invoiceUnitDifferent.getQuantityUnit1());
                                        sphSl2 = sphSl2.add(sl == null ? new BigDecimal("0") : sl2);
                                    }
                                }
                            }
                            map.put("sphSl", sphSl2);
                            map.put("mengeQm", mengeQm2);
                        }
                    }
                }
            } else {
                BigDecimal sphSl2 = new BigDecimal(0);
                BigDecimal mengeQm2 = new BigDecimal(0);
                List<String> sapMatnrList = new ArrayList<>();
                List<Integer> integers = new ArrayList<>();
                for (int i = 0; i < invoiceMaterialSapEntityList.size(); i++) {
                    if (!sapMatnrList.contains(invoiceMaterialSapEntityList.get(i).getMatnr())) {
                        sapMatnrList.add(invoiceMaterialSapEntityList.get(i).getMatnr());
                    }
                    for (int j = 0; j < invoiceMaterialEntityList.size(); j++) {
                        InputInvoiceUnitDifferent invoiceUnitDifferent = new InputInvoiceUnitDifferent();
                        invoiceUnitDifferent.setSapMblnr(invoiceMaterialSapEntityList.get(i).getMatnr());
                        invoiceUnitDifferent.setUnitClassification1(invoiceMaterialSapEntityList.get(i).getMeins());
                        invoiceUnitDifferent.setUnitClassification2(invoiceMaterialEntityList.get(j).getSphJldw());
                        invoiceUnitDifferent = invoiceUnitDifferentService.getByThree(invoiceUnitDifferent);
                        if (invoiceUnitDifferent != null) {
                            if (invoiceMaterialSapEntityList.get(i).getMeins().equals(invoiceUnitDifferent.getUnitClassification1())) {
                                BigDecimal sl = invoiceMaterialSapEntityList.get(i).getMengeQm().multiply(invoiceUnitDifferent.getQuantityUnit2());
                                if (!integers.contains(i)) {
                                    mengeQm2 = mengeQm2.add(sl == null ? new BigDecimal("0") : sl);
                                    integers.add(i);
                                }
                                BigDecimal sl2 = invoiceMaterialEntityList.get(j).getSphSl().multiply(invoiceUnitDifferent.getQuantityUnit1());
                                sphSl2 = sphSl2.add(sl == null ? new BigDecimal("0") : sl2);
                            } else {
                                BigDecimal sl = invoiceMaterialSapEntityList.get(i).getMengeQm().multiply(invoiceUnitDifferent.getQuantityUnit1());
                                if (!integers.contains(i)) {
                                    mengeQm2 = mengeQm2.add(sl == null ? new BigDecimal("0") : sl);
                                    integers.add(i);
                                }
                                BigDecimal sl2 = invoiceMaterialEntityList.get(j).getSphSl().multiply(invoiceUnitDifferent.getQuantityUnit2());
                                sphSl2 = sphSl2.add(sl == null ? new BigDecimal("0") : sl2);
                            }
                        }
                    }
                    map.put("sphSl", sphSl2);
                    map.put("mengeQm", mengeQm2);
                }
            }
        }


        //TODO
        map.put("sphse", sphse);
        map.put("wmwst", wmwst);
        map.put("mTotalPrice", mTotalPrice);
        map.put("sapTotalPrice", sapTotalPrice);
        map.put("mFreePrice", mFreePrice);
        map.put("sapFreePrice", sapFreePrice);
        map.put("fault", new BigDecimal(invoiceFaultTolerantEntity.getFault()));
        return map;
    }

    private void updateInvoice(InputInvoiceMaterialSapEntity invoiceMaterialSapEntity) {
        invoiceMaterialSapEntity = invoiceMaterialSapService.get(invoiceMaterialSapEntity);
        if ("1111111111".equals(invoiceMaterialSapEntity.getMatchStatus())) {
            invoiceMaterialSapEntity.setMate("1");
            invoiceMaterialSapService.update(invoiceMaterialSapEntity);
            List<Integer> materialIdList = new ArrayList<>();
            String ids[] = invoiceMaterialSapEntity.getMaterialLineId().split(",");
            for (int i = 0; i < ids.length; i++) {
                materialIdList.add(Integer.parseInt(ids[i]));
            }
            InputInvoiceMaterialEntity invoiceMaterialEntity = new InputInvoiceMaterialEntity();
            invoiceMaterialEntity.setIds(materialIdList);
            List<InputInvoiceMaterialEntity> invoiceMaterialEntityList = invoiceMaterialService.getListByIds(invoiceMaterialEntity);
            for (int i = 0; i < invoiceMaterialEntityList.size(); i++) {
                invoiceMaterialEntityList.get(i).setStatus("1");
                invoiceMaterialService.update(invoiceMaterialEntityList.get(i));
                invoiceMaterialEntityList.get(i).setStatus("");
                List<InputInvoiceMaterialEntity> invoiceMaterialEntityList2 = invoiceMaterialService.getByInvoiceId(invoiceMaterialEntityList.get(i));
                InputInvoiceEntity invoiceEntity = new InputInvoiceEntity();
                invoiceEntity.setId(invoiceMaterialEntity.getInvoiceId());
                int count = 0;
                for (int j = 0; j < invoiceMaterialEntityList2.size(); j++) {
                    if ("1".equals(invoiceMaterialEntityList2.get(j).getStatus())) {
                        count += 1;
                    }
                }
                if (count == invoiceMaterialEntityList2.size()) {
                    invoiceEntity.setInvoiceStatus("8");
                    invoiceEntity.setInvoiceErrorDescription("三单匹配成功");
                } else if (count < invoiceMaterialEntityList2.size()) {
                    invoiceEntity.setInvoiceStatus("14");
                    invoiceEntity.setInvoiceErrorDescription("部分匹配");
                } else if (count == 0) {
                    invoiceEntity.setInvoiceStatus("9");
                    invoiceEntity.setInvoiceErrorDescription("三单匹配失败");
                }
                this.update(invoiceEntity);
            }

        }
    }

    /**
     * 判断一个大组中的发票是否都已匹配
     *
     * @param invoiceMaterialEntityList
     * @param invoiceMaterialSapEntityList
     * @return
     */
    private Boolean checkMate(List<InputInvoiceMaterialEntity> invoiceMaterialEntityList, List<InputInvoiceMaterialSapEntity> invoiceMaterialSapEntityList) {
        int materialCount = 0;
        for (int i = 0; i < invoiceMaterialEntityList.size(); i++) {
            if (invoiceMaterialEntityList.get(i).getStatus().equals("1")) {
                materialCount++;
            }
        }
        int sapCount = 0;
        for (int i = 0; i < invoiceMaterialSapEntityList.size(); i++) {
            if (invoiceMaterialSapEntityList.get(i).getMate().equals("1")) {
                sapCount++;
            }
        }
        if (materialCount == invoiceMaterialEntityList.size() && sapCount == invoiceMaterialSapEntityList.size()) {
            return true;
        } else {
            return false;
        }
    }

    // B 购方校验 （111）
    private String matchPurchaser(List<InputInvoiceMaterialEntity> invoiceMaterialEntityList, List<InputInvoiceMaterialSapEntity> invoiceMaterialSap) {
        String result = "1";
        // 将sap分组
        List<List<InputInvoiceMaterialSapEntity>> allList = byGroup(invoiceMaterialSap);
        for (int i = 0; i < allList.size(); i++) {
            List<InputInvoiceMaterialSapEntity> sapEntityList = allList.get(i);
            String materialArr[] = sapEntityList.get(0).getMaterialLineId().split(",");
            List<Integer> listList = new ArrayList<>();
            for (int j = 0; j < materialArr.length; j++) {
                listList.add(Integer.parseInt(materialArr[j]));
            }
            InputInvoiceMaterialEntity invoiceMaterialEntity = new InputInvoiceMaterialEntity();
            invoiceMaterialEntity.setIds(listList);
            List<InputInvoiceMaterialEntity> materialEntityList = invoiceMaterialService.getListByIds(invoiceMaterialEntity);
            String flag = checkPurchaser(materialEntityList, sapEntityList);
            if (flag.substring(0, 1).equals("1")) {
                updateMatchPurchaser3(materialEntityList, sapEntityList, "1", "");
            } else {
                updateMatchPurchaser3(materialEntityList, sapEntityList, "0", "购方企业名称校验不通过");
            }
            if (flag.substring(1).equals("1")) {
                updateMatchPurchaser3(materialEntityList, sapEntityList, "1", "");
            } else {
                updateMatchPurchaser3(materialEntityList, sapEntityList, "0", "购方企业税号校验不通过");
            }
        }
//        updateMatchPurchaser2(invoiceMaterialEntityList, invoiceMaterialSapEntityList, 4);
        return result;
    }

    private String checkPurchaser(List<InputInvoiceMaterialEntity> invoiceMaterialEntityList, List<InputInvoiceMaterialSapEntity> invoiceMaterialSapEntityList) {
        String result = "";
        List<Integer> mIds = new ArrayList<>();
        for (int i = 0; i < invoiceMaterialEntityList.size(); i++) {
            mIds.add(invoiceMaterialEntityList.get(i).getInvoiceId());
        }
        List<String> companyNumbers = new ArrayList<>();
        for (int i = 0; i < invoiceMaterialSapEntityList.size(); i++) {
            companyNumbers.add(invoiceMaterialSapEntityList.get(i).getBukrs());
        }
        List<InputInvoiceEntity> invoiceEntityList = this.getListByIds(mIds);
//        List<CompanyEntity> companyEntityList = companyService.getListByCompanyNumber(companyNumbers);
        List<SysDeptEntity> deptEntityList = (List<SysDeptEntity>) sysDeptService.listByIds(companyNumbers);
        int count = 1;
        int count2 = 1;
        for (int i = 0; i < invoiceEntityList.size(); i++) {
            //购方名称
            String invoicePurchaserCompany = invoiceEntityList.get(i).getInvoicePurchaserCompany().replace("（", "(").replace("）", ")").replace(" ", "");
            //购方税号
            String invoicePurchaserParagraph = invoiceEntityList.get(i).getInvoicePurchaserParagraph();
            for (int j = 0; j < deptEntityList.size(); j++) {
                String companyName = deptEntityList.get(j).getName().replace("（", "(").replace("）", ")").replace(" ", "");
                if (invoicePurchaserCompany.equals(companyName)) {
                    count += 1;
                }
                if (invoicePurchaserParagraph.equals(deptEntityList.get(j).getTaxCode())) {
                    count2 += 1;
                }
            }
        }
        //购方名称相同
        if (count == invoiceEntityList.size() + deptEntityList.size()) {
            result += "1";
        } else {
            result += invoiceCheckActionService.checkAction("GFMC");
        }
        //购方税号相同
        if (count2 == invoiceEntityList.size() + deptEntityList.size()) {
            result += "1";
        } else {
            result += invoiceCheckActionService.checkAction("GFSH");
        }
        return result;
    }

    private void saveGoodsMatnrMappingId(InputSapInvoiceMappingIdEntity sapInvoiceMappingIdEntity, InputInvoiceMaterialSapEntity invoiceMaterialSapEntity, Integer id) {
        if (sapInvoiceMappingIdEntity == null) {
            sapInvoiceMappingIdEntity = new InputSapInvoiceMappingIdEntity();
            sapInvoiceMappingIdEntity.setSapId(invoiceMaterialSapEntity.getId());
            sapInvoiceMappingIdEntity.setGoodsMatnrId(id);
            sapInvoiceMappingIdService.save(sapInvoiceMappingIdEntity);
        } else {
            sapInvoiceMappingIdEntity.setGoodsMatnrId(id);
            sapInvoiceMappingIdService.update(sapInvoiceMappingIdEntity);
        }
    }


    //传入sapEntity对象，做update操作
    private void updateSapEntity(InputInvoiceMaterialEntity invoiceMaterialEntity, InputInvoiceMaterialSapEntity invoiceMaterialSapEntity, Integer id) {
        if (!"11".equals(invoiceMaterialEntity.getMatchStatus())) {
            invoiceMaterialEntity.setMatchStatus(invoiceMaterialEntity.getMatchStatus() + "1");
        }
        invoiceMaterialService.update(invoiceMaterialEntity);
        if (invoiceMaterialSapEntity.getMaterialLineId() != null && !"".equals(invoiceMaterialSapEntity.getMaterialLineId())) {
            invoiceMaterialSapEntity.setMaterialLineId(invoiceMaterialSapEntity.getMaterialLineId() + "," + id);
        } else {
            invoiceMaterialSapEntity.setMaterialLineId(String.valueOf(id));
        }
        invoiceMaterialSapEntity.setIsLineId("1");
        if (!"11".equals(invoiceMaterialSapEntity.getMatchStatus())) {
            invoiceMaterialSapEntity.setMatchStatus(invoiceMaterialSapEntity.getMatchStatus() + "1");
        }
        invoiceMaterialSapService.update(invoiceMaterialSapEntity);
    }

    // E 明细分组 (11)
    private String groupByDetails(List<InputInvoiceMaterialEntity> invoiceMaterialEntityList,
                                  List<InputInvoiceMaterialSapEntity> invoiceMaterialSapEntityList) {
        String result = "1";
        int count = 0;
        for (int i = 0; i < invoiceMaterialEntityList.size(); i++) {
            if ("1".equals(invoiceMaterialEntityList.get(i).getMatchStatus())) {
                String spmcStr = "";
                //如果发票明细名称不为空,获取发票名称
                if (!"".equals(invoiceMaterialEntityList.get(i).getSphSpmc()) && invoiceMaterialEntityList.get(i).getSphSpmc() != null) {
                    String sphSpmc = invoiceMaterialEntityList.get(i).getSphSpmc().replace(" ", "");
                    String spmc[] = sphSpmc.split("\\*");
                    //商品名
                    spmcStr = spmc[2];
                    System.out.println(spmcStr);
                    String ggxh = invoiceMaterialEntityList.get(i).getSphGgxh();
                    if (StringUtils.isNotBlank(invoiceMaterialEntityList.get(i).getSphGgxh())) {
                        ggxh = ggxh.replace(" ", "");
                    }
                    for (int j = 0; j < invoiceMaterialSapEntityList.size(); j++) {
                        //已经关联过的物料明细不再次计算
                        if ("1".equals(invoiceMaterialSapEntityList.get(j).getMatchStatus()) || "11".equals(invoiceMaterialSapEntityList.get(j).getMatchStatus())) {
                            InputSapInvoiceMappingIdEntity sapInvoiceMappingIdEntity = new InputSapInvoiceMappingIdEntity();
                            sapInvoiceMappingIdEntity.setSapId(invoiceMaterialSapEntityList.get(j).getId());
                            sapInvoiceMappingIdEntity = sapInvoiceMappingIdService.getOneBySapId(sapInvoiceMappingIdEntity);
                            // sap物料描述
                            String maktx = invoiceMaterialSapEntityList.get(j).getMaktx().replace(" ", "");
                            if ((spmcStr + ggxh).equals(maktx)) {
                                saveGoodsMatnrMappingId(sapInvoiceMappingIdEntity, invoiceMaterialSapEntityList.get(j), 0);
                                updateSapEntity(invoiceMaterialEntityList.get(i), invoiceMaterialSapEntityList.get(j), invoiceMaterialEntityList.get(i).getId());
                                count += 1;
                            } else {
                                //规格型号不为空
                                if (!"".equals(ggxh) && ggxh != null) {
                                    if (maktx.contains(spmcStr) && maktx.contains(ggxh)) {
                                        saveGoodsMatnrMappingId(sapInvoiceMappingIdEntity, invoiceMaterialSapEntityList.get(j), 0);
                                        updateSapEntity(invoiceMaterialEntityList.get(i), invoiceMaterialSapEntityList.get(j), invoiceMaterialEntityList.get(i).getId());
                                        count += 1;
                                    } else {
                                        InputInvoiceGoodsMatnr invoiceGoodsMatnr = new InputInvoiceGoodsMatnr();
                                        invoiceGoodsMatnr.setMaktx(invoiceMaterialSapEntityList.get(j).getMaktx());
                                        invoiceGoodsMatnr.setGoodsName(spmc[2]);
                                        invoiceGoodsMatnr.setGoodsType(invoiceMaterialEntityList.get(i).getSphGgxh());
                                        invoiceGoodsMatnr.setUsed("1");
                                        invoiceGoodsMatnr = invoiceGoodsMatnrService.getOneByCondition(invoiceGoodsMatnr);
                                        if (invoiceGoodsMatnr != null) {
                                            saveGoodsMatnrMappingId(sapInvoiceMappingIdEntity, invoiceMaterialSapEntityList.get(j), invoiceGoodsMatnr.getId());
                                            updateSapEntity(invoiceMaterialEntityList.get(i), invoiceMaterialSapEntityList.get(j), invoiceMaterialEntityList.get(i).getId());
                                            count += 1;
                                        }
                                    }
                                } else {
                                    //规格型号为空,仅判断商品名是否存在于物料描述
                                    if (maktx.contains(spmcStr)) {
                                        saveGoodsMatnrMappingId(sapInvoiceMappingIdEntity, invoiceMaterialSapEntityList.get(j), 0);
                                        updateSapEntity(invoiceMaterialEntityList.get(i), invoiceMaterialSapEntityList.get(j), invoiceMaterialEntityList.get(i).getId());
                                        count += 1;
                                    } else {
                                        InputInvoiceGoodsMatnr invoiceGoodsMatnr = new InputInvoiceGoodsMatnr();
                                        invoiceGoodsMatnr.setMaktx(invoiceMaterialSapEntityList.get(j).getMaktx());
                                        invoiceGoodsMatnr.setGoodsName(spmc[2]);
                                        invoiceGoodsMatnr.setGoodsType("");
                                        invoiceGoodsMatnr.setUsed("1");
                                        invoiceGoodsMatnr = invoiceGoodsMatnrService.getOneByCondition(invoiceGoodsMatnr);
                                        if (invoiceGoodsMatnr != null) {
                                            saveGoodsMatnrMappingId(sapInvoiceMappingIdEntity, invoiceMaterialSapEntityList.get(j), invoiceGoodsMatnr.getId());
                                            updateSapEntity(invoiceMaterialEntityList.get(i), invoiceMaterialSapEntityList.get(j), invoiceMaterialEntityList.get(i).getId());
                                            count += 1;
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    //商品名为空，return
                    result = "2";
                    invoiceMaterialEntityList.get(i).setMatchStatus("10");
                    invoiceMaterialService.update(invoiceMaterialEntityList.get(i));
                }
            }
            if (i == invoiceMaterialEntityList.size() - 1) {
                for (int p = 0; p < invoiceMaterialEntityList.size(); p++) {
                    if (!"11".equals(invoiceMaterialEntityList.get(p).getMatchStatus())) {
                        updateSapTaxCode2(invoiceMaterialEntityList.get(p), "0", "SAP物料说明与开票商品名称、规格型号不一致");
                    }
                }
                for (int p = 0; p < invoiceMaterialSapEntityList.size(); p++) {
                    if (!"11".equals(invoiceMaterialSapEntityList.get(p).getMatchStatus())) {
                        updateSapTaxCode3(invoiceMaterialSapEntityList.get(p), "0", "SAP物料说明与开票商品名称、规格型号不一致");
                    }
                }
            }

        }
        if (count == 0) {
            result = "2";
            return result;
        }
        updateMatchPurchaser2(invoiceMaterialEntityList, invoiceMaterialSapEntityList, 1);
        // 分组成功，去重lineId
        if (result.equals("1")) {
            List<InputInvoiceMaterialSapEntity> list = invoiceMaterialSapService.getListBySapId(invoiceMaterialSapEntityList);
            for (int i = 0; i < list.size(); i++) {
                if (StringUtils.isNotBlank(list.get(i).getMaterialLineId())) {
                    String[] a = list.get(i).getMaterialLineId().split(",");
                    Arrays.sort(a);
                    List<String> stringList = new ArrayList<>();
                    String newMaterialLineId = "";
                    for (int j = 0; j < a.length; j++) {
                        if (!stringList.contains(a[j]) && !a[j].equals(" ") && !a[j].equals("")) {
                            stringList.add(a[j]);
                            newMaterialLineId = newMaterialLineId + a[j] + ",";
                        }
                    }
                    list.get(i).setMaterialLineId(newMaterialLineId.substring(0, newMaterialLineId.length() - 1));
                }
            }
            invoiceMaterialSapService.updateLineId(list);
        }
        return result;
    }


    // 当明细分组失败时候，line_id 为空，所有的sap购方已经校验以后，剩下的发票状态为2位的统一加0
    private void updateMatchPurchaser2(List<InputInvoiceMaterialEntity> invoiceMaterialEntityList, List<InputInvoiceMaterialSapEntity> invoiceMaterialSapEntityList, int count) {
        InputInvoiceMaterialEntity invoiceMaterialEntity = new InputInvoiceMaterialEntity();
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < invoiceMaterialEntityList.size(); i++) {
            ids.add(invoiceMaterialEntityList.get(i).getId());
        }
        invoiceMaterialEntity.setIds(ids);
        invoiceMaterialEntityList = invoiceMaterialService.getListByIds(invoiceMaterialEntity);
        for (int i = 0; i < invoiceMaterialEntityList.size(); i++) {
            if (invoiceMaterialEntityList.get(i).getMatchStatus().length() == count) {
                invoiceMaterialEntityList.get(i).setMatchStatus(invoiceMaterialEntityList.get(i).getMatchStatus() + "0");
                invoiceMaterialService.update(invoiceMaterialEntityList.get(i));
            }
        }
        for (int i = 0; i < invoiceMaterialSapEntityList.size(); i++) {
            if (invoiceMaterialSapEntityList.get(i).getMatchStatus().length() == count) {
                invoiceMaterialSapEntityList.get(i).setMatchStatus(invoiceMaterialSapEntityList.get(i).getMatchStatus() + "0");
                invoiceMaterialSapService.update(invoiceMaterialSapEntityList.get(i));
            }
        }
    }

    public void updateMatchPurchaser3(List<InputInvoiceMaterialEntity> invoiceMaterialEntityList, List<InputInvoiceMaterialSapEntity> invoiceMaterialSapEntityList,
                                      String matchStatus,
                                      String description) {
        for (int i = 0; i < invoiceMaterialEntityList.size(); i++) {
            invoiceMaterialEntityList.get(i).setMatchStatus(invoiceMaterialEntityList.get(i).getMatchStatus() + matchStatus);
        }

        for (int i = 0; i < invoiceMaterialSapEntityList.size(); i++) {
            invoiceMaterialSapEntityList.get(i).setMatchStatus(invoiceMaterialSapEntityList.get(i).getMatchStatus() + matchStatus);
            if (StringUtils.isNotBlank(description)) {
                if (description.equals("税率不等")) {
                    invoiceMaterialSapEntityList.get(i).setDescription("物料（" + invoiceMaterialSapEntityList.get(i).getMaktx() + "）SAP物料凭证税率与发票税率不一致");
                } else if (description.equals("RCODE")) {
                    String rcode = invoiceMaterialSapEntityList.get(i).getRcode();
                    if (rcode.equals("01")) {
                        invoiceMaterialSapEntityList.get(i).setDescription("成功，可进行匹配");
                    } else if (rcode.equals("02")) {
                        invoiceMaterialSapEntityList.get(i).setDescription("合同编号未维护");
                    } else if (rcode.equals("03")) {
                        invoiceMaterialSapEntityList.get(i).setDescription("付款条件为空或不一致");
                    } else if (rcode.equals("04")) {
                        invoiceMaterialSapEntityList.get(i).setDescription("质检未通过");
                    } else if (rcode.equals("05")) {
                        invoiceMaterialSapEntityList.get(i).setDescription("物料凭证已被冲销");
                    } else if (rcode.equals("06")) {
                        invoiceMaterialSapEntityList.get(i).setDescription("物料凭证移动类型有误");
                    } else if (rcode.equals("07")) {
                        invoiceMaterialSapEntityList.get(i).setDescription("物料凭证不存在");
                    }
                } else {
                    invoiceMaterialSapEntityList.get(i).setDescription(description);
                }
            }
        }
        invoiceMaterialService.updateByEnter(invoiceMaterialEntityList);
        invoiceMaterialSapService.updateByEnter(invoiceMaterialSapEntityList);
    }

    public List<List<InputInvoiceMaterialSapEntity>> byGroup(List<InputInvoiceMaterialSapEntity> invoiceMaterialSapEntityList) {
        List<List<InputInvoiceMaterialSapEntity>> allList = new ArrayList<>();
        List<String> mids = new ArrayList<>();
        for (int i = 0; i < invoiceMaterialSapEntityList.size(); i++) {
            if (!mids.contains(invoiceMaterialSapEntityList.get(i).getMaterialLineId()) && StringUtils.isNotBlank(invoiceMaterialSapEntityList.get(i).getMaterialLineId())) {
                InputInvoiceMaterialSapEntity invoiceMaterialSapEntity = new InputInvoiceMaterialSapEntity();
                invoiceMaterialSapEntity.setMaterialLineId(invoiceMaterialSapEntityList.get(i).getMaterialLineId());
                List<InputInvoiceMaterialSapEntity> materialSapEntityList = invoiceMaterialSapService.getListByLineIdAndMate(invoiceMaterialSapEntity);
                allList.add(materialSapEntityList);
                mids.add(invoiceMaterialSapEntityList.get(i).getMaterialLineId());
            }
        }
        return allList;
    }

    //A 判断sap返回物料状态码，只匹配01，其他状态都不匹配
    private String matchRcode(List<InputInvoiceMaterialEntity> invoiceMaterialEntityList, List<InputInvoiceMaterialSapEntity> invoiceMaterialSap) {
        String result = "1";
        // 将sap分组
        List<List<InputInvoiceMaterialSapEntity>> allList = byGroup(invoiceMaterialSap);
        for (int i = 0; i < allList.size(); i++) {
            List<InputInvoiceMaterialSapEntity> sapEntityList = allList.get(i);
            String materialArr[] = sapEntityList.get(0).getMaterialLineId().split(",");
            List<Integer> listList = new ArrayList<>();
            for (int j = 0; j < materialArr.length; j++) {
                listList.add(Integer.parseInt(materialArr[j]));
            }
            InputInvoiceMaterialEntity invoiceMaterialEntity = new InputInvoiceMaterialEntity();
            invoiceMaterialEntity.setIds(listList);
            List<InputInvoiceMaterialEntity> materialEntityList = invoiceMaterialService.getListByIds(invoiceMaterialEntity);
            int count = 0;
            for (int j = 0; j < sapEntityList.size(); j++) {
                if ("01".equals(sapEntityList.get(j).getRcode()) || "02".equals(sapEntityList.get(j).getRcode())) {
                    count++;
                }
            }
            if (count == sapEntityList.size()) {
                updateMatchPurchaser3(materialEntityList, sapEntityList, "1", "RCODE");
            } else {
                updateMatchPurchaser3(materialEntityList, sapEntityList, "0", "RCODE");
            }
        }
        return result;
    }

    //TODO 分割线03/18
    // D 税收分类和sap物料组是否匹配 (1)
    private String matchTaxGroup(List<InputInvoiceMaterialEntity> invoiceMaterialEntityList,
                                 List<InputInvoiceMaterialSapEntity> invoiceMaterialSapEntityList) {
        String result = "1";
        int count = 0;
        for (int i = 0; i < invoiceMaterialEntityList.size(); i++) {
            String sphSpmc = invoiceMaterialEntityList.get(i).getSphSpmc();
            String sphSpmc1 = sphSpmc.substring(1);
            String skjm[] = sphSpmc1.split("\\*");
//            String skjm[] = {""};
            System.out.println(skjm[0]);
            int taxCount = 0;
            for (int j = 0; j < invoiceMaterialSapEntityList.size(); j++) {
//                if (!"1".equals(invoiceMaterialSapEntityList.get(j).getMatchStatus())) {
                //如果当前sap对象materialIds中包含发票明细id再去比较
//                if (invoiceMaterialSapEntityList.get(j).getMaterialIds().contains(String.valueOf(invoiceMaterialEntityList.get(i).getId()))) {
                InputSapInvoiceMappingIdEntity sapInvoiceMappingIdEntity = new InputSapInvoiceMappingIdEntity();
                sapInvoiceMappingIdEntity.setSapId(invoiceMaterialSapEntityList.get(j).getId());
                sapInvoiceMappingIdEntity = sapInvoiceMappingIdService.getOneBySapId(sapInvoiceMappingIdEntity);
                //税收分类等于SAP物料组明细
                if (skjm[0].equals(invoiceMaterialSapEntityList.get(j).getWgbez())) {
                    if (sapInvoiceMappingIdEntity == null) {
                        InputSapInvoiceMappingIdEntity sapMappingEntity = new InputSapInvoiceMappingIdEntity();
                        sapMappingEntity.setSapId(invoiceMaterialSapEntityList.get(j).getId());
                        sapMappingEntity.setResponsibleId(0);
                        sapInvoiceMappingIdService.save(sapMappingEntity);
                    } else {
                        sapInvoiceMappingIdEntity.setResponsibleId(0);
                        sapInvoiceMappingIdService.update(sapInvoiceMappingIdEntity);
                    }
                    count += 1;
                    updateSapTaxCode(invoiceMaterialEntityList.get(i), invoiceMaterialSapEntityList.get(j), "1", "");
                } else {
                    InputInvoiceResponsibleEntity invoiceResponsibleEntity = new InputInvoiceResponsibleEntity();
                    invoiceResponsibleEntity.setGoodsCategory(skjm[0]);
                    //税收分类不等于SAP物料组明细,查映射表，是否存在映射关系
                    invoiceResponsibleEntity.setInvoiceResponsible(invoiceMaterialSapEntityList.get(j).getWgbez());
                    invoiceResponsibleEntity = invoiceResponsibleService.getOneByCondition(invoiceResponsibleEntity);
                    //税控存在映射关系
                    if (invoiceResponsibleEntity != null) {
                        saveResponsibleMappingId(sapInvoiceMappingIdEntity, invoiceMaterialSapEntityList.get(j), invoiceResponsibleEntity);
                        updateSapTaxCode(invoiceMaterialEntityList.get(i), invoiceMaterialSapEntityList.get(j), "1", "");
                        count += 1;
                    }
                }
            }
            if (i == invoiceMaterialEntityList.size() - 1) {
                for (int p = 0; p < invoiceMaterialEntityList.size(); p++) {
                    if (!"1".equals(invoiceMaterialEntityList.get(p).getMatchStatus())) {
                        updateSapTaxCode2(invoiceMaterialEntityList.get(p), "0", "发票税控简码与SAP物料组不一致");
                    }
                }
                for (int p = 0; p < invoiceMaterialSapEntityList.size(); p++) {
                    if (!"1".equals(invoiceMaterialSapEntityList.get(p).getMatchStatus())) {
                        updateSapTaxCode3(invoiceMaterialSapEntityList.get(p), "0", "发票税控简码与SAP物料组不一致");
                    }
                }
            }
        }
        updateMatchPurchaser2(invoiceMaterialEntityList, invoiceMaterialSapEntityList, 0);
        if (count == 0) {
            result = "2";
        }
        return result;
    }


    private void updateSapTaxCode2(InputInvoiceMaterialEntity invoiceMaterialEntity, String status, String description) {
        if (invoiceMaterialEntity.getMatchStatus() != null && !"".equals(invoiceMaterialEntity.getMatchStatus())) {
            invoiceMaterialEntity.setMatchStatus(invoiceMaterialEntity.getMatchStatus() + status);
        } else {
            invoiceMaterialEntity.setMatchStatus(status);
        }
        invoiceMaterialService.update(invoiceMaterialEntity);
        List<InputInvoiceMaterialEntity> invoiceMaterialEntityList = invoiceMaterialService.getByInvoiceId(invoiceMaterialEntity);
        InputInvoiceEntity invoiceEntity = new InputInvoiceEntity();
        invoiceEntity.setId(invoiceMaterialEntity.getInvoiceId());
        int count = 0;
        for (int i = 0; i < invoiceMaterialEntityList.size(); i++) {
            if ("1".equals(invoiceMaterialEntityList.get(i).getStatus())) {
                count += 1;
            }
        }
        invoiceEntity.setInvoiceErrorDescription(description);
        if (count == invoiceMaterialEntityList.size()) {
            invoiceEntity.setInvoiceStatus("8");
        } else {
            invoiceEntity.setInvoiceStatus("9");
        }
        this.update(invoiceEntity);
    }

    private void updateSapTaxCode3(InputInvoiceMaterialSapEntity invoiceMaterialSapEntity, String status, String description) {
        if (invoiceMaterialSapEntity.getMatchStatus() != null && !"".equals(invoiceMaterialSapEntity.getMatchStatus())) {
            invoiceMaterialSapEntity.setMatchStatus(invoiceMaterialSapEntity.getMatchStatus() + status);
        } else {
            invoiceMaterialSapEntity.setMatchStatus(status);
        }
        invoiceMaterialSapEntity.setDescription(description);
        invoiceMaterialSapService.update(invoiceMaterialSapEntity);
    }

    private void saveResponsibleMappingId(InputSapInvoiceMappingIdEntity sapInvoiceMappingIdEntity,
                                          InputInvoiceMaterialSapEntity invoiceMaterialSapEntity,
                                          InputInvoiceResponsibleEntity invoiceResponsibleEntity) {
        if (sapInvoiceMappingIdEntity == null) {
            sapInvoiceMappingIdEntity = new InputSapInvoiceMappingIdEntity();
            sapInvoiceMappingIdEntity.setSapId(invoiceMaterialSapEntity.getId());
            sapInvoiceMappingIdEntity.setResponsibleId(invoiceResponsibleEntity.getId());
            sapInvoiceMappingIdService.save(sapInvoiceMappingIdEntity);
        } else {
            sapInvoiceMappingIdEntity.setResponsibleId(invoiceResponsibleEntity.getId());
            sapInvoiceMappingIdService.update(sapInvoiceMappingIdEntity);
        }
    }

    //税控简码 判断通过/不通过以后调用修改状态
    private void updateSapTaxCode(InputInvoiceMaterialEntity invoiceMaterialEntity, InputInvoiceMaterialSapEntity invoiceMaterialSapEntity, String status, String description) {
        invoiceMaterialEntity.setMatchStatus(status);
        invoiceMaterialService.update(invoiceMaterialEntity);
        invoiceMaterialSapEntity.setMatchStatus(status);
        invoiceMaterialSapEntity.setDescription(description);
        invoiceMaterialSapService.update(invoiceMaterialSapEntity);
    }

    /**
     * 按groupId分组入账
     */
    private List<List<InputInvoiceEntity>> groupByGroupId(List<InputInvoiceEntity> invoiceEntityList, InputInvoiceBatchEntity invoiceBatchEntity) {
        List<Integer> strings = new ArrayList<>();
        for (int i = 0; i < invoiceEntityList.size(); i++) {
            if (i == 0) {
                strings.add(invoiceEntityList.get(i).getInvoiceGroup());
            } else {
                if (!strings.contains(invoiceEntityList.get(i).getInvoiceGroup())) {
                    strings.add(invoiceEntityList.get(i).getInvoiceGroup());
                }
            }
        }

        List<List<InputInvoiceEntity>> listList = new ArrayList<>();
        for (Integer str : strings) {
            List<InputInvoiceEntity> invoiceEntities1 = new ArrayList<>();
            for (InputInvoiceEntity invoice : invoiceEntityList) {
                if (str.equals(invoice.getInvoiceGroup())) {
                    invoiceEntities1.add(invoice);

                }
            }
            listList.add(invoiceEntities1);
        }
        System.out.println("分组完成");
        for (int i = 0; i < listList.size(); i++) {
            // 根据发票Id获取发票物料信息
            List<String> invoiceIds = new ArrayList<>();
            for (int j = 0; j < listList.get(i).size(); j++) {
                invoiceIds.add(String.valueOf(listList.get(i).get(j).getId()));

            }
            InputInvoiceMaterialEntity invoiceMaterialEntity = new InputInvoiceMaterialEntity();
            invoiceMaterialEntity.setInvoiceIds(invoiceIds);
            // 已匹配发票物料
            List<InputInvoiceMaterialEntity> invoiceMaterialEntityList = invoiceMaterialService.getListByInvoiceId(invoiceMaterialEntity);
            for (InputInvoiceEntity invoice : invoiceEntityList) {
                TagInputTransOut(invoiceMaterialEntityList, invoice);

            }
            // 获取发票物料信息id
            List<String> mIds = new ArrayList<>();

            for (int j = 0; j < invoiceMaterialEntityList.size(); j++) {
                mIds.add(String.valueOf(invoiceMaterialEntityList.get(j).getId()));
            }
            InputInvoiceMaterialSapEntity invoiceMaterialSapEntity = new InputInvoiceMaterialSapEntity();
            invoiceMaterialSapEntity.setBatchId(invoiceEntityList.get(0).getInvoiceBatchNumber());
            List<InputInvoiceMaterialSapEntity> invoiceMaterialSapEntityList = invoiceMaterialSapService.getListByBatchId(invoiceMaterialSapEntity);

            // 已匹配sap物料
            List<InputInvoiceMaterialSapEntity> materialSapEntities = new ArrayList<>();
            for (int j = 0; j < invoiceMaterialSapEntityList.size(); j++) {
                if (StringUtils.isNotBlank(invoiceMaterialSapEntityList.get(j).getMaterialIds())) {
                    String[] materialIds = invoiceMaterialSapEntityList.get(j).getMaterialIds().split(",");
                    for (int k = 0; k < materialIds.length; k++) {
                        if (mIds.indexOf(materialIds[k]) > -1) {
                            // 已匹配sap明细
                            materialSapEntities.add(invoiceMaterialSapEntityList.get(j));
                            break;
                        }
                    }
                }
            }
            String result = entry(invoiceMaterialEntityList, materialSapEntities, listList.get(i), invoiceBatchEntity);

//            String result = entry(invoiceMaterialEntityList, materialSapEntities, listList.get(i), invoiceBatchEntity);
//            if(result.equals("1")) {
//                InputInvoiceEntity invoiceEntity = new InputInvoiceEntity();
//                invoiceBatchEntity = new InputInvoiceBatchEntity();
//                invoiceBatchEntity.setId(Integer.valueOf(listList.get(i).get(0).getInvoiceBatchNumber()));
//                invoiceEntity.setInvoiceBatchNumber(invoiceEntityList.get(0).getInvoiceBatchNumber());
//                invoiceEntity.setInvoiceGroup(listList.get(i).get(0).getInvoiceGroup());
//                invoiceEntity.setInvoiceErrorDescription("付款条件小于30天，需人工复核处理");
//
//                invoiceService.updateByEnter(invoiceEntity);
//
//                invoiceBatchEntity.setThreeErrorDescription("付款条件小于30天，需人工复核处理");
//                invoiceBatchService.update(invoiceBatchEntity);

            System.out.println("tag进项转出");

//            break;
//            }
        }
        return listList;
    }

    /**
     * 自动入账
     *
     * @param invoiceMaterialEntityList
     * @param invoiceMaterialSapEntityList
     * @param invoiceEntityList
     */
    private String entry(List<InputInvoiceMaterialEntity> invoiceMaterialEntityList, List<InputInvoiceMaterialSapEntity> invoiceMaterialSapEntityList, List<InputInvoiceEntity> invoiceEntityList, InputInvoiceBatchEntity invoiceBatchEntity) {
        return createProvider(invoiceMaterialEntityList, invoiceMaterialSapEntityList, invoiceEntityList, invoiceBatchEntity);
    }


    private List<InputInvoiceMaterialSapEntity> setAmount(List<InputInvoiceMaterialEntity> invoiceMaterialEntityList, List<InputInvoiceMaterialSapEntity> invoiceMaterialSapEntityList) {
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < invoiceMaterialSapEntityList.size(); i++) {
            if (i == 0) {
                strings.add(invoiceMaterialSapEntityList.get(i).getMaterialLineId());
            } else {
                if (!strings.contains(invoiceMaterialSapEntityList.get(i).getMaterialLineId())) {
                    strings.add(invoiceMaterialSapEntityList.get(i).getMaterialLineId());
                }
            }
        }

        List<List<InputInvoiceMaterialSapEntity>> listList = new ArrayList<>();
        for (String str : strings) {
            List<InputInvoiceMaterialSapEntity> invoiceEntities1 = new ArrayList<>();
            for (InputInvoiceMaterialSapEntity invoice : invoiceMaterialSapEntityList) {
                if (str.equals(invoice.getMaterialLineId())) {
                    invoiceEntities1.add(invoice);
                }
            }
            listList.add(invoiceEntities1);
        }
        System.out.println("分组完成");

        for (int i = 0; i < listList.size(); i++) {
            //凭证货币金额
            String[] material = listList.get(i).get(0).getMaterialLineId().split(",");
            List<Integer> integerList = new ArrayList<>();
            for (int j = 0; j < material.length; j++) {
                integerList.add(Integer.valueOf(material[j]));
            }
            InputInvoiceMaterialEntity invoiceMaterialEntity = new InputInvoiceMaterialEntity();
            invoiceMaterialEntity.setIds(integerList);
            List<InputInvoiceMaterialEntity> invoiceMaterialEntities1 = invoiceMaterialService.getListByIds(invoiceMaterialEntity);

            // 发票物料不含税金额合计
            BigDecimal materialFreePrice = new BigDecimal("0");
            // SAP物料发票不含税金额合计
            BigDecimal sapFreePrice = new BigDecimal("0");
            for (int j = 0; j < invoiceMaterialEntities1.size(); j++) {
                materialFreePrice = materialFreePrice.add(invoiceMaterialEntities1.get(j).getSphJe());
            }

            for (int j = 0; j < listList.get(i).size(); j++) {
                sapFreePrice = sapFreePrice.add(listList.get(i).get(j).getZzkkbhs());
            }

            // 等于0，则不含税金额相等，直接取SAP不含税金额
            if (materialFreePrice.compareTo(sapFreePrice) == 0) {
                BigDecimal bigDecima = new BigDecimal("0");
                for (int j = 0; j < listList.get(i).size(); j++) {
                    if (j == listList.get(i).size() - 1) {
                        BigDecimal decimal = materialFreePrice.subtract(bigDecima);
                        listList.get(i).get(j).setItemAmount(decimal);
                    } else {
                        listList.get(i).get(j).setItemAmount(listList.get(i).get(j).getZzkkbhs());
                        bigDecima = bigDecima.add(listList.get(i).get(j).getZzkkbhs());
                    }
                }
                invoiceMaterialSapService.updateByPostQm(listList.get(i));
                // 不为0; 则按金额分配
            } else {
                BigDecimal bigDecima = new BigDecimal("0");
                for (int j = 0; j < listList.get(i).size(); j++) {
                    if (j == listList.get(i).size() - 1) {
                        BigDecimal decimal = materialFreePrice.subtract(bigDecima);
                        listList.get(i).get(j).setItemAmount(decimal);
                    } else {
//                        BigDecimal c = materialFreePrice.divide(sapFreePrice,8, RoundingMode.HALF_UP);
                        BigDecimal bigDecimal = materialFreePrice.divide(sapFreePrice, 8, RoundingMode.HALF_UP).multiply(listList.get(i).get(j).getZzkkbhs());
                        bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
                        listList.get(i).get(j).setItemAmount(bigDecimal);
                        bigDecima = bigDecima.add(bigDecimal);
                    }
                }
                invoiceMaterialSapService.updateByPostQm(listList.get(i));
            }
        }
        return invoiceMaterialSapService.getListBySapId(invoiceMaterialSapEntityList);
    }

    private String createProvider(List<InputInvoiceMaterialEntity> invoiceMaterialEntityList, List<InputInvoiceMaterialSapEntity> invoiceMaterialSapEntityList, List<InputInvoiceEntity> invoiceEntityList, InputInvoiceBatchEntity invoiceBatchEntity) {
        //        destination.removeThroughput();
        List<InputInvoiceMaterialSapEntity> invoiceMaterialSapEntities = setAmount(invoiceMaterialEntityList, invoiceMaterialSapEntityList);
        ZInvoiceInfo invoice = getInvoiceInfo(invoiceMaterialEntityList, invoiceMaterialSapEntities, invoiceBatchEntity);
        try {
            System.out.println("发票校验成功!");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            Date pstngDate = formatter.parse(invoice.PSTNG_DATE);
            //更新批次发票状态
            InputInvoiceEntity invoiceEntity = invoiceEntityList.get(0);
            invoiceEntity.setInvoiceStatus("11");
            invoiceEntity.setEntryDate(format1.format(pstngDate));
            invoiceEntity.setEntrySuccessCode("12");
            invoiceEntity.setBelnr("12");
            invoiceEntity.setInvoiceErrorDescription("入账成功");
            this.updateByEnter(invoiceEntity);

            // 获取批次下所属发票/invoiceList
            List<InputInvoiceEntity> invoiceEntities = this.getListByBatchId(invoiceEntity);
            int count = 0;
            for (int i = 0; i < invoiceEntities.size(); i++) {
                if (invoiceEntities.get(i).getInvoiceStatus().equals("11")) {
                    count++;
                }
            }
            if (count == invoiceEntities.size()) {
                // 更新批次状态
                InputInvoiceBatchEntity batchEntity = new InputInvoiceBatchEntity();
                batchEntity.setId(Integer.valueOf(invoiceEntity.getInvoiceBatchNumber()));
                batchEntity.setInvoiceBatchStatus("7");
                batchEntity.setEntrySuccessCode("23");
                batchEntity.setBelnr("22");
                batchEntity.setEntryDate(format1.format(pstngDate));
                batchEntity.setThreeErrorDescription("");
                batchEntity.setCheckWhiteBlackStatus("");
                invoiceBatchService.update(batchEntity);
            }

            for (int i = 0; i < invoiceMaterialSapEntityList.size(); i++) {
                invoiceMaterialSapEntityList.get(i).setMessage("");
                invoiceMaterialSapEntityList.get(i).setEntryYear("2019");
            }
            invoiceMaterialSapService.updateByEnter(invoiceMaterialSapEntityList);
//                }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("RFC函数调用失败!" + ex.getMessage());
        }
        return "0";
    }


    private ZInvoiceInfo getInvoiceInfo(List<InputInvoiceMaterialEntity> invoiceMaterialEntityList, List<InputInvoiceMaterialSapEntity> invoiceMaterialSapEntityList, InputInvoiceBatchEntity invoiceBatchEntity) {
        ZInvoiceInfo invoice = new ZInvoiceInfo();
        // 获取批次号
        InputInvoiceBatchEntity batchEntity = new InputInvoiceBatchEntity();
        batchEntity.setId(Integer.valueOf(invoiceMaterialSapEntityList.get(0).getBatchId()));
        batchEntity = invoiceBatchService.get(batchEntity);
        List<Integer> invoiceIds = new ArrayList<>();
        for (int i = 0; i < invoiceMaterialEntityList.size(); i++) {
            invoiceIds.add(invoiceMaterialEntityList.get(i).getInvoiceId());
        }
        List<InputInvoiceEntity> invoiceEntityList = this.getListByIds(invoiceIds);
        BigDecimal totlePrice = new BigDecimal("0");
        for (int i = 0; i < invoiceEntityList.size(); i++) {
            // 初始化Acctount
            ZInvoiceAccount account = new ZInvoiceAccount();
            //TODO 存发票号码
            account.INVOICE_NO = invoiceEntityList.get(i).getInvoiceNumber();
            account.TAX_AMOUNT = invoiceEntityList.get(i).getInvoiceTaxPrice().doubleValue();
            invoice.Accounts.add(account);
            totlePrice = totlePrice.add(invoiceEntityList.get(i).getInvoiceTotalPrice());
        }
        invoice.BATCH = (invoiceEntityList.get(0).getBatchNumber()).substring(2);  // 税务平台的批次号
        invoice.INV_NEW = invoiceEntityList.get(0).getInvoiceNumber(); // 凭着组中最近一张发票的发票号码
        invoice.COMP_CODE = invoiceMaterialSapEntityList.get(0).getBukrs();  // 公司代码
        if (invoiceBatchEntity != null && StringUtils.isNotBlank(invoiceBatchEntity.getPMNTTRMS())) {
            invoice.PMNTTRMS = invoiceBatchEntity.getPMNTTRMS();
        } else {
            InputInvoiceConditionMap invoiceConditionMap = invoiceConditionMapService.getMaxConditionCode(invoiceMaterialSapEntityList.get(0));
            if (invoiceConditionMap.getConditionDate() < 30) {
                return null;
            }
            invoice.PMNTTRMS = invoiceConditionMap.getConditionCode();  //付款条件代码
        }
        //TODO 当前时间 String类型
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat mat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
        invoice.DOC_DATE = invoiceEntityList.get(0).getInvoiceCreateDate().replace("-", "");  //凭证中的凭证日期
        if (invoiceBatchEntity != null && StringUtils.isNotBlank(invoiceBatchEntity.getPSTNGDATE())) {
            try {
                if (invoiceBatchEntity.getPSTNGDATE().length() != 10) {
                    String date = invoiceBatchEntity.getPSTNGDATE();
                    //TODO BUG
                    date = date.replace("Z", " UTC");
                    try {
                        Date d = mat.parse(date);
                        invoice.PSTNG_DATE = formatter.format(d);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            invoice.PSTNG_DATE = formatter.format(new Date());  //凭证中的过帐日期
        }
        //TODO 取质检入账日期（来源于SAP物料接口QMDATE 质检日期）、仓库入账日期（来源SAP物料接口BUDATE_MKPF 入库日期）和发票开票日期（即DOC_DATE）三个日期的最大（近）值
        Integer maxQmdate = Integer.valueOf(invoiceMaterialSapService.getMaxQmdateByBatchId(invoiceMaterialSapEntityList.get(0)).replace("-", ""));
        Integer maxBudatMkpf = Integer.valueOf(invoiceMaterialSapService.getMaxBudatMkpfByBatchId(invoiceMaterialSapEntityList.get(0)).replace("-", ""));
        Integer maxCreateDate = Integer.valueOf(invoiceEntityList.get(0).getInvoiceCreateDate().replace("-", ""));
        if ((maxQmdate > maxBudatMkpf ? (maxQmdate > maxCreateDate ? maxQmdate : maxCreateDate) : (maxBudatMkpf > maxCreateDate ? maxBudatMkpf : maxCreateDate)) == maxQmdate) {
            invoice.BLINE_DATE = invoiceMaterialSapService.getMaxQmdateByBatchId(invoiceMaterialSapEntityList.get(0)).replace("-", "");
        } else if ((maxQmdate > maxBudatMkpf ? (maxQmdate > maxCreateDate ? maxQmdate : maxCreateDate) : (maxBudatMkpf > maxCreateDate ? maxBudatMkpf : maxCreateDate)) == maxBudatMkpf) {
            invoice.BLINE_DATE = invoiceMaterialSapService.getMaxBudatMkpfByBatchId(invoiceMaterialSapEntityList.get(0)).replace("-", "");
        } else {
            invoice.BLINE_DATE = invoiceEntityList.get(0).getInvoiceCreateDate().replace("-", "");
        }
        invoice.VEND_NAME = invoiceMaterialSapEntityList.get(0).getSortl();  //供应商名称
        invoice.GROSS_AMOUNT = totlePrice.doubleValue();  //凭证货币的总发票金额
        for (int i = 0; i < invoiceMaterialSapEntityList.size(); i++) {
            String taxCode = "";
            String[] materialLineIdInteger = invoiceMaterialSapEntityList.get(i).getMaterialLineId().split(",");
            Integer[] materialLineId = (Integer[]) ConvertUtils.convert(materialLineIdInteger, Integer.class);
            List<InputInvoiceMaterialEntity> invoiceMaterialEntities = invoiceMaterialService.getByIds(materialLineId);
            InputInvoiceTaxRateTransition invoiceTaxRateTransition = new InputInvoiceTaxRateTransition();
            // 只有一条发票行
            if (invoiceMaterialEntities.size() == 1) {
                String taxRate = invoiceMaterialEntities.get(0).getSphSlv().substring(0, invoiceMaterialEntities.get(0).getSphSlv().length() - 1);
                invoiceTaxRateTransition.setTaxRate(taxRate);
                invoiceTaxRateTransition = invoiceTaxRateTransitionService.getByTaxRate(invoiceTaxRateTransition);
                // 税率映射对象不为null,传递对应税码
                if (invoiceTaxRateTransition != null) {
                    taxCode = invoiceTaxRateTransition.getTaxCode();
                }
            } else {
                String sphSlv = invoiceMaterialEntities.get(0).getSphSlv();
                int count = 1;
                for (int j = 1; j < invoiceMaterialEntities.size(); j++) {
                    if (invoiceMaterialEntities.get(j).getSphSlv().equals(sphSlv)) {
                        count++;
                    }
                }
                // 判断多个发票行的税率是否相等
                if (count == invoiceMaterialEntities.size()) {
                    invoiceTaxRateTransition.setTaxRate(sphSlv.substring(0, sphSlv.length() - 1));
                    invoiceTaxRateTransition = invoiceTaxRateTransitionService.getByTaxRate(invoiceTaxRateTransition);
                    // 税率映射对象不为null,传递对应税码
                    if (invoiceTaxRateTransition != null) {
                        taxCode = invoiceTaxRateTransition.getTaxCode();
                    }
                }
            }

            ZInvoiceDetailInfo invoiceDetail = new ZInvoiceDetailInfo();
            invoiceDetail.PO_NUMBER = invoiceMaterialSapEntityList.get(i).getEbeln();  //采购订单编号
            invoiceDetail.PO_ITEM = invoiceMaterialSapEntityList.get(i).getEbelp();  //采购凭证的项目编号
            invoiceDetail.MAT_DOC = invoiceMaterialSapEntityList.get(i).getMblnr();  //物料凭证编号
            invoiceDetail.MAT_ITEM = invoiceMaterialSapEntityList.get(i).getZeile();  //物料凭证中的项目
            BigDecimal quantity = invoiceMaterialSapEntityList.get(i).getMengeQm();
            if (quantity.toString().contains("-")) {
                quantity = new BigDecimal(quantity.toString().replace("-", ""));
            }
            invoiceDetail.QUANTITY = quantity.doubleValue();  //数量
            invoiceDetail.ITEM_AMOUNT = invoiceMaterialSapEntityList.get(i).getItemAmount().doubleValue();  //凭证货币金额
            invoiceDetail.UNIT = invoiceMaterialSapEntityList.get(i).getMeins();  //订单单位
//            invoiceDetail.UNIT = "";  // 订单单位
            invoiceDetail.TAX_CODE = taxCode;  // 税码
            invoice.Details.add(invoiceDetail);
        }
        return invoice;
    }


    private void TagInputTransOut(List<InputInvoiceMaterialEntity> invoiceMaterialEntityList, InputInvoiceEntity invoiceEntity) {
        List<InputTansOutCategoryEntity> inputTransOutCateogryEntities = inputTransOutCateogryService.getList();
        Map<String, String> inputTransOutCateogryMap = new HashMap<>();

        for (int i = 0; i < inputTransOutCateogryEntities.size(); i++) {
            inputTransOutCateogryMap.put(inputTransOutCateogryEntities.get(i).getItem(), inputTransOutCateogryEntities.get(i).getCategory());

        }

        Set keys = inputTransOutCateogryMap.keySet();

        for (int i = 0; i < invoiceMaterialEntityList.size(); i++) {
            String sphSpmc = invoiceMaterialEntityList.get(i).getSphSpmc();
            String sphSpmc1 = sphSpmc.substring(1);
            String skjm[] = sphSpmc1.split("\\*");
            System.out.println(skjm[0]);
            String spmc[] = sphSpmc.split("\\*");
            String spmcStr = spmc[2];
            if (inputTransOutCateogryMap.get(skjm[0]) != null) {
                invoiceEntity.setInvoiceTransOut(inputTransOutCateogryMap.get(skjm[0]));
                update(invoiceEntity);
            }
            if (inputTransOutCateogryMap.get(spmcStr) != null) {
                invoiceEntity.setInvoiceTransOut(inputTransOutCateogryMap.get(spmcStr));
                update(invoiceEntity);

            }
            System.out.println(invoiceEntity);


        }
    }


    private BigDecimal getBigDecimal(Object value) {
        BigDecimal ret = null;
        if (value != null) {
            if (value instanceof BigDecimal) {
                ret = (BigDecimal) value;
            } else if (value instanceof String) {
                ret = new BigDecimal((String) value);
            } else if (value instanceof BigInteger) {
                ret = new BigDecimal((BigInteger) value);
            } else if (value instanceof Number) {
                ret = new BigDecimal(((Number) value).doubleValue());
            } else {
                throw new ClassCastException("Not possible to coerce [" + value + "] from class " + value.getClass() + " into a BigDecimal.");
            }
        }
        return ret;
    }


    //验真
    private Map<String, String> invoiceCheck(InputInvoiceEntity invoiceEntity) {
        InputSapConfEntity sapConfEntity = sapConfEntityService.getOneById(5);
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("appid", sapConfEntity.getUser());// appid
        querys.put("serviceid", "S000401");
        querys.put("jtnsrsbh", sapConfEntity.getClient());// jtnsrsbh
        querys.put("nsrsbh", "");//值可以为空

        com.alibaba.fastjson.JSONObject contentMap = new com.alibaba.fastjson.JSONObject();
        if ("专用发票".equals(invoiceEntity.getInvoiceEntity())) {
            contentMap.put("fplx", "01");//发票类型
            contentMap.put("fpje", invoiceEntity.getInvoiceFreePrice());//发票金额
        } else if ("普通发票".equals(invoiceEntity.getInvoiceEntity())) {
            contentMap.put("fplx", "04");//发票类型
            contentMap.put("jym", invoiceEntity.getInvoiceCheckCode());
        } else if ("电子发票".equals(invoiceEntity.getInvoiceEntity())) {
            contentMap.put("fplx", "10");//发票类型
            contentMap.put("jym", invoiceEntity.getInvoiceCheckCode());
        }
        try {
//            Date date = sdf.parse(invoiceEntity.getInvoiceCreateDate());
            Date date = DateUtil.parse(invoiceEntity.getInvoiceCreateDate());
            contentMap.put("kprq", sdf1.format(date));//开票日期
        } catch (Exception e) {
            e.printStackTrace();
        }
        contentMap.put("fpdm", invoiceEntity.getInvoiceCode());//发票代码
        contentMap.put("fphm", invoiceEntity.getInvoiceNumber());//发票号码

        System.out.println("contentMap====" + contentMap);
        String content = Base64Util.encode(contentMap.toString());
        querys.put("content", content);

        String signR = SignUtil.sign(sapConfEntity.getPassWord(), querys);
        querys.put("signature", signR);
        System.out.println("querys====" + querys);

        Map<String, String> result = HttpUtil.post(sapConfEntity.getPostUrl(), querys);
        System.out.println("result===" + result);
        //将该接口的日志生成到服务器中
        Map<String, String> map = new HashMap<>();
        map.put("querys", querys.toString());
        String inParam = map.toString();
        String outParam = result.toString();
        createLog(inParam, outParam, "invoiceCheck");
        return result;
    }


    private static List<String> removeString(List<String> list) {
        List<String> listTemp = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            if (!listTemp.contains(list.get(i))) {
                listTemp.add(list.get(i));
            }
        }
        return listTemp;
    }


    private List<InputInvoiceEntity> deleteInvoiceList(List<InputInvoiceEntity> invoiceEntityList, List<String> invoiceNumber) {
        for (int i = 0; i < invoiceNumber.size(); i++) {
            Iterator<InputInvoiceEntity> it = invoiceEntityList.iterator();

            try {
                while (it.hasNext()) {
                    if (invoiceNumber.get(i).equals(it.next().getInvoiceNumber())) {
                        it.remove();
                    }
                }
            } catch (Exception e) {
                System.out.println(
                        "error: " + e.getMessage()
                );


            }

        }
        return invoiceEntityList;
    }

    private InputInvoiceEntity setInvoiceCheckEntity(CallResult<BaseInvoice> invoiceCheck, InputInvoiceEntity invoiceEntity) {

        if (invoiceCheck.getData().getInvoiceType().equals("01")) {
            SpecialInvoiceInfo invoiceCheckEntity = (SpecialInvoiceInfo) invoiceCheck.getData();
            if (invoiceCheckEntity != null) {
                invoiceEntity.setInvoiceTotalPrice(new BigDecimal(invoiceCheckEntity.getAmountTax()));
                invoiceEntity.setInvoicePurchaserParagraph(invoiceCheckEntity.getPurchaserTaxNo());
                invoiceEntity.setInvoicePurchaserCompany(invoiceCheckEntity.getPurchaserName());
                invoiceEntity.setInvoiceSellParagraph(invoiceCheckEntity.getSalesTaxNo());
                invoiceEntity.setInvoiceSellCompany(invoiceCheckEntity.getSalesName());
                invoiceEntity.setInvoiceCheckCode(invoiceCheckEntity.getCheckCode());
                invoiceEntity.setInvoicePurchaserBankAccount(invoiceCheckEntity.getPurchaserBank());
                invoiceEntity.setInvoicePurchaserAddress(invoiceCheckEntity.getPurchaserAddressPhone());
                invoiceEntity.setInvoiceSellBankAccount(invoiceCheckEntity.getSalesBank());
                invoiceEntity.setInvoiceSellAddress(invoiceCheckEntity.getSalesAddressPhone());
                invoiceEntity.setInvoiceTaxPrice(new BigDecimal(invoiceCheckEntity.getTotalTax()));
                invoiceEntity.setInvoiceRemarks(invoiceCheckEntity.getRemarks());
                invoiceEntity.setInvoiceEntity("专用发票");
            }
        } else if (invoiceCheck.getData().getInvoiceType().equals("10")) {
            ENormalInvoiceInfo invoiceCheckEntity = (ENormalInvoiceInfo) invoiceCheck.getData();
            if (invoiceCheckEntity != null) {
                invoiceEntity.setInvoiceTotalPrice(new BigDecimal(invoiceCheckEntity.getAmountTax()));
                invoiceEntity.setInvoicePurchaserParagraph(invoiceCheckEntity.getPurchaserTaxNo());
                invoiceEntity.setInvoicePurchaserCompany(invoiceCheckEntity.getPurchaserName());
                invoiceEntity.setInvoiceSellParagraph(invoiceCheckEntity.getSalesTaxNo());
                invoiceEntity.setInvoiceSellCompany(invoiceCheckEntity.getSalesName());
                invoiceEntity.setInvoiceCheckCode(invoiceCheckEntity.getCheckCode());
                invoiceEntity.setInvoicePurchaserBankAccount(invoiceCheckEntity.getPurchaserBank());
                invoiceEntity.setInvoicePurchaserAddress(invoiceCheckEntity.getPurchaserAddressPhone());
                invoiceEntity.setInvoiceSellBankAccount(invoiceCheckEntity.getSalesBank());
                invoiceEntity.setInvoiceSellAddress(invoiceCheckEntity.getSalesAddressPhone());
                invoiceEntity.setInvoiceTaxPrice(new BigDecimal(invoiceCheckEntity.getTotalTax()));
                invoiceEntity.setInvoiceRemarks(invoiceCheckEntity.getRemarks());
                invoiceEntity.setInvoiceEntity("普通发票");
            }
        } else if (invoiceCheck.getData().getInvoiceType().equals("04")) {
            NormalInvoiceInfo invoiceCheckEntity = (NormalInvoiceInfo) invoiceCheck.getData();
            if (invoiceCheckEntity != null) {
                invoiceEntity.setInvoiceTotalPrice(new BigDecimal(invoiceCheckEntity.getAmountTax()));
                invoiceEntity.setInvoicePurchaserParagraph(invoiceCheckEntity.getPurchaserTaxNo());
                invoiceEntity.setInvoicePurchaserCompany(invoiceCheckEntity.getPurchaserName());
                invoiceEntity.setInvoiceSellParagraph(invoiceCheckEntity.getSalesTaxNo());
                invoiceEntity.setInvoiceSellCompany(invoiceCheckEntity.getSalesName());
                invoiceEntity.setInvoiceCheckCode(invoiceCheckEntity.getCheckCode());
                invoiceEntity.setInvoicePurchaserBankAccount(invoiceCheckEntity.getPurchaserBank());
                invoiceEntity.setInvoicePurchaserAddress(invoiceCheckEntity.getPurchaserAddressPhone());
                invoiceEntity.setInvoiceSellBankAccount(invoiceCheckEntity.getSalesBank());
                invoiceEntity.setInvoiceSellAddress(invoiceCheckEntity.getSalesAddressPhone());
                invoiceEntity.setInvoiceTaxPrice(new BigDecimal(invoiceCheckEntity.getTotalTax()));
                invoiceEntity.setInvoiceRemarks(invoiceCheckEntity.getRemarks());
                invoiceEntity.setInvoiceEntity("普通发票");
            }
        }
        return invoiceEntity;
    }


    private String getInvoiceOCRresult(InputInvoiceEntity invoiceEntity) {

        InputSapConfEntity sapConfEntity = new InputSapConfEntity();
        if (invoiceEntity.getInvoiceType().equals("picture")) {
            sapConfEntity = sapConfEntityService.getOneById(7);
        } else {
            sapConfEntity = sapConfEntityService.getOneById(9);
        }

        String url = sapConfEntity.getPostUrl();
        String sapPath = ClassUtil.getClassPath() + "statics/db/pic/";
        String picUrl = OAPathUtil.imagePath + invoiceEntity.getInvoiceImage(); // 获取路径

//        String[] arr = picUrl.split("/");
//        String fileName = sapPath + arr[arr.length - 1];  // 输出路径
//        try {
//            DownloadUtil.downloadPicture(picUrl, fileName);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "19";
//        }
//        String fileName = osName.toLowerCase().contains("windows")?"D:/"+ invoiceEntity.getInvoiceImage(): "/data"+ invoiceEntity.getInvoiceImage();
        String content = httpUploadFile.connectionOCR(url, picUrl);
        //TODO 接收到ocr返回结果，删除图片
//        File file = new File(fileName);
//        file.delete();

        return content;

    }

    /**
     * 数据同步
     */
    private List<InputInvoiceSyncEntity> getSkssqEntity(String nsrsbh, String createDate, String synctype) {
        try {
            Map<String, String> querys = new HashMap<String, String>();
            querys.put("appid", appid);
            querys.put("serviceid", "S000501");
            querys.put("jtnsrsbh", jtnsrsbh);// 集团税号
            querys.put("nsrsbh", nsrsbh);// 集团下企业税号
            /*913100007178609214*/
            com.alibaba.fastjson.JSONObject contentMap = new com.alibaba.fastjson.JSONObject();
            contentMap.put("synctype", synctype);//01--按开票日期同步（YYYY-MM-DD）
            contentMap.put("synccondition", createDate);
            //		contentMap.put("synctype", "02");//02--按开票日期所属月份同步（YYYY-MM）
            //		contentMap.put("synccondition", "2018-03");
            //		contentMap.put("synctype", "03");//03--按税款所属期同步（YYYY-MM）
            //		contentMap.put("synccondition", "2018-03");
            //		contentMap.put("synctype", "04");//04--按开票日期范围同步（YYYY-MM-DD,YYYY-MM-DD）
            //		contentMap.put("synccondition", "2018-03-01,2018-03-31");
            System.out.println("contentMap====" + contentMap);
            String content = Base64Util.encode(contentMap.toString());
            querys.put("content", content);

            String signR = SignUtil.sign(appsecret, querys);
            querys.put("signature", signR);
            System.out.println("querys====" + querys);

            Map<String, String> result = HttpUtil.post(url, querys);
            System.out.println("result===" + result);
            // base64解密，获取具体所属期
            String value = result.get("response");
            JSONObject json = JSONObject.fromObject(value);
            System.out.println(json);
            String code = (String) json.get("code");

            if ("0000".equals(code)) {
                String contentValue = (String) json.get("content");
                String val = Base64Util.decode(contentValue);
                System.out.println(val + "val");
                String invoiceListJson = com.alibaba.fastjson.JSONObject.parseObject(val).getJSONArray("InvoiceList").toJSONString();
                System.out.println("invoiceListJson" + invoiceListJson);
                // 企业当前税款所属期
                List<InputInvoiceSyncEntity> invoiceList = JSON.parseObject(invoiceListJson, new TypeReference<List<InputInvoiceSyncEntity>>() {
                });
                // map.put("content", fpxx);
                return invoiceList;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}