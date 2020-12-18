package com.pwc.modules.input.controller;

import com.pwc.common.annotation.SysLog;
import com.pwc.common.utils.R;
import com.pwc.modules.input.entity.InputCompanyEntity;
import com.pwc.modules.input.entity.InputCompanyTaskResult;
import com.pwc.modules.input.entity.InputInvoiceEntity;
import com.pwc.modules.input.service.InputCompanyService;
import com.pwc.modules.input.service.InputCompanyTaskResultService;
import com.pwc.modules.input.service.InputInvoiceService;
import com.pwc.modules.sys.entity.SysDeptEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.lang.model.util.ElementScanner6;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("input/authebtucation")
public class AuthebtucationStatisticsController {

    @Autowired
    private InputInvoiceService invoiceService;
    @Autowired
    private InputCompanyService companyService;
    @Autowired
    private InputCompanyTaskResultService companyTaskResultService;

    /**
     * 获取撤销结果
     * @param companyEntity
     * @return
     */
    @SysLog("获取撤销结果")
    @RequestMapping("/cancel3")
    public R cancel3(InputCompanyEntity companyEntity) {
        companyEntity = companyService.getById(companyEntity.getId());
        R r2 = invoiceService.getApplyResule(companyEntity.getCompanyDutyParagraph(),"2",companyEntity.getApplyTaskno());
        String taskStatus = (String)r2.get("taskStatus");
        // 撤销操作失败，返回前台
        if(((Integer)r2.get("code")) == 555) {
            return r2;
        } else {
            // 撤销操作成功获取业务执行状态
            String businessStatus = (String)r2.get("businessStatus");
            companyEntity.setApplyResult(businessStatus);
            // 状态为1，则发起统计申请
            if(taskStatus.equals("1")) {
                companyEntity.setStatus("0");
                companyEntity.setApplyResult("0");
                companyEntity.setCensusResult("0");
                companyEntity.setStatisticsMonth("");
                companyEntity.setStatisticsTime("");
                companyService.update(companyEntity);
                companyTaskResultService.delByCompanyId(companyEntity.getId());
                InputInvoiceEntity invoiceEntity = new InputInvoiceEntity();
                invoiceEntity.setApplyStatus("1");
                invoiceEntity.setCompanyId(companyEntity.getId());
                invoiceEntity.setDeptId(companyEntity.getDeptId().intValue());
                invoiceService.updateApply(invoiceEntity);
                return R.ok();
            } else {
                companyEntity.setStatus("0");
                companyEntity.setApplyResult("99");
                companyEntity.setCensusResult("0");
                companyEntity.setStatisticsMonth("");
                companyEntity.setStatisticsTime("");
                if(taskStatus.equals("2")) {
                    taskStatus = "任务处理中";
                } else if(taskStatus.equals("0")) {
                    taskStatus = "任务接收成功,待处理";
                }
                companyService.update(companyEntity);
                return new R().error().put("msg",taskStatus);
            }
        }
    }


