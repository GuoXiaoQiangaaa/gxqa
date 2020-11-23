package com.pwc.modules.input.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.modules.input.entity.InputInvoiceCustomsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 海关缴款书（同步）
 * 
 * @author zlb
 * @email 
 * @date 2020-08-10 18:53:50
 */
@Mapper
public interface InputInvoiceCustomsDao extends BaseMapper<InputInvoiceCustomsEntity> {

    int getListByShow();

    /**
     * 作废(退票)
     */
    void updateByIdReturn(@Param("ids") List<Long> ids, @Param("returnReason") String returnReason,
                          @Param("updateBy") Integer updateBy, @Param("updateTime") Date updateTime);

    String getCountByVoucherCode(@Param("voucherCode") String voucherCode );

    void updateByentryState(@Param("entryState")String entryState,@Param("voucherCode") String voucherCode);

    int  updateByVoucherCode(@Param("voucherCode")String voucherCode,@Param("list") List<String> list);

}
