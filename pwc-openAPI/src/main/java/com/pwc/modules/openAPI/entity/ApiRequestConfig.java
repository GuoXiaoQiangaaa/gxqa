package com.pwc.modules.openAPI.entity;



import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("api_request_config")
public class ApiRequestConfig implements Serializable {
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
     *请求服务服务key
     */
    private String companyKey;
    /**
     *状态 0 正常 -1删除
     */
    @TableLogic(value = "0", delval = "-1")
    private Integer status;

}
