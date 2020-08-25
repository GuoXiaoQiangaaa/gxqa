package com.pwc.modules.input.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.modules.input.entity.InputInvoiceEntity;
import com.pwc.modules.input.entity.InputMaterialDocumentEntity;
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
public interface InputMaterialDocumentDao extends BaseMapper<InputMaterialDocumentEntity> {
//    List<InputInvoiceEntity> findList(Pagination page, @Param("invoice") InputInvoiceEntity invoice, @Param("key") String key);
    InputMaterialDocumentEntity get(InputMaterialDocumentEntity materialDocumentEntity);
    List<InputMaterialDocumentEntity> getList();
    void save(InputMaterialDocumentEntity materialDocumentEntity);
    void ruleDelete(Integer[] ids);
    /**
     * 根据发票Id获取凭证
     * @param invoiceEntity
     * @return
     */
    List<InputMaterialDocumentEntity> getListByInvoiceId(InputInvoiceEntity invoiceEntity);
}
