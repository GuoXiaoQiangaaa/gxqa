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
import com.pwc.modules.input.entity.InputInvoiceEntity;
import com.pwc.modules.input.service.InputCompanyService;
import com.pwc.modules.sys.entity.SysDeptEntity;
import com.pwc.modules.sys.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("companyService")
public class InputCompanyServiceImpl extends ServiceImpl<InputCompanyDao, InputCompanyEntity> implements InputCompanyService {

    @Autowired
    SysDeptService sysDeptService;

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
}
