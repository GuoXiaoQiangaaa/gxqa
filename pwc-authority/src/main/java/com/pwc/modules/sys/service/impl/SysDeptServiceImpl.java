

package com.pwc.modules.sys.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.annotation.DataFilter;
import com.pwc.common.exception.RRException;
import com.pwc.common.utils.Constant;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;
import com.pwc.modules.sys.dao.SysDeptDao;
import com.pwc.modules.sys.entity.InputCompanyDto;
import com.pwc.modules.sys.entity.SysDeptEntity;
import com.pwc.modules.sys.entity.TreeSelectVo;
import com.pwc.modules.sys.service.FilingThirdCityCodeService;
import com.pwc.modules.sys.service.SysDeptService;
import com.pwc.modules.sys.shiro.ShiroUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 组织部门相关
 * @author zk
 */
@Service("sysDeptService")
public class SysDeptServiceImpl extends ServiceImpl<SysDeptDao, SysDeptEntity> implements SysDeptService {

	@Autowired
	private FilingThirdCityCodeService filingThirdCityCodeService;
	@Resource
	private SysDeptDao sysDeptDao;

	private final Log log = Log.get(this.getClass());

	@Override
	public List<SysDeptEntity> queryList(Map<String, Object> params){
		return baseMapper.queryList(params);
	}

	@Override
	public List<Long> queryDetpIdList(Long parentId) {
		return baseMapper.queryDetpIdList(parentId);
	}

	@Override
	public List<Long> getSubDeptIdList(Long deptId){
		//部门及子部门ID列表
		List<Long> deptIdList = new ArrayList<>();

		//获取子部门ID
		List<Long> subIdList = queryDetpIdList(deptId);
		getDeptIdTreeList(subIdList, deptIdList);

		return deptIdList;
	}

	@Override
	@DataFilter(subDept = true, user = false)
	public PageUtils queryPage(Map<String, Object> params) {
		String name = (String)params.get("name");
		String contact = (String)params.get("contact");
		String parentId = (String) params.get("parentId");
		String owner = (String) params.get("owner");

//		parentId = StringUtils.isNotBlank(parentId) ? parentId : "0";
		if("0".equals(parentId)){
			parentId = "";
		}
		IPage<SysDeptEntity> page = this.page(
				new Query<SysDeptEntity>().getPage(params),
				new QueryWrapper<SysDeptEntity>()
						.like(StringUtils.isNotBlank(name),"name", name)
						.like(StringUtils.isNotBlank(contact),"contact", contact)
						.like(StringUtils.isNotBlank(owner),"owner", owner)
						.eq(StringUtils.isNotBlank(parentId),"parent_id", parentId)
						.apply(params.get(Constant.SQL_FILTER) != null, (String)params.get(Constant.SQL_FILTER))
						.orderByDesc("create_time")
		);

		for(SysDeptEntity sysDeptEntity : page.getRecords()){
			 Integer childNum= baseMapper.selectCount(new QueryWrapper<SysDeptEntity>().eq("parent_id", sysDeptEntity.getDeptId()));
			sysDeptEntity.setChildNum(childNum);
		}

		return new PageUtils(page);
	}

	/**
	 * 关键字查询
	 */
	@Override
	@DataFilter(subDept = true, user = false)
	public PageUtils search(Map<String, Object> params) {
		String keyWords = (String) params.get("keyWords");

		if(StringUtils.isNotBlank(keyWords)){
			keyWords = keyWords.trim();
			String parentId = (String) params.get("parentId");
//			parentId = StringUtils.isNotBlank(parentId) ? parentId : "0";
			if("0".equals(parentId)){
				parentId = "";
			}

			IPage<SysDeptEntity> page = this.page(
					new Query<SysDeptEntity>().getPage(params),
					new QueryWrapper<SysDeptEntity>()
							.like("dept_code", keyWords).or()
							.like("sap_dept_code", keyWords).or()
							.like("name", keyWords).or()
							.like("tax_code", keyWords).or()
							.like("manage_address", keyWords).or()
							.like("regist_address", keyWords).or()
							.like("bank", keyWords).or()
							.like("bank_account", keyWords).or()
							.like("contact", keyWords)
							.eq(StringUtils.isNotBlank(parentId),"parent_id", parentId)
							.apply(params.get(Constant.SQL_FILTER) != null, (String)params.get(Constant.SQL_FILTER))
							.orderByDesc("create_time")
			);
			for(SysDeptEntity sysDeptEntity : page.getRecords()){
				Integer childNum= baseMapper.selectCount(new QueryWrapper<SysDeptEntity>().eq("parent_id", sysDeptEntity.getDeptId()));
				sysDeptEntity.setChildNum(childNum);
			}
			return new PageUtils(page);
		}else {
			return this.queryPage(params);
		}
	}

