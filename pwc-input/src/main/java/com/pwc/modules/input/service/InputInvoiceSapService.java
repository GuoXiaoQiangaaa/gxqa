package com.pwc.modules.input.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.input.entity.InputInvoiceSapEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author: Gxw
 * @create: 2020-09-23 15:07
 **/
public interface InputInvoiceSapService  extends IService<InputInvoiceSapEntity> {
    Map<String, Object> getImportBySap(MultipartFile file) throws Exception;

    /**
     * 查询列表页面
     * @param params
     * @return
     */
    PageUtils getListBySap(Map<String, Object> params);

    /**
     * 根据凭证号查询数据
     * @param documentNo
     * @return
     */
    InputInvoiceSapEntity getEntityByNo(String documentNo);

    /**
     * 根据日期 和类型查询入账数据
     * @param date
     * @param status
     * @return
     */
    List<InputInvoiceSapEntity> getEntityByDateAndStatus(String date, String status);
}
