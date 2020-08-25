package com.pwc.modules.filing.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.exception.RRException;
import com.pwc.common.third.TtkOrgUtil;
import com.pwc.common.third.common.TtkConstants;
import com.pwc.common.third.request.TaxLoginInfo;
import com.pwc.common.third.request.TtkOrgRequest;
import com.pwc.common.third.request.TtkTaxLoginInfoRequest;
import com.pwc.common.third.response.TtkOrgResponse;
import com.pwc.common.third.response.TtkResponse;
import com.pwc.modules.filing.dao.FilingCompanyDao;
import com.pwc.modules.filing.entity.FilingCompanyEntity;
import com.pwc.modules.filing.service.FilingCompanyService;
import com.pwc.modules.sys.entity.SysDeptEntity;
import com.pwc.modules.sys.service.FilingThirdCityCodeService;
import com.pwc.modules.sys.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("filingCompanyService")
public class FilingCompanyServiceImpl extends ServiceImpl<FilingCompanyDao, FilingCompanyEntity> implements FilingCompanyService  {

    private final Log log = Log.get(this.getClass());
    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private FilingThirdCityCodeService filingThirdCityCodeService;

    @Override
    public FilingCompanyEntity getByDeptId(Long deptId) {
        return this.getOne(new QueryWrapper<FilingCompanyEntity>().eq("dept_id", deptId));
    }

    @Override
    public boolean saveCompany(FilingCompanyEntity companyEntity) {
        SysDeptEntity deptEntity =sysDeptService.getById(companyEntity.getDeptId());
        // 创建企业第三方返回企业ID
        TtkOrgRequest createOrgReq = new TtkOrgRequest();
        createOrgReq.setName(deptEntity.getName());
        createOrgReq.setAccountingStandards(Long.parseLong(companyEntity.getAccountingStandards()));
        createOrgReq.setEnabledMonth(companyEntity.getEnabledMonth());
        createOrgReq.setEnabledYear(companyEntity.getEnabledYear());
        createOrgReq.setVatTaxpayer(Long.parseLong(companyEntity.getVatTaxpayer()));
        TtkResponse<TtkOrgResponse> createOrgResponse = TtkOrgUtil.createOrg(createOrgReq);
        log.info("新增企业创建第三方平台企业信息："+createOrgResponse.toString());
        if (createOrgResponse.getHead().getErrorCode().equals(TtkConstants.TtkResCode.SUCCESS.getValue())) {
            companyEntity.setOrgId(createOrgResponse.getBody().getOrgId());
            TtkTaxLoginInfoRequest request = new TtkTaxLoginInfoRequest();
            request.setId(companyEntity.getOrgId());
            request.setReturnValue(true);
            TaxLoginInfo loginInfo = new TaxLoginInfo();
            loginInfo.setDLFS(companyEntity.getLoginMethod().toString());
            loginInfo.setNSRSBH(deptEntity.getTaxCode());
            loginInfo.setQYMC(deptEntity.getName());
            // 获取第三方citycode
            String cityCode = filingThirdCityCodeService.getByDeptCityCode(deptEntity.getCityCode());
            if (StrUtil.isBlank(cityCode)) {
                throw new RRException("未匹配到第三方地区");
            }
            loginInfo.setSS(cityCode);
            loginInfo.setCanChange(!"false".equals(companyEntity.getCanChange().toLowerCase()));
            loginInfo.setDLZH(companyEntity.getLoginAccount());
            loginInfo.setDLMM(companyEntity.getLoginPassword());
            loginInfo.setGssbmm(companyEntity.getFilingPassowrd());
            request.setDlxxDto(loginInfo);
            log.info("新增企业上传企业网报请求信息："+request.toString());
            log.info("新增企业上传企业网报返回信息："+TtkOrgUtil.saveTaxLoginInfo(request).toString());
        }
        return super.save(companyEntity);
    }

    @Override
    public boolean updateCompany(FilingCompanyEntity companyEntity) {
        SysDeptEntity deptEntity =sysDeptService.getById(companyEntity.getDeptId());
        // 修改企业第三方
        TtkOrgRequest updateOrgReq = new TtkOrgRequest();
        updateOrgReq.setOrgId(companyEntity.getOrgId());
        updateOrgReq.setName(deptEntity.getName());
        updateOrgReq.setAccountingStandards(Long.parseLong(companyEntity.getAccountingStandards()));
        updateOrgReq.setEnabledMonth(companyEntity.getEnabledMonth());
        updateOrgReq.setEnabledYear(companyEntity.getEnabledYear());
        updateOrgReq.setVatTaxpayer(Long.parseLong(companyEntity.getVatTaxpayer()));
        TtkResponse<TtkOrgResponse> updateOrgResponse = TtkOrgUtil.createOrg(updateOrgReq);
        log.info("更新企业修改第三方平台企业信息："+updateOrgResponse.toString());
        return super.updateById(companyEntity);
    }
}