	/**
	 * 修改部门状态 启用/禁用
	 */
	@Override
	public boolean updateStatus(Long deptId, Integer status) {
		SysDeptEntity sysDeptEntity = new SysDeptEntity();
		sysDeptEntity.setStatus(status);
		sysDeptEntity.setUpdateBy(String.valueOf(ShiroUtils.getUserId()));
		sysDeptEntity.setUpdateTime(new Date());
		return this.update(sysDeptEntity, new QueryWrapper<SysDeptEntity>().eq("dept_id", deptId));
	}

	@Override
	public List<SysDeptEntity> queryDeptList(Long parentId) {
		return baseMapper.queryDeptList(parentId);
	}

	@Override
	public List<SysDeptEntity> getTreeDeptList(Long deptId) {

		//获取子部门ID
		List<SysDeptEntity> subList = queryDeptList(deptId);
		treeList(subList);

		return subList;
	}

	/**
	 * 为了满足下拉多选框
	 * @param deptId
	 * @return
	 */
	@Override
	public List<TreeSelectVo> getTreeSelectList(Long deptId){
		//获取子部门ID
		List<TreeSelectVo> subList = baseMapper.queryVOList(deptId);
		treeSelectList(subList);

		return subList;
	}

	@Override
	public SysDeptEntity getByTaxCode(String taxCode) {
		return super.getOne(new QueryWrapper<SysDeptEntity>().eq("tax_code", taxCode));
	}
	@Override
	public SysDeptEntity getByDeptCode(String deptCode) {
		return super.getOne(new QueryWrapper<SysDeptEntity>().eq("dept_code", deptCode));
	}

