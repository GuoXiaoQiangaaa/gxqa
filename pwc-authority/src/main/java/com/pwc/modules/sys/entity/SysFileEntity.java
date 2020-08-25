package com.pwc.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 *
 * @author zk
 */
@Data
@TableName("sys_file")
public class SysFileEntity {

    /** 文件ID */
    @TableId
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
    /**
     * 税种
     */
    @TableField(exist = false)
    private String taxType;
    /**
     * 税种关联表id
     */
    @TableField(exist = false)
    private Long filingRecordFileId;

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
