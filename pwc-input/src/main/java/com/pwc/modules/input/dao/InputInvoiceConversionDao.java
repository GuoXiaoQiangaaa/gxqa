package com.pwc.modules.input.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.modules.input.entity.InputInvoiceConversionEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 *
 *
 * @author qiuyin
 * @email sunlightcs@gmail.com
 * @date 2018-12-13 09:31:08
 */
@Mapper
public interface InputInvoiceConversionDao extends BaseMapper<InputInvoiceConversionEntity> {
//    List<InputInvoiceConversionEntity> findList(Pagination page, @Param("invoiceConversion") InputInvoiceConversionEntity invoiceConversion, @Param("key") String key);
    InputInvoiceConversionEntity get(InputInvoiceConversionEntity invoiceConversionEntity);
    List<InputInvoiceConversionEntity> getList();
    void save(InputInvoiceConversionEntity invoiceConversionEntity);
    void conversionDelete(Integer[] ids);
}
