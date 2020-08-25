package com.pwc.common.utils;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 *
 * @author zk
 */
@Data
public class UploadFileEntity {

    /** 文件ID */
    private Integer id;
    /** 文件原名称 */
    private String origName;
    /** 文件新名称 */
    private String newName;
    /** 文件上传后服务器访问地址 */
    private String serverPath;
    /** 文件大小 */
    private String fileSize;
    /** 文件MD5 */
    private String fileMd5;
    /** 文件上传时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date uploadTime;

    @Override
    public String toString() {
        return "UploadFileEntity{" +
                "id='" + id + '\'' +
                ", origName='" + origName + '\'' +
                ", newName='" + newName + '\'' +
                ", serverPath='" + serverPath + '\'' +
                ", fileSize=" + fileSize +
                ", fileMd5='" + fileMd5 + '\'' +
                ", uploadTime=" + uploadTime +
                '}';
    }
}
