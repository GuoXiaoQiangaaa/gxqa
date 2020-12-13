package com.pwc.modules.input.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.modules.input.entity.InputSapInvoiceMappingIdEntity;
import com.pwc.modules.input.entity.InputSapMatchResultEntity;
import com.pwc.modules.input.entity.vo.InvoiceCustomsDifferenceMatch;
import com.pwc.modules.input.entity.vo.InvoiceDifferenceMatch;
import com.pwc.modules.input.entity.vo.RedInvoiceDifferenceMatch;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 * 
 * @author myz
 * @email 
 * @date 2020-12-09 11:35:43
 */
@Mapper
public interface InputSqpMatchResultDao extends BaseMapper<InputSapMatchResultEntity> {

    List<InvoiceDifferenceMatch> getDifferenceMatchInvoice(@Param("offset") int offset, @Param("limit") int limit,@Param("deptId")String deptId, @Param("yearAndMonth")String yearAndMonth);
    List<RedInvoiceDifferenceMatch> getRedDifferenceMatchInvoice(@Param("offset") int offset, @Param("limit") int limit, @Param("deptId")String deptId, @Param("yearAndMonth")String yearAndMonth);
    List<InvoiceCustomsDifferenceMatch> getCustomsDifferenceMatchResult(@Param("offset") int offset, @Param("limit") int limit, @Param("deptId")String deptId, @Param("yearAndMonth")String yearAndMonth);
    int getDifferenceMatchInvoiceCount(@Param("offset") int offset, @Param("limit") int limit,@Param("deptId")String deptId, @Param("yearAndMonth")String yearAndMonth);
    int getRedDifferenceMatchInvoiceCount(@Param("offset") int offset, @Param("limit") int limit,@Param("deptId")String deptId, @Param("yearAndMonth")String yearAndMonth);
    int getCustomsDifferenceMatchResultCount(@Param("offset") int offset, @Param("limit") int limit,@Param("deptId")String deptId, @Param("yearAndMonth")String yearAndMonth);
    List<InvoiceDifferenceMatch> getDifferenceMatchInvoiceExcel(@Param("deptId")String deptId, @Param("yearAndMonth")String yearAndMonth);
    List<RedInvoiceDifferenceMatch> getRedDifferenceMatchInvoiceExcel(@Param("deptId")String deptId, @Param("yearAndMonth")String yearAndMonth);
    List<InvoiceCustomsDifferenceMatch> getCustomsDifferenceMatchResultExcel(@Param("deptId")String deptId, @Param("yearAndMonth")String yearAndMonth);


}
