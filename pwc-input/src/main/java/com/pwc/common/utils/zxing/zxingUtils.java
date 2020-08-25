package com.pwc.common.utils.zxing;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.lang.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Hashtable;

public class zxingUtils {

    private static final String CHARSET = "UTF-8";
    private static final String FORMAT_NAME = "JPG";
    // 二维码尺寸
    private static final int QRCODE_SIZE = 500;
    // LOGO宽度
    private static final int WIDTH = 100;
    // LOGO高度
    private static final int HEIGHT = 100;
    // 图片临时文件路径
    public static final String diskPath = "D:/2barcode/";
    /**
     * user: Rex
     * date: 2016年12月29日  上午12:31:29
     * @param content 二维码内容
     * @param needCompress 是否压缩Logo
     * @return 返回二维码图片
     * @throws WriterException
     * @throws IOException
     * BufferedImage
     * TODO 创建二维码图片
     */
    private static BufferedImage createImage(String content,boolean needCompress) throws Exception {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE, hints);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }

        BufferedImage logoImgPath=generateImageTips(content.split("-")[0],Color.BLUE);
        if (logoImgPath == null || "".equals(logoImgPath)) {
            return image;
        }

        // 插入图片
        zxingUtils.insertImage(image, logoImgPath, needCompress);
        return image;
    }


    // 生成提示文字图片
    public static BufferedImage generateImageTips(String tips,Color color) throws Exception{
        int width = 150;
        int height = 150;
        File file = new File(diskPath+tips);

        Font font = new Font("微软雅黑", Font.BOLD, 60);
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D)bi.getGraphics();
        g2.setBackground(Color.WHITE);
        g2.clearRect(0, 0, width, height);
        g2.setPaint(color);
        g2.setFont(font);
        FontRenderContext context = g2.getFontRenderContext();
        Rectangle2D bounds = font.getStringBounds(tips, context);
        double x = (width - bounds.getWidth()) / 2;
        double y = (height - bounds.getHeight()) / 2;
        double ascent = -bounds.getY();
        double baseY = y + ascent;
        g2.drawString(tips, (int)x, (int)baseY);
        return bi;
    }






    /**
     * user: Rex
     * date: 2016年12月29日  上午12:30:09
     * @param source 二维码图片
     * @param logoImgPath Logo
     * @param needCompress 是否压缩Logo
     * @throws IOException
     * void
     * TODO 添加Logo
     */
    private static void insertImage(BufferedImage source, BufferedImage logoImgPath, boolean needCompress) throws IOException{

        int width = logoImgPath.getWidth(null);
        int height = logoImgPath.getHeight(null);

        Image src =logoImgPath;
        if (needCompress) {
            if (width > WIDTH) {
                width = WIDTH;
            }
            if (height > HEIGHT) {
                height = HEIGHT;
            }

            Image  image = logoImgPath.getScaledInstance(width, height,
                    Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null);
            g.dispose();
            src = image;
        }
        Graphics2D graph = source.createGraphics();
        int x = (QRCODE_SIZE - width) / 2;
        int y = (QRCODE_SIZE - height) / 2;
        graph.drawImage(src, x, y, width, height, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }

    /**
     * user: Rex
     * date: 2016年12月29日  上午12:32:32
     * @param content 二维码内容
     * @param logoImgPath Logo
     * @param destPath 二维码输出路径
     * @param needCompress 是否压缩Logo
     * @throws Exception
     * void
     * TODO 生成带Logo的二维码
     */
    public static void encode(String content, BufferedImage logoImgPath, String destPath, boolean needCompress) throws Exception {
        BufferedImage image = zxingUtils.createImage(content, needCompress);
        mkdirs(destPath);
        ImageIO.write(image, FORMAT_NAME, new File(destPath));
    }



    /**
     * user: Rex
     * date: 2016年12月29日  上午12:36:58
     * @param content 二维码内容
     * @param output 输出流
     * @param needCompress 是否压缩Logo
     * @throws Exception
     * void
     * TODO 生成带Logo的二维码，并输出到指定的输出流
     */
    public static void encode(String content, OutputStream output, boolean needCompress) throws Exception {
        BufferedImage image = zxingUtils.createImage(content, needCompress);
        ImageIO.write(image, FORMAT_NAME, output);
    }

    /**
     * user: Rex
     * date: 2016年12月29日  上午12:39:10
     * @param file 二维码
     * @return 返回解析得到的二维码内容
     * @throws Exception
     * String
     * TODO 二维码解析
     */
    public static String decode(File file) throws Exception {
        BufferedImage image;
        image = ImageIO.read(file);
        if (image == null) {
            return null;
        }
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result;
        Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();
        hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
        result = new MultiFormatReader().decode(bitmap, hints);
        String resultStr = result.getText();
        return resultStr;
    }

    /**
     * user: Rex
     * date: 2016年12月29日  上午12:39:48
     * @param path 二维码存储位置
     * @return 返回解析得到的二维码内容
     * @throws Exception
     * String
     * TODO 二维码解析
     */
    public static String decode(String path) throws Exception {
        return zxingUtils.decode(new File(path));
    }


    /**
     * 如果路径不存在，则创建路径
     * @param dir
     */
    public static void mkdirs(String dir){
        if(StringUtils.isEmpty(dir)){
            return;
        }

        File file = new File(dir);
        if(file.isDirectory()){
            return;
        } else {
            file.mkdirs();
        }
    }


//    @Test
    public void testEncode() throws FileNotFoundException, Exception {
        String content = "618";
        String dir = "D:/2barcode/"+content+".png";
       File file = new File(dir);


       zxingUtils.encode(content, new FileOutputStream(file), true);
    }
}
