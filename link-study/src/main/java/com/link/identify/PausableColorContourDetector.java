package com.link.identify;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author Link
 * @Date 2025/6/26 17:02
 */
public class PausableColorContourDetector {

    // 配置参数
    private static final Color TARGET_COLOR = new Color(169, 212, 248); // 目标颜色 (蓝绿色)
    // 颜色容差
    private static final int COLOR_TOLERANCE = 15;
    // 角度阈值
    private static final double ANGLE_THRESHOLD = 12.0;
    // 最小距离
    private static final int MIN_DISTANCE = 20;
    // 鼠标点击延迟   刚开始调试要大一点，不然容易连点。最快 400
    private static final long CLICK_DELAY_MS = 4000;
    // 最小区域大小
    private static final int MIN_REGION_SIZE = 100;

    // 调试设置
    private static final boolean DEBUG_MODE = true;
    // 生成的测试图片地址
    private static final String DEBUG_PATH = "D:/contour_debug/";

    // 暂停控制
    private static final AtomicBoolean isPaused = new AtomicBoolean(false);
    private static final Object pauseLock = new Object();
    // 暂停按键  不好用
    private static final int PAUSE_KEY = KeyEvent.VK_F7;
    // 继续按键  不好用
    private static final int RESUME_KEY = KeyEvent.VK_F8;

    public static void main(String[] args) throws Exception {
        // 1. 创建调试目录
        if (DEBUG_MODE) {
            new File(DEBUG_PATH).mkdirs();
        }

        // 2. 启动键盘监听
        startKeyboardListener();
        System.out.println("按 Shift+F7 暂停，按 Shift+F8 继续");

        // 3. 选择目标屏幕
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] screens = ge.getScreenDevices();
        int targetScreenIndex = 0; // 第二屏幕

        if (targetScreenIndex >= screens.length) {
            System.err.println("警告: 使用主屏幕替代");
            targetScreenIndex = 0;
        }

        GraphicsDevice targetScreen = screens[targetScreenIndex];
        GraphicsConfiguration gc = targetScreen.getDefaultConfiguration();
        Rectangle screenRect = gc.getBounds();
        System.out.printf("目标屏幕区域: [x=%d, y=%d, w=%d, h=%d]%n",
                screenRect.x, screenRect.y, screenRect.width, screenRect.height);

        // 4. 捕获屏幕
        Robot robot = new Robot();
        BufferedImage screenImage = robot.createScreenCapture(screenRect);
        saveDebugImage(screenImage, "01_original.png");

        // 5. 颜色识别
        boolean[][] colorMap = detectColorRegions(screenImage);
        saveColorMapImage(screenImage, colorMap, "02_color_map.png");

        // 6. 区域分析和过滤
        List<Set<Point>> regions = findColorRegions(colorMap);
        System.out.println("发现区域数量: " + regions.size());

        // 过滤小区域
        regions.removeIf(region -> region.size() < MIN_REGION_SIZE);
        if (regions.isEmpty()) {
            System.err.println("错误: 未找到有效目标区域");
            return;
        }

        // 选择最大区域
        Set<Point> targetRegion = Collections.max(regions, Comparator.comparing(Set::size));
        System.out.println("目标区域大小: " + targetRegion.size() + " 像素");

        // 7. 轮廓检测
        List<Point> contour = findContour(targetRegion);
        saveContourImage(screenImage, contour, "03_contour_raw.png");

        // 8. 轮廓简化
        List<Point> simplified = simplifyContour(contour);
        saveContourImage(screenImage, simplified, "04_contour_simplified.png");

        System.out.println("简化轮廓点数量: " + simplified.size());

        // 9. 转换为全局坐标
        List<Point> globalPoints = convertToGlobalCoordinates(simplified, screenRect);

        // 10. 鼠标点击 (支持暂停)
        performMouseClicks(robot, globalPoints, CLICK_DELAY_MS);
        System.out.println("操作成功完成!");
    }

    // ================= 暂停控制功能 =================

