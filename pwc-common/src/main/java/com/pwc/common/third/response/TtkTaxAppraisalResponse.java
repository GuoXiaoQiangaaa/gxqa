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
public class TtkTaxAppraisalResponse {

    private String endDate;
    private String payStateName;
    private Integer projectType;
    private String cycleId;
    @JSONField(name = "selfSystemDeclare")
    private boolean selfSystemDeclare;
    private Integer payState;
    private String taxProjectName;
    private String stateName;
    private Long id;
    private Integer state;
    @JSONField(name = "connect")
    private boolean connect;
    @JSONField(name = "sbywbm")
    private String sbywbm;
    private String toDate;
    @JSONField(name = "reportEnclosures")
    private List<String> reportEnclosures;
    private String fromDate;
    @JSONField(name = "yzpzzlDm")
    private String yzpzzlDm;
    @JSONField(name = "nodeEnclosureses")
    private List<String> nodeEnclosureses;
    private String taxProjectCode;
    private String ts;

}

