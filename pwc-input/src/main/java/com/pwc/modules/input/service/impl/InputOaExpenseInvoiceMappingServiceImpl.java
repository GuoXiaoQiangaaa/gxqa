package com.pwc.modules.input.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.annotation.DataFilter;
import com.pwc.common.utils.Constant;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;
import com.pwc.modules.input.dao.InputOaExpenseInvoiceMappingDao;
import com.pwc.modules.input.entity.InputExpenseInvoiceMappingBean;
import com.pwc.modules.input.entity.InputOaExpenseInfoEntity;
import com.pwc.modules.input.entity.InputOaExpenseInvoiceMappingEntity;
import com.pwc.modules.input.service.InputInvoiceService;
import com.pwc.modules.input.service.InputOaExpenseInfoService;
import com.pwc.modules.input.service.InputOaExpenseInvoiceMappingService;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("oaExpenseInvoiceMappingService")
public class InputOaExpenseInvoiceMappingServiceImpl extends ServiceImpl<InputOaExpenseInvoiceMappingDao, InputOaExpenseInvoiceMappingEntity>
        implements InputOaExpenseInvoiceMappingService {

    @Autowired
    private InputInvoiceService invoiceService;
    @Autowired
    private InputOaExpenseInfoService oaExpenseInfoService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<InputOaExpenseInvoiceMappingEntity> page = this.page(
                new Query<InputOaExpenseInvoiceMappingEntity>().getPage(params, null, true),
                new QueryWrapper<InputOaExpenseInvoiceMappingEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<String> getInformalInvoiceIdsByExpenseNoAndType(String expenseNo, String Type) {
        return this.baseMapper.getInformalInvoiceIdsByExpenseNoAndType(expenseNo, Type);
    }


    @Override
    public List<InputExpenseInvoiceMappingBean> getInvoiceExpenseMappingRelationShip(Integer expenseNo, String sqlFilter) {

        List<InputExpenseInvoiceMappingBean> expenseInvoiceMappingBeanList = new ArrayList<InputExpenseInvoiceMappingBean>();
        expenseInvoiceMappingBeanList = this.baseMapper.getInvoiceExpenseMappingRelationShip(expenseNo,sqlFilter);


        return expenseInvoiceMappingBeanList;

    }

    @Override
    public List<Integer> getInvoiceEntitiesByExpenseNoAndType(Integer expenseNo, String invoiceType) {
        return this.baseMapper.getInvoiceEntitiesByExpenseNoAndType(expenseNo, invoiceType);
    }

    @Override
    public List<Integer> getInvoiceEntitiesByExpenseNoAndTypeAndStatus(Integer expenseNo, List<String> invoiceType, List<Integer> invoiceStatus) {
        return this.baseMapper.getInvoiceEntitiesByExpenseNoAndTypeAndStatus(expenseNo, invoiceType, invoiceStatus);
    }

    @Override
    public InputExpenseInvoiceMappingBean getUnformatInvoiceIdsAmountsByIds(List<Integer> unformatIds,String sqlFilter) {
        return this.baseMapper.getUnformatInvoiceIdsAmountsByIds(unformatIds,sqlFilter);

    }

    @Override
    public InputOaExpenseInvoiceMappingEntity getById(Integer id) {
        return this.baseMapper.getById(id);

    }

    @Override
    public void removeByIds(Integer[] ids) {
        this.baseMapper.removeByIds(ids);
    }

    @Override
    public String getSumAmountOfInvoiceByExpenseNo(List<Integer> ids) {
        return this.baseMapper.getSumAmountOfInvoiceByExpenseNo(ids);
    }

    @Override
    public List<Integer> getInformalInvoiceEntitiesByExpenseNoAndType(Integer expenseNo, String invoiceType) {
        return this.baseMapper.getInformalInvoiceEntitiesByExpenseNoAndType(expenseNo, invoiceType);
    }

    @Override
    public InputExpenseInvoiceMappingBean getInvoicesAmountsAndCountsByIds(List<Integer> invoiceIds,String sqlFilter) {
        return this.baseMapper.getInvoicesAmountsAndCountsByIds(invoiceIds,sqlFilter);
    }

    @Override
    @DataFilter(deptId = "company_id", subDept = true,user = false)
    public List<InputExpenseInvoiceMappingBean> getByExpenseNo(Map<String, Object> params) {
        BigDecimal expenseMoney = new BigDecimal(0.0);
    Integer expenseNo = (Integer)params.get("expenseNo");
        // 替换原来的公司查询数据权限
        String sqlFilter = null;
        if (!StrUtil.isBlankIfStr(params.get(Constant.SQL_FILTER))) {
            sqlFilter = (String) params.get(Constant.SQL_FILTER);
        }
        List<InputExpenseInvoiceMappingBean> expenseInvoiceMappingBeanList = this.getInvoiceExpenseMappingRelationShip(expenseNo, sqlFilter);
        for (InputExpenseInvoiceMappingBean expenseInvoiceMappingBean : expenseInvoiceMappingBeanList) {
            String totalMoney = null;
            if (expenseInvoiceMappingBean.getInvoiceType() == null) {
                continue;
            }

            if (expenseInvoiceMappingBean.getInvoiceType().equals("专用发票") || expenseInvoiceMappingBean.getInvoiceType().equals("普通发票")) {

                Integer countNumber = 0;
                List<Integer> ids = this.getInvoiceEntitiesByExpenseNoAndType(expenseNo, expenseInvoiceMappingBean.getInvoiceType());
                if (CollUtil.isNotEmpty(ids)) {
                    if (this.getInvoicesAmountsAndCountsByIds(ids,sqlFilter).getTotalMoney() == null) {
                        totalMoney = "0";
                    } else {
                        totalMoney = this.getInvoicesAmountsAndCountsByIds(ids,sqlFilter).getTotalMoney().toString();
                    }
                    countNumber = this.getInvoicesAmountsAndCountsByIds(ids, sqlFilter).getCountNumber();
                }
                if (totalMoney == null) {
                    totalMoney = "0";
                }
                expenseInvoiceMappingBean.setCountNumber(countNumber);
                expenseInvoiceMappingBean.setTotalMoney(new BigDecimal(totalMoney));
            } else {
                List<String> unformatInvoiceIds = this.getInformalInvoiceIdsByExpenseNoAndType(expenseNo.toString(), expenseInvoiceMappingBean.getInvoiceType());

                List<Integer> unformatInvoiceIntegerIds = new ArrayList<Integer>();
                for (String unformatInvoiceId : unformatInvoiceIds) {
                    if (StringUtils.isNotBlank(unformatInvoiceId)) {
                        unformatInvoiceIntegerIds.add(Integer.parseInt(unformatInvoiceId));
                    }
                }
                BigDecimal money = new BigDecimal("0");
                if (unformatInvoiceIntegerIds.size() > 0) {
                    money = this.getUnformatInvoiceIdsAmountsByIds(unformatInvoiceIntegerIds,sqlFilter).getTotalMoney();
                }
                if (null != money) {
                    expenseMoney.add(money);
                }
            }
            if (totalMoney == null) {
                totalMoney = "0";
            }
            expenseMoney = expenseMoney.add(new BigDecimal(totalMoney));
            InputOaExpenseInfoEntity oaExpenseInfoEntity = oaExpenseInfoService.getById(expenseNo);
            oaExpenseInfoEntity.setInvoicesAmount(expenseMoney);
            if (expenseMoney.compareTo(oaExpenseInfoEntity.getAmount()) == 1) {
                oaExpenseInfoEntity.setExpenseStatus(3); // 异常 金额超出报销金额
            }

            oaExpenseInfoService.update(oaExpenseInfoEntity);

        }
        return expenseInvoiceMappingBeanList;
    }
}