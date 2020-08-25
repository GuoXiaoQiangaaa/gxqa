package com.pwc.modules.input.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.annotation.DataFilter;
import com.pwc.common.utils.Constant;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;
import com.pwc.modules.input.dao.InputUnformatInvoiceDao;
import com.pwc.modules.input.entity.InputUnformatInvoiceEntity;
import com.pwc.modules.input.service.InputUnformatInvoiceService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("unformatInvoiceService")
public class InputUnformatInvoiceServiceImpl extends ServiceImpl<InputUnformatInvoiceDao, InputUnformatInvoiceEntity>
    implements
        InputUnformatInvoiceService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<InputUnformatInvoiceEntity> page = this.page(
            new Query<InputUnformatInvoiceEntity>().getPage(params, null, true),
            new QueryWrapper<InputUnformatInvoiceEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public boolean save(InputUnformatInvoiceEntity unformatInvoiceEntity){
//        String issueDate = unformatInvoiceEntity.getIssueDate().replace("T"," ").replace("Z","");
//        unformatInvoiceEntity.setIssueDate(issueDate);
        return this.baseMapper.save(unformatInvoiceEntity) > 0;
    }
    @Override
    public InputUnformatInvoiceEntity getById(Integer id){
        return this.baseMapper.getById(id);

    }
    @Override
    public void removeByIds(Integer[] ids){
        this.baseMapper.removeByIds(ids);
    }


    @Override
    @DataFilter(deptId = "company_id", subDept = true,user = false)
    public PageUtils findListByIds(Map<String, Object> params, List<Integer> ids) {


        if(ids.size()>0){

            IPage<InputUnformatInvoiceEntity> page = this.page(
                new Query<InputUnformatInvoiceEntity>().getPage(params, null, true),
                new QueryWrapper<InputUnformatInvoiceEntity>()
                    .eq("unformat_delete", "0")
                    .in("id", ids)
                        .apply(params.get(Constant.SQL_FILTER) != null, (String)params.get(Constant.SQL_FILTER))

            );
            return new PageUtils(page);

        }
        else{

            IPage<InputUnformatInvoiceEntity> page = this.page(
                new Query<InputUnformatInvoiceEntity>().getPage(params, null, true),
                new QueryWrapper<InputUnformatInvoiceEntity>()
                    .eq("unformat_delete", "5")
                        .apply(params.get(Constant.SQL_FILTER) != null, (String)params.get(Constant.SQL_FILTER)));
            return new PageUtils(page);


        }
    }
}