//    private static void startKeyboardListener() {
//        Thread keyboardThread = new Thread(() -> {
//            KeyboardFocusManager.getCurrentKeyboardFocusManager()
//                    .addKeyEventDispatcher(event -> {
//                        if (event.getID() == KeyEvent.KEY_PRESSED) {
//                            if (event.getKeyCode() == PAUSE_KEY) {
//                                pauseExecution();
//                                return true;
//                            } else if (event.getKeyCode() == RESUME_KEY) {
//                                resumeExecution();
//                                return true;
//                            }
//                        }
//                        return false;
//                    });
//        });
//        keyboardThread.setDaemon(true);
//        keyboardThread.start();
//    }

    private static void startKeyboardListener() {
        Thread keyboardThread = new Thread(() -> {
            KeyboardFocusManager.getCurrentKeyboardFocusManager()
                    .addKeyEventDispatcher(event -> {
                        if (event.getID() == KeyEvent.KEY_PRESSED) {
                            int modifiersEx = event.getModifiersEx();
                            boolean shiftPressed = (modifiersEx & InputEvent.SHIFT_DOWN_MASK) != 0;

                            if (shiftPressed && event.getKeyCode() == PAUSE_KEY) {
                                pauseExecution();
                                return true;
                            } else if (shiftPressed && event.getKeyCode() == RESUME_KEY) {
                                resumeExecution();
                                return true;
                            }
                        }
                        return false;
                    });
        });
        keyboardThread.setDaemon(true);
        keyboardThread.start();
    }

    private static void pauseExecution() {
        isPaused.set(true);
        System.out.println("\n===== 程序已暂停 (Shift+F7) =====");
    }

    private static void resumeExecution() {
        synchronized (pauseLock) {
            isPaused.set(false);
            pauseLock.notifyAll();
            System.out.println("\n===== 程序已继续 (Shift+F8) =====");
        }
    }

    private static void checkPauseState() {
        if (isPaused.get()) {
            System.out.println("等待继续... (按Shift+F8)");
            synchronized (pauseLock) {
                while (isPaused.get()) {
                    try {
                        pauseLock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }

    // ================= 核心功能 =================

    // 区域生长算法 - 查找连续区域
    private static List<Set<Point>> findColorRegions(boolean[][] map) {
        int width = map.length;
        int height = map[0].length;
        boolean[][] visited = new boolean[width][height];
        List<Set<Point>> regions = new ArrayList<>();
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, -1}, {-1, 1}, {1, -1}, {1, 1}};

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (map[x][y] && !visited[x][y]) {
                    Set<Point> region = new HashSet<>();
                    Queue<Point> queue = new LinkedList<>();
                    queue.add(new Point(x, y));
                    visited[x][y] = true;

                    while (!queue.isEmpty()) {
                        Point p = queue.poll();
                        region.add(p);

                        // 检查8邻域
                        for (int[] dir : directions) {
                            int nx = p.x + dir[0];
                            int ny = p.y + dir[1];

                            if (nx >= 0 && nx < width && ny >= 0 && ny < height &&
                                    map[nx][ny] && !visited[nx][ny]) {
                                visited[nx][ny] = true;
                                queue.add(new Point(nx, ny));
                            }
                        }
                    }
                    regions.add(region);
                }
            }
        }
        return regions;
    }

    // 基于区域查找轮廓
    private static List<Point> findContour(Set<Point> region) {
        List<Point> contour = new ArrayList<>();
        if (region.isEmpty()) return contour;

        // 找到最左边的点作为起点
        Point start = region.stream()
                .min(Comparator.comparingInt(p -> p.x))
                .orElse(region.iterator().next());

        Point current = start;
        Point prev = new Point(current.x, current.y - 1); // 初始向上
        int[][] directions = {{1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}, {0, 1}, {1, 1}};

        do {
            contour.add(current);
            int backDir = getDirectionIndex(prev, current);
            boolean foundNext = false;

            // 尝试8个方向
            for (int i = 0; i < 8; i++) {
                int dirIndex = (backDir + 7 + i) % 8; // 逆时针搜索
                int nx = current.x + directions[dirIndex][0];
                int ny = current.y + directions[dirIndex][1];
                Point next = new Point(nx, ny);

                if (region.contains(next)) {
                    prev = current;
                    current = next;
                    foundNext = true;
                    break;
                }
            }

            if (!foundNext) break;
        } while (!current.equals(start) && contour.size() < region.size() * 2);

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

    private static List<Point> simplifyContour(List<Point> contour) {
        if (contour.size() < 3) return new ArrayList<>(contour);

        // 1. 预处理：跳过密集点
        List<Point> filtered = new ArrayList<>();
        filtered.add(contour.get(0));
        for (int i = 1; i < contour.size(); i++) {
            Point last = filtered.get(filtered.size() - 1);
            if (last.distance(contour.get(i)) > 5) { // 5像素最小间距
                filtered.add(contour.get(i));
            }
        }
        filtered.add(contour.get(contour.size() - 1));

        // 2. 主简化过程
        List<Point> simplified = new ArrayList<>();
        simplified.add(filtered.get(0));
        Point lastAdded = filtered.get(0);

        for (int i = 1; i < filtered.size() - 1; i++) {
            Point current = filtered.get(i);
            Point next = filtered.get(i + 1);

            // 计算实际步长距离
            double stepDistance = filtered.get(i - 1).distance(current);

            // 计算角度变化
            Point vec1 = new Point(current.x - lastAdded.x, current.y - lastAdded.y);
            Point vec2 = new Point(next.x - current.x, next.y - current.y);
            double angle = calculateAngle(vec1, vec2);

            // 添加条件：步长距离足够大或角度变化显著
            if (stepDistance >= MIN_DISTANCE || angle >= ANGLE_THRESHOLD) {
                simplified.add(current);
                lastAdded = current;
            }
        }

        // 3. 确保添加终点
        if (!lastAdded.equals(filtered.get(filtered.size() - 1))) {
            simplified.add(filtered.get(filtered.size() - 1));
        }

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

    // 执行鼠标点击操作 (带暂停检查)
    private static void performMouseClicks(Robot robot, List<Point> points, long delayMs) {
        System.out.println("将在5秒后开始点击操作...");
        robot.delay(5000); // 5秒准备时间

        for (int i = 0; i < points.size(); i++) {
            // 检查暂停状态
            checkPauseState();

            Point p = points.get(i);
            robot.mouseMove(p.x, p.y);
            System.out.printf("点击 #%d: (%d, %d)%n", i + 1, p.x, p.y);

            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.delay(50); // 保持按下状态50ms
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

            // 精确延迟控制
            if (i < points.size() - 1) {
                long start = System.currentTimeMillis();

                // 带暂停检查的延迟
                while (System.currentTimeMillis() - start < delayMs) {
                    robot.delay(50);
                    if (isPaused.get()) {
                        checkPauseState(); // 如果暂停则等待
                    }
                }

                long elapsed = System.currentTimeMillis() - start;
                System.out.printf("点击间隔: %dms%n", elapsed);
            }
        }
    }

    // ================= 调试方法 =================

    private static void saveDebugImage(BufferedImage image, String filename) {
        if (!DEBUG_MODE) return;
        try {
            ImageIO.write(image, "PNG", new File(DEBUG_PATH + filename));
        } catch (Exception e) {
            System.err.println("保存调试图像失败: " + e.getMessage());
        }
    }

    private static void saveColorMapImage(BufferedImage original, boolean[][] map, String filename) {
        if (!DEBUG_MODE) return;
        try {
            BufferedImage debug = new BufferedImage(
                    original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_RGB);

            for (int x = 0; x < map.length; x++) {
                for (int y = 0; y < map[0].length; y++) {
                    if (map[x][y]) {
                        debug.setRGB(x, y, Color.RED.getRGB());
                    } else {
                        debug.setRGB(x, y, original.getRGB(x, y));
                    }
                }
            }
            ImageIO.write(debug, "PNG", new File(DEBUG_PATH + filename));
        } catch (Exception e) {
            System.err.println("保存颜色地图失败: " + e.getMessage());
        }
    }

    private static void saveContourImage(BufferedImage original, List<Point> contour, String filename) {
        if (!DEBUG_MODE) return;
        try {
            BufferedImage debug = new BufferedImage(
                    original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_RGB);

            // 复制原始图像
            Graphics2D g2d = debug.createGraphics();
            g2d.drawImage(original, 0, 0, null);

            // 绘制轮廓
            g2d.setColor(Color.GREEN);
            for (int i = 0; i < contour.size(); i++) {
                Point p1 = contour.get(i);
                Point p2 = contour.get((i + 1) % contour.size());
                g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
            }

            // 绘制轮廓点
            g2d.setColor(Color.RED);
            for (Point p : contour) {
                g2d.fillOval(p.x - 2, p.y - 2, 5, 5);
            }

            g2d.dispose();
            ImageIO.write(debug, "PNG", new File(DEBUG_PATH + filename));
        } catch (Exception e) {
            System.err.println("保存轮廓图像失败: " + e.getMessage());
        }
    }
}