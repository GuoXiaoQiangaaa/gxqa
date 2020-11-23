package com.pwc.modules.output.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.exception.RRException;
import com.pwc.modules.output.dao.OutputInvoiceInventoryDao;
import com.pwc.modules.output.entity.OutputAlarmRulesEntity;
import com.pwc.modules.output.entity.OutputInvoiceInventoryEntity;
import com.pwc.modules.output.service.OutputAlarmRulesService;
import com.pwc.modules.output.service.OutputInvoiceInventoryService;
import com.pwc.modules.sys.entity.SysDeptEntity;
import com.pwc.modules.sys.service.SysDeptService;
import com.pwc.modules.sys.shiro.ShiroUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 发票库存服务实现
 *
 * @author fanpf
 * @date 2020/9/25
 */
@Service("outputInvoiceInventoryService")
@Slf4j
public class OutputInvoiceInventoryServiceImpl extends ServiceImpl<OutputInvoiceInventoryDao, OutputInvoiceInventoryEntity> implements OutputInvoiceInventoryService {

    @Autowired
    private OutputAlarmRulesService rulesService;
    @Autowired
    private SysDeptService deptService;

    /**
     * 列表
     */
    @Override
    public List<OutputInvoiceInventoryEntity> list(Map<String, Object> params) {
        String invoiceType = (String) params.get("invoiceType");
        String status = (String) params.get("status");
        String deptId = (String) params.get("deptId");
        if(StringUtils.isBlank(deptId)){
            deptId = ShiroUtils.getUserEntity().getDeptId().toString();
        }
        SysDeptEntity deptEntity = deptService.getById(deptId);

        List<OutputInvoiceInventoryEntity> list = super.list(
                new QueryWrapper<OutputInvoiceInventoryEntity>()
                        .eq("dept_id", deptId)
                        .eq(StringUtils.isNotBlank(invoiceType), "invoice_type", invoiceType)
                        .eq(StringUtils.isNotBlank(status), "status", status)
        );

        // 设置报警条件
        if(CollectionUtil.isNotEmpty(list)){
            for (OutputInvoiceInventoryEntity entity : list) {
                entity.setDeptName(deptEntity.getName());
                entity.setTaxCode(deptEntity.getTaxCode());
                List<OutputAlarmRulesEntity> rulesEntityList = rulesService.list(
                        new QueryWrapper<OutputAlarmRulesEntity>()
                                .eq("inventory_id", entity.getInventoryId())
                );
                if(CollectionUtil.isNotEmpty(rulesEntityList)){
                    StringBuilder alarmCondition = new StringBuilder();
                    int count = 0;
                    for (OutputAlarmRulesEntity rulesEntity : rulesEntityList) {
                        count++;
                        String operation = rulesEntity.getOperation();
                        String quantity = rulesEntity.getQuantity();
                        String alarmDesc = rulesEntity.getAlarmDesc();
                        if("1".equals(alarmDesc)){
                            alarmDesc = "库存不足";
                        }else if("2".equals(alarmDesc)){
                            alarmDesc = "库存过多";
                        }
                        alarmCondition.append(operation).append(quantity).append(" ").append(alarmDesc);
                        if(1 == count && 1 < rulesEntityList.size()){
                            alarmCondition.append("|");
                        }
                    }
                    entity.setAlarmCondition(alarmCondition.toString());
                }
            }
        }
        return list;
    }

