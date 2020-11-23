package com.pwc.modules.input.controller;

import cn.hutool.core.util.ClassUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pwc.common.utils.*;
import com.pwc.modules.input.entity.*;
import com.pwc.modules.input.service.*;
import com.pwc.modules.sys.shiro.ShiroUtils;
import org.apache.http.entity.ContentType;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("input/OCRService")
public class OCRcontroller {

    @Autowired
    InputOaExpenseInfoService oaExpenseInfoService;


    @Autowired
    InputOaExpenseInvoicetypeMappingService oaExpenseInvoicetypeMappingService;

    @Autowired
    InputOaExpenseInvoiceMappingService oaExpenseInvoiceMappingService;

    @Autowired
    InputInvoiceService invoiceService;

    @Autowired
    InputUnformatInvoiceService unformatInvoiceService;


    @PostMapping("/uploadOcr")
    @RequiresPermissions("input:OCRService:uploadOcr")
    public R uploadOcr(@RequestBody InputImageUrlListBean imageUrlListBean) throws IOException {

        R r = new R();
        InvoiceUtils invoiceUtils = new InvoiceUtils();
        String expenseNo = imageUrlListBean.getExpenseNo();
        String fromTo = imageUrlListBean.getUploadType();
        String type = imageUrlListBean.getType();


        /**©
         * dowland image form image url
         */
        List<String> invoiceImageList = imageUrlListBean.getImagesList();
        List<String> localimageUrls = new ArrayList<>();
        Set<String> multipartFileUrls = new HashSet<>();
        Map<String, JsonObject> ocrMapResult = new HashMap<>();
        Map<String, MultipartFile> fileMapResult = new HashMap<String, MultipartFile>();
        List<Integer> invoiceNos = new ArrayList<>();
        List<InputInvoiceEntity> invoiceEntities = new ArrayList<>();
        List<InputOaExpenseInvoicetypeMappingEntity> oaExpenseInvoicetypeMappingEntities = new ArrayList<>();
        List<InputOaExpenseInvoiceMappingEntity> oaExpenseInvoiceMappingEntities = new ArrayList<>();
        List<InputUnformatInvoiceEntity> unformatInvoiceEntities = new ArrayList<InputUnformatInvoiceEntity>();
        Map<Integer, String> invoiceNOTypeMapping = new HashMap<>();
        InputOaExpenseInfoEntity oaExpenseInfoEntity = oaExpenseInfoService.getById(Integer.parseInt(expenseNo));


//        for (String localimageUrl : localimageUrls) {
//
//            File file = new File(localimageUrl);
//            FileInputStream fileInputStream = new FileInputStream(file);
//
//            MultipartFile multipartFile = new MockMultipartFile(file.getName(),
//                    file.getName(),
//                    ContentType.APPLICATION_OCTET_STREAM.toString(),
//                    fileInputStream);
//            fileMapResult.put(localimageUrl, multipartFile);
//
//        }
//        multipartFileUrls = fileMapResult.keySet();
        JsonArray objectList = new JsonArray();

        for (String localUrl : invoiceImageList) {
            try {
                File file = new File(ClassUtil.getClassPath() + localUrl);
                FileInputStream fileInputStream = new FileInputStream(file);

                MultipartFile multipartFile = new MockMultipartFile(file.getName(),
                        file.getName(),
                        ContentType.APPLICATION_OCTET_STREAM.toString(),
                        fileInputStream);
                fileMapResult.put(localUrl, multipartFile);
                String ocrResult = OCRUtils.thirdpartyOCR(multipartFile);
                JsonObject jsonObject = new JsonParser().parse(ocrResult).getAsJsonObject();
                JsonObject result = jsonObject.get("result").getAsJsonObject();
                objectList = result.get("object_list").getAsJsonArray();
            } catch (Exception e) {

                return R.error(510, "调用OCR接口失败");
            }

            for (JsonElement jsonElement : objectList) {
                JsonObject obj = jsonElement.getAsJsonObject();
                String typeDescription = obj.get("type_description").getAsString();
                String invoiceType = obj.get("type").getAsString();
                String classType = obj.get("class").getAsString();
                JsonArray itemList = obj.get("item_list").getAsJsonArray();
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


                if (classType.equals("nation_tax_invoice")) {
                    InputInvoiceEntity invoiceEntity = new InputInvoiceEntity();
                    invoiceEntity.setInvoiceImage(localUrl);
                    invoiceEntity = invoiceUtils.saveHEHEVATInvoice(invoiceEntity, itemList, invoiceType, fromTo);
                    invoiceEntity.setCreateBy(ShiroUtils.getUserEntity().getUserId().intValue());
                    invoiceEntity.setCompanyId(ShiroUtils.getUserEntity().getDeptId().intValue());
                    invoiceEntity.setInvoiceStatus("2");
                    invoiceService.functionCheckTrue(invoiceEntity, "1");
                    if ("增值税专用发票".equals(typeDescription)) {
                        invoiceEntity.setInvoiceStatus(InputConstant.InvoiceStatus.PENDING_CERTIFIED.getValue());
                    } else {
                        invoiceEntity.setInvoiceStatus(InputConstant.InvoiceStatus.FINISHED.getValue());
                    }

                    // TODO check duplicate
                    if (invoiceService.any(invoiceEntity.getInvoiceCode(), invoiceEntity.getInvoiceNumber())) {
                        invoiceEntity.setInvoiceStatus(InputConstant.InvoiceStatus.REPEAT.getValue());
                    }
//                    invoiceService.functionCheckTrue(invoiceEntity, "1");
                    invoiceEntity.setInvoiceReturn("0");
                    invoiceEntity.setInvoiceDelete("0");
//                    invoiceEntity.setInvoiceCreateDate(sdf2.format(new Date()));
                    invoiceEntity.setInvoiceUploadDate(sdf2.format(new Date()));
                    invoiceEntity.setInvoiceExpense(expenseNo);
                    invoiceService.save(invoiceEntity);
                    invoiceEntities.add(invoiceEntity);

                } else {
                    InputUnformatInvoiceEntity unformatInvoiceEntity = new InputUnformatInvoiceEntity();
                    // TODO change the type to right field
                    unformatInvoiceEntity.setFields(typeDescription);
                    unformatInvoiceEntity.setImageUrl(localUrl);
                    unformatInvoiceEntity.setCreateBy(ShiroUtils.getUserEntity().getUserId().intValue());
                    unformatInvoiceEntity.setCompanyId(ShiroUtils.getUserEntity().getDeptId());
                    unformatInvoiceEntity.setCreateTime(sdf2.format(new Date()));
                    invoiceUtils.saveHEHEUnformatInvoide(unformatInvoiceEntity, itemList);
                    unformatInvoiceService.save(unformatInvoiceEntity);
                    unformatInvoiceEntities.add(unformatInvoiceEntity);
                }
            }
        }

        for (InputInvoiceEntity invoiceEntity : invoiceEntities) {
            InputOaExpenseInvoiceMappingEntity oaExpenseInvoiceMappingEntity = new InputOaExpenseInvoiceMappingEntity();
            oaExpenseInvoiceMappingEntity
                    .setInvoiceNo(invoiceEntity.getId());
            oaExpenseInvoiceMappingEntity
                    .setExpenseNo(Integer.parseInt(expenseNo));
            if (invoiceEntity.getInvoiceStatus().equals("0") ||
                    invoiceEntity.getInvoiceStatus().equals("6")) {
                oaExpenseInfoEntity.setExpenseStatus(2);
            } else {
                oaExpenseInfoEntity.setExpenseStatus(1);
            }

            BigDecimal amount = oaExpenseInfoEntity.getAmount();
            if (invoiceEntity.getInvoiceTotalPrice() != null) {
                amount = amount.add(invoiceEntity.getInvoiceTotalPrice());

            } else {
                amount = new BigDecimal(0.0);
            }

            oaExpenseInfoEntity.setInvoicesAmount(amount);

            oaExpenseInfoService.update(oaExpenseInfoEntity);

            oaExpenseInvoiceMappingEntity.setInvoiceType(invoiceEntity.getInvoiceEntity());
            oaExpenseInvoiceMappingEntity.setMoney(invoiceEntity.getInvoiceTotalPrice());
            oaExpenseInvoiceMappingEntity.setCompanyId(ShiroUtils.getUserEntity().getDeptId());
            oaExpenseInvoiceMappingService.save(oaExpenseInvoiceMappingEntity);
        }

        for (InputUnformatInvoiceEntity unformatInvoiceEntity : unformatInvoiceEntities) {
            InputOaExpenseInvoiceMappingEntity oaExpenseInvoiceMappingEntity = new InputOaExpenseInvoiceMappingEntity();
            oaExpenseInvoiceMappingEntity.setInvoiceType(unformatInvoiceEntity.getFields());
            oaExpenseInvoiceMappingEntity.setUnformatNo(unformatInvoiceEntity.getId());
            oaExpenseInvoiceMappingEntity.setMoney(unformatInvoiceEntity.getAmount());
            oaExpenseInvoiceMappingEntity.setExpenseNo(Integer.parseInt(expenseNo));
            oaExpenseInvoiceMappingEntity.setCompanyId(ShiroUtils.getUserEntity().getDeptId());
            oaExpenseInvoiceMappingService.save(oaExpenseInvoiceMappingEntity);
        }

        return R.ok();
    }


}
