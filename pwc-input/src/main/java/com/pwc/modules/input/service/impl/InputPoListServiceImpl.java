package com.pwc.modules.input.service.impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.modules.input.dao.InputPoListDao;
import com.pwc.modules.input.entity.InputPoListEntity;
import com.pwc.modules.input.service.InputPoListService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * @description: polist数据
 * @author: Gxw
 * @create: 2020-09-11 17:37
 **/
@Service("inputPoListService")
public class InputPoListServiceImpl extends ServiceImpl<InputPoListDao, InputPoListEntity> implements InputPoListService {
    @Override
    public List<String> receiveInvoice(MultipartFile file) throws Exception {
        ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
        String[] excelHead = {"Company code", "Vendor Name", "Purchasing Document",
                "PO Item", "Material No","Short Text","Qty ordered ","Net Price",
                "P.O. Create Date ", "Vendor No"};
        String [] excelHeadAlias = {"companyCode", "vendorName", "purchasingDocument",
                "poItem", "materialNo", "shortText", "qtyOrdered","netPrice",
                "createDate","vendorNo"};
        for (int i = 0; i < excelHead.length; i++) {
            reader.addHeaderAlias(excelHead[i], excelHeadAlias[i]);
        }
        List<InputPoListEntity> dataList = reader.read(0, 1, InputPoListEntity.class);
        for(InputPoListEntity poListEntity:dataList){
            this.save(poListEntity);
        }
        return null;
    }

    @Override
    public InputPoListEntity getPoListByPoItem(String poItem){
        return this.getOne(
                new QueryWrapper<InputPoListEntity>()
                        .eq("po_item",poItem)
        );
    }
    @Override
    public InputPoListEntity getPoListByCode(String companyCode){
        return this.getOne(
                new QueryWrapper<InputPoListEntity>()
                        .eq("company_code",companyCode)
        );
    }
}
