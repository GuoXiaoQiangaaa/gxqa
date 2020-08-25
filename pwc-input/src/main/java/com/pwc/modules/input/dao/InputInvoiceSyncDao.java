package com.pwc.modules.input.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fapiao.neon.model.in.StatisticsDetailsInfo;
import com.fapiao.neon.model.in.StatisticsInvoiceInfo;
import com.pwc.modules.input.entity.InputCompanyEntity;
import com.pwc.modules.input.entity.InputInvoiceSyncEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 *
 * @author zk
 * @email
 * @date 2020-01-19 18:27:48
 */
@Mapper
public interface InputInvoiceSyncDao extends BaseMapper<InputInvoiceSyncEntity> {

    void insertDetailsBatch(@Param("companyEntity") InputCompanyEntity companyEntity, @Param("details") List<StatisticsDetailsInfo> details);

    void insertDifferenceBatch(@Param("companyEntity")InputCompanyEntity companyEntity, @Param("differenceInvoices")List<StatisticsInvoiceInfo> differenceInvoices);

    void insertInvoicesBatch(@Param("companyEntity")InputCompanyEntity companyEntity,@Param("invoices") List<StatisticsInvoiceInfo> invoices);

    void deleteDetailsByCompanyId(Integer id);
    void deleteDifferenceByCompanyId(Integer id);

    void deleteInvoicesByCompanyId(Integer id);
}