    /**
     * 撤销统计申请
     * @param companyEntity
     * @return
     */
    @SysLog("撤销统计申请")
    @RequestMapping("/cancel2")
    public R cancel2(InputCompanyEntity companyEntity) {
        companyEntity = companyService.getById(companyEntity.getId());
        // 申请撤销
        R r = invoiceService.getApply(companyEntity.getCompanyDutyParagraph(),"2");
        if((Integer)r.get("code") != 0) {
            return r;
        }
        // 根据撤销任务编码获取撤销结果
        R r2 = invoiceService.getApplyResule(companyEntity.getCompanyDutyParagraph(),"2",(String)r.get("taskNo"));
        String taskStatus = (String)r2.get("taskStatus");
        // 撤销操作失败，返回前台
        if(((Integer)r2.get("code")) == 555) {
            return r2;
        } else {
            // 撤销操作成功获取业务执行状态
            String businessStatus = (String)r2.get("businessStatus");
            companyEntity.setApplyResult(businessStatus);
            // 状态为1，则发起统计申请
            if(taskStatus.equals("1")) {
                companyEntity.setStatus("0");
                companyEntity.setApplyResult("0");
                companyEntity.setCensusResult("0");
                companyEntity.setStatisticsMonth("");
                companyEntity.setStatisticsTime("");
                companyService.update(companyEntity);
                companyTaskResultService.delByCompanyId(companyEntity.getId());
                InputInvoiceEntity invoiceEntity = new InputInvoiceEntity();
                invoiceEntity.setApplyStatus("1");
                invoiceEntity.setCompanyId(companyEntity.getId());
                invoiceEntity.setDeptId(companyEntity.getDeptId().intValue());
                invoiceService.updateApply(invoiceEntity);
                return R.ok();
            } else {
                companyEntity.setStatus("0");
                companyEntity.setApplyResult("99");
                companyEntity.setCensusResult("0");
                companyEntity.setStatisticsMonth("");
                companyEntity.setStatisticsTime("");
                companyEntity.setApplyTaskno((String)r.get("taskNo"));
                if(taskStatus.equals("2")) {
                    taskStatus = "任务处理中";
                } else if(taskStatus.equals("0")) {
                    taskStatus = "任务接收成功,待处理";
                }
                companyService.update(companyEntity);
                return new R().error().put("msg",taskStatus);
            }
        }
    }


    /**
     * 撤销统计申请
     * @param companyEntity
     * @return
     */
    @SysLog("撤销统计申请")
    @RequestMapping("/cancel")
    public R cancel(InputCompanyEntity companyEntity) {
        companyEntity = companyService.getById(companyEntity.getId());
        // 申请撤销
        R r = invoiceService.getApply(companyEntity.getCompanyDutyParagraph(),"2");
        if((Integer)r.get("code") != 0) {
            return r;
        }
        // 根据撤销任务编码获取撤销结果
        R r2 = invoiceService.getApplyResule(companyEntity.getCompanyDutyParagraph(),"2",(String)r.get("taskNo"));
        // 撤销操作失败，返回前台
        if(((Integer)r2.get("code")) == 555) {
            return r2;
        } else {
            // 撤销操作成功获取业务执行状态
            String businessStatus = (String)r2.get("businessStatus");
            String taskStatus = (String)r2.get("taskStatus");
            companyEntity.setApplyResult(businessStatus);
            // 状态为1，则发起统计申请
            if(taskStatus.equals("1")) {
                companyEntity.setStatus("0");
                companyEntity.setApplyResult("0");
                companyEntity.setCensusResult("0");
                companyEntity.setStatisticsMonth("");
                companyEntity.setStatisticsTime("");
                companyService.update(companyEntity);
                companyTaskResultService.delByCompanyId(companyEntity.getId());
                InputInvoiceEntity invoiceEntity = new InputInvoiceEntity();
                invoiceEntity.setApplyStatus("1");
                invoiceEntity.setCompanyId(companyEntity.getId());
                invoiceEntity.setDeptId(companyEntity.getDeptId().intValue());
                invoiceService.updateApply(invoiceEntity);
                return apply(companyEntity);
            } else {
                companyEntity.setStatus("0");
                companyEntity.setApplyResult("99");
                companyEntity.setCensusResult("0");
                companyEntity.setStatisticsMonth("");
                companyEntity.setStatisticsTime("");
                companyEntity.setApplyTaskno((String)r.get("taskNo"));
                if(taskStatus.equals("2")) {
                    taskStatus = "任务处理中";
                } else if(taskStatus.equals("0")) {
                    taskStatus = "任务接收成功,待处理";
                }
                companyService.update(companyEntity);
                return new R().error().put("msg",taskStatus);
            }
        }
    }

