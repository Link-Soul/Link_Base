package com.link.identify;

/**
 * @Author zhoubinbin
 * @Date 2025/6/26 16:12
 */
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgcodecs.Imgcodecs;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.List;
// 元宝
public class ScreenColorContour {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) throws AWTException, InterruptedException {
        // 屏幕捕获参数
        int x = 0, y = 0, width = 800, height = 600;
        Robot robot = new Robot();
        Rectangle screenRect = new Rectangle(x, y, width, height);

        // 颜色范围（示例：红色）
        Scalar lowerRed = new Scalar(165, 210, 240);
        Scalar upperRed = new Scalar(170, 215, 250);
//        private static final Color TARGET_COLOR = new Color(169, 212, 248); // 目标颜色 (蓝绿色)


        // 1. 截屏并转为OpenCV Mat
        BufferedImage screenshot = robot.createScreenCapture(screenRect);
        Mat image = bufferedImageToMat(screenshot);

        // 2. 颜色过滤
        Mat hsv = new Mat();
        Imgproc.cvtColor(image, hsv, Imgproc.COLOR_BGR2HSV);
        Mat mask = new Mat();
        Core.inRange(hsv, lowerRed, upperRed, mask);

        // 3. 查找轮廓
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        // 选择最大轮廓
        MatOfPoint largestContour = contours.get(0);
        for (MatOfPoint contour : contours) {
            if (Imgproc.contourArea(contour) > Imgproc.contourArea(largestContour)) {
                largestContour = contour;
            }
        }

        // 4. 轮廓近似（调整epsilon控制精度）
        MatOfPoint2f approxCurve = new MatOfPoint2f();
        Imgproc.approxPolyDP(
                new MatOfPoint2f(largestContour.toArray()),
                approxCurve,
                0.02 * Imgproc.arcLength(new MatOfPoint2f(largestContour.toArray()), true),
                true
        );

        // 转换为点列表
        List<Point> points = new ArrayList<>();
        for (Point p : approxCurve.toList()) {
            points.add(new Point((int)p.x, (int)p.y));
        }

        // 5. 按角度变化进一步筛选点（示例逻辑）
        List<Point> filteredPoints = filterByAngle(points, 30); // 角度阈值30度

        // 6. 绘制验证（可选）
        BufferedImage resultImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resultImage.createGraphics();
        g.drawImage(screenshot, 0, 0, null);
        g.setColor(Color.GREEN);
        for (Point p : filteredPoints) {
            g.fillOval((int)p.x, (int)p.y, 5, 5);
        }
        g.dispose();
        // 保存结果图像查看效果

        // 7. 自动点击坐标
        for (Point p : filteredPoints) {
            robot.mouseMove((int)p.x, (int)p.y);
            robot.delay(100);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.delay(50);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        }
    }

    // 角度过滤方法（需实现）
    private static List<Point> filterByAngle(List<Point> points, double angleThreshold) {
        // 实现角度变化检测逻辑
        // 返回筛选后的点列表
        return points;
    }

    // BufferedImage转Mat
    private static Mat bufferedImageToMat(BufferedImage bi) {
        Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
        int[] data = ((DataBufferInt) bi.getRaster().getDataBuffer()).getData();
        mat.put(0, 0, data);
        return mat;
    }
}