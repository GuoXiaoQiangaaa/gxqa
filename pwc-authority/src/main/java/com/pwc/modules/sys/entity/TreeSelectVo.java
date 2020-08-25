package com.pwc.modules.sys.entity;

import lombok.Data;

import java.util.List;

/**
 * @author zk
 */
@Data
public class TreeSelectVo {
    private String title;
    private String key;
    private List<TreeSelectVo> children;
}
