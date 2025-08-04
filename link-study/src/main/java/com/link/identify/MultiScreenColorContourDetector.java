package com.link.identify;

/**
 * @Author zhoubinbin
 * @Date 2025/6/26 16:28
 */
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class MultiScreenColorContourDetector {

    // 目标颜色和容差设置
    private static final Color TARGET_COLOR = new Color(169, 212, 248); // 目标颜色 (蓝绿色)
    private static final int COLOR_TOLERANCE = 1; // 颜色容差范围

    // 轮廓简化参数
    private static final double ANGLE_THRESHOLD = 10.0; // 角度变化阈值 (度)
    private static final int MIN_DISTANCE = 100; // 点间最小距离 (像素)
    private static final long CLICK_DELAY_MS = 500; // 点击间隔0.5秒

    public static void main(String[] args) throws Exception {
        // 1. 获取所有屏幕设备
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] screens = ge.getScreenDevices();

        // 选择目标屏幕 (这里选择第二块屏幕)
        int targetScreenIndex = 0; // 0=主屏幕, 1=第二块屏幕
        if (targetScreenIndex >= screens.length) {
            System.err.println("错误: 请求的屏幕不存在，使用主屏幕");
            targetScreenIndex = 0;
        }

        System.out.println("正在使用屏幕 #" + targetScreenIndex);
        GraphicsDevice targetScreen = screens[targetScreenIndex];

        // 2. 获取目标屏幕的尺寸和位置
        GraphicsConfiguration gc = targetScreen.getDefaultConfiguration();
        Rectangle screenRect = gc.getBounds();

        // 3. 捕获目标屏幕
        Robot robot = new Robot();
        BufferedImage screenImage = robot.createScreenCapture(screenRect);

        // 4. 识别目标颜色区域
        boolean[][] colorMap = detectColorRegions(screenImage);

        // 5. 查找并简化轮廓
        List<Point> contour = findContour(colorMap);
        List<Point> simplifiedContour = simplifyContour(contour);

        System.out.println("检测到轮廓点数量: " + simplifiedContour.size());

        // 6. 将坐标转换为全局屏幕坐标
        List<Point> globalPoints = convertToGlobalCoordinates(simplifiedContour, screenRect);

        // 7. 模拟鼠标点击轮廓点 (0.5秒间隔)
        performMouseClicks(robot, globalPoints, CLICK_DELAY_MS);

        System.out.println("操作完成!");
    }

    // 颜色区域检测
    private static boolean[][] detectColorRegions(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        boolean[][] map = new boolean[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color pixelColor = new Color(image.getRGB(x, y));
                if (colorDistance(pixelColor, TARGET_COLOR) <= COLOR_TOLERANCE) {
                    map[x][y] = true;
                }
            }
        }
        return map;
    }

    // 计算颜色距离 (简化欧氏距离)
    private static double colorDistance(Color c1, Color c2) {
        int rDiff = c1.getRed() - c2.getRed();
        int gDiff = c1.getGreen() - c2.getGreen();
        int bDiff = c1.getBlue() - c2.getBlue();
        return Math.sqrt(rDiff * rDiff + gDiff * gDiff + bDiff * bDiff);
    }

    // 查找轮廓 (边界跟踪算法)
    private static List<Point> findContour(boolean[][] map) {
        int width = map.length;
        int height = map[0].length;
        List<Point> contour = new ArrayList<>();

        // 查找起始点 (第一个目标颜色像素)
        Point start = findStartPoint(map);
        if (start == null) return contour;

        // 使用Moore邻域跟踪算法
        Point current = start;
        Point previous = new Point(start.x, start.y - 1); // 假设初始方向向上

        // 8个邻域方向 (顺时针)
        int[][] directions = {{1,0}, {1,-1}, {0,-1}, {-1,-1}, {-1,0}, {-1,1}, {0,1}, {1,1}};

        do {
            contour.add(new Point(current));

            // 从上次方向开始搜索 (顺时针)
            int backDir = getDirectionIndex(previous, current);
            int nextDir = -1;

            for (int i = 0; i < 8; i++) {
                int dirIndex = (backDir + 1 + i) % 8;
                int nx = current.x + directions[dirIndex][0];
                int ny = current.y + directions[dirIndex][1];

                if (nx >= 0 && nx < width && ny >= 0 && ny < height && map[nx][ny]) {
                    nextDir = dirIndex;
                    break;
                }
            }

            if (nextDir == -1) break; // 未找到下一个点

            // 更新位置
            previous = current;
            current = new Point(
                    current.x + directions[nextDir][0],
                    current.y + directions[nextDir][1]
            );

        } while (!current.equals(start) && contour.size() < 10000); // 防止无限循环

        return contour;
    }

    // 获取两点间方向索引
    private static int getDirectionIndex(Point from, Point to) {
        int dx = to.x - from.x;
        int dy = to.y - from.y;

        if (dx == 1 && dy == 0) return 0;
        if (dx == 1 && dy == -1) return 1;
        if (dx == 0 && dy == -1) return 2;
        if (dx == -1 && dy == -1) return 3;
        if (dx == -1 && dy == 0) return 4;
        if (dx == -1 && dy == 1) return 5;
        if (dx == 0 && dy == 1) return 6;
        if (dx == 1 && dy == 1) return 7;
        return 0;
    }

    // 查找起始点
    private static Point findStartPoint(boolean[][] map) {
        // 从中心开始搜索提高效率
        int centerX = map.length / 2;
        int centerY = map[0].length / 2;

        // 螺旋搜索
        int radius = 0;
        while (radius < Math.max(map.length, map[0].length) / 2) {
            // 上边
            for (int x = centerX - radius; x <= centerX + radius; x++) {
                int y = centerY - radius;
                if (x >= 0 && x < map.length && y >= 0 && y < map[0].length && map[x][y]) {
                    return new Point(x, y);
                }
            }
            // 右边
            for (int y = centerY - radius; y <= centerY + radius; y++) {
                int x = centerX + radius;
                if (x >= 0 && x < map.length && y >= 0 && y < map[0].length && map[x][y]) {
                    return new Point(x, y);
                }
            }
            // 下边
            for (int x = centerX + radius; x >= centerX - radius; x--) {
                int y = centerY + radius;
                if (x >= 0 && x < map.length && y >= 0 && y < map[0].length && map[x][y]) {
                    return new Point(x, y);
                }
            }
            // 左边
            for (int y = centerY + radius; y >= centerY - radius; y--) {
                int x = centerX - radius;
                if (x >= 0 && x < map.length && y >= 0 && y < map[0].length && map[x][y]) {
                    return new Point(x, y);
                }
            }
            radius++;
        }

        // 回退到全屏扫描
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[0].length; y++) {
                if (map[x][y]) return new Point(x, y);
            }
        }
        return null;
    }

    // 轮廓简化算法
    private static List<Point> simplifyContour(List<Point> contour) {
        if (contour.size() < 3) return contour;

        List<Point> simplified = new ArrayList<>();
        simplified.add(contour.get(0));

        Point lastAdded = contour.get(0);
        Point lastDirection = new Point(0, 0);

        for (int i = 1; i < contour.size() - 1; i++) {
            Point current = contour.get(i);
            Point next = contour.get(i + 1);

            // 计算方向向量
            Point dir1 = new Point(current.x - lastAdded.x, current.y - lastAdded.y);
            Point dir2 = new Point(next.x - current.x, next.y - current.y);

            // 计算角度变化
            double angleChange = calculateAngle(dir1, dir2);

            // 检查是否满足添加条件
            double distance = lastAdded.distance(current);
            if (angleChange > ANGLE_THRESHOLD || distance > MIN_DISTANCE) {
                simplified.add(current);
                lastAdded = current;
                lastDirection = dir1;
            }
        }

        // 添加最后一个点
        simplified.add(contour.get(contour.size() - 1));
        return simplified;
    }

    // 计算两个向量间的角度变化 (度)
    private static double calculateAngle(Point v1, Point v2) {
        if (v1.x == 0 && v1.y == 0) return 0;
        if (v2.x == 0 && v2.y == 0) return 0;

        double dot = v1.x * v2.x + v1.y * v2.y;
        double mag1 = Math.sqrt(v1.x * v1.x + v1.y * v1.y);
        double mag2 = Math.sqrt(v2.x * v2.x + v2.y * v2.y);

        double cosAngle = dot / (mag1 * mag2);
        cosAngle = Math.max(-1, Math.min(1, cosAngle)); // 夹紧值
        return Math.toDegrees(Math.acos(cosAngle));
    }

    // 将相对坐标转换为全局屏幕坐标
    private static List<Point> convertToGlobalCoordinates(List<Point> points, Rectangle screenRect) {
        List<Point> globalPoints = new ArrayList<>();
        for (Point p : points) {
            globalPoints.add(new Point(
                    screenRect.x + p.x,
                    screenRect.y + p.y
            ));
        }
        return globalPoints;
    }

    // 执行鼠标点击操作
    private static void performMouseClicks(Robot robot, List<Point> points, long delayMs) {
        System.out.println("将在5秒后开始点击操作...");
        robot.delay(5000); // 5秒准备时间

        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            robot.mouseMove(p.x, p.y);
            System.out.printf("点击 #%d: (%d, %d)%n", i+1, p.x, p.y);

            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.delay(50); // 保持按下状态50ms
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

            // 精确延迟控制
            if (i < points.size() - 1) {
                long start = System.currentTimeMillis();
                robot.delay((int) delayMs);
                long elapsed = System.currentTimeMillis() - start;
                System.out.printf("点击间隔: %dms%n", elapsed);
            }
        }
    }
}