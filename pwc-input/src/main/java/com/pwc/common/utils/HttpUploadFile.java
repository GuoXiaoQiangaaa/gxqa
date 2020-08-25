package com.pwc.common.utils;

import cn.hutool.core.util.ClassUtil;
import org.springframework.stereotype.Service;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * java通过模拟post方式提交表单实现图片上传功能实例
 * 其他文件类型可以传入 contentType 实现
 *
 * @author cj
 *
 * @version 1.0
 */
@Service
public class HttpUploadFile {

    /**
     * 发票上传png图片
     */
    public String connectionOCR(String url,String fileName/*,String type*/) {
//        String url = "http://xxx/wnwapi/index.php/Api/Index/testUploadModelBaking";
//        String url = "http://101.132.78.217:8000/scan_five_factors/";
//        String fileName = "D:/test/read/223.jpg";


        Map<String, String> textMap = new HashMap<String, String>();
        //可以设置多个input的name，value
        textMap.put("key", "2");
        //设置file的name，路径
        Map<String, String> fileMap = new HashMap<String, String>();
        fileMap.put("image",fileName);
        fileMap.put("file",fileName);


       /* if(type.equals("2")) {
            fileMap.put("invoice", fileName);
        } else {
            fileMap.put("image", fileName);
        }*/
        String contentType = "";//image/png
        String ret = formUpload(url, textMap, fileMap, contentType);
        System.out.println(ret);
        return ret;
        //{"status":"0","message":"add succeed","baking_url":"group1\/M00\/00\/A8\/CgACJ1Zo-LuAN207AAQA3nlGY5k151.png"}
    }


    /**
     * 物料凭证png图片ocr
     */
    public String voucherOCR(String url,String fileName,String key) {
        Map<String, String> textMap = new HashMap<String, String>();
        //可以设置多个input的name，value
//        textMap.put("name", "testname");
//        textMap.put("type", "2");
        textMap.put("key",key);
        //设置file的name，路径
        Map<String, String> fileMap = new HashMap<String, String>();
        fileMap.put("image", fileName);
        String contentType = "";//image/png
        String ret = formUpload(url, textMap, fileMap, contentType);
        System.out.println(ret);
        return ret;
        //{"status":"0","message":"add succeed","baking_url":"group1\/M00\/00\/A8\/CgACJ1Zo-LuAN207AAQA3nlGY5k151.png"}
    }


    /**
     * 上传图片
     *
     * @param urlStr
     * @param textMap
     * @param fileMap
     * @param contentType 没有传入文件类型默认采用application/octet-stream
     *                    contentType非空采用filename匹配默认的图片类型
     * @return 返回response数据
     */
    @SuppressWarnings("rawtypes")
    public static String formUpload(String urlStr, Map<String, String> textMap,
                                    Map<String, String> fileMap, String contentType) {
        String res = "";
        HttpURLConnection conn = null;
        // boundary就是request头和上传文件内容的分隔符
        String BOUNDARY = "---------------------------123821742118716";
        String code="";
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(0);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            conn.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=" + BOUNDARY);
            OutputStream out = new DataOutputStream(conn.getOutputStream());
            // text
            if (textMap != null) {
                StringBuffer strBuf = new StringBuffer();
                Iterator iter = textMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String inputName = (String) entry.getKey();
                    String inputValue = (String) entry.getValue();
                    if (inputValue == null) {
                        continue;
                    }
                    strBuf.append("\r\n").append("--").append(BOUNDARY)
                            .append("\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\""
                            + inputName + "\"\r\n\r\n");
                    strBuf.append(inputValue);
                }
                out.write(strBuf.toString().getBytes());
            }
            // file
            if (fileMap != null) {
                Iterator iter = fileMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String inputName = (String) entry.getKey();
                    String inputValue = (String) entry.getValue();
                    if (inputValue == null) {
                        continue;
                    }
                    File file = new File(ClassUtil.getClassPath() + inputValue);
                    String filename = file.getName();
                    //没有传入文件类型，同时根据文件获取不到类型，默认采用application/octet-stream
                    contentType = new MimetypesFileTypeMap().getContentType(file);
                    //contentType非空采用filename匹配默认的图片类型
                    if (!"".equals(contentType)) {
                        if (filename.endsWith(".png")) {
                            contentType = "image/png";
                        } else if (filename.endsWith(".jpg") || filename.endsWith(".jpeg") || filename.endsWith(".jpe")) {
                            contentType = "image/jpeg";
                        } else if (filename.endsWith(".gif")) {
                            contentType = "image/gif";
                        } else if (filename.endsWith(".ico")) {
                            contentType = "image/image/x-icon";
                        }
                    }
                    if (contentType == null || "".equals(contentType)) {
                        contentType = "application/octet-stream";
                    }
                    StringBuffer strBuf = new StringBuffer();
                    strBuf.append("\r\n").append("--").append(BOUNDARY)
                            .append("\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\""
                            + inputName + "\"; filename=\"" + filename
                            + "\"\r\n");
                    strBuf.append("Content-Type:" + contentType + "\r\n\r\n");
                    out.write(strBuf.toString().getBytes());
                    DataInputStream in = new DataInputStream(
                            new FileInputStream(file));
                    int bytes = 0;
                    byte[] bufferOut = new byte[1024];
                    while ((bytes = in.read(bufferOut)) != -1) {
                        out.write(bufferOut, 0, bytes);
                    }
                    in.close();
                }
            }

            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
            out.write(endData);
            out.flush();
            out.close();
            // 读取返回数据
            code= new Integer(conn.getResponseCode()).toString();
            StringBuffer strBuf = new StringBuffer();
            if("201".equals(code)){
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    strBuf.append(line).append("\n");
                }
                res = strBuf.toString();
                reader.close();
                reader = null;
            }else if("400".equals(code)){
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        conn.getErrorStream()));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    strBuf.append(line).append("\n");
                }
                res = strBuf.toString();
                reader.close();
                reader = null;
            }else{
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        conn.getErrorStream()));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    strBuf.append(line).append("\n");
                }
                res = strBuf.toString();
                reader.close();
                reader = null;
            }

        } catch (Exception e) {
            System.out.println("发送POST请求出错。" + urlStr);
            System.out.println(code);
//            e.printStackTrace();
            return "2";
           /* e.printStackTrace();*/
        } finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        return res;
    }
}
