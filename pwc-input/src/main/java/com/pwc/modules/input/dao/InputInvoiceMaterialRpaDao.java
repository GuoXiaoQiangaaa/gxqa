package com.pwc.modules.input.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.modules.input.entity.InputInvoiceMaterial;
import com.pwc.modules.input.entity.InputInvoiceMaterialRpaEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 *
 * @author QIU
 * @email sunlightcs@gmail.com
 * @date 2018-12-13 09:31:08
 */
@Mapper
public interface InputInvoiceMaterialRpaDao extends BaseMapper<InputInvoiceMaterialRpaEntity> {
    InputInvoiceMaterial get(int id);
    List<InputInvoiceMaterial> getListByStatus();

    /**
     * 更新状态为0
     * @param list
     */
    void updateByStatus(@Param("list") List<InputInvoiceMaterial> list);
    InputInvoiceMaterial getOneByBatchId(InputInvoiceMaterial invoiceMaterial);
    int insert(InputInvoiceMaterial invoiceMaterial);
    void deleteByBatchId(InputInvoiceMaterial invoiceMaterial);
}
