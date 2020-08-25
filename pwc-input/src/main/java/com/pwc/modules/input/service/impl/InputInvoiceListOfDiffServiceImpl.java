package com.pwc.modules.input.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.InputConstant;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;
import com.pwc.modules.input.dao.InputInvoiceListOfDiffDao;
import com.pwc.modules.input.entity.InputInvoiceEntity;
import com.pwc.modules.input.service.InputInvoiceListOfDiffService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;

@Slf4j
@Service
public class InputInvoiceListOfDiffServiceImpl extends ServiceImpl<InputInvoiceListOfDiffDao, InputInvoiceEntity> implements InputInvoiceListOfDiffService {

    /**
     * 入账金额差异清单查询列表
     *
     * @param params params
     * @return pageData
     */
    @Override
    public PageUtils listPage(Map<String, Object> params) {
        //TODO 根据原型页面看时间暂时到月 格式 2020-04 之后又变化再修改
        String queryDate = Convert.toStr(params.get("queryDate"));
        //入账之后状态
        String[] creditInvoiceStatusArr = {
                InputConstant.InvoiceStatus.PENDING_CERTIFIED.getValue(),//待认证
                InputConstant.InvoiceStatus.CERTIFICATION.getValue(),//认证中
                InputConstant.InvoiceStatus.UNDO_CERTIFICATION.getValue(),//撤销认证
                InputConstant.InvoiceStatus.SUCCESSFUL_AUTHENTICATION.getValue(),//认证成功
                InputConstant.InvoiceStatus.AUTHENTICATION_FAILED.getValue(),//认证失败
                InputConstant.InvoiceStatus.FINISHED.getValue(),//完成
        };
        QueryWrapper<InputInvoiceEntity> wrapper = new QueryWrapper<>();
        wrapper.in("invoice_status", Arrays.asList(creditInvoiceStatusArr))
                .likeRight(StrUtil.isNotBlank(queryDate), "invoice_create_date", queryDate);
        IPage<InputInvoiceEntity> result = this.page(
                new Query<InputInvoiceEntity>().getPage(params, null, true), wrapper);
        return new PageUtils(result);
    }

}
