package com.pwc.modules.input.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.exception.RRException;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;
import com.pwc.modules.input.dao.InputExportDetailDao;
import com.pwc.modules.input.entity.InputExportDetailEntity;
import com.pwc.modules.input.service.InputExportDetailService;
import com.pwc.modules.sys.shiro.ShiroUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.NumberFormat;
import java.util.*;

/**
 * 进项转出明细服务实现
 *
 * @author fanpf
 * @date 2020/9/17
 */
@Service("inputExportDetailService")
@Slf4j
public class InputExportDetailServiceImpl extends ServiceImpl<InputExportDetailDao, InputExportDetailEntity> implements InputExportDetailService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<InputExportDetailEntity> page = this.page(
                new Query<InputExportDetailEntity>().getPage(params),
                new QueryWrapper<InputExportDetailEntity>()
        );

        return new PageUtils(page);
    }


    /**
     * 进项转出明细导入
     */
    @Override
    public Map<String, Object> importData(MultipartFile[] files) {
        Map<String, Object> resMap = new HashMap<>();
        // 数据校验正确的集合
        List<InputExportDetailEntity> entityList = new ArrayList<>();
        // 数据重复的集合
        List<InputExportDetailEntity> duplicateList = new ArrayList<>();
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
                String[] excelHead = {"Company Code", "Account", "Reference", "Document Number", "Document Type",
                        "Document Date", "Posting Date", "Amount in local currency", "Local Currency", "Amount in doc. curr.",
                        "Document currency", "User name", "Assignment", "Text", "Tax code", "Trading Partner", "Posting Key", "Year/month", "Document Header Text"};
                String [] excelHeadAlias = {"companyCode", "account", "reference", "documentNo", "documentType",
                        "docDate", "pstngDate", "amountInLocal", "lcurr", "amountInDoc", "curr",
                        "userName", "assignment", "text", "tx", "tradingPartner", "postingKey", "yearAndMonth", "headerText"};
                for (int i = 0; i < excelHead.length; i++) {
                    reader.addHeaderAlias(excelHead[i], excelHeadAlias[i]);
                }
                List<InputExportDetailEntity> dataList = reader.read(0, 1, InputExportDetailEntity.class);

                if(CollectionUtils.isEmpty(dataList)){
                    log.error("上传的{}Excel为空,请重新上传", filename);
                    throw new RRException("上传的Excel为空,请重新上传");
                }
                total += dataList.size();
                int count = 1;
                for (InputExportDetailEntity entity : dataList) {
                    count++;
                    // 参数校验
                    if(1 == checkExcel(entity)){
                        // 参数有误
                        fail += 1;
                        if(!StringUtils.contains(sb.toString(), filename)){
                            sb.append("文件" + filename + "的错误行号为:");
                        }
                        sb.append(count + ",");
                    }else {
                        // 数据库验重
                        InputExportDetailEntity duplicate = super.getOne(
                                new QueryWrapper<InputExportDetailEntity>()
                                        .eq("company_code", entity.getCompanyCode())
                                        .eq("document_no", entity.getDocumentNo())
                                        .eq("pstng_date", entity.getPstngDate())
                        );
                        if(null != duplicate){
                            duplicate.setAccount(entity.getAccount());
                            duplicate.setReference(entity.getReference());
                            duplicate.setDocumentType(entity.getDocumentType());
                            duplicate.setDocDate(entity.getDocDate());
                            duplicate.setAmountInLocal(entity.getAmountInLocal());
                            duplicate.setCurr(entity.getCurr());
                            duplicate.setAmountInLocal(entity.getAmountInLocal());
                            duplicate.setUserName(entity.getUserName());
                            duplicate.setAssignment(entity.getAssignment());
                            duplicate.setText(entity.getText());
                            duplicate.setTx(entity.getTx());
                            duplicate.setTradingPartner(entity.getTradingPartner());
                            duplicateList.add(this.paraphraseParams(duplicate));
                        }else {
                            entity.setCreateBy(ShiroUtils.getUserId().intValue());
                            entity.setCreateTime(new Date());
                            entityList.add(this.paraphraseParams(entity));
                        }
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
            log.error("导入进项转出明细出错: {}", e);
            throw new RRException("导入进项转出明细出现异常");
        }
    }

    /**
     * 校验Excel中参数
     */
    private int checkExcel(InputExportDetailEntity entity){
        // 非空校验
        if(StringUtils.isBlank(entity.getCompanyCode()) || StringUtils.isBlank(entity.getAccount()) ||
                StringUtils.isBlank(entity.getReference()) || StringUtils.isBlank(entity.getDocumentNo()) ||
                StringUtils.isBlank(entity.getDocumentType()) || StringUtils.isBlank(entity.getDocDate()) ||
                StringUtils.isBlank(entity.getPstngDate()) || null == entity.getAmountInLocal() ||
                StringUtils.isBlank(entity.getCurr()) || null == entity.getAmountInLocal() ||
                StringUtils.isBlank(entity.getText())){
            return 1;
        }

        return 0;
    }

    /**
     * 枚举值转义
     */
    private InputExportDetailEntity paraphraseParams(InputExportDetailEntity entity){
        String type = entity.getDocumentType();
        String currencyLocal = entity.getLcurr();
        String currency = entity.getCurr();
        String reference = entity.getReference();
        String text = entity.getText();
        // 对类型转义
        entity.setDocumentType("0");
        // 对当地币种转义
        if("CNY".equalsIgnoreCase(currencyLocal)){
            entity.setLcurr("0");
        }
        // 对币种转义
        if("CNY".equalsIgnoreCase(currency)){
            entity.setCurr("0");
        }
        // 对转出类型转义 0:红字转出; 1:海关免税转出; 2:福利转出; 3:其他转出
        if(StringUtils.contains(reference, "red invoice")){
            entity.setExportType("0");
        }else if(StringUtils.contains(reference, "exemption") || StringUtils.contains(text, "exemption")){
            entity.setExportType("1");
        }else if(StringUtils.contains(reference, "welfare")){
            entity.setExportType("2");
        }else if(StringUtils.contains(reference, "others")){
            entity.setExportType("3");
        }
        return entity;

    }
}
