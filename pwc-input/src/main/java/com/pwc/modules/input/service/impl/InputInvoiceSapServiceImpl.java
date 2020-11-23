package com.pwc.modules.input.service.impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.*;
import com.pwc.modules.input.dao.InputInvoiceSapDao;
import com.pwc.modules.input.entity.InputInvoiceEntity;
import com.pwc.modules.input.entity.InputInvoiceSapEntity;
import com.pwc.modules.input.service.InputInvoiceSapService;
import com.pwc.modules.input.service.InputInvoiceService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @description: Sap导入进项税明细
 * @author: Gxw
 * @create: 2020-09-23 15:07
 **/
@Service("inputInvoiceSapService")
public class InputInvoiceSapServiceImpl extends ServiceImpl<InputInvoiceSapDao, InputInvoiceSapEntity> implements InputInvoiceSapService {
    @Autowired
    public InputInvoiceService inputInvoiceService;
    @Override
    public void getImportBySap(MultipartFile file) throws Exception {
        List<String> invoiceNumbers = new ArrayList<>();
        ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
        String[] excelHead = {"Company code", "Account", "Reference", "Document No",
                "Document Type", "Doc. Date", "Pstng Date", "Amount in local cur.",
                "Lcurr", "Amount in doc. curr", "Curr.", "User name",
                "Assignment", "Text", "Tx", "Trading Partner"

        };
        String[] excelHeadAlias = {
                "companyCode", "account", "reference", "documentNo",
                "documentType", "docDate", "pstngDate", "amountInLocal",
                "lcurr", "amountInDoc", "curr", "userName",
                "assignment", "text", "tx", "tradingPartner"};
        for (int i = 0; i < excelHead.length; i++) {
            reader.addHeaderAlias(excelHead[i], excelHeadAlias[i]);
        }
        List<InputInvoiceSapEntity> dataList = reader.read(0, 1, InputInvoiceSapEntity.class);
        for(InputInvoiceSapEntity sapEntity:dataList){
            sapEntity.setSapMatch(InputConstant.InvoiceMatch.MATCH_NO.getValue());
            String[] numbers = sapEntity.getReference().split("/");
            for(String Number:numbers){
                List<InputInvoiceEntity> invoiceEntityList = null;// inputInvoiceService.getInvoiceByNumberAndStatus(Number);
                for(InputInvoiceEntity invoiceEntity: invoiceEntityList){
                    invoiceEntity.setEntrySuccessCode(sapEntity.getDocumentNo());
                    // 更新
                    invoiceEntity.setInvoiceMatch(InputConstant.InvoiceMatch.MATCH_YES.getValue());
                    sapEntity.setSapMatch(InputConstant.InvoiceMatch.MATCH_YES.getValue());
                    invoiceEntity.setMatchDate(DateUtils.format(new Date()));
                    inputInvoiceService.updateById(invoiceEntity);
                }

            }
            save(sapEntity);
        }
    }
    /**查询列表*/
    @Override
    public PageUtils getListBySap(Map<String, Object> params){
        // 公司代码
        String companyCode = (String) params.get("companyCode");
        // 入账日期
        String pstngDate = (String) params.get("pstngDate");
        // 凭证编码
        String documentNo = (String) params.get("documentNo");
        // 科目
        String account = (String) params.get("account");
        // 参考
        String reference = (String) params.get("reference");
        // 分配
        String assignment = (String) params.get("assignment");
        //摘要
        String headerText = (String) params.get("headerText");
        // 匹配状态
        String match =  ParamsMap.findMap(params,"match");
        IPage<InputInvoiceSapEntity> page = this.page(
                new Query<InputInvoiceSapEntity>().getPage(params, null, true),
                new QueryWrapper<InputInvoiceSapEntity>()
                        .eq(StringUtils.isNotBlank(companyCode), "company_code", companyCode)
                        .ge(StringUtils.isNotBlank(pstngDate), "pstng_date",StringUtils.isNotBlank(pstngDate)? pstngDate.split(",")[0]:"")
                        .le(StringUtils.isNotBlank(pstngDate), "pstng_date",StringUtils.isNotBlank(pstngDate)? pstngDate.split(",")[1]:"")
                        .eq(StringUtils.isNotBlank(documentNo), "document_no", documentNo)
                        .eq(StringUtils.isNotBlank(account), "account", account)
                        .like(StringUtils.isNotBlank(reference), "reference", reference)
                        .eq(StringUtils.isNotBlank(assignment), "assignment", assignment)
                        .in(StringUtils.isNotBlank(match), "sap_match", (String[]) params.get("match"))
                        .like(StringUtils.isNotBlank(headerText), "header_text", headerText)
        );
        return new PageUtils(page);

    }
    @Override
    public InputInvoiceSapEntity  getEntityByNo(String documentNo ){
        InputInvoiceSapEntity sapEntity = this.getOne(
                new QueryWrapper<InputInvoiceSapEntity>()
                        .eq("document_no",documentNo)
        );
        return sapEntity;
    }

    @Override
    public List<InputInvoiceSapEntity>  getEntityByDateAndStatus(String date,String status ){
        List<InputInvoiceSapEntity> sapEntitys = this.list(
                new QueryWrapper<InputInvoiceSapEntity>()
                        .like("pstng_date",date)
                        .eq("match_type",status)
        );
        return sapEntitys;
    }

}
