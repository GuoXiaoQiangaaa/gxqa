package com.pwc.modules.sys.service.impl;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pwc.common.utils.PageUtils;
import com.pwc.common.utils.Query;

import com.pwc.modules.sys.dao.SysDistrictDao;
import com.pwc.modules.sys.entity.SysDistrictEntity;
import com.pwc.modules.sys.service.SysDistrictService;


/**
 * @author zk
 */
@Service("sysDistrictService")
public class SysDistrictServiceImpl extends ServiceImpl<SysDistrictDao, SysDistrictEntity> implements SysDistrictService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SysDistrictEntity> page = this.page(
                new Query<SysDistrictEntity>().getPage(params),
                new QueryWrapper<SysDistrictEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<SysDistrictEntity> queryList(Map<String, Object> map) {
        return baseMapper.queryList(map);
    }

    @Override
    public List<SysDistrictEntity> queryDistrictList(Long parentId) {
        return baseMapper.queryDistrictList(parentId);
    }

    @Override
    public List<SysDistrictEntity> getSubDistrictList(Long districtId) {

        //获取子
        List<SysDistrictEntity> subList = queryDistrictList(districtId);
        getDistrictTreeList(subList);

        return subList;
    }

    @Override
    public SysDistrictEntity queryById(Long id) {
        return baseMapper.queryById(id);
    }

    /**
     * 递归
     */
    private void getDistrictTreeList(List<SysDistrictEntity> subList){
        for(SysDistrictEntity district : subList){
            List<SysDistrictEntity> list = queryDistrictList(district.getId().longValue());
            district.setChildren(list);
            if(list.size() > 0){
                getDistrictTreeList(list);
            }
        }
    }
}
