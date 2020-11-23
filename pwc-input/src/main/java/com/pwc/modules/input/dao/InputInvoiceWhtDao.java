package com.pwc.modules.input.dao;

import com.pwc.modules.input.entity.InputInvoiceWhtEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * WHT(代扣代缴单据)持久层
 *
 * @author fanpf
 * @date 2020/9/4
 */
@Mapper
public interface InputInvoiceWhtDao extends BaseMapper<InputInvoiceWhtEntity> {

    int remove(@Param("ids")String[] ids);
	
}