	/**
	 * 新增部门
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean saveDept(SysDeptEntity deptEntity) {
		// 创建企业第三方返回企业ID
//		TtkOrgRequest createOrgReq = new TtkOrgRequest();
//		createOrgReq.setName(deptEntity.getName());
//		createOrgReq.setAccountingStandards(Long.parseLong(deptEntity.getThirdAccountingStandards()));
//		createOrgReq.setEnabledMonth(deptEntity.getThirdEnabledMonth());
//		createOrgReq.setEnabledYear(deptEntity.getThirdEnabledYear());
//		createOrgReq.setVatTaxpayer(Long.parseLong(deptEntity.getThirdVatTaxpayer()));
//		TtkResponse<TtkOrgResponse> createOrgResponse = TtkOrgUtil.createOrg(createOrgReq);
//		log.info("新增企业创建第三方平台企业信息："+createOrgResponse.toString());
//		if (createOrgResponse.getHead().getErrorCode().equals(TtkConstants.TtkResCode.SUCCESS.getValue())) {
//			deptEntity.setThirdOrgId(createOrgResponse.getBody().getOrgId());
//			TtkTaxLoginInfoRequest request = new TtkTaxLoginInfoRequest();
//			request.setId(deptEntity.getThirdOrgId());
//			request.setReturnValue(true);
//			TaxLoginInfo loginInfo = new TaxLoginInfo();
//			loginInfo.setDLFS(deptEntity.getThirdLoginMethod().toString());
//			loginInfo.setNSRSBH(deptEntity.getTaxCode());
//			loginInfo.setQYMC(deptEntity.getName());
//			// 获取第三方citycode
//			String cityCode = filingThirdCityCodeService.getByDeptCityCode(deptEntity.getCityCode());
//			if (StrUtil.isBlank(cityCode)) {
//				throw new RRException("未匹配到第三方地区");
//			}
////			loginInfo.setSS(TtkConstants.DISTRICT_MAP.get(deptEntity.getCityCode()));
//			loginInfo.setSS(cityCode);
//			loginInfo.setCanChange(!"false".equals(deptEntity.getThirdCanChange().toLowerCase()));
//			loginInfo.setDLZH(deptEntity.getThirdLoginAccount());
//			loginInfo.setDLMM(deptEntity.getThirdLoginPassword());
//			loginInfo.setGssbmm(deptEntity.getThirdFilingPassowrd());
//			request.setDlxxDto(loginInfo);
//			log.info("新增企业上传企业网报请求信息："+request.toString());
//			log.info("新增企业上传企业网报返回信息："+TtkOrgUtil.saveTaxLoginInfo(request).toString());
//		}
		int count = super.count(
				new QueryWrapper<SysDeptEntity>()
						.eq("dept_code", deptEntity.getDeptCode()).or()
						.eq("tax_code", deptEntity.getTaxCode())

		);
		if(count > 0){
			throw new RRException("该数据已存在,请核对后再添加");
		}
		deptEntity.setCreateBy(String.valueOf(ShiroUtils.getUserId()));
		deptEntity.setCreateTime(new Date());
		boolean save = super.save(deptEntity);
		// 维护input_company表
		InputCompanyDto company = new InputCompanyDto();
		company.setCompanyNumber(deptEntity.getDeptCode());
		company.setCompanyName(deptEntity.getName());
		company.setCompanyDutyParagraph(deptEntity.getTaxCode());
		company.setCompanyAddressPhone(deptEntity.getRegistAddress() + deptEntity.getContact());
		company.setCompanyBankAccount(deptEntity.getBank() + deptEntity.getBankAccount());
		company.setStatus("0");
		company.setDeptId(deptEntity.getDeptId());
		company.setCreateBy(String.valueOf(ShiroUtils.getUserId()));
		company.setCreateTime(new Date());
		sysDeptDao.saveCompany(company);
		return save;
	}

	/**
	 * 更新部门
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateDept(SysDeptEntity deptEntity) {
		// 修改企业第三方
//		TtkOrgRequest updateOrgReq = new TtkOrgRequest();
//		updateOrgReq.setOrgId(deptEntity.getThirdOrgId());
//		updateOrgReq.setName(deptEntity.getName());
//		updateOrgReq.setAccountingStandards(Long.parseLong(deptEntity.getThirdAccountingStandards()));
//		updateOrgReq.setEnabledMonth(deptEntity.getThirdEnabledMonth());
//		updateOrgReq.setEnabledYear(deptEntity.getThirdEnabledYear());
//		updateOrgReq.setVatTaxpayer(Long.parseLong(deptEntity.getThirdVatTaxpayer()));
//		TtkResponse<TtkOrgResponse> updateOrgResponse = TtkOrgUtil.createOrg(updateOrgReq);
//		log.info("更新企业修改第三方平台企业信息："+updateOrgResponse.toString());
//		TtkTaxLoginInfoRequest request = new TtkTaxLoginInfoRequest();
//		request.setId(deptEntity.getThirdOrgId());
//		request.setReturnValue(true);
//		TaxLoginInfo loginInfo = new TaxLoginInfo();
//		loginInfo.setDLFS(deptEntity.getThirdLoginMethod().toString());
//		loginInfo.setNSRSBH(deptEntity.getTaxCode());
//		loginInfo.setQYMC(deptEntity.getName());
//		loginInfo.setSS(TtkConstants.DISTRICT_MAP.get(deptEntity.getCityCode()));
//		loginInfo.setCanChange(!"false".equals(deptEntity.getThirdCanChange().toLowerCase()));
//		loginInfo.setDLZH(deptEntity.getThirdLoginAccount());
//		loginInfo.setDLMM(deptEntity.getThirdLoginPassword());
//		loginInfo.setGssbmm(deptEntity.getThirdFilingPassowrd());
//		request.setDlxxDto(loginInfo);
//		log.info("新增企业上传企业网报请求信息："+request.toString());
//		log.info("新增企业上传企业网报返回信息："+TtkOrgUtil.saveTaxLoginInfo(request).toString());
		// 验重
		List<SysDeptEntity> entityList = super.list(
				new QueryWrapper<SysDeptEntity>()
						.eq("dept_code", deptEntity.getDeptCode()).or()
						.eq("tax_code", deptEntity.getTaxCode())
		);
		if(CollectionUtil.isNotEmpty(entityList)){
			for (SysDeptEntity entity : entityList) {
				if(!deptEntity.getDeptId().equals(entity.getDeptId())){
					throw new RRException("该数据已存在,请核对后再修改");
				}
			}
		}

		deptEntity.setUpdateBy(String.valueOf(ShiroUtils.getUserId()));
		deptEntity.setUpdateTime(new Date());
		boolean update = super.updateById(deptEntity);
		// 维护input_company表
		InputCompanyDto company = sysDeptDao.queryCompanyByDeptId(deptEntity.getDeptId());
		if(null == company){
			company = new InputCompanyDto();
			company.setCompanyNumber(deptEntity.getDeptCode());
			company.setCompanyName(deptEntity.getName());
			company.setCompanyDutyParagraph(deptEntity.getTaxCode());
			company.setCompanyAddressPhone(deptEntity.getRegistAddress() + deptEntity.getContact());
			company.setCompanyBankAccount(deptEntity.getBank() + deptEntity.getBankAccount());
			company.setStatus("0");
			company.setDeptId(deptEntity.getDeptId());
			company.setCreateBy(String.valueOf(ShiroUtils.getUserId()));
			company.setCreateTime(new Date());
			sysDeptDao.saveCompany(company);
			return update;
		}
		company.setCompanyNumber(deptEntity.getDeptCode());
		company.setCompanyName(deptEntity.getName());
		company.setCompanyDutyParagraph(deptEntity.getTaxCode());
		company.setCompanyAddressPhone(deptEntity.getRegistAddress() + deptEntity.getContact());
		company.setCompanyBankAccount(deptEntity.getBank() + deptEntity.getBankAccount());
		company.setUpdateBy(String.valueOf(ShiroUtils.getUserId()));
		company.setUpdateTime(new Date());
		sysDeptDao.updateCompany(company);
		return update;
	}

	@Override
	public int getCountByTaxCodAndName(String taxCode, String name) {
		return this.baseMapper.selectCount(new QueryWrapper<SysDeptEntity>()
				.eq("tax_code", taxCode)
				.eq("name", name)
		);
	}

	@Override
	public SysDeptEntity getByName(String name) {
		return this.baseMapper.selectOne(new QueryWrapper<SysDeptEntity>().eq("name", name).eq("status","1"));
	}

	@Override
	public List<String> getTaxCodeByIds(Map<String, Object> params) {
		return baseMapper.queryTaxCodeByIds(params);
	}

	/**
	 * 根据部门id获取税号
	 */
	@Override
	public String queryTaxCodeById(Long deptId) {
		return sysDeptDao.queryTaxCodeById(deptId);
	}

