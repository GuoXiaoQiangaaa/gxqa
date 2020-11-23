package com.pwc.modules.output.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.exception.RRException;
import com.pwc.modules.output.dao.OutputAlarmRulesDao;
import com.pwc.modules.output.entity.OutputAlarmRulesEntity;
import com.pwc.modules.output.entity.OutputInvoiceInventoryEntity;
import com.pwc.modules.output.service.OutputAlarmRulesService;
import com.pwc.modules.output.service.OutputInvoiceInventoryService;
import com.pwc.modules.sys.shiro.ShiroUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 报警规则服务实现
 *
 * @author fanpf
 * @date 2020/9/25
 */
@Service("outputAlarmRulesService")
@Slf4j
public class OutputAlarmRulesServiceImpl extends ServiceImpl<OutputAlarmRulesDao, OutputAlarmRulesEntity> implements OutputAlarmRulesService {

    @Autowired
    private OutputInvoiceInventoryService inventoryService;

    /**
     * 列表
     */
    @Override
    public List<OutputAlarmRulesEntity> list(Map<String, Object> params) {
        String inventoryId = (String) params.get("inventoryId");
        if(null == inventoryId){
            throw new RRException("发票库存id不能为空");
        }

        List<OutputAlarmRulesEntity> rulesEntityList = super.list(
                new QueryWrapper<OutputAlarmRulesEntity>()
                        .eq("inventory_id", inventoryId)
        );
        return rulesEntityList;
    }

    /**
     * 添加/编辑告警规则
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(List<OutputAlarmRulesEntity> rulesEntityList, Map<String, Object> params) {
        String inventoryId = (String) params.get("inventoryId");
        if(null == inventoryId){
            throw new RRException("发票库存id不能为空");
        }
        OutputInvoiceInventoryEntity inventoryEntity = inventoryService.getById(inventoryId);
        if(null == inventoryEntity){
            throw new RRException("参数有误");
        }

        super.remove(
                new QueryWrapper<OutputAlarmRulesEntity>()
                        .eq("inventory_id", inventoryId)
        );

        if(CollectionUtil.isEmpty(rulesEntityList)){
            throw new RRException("报警规则已被全部清除,请设置报警规则");
        }else {
            // 报警运算不能重复或相近,如:已经有LT不能再添加LT或LTE
            if(rulesEntityList.size() > 2){
                // 报警运算为: LT,LTE,GT,GTE 若规则数量大于2,必定重复或相近
                throw new RRException("报警规则设置重复");
            }
            if(rulesEntityList.size() == 2){
                if(rulesEntityList.get(0).getOperation().contains(rulesEntityList.get(1).getOperation()) ||
                        rulesEntityList.get(1).getOperation().contains(rulesEntityList.get(0).getOperation())){
                    throw new RRException("报警规则设置重复");
                }
            }
            rulesEntityList.forEach(
                    entity -> {
                        String alarmDesc = entity.getAlarmDesc();
                        String operation = entity.getOperation();
                        if("1".equals(alarmDesc)){
                            if("GT".equalsIgnoreCase(operation) || "GTE".equalsIgnoreCase(operation)){
                                throw new RRException("报警状态与报警参数不匹配");
                            }
                        }else if("2".equals(alarmDesc)){
                            if("LT".equalsIgnoreCase(operation) || "LTE".equalsIgnoreCase(operation)){
                                throw new RRException("报警状态与报警参数不匹配");
                            }
                        }

                        entity.setDeptId(inventoryEntity.getDeptId());
                        entity.setCreateBy(ShiroUtils.getUserId());
                        entity.setCreateTime(new Date());
                    }
            );
            super.saveBatch(rulesEntityList);
        }

        // 根据报警规则设置库存状态
        Integer inventory = inventoryEntity.getInventory();
        int status = this.setInventoryStatus(rulesEntityList, inventory);
        if(inventoryEntity.getStatus() == status){
            return;
        }
        inventoryEntity.setStatus(status);
        inventoryEntity.setUpdateBy(ShiroUtils.getUserId());
        inventoryEntity.setUpdateTime(new Date());
        inventoryService.updateById(inventoryEntity);

    }

    /**
     * 根据库存告警设置库存状态
     * 库存状态: 1:正常; 2:不足; 3:过多
     */
    private int setInventoryStatus(List<OutputAlarmRulesEntity> rulesEntityList, Integer inventory){
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
        return 1;
    }
}
