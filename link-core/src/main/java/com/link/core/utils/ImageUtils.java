package com.link.core.utils;

import com.link.core.entity.Url;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 *@Description 图片工具类
 *@author Link
 *@since 2025/10/17 11:11
 **/
public class ImageUtils {


    /**
     *@Description 图片转像素
     *@author Link
     *@since 2025/10/17 11:11
     **/
    public static void ImgToPixels(Url url){
        BufferedImage originalImage = null; // 声明一个BufferedImage对象
        try {
            originalImage = ImageIO.read(new File(url.getTotalUrl())); // 加载图像
        } catch (IOException e) {
            e.printStackTrace(); // 处理异常
        }
        int scaledWidth = originalImage.getWidth() / 16; // 计算缩放后的宽度
        int scaledHeight = originalImage.getHeight() / 16; // 计算缩放后的高度
        BufferedImage scaledImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB); // 创建新的图像
        scaledImage
                .getGraphics()
                .drawImage(originalImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH),
                        0, 0, null); // 绘制缩小后的图像
        BufferedImage pixelArtImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB); // 创建最终图像
        pixelArtImage.getGraphics()
                .drawImage(scaledImage.getScaledInstance(originalImage.getWidth(),
                        originalImage.getHeight(), Image.SCALE_AREA_AVERAGING), 0, 0, null); // 将缩略图绘制回原尺寸
        try {
            ImageIO.write(pixelArtImage, "png", new File(url.getUrl()+url.getFileName()+"_Pixels."+url.getSuffix())); // 保存为PNG格式图像
        } catch (IOException e) {
            e.printStackTrace(); // 处理异常
        }
    }

    public static void main(String[] args) {
        String url = "C:\\Users\\ty\\Desktop\\学习文档\\Image_1760477767956.jpg";
        ImgToPixels(new Url(url));
    }

}
