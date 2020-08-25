package com.pwc.modules.input.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.modules.input.dao.InputSapInvoiceMappingIdDao;
import com.pwc.modules.input.entity.InputSapInvoiceMappingIdEntity;
import com.pwc.modules.input.service.InputSapInvoiceMappingIdService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("iapInvoiceMappingIdService")
public class InputSapInvoiceMappingIdServiceImpl extends ServiceImpl<InputSapInvoiceMappingIdDao, InputSapInvoiceMappingIdEntity> implements InputSapInvoiceMappingIdService {

    @Override
    public InputSapInvoiceMappingIdEntity getOneBySapId(InputSapInvoiceMappingIdEntity sapInvoiceMappingIdEntity) {
        return this.baseMapper.getOneBySapId(sapInvoiceMappingIdEntity);
    }


    @Override
    public void update(InputSapInvoiceMappingIdEntity sapInvoiceMappingIdEntity) {
        this.baseMapper.update(sapInvoiceMappingIdEntity);
    }

    @Override
    public List<InputSapInvoiceMappingIdEntity> getListByTaxOrName(InputSapInvoiceMappingIdEntity sapInvoiceMappingIdEntity) {
        return this.baseMapper.getListByTaxOrName(sapInvoiceMappingIdEntity);
    }
}
