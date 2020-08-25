package com.pwc.modules.input.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;
import com.pwc.modules.input.dao.InputTransOutCategoryDao;
import com.pwc.modules.input.entity.InputInvoiceResponsibleEntity;
import com.pwc.modules.input.entity.InputTansOutCategoryEntity;
import com.pwc.modules.input.service.InputTransOutCategoryService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("InputTransOutCategoryService")
public class InputTransOutCategoryServiceImpl extends ServiceImpl<InputTransOutCategoryDao, InputTansOutCategoryEntity>
    implements InputTransOutCategoryService {

    @Override
    public List<InputTansOutCategoryEntity> getList(){
        return this.baseMapper.getList();


    }

    @Override
    public void inputTransOutCategoryDelete(Integer[] ints) {
        this.baseMapper.inputTransOutCategoryDelete(ints);
    }

    @Override
    public PageUtils findList(Map<String, Object> params) {
        String category = (String) params.get("category");
        String item = (String) params.get("item");
        IPage<InputTansOutCategoryEntity> page = this.page(
                new Query<InputTansOutCategoryEntity>().getPage(params, null, true),
                new QueryWrapper<InputTansOutCategoryEntity>()
                        .like(StringUtils.isNotBlank(category), "category", category)
                        .like(StringUtils.isNotBlank(item),"item",item)
                        //.eq("status",0)
        );
        return new PageUtils(page);
    }

    ;

}
