package com.pwc.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;

public class DownloadUtil {

    public static void main(String[] args) {
//        String picUrl = "/group1/M00/00/00/wKhsbl1wuu2AXqH_AAedAFXgr9c632.png";
//        String url = "http://192.168.108.110:88" + picUrl; // 获取路径
//        String[] arr = picUrl.split("/");
//        String path="/" + arr[arr.length-1];  // 输出路径
//        try {
//            downloadPicture(url,path);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        File file = new File(path);
//        file.delete();

                delFolder("D:/pic");
    }

    //链接url下载图片
    public static void downloadPicture(String urlList,String path) throws Exception {
        URL url = null;
            url = new URL(urlList);
            DataInputStream dataInputStream = new DataInputStream(url.openStream());

            FileOutputStream fileOutputStream = new FileOutputStream(new File(path));
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int length;

            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            fileOutputStream.write(output.toByteArray());
            dataInputStream.close();
            fileOutputStream.close();
    }

    //删除文件夹
//param folderPath 文件夹完整绝对路径

    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); //删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
//            myFilePath.delete(); //删除空文件夹
        } catch (Exception e) {
//            e.printStackTrace();
        }
    } 


    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
//                delFolder(path + "/" + tempList[i]);//再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }

}