    /**
     * 重新获取申请结果
     * @param companyEntity
     * @return
     */
    @SysLog("重新获取申请结果")
    @RequestMapping("/apply2")
    public R apply2(InputCompanyEntity companyEntity) {
        companyEntity = companyService.getById(companyEntity.getId());
        R r2 = invoiceService.getApplyResule(companyEntity.getCompanyDutyParagraph(),"1",companyEntity.getApplyTaskno());
        if(((Integer)r2.get("code")) == 555) {
            return r2;
        } else {
            // 获取返回结果
            String businessStatus = (String)r2.get("businessStatus");
            companyEntity.setApplyResult(businessStatus);
            if(businessStatus.equals("1") || businessStatus.equals("7")) {
                Map<String,Object> businessData = (Map<String, Object>) r2.get("businessData");
                String statisticsTime = (String)businessData.get("statisticsTime");
                String statisticsMonth = (String)businessData.get("statisticsMonth");
                // 正常统计
                List<Map<String,String>> normalList = (List<Map<String, String>>) businessData.get("normal");
                if(!normalList.isEmpty()) {
                    for(int i = 0; i < normalList.size(); i++) {
                        InputCompanyTaskResult companyTaskResult = new InputCompanyTaskResult();
                        companyTaskResult.setType("0");
                        companyTaskResult.setCompanyId(companyEntity.getId());
                        if(StringUtils.isNoneBlank(normalList.get(i).get("invoiceType"))) {
                            companyTaskResult.setInvoiceType(normalList.get(i).get("invoiceType"));
                        }
                        if(StringUtils.isNoneBlank(normalList.get(i).get("deductibleNum"))) {
                            companyTaskResult.setDeductibleNum(normalList.get(i).get("deductibleNum"));
                        }
                        if(StringUtils.isNoneBlank(normalList.get(i).get("deductibleAmount"))) {
                            companyTaskResult.setDeductibleAmount(normalList.get(i).get("deductibleAmount"));
                        }
                        if(StringUtils.isNoneBlank(normalList.get(i).get("deductibleTax"))) {
                            companyTaskResult.setDeductibleTax(normalList.get(i).get("deductibleTax"));
                        }
                        if(StringUtils.isNoneBlank(normalList.get(i).get("unDeductibleNum"))) {
                            companyTaskResult.setUnDeductibleNum(normalList.get(i).get("unDeductibleNum"));
                        }
                        if(StringUtils.isNoneBlank(normalList.get(i).get("unDeductibleAmount"))) {
                            companyTaskResult.setUnDeductibleAmount(normalList.get(i).get("unDeductibleAmount"));
                        }
                        if(StringUtils.isNoneBlank(normalList.get(i).get("unDeductibleTax"))) {
                            companyTaskResult.setUnDeductibleTax(normalList.get(i).get("unDeductibleTax"));
                        }
                        // 新增
                        companyTaskResultService.save(companyTaskResult);
                    }
                }
                // 异常统计
                List<Map<String,String>> abnormalList = ( List<Map<String,String>>) businessData.get("abnormal");
                if(!abnormalList.isEmpty()) {
                    for(int i = 0; i < abnormalList.size(); i++) {
                        InputCompanyTaskResult companyTaskResult = new InputCompanyTaskResult();
                        companyTaskResult.setType("1");
                        companyTaskResult.setCompanyId(companyEntity.getId());
                        if(StringUtils.isNoneBlank(abnormalList.get(i).get("invoiceType"))) {
                            companyTaskResult.setInvoiceType(abnormalList.get(i).get("invoiceType"));
                        }
                        if(StringUtils.isNoneBlank(abnormalList.get(i).get("deductibleNum"))) {
                            companyTaskResult.setDeductibleNum(abnormalList.get(i).get("deductibleNum"));
                        }
                        if(StringUtils.isNoneBlank(abnormalList.get(i).get("deductibleAmount"))) {
                            companyTaskResult.setDeductibleAmount(abnormalList.get(i).get("deductibleAmount"));
                        }
                        if(StringUtils.isNoneBlank(abnormalList.get(i).get("deductibleTax"))) {
                            companyTaskResult.setDeductibleTax(abnormalList.get(i).get("deductibleTax"));
                        }
                        if(StringUtils.isNoneBlank(abnormalList.get(i).get("unDeductibleNum"))) {
                            companyTaskResult.setUnDeductibleNum(abnormalList.get(i).get("unDeductibleNum"));
                        }
                        if(StringUtils.isNoneBlank(abnormalList.get(i).get("unDeductibleAmount"))) {
                            companyTaskResult.setUnDeductibleAmount(abnormalList.get(i).get("unDeductibleAmount"));
                        }
                        if(StringUtils.isNoneBlank(abnormalList.get(i).get("unDeductibleTax"))) {
                            companyTaskResult.setUnDeductibleTax(abnormalList.get(i).get("unDeductibleTax"));
                        }
                        // 新增
                        companyTaskResultService.save(companyTaskResult);
                    }
                }
                companyEntity.setStatisticsTime(statisticsTime);
                companyEntity.setStatisticsMonth(statisticsMonth);
                companyEntity.setStatus("1");
                companyService.update(companyEntity);
                return R.ok();
            } else {
                companyEntity.setStatus("1");
                companyService.update(companyEntity);
                return R.error().put("msg",businessStatus);
            }
        }
    }