	@Override
	@DataFilter(subDept = true, user = false)
	public PageUtils queryPageForStatistics(Map<String, Object> params) {
		String name = (String)params.get("companyName");
		String taxCode=(String)params.get("companyDutyParagraph");
		String contact = (String)params.get("contact");
		String owner = (String) params.get("owner");

		IPage<SysDeptEntity> page = this.page(
				new Query<SysDeptEntity>().getPage(params),
				new QueryWrapper<SysDeptEntity>()
						.like(StringUtils.isNotBlank(name),"name", name)
						.eq(StringUtils.isNotBlank(taxCode),"tax_code", taxCode)
						.like(StringUtils.isNotBlank(owner),"owner", owner)
						.apply(params.get(Constant.SQL_FILTER) != null, (String)params.get(Constant.SQL_FILTER))
						.orderByDesc("dept_id")
		);

		return new PageUtils(page);
	}

	/**
	 * 查询详情
	 */
	@Override
	public SysDeptEntity queryInfo(Long deptId) {
		SysDeptEntity deptEntity = super.getById(deptId);
		// 查询该部门的权限分配人员
		// allData
		List<String> allData = sysDeptDao.queryUsernameByDeptId(deptId, 0);
		// singleData
		List<String> singleData = sysDeptDao.queryUsernameByDeptId(deptId, 1);

		if(CollectionUtil.isNotEmpty(allData)){
			deptEntity.setAllData(allData);
		}

		if(CollectionUtil.isNotEmpty(singleData)){
			deptEntity.setSingleData(singleData);
		}
		return deptEntity;
	}

	/**
	 * 递归 select
	 */
	private void treeSelectList(List<TreeSelectVo> subList){
		for(TreeSelectVo dept : subList){
			List<TreeSelectVo> list = baseMapper.queryVOList(Long.parseLong(dept.getKey()));
			dept.setChildren(list);
			if(list.size() > 0){
				treeSelectList(list);
			}
		}
	}
	/**
	 * 递归
	 */
	private void treeList(List<SysDeptEntity> subList){
		for(SysDeptEntity dept : subList){
			List<SysDeptEntity> list = queryDeptList(dept.getDeptId());
			dept.setChildren(list);
			if(list.size() > 0){
				treeList(list);
			}
		}
	}


	/**
	 * 递归
	 */
	private void getDeptIdTreeList(List<Long> subIdList, List<Long> deptIdList){
		for(Long deptId : subIdList){
			List<Long> list = queryDetpIdList(deptId);
			if(list.size() > 0){
				getDeptIdTreeList(list, deptIdList);
			}

			deptIdList.add(deptId);
		}
	}

	/**
	 * 获取当前正常未删除数据
	 * @return
	 */
	@Override
	public List<SysDeptEntity> getDeptByStatus(){
		List<SysDeptEntity>  sysDeptEntitys =   this.list(
				new QueryWrapper<SysDeptEntity>().eq("status",1).eq("del_flag",0)
		);
		return  sysDeptEntitys;
	}

}
