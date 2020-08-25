package com.pwc.modules.filing.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;

import com.pwc.modules.filing.dao.FilingRecordFileDao;
import com.pwc.modules.filing.entity.FilingRecordFileEntity;
import com.pwc.modules.filing.service.FilingRecordFileService;


/**
 * @author zk
 */
@Service("filingRecordFileService")
public class FilingRecordFileServiceImpl extends ServiceImpl<FilingRecordFileDao, FilingRecordFileEntity> implements FilingRecordFileService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<FilingRecordFileEntity> page = this.page(
                new Query<FilingRecordFileEntity>().getPage(params),
                new QueryWrapper<FilingRecordFileEntity>()
        );

        return new PageUtils(page);
    }

}
