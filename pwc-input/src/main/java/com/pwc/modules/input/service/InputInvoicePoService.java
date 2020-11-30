package com.pwc.modules.input.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.input.entity.InputInvoicePoEntity;

import java.util.List;
import java.util.Map;

/**
 * @author: Gxw
 * @create: 2020-09-02 18:43
 **/
public interface InputInvoicePoService extends IService<InputInvoicePoEntity> {
    /**
     * 查询
     * @param params
     * @return
     */
    PageUtils getPoEntity(Map<String, Object> params);

    /**
     * 更新数据
     * @param poEntity
     * @return
     */
    InputInvoicePoEntity uploadPo(InputInvoicePoEntity  poEntity);

    /**
     * 获取所有数据
     * @return
     */
    int getListByShow();

    /**
     * 根据发票号码
     * @param invoiceNumber
     * @return
     */
    List<InputInvoicePoEntity> getListByNumber(String invoiceNumber);

    /**
     * 根据关联上传id查询
     * @param uploadId
     * @return
     */
    InputInvoicePoEntity findByuploadId(String uploadId);

    /**
     * 根据PO号码查询一条数据
     * @param poNumber
     * @return
     */
    List<InputInvoicePoEntity> getByPoNumber(String poNumber);
}
