package com.pwc.modules.input.service.impl;

import com.pwc.common.exception.RRException;
import com.pwc.common.utils.*;
import com.pwc.common.utils.excel.ImportExcel;
import com.pwc.modules.input.entity.InputSapConfEntity;
import com.pwc.modules.input.service.InputSapConfEntityService;
import com.pwc.modules.sys.shiro.ShiroUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.pwc.modules.input.dao.InputRedInvoiceDao;
import com.pwc.modules.input.entity.InputRedInvoiceEntity;
import com.pwc.modules.input.service.InputRedInvoiceService;
import org.springframework.web.multipart.MultipartFile;

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
    private HttpUploadFile httpUploadFile;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<InputRedInvoiceEntity> page = this.page(
                new Query<InputRedInvoiceEntity>().getPage(params),
                new QueryWrapper<InputRedInvoiceEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 条件查询
     *
     * @param params 分页参数
     * @param redInvoiceEntity 条件参数
     * @return 查询结果
     */
    @Override
    public PageUtils conditionList(Map<String, Object> params, InputRedInvoiceEntity redInvoiceEntity) {
        IPage<InputRedInvoiceEntity> page = this.page(
                new Query<InputRedInvoiceEntity>().getPage(params, null, true),
                new QueryWrapper<InputRedInvoiceEntity>()
                        .orderByDesc("red_id")
                        .like(StringUtils.isNotBlank(redInvoiceEntity.getPurchaserCompany()), "purchaser_company", redInvoiceEntity.getPurchaserCompany())
                        .eq(StringUtils.isNotBlank(redInvoiceEntity.getRedNoticeNumber()), "red_notice_number", redInvoiceEntity.getRedNoticeNumber())
                        .eq(StringUtils.isNotBlank(redInvoiceEntity.getBlueInvoiceNumber()), "blue_invoice_number", redInvoiceEntity.getBlueInvoiceNumber())
                        .eq(StringUtils.isNotBlank(redInvoiceEntity.getRedStatus()), "red_status", !StringUtils.isNotBlank(redInvoiceEntity.getRedStatus()) ? "0" : redInvoiceEntity.getRedStatus())
        );
        return new PageUtils(page);
    }

    /**
     * 导入红字通知单
     *
     * @param file 待导入文件
     */
    @Override
    public void importRedNotice(MultipartFile file) {
        try {
            ImportExcel ie = new ImportExcel(file, 1, 0);
            List<InputRedInvoiceEntity> redInvoiceEntityList = ie.getDataList(InputRedInvoiceEntity.class);
            Long userId = ShiroUtils.getUserId();
            Date importDate = new Date();
            redInvoiceEntityList.forEach(
                    redInvoiceEntity -> {
                        // 新导入的红字通知单状态为: 红字通知单开具
                        redInvoiceEntity.setRedStatus("0");
                        redInvoiceEntity.setCreateBy(String.valueOf(userId));
                        redInvoiceEntity.setCreateTime(importDate);
                    }
            );
            super.saveBatch(redInvoiceEntityList);
        } catch (Exception e) {
            log.error("导入红字通知单出错: {}", e);
        }
    }


    /**
     * 导入红字发票并更新红字通知单状态
     *
     * @param file 待导入红字发票
     */
    @Override
    public void importRedInvoice(MultipartFile file) {
        // 获取文件类型
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        // 文件后缀校验
        if(!("jpg".equalsIgnoreCase(suffix) || "jpeg".equalsIgnoreCase(suffix) || "png".equalsIgnoreCase(suffix))){
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

        } catch (Exception e) {
            log.error("导入红字发票出错: {}", e);
            throw new RRException("导入红字发票发生异常");
        }

    }
}
