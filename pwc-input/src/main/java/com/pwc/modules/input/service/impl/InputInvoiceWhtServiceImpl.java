package com.pwc.modules.input.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.exception.RRException;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;
import com.pwc.modules.input.dao.InputInvoiceWhtDao;
import com.pwc.modules.input.entity.InputInvoiceWhtEntity;
import com.pwc.modules.input.service.InputInvoiceWhtService;
import com.pwc.modules.sys.entity.SysDeptEntity;
import com.pwc.modules.sys.service.SysDeptService;
import com.pwc.modules.sys.shiro.ShiroUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * WHT(代扣代缴单据)服务实现
 *
 * @author fanpf
 * @date 2020/9/4
 */
@Service("inputInvoiceWhtService")
@Slf4j
public class InputInvoiceWhtServiceImpl extends ServiceImpl<InputInvoiceWhtDao, InputInvoiceWhtEntity> implements InputInvoiceWhtService {

    @Autowired
    private SysDeptService deptService;

    /**
     * 分页查询列表
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String whtCode = (String) params.get("whtCode");
        String deptCode = (String) params.get("deptCode");
        String deptName = (String) params.get("deptName");
        String taxPrice = (String) params.get("taxPrice");
        String totalPrice = (String) params.get("totalPrice");
        String authDate = (String) params.get("authDate");

        IPage<InputInvoiceWhtEntity> page = this.page(
                new Query<InputInvoiceWhtEntity>().getPage(params),
                new QueryWrapper<InputInvoiceWhtEntity>()
                        .like(StringUtils.isNotBlank(whtCode), "wht_code", whtCode)
                        .like(StringUtils.isNotBlank(deptCode), "dept_code", deptCode)
                        .eq(StringUtils.isNotBlank(deptName), "dept_name", deptName)
                        .like(null != taxPrice, "tax_price", taxPrice)
                        .like(null != totalPrice, "total_price", totalPrice)
                        .eq(StringUtils.isNotBlank(authDate), "auth_date", authDate)
                        .eq("del_flag", "1")
                        .orderByDesc("update_time")
        );

        // 查询时关联dept_id
        for (InputInvoiceWhtEntity entity : page.getRecords()) {
            if(null == entity.getDeptId()){
                SysDeptEntity deptEntity = deptService.getByDeptCode(entity.getDeptCode());
                if(null != deptEntity){
                    entity.setDeptId(deptEntity.getDeptId().intValue());
                    entity.setDeptName(deptEntity.getName());
                    entity.setUpdateTime(new Date());
                    entity.setUpdateBy(ShiroUtils.getUserId().intValue());
                    super.updateById(entity);
                }
            }
        }

        return new PageUtils(page);
    }

    /**
     * 编辑
     */
    @Override
    public boolean updateById(InputInvoiceWhtEntity entity){
        // 校验认证所属期
//        String authDate = entity.getAuthDate();
//        DateTime parse = DateUtil.parse(authDate, "yyyyMM");
//        String now = DateUtil.format(new Date(), "yyyyMM");
//        int compare = DateUtil.compare(parse, DateUtil.parse(now, "yyyyMM"));
//        if(compare < 0){
//            throw new RRException("认证所属期已过,请核对后再修改");
//        }

        // 验重
        InputInvoiceWhtEntity whtEntity = super.getOne(
                new QueryWrapper<InputInvoiceWhtEntity>()
                        .eq("wht_code", entity.getWhtCode())
                        .eq("del_flag", "1")
        );
        if(null != whtEntity && !entity.getWhtId().equals(whtEntity.getWhtId())){
            throw new RRException("该数据已存在,请核对后再修改");
        }
        SysDeptEntity deptEntity = deptService.getByDeptCode(entity.getDeptCode());
        if(null != deptEntity){
            entity.setDeptId(deptEntity.getDeptId().intValue());
            entity.setDeptName(deptEntity.getName());
        }else {
            throw new RRException("公司代码不存在,请核对后再修改");
        }
        entity.setUpdateTime(new Date());
        entity.setUpdateBy(ShiroUtils.getUserId().intValue());
        return super.updateById(entity);
    }


    /**
     * 逻辑删除
     */
    @Override
    public void remove(Map<String, Object> params) {
        String idsStr = (String) params.get("ids");
        if(StringUtils.isNotBlank(idsStr)){
            String[] ids = idsStr.split(",");
            List<InputInvoiceWhtEntity> entities = super.list(
                    new QueryWrapper<InputInvoiceWhtEntity>()
                            .in("wht_id", ids)
            );
            if(CollectionUtil.isEmpty(entities)){
                return;
            }
            for (InputInvoiceWhtEntity entity : entities) {
                entity.setUpdateBy(ShiroUtils.getUserId().intValue());
                entity.setUpdateTime(new Date());
                entity.setDelFlag("0");
            }
            super.updateBatchById(entities);
        }
    }

