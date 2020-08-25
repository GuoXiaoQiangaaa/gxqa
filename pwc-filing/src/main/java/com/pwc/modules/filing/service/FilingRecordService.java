package com.pwc.modules.filing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.R;
import com.pwc.modules.filing.entity.FilingRecordEntity;
import com.pwc.modules.sys.entity.SysFileEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 申报流程信息
 *
 * @author zk
 * @email 
 * @date 2019-11-08 15:11:47
 */
public interface FilingRecordService extends IService<FilingRecordEntity> {
    /**
     * 分页
     * @param params
     * @return 分页
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 申报上传提交
     * @param filingRecord
     * @return R
     */
    R filingUpload(FilingRecordEntity filingRecord);

    /**
     * 确认报告提交
     * @param filingRecord
     * @return R
     */
    R confirmReport(FilingRecordEntity filingRecord);

    /**
     * 报告审核
     * @param filingRecord
     * @return bool
     */
    boolean confirmReportAudit(FilingRecordEntity filingRecord);

    /**
     * 申报节点提交
     * @param filingRecord
     * @return
     */
    R filing(FilingRecordEntity filingRecord);

    /**
     * 扣款节点提交
     * @param filingRecord
     * @return
     */
    R deduction(FilingRecordEntity filingRecord);

    /**
     * 当前月的的分公司流
     * @param deptId
     * @return
     */
    FilingRecordEntity getCurrentMonthFiling(Long deptId);

    /**
     * 查询时间内的流
     * @param deptId
     * @param createTime
     * @return
     */
    FilingRecordEntity queryByDeptAndCreateTime(Long deptId, String createTime);

    /**
     * 修改状态
     * @param filingId
     * @param status
     * @return
     */
    boolean updateStatus(Long filingId, Integer status);

    /**
     * 统计数量根据deptIds
     * @param deptIds 企业id集合
     * @param query 数据库对应字段名 upload_status，confirm_report_status，declare_status，deduction_status
     * @param status 对应的状态值
     * @return
     */
    Integer countFilingByDeptIds(List<Long> deptIds, String query, Integer status, String date);

    Map<String, Object> extractPdf(Long filingId, MultipartFile file) throws IOException;

    List<SysFileEntity> getFilingFiles(Long filingId, int type);

    /**
     * 分页 查询第三方URL
     * @param params
     * @return 分页
     */
    String queryThirdFilingUrl(Map<String, Object> params);

    boolean submitFileByFileId(Long filingRecordFileId, Long filingId);
}

