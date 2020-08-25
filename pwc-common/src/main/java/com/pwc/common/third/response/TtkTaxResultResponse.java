package com.pwc.common.third.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author zk
 */
@Data
@ToString
public class TtkTaxResultResponse {

    @JSONField(name = "taxAppraisalDtoList")
    private List<TtkTaxAppraisalResponse> taxAppraisalDtoList;
}
