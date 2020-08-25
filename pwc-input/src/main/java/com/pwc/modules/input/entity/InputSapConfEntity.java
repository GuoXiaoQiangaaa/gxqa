package com.pwc.modules.input.entity;


import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

@TableName("input_sap_conf")
public class InputSapConfEntity implements Serializable {
    private String name;

    private Integer id;

    private String ashost;

    private String client;

    private String lang;

    private String passWord;

    private String sysnr;

    private String user;

    private String postUrl;

    public String getPostUrl() {
        return postUrl;
    }

    public void setPostUrl(String postUrl) {
        this.postUrl = postUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAshost() {
        return ashost;
    }

    public void setAshost(String ashost) {
        this.ashost = ashost;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getSysnr() {
        return sysnr;
    }

    public void setSysnr(String sysnr) {
        this.sysnr = sysnr;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
