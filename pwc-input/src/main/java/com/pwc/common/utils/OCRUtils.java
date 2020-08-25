package com.pwc.common.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class OCRUtils {




    public static String thirdpartyOCR(MultipartFile file) throws Exception {
//        String url = "https://ocr-api.ccint.com/cci_ai/service/v1/bills_crop";
//        String appKey = "ai_demo_bills_crop"; // your app_key
//        String appSecret = "ai_demo_bills_crop";

        String url = "https://ocr-api.ccint.com/cci_ai/service/v1/bills_crop";
        String appKey = "12bac0548debb6d6c755bb28afcf6c75"; // your app_key
        String appSecret = "c13c4d1e3895dd2979ceb6109722186e"; // your app_secret
        BufferedReader in = null;
        DataOutputStream out = null;
        String result = "";

        try {
            byte[] imgData =  file.getBytes();
//                readfile("example.jpg"); // image
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/octet-stream");
            conn.setRequestProperty("app-key",appKey);
            conn.setRequestProperty("app-secret",appSecret);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST"); // 设置请求方式
            out = new DataOutputStream(conn.getOutputStream());
            out.write(imgData);
            out.flush();
            out.close();
            in = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        System.out.println(result);
        return result;
    }

    public static byte[] readfile(String path) {
        String imgFile = path;
        InputStream in = null;
        byte[] data = null;
        try
        {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
