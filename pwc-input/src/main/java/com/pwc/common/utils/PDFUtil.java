package com.pwc.common.utils;

import cn.hutool.core.util.ClassUtil;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @description: 图片转成pdf下载
 * @author: Gxw
 * @create: 2020-09-23 11:13
 **/
public class PDFUtil {
    public  static  void downFile(HttpServletResponse response,String filename,String file)throws IOException{
        if(StringUtils.isNotBlank(filename)&&StringUtils.isNotBlank(file)) {
            downFileByPDF(response,filename+".pdf", ClassUtil.getClassPath() + file);
        }
    }
    public static void downFileByPDF(HttpServletResponse response, String filename, String file) throws IOException {
            OutputStream out = null;
            FileInputStream in = null;
            ByteArrayOutputStream baos = null;
            try {
                Document document = new Document();
                //设置文档页边距
                document.setMargins(0,0,0,0);
                baos = new ByteArrayOutputStream();
                PdfWriter.getInstance(document,baos);
                document.open();
                //获取图片的宽高
                Image image = Image.getInstance(file);
                float imageHeight=image.getScaledHeight();
                float imageWidth=image.getScaledWidth();
                //设置页面宽高与图片一致
                Rectangle rectangle = new Rectangle(imageWidth, imageHeight);
                document.setPageSize(rectangle);
                //图片居中
                image.setAlignment(Image.ALIGN_CENTER);
                //新建一页添加图片
                document.newPage();
                document.add(image);
                document.close();
                filename = new String(filename.getBytes("UTF-8"), "iso-8859-1");
                // 设置下载文件的mineType，告诉浏览器下载文件类型
                response.setHeader("Content-Type","application/pdf");
                response.setContentType("application/pdf");
                // 设置一个响应头，无论是否被浏览器解析，都下载
                response.setHeader("Content-Disposition", "attachment; filename=" + filename);
                // 将要下载的文件内容通过输出流写到浏览器
                out = response.getOutputStream();
                int len = 0;
                byte[] buffer = new byte[1024];
                ByteArrayInputStream inStream = new ByteArrayInputStream(
                        baos.toByteArray());
                while ((len = inStream.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            }
        }
}
