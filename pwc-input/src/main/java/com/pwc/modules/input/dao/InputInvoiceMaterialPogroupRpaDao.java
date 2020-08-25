package com.pwc.modules.input.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.modules.input.entity.InputInvoiceMaterialPogroupRpaEntity;
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
public interface InputInvoiceMaterialPogroupRpaDao extends BaseMapper<InputInvoiceMaterialPogroupRpaEntity> {
    List<InputInvoiceMaterialPogroupRpaEntity> getList(int invoiceInvoicegroupId);

}
