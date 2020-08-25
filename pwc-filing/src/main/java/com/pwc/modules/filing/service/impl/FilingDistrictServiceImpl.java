package com.pwc.modules.filing.service.impl;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;

import com.pwc.modules.filing.dao.FilingDistrictDao;
import com.pwc.modules.filing.entity.FilingDistrictEntity;
import com.pwc.modules.filing.service.FilingDistrictService;


/**
 * @author zk
 */
@Service("filingDistrictService")
public class FilingDistrictServiceImpl extends ServiceImpl<FilingDistrictDao, FilingDistrictEntity> implements FilingDistrictService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<FilingDistrictEntity> page = this.page(
                new Query<FilingDistrictEntity>().getPage(params),
                new QueryWrapper<FilingDistrictEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<FilingDistrictEntity> getByCityCode(String cityCode) {
        return super.list(new QueryWrapper<FilingDistrictEntity>().eq("city_code", cityCode));
    }

    @Override
    public List<FilingDistrictEntity> getSubDistrictList(Long districtId) {
        List<FilingDistrictEntity> list = queryDistrictList(districtId);
        getDistrictTreeList(list);
        return list;
    }


    @Override
    public void saveDistrict(List<FilingDistrictEntity> districtList) {
        baseMapper.delete(new QueryWrapper<FilingDistrictEntity>());
        recursiveSave(districtList, 0L);
    }


    private List<FilingDistrictEntity> queryDistrictList(long districtId) {
        return baseMapper.selectList(new QueryWrapper<FilingDistrictEntity>().eq("parent_id", districtId));
    }

    /**
     * 递归保存
     * @param list
     */
    private void recursiveSave(List<FilingDistrictEntity> list, Long parentId) {
        if (CollUtil.isNotEmpty(list)) {
            for (FilingDistrictEntity district:list) {
                district.setParentId(parentId);
                super.save(district);
                if (CollUtil.isNotEmpty(district.getChildren())) {
                    recursiveSave(district.getChildren(), district.getDistrictId());
                }
            }
        }

    }
    /**
     * 递归查询
     */
    private void getDistrictTreeList(List<FilingDistrictEntity> subList){
        for(FilingDistrictEntity district : subList){
            List<FilingDistrictEntity> list = queryDistrictList(district.getDistrictId());
            district.setChildren(list);
            if(list.size() > 0){
                getDistrictTreeList(list);
            }
        }
    }
}
