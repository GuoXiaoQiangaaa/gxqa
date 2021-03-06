package com.pwc.modules.data.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.exception.RRException;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;
import com.pwc.modules.data.dao.OutputCustomerNewDao;
import com.pwc.modules.data.entity.OutputCustomerNewEntity;
import com.pwc.modules.data.service.OutputCustomerNewService;
import com.pwc.modules.sys.shiro.ShiroUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * 客户信息服务实现
 *
 * @author fanpf
 * @date 2020/8/24
 */
@Service("outputCustomerNewService")
@Slf4j
public class OutputCustomerNewServiceImpl extends ServiceImpl<OutputCustomerNewDao, OutputCustomerNewEntity> implements OutputCustomerNewService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OutputCustomerNewEntity> page = this.page(
                new Query<OutputCustomerNewEntity>().getPage(params),
                new QueryWrapper<OutputCustomerNewEntity>()
                        .orderByDesc("create_time")
        );

        return new PageUtils(page);
    }

    /**
     * 新增
     */
    @Override
    public boolean save(OutputCustomerNewEntity outputCustomerNew) {
        // 参数校验
//        this.checkParams(outputCustomerNew);
        int count = super.count(
                new QueryWrapper<OutputCustomerNewEntity>()
                        .eq("sap_code", outputCustomerNew.getSapCode())
                        .eq("tax_code", outputCustomerNew.getTaxCode())
        );

        if(count > 0){
            throw new RRException("该数据已存在,请核对后再添加");
        }

        outputCustomerNew.setDelFlag("1");
        outputCustomerNew.setCreateBy(String.valueOf(ShiroUtils.getUserId()));
        outputCustomerNew.setCreateTime(new Date());
        return super.save(outputCustomerNew);
    }

    /**
     * 编辑
     */
    @Override
    public boolean updateById(OutputCustomerNewEntity outputCustomerNew) {
        // 参数校验
//        this.checkParams(outputCustomerNew);
        // 验重
        OutputCustomerNewEntity entity = super.getOne(
                new QueryWrapper<OutputCustomerNewEntity>()
                        .eq("sap_code", outputCustomerNew.getSapCode())
                        .eq("tax_code", outputCustomerNew.getTaxCode())
        );

        if(null != entity && !outputCustomerNew.getCustomerId().equals(entity.getCustomerId())){
            throw new RRException("该数据已存在,请核对后再修改");
        }

        outputCustomerNew.setUpdateBy(String.valueOf(ShiroUtils.getUserId()));
        outputCustomerNew.setUpdateTime(new Date());
        return super.updateById(outputCustomerNew);
    }

    /**
     * 禁用/启用
     */
    @Override
    public void disableOrEnable(OutputCustomerNewEntity reqVo) {
        reqVo.setUpdateBy(String.valueOf(ShiroUtils.getUserId()));
        reqVo.setUpdateTime(new Date());
        super.updateById(reqVo);
    }

    /**
     * 关键字查询
     */
    @Override
    public PageUtils search(Map<String, Object> params) {
        String keyWords = (String) params.get("keyWords");

        IPage<OutputCustomerNewEntity> page;
        if(StringUtils.isNotBlank(keyWords)){
            keyWords = keyWords.trim();
            page = this.page(
                    new Query<OutputCustomerNewEntity>().getPage(params),
                    new QueryWrapper<OutputCustomerNewEntity>()
                            .like("sap_code", keyWords).or()
                            .like("dept_code", keyWords).or()
                            .like("name", keyWords).or()
                            .like("name_cn", keyWords).or()
                            .like("tax_code", keyWords).or()
                            .like("address", keyWords).or()
                            .like("contact", keyWords).or()
                            .like("bank", keyWords).or()
                            .like("bank_account", keyWords).or()
                            .like("email", keyWords)
                            .orderByDesc("create_time")
            );
        }else {
            page = this.page(
                    new Query<OutputCustomerNewEntity>().getPage(params),
                    new QueryWrapper<OutputCustomerNewEntity>()
                            .orderByDesc("create_time")
            );
        }

        return new PageUtils(page);
    }

    /**
     * 数据导入
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importCustomer(MultipartFile[] files) {
        Map<String, Object> resMap = new HashMap<>();
        // 数据校验正确的集合
        List<OutputCustomerNewEntity> entityList = new ArrayList<>();
        // 数据重复的集合
        List<OutputCustomerNewEntity> duplicateList = new ArrayList<>();
        // 数据总量
        int total = 0;
        // 数据有误条数
        int fail = 0;
        // 记录excel中的重复数据
        List<String> repeatDataList = new ArrayList<>();
        // 记录数据有误的文件
        StringBuffer sb = new StringBuffer();
        try {
            for (MultipartFile file : files) {
                String filename = file.getOriginalFilename();
                ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
                String[] excelHead = {"客户SAP代码（必填）", "公司代码", "英文客户名称", "中文客户名称（必填）",
                        "纳税人识别号（必填）", "地址（必填）", "电话号码（必填）", "开户行", "银行账号", "客户邮箱"};
                String [] excelHeadAlias = {"sapCode", "deptCode", "name", "nameCn", "taxCode", "address", "contact",
                        "bank", "bankAccount", "email"};
                for (int i = 0; i < excelHead.length; i++) {
                    reader.addHeaderAlias(excelHead[i], excelHeadAlias[i]);
                }
                List<OutputCustomerNewEntity> dataList = reader.read(0, 1, OutputCustomerNewEntity.class);

                if(CollectionUtils.isEmpty(dataList)){
                    log.error("上传的{}Excel为空,请重新上传", filename);
//                    throw new RRException("上传的Excel为空,请重新上传");
                    continue;
                }
                total += dataList.size();
                int count = 1;
                for (OutputCustomerNewEntity customerEntity : dataList) {
                    count++;
                    // 参数校验
                    if(1 == checkExcel(customerEntity)){
                        // 参数有误
                        fail += 1;
                        if(!StringUtils.contains(sb.toString(), filename)){
                            sb.append("文件" + filename + "的错误行号为:");
                        }
                        sb.append(count + ",");
                    }else {
                        // 去除Excel中重复数据
                        String repeatData = customerEntity.getSapCode() + customerEntity.getTaxCode();
                        if(CollectionUtil.contains(repeatDataList, repeatData)){
                            fail += 1;
                            if(!StringUtils.contains(sb.toString(), filename)){
                                sb.append("文件" + filename + "的错误行号为:");
                            }
                            sb.append(count + ",");
                            continue;
                        }
                        repeatDataList.add(repeatData);
                        // 验重
                        OutputCustomerNewEntity duplicate = super.getOne(
                                new QueryWrapper<OutputCustomerNewEntity>()
                                        .eq("sap_code", customerEntity.getSapCode())
                                        .eq("tax_code", customerEntity.getTaxCode())
                        );
                        if(null != duplicate){
                            duplicate.setDeptCode(customerEntity.getDeptCode());
                            duplicate.setName(customerEntity.getName());
                            duplicate.setNameCn(customerEntity.getNameCn());
                            duplicate.setAddress(customerEntity.getAddress());
                            duplicate.setContact(customerEntity.getContact());
                            duplicate.setBank(customerEntity.getBank());
                            duplicate.setBankAccount(customerEntity.getBankAccount());
                            duplicate.setEmail(customerEntity.getEmail());
                            duplicate.setUpdateBy(String.valueOf(ShiroUtils.getUserId()));
                            duplicate.setUpdateTime(new Date());
                            duplicateList.add(duplicate);
                        }else {
                            // 添加校验正确的实体
                            customerEntity.setDelFlag("1");
                            customerEntity.setCreateBy(String.valueOf(ShiroUtils.getUserId()));
                            customerEntity.setCreateTime(new Date());
                            entityList.add(customerEntity);
                        }
                    }
                }
                if(sb.toString().endsWith(",")){
                    sb.deleteCharAt(sb.lastIndexOf(",")).append(";");
                }
            }
            if(sb.toString().endsWith(";")){
                sb.deleteCharAt(sb.lastIndexOf(";")).append("。");
            }
            resMap.put("total", total);
            resMap.put("success", duplicateList.size() + entityList.size());
            resMap.put("fail", fail);
            resMap.put("failDetail", sb.toString());

            if(CollectionUtil.isNotEmpty(duplicateList)){
                super.updateBatchById(duplicateList);
            }
            if(CollectionUtil.isNotEmpty(entityList)){
                super.saveBatch(entityList);
            }

            return resMap;
        } catch (RRException e){
            throw e;
        } catch (Exception e) {
            log.error("客户信息导入出错: {}", e);
            throw new RRException("客户信息导入出现异常");
        }
    }

    /**
     * 参数校验
     */
    private void checkParams(OutputCustomerNewEntity outputCustomerNew){
        if(StringUtils.isBlank(outputCustomerNew.getSapCode())){
            throw new RRException("客户SAP代码不能为空");
        }
        if(StringUtils.isBlank(outputCustomerNew.getNameCn())){
            throw new RRException("中文客户名称不能为空");
        }
        if(StringUtils.isBlank(outputCustomerNew.getTaxCode())){
            throw new RRException("纳税人识别号不能为空");
        }
        if(StringUtils.isBlank(outputCustomerNew.getAddress())){
            throw new RRException("客户地址不能为空");
        }
        if(StringUtils.isBlank(outputCustomerNew.getContact())){
            throw new RRException("客户电话不能为空");
        }
    }

    /**
     * 校验Excel中参数
     */
    private int checkExcel(OutputCustomerNewEntity entity){
        if(StringUtils.isBlank(entity.getSapCode()) || StringUtils.isBlank(entity.getNameCn()) ||
                StringUtils.isBlank(entity.getTaxCode()) || StringUtils.isBlank(entity.getAddress()) ||
                StringUtils.isBlank(entity.getContact())){
            return 1;
        }
        return 0;
    }
}
