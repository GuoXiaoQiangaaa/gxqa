package com.pwc.modules.input.controller;

import com.github.tobato.fastdfs.domain.MataData;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.google.gson.Gson;
import com.pwc.common.utils.OCRUtils;
import com.pwc.common.utils.R;
import com.pwc.common.utils.UploadFileEntity;
import com.pwc.common.utils.UploadKitUtil;
import com.pwc.common.utils.excel.ExportExcel;
import com.pwc.common.utils.excel.ImportExcel;
import com.pwc.modules.input.entity.vo.InputInvoiceTaxationVo;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FilenameUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.*;

@RestController
@RequestMapping("input/fileServer")
public class FastdfsController extends HttpServlet {

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    /**
     * 文件上传
     *
     * @param
     * @return
     * @throws IOException
     */
    @ApiOperation("文件上传")
    @PostMapping("/upload")
    @RequiresPermissions("input:fileServer:upload")
    public R test(HttpServletRequest request) throws IOException {


        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("file");
        // 文件保存路径
        String filepath = "statics/db/pic/";

        UploadFileEntity fileEntity = UploadKitUtil.uploadFile(file,filepath,true, false);
        return R.ok(fileEntity.getServerPath()).put("fileName",file.getOriginalFilename());
    }

    @PostMapping("/uploadOcr")
    @RequiresPermissions("input:fileServer:uploadOcr")
    public R uploadOcr(HttpServletRequest request) throws IOException {
        R r = new R();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("file");
        try {

            String ocrResult = OCRUtils.thirdpartyOCR(file);
            r.put("ocrResult",ocrResult);
            return r;
        }
        catch(Exception e){
            System.out.println("thirdartyOCR 调用异常" + e);
    //            e.printStackTrace();
        }
        return R.ok();
    }

    public  String uploadQNImg(FileInputStream file, String key) {

//        String accessKey = "-4_KxRQslAjfSYpEaCkbFXAd792TINkUFzUCHOdE";
//        String secretKey = "3Ehh1CN2PIEXNSeivFevSLBE3PzO3evo_UwdOckc";
//        String bucket = "pwcinputsystem";
//        String DOMAIN = "http://q3rqhynq2.bkt.clouddn.com";
//        String accessKey = "WlFoWuOXEgNwQWVPd4X1H3Kh2T8aQp6yQM9n7sGC";
//        String secretKey = "mPaRM4IUorY2GMDu6DeSqFmEkYM81Jhj0rZop8H5";
//        String bucket = "vendingmachinesunday";
//        String DOMAIN = "http://q8gflefk3.bkt.clouddn.com";

        String accessKey = "18XNlwFo9Jw9SfJd8TcoqmE38qm1Ny6MsG__IDbb";
        String secretKey = "i73qTUaaM2aWCiR3cbkf4E7jsS7qHeqqLE4EVi6p";
        String bucket = "pwc";
        String DOMAIN = "http://qa0bxsfth.bkt.clouddn.com";

        long expireSeconds = 3600;


        // 构造一个带指定Zone对象的配置类
//        Configuration cfg = new Configuration(Zone.zone2());
        Configuration cfg = new Configuration(Zone.zone0());
        // 其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        // 生成上传凭证，然后准备上传

        try {
            Auth auth = Auth.create(accessKey,secretKey);
            String upToken = auth.uploadToken(bucket);
            try {
                Response response = uploadManager.put(file, key, upToken, null, null);
                // 解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);

                String returnPath = DOMAIN + "/" + putRet.key;
                return returnPath;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }




    /**
     * 文件删除
     * @param path
     * @return
     */
    @ApiOperation("文件删除")
    @DeleteMapping("/delete")
    public String delete(@RequestParam String path) {
        String arr[]=path.split(",");
        for(int i=0;i<arr.length;i++){
            String imgPath="139.196.149.50:88/"+arr[i];
            fastFileStorageClient.deleteFile(imgPath);
        }
        // 第一种删除：参数：完整地址
        // 第二种删除：参数：组名加文件路径
        // fastFileStorageClient.deleteFile(group,path);

        return "恭喜恭喜，删除成功！";
    }

    public static void main(String[] args) {

    }


    /**
     * 文件下载
     *
     * @param path
     * @return
     */
    @ApiOperation("文件下载")
    @GetMapping("/download")
    public void downLoad(@RequestParam String group, @RequestParam String path, @RequestParam String fileName, HttpServletResponse
    response) throws IOException {

        // 获取文件
        byte[] bytes = fastFileStorageClient.downloadFile(group, path, new DownloadByteArray());

        //设置相应类型application/octet-stream        （注：applicatoin/octet-stream 为通用，一些其它的类型苹果浏览器下载内容可能为空）
        response.reset();
        response.setContentType("applicatoin/octet-stream");
        //设置头信息                 Content-Disposition为属性名  附件形式打开下载文件   指定名称  为 设定的fileName
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        // 写入到流
        ServletOutputStream out = response.getOutputStream();
        out.write(bytes);
        out.close();
    }

    public void uoloadPic(){
        File file = new File("/pic/888.png");
        try {
            // 设置文件信息
            Set<MataData> mataData = new HashSet<>();
            mataData.add(new MataData("author", "pwc"));
            // 上传   （文件上传可不填文件信息，填入null即可）
            InputStream inputStream = new FileInputStream(file);
            System.out.println(inputStream.toString());
            System.out.println(file.length());
            System.out.println(file.getName());
            StorePath storePath = fastFileStorageClient.uploadFile(inputStream, file.length(), FilenameUtils.getExtension(file.getName()), mataData);
            System.out.println(storePath.getFullPath());
        }catch (Exception e) {
            e.printStackTrace();
        }
        file.delete();
    }


    @GetMapping("/uploadExcel")

    public R upload(@RequestParam Map<Object,String> params, HttpServletResponse response) throws IOException {
        String fileName = "模板数据";
        Class nameClass = InputInvoiceTaxationVo.class;
        new ExportExcel("进项列表", nameClass).setDataList(new ArrayList<>()).write(response, fileName).dispose();
        return R.ok();
    }

    public List upload(MultipartFile file){
        Class nameClass = InputInvoiceTaxationVo.class;
        List list = new ArrayList();
        try {
            ImportExcel ei = new ImportExcel(file, 1, 0);
             list = ei.getDataList(nameClass);
        }catch (Exception e){
        }
        return list;
    }

}
