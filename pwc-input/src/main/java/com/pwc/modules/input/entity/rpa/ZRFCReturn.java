package com.pwc.modules.input.entity.rpa;

public class ZRFCReturn {
    public String ZTYPE;  //消息类型: S 成功,E 错误,W 警告,I 信息,A 中断
    public String ID; //消息类
    public String NUMBER;  //消息编号
    public String MESSAGE; //消息文本

    @Override
    public String toString() {
        return "ZRFCReturn{" +
                "ZTYPE='" + ZTYPE + '\'' +
                ", ID='" + ID + '\'' +
                ", NUMBER='" + NUMBER + '\'' +
                ", MESSAGE='" + MESSAGE + '\'' +
                '}';
    }
}
