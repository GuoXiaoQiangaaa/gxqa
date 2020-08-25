package com.pwc.modules.sys.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ClassUtil;
import com.pwc.common.utils.DownloadKit;
import com.pwc.common.utils.R;
import com.pwc.common.utils.UploadFileEntity;
import com.pwc.common.utils.UploadKitUtil;
import com.pwc.modules.sys.entity.SysFileEntity;
import com.pwc.modules.sys.service.SysFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName fileController
 * @Description
 * @author zk
 */

@RestController
@RequestMapping(value = "sys/file")
public class SysFileController {

    @Autowired
    SysFileService sysFileService;
    /**
     * 上传
     * @param file
     * @param request
     */
    @PostMapping(value = "/upload" ,consumes = "multipart/*",headers="content-type=multipart/form-data")
    public R fileUpload(MultipartFile file , HttpServletRequest request) {
        String uploadUrl = ClassUtil.getClassPath()+ "statics/upload/";
        UploadFileEntity uploadFile = UploadKitUtil.uploadFile(file, uploadUrl, true, true);
        SysFileEntity fileEntity = new SysFileEntity();
        BeanUtil.copyProperties(uploadFile,fileEntity);
        sysFileService.save(fileEntity);
        return R.ok().put("file", fileEntity);
    }

    /**
     * 下载
     * @param response
     */
    @GetMapping(value = "/download")
    public void fileDownLoad(Long fileId, HttpServletResponse response) {
        SysFileEntity fileEntity = sysFileService.getById(fileId);
        try {
            DownloadKit.download(response,fileEntity.getServerPath(),fileEntity.getOrigName());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
