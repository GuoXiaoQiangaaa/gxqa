package com.pwc.modules.input.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pwc.modules.input.entity.InputCompanyEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2018-12-13 09:31:08
 */
@Mapper
public interface InputCompanyDao extends BaseMapper<InputCompanyEntity> {
    InputCompanyEntity getListByParagraph(InputCompanyEntity companyEntity);

    InputCompanyEntity get(InputCompanyEntity companyEntity);

    List<String> getNumberList();

    List<InputCompanyEntity> getCompanyNumberList();

    /**
     * 根据公司编号获取
     *
     * @param companyNumber
     * @return
     */
    InputCompanyEntity getByCompanyNumber(String companyNumber);

    /**
     * 根据公司名称获取
     *
     * @param companyName
     * @return
     */
    InputCompanyEntity getByName(String companyName);

    /**
     * 批量获取公司信息
     *
     * @param companyNumbers
     * @return
     */
    List<InputCompanyEntity> getListByCompanyNumber(@Param("list") List<String> companyNumbers);

    List<InputCompanyEntity> getList();

    Integer getCount(InputCompanyEntity companyEntity);

    void insertCompanyAll(List<InputCompanyEntity> list);

    InputCompanyEntity getByNsrsbh(String nsrsbh);

    void update(InputCompanyEntity companyEntity);



}
