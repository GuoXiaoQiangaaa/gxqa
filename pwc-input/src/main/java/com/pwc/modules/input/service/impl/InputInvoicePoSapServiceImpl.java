package com.pwc.modules.input.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.pwc.common.exception.RRException;
import com.pwc.modules.input.dao.InputInvoicePoSapDao;
import com.pwc.modules.input.entity.InputExportDetailEntity;
import com.pwc.modules.input.entity.InputInvoicePoSapEntity;
import com.pwc.modules.input.service.InputInvoicePoSapService;
import com.pwc.modules.sys.shiro.ShiroUtils;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;


@Service("inputInvoicePoSapService")
public class InputInvoicePoSapServiceImpl extends ServiceImpl<InputInvoicePoSapDao, InputInvoicePoSapEntity> implements InputInvoicePoSapService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<InputInvoicePoSapEntity> page = this.page(
                new Query<InputInvoicePoSapEntity>().getPage(params),
                new QueryWrapper<InputInvoicePoSapEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * SAP抓取PO明细导入
    */
    @Override
    public Map<String, Object> importSapPoData(MultipartFile[] files) {
        Map<String, Object> resMap = new HashMap<>();
        // 数据校验正确的集合
        List<InputInvoicePoSapEntity> entityList = new ArrayList<>();
        // 数据重复的集合
        List<InputInvoicePoSapEntity> duplicateList = new ArrayList<>();
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
                String[] excelHead = {"Outline Agreement", "Purchasing Document", "Item", "Document Date", "Purch. Organization",
                        "Plant", "Material", "Name of Vendor", "Short Text", "Order Quantity",
                        "Order Unit", "Net price", "Currency", "Net Order Value", "Still to be delivered (qty)", "Still to be delivered (value)",
                        "Still to be invoiced (qty)", "Still to be invoiced (val.)", "Acct Assignment Cat.", "Purchasing Group", "Material Group",
                        "Req. Tracking Number", "Deletion Indicator", "Item Category"};
                String [] excelHeadAlias = {"outlineAgreement", "purchasingDocument", "item", "documentDate", "purchOrganization",
                        "plant", "material", "nameOfVendor", "shortText", "orderQuantity",
                        "orderUnit", "netPrice", "currency", "netOrderValue", "qtyDelivered", "valDelivered",
                        "qtyInvoiced", "valInvoiced", "assignmentCat", "purchasingGroup", "materialGroup",
                        "trackingNumber", "deletionIndicator", "itemCategory"};
                for (int i = 0; i < excelHead.length; i++) {
                    reader.addHeaderAlias(excelHead[i], excelHeadAlias[i]);
                }
                List<InputInvoicePoSapEntity> dataList = reader.read(0, 1, InputInvoicePoSapEntity.class);

                if(CollectionUtils.isEmpty(dataList)){
                    log.error("上传的{}Excel为空,请重新上传");
//                    throw new RRException("上传的Excel为空,请重新上传");
                    continue;
                }
                total += dataList.size();
                int count = 1;
                for (InputInvoicePoSapEntity entity : dataList) {
                    count++;
                        // 去除Excel中重复数据
                        String repeatData = entity.getOutlineAgreement() + entity.getPurchasingDocument();
                        if(CollectionUtil.contains(repeatDataList, repeatData)){
                            fail += 1;
                            if(!org.apache.commons.lang3.StringUtils.contains(sb.toString(), filename)){
                                sb.append("文件" + filename + "的错误行号为:");
                            }
                            sb.append(count + ",");
                            continue;
                        }
                        repeatDataList.add(repeatData);

                        // 数据库验重
                    InputInvoicePoSapEntity poSap = super.getOne(
                                new QueryWrapper<InputInvoicePoSapEntity>()
                                        .eq("outline_agreement", entity.getOutlineAgreement())
                                        .eq("purchasing_document", entity.getPurchasingDocument())
                        );
                        if(null != poSap){
                            entity.setUpdateBy(String.valueOf(ShiroUtils.getUserId()));
                            entity.setUpdateTime(new Date());
                            duplicateList.add(entity);
                        }else {
                            entity.setCreateBy(String.valueOf(ShiroUtils.getUserId()));
                            entity.setCreateTime(new Date());
                            entityList.add(entity);
                        }
                }
                if(sb.toString().endsWith(",")){
                    sb.deleteCharAt(sb.lastIndexOf(",")).append(";");
                }
            }
            if(sb.toString().endsWith(";")){
                sb.deleteCharAt(sb.lastIndexOf(";")).append("。");
            }

            resMap.put("total", total);
            resMap.put("success", duplicateList.size() + entityList.size());
            resMap.put("fail", fail);
            resMap.put("failDetail", sb.toString());

            if(CollectionUtil.isNotEmpty(duplicateList)){
                super.updateBatchById(duplicateList);
            }
            if(CollectionUtil.isNotEmpty(entityList)){
                super.saveBatch(entityList);
            }
            return resMap;
        } catch (RRException e){
            throw e;
        } catch (Exception e) {
            log.error("SAP抓取PO明细出错: {}", e);
            throw new RRException("SAP抓取PO明细出现异常");
        }
    }

}
