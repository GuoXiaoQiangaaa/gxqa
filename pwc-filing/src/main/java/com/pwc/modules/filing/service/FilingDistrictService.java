package com.pwc.modules.filing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.filing.entity.FilingDistrictEntity;

import java.util.List;
import java.util.Map;

/**
 * 地区设置
 *
 * @author zk
 * @email 
 * @date 2019-11-13 18:47:17
 */
public interface FilingDistrictService extends IService<FilingDistrictEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 根据城市编码查询
     * @param cityCode
     * @return
     */
    List<FilingDistrictEntity> getByCityCode(String cityCode);

    /**
     * 获取子
     */
    List<FilingDistrictEntity> getSubDistrictList(Long districtId);

    /**
     * 设置节点
     * @param districtList
     * @return
     */
    void saveDistrict(List<FilingDistrictEntity> districtList);
}

