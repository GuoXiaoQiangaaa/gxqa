package com.pwc.modules.filing.controller;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Maps;
import com.pwc.common.utils.FilingConstants;
import com.pwc.common.utils.R;
import com.pwc.common.utils.StatusDefine;
import com.pwc.modules.filing.entity.FilingDistrictEntity;
import com.pwc.modules.filing.entity.FilingRecordEntity;
import com.pwc.modules.filing.service.FilingDistrictService;
import com.pwc.modules.filing.service.FilingNodeService;
import com.pwc.modules.filing.service.FilingRecordService;
import com.pwc.modules.sys.entity.SysDeptEntity;
import com.pwc.modules.sys.service.SysDeptService;
import com.pwc.modules.sys.service.SysFileService;
import com.pwc.modules.sys.shiro.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

/**
 * @author zk
 */
@RestController
@RequestMapping("/filing")
public class FilingController{

    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private FilingRecordService filingRecordService;
    @Autowired
    private FilingNodeService filingNodeService;
    @Autowired
    private SysFileService sysFileService;
    @Autowired
    private FilingDistrictService filingDistrictService;

    /**
     * 纵览页
     * @param deptId
     * @return
     */
    @GetMapping("/dashboard")
    public R dashboard(Long deptId, String globalDate){
        if (null == deptId) {
            deptId = ShiroUtils.getUserEntity().getDeptId();
        }
        Map<String, Object> result = Maps.newHashMap();
        SysDeptEntity deptEntity = sysDeptService.getById(deptId);
        if (null != deptEntity) {
            if(deptEntity.getType().equals(StatusDefine.DeptType.HEAD_OFFICE.getValue())) {
                List<Long> subDeptIdList = sysDeptService.queryDetpIdList(deptId);
//                subDeptIdList.add(deptId);
                //总申报数
                Integer totalCount = filingRecordService.countFilingByDeptIds(subDeptIdList, null, null, globalDate);
                totalCount = totalCount == 0 ? 1 : totalCount;
                //待上传
                Integer status = FilingConstants.ProcessNodeStatus.FINISHED.getValue();
                //子公司数
                result.put("branchNum", CollUtil.isNotEmpty(subDeptIdList) ? subDeptIdList.size() : 0);
                int uploadNum = filingRecordService.countFilingByDeptIds(subDeptIdList, "upload_status",status,globalDate);
                //上传状态
                result.put("uploadNum",uploadNum);
                //报告确认状态统计数量
                int confirmNum = filingRecordService.countFilingByDeptIds(subDeptIdList, "confirm_report_status", status,globalDate);
                result.put("confirmNum", confirmNum);
                //申报状态统计数量
                int declareNum = filingRecordService.countFilingByDeptIds(subDeptIdList, "declare_status", status,globalDate);
                result.put("declareNum", declareNum);
                //扣款状态统计数量
                int deductionNum = filingRecordService.countFilingByDeptIds(subDeptIdList, "deduction_status", status,globalDate);
                result.put("deductionNum", deductionNum);
                //节点时间
                result.put("filingNode", filingNodeService.queryByDeptAndCreateTime(deptId, globalDate));
                //申报上传百分比
                BigDecimal uploadPercent= new BigDecimal(uploadNum).divide(new BigDecimal(totalCount),2, RoundingMode.HALF_UP);
                result.put("uploadPercent", uploadPercent);
                //报告确认百分比
                BigDecimal confirmPercent= new BigDecimal(confirmNum).divide(new BigDecimal(totalCount), 2, RoundingMode.HALF_UP);
                result.put("confirmPercent", confirmPercent);
                //申报回执百分比
                BigDecimal declarePercent= new BigDecimal(declareNum).divide(new BigDecimal(totalCount), 2, RoundingMode.HALF_UP);
                result.put("declarePercent", declarePercent);
                //扣款百分比
                BigDecimal deductionPercent= new BigDecimal(deductionNum).divide(new BigDecimal(totalCount), 2, RoundingMode.HALF_UP);
                result.put("deductionPercent", deductionPercent);

            } else {
                //申报流程进度
                FilingRecordEntity filingRecord = filingRecordService.queryByDeptAndCreateTime(deptId, globalDate);
                if (null != filingRecord) {
                    List<FilingDistrictEntity> filingDistrictList = filingDistrictService.getByCityCode(deptEntity.getCityCode());
                    //是否支持第三方
                    if (CollUtil.isEmpty(filingDistrictList)) {
                        filingRecord.setThird(0);
                    } else {
                        filingRecord.setThird(1);
                    }
//                    filingRecord.setUploadFile(sysFileService.getById(filingRecord.getUploadAttachment()));
//                    filingRecord.setReportFile(sysFileService.getById(filingRecord.getComfirmReportAttachment()));
//                    filingRecord.setDeclareFile(sysFileService.getById(filingRecord.getDeclareAttachment()));
//                    filingRecord.setDeductionFile(sysFileService.getById(filingRecord.getDeductionAttachment()));
                }
                result.put("filingRecord", filingRecord);
                //申报节点
                result.put("filingNode", filingNodeService.queryByDeptAndCreateTime(deptId, globalDate));
                //分公司信息
                result.put("dept", deptEntity);
                //总公司信息
                result.put("parent", sysDeptService.getById(deptEntity.getParentId()));
            }
        }
        return R.ok().put("data", result);
    }
}