    /**
     * 数据导入
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importWht(MultipartFile[] files) {
        Map<String, Object> resMap = new HashMap<>();
        // 数据校验正确的集合
        List<InputInvoiceWhtEntity> entityList = new ArrayList<>();
        // 数据重复的集合
        List<InputInvoiceWhtEntity> duplicateList = new ArrayList<>();
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
                String[] excelHead = {"代扣代缴编号", "公司编码（Legal Entity）", "税额（CNY）", "金额（CNY）", "认证所属期"};
                String [] excelHeadAlias = {"whtCode", "deptCode", "taxPrice", "totalPrice", "authDate"};
                for (int i = 0; i < excelHead.length; i++) {
                    reader.addHeaderAlias(excelHead[i], excelHeadAlias[i]);
                }
                List<InputInvoiceWhtEntity> dataList = reader.read(0, 1, InputInvoiceWhtEntity.class);

                if(CollectionUtils.isEmpty(dataList)){
                    log.error("上传的{}Excel为空,请重新上传", filename);
//                    throw new RRException("上传的Excel为空,请重新上传");
                    continue;
                }
                total += dataList.size();
                int count = 1;
                for (InputInvoiceWhtEntity entity : dataList) {
                    count++;
                    // 参数校验
                    if(1 == checkExcel(entity)){
                        // 参数有误
                        fail += 1;
                        if(!StringUtils.contains(sb.toString(), filename)){
                            sb.append("文件" + filename + "的错误行号为:");
                        }
                        sb.append(count + ",");
                    }else {
                        // 去除Excel中重复数据
                        String repeatData = entity.getWhtCode();
                        if(CollectionUtil.contains(repeatDataList, repeatData)){
                            fail += 1;
                            if(!StringUtils.contains(sb.toString(), filename)){
                                sb.append("文件" + filename + "的错误行号为:");
                            }
                            sb.append(count + ",");
                            continue;
                        }
                        repeatDataList.add(repeatData);
                        // 根据dept_code关联dept_id
                        SysDeptEntity sysDeptEntity = deptService.getByDeptCode(entity.getDeptCode());
                        if(null != sysDeptEntity){
                            entity.setDeptId(sysDeptEntity.getDeptId().intValue());
                            entity.setDeptName(sysDeptEntity.getName());
                        }

                        // 数据库验重
                        InputInvoiceWhtEntity duplicate = super.getOne(
                                new QueryWrapper<InputInvoiceWhtEntity>()
                                        .eq("wht_code", entity.getWhtCode())
                                        .eq("del_flag", "1")
                        );
                        if(null != duplicate){
                            duplicate.setDeptCode(entity.getDeptCode());
                            duplicate.setAuthDate(entity.getAuthDate());
                            duplicate.setTaxPrice(entity.getTaxPrice());
                            duplicate.setTotalPrice(entity.getTotalPrice());
                            duplicate.setUpdateBy(ShiroUtils.getUserId().intValue());
                            duplicate.setUpdateTime(new Date());
                            duplicateList.add(duplicate);
                        }else {
                            entity.setCreateTime(new Date());
                            entity.setCreateBy(ShiroUtils.getUserId().intValue());
                            entity.setDelFlag("1");
                            entityList.add(entity);
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
            log.error("导入代扣代缴单出错: {}", e);
            throw new RRException("导入代扣代缴单出现异常");
        }
    }

    /**
     * 查询列表(数据导出用)
     */
    @Override
    public List<InputInvoiceWhtEntity> queryList(Map<String, Object> params) {
        // 根据id导出
        String idsStr = (String) params.get("ids");
        if(StringUtils.isNotBlank(idsStr)){
            String[] ids = idsStr.split(",");
            return super.list(
                    new QueryWrapper<InputInvoiceWhtEntity>()
                            .in("wht_id", ids)
            );
        }
        // 根据条件查询导出
        String whtCode = (String) params.get("whtCode");
        String deptCode = (String) params.get("deptCode");
        String deptName = (String) params.get("deptName");
        String taxPrice = (String) params.get("taxPrice");
        String totalPrice = (String) params.get("totalPrice");
        String authDate = (String) params.get("authDate");

        return super.list(
                new QueryWrapper<InputInvoiceWhtEntity>()
                        .like(StringUtils.isNotBlank(whtCode), "wht_code", whtCode)
                        .like(StringUtils.isNotBlank(deptCode), "dept_code", deptCode)
                        .eq(StringUtils.isNotBlank(deptName), "dept_name", deptName)
                        .like(null != taxPrice, "tax_price", taxPrice)
                        .like(null != totalPrice, "total_price", totalPrice)
                        .eq(StringUtils.isNotBlank(authDate), "auth_date", authDate)
                        .eq("del_flag", "1")
                        .orderByDesc("update_time")
        );
    }

    /**
     * 校验Excel中参数
     */
    private int checkExcel(InputInvoiceWhtEntity entity){
        // 非空校验
        if(StringUtils.isBlank(entity.getDeptCode()) || StringUtils.isBlank(entity.getWhtCode()) ||
                StringUtils.isBlank(entity.getAuthDate()) || null == entity.getTaxPrice() ||
                null == entity.getTotalPrice()){
            return 1;
        }
        // 认证所属期校验
//        String authDate = entity.getAuthDate();
//        DateTime parse = DateUtil.parse(authDate, "yyyyMM");
//        String now = DateUtil.format(new Date(), "yyyyMM");
//        int compare = DateUtil.compare(parse, DateUtil.parse(now, "yyyyMM"));
//        if(compare < 0){
//            return 1;
//        }

        return 0;
    }
}