    /**
     * 发起统计申请并获得申请结果
     * @param companyEntity
     * @return
     */
    @SysLog("发起统计申请")
    @RequestMapping("/apply")
    public R apply(InputCompanyEntity companyEntity) {
        companyEntity = companyService.getById(companyEntity.getId());
        // 发起统计申请
        R r = invoiceService.getApply(companyEntity.getCompanyDutyParagraph(),"1");
        if((Integer)r.get("code") != 0) {
            return r;
        }
        companyEntity.setApplyTaskno((String)r.get("taskNo"));
        companyEntity.setStatus("1");
        companyService.update(companyEntity);
        if(((String)r.get("msg")).contains("存在未处理完的任务")) {
            return R.error().put("msg","已提交统计申请，请10分钟后获取统计结果");
        }
        companyEntity.setApplyTaskno((String)r.get("taskNo"));
        companyService.update(companyEntity);
        // 根据申请任务编码获取统计结果
        R r2 = invoiceService.getApplyResule(companyEntity.getCompanyDutyParagraph(),"1",(String)r.get("taskNo"));
        if(((Integer)r2.get("code")) == 555) {
            return r2;
        } else {
            // 获取返回结果
            String businessStatus = (String)r2.get("businessStatus");
            companyEntity.setApplyResult(businessStatus);
            if(businessStatus.equals("1") || businessStatus.equals("7")) {
                Map<String,Object> businessData = (Map<String, Object>) r2.get("businessData");
                String statisticsTime = (String)businessData.get("statisticsTime");
                String statisticsMonth = (String)businessData.get("statisticsMonth");
                // 正常统计
                List<Map<String,String>> normalList = (List<Map<String, String>>) businessData.get("normal");
                if(!normalList.isEmpty()) {
                    for(int i = 0; i < normalList.size(); i++) {
                        InputCompanyTaskResult companyTaskResult = new InputCompanyTaskResult();
                        companyTaskResult.setType("0");
                        companyTaskResult.setCompanyId(companyEntity.getId());
                        if(StringUtils.isNoneBlank(normalList.get(i).get("invoiceType"))) {
                            companyTaskResult.setInvoiceType(normalList.get(i).get("invoiceType"));
                        }
                        if(StringUtils.isNoneBlank(normalList.get(i).get("deductibleNum"))) {
                            companyTaskResult.setDeductibleNum(normalList.get(i).get("deductibleNum"));
                        }
                        if(StringUtils.isNoneBlank(normalList.get(i).get("deductibleAmount"))) {
                            companyTaskResult.setDeductibleAmount(normalList.get(i).get("deductibleAmount"));
                        }
                        if(StringUtils.isNoneBlank(normalList.get(i).get("deductibleTax"))) {
                            companyTaskResult.setDeductibleTax(normalList.get(i).get("deductibleTax"));
                        }
                        if(StringUtils.isNoneBlank(normalList.get(i).get("unDeductibleNum"))) {
                            companyTaskResult.setUnDeductibleNum(normalList.get(i).get("unDeductibleNum"));
                        }
                        if(StringUtils.isNoneBlank(normalList.get(i).get("unDeductibleAmount"))) {
                            companyTaskResult.setUnDeductibleAmount(normalList.get(i).get("unDeductibleAmount"));
                        }
                        if(StringUtils.isNoneBlank(normalList.get(i).get("unDeductibleTax"))) {
                            companyTaskResult.setUnDeductibleTax(normalList.get(i).get("unDeductibleTax"));
                        }
                        // 新增
                        companyTaskResultService.save(companyTaskResult);
                    }
                }
                // 异常统计
                List<Map<String,String>> abnormalList = ( List<Map<String,String>>) businessData.get("abnormal");
                if(!abnormalList.isEmpty()) {
                    for(int i = 0; i < abnormalList.size(); i++) {
                        InputCompanyTaskResult companyTaskResult = new InputCompanyTaskResult();
                        companyTaskResult.setType("1");
                        companyTaskResult.setCompanyId(companyEntity.getId());
                        if(StringUtils.isNoneBlank(abnormalList.get(i).get("invoiceType"))) {
                            companyTaskResult.setInvoiceType(abnormalList.get(i).get("invoiceType"));
                        }
                        if(StringUtils.isNoneBlank(abnormalList.get(i).get("deductibleNum"))) {
                            companyTaskResult.setDeductibleNum(abnormalList.get(i).get("deductibleNum"));
                        }
                        if(StringUtils.isNoneBlank(abnormalList.get(i).get("deductibleAmount"))) {
                            companyTaskResult.setDeductibleAmount(abnormalList.get(i).get("deductibleAmount"));
                        }
                        if(StringUtils.isNoneBlank(abnormalList.get(i).get("deductibleTax"))) {
                            companyTaskResult.setDeductibleTax(abnormalList.get(i).get("deductibleTax"));
                        }
                        if(StringUtils.isNoneBlank(abnormalList.get(i).get("unDeductibleNum"))) {
                            companyTaskResult.setUnDeductibleNum(abnormalList.get(i).get("unDeductibleNum"));
                        }
                        if(StringUtils.isNoneBlank(abnormalList.get(i).get("unDeductibleAmount"))) {
                            companyTaskResult.setUnDeductibleAmount(abnormalList.get(i).get("unDeductibleAmount"));
                        }
                        if(StringUtils.isNoneBlank(abnormalList.get(i).get("unDeductibleTax"))) {
                            companyTaskResult.setUnDeductibleTax(abnormalList.get(i).get("unDeductibleTax"));
                        }
                        // 新增
                        companyTaskResultService.save(companyTaskResult);
                    }
                }
                companyEntity.setStatisticsTime(statisticsTime);
                companyEntity.setStatisticsMonth(statisticsMonth);
                companyEntity.setStatus("1");
                companyService.update(companyEntity);
                return R.ok();
            } else {
                companyEntity.setStatus("1");
                companyService.update(companyEntity);
                return R.error().put("msg",businessStatus);
            }
        }
    }