    /**
     * 人工录入
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void manmade(List<OutputInvoiceInventoryEntity> entityList, Map<String, Object> params) {
        if(CollectionUtil.isEmpty(entityList)){
            return;
        }
        // 校验是否有库存记录
        String deptId = (String) params.get("deptId");
        if(StringUtils.isBlank(deptId)){
            deptId = ShiroUtils.getUserEntity().getDeptId().toString();
        }

        List<OutputInvoiceInventoryEntity> inventoryList = super.list(
                new QueryWrapper<OutputInvoiceInventoryEntity>()
                        .eq("dept_id", deptId)
        );

        for (OutputInvoiceInventoryEntity entity : entityList) {
            if(0 < entity.getInputNum()){ // 当期有购进发票
                // 根据发票号码计算发票数量与当期购进发票数量进行比对
                if(Long.valueOf(entity.getInvoiceNumEnd()) - Long.valueOf(entity.getInvoiceNumBegin()) != entity.getInputNum()){
                    log.error("发票数量与结算结果不一致");
                    throw new RRException("发票数量有误");
                }

                if(CollectionUtil.isEmpty(inventoryList)){ // 无记录
                    entity.setOutputNum(0);
                    entity.setInventory(entity.getInputNum());
                    // 库存状态: 1:正常; 2:不足; 3:过多
                    entity.setStatus(1);
                    entity.setCreateBy(ShiroUtils.getUserId());
                    entity.setCreateTime(new Date());
                    entity.setDeptId(Long.valueOf(deptId));
                    super.save(entity);
                }else { // 有记录
                    // 记录数据库中是否有该类型的发票
                    boolean isExit = false;
                    for (OutputInvoiceInventoryEntity inventoryEntity : inventoryList) {
                        if(inventoryEntity.getInvoiceType().equals(entity.getInvoiceType())){
                            isExit = true;
                            entity.setOutputNum(0);
                            inventoryEntity.setInputNum(entity.getInputNum());
                            int inventory = inventoryEntity.getInventory() + entity.getInputNum();
                            inventoryEntity.setInventory(inventory);
                            inventoryEntity.setStatus(this.setInventoryStatus(inventoryEntity.getInventoryId(), inventory));
                            inventoryEntity.setUpdateBy(ShiroUtils.getUserId());
                            inventoryEntity.setUpdateTime(new Date());
                            inventoryEntity.setInvoiceCodeBegin(entity.getInvoiceCodeBegin());
                            inventoryEntity.setInvoiceCodeEnd(entity.getInvoiceCodeEnd());
                            inventoryEntity.setInvoiceNumBegin(entity.getInvoiceNumBegin());
                            inventoryEntity.setInvoiceNumEnd(entity.getInvoiceNumEnd());
                            super.updateById(inventoryEntity);
                        }
                    }
                    if(!isExit){ // 数据库中没有该类型的发票,表示新增的类型
                        entity.setOutputNum(0);
                        entity.setInventory(entity.getInputNum());
                        // 库存状态: 1:正常; 2:不足; 3:过多
                        entity.setStatus(1);
                        entity.setCreateBy(ShiroUtils.getUserId());
                        entity.setCreateTime(new Date());
                        entity.setDeptId(Long.valueOf(deptId));
                        super.save(entity);
                    }
                }
            }
        }
    }

    /**
     * 编辑
     */
    @Override
    public void update(OutputInvoiceInventoryEntity entity) {
        // 设置库存状态
        int status = this.setInventoryStatus(entity.getInventoryId(), entity.getInventory());
        entity.setStatus(status);
        entity.setUpdateBy(ShiroUtils.getUserId());
        entity.setUpdateTime(new Date());
        super.updateById(entity);

    }

    /**
     * 查询发票库存是否低于临界值
     * true:低于; false:高于
     */
    @Override
    public boolean isUnderCrisis() {
        Long deptId = ShiroUtils.getUserEntity().getDeptId();
        List<OutputInvoiceInventoryEntity> inventoryList = super.list(
                new QueryWrapper<OutputInvoiceInventoryEntity>()
                        .eq("dept_id", deptId)
        );
        if(CollectionUtil.isEmpty(inventoryList)){
            return false;
        }
        for (OutputInvoiceInventoryEntity inventoryEntity : inventoryList) {
            if(2 == inventoryEntity.getStatus()){
                return true;
            }
        }
        return false;
    }

    /**
     * 根据库存告警设置库存状态
     * 库存状态: 1:正常; 2:不足; 3:过多
     */
    private int setInventoryStatus(Long inventoryId, int inventory){
        List<OutputAlarmRulesEntity> rulesEntityList = rulesService.list(
                new QueryWrapper<OutputAlarmRulesEntity>()
                        .eq("inventory_id", inventoryId)
        );

        if(CollectionUtil.isEmpty(rulesEntityList)){
            return 1;
        }else {
            for (OutputAlarmRulesEntity rulesEntity : rulesEntityList) {
                // 告警数量
                Integer quantity = Integer.valueOf(rulesEntity.getQuantity());
                // 库存与告警数量比较
                if("LT".equalsIgnoreCase(rulesEntity.getOperation())){
                    if(inventory < quantity){
                        return 2;
                    }
                }else if("LTE".equalsIgnoreCase(rulesEntity.getOperation())){
                    if(inventory <= quantity){
                        return 2;
                    }
                }else if("GT".equalsIgnoreCase(rulesEntity.getOperation())){
                    if(inventory > quantity){
                        return 3;
                    }
                }else if("GTE".equalsIgnoreCase(rulesEntity.getOperation())){
                    if(inventory >= quantity){
                        return 3;
                    }
                }
            }
        }
        return 1;
    }
}
