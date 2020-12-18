package com.pwc.modules.input.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.annotation.DataFilter;
import com.pwc.common.utils.Constant;
import com.pwc.common.utils.InputConstant;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;
import com.pwc.modules.input.dao.InputOaExpenseInfoDao;
import com.pwc.modules.input.entity.InputOaExpenseInfoEntity;
import com.pwc.modules.input.service.InputInvoiceService;
import com.pwc.modules.input.service.InputOaExpenseInfoService;
import com.pwc.modules.input.service.InputOaExpenseInvoiceMappingService;
import com.pwc.modules.input.service.InputUnformatInvoiceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;


@Service("oaExpenseInfoService")
public class InputOaExpenseInfoServiceImpl extends ServiceImpl<InputOaExpenseInfoDao, InputOaExpenseInfoEntity>
        implements InputOaExpenseInfoService {

    @Autowired
    InputOaExpenseInvoiceMappingService oaExpenseInvoiceMappingService;
    @Autowired
    InputUnformatInvoiceService unformatInvoiceService;
    @Autowired
    InputInvoiceService invoiceService;


    @Override
    @DataFilter(subDept = true, user = false, deptId = "company_id")
    public PageUtils queryPage(Map<String, Object> params) {
        // 报销单号
        String expenseNumber = (String) params.get("expenseNumber");
        // 报销单状态
        String expenseStatus = (String) params.get("expenseStatus");
        // 最小金额
        String minAmount = (String) params.get("minAmount");
        // 最大金额
        String maxAmount = (String) params.get("maxAmount");
        // 申请人
        String applyUser = (String) params.get("applyUser");
        // 报销人
        String endUser = (String) params.get("endUser");
        // 申请者
        String applyTime = (String) params.get("applyTime");
        // 对应不同菜单展示不同类型
        String menuType = (String) params.get("menuType");
//        String statusSql = null;
//        if (StrUtil.isNotBlank(menuType)) {
//            // 1.管理 2.验真 3.三单匹配 4.入账 5.异常
//            if (InputConstant.MenuType.CHECK_TRUE.getValue().equals(menuType)) {
//                statusSql = "expense_status in (3,4)";
//            } else if (InputConstant.MenuType.ENTER.getValue().equals(menuType)) {
//                statusSql = "expense_status in (7,8)";
//            } else if (InputConstant.MenuType.ABNORMAL.getValue().equals(menuType)) {
//                statusSql = "expense_status in (-1, -2, -3, -4, -5)";
//            } else if (InputConstant.MenuType.MANAGE.getValue().equals(menuType)) {
//                if (StrUtil.isNotBlank(expenseStatus)) {
//                    statusSql = "expense_status = " + expenseStatus;
//                }
//            }
//        }


        String applyTimeBegin = "", applyTimeEnd = "";
        if (StringUtils.isNotBlank(applyTime)) {
            String[] time = applyTime.split(",");
            applyTimeBegin = time[0] + " 00:00";
            applyTimeEnd = time[1] + " 23:59";
        }

        IPage<InputOaExpenseInfoEntity> page = this.page(
                new Query<InputOaExpenseInfoEntity>().getPage(params, null, true),
                new QueryWrapper<InputOaExpenseInfoEntity>()
                        .like(StringUtils.isNotBlank(expenseNumber), "expense_number", expenseNumber)
//                        .apply(StrUtil.isNotBlank(statusSql), statusSql)
//                        .eq(StringUtils.isNotBlank(expenseStatus),"expense_status", expenseStatus)
                        .eq(StringUtils.isNotBlank(applyUser), "apply_user", applyUser)
                        .eq(StringUtils.isNotBlank(endUser), "end_user", endUser)
                        .gt(StringUtils.isNotBlank(applyTimeBegin), "apply_time", applyTimeBegin)
                        .lt(StringUtils.isNotBlank(applyTimeEnd), "apply_time", applyTimeEnd)
                        .between(StringUtils.isNotBlank(maxAmount) && StringUtils.isNotBlank(minAmount), "amount", minAmount, maxAmount)
                        .apply(params.get(Constant.SQL_FILTER) != null, (String)params.get(Constant.SQL_FILTER))
        );
        if (InputConstant.MenuType.MANAGE.getValue().equals(menuType)) {
            for (InputOaExpenseInfoEntity expenseInfo : page.getRecords()) {
                Map<String, Object> param = new HashMap<>();
                param.put("expenseNo", expenseInfo.getId());
                expenseInfo.setExpenseInvoiceMappingBeanList(oaExpenseInvoiceMappingService.getByExpenseNo(param));
            }
        } else {
            for (InputOaExpenseInfoEntity expenseInfo : page.getRecords()) {
                List<Integer> invoiceStatusSql = new ArrayList<>();
                if (StrUtil.isNotBlank(menuType)) {
                    // 1.管理 2.验真 3.三单匹配 4.入账 5.异常
                    if (InputConstant.MenuType.CHECK_TRUE.getValue().equals(menuType)) {
                        invoiceStatusSql.add(3);
                        invoiceStatusSql.add(4);
                    } else if (InputConstant.MenuType.ENTER.getValue().equals(menuType)) {
                        invoiceStatusSql.add(7);
                        invoiceStatusSql.add(8);
                    } else if (InputConstant.MenuType.ABNORMAL.getValue().equals(menuType)) {
                        invoiceStatusSql.add(-1);
                        invoiceStatusSql.add(-2);
                        invoiceStatusSql.add(-3);
                        invoiceStatusSql.add(-4);
                        invoiceStatusSql.add(-5);
                    } else if (InputConstant.MenuType.VERIFY.getValue().equals(menuType)){
                        invoiceStatusSql.add(9);
                        invoiceStatusSql.add(10);
                        invoiceStatusSql.add(12);
                        invoiceStatusSql.add(13);
                    }
                }
                List<String> types = new ArrayList<String>();
                if (InputConstant.MenuType.VERIFY.getValue().equals(menuType)){
                    types.add("专用发票");
                }else{
                    types.add("专用发票");
                    types.add("普通发票");
                }
                List<Integer> ids = oaExpenseInvoiceMappingService.getInvoiceEntitiesByExpenseNoAndTypeAndStatus(expenseInfo.getId(), types, invoiceStatusSql);
                expenseInfo.setInvoiceList(invoiceService.getByInvoiceIds(ids));
            }
        }


        return new PageUtils(page);
    }

    @Override
    public InputOaExpenseInfoEntity getById(Integer id) {

        return this.baseMapper.getById(id);


    }

    @Override
    public void removeByIds(Integer[] ids) {
        List<Integer> idlist = Arrays.asList(ids);
        this.baseMapper.removeByIds(idlist);
    }


    @Override
    public int update(InputOaExpenseInfoEntity oaExpenseInfoEntity) {
        String amount = "0";
        List<Integer> invoiceIds = oaExpenseInvoiceMappingService.getInvoiceEntitiesByExpenseNoAndType(oaExpenseInfoEntity.getId(), oaExpenseInfoEntity.getExpenseType());
        if (invoiceIds.size() == 0) {
            oaExpenseInfoEntity.setAmount(new BigDecimal("0"));
        } else {

            try {
                amount = oaExpenseInvoiceMappingService.getInvoicesAmountsAndCountsByIds(invoiceIds,"").getTotalMoney().toString();
            } catch (Exception e) {
                amount = "0";

            }
            oaExpenseInfoEntity.setAmount(new BigDecimal(amount));
        }
        return this.baseMapper.update(oaExpenseInfoEntity);

    }


}