    /**
     * 重新获取确认统计结果
     * @param companyEntity
     * @return
     */
    @SysLog("重新获取确认统计结果")
    @RequestMapping("/censusResult2")
    public R censusResult2(InputCompanyEntity companyEntity) {
        companyEntity = companyService.getById(companyEntity.getId());
        R r2 = invoiceService.getCensusResult(companyEntity.getCompanyDutyParagraph(),companyEntity.getCensusTaskno());
        if(((Integer)r2.get("code")) == 555) {
            return r2;
        } else {
            Map<String,String> taskResult = (Map<String,String>)r2.get("taskResult");
            String businessStatus = (String)taskResult.get("businessStatus");
            String taskStatus = (String)r2.get("taskStatus");
            if(businessStatus == null) {
                // 任务接收成功,待处理
                if(taskStatus.equals("0")) {
                    businessStatus = "9";
                    // 任务处理完成
                } else if(taskStatus.equals("1")) {

                    // 任务处理中
                } else {
                    businessStatus = "9";
                }
            }
            companyEntity.setCensusResult(businessStatus);
            if(businessStatus.equals("1")) {
                companyEntity.setStatus("2");
                InputInvoiceEntity invoiceEntity = new InputInvoiceEntity();
                invoiceEntity.setApplyStatus("2");
                invoiceEntity.setCompanyId(companyEntity.getId());
                invoiceEntity.setDeptId(companyEntity.getDeptId().intValue());
                invoiceService.updateApply(invoiceEntity);
            }
            companyService.update(companyEntity);
        }
        return R.ok();
    }

