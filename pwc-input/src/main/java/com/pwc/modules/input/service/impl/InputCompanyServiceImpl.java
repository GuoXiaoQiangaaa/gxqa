package com.pwc.modules.input.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.annotation.DataFilter;
import com.pwc.common.utils.Constant;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;
import com.pwc.modules.input.dao.InputCompanyDao;
import com.pwc.modules.input.entity.InputCompanyEntity;
import com.pwc.modules.input.entity.InputCompanyTaskDetail;
import com.pwc.modules.input.entity.InputCompanyTaskDifference;
import com.pwc.modules.input.entity.InputCompanyTaskInvoices;
import com.pwc.modules.input.service.InputCompanyService;
import com.pwc.modules.input.service.InputCompanyTaskDetailService;
import com.pwc.modules.input.service.InputCompanyTaskDifferenceService;
import com.pwc.modules.input.service.InputCompanyTaskInvoicesService;
import com.pwc.modules.sys.entity.SysDeptEntity;
import com.pwc.modules.sys.service.SysDeptService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("companyService")
public class InputCompanyServiceImpl extends ServiceImpl<InputCompanyDao, InputCompanyEntity> implements InputCompanyService {

    @Autowired
    SysDeptService sysDeptService;
    @Autowired
    private InputCompanyTaskDetailService detailService;
    @Autowired
    private InputCompanyTaskInvoicesService invoicesService;
    @Autowired
    private InputCompanyTaskDifferenceService differenceService;

    @Override
    @DataFilter(subDept = true, user = false)
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<InputCompanyEntity> page = this.page(
                new Query<InputCompanyEntity>().getPage(params),
                new QueryWrapper<InputCompanyEntity>()
                        .apply(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER))
        );
        for (InputCompanyEntity companyEntity : page.getRecords()) {
            SysDeptEntity deptEntity = sysDeptService.getById(companyEntity.getDeptId());
            if (null != deptEntity) {
                companyEntity.setCompanyName(deptEntity.getName());
                companyEntity.setCompanyDutyParagraph(deptEntity.getTaxCode());
                companyEntity.setAddress(deptEntity.getAddress());
                companyEntity.setCompanyAddressPhone(deptEntity.getContact());
//            companyEntity.set
            }
        }
        return new PageUtils(page);
    }

    @Override
    public List<InputCompanyEntity> getList() {
        return this.baseMapper.getList();
    }

    @Override
    public void update(InputCompanyEntity companyEntity) {
        this.baseMapper.update(companyEntity);
    }

    @Override
    public InputCompanyEntity getByDeptId(Long deptId) {
        return baseMapper.selectOne(new QueryWrapper<InputCompanyEntity>().eq("dept_id", deptId));
    }


    /**
     * 统计情况列表
     */
    @Override
    @DataFilter(subDept = true, user = false)
    public PageUtils list(Map<String, Object> params) {
        String companyNumber = (String) params.get("companyNumber");
        String companyName = (String) params.get("companyName");
        String taxCode = (String) params.get("taxCode");
        String status = (String) params.get("status");

        IPage<InputCompanyEntity> page = this.page(
                new Query<InputCompanyEntity>().getPage(params),
                new QueryWrapper<InputCompanyEntity>()
                        .like(StringUtils.isNotBlank(companyNumber), "company_number", companyNumber)
                        .eq(StringUtils.isNotBlank(companyName), "company_name", companyName)
                        .like(StringUtils.isNotBlank(taxCode), "company_duty_paragraph", taxCode)
                        .eq(StringUtils.isNotBlank(status), "status", status)
                        .apply(null != params.get(Constant.SQL_FILTER), (String) params.get(Constant.SQL_FILTER))
        );

        return new PageUtils(page);
    }

    /**
     * 统计详情列表
     */
    @Override
    public PageUtils details(Integer companyId, Map<String, Object> params) {
        IPage<InputCompanyTaskDetail> page = detailService.page(
                new Query<InputCompanyTaskDetail>().getPage(params),
                new QueryWrapper<InputCompanyTaskDetail>()
                        .eq("company_id", companyId)
                        .orderByAsc("invoice_type")
        );
        return new PageUtils(page);
    }

    /**
     * 统计成功发票列表
     */
    @Override
    public PageUtils invoices(Integer companyId, Map<String, Object> params) {
        IPage<InputCompanyTaskInvoices> page = invoicesService.page(
                new Query<InputCompanyTaskInvoices>().getPage(params),
                new QueryWrapper<InputCompanyTaskInvoices>()
                        .eq("company_id", companyId)
                        .orderByAsc("invoice_type")
        );
        return new PageUtils(page);
    }

    /**
     * 统计差异发票列表
     */
    @Override
    public PageUtils differenceInvoices(Integer companyId, Map<String, Object> params) {
        IPage<InputCompanyTaskDifference> page = differenceService.page(
                new Query<InputCompanyTaskDifference>().getPage(params),
                new QueryWrapper<InputCompanyTaskDifference>()
                        .eq("company_id", companyId)
                        .orderByAsc("invoice_type")
        );
        return new PageUtils(page);
    }

}
