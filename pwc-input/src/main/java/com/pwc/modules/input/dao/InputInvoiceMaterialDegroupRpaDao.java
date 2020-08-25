package com.pwc.modules.input.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.modules.input.entity.InputInvoiceMaterialDegroupRpaEntity;
import com.pwc.modules.input.entity.InputInvoiceMaterialInvoicegroupRpa;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 *
 *
 * @author QIU
 * @email sunlightcs@gmail.com
 * @date 2018-12-13 09:31:08
 */
@Mapper
public interface InputInvoiceMaterialDegroupRpaDao extends BaseMapper<InputInvoiceMaterialDegroupRpaEntity> {
    InputInvoiceMaterialDegroupRpaEntity get(InputInvoiceMaterialDegroupRpaEntity degroupRpaEntity);

    /**
     * 根据公司Id获取集合
     * @return
     */
    List<InputInvoiceMaterialDegroupRpaEntity> getListByCompanyId(int companyId);

    /**
     * 获取发票号（日期最新的那张发票表）下的集合
     * @param degroupRpaId
     * @return
     */
    List<InputInvoiceMaterialInvoicegroupRpa> getByDegroupRpaId(int degroupRpaId);
    int save(InputInvoiceMaterialDegroupRpaDao invoiceMaterialDegroupRpaEntity);
    int saveInvoiceMaterialInvoicegroupRpa(InputInvoiceMaterialInvoicegroupRpa invoiceMaterialInvoicegroupRpa);

    void insertInvoiceGroupRpa(InputInvoiceMaterialInvoicegroupRpa invoiceMaterialInvoicegroupRpa);

}