    /**
     * 发起确认统计申请并确认
     * @param companyEntity
     * @return
     */
    @SysLog("发起确认统计")
    @RequestMapping("/censusResult")
    public R censusResult(InputCompanyEntity companyEntity) {
        companyEntity = companyService.getById(companyEntity.getId());
        R r = invoiceService.getAffirmCensus(companyEntity.getCompanyDutyParagraph(),companyEntity.getStatisticsTime());
        if((Integer) r.get("code") != 0) {
            return r;
        } else {
            companyEntity.setCensusTaskno((String)r.get("taskNo"));
            companyEntity.setStatus("2");
            companyService.update(companyEntity);
            if(((String)r.get("msg")).contains("存在未处理完的任务")) {
                return R.error().put("msg","已提交统计申请，请10分钟后获取确认结果");
            }
            R r2 = invoiceService.getCensusResult(companyEntity.getCompanyDutyParagraph(),(String)r.get("taskNo"));
            if(((Integer)r2.get("code")) == 555) {
                return r2;
            } else {
                Map<String,String> taskResult = (Map<String,String>)r2.get("taskResult");
                String businessStatus = (String)taskResult.get("businessStatus");
                String taskStatus = (String)r2.get("taskStatus");
                if(businessStatus == null) {
                    // 任务接收成功,待处理
                    if(taskStatus.equals("0")) {
                        businessStatus = "9";
                        // 任务处理完成
                    } else if(taskStatus.equals("1")) {

                        // 任务处理中
                    } else {
                        businessStatus = "9";
                    }
                }
                companyEntity.setCensusResult(businessStatus);
                if(businessStatus.equals("1")) {
                    companyEntity.setStatus("2");
                    InputInvoiceEntity invoiceEntity = new InputInvoiceEntity();
                    invoiceEntity.setApplyStatus("1");
                    invoiceEntity.setCompanyId(companyEntity.getId());
                    invoiceEntity.setDeptId(companyEntity.getDeptId().intValue());
                    invoiceService.updateApply(invoiceEntity);
                }
                companyService.update(companyEntity);
            }
        }
        return R.ok();
    }

    /**
     * 查看统计信息
     * @return
     */
    @SysLog("查看统计信息")
    @RequestMapping("/getTaskResult")
    public R getTaskResult(InputCompanyTaskResult companyTaskResult,Long deptId) {
        //根据deptId查询company的id，之后赋值给InputCompanyTaskResult
        InputCompanyEntity conpanyIdByDeptId = companyTaskResultService.findConpanyIdByDeptId(deptId);
        if (conpanyIdByDeptId.getId()== null){
            return R .error();
        }
            companyTaskResult.setCompanyId(conpanyIdByDeptId.getId());
            companyTaskResult.setType("0");
            List<InputCompanyTaskResult> companyTaskResults = companyTaskResultService.getListByType(companyTaskResult);
            companyTaskResult.setCompanyId(conpanyIdByDeptId.getId());
            companyTaskResult.setType("1");
            List<InputCompanyTaskResult> companyTaskResultList = companyTaskResultService.getListByType(companyTaskResult);

        return R.ok().put("companyTaskResults",companyTaskResults).put("companyTaskResultList",companyTaskResultList);
    }

}
