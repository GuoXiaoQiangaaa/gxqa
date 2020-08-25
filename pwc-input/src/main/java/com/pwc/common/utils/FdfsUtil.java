package com.pwc.common.utils;

import cn.hutool.core.util.ClassUtil;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FdfsUtil {

    public static void createLog(String inParam, String outParam, String interfaceName) {
        String oaPath = ClassUtil.getClassPath() + "statics/db/log/";
        Map map = new HashMap();
        map.put("入参", inParam);
        map.put("出参", outParam);
        map.put("接口名称", interfaceName);
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fileName = simpleDateFormat2.format(new Date()).replaceAll("-", "").replaceAll(" ", "").replaceAll(":", "").substring(0, 8);
        String filePath = oaPath + fileName + ".txt";
//        String filePath = "/data/log/" + fileName + ".txt";
        fastCopy(map.toString(), filePath);
    }

    /**
     * 通过NIC（块）快速复制文件
     *
     * @param
     * @throws
     */
    public static void fastCopy(String src, String dist) {
        try {
            //true不覆盖已有内容
            FileOutputStream fileOutputStream = new FileOutputStream(dist, true);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String string = simpleDateFormat.format(new Date());
            String string1 = new String();
            string1 = string + "      " + src;
            fileOutputStream.write(string1.getBytes());
            // 写入一个换行
            fileOutputStream.write("\r\n".getBytes());
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
