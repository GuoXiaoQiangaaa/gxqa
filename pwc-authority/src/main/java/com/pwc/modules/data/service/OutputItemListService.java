package com.pwc.modules.data.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pwc.common.utils.PageUtils;
import com.pwc.modules.data.entity.OutputItemListEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 科目清单服务
 *
 * @author fanpf
 * @date 2020/8/27
 */
public interface OutputItemListService extends IService<OutputItemListEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /** 添加 */
    boolean save(OutputItemListEntity outputItemList);

    boolean updateById(OutputItemListEntity outputItemList);

    /**
     * 禁用/启用
     */
    void disableOrEnable(OutputItemListEntity reqVo);

    /**
     * 关键字查询
     */
    PageUtils search(Map<String, Object> params);

    /**
     * 数据导入
     */
    Map<String, Object> importItem(MultipartFile[] files);
}

