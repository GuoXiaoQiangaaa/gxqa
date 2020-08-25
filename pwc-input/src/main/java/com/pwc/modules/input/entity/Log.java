package com.pwc.modules.input.entity;

import java.io.Serializable;

/**
 * @author Administrator
 * 日志
 */
public class Log implements Serializable {
    private Integer id;
    private String interfaceName;
    private String inParam;
    private String outParam;
    private String time;
    private String requestId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getInParam() {
        return inParam;
    }

    public void setInParam(String inParam) {
        this.inParam = inParam;
    }

    public String getOutParam() {
        return outParam;
    }

    public void setOutParam(String outParam) {
        this.outParam = outParam;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
