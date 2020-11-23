package com.pwc.modules.input.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.input.entity.InputInvoiceWhtEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * WHT(代扣代缴单据)服务
 *
 * @author fanpf
 * @date 2020/9/4
 */
public interface InputInvoiceWhtService extends IService<InputInvoiceWhtEntity> {

    /**
     * 分页查询列表
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 编辑
     */
    boolean updateById(InputInvoiceWhtEntity entity);

    /**
     * 逻辑删除
     */
    void remove(Map<String, Object> params);

    /**
     * 数据导入
     */
    Map<String, Object> importWht(MultipartFile[] files);

    /**
     * 查询列表
     */
    List<InputInvoiceWhtEntity> queryList(Map<String, Object> params);
}

