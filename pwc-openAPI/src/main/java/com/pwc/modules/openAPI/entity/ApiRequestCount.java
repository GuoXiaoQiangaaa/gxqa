package com.pwc.modules.openAPI.entity;



import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("api_request_count")
public class ApiRequestCount implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *id
     */
    @TableId
    private Integer id;
    /**
     *companyId
     */
    private Integer companyId;
    /**
     *请求服务名称
     */
    private String type;
    /**
     *请求服务服务次数
     */
    private Integer count;

}
