package com.pwc.modules.openAPI.service.impl;


import cn.hutool.core.util.ClassUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fapiao.neon.client.in.CheckInvoiceClient;
import com.fapiao.neon.model.CallResult;
import com.fapiao.neon.model.in.inspect.BaseInvoice;
import com.fapiao.neon.param.in.InvoiceInspectionParamBody;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pwc.common.utils.HttpUploadFile;
import com.pwc.common.utils.R;
import com.pwc.common.utils.UploadFileEntity;
import com.pwc.common.utils.UploadKitUtil;
import com.pwc.modules.input.entity.InputSapConfEntity;
import com.pwc.modules.input.service.InputSapConfEntityService;
import com.pwc.modules.openAPI.entity.ApiRequestConfig;
import com.pwc.modules.openAPI.service.ApiRequestConfigService;
import com.pwc.modules.openAPI.service.ApiRequestCountService;
import com.pwc.modules.openAPI.service.ApiService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;

import java.util.HashMap;
import java.util.Map;

@Service("apiService")
public class ApiServiceImpl implements ApiService {
    @Resource
    private CheckInvoiceClient checkInvoiceClient;
    @Autowired
    private ApiRequestCountService apiRequestCountService;
    @Autowired
    private ApiRequestConfigService apiRequestConfigService;
    @Autowired
    private InputSapConfEntityService inputSapConfEntityService;
    @Autowired
    private HttpUploadFile httpUploadFile;
    @Override
    public R invoiceCheck(Map<String, Object> params) {
        CallResult<BaseInvoice> invoiceCheck ;
        try {
            Integer companyId = Integer.valueOf((String)params.get("companyId"));
            String companyKey = (String) params.get("companyKey");
            if(StringUtils.isBlank(companyKey)||StringUtils.isBlank((String)params.get("companyId"))){
                return R.error("缺少必要参数，companyId和companyKey不可为空");
            }
            apiRequestCountService.saveRequest(companyId,"invoiceCheckTrue");
            ApiRequestConfig one = apiRequestConfigService.getOne(
                    new QueryWrapper<ApiRequestConfig>()
                            .eq("company_id", companyId)
                            .eq("type", "invoiceCheckTrue")
                            .eq("company_key", companyKey));
            if (one==null){
                return R.error("无权限");
            }else {
                String type = (String) params.get("type");
                //发票类型为 01、03、15、20时不可为空；01、03、20填写发票不含税金额；15填写发票车价合计。
                String totalAmount = (String) params.get("totalAmount");
                if (("01".equals(type) || "03".equals(type) || "15".equals(type) || "20".equals(type)) && StringUtils.isBlank(totalAmount)) {
                    return R.error("发票类型为 01、03、15、20时，totalAmount不可为空");
                }
                String billingDate = (String) params.get("billingDate");
                String invoiceNumber = (String) params.get("invoiceNumber");
                String invoiceCode = (String) params.get("invoiceCode");
                //发票校验码后6位。发票类型为04、10、11、14时此项不可为空。
                String checkCode = (String) params.get("checkCode");
                if (("04".equals(type) || "10".equals(type) || "11".equals(type) || "14".equals(type)) && StringUtils.isBlank(checkCode)) {
                    return R.error("发票类型为04、10、11、14时，checkCode不可为空");
                }
                System.out.println("type:" + type + "\ntotalAmount：" + totalAmount + "\nbillingDate：" + billingDate + "\ninvoiceNumber：" + invoiceNumber + "\ninvoiceCode：" + invoiceCode + "\ncheckCode：" + checkCode);
                InvoiceInspectionParamBody invoiceInspectionParamBody = new InvoiceInspectionParamBody();
                invoiceInspectionParamBody.setBillingDate(billingDate);
                invoiceInspectionParamBody.setInvoiceNumber(invoiceNumber);
                invoiceInspectionParamBody.setInvoiceCode(invoiceCode);
                invoiceInspectionParamBody.setTotalAmount(totalAmount);
                invoiceInspectionParamBody.setCheckCode(checkCode);
                invoiceCheck = checkInvoiceClient.check(invoiceInspectionParamBody);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return  R.error();
        }
        return  R.ok().put("result",invoiceCheck);
    }

    @Override
    public R vatInvoice(Map<String, Object> params, MultipartFile file) {
        Integer companyId = Integer.valueOf((String)params.get("companyId"));
        String companyKey = (String) params.get("companyKey");
        if(StringUtils.isBlank(companyKey)||StringUtils.isBlank((String)params.get("companyId"))||file==null){
            return R.error("缺少必要参数，companyId、companyKey、file不可为空");
        }
        String originalFilename = file.getOriginalFilename();
        String substring = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
        if(!("jpg".equals(substring)||"png".equals(substring)||"jpeg".equals(substring))){
            return R.error("file类型只支持jpg,png,jpeg");
        }
        try {
            apiRequestCountService.saveRequest(companyId,"vatInvoice");
            ApiRequestConfig one = apiRequestConfigService.getOne(
                    new QueryWrapper<ApiRequestConfig>()
                            .eq("company_id", companyId)
                            .eq("type", "vatInvoice")
                            .eq("company_key", companyKey));
            if (one==null){
                return R.error("无权限");
            }else {
                InputSapConfEntity sapConfEntity = inputSapConfEntityService.getOneById(7);
                String url = sapConfEntity.getPostUrl();
                // 文件保存路径
                String filepath = "statics/db/pic/";
                UploadFileEntity fileEntity = UploadKitUtil.uploadFile(file,filepath,true, false);
                String fileName = fileEntity.getServerPath();
                String  content = httpUploadFile.connectionOCR(url, fileName/*,""*/);
                // 接收到ocr返回结果，删除图片
                File file1 = new File(ClassUtil.getClassPath()+fileName);
                file1.delete();

                //处理json格式问题
                Gson g = new Gson();
                JsonObject jsonObject = g.fromJson(content, JsonObject.class);
                String r=jsonObject.toString();
                Map<String,Object> map1=new HashMap<>();
                if(r.contains("scan_result")){
                    String filename = jsonObject.get("filename").getAsString();
                    map1.put("filename",filename);
                    JsonElement scan_result= jsonObject.get("scan_result");
                    Map<String,Object> map2=new HashMap<>();
                    JsonObject asJsonObject = scan_result.getAsJsonObject();
                    String vat_type = asJsonObject.get("vat_type").getAsString();
                    String number = asJsonObject.get("number").getAsString();
                    String code = asJsonObject.get("code").getAsString();
                    String date1 = asJsonObject.get("date").getAsString();
                    String check_code = asJsonObject.get("check_code").getAsString();
                    String freePrice = asJsonObject.get("total_amount_without_tax").getAsString();
                    String invoice_type = asJsonObject.get("invoice_type").getAsString();
                    String printed_code = asJsonObject.get("printed_code").getAsString();
                    String printed_number = asJsonObject.get("printed_number").getAsString();
                    map2.put("vat_type",vat_type);
                    map2.put("number",number);
                    map2.put("code",code);
                    map2.put("date",date1);
                    map2.put("check_code",check_code);
                    map2.put("total_amount_without_tax",freePrice);
                    map2.put("invoice_type",invoice_type);
                    map2.put("printed_code",printed_code);
                    map2.put("printed_number",printed_number);
                    map1.put("scan_result",map2);
                }else{
                    String errorMsg = jsonObject.get("errorMsg").getAsString();
                    String errorCode =  jsonObject.get("errorCode").getAsString();
                    map1.put("errorMsg",errorMsg);
                    map1.put("errorCode",errorCode);
                }
                return  R.ok().put("result",map1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.error();
        }

    }
}
