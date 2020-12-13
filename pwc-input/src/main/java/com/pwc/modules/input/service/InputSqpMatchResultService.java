package com.pwc.modules.input.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.input.entity.InputSapMatchResultEntity;
import com.pwc.modules.input.entity.vo.InvoiceCustomsDifferenceMatch;
import com.pwc.modules.input.entity.vo.InvoiceDifferenceMatch;
import com.pwc.modules.input.entity.vo.RedInvoiceDifferenceMatch;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author myz
 * @email 
 * @date 2020-12-09 11:35:43
 */
public interface InputSqpMatchResultService extends IService<InputSapMatchResultEntity> {

    PageUtils queryPage(Map<String, Object> params);

    //实时查询账票匹配接口
    List<InputSapMatchResultEntity> queryMatchCurTime(Map<String, Object> params);

    Map<String,Object> getDifferenceMatchResult(Map<String, Object> params);

    Map<String,Object> getRedDifferenceMatchResult(Map<String, Object> params);

    Map<String,Object> getCustomsDifferenceMatchResult(Map<String, Object> params);

    List<InvoiceDifferenceMatch> getDifferenceMatchResultExcel(Map<String, Object> params);
/*    List<InvoiceCustomsDifferenceMatch> getDifferenceMatchResultExcel2(Map<String, Object> params);
    List<RedInvoiceDifferenceMatch> getDifferenceMatchResultExcel3(Map<String, Object> params);*/

    //根据ID查询账票匹配接口
    Collection<InputSapMatchResultEntity> getMatchResultByIds(Map<String, Object> params);

    //修改账票匹配信息
    boolean updateMatchResult(Map<String, Object> params);
}

