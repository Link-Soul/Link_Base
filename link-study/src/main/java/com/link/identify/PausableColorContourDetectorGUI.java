package com.link.identify;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Queue;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author zhoubinbin
 * @Date 2025/6/27 09:05
 */
public class PausableColorContourDetectorGUI extends JFrame {

    // 配置参数
    private static final Color TARGET_COLOR = new Color(169, 212, 248); // 目标颜色 (蓝绿色)
    private static final int COLOR_TOLERANCE = 15;
    // 高精度 11  普通 12
    private static final double ANGLE_THRESHOLD = 11.5;
    // 点与点之间最短距离
    private static final int MIN_DISTANCE = 50;
    // 鼠标点击间隔
    private static double CLICK_DELAY_MS = 5;
    private static double confirmExecutionMs = 1000;
    private static final int MIN_REGION_SIZE = 100;
    private static final boolean DEBUG_MODE = true;
    private static final String DEBUG_PATH = "D:/contour_debug/";
    private static final boolean ifHide = false;
    // 针对水库场景，连续自动画图和保存。只通过快捷键暂停来终止
    private static boolean ifContinue = true;
    // 是否停止循环自动，通过快捷键激活。
    private static final AtomicBoolean breakContinue = new AtomicBoolean(true);
    private static double continueTime = 500;
    // 等待保存的时间
    private static double waitSaveTime = 500;


    // 暂停控制
    private static final AtomicBoolean isPaused = new AtomicBoolean(false);
    private static final Object pauseLock = new Object();
    // 暂停
    private static final int PAUSE_KEY = NativeKeyEvent.VC_F7;
    // 继续
    private static final int RESUME_KEY = NativeKeyEvent.VC_F8;
    // 确认执行
    private static final int BREAK_CONTINUE_KEY = NativeKeyEvent.VC_Q;
    // 继续
    private static final int CONTINUE_KEY = NativeKeyEvent.VC_W;
    // 检查围栏
    private static final int CHECK_KEY = NativeKeyEvent.VC_A;
    // 保存
    private static final int SAVE_KEY = NativeKeyEvent.VC_S;
    // 开始识别
    private static final int START_KEY = NativeKeyEvent.VC_C;
    // 开始进行操作
    private static final int START_IDENTITY_KEY = NativeKeyEvent.VC_V;

    // GUI组件
    private JLabel imageLabel;
    private JTextArea consoleArea;
    private JButton startButton;
    private JButton confirmButton;
    private JButton cancelButton;
    private JRadioButton primaryScreenRadio;
    private JRadioButton secondaryScreenRadio;
    private JRadioButton continueRadio;
    private JRadioButton breakContinueRadio;
    private BufferedImage contourImage;
    private int targetScreenIndex = 0; // 默认主屏幕

    /**
     * 测试添加的请求头
     *
     * @author ZhouBinBin
     * @since 2025/7/28 14:53
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PausableColorContourDetectorGUI app = new PausableColorContourDetectorGUI();
//            if (ifHide) {
            // changeTest
            app.setVisible(true);
//            }
            System.out.println("应用程序已启动");
            System.out.println("按 Shift+F7 暂停，按 Shift+F8 继续");
        });
    }


    public PausableColorContourDetectorGUI() {
        setTitle("画图自动标点工具");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
        startKeyboardListener();
    }

    private void initUI() {
        // 主面板使用边框布局
        JPanel mainPanel = new JPanel(new BorderLayout());

        // 顶部：图片展示区域
        imageLabel = new JLabel("等待识别结果...", JLabel.CENTER);
        imageLabel.setPreferredSize(new Dimension(800, 600));
        imageLabel.setBorder(BorderFactory.createTitledBorder("轮廓预览"));
        JScrollPane imageScrollPane = new JScrollPane(imageLabel);
        mainPanel.add(imageScrollPane, BorderLayout.NORTH);

        // 中间：控制面板
        JPanel controlPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        controlPanel.setBorder(BorderFactory.createTitledBorder("控制面板"));
        controlPanel.setPreferredSize(new Dimension(800, 150));

        // 屏幕选择和点击延迟设置面板
        JPanel topControlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // 屏幕选择部分
        JPanel screenPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        screenPanel.add(new JLabel("选择目标屏幕:"));
        primaryScreenRadio = new JRadioButton("主屏幕", true);
        secondaryScreenRadio = new JRadioButton("副屏幕");
        ButtonGroup screenGroup = new ButtonGroup();
        screenGroup.add(primaryScreenRadio);
        screenGroup.add(secondaryScreenRadio);
        screenPanel.add(primaryScreenRadio);
        screenPanel.add(secondaryScreenRadio);
        topControlPanel.add(screenPanel);

        // 循环执行部分
        JPanel continuePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        continuePanel.add(new JLabel("选择是否循环执行:"));
        continueRadio = new JRadioButton("是");
        breakContinueRadio = new JRadioButton("否",true);
        ButtonGroup continueGroup = new ButtonGroup();
        continueGroup.add(continueRadio);
        continueGroup.add(breakContinueRadio);
        continuePanel.add(continueRadio);
        continuePanel.add(breakContinueRadio);
        topControlPanel.add(continuePanel);

        // 添加分隔符
//        topControlPanel.add(Box.createHorizontalStrut(30));

        // 点击延迟设置部分
        JPanel delayPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        delayPanel.add(new JLabel("点击间隔(ms):"));

        SpinnerModel delayModel = new SpinnerNumberModel(CLICK_DELAY_MS, 5, 4000, 50);
        JSpinner delaySpinner = new JSpinner(delayModel);
        delaySpinner.setPreferredSize(new Dimension(80, 20));
        delayPanel.add(delaySpinner);

        // 添加监听器保存设置
        delaySpinner.addChangeListener(e -> {
            CLICK_DELAY_MS = (double) delaySpinner.getValue();
            System.out.println("点击间隔更新为: " + CLICK_DELAY_MS + "ms");
        });

        topControlPanel.add(delayPanel);
        // 点击延迟设置部分

        JPanel confirmExecutionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        confirmExecutionPanel.add(new JLabel("开始等待时间(ms):"));

        SpinnerModel confirmExecutionModel = new SpinnerNumberModel(confirmExecutionMs, 100, 4000, 500);
        JSpinner confirmExecutionSpinner = new JSpinner(confirmExecutionModel);
        confirmExecutionSpinner.setPreferredSize(new Dimension(80, 20));
        confirmExecutionPanel.add(confirmExecutionSpinner);

        // 添加监听器保存设置
        confirmExecutionSpinner.addChangeListener(e -> {
            CLICK_DELAY_MS = (double) confirmExecutionSpinner.getValue();
            System.out.println("开始等待时间更新为: " + CLICK_DELAY_MS + "ms");
        });

        topControlPanel.add(confirmExecutionPanel);

        controlPanel.add(topControlPanel);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        startButton = new JButton("开始识别");
        confirmButton = new JButton("确认执行");
        cancelButton = new JButton("取消重置");

        confirmButton.setEnabled(false);

        startButton.addActionListener(e -> startRecognition());
        confirmButton.addActionListener(e -> confirmExecution());
        cancelButton.addActionListener(e -> resetApp());

        buttonPanel.add(startButton);
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        controlPanel.add(buttonPanel);

        // 提示区域
        JLabel hintLabel = new JLabel("<html><center>按 Shift+F7 暂停, Shift+F8 继续。Shift+C 开始识别，Shift+V 开始执行。Shift+A 预览调整，Shift+V 直接保存。<br>" +
                "Shift+Q取消循环执行，Shift+W开始循环执行。第一次开始循环需要手动开始</center></html>", JLabel.CENTER);
        hintLabel.setForeground(Color.BLUE);
        controlPanel.add(hintLabel);

        mainPanel.add(controlPanel, BorderLayout.CENTER);

        // 底部：控制台输出
        consoleArea = new JTextArea();
        consoleArea.setEditable(false);
        consoleArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        consoleArea.setBackground(new Color(240, 240, 240));

        JScrollPane consoleScrollPane = new JScrollPane(consoleArea);
        consoleScrollPane.setBorder(BorderFactory.createTitledBorder("控制台输出"));
        consoleScrollPane.setPreferredSize(new Dimension(800, 120));
        mainPanel.add(consoleScrollPane, BorderLayout.SOUTH);

        // 重定向控制台输出
        redirectConsoleOutput();

        add(mainPanel);
        // 设置窗口大小
        setSize(820, 900);
    }

    private void redirectConsoleOutput() {
        PrintStream consoleStream = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
                consoleArea.append(String.valueOf((char) b));
                consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
            }

            @Override
            public void write(byte[] b, int off, int len) {
                consoleArea.append(new String(b, off, len));
                consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
            }
        });

        System.setOut(consoleStream);
        System.setErr(consoleStream);
    }

    /**
     * 开始识别
     */
    private void startRecognition() {
        // 更新屏幕选择
        targetScreenIndex = primaryScreenRadio.isSelected() ? 0 : 1;
        ifContinue = continueRadio.isSelected();

        // 禁用按钮防止重复点击
        startButton.setEnabled(false);
        primaryScreenRadio.setEnabled(false);
        secondaryScreenRadio.setEnabled(false);
        breakContinueRadio.setEnabled(false);
        continueRadio.setEnabled(false);

        // 在后台线程执行识别任务
        new Thread(() -> {
            try {
                System.out.println("开始识别屏幕内容...");
                if (ifHide){
                    setVisible(false); // 隐藏窗口
                }

                // 添加200ms延迟，确保窗口完全隐藏
                Thread.sleep(200);

                // 执行识别逻辑
                performRecognition();

                // 完成后显示窗口
                SwingUtilities.invokeLater(() -> {
                    if (ifHide){
                        setVisible(true);
                    }
                    confirmButton.setEnabled(true);

                    // 显示轮廓图片
                    if (contourImage != null) {
                        Image scaledImage = contourImage.getScaledInstance(
                                // 缩放
                                (int) (contourImage.getWidth() / 2.4),
                                (int) (contourImage.getHeight() / 2.4),
                                Image.SCALE_SMOOTH);
                        ImageIcon icon = new ImageIcon(scaledImage);

//                        ImageIcon icon = new ImageIcon(contourImage);
                        imageLabel.setIcon(icon);
                        imageLabel.setText("");
                    }
                });

                // 尝试继续执行
                if (ifContinue){
                    Thread.sleep((long) continueTime);
                    confirmExecution();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                SwingUtilities.invokeLater(() -> {
                    if (ifHide){
                        setVisible(true);
                    }
                    resetApp();
                });
            }
        }).start();
    }

    private void performRecognition() throws Exception {
        // 1. 创建调试目录
        if (DEBUG_MODE) {
            new File(DEBUG_PATH).mkdirs();
        }

        // 2. 选择目标屏幕
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] screens = ge.getScreenDevices();

        if (targetScreenIndex >= screens.length) {
            System.err.println("警告: 使用主屏幕替代");
            targetScreenIndex = 0;
        }

        GraphicsDevice targetScreen = screens[targetScreenIndex];
        GraphicsConfiguration gc = targetScreen.getDefaultConfiguration();
        Rectangle screenRect = gc.getBounds();
        System.out.printf("目标屏幕区域: [x=%d, y=%d, w=%d, h=%d]%n",
                screenRect.x, screenRect.y, screenRect.width, screenRect.height);

        // 3. 捕获屏幕
        Robot robot = new Robot();
        BufferedImage screenImage = robot.createScreenCapture(screenRect);
        saveDebugImage(screenImage, "01_original.png");

        // 4. 颜色识别
        boolean[][] colorMap = detectColorRegions(screenImage);
        saveColorMapImage(screenImage, colorMap, "02_color_map.png");

        // 5. 区域分析和过滤
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
        if (targetRegion.size() < 1000){
            System.err.println("错误: 未找到有效目标区域:小于1000像素");
            return;
        }

        // 6. 轮廓检测
        List<Point> contour = findContour(targetRegion);
        saveContourImage(screenImage, contour, "03_contour_raw.png");

        // 7. 轮廓简化,转换为点坐标
        List<Point> simplified = simplifyContour(contour);
//        List<Point> simplified = simplifyContourNew(contour);
        saveContourImage(screenImage, simplified, "04_contour_simplified.png");

        System.out.println("简化轮廓点数量: " + simplified.size());

        // 8. 加载轮廓图片用于显示
        contourImage = ImageIO.read(new File(DEBUG_PATH + "04_contour_simplified.png"));
    }

    /**
     * 确认执行
     */
    private void confirmExecution() {
        confirmButton.setEnabled(false);
        // 让重置按钮一直可以按
//        cancelButton.setEnabled(false);

        new Thread(() -> {
            try {

                if (ifHide){
                    setVisible(false); // 隐藏窗口
                }

                // 执行鼠标点击操作
                performMouseClicks();

                System.out.println("操作成功完成!");

                // 暂停时重新显示窗口
                SwingUtilities.invokeLater(() -> {
                    if (ifHide){
                        setVisible(true);
                    }
                    toFront(); // 将窗口置于最前端
                    System.out.println("窗口已显示");
                });

                SwingUtilities.invokeLater(this::resetApp);

            } catch (Exception ex) {
                ex.printStackTrace();
                SwingUtilities.invokeLater(() -> {
                    if (ifHide){
                        setVisible(true);
                    }
                    resetApp();
                });
            }
        }).start();
    }

    private void resetApp() {
        // 重置所有状态
        imageLabel.setIcon(null);
        imageLabel.setText("等待识别结果...");
        consoleArea.setText("");
        // 禁用开始按钮
        startButton.setEnabled(true);
        // 禁用确认按钮
        confirmButton.setEnabled(false);
        primaryScreenRadio.setEnabled(true);
        secondaryScreenRadio.setEnabled(true);
        breakContinueRadio.setEnabled(true);
        continueRadio.setEnabled(true);

        contourImage = null;
//        breakContinue.set(true);

        System.out.println("应用程序已重置");
    }

    private void performMouseClicks() throws Exception {
        // 重新获取屏幕信息
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice targetScreen = ge.getScreenDevices()[targetScreenIndex];
        GraphicsConfiguration gc = targetScreen.getDefaultConfiguration();
        Rectangle screenRect = gc.getBounds();

        // 读取简化轮廓点
        List<Point> simplified = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(DEBUG_PATH + "contour_points.txt"))) {
            while (scanner.hasNextLine()) {
                String[] coords = scanner.nextLine().split(",");
                if (coords.length == 2) {
                    simplified.add(new Point(
                            Integer.parseInt(coords[0]),
                            Integer.parseInt(coords[1])
                    ));
                }
            }
        }
        // 转换为全局坐标
        List<Point> globalPoints = convertToGlobalCoordinates(simplified, screenRect);
        // 为了取消 ： !current.equals(start)  导致的最终轮廓闭合，所以最后的一个点删除掉。
        globalPoints.remove(globalPoints.size() - 1);

        // 执行鼠标点击
        Robot robot = new Robot();
        System.out.println("将在 "+confirmExecutionMs+" 秒后开始点击操作...");
        robot.delay((int) confirmExecutionMs); // 准备时间

        for (int i = 0; i < globalPoints.size(); i++) {
            checkPauseState();

            Point p = globalPoints.get(i);
            robot.mouseMove(p.x, p.y);
            System.out.printf("点击 #%d: (%d, %d)%n", i + 1, p.x, p.y);

            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.delay(50);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

            if (i < globalPoints.size() - 1) {
                long start = System.currentTimeMillis();
                while (System.currentTimeMillis() - start < CLICK_DELAY_MS) {
                    robot.delay(50);
                    if (isPaused.get()) {
                        checkPauseState();
                    }
                }
            }
        }

        if (ifContinue){
            Thread.sleep((long) waitSaveTime);
            // 这是直接保存围栏的点。
            Point savePoint = new Point(1906, 187);
            save(savePoint,screenRect, robot);

            // 清楚逻辑

            // 之后再执行识别
            Thread.sleep((long) waitSaveTime);
            resetApp();
            if (!breakContinue.get()){
                startRecognition();
            }
        }


    }

    /**
     * 点击 直接保存围栏
     * @param screenRect
     * @param robot
     */
    private void save(Point savePoint,Rectangle screenRect, Robot robot) {
        if (screenRect == null){
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice targetScreen = ge.getScreenDevices()[targetScreenIndex];
            GraphicsConfiguration gc = targetScreen.getDefaultConfiguration();
            screenRect = gc.getBounds();
        }
        if (robot == null){
            try {
                robot = new Robot();
            } catch (Exception e) {
                System.err.println("直接保存围栏功能失效: " + e.getMessage());
            }

        }

        List<Point> savePointList = new ArrayList<>();
        savePointList.add(savePoint);
        // 转换为全局坐标
        List<Point> savePoints = convertToGlobalCoordinates(savePointList, screenRect);

        Point p = savePoints.get(0);

        robot.mouseMove(p.x, p.y);
        System.out.printf("点击 [直接保存围栏]: (%d, %d)%n", p.x, p.y);

        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.delay(50);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    // ================= 暂停控制功能 =================

    private void startKeyboardListener() {
        try {
            GlobalScreen.registerNativeHook();
            System.out.println("全局键盘钩子已注册");
        } catch (NativeHookException ex) {
            System.err.println("注册全局钩子失败: " + ex.getMessage());
            System.err.println("请检查是否已安装必要的本地库");
            return;
        }

        GlobalScreen.addNativeKeyListener(new NativeKeyListener() {
            @Override
            public void nativeKeyPressed(NativeKeyEvent e) {
                int keyCode = e.getKeyCode();
                boolean shiftPressed = (e.getModifiers() & NativeKeyEvent.SHIFT_MASK) != 0;

                if (shiftPressed && keyCode == PAUSE_KEY) {
                    System.out.println("检测到 暂停动作");
                    pauseExecution();
                } else if (shiftPressed && keyCode == RESUME_KEY) {
                    System.out.println("检测到 继续动作");
                    resumeExecution();
                } else if (shiftPressed && keyCode == BREAK_CONTINUE_KEY) {
                    System.out.println("检测到 停止循环动作");
                    breakContinue.set(true);
                } else if (shiftPressed && keyCode == CONTINUE_KEY) {
                    System.out.println("检测到 继续循环动作");
                    breakContinue.set(false);
                } else if (shiftPressed && keyCode == START_KEY) {
                    System.out.println("检测到 开始识别动作");
                    startRecognition();
                } else if (shiftPressed && keyCode == START_IDENTITY_KEY) {
                    System.out.println("检测到 开始执行动作");
                    confirmExecution();
                } else if (shiftPressed && keyCode == SAVE_KEY) {
                    System.out.println("检测到 开始直接保存围栏动作");
                    Point savePoint = new Point(1906, 187);
                    save(savePoint,null, null);
                } else if (shiftPressed && keyCode == CHECK_KEY) {
                    System.out.println("检测到 开始预览调整动作");
                    Point checkPoint = new Point(1753, 187);
                    save(checkPoint,null, null);
                }
            }

            @Override
            public void nativeKeyReleased(NativeKeyEvent e) {
            }

            @Override
            public void nativeKeyTyped(NativeKeyEvent e) {
            }
        });
    }

    @Override
    public void dispose() {
        try {
            GlobalScreen.unregisterNativeHook();
            System.out.println("全局键盘钩子已注销");
        } catch (NativeHookException e) {
            e.printStackTrace();
        }
        super.dispose();
    }

    // 暂停方法
    private void pauseExecution() {
        isPaused.set(true);
        System.out.println("\n===== 程序已暂停 (Shift+F7) =====");

        // 暂停时重新显示窗口
        SwingUtilities.invokeLater(() -> {
            if (ifHide){
                setVisible(true);
            }
            toFront(); // 将窗口置于最前端
            System.out.println("窗口已显示");
        });
    }

    // 继续方法
    private void resumeExecution() {
//        synchronized (pauseLock) {
//            isPaused.set(false);
//            pauseLock.notifyAll();
//            System.out.println("\n===== 程序已继续 (Shift+F8) =====");
//        }

        // 继续执行时再次隐藏窗口
        SwingUtilities.invokeLater(() -> {
            if (ifHide){
                setVisible(false);
                System.out.println("窗口已隐藏");
            }

            // 在后台线程添加200ms延迟
            new Thread(() -> {
                try {
                    Thread.sleep(200);
                    System.out.println("200ms延迟完成");

                    // 继续执行
                    synchronized (pauseLock) {
                        isPaused.set(false);
                        pauseLock.notifyAll();
                        System.out.println("\n===== 程序已继续 (Shift+F8) =====");
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        });
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

            // 计算实际步长距离     低精度
//            double stepDistance = filtered.get(i - 1).distance(current);
            // 方案1              高精度
            double stepDistance = lastAdded.distance(current);  // 关键修改

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

            // 保存轮廓点到文件
            try (PrintWriter writer = new PrintWriter(DEBUG_PATH + "contour_points.txt")) {
                for (Point p : contour) {
                    writer.println(p.x + "," + p.y);
                }
            } catch (Exception e) {
                System.err.println("保存轮廓点失败: " + e.getMessage());
            }

        } catch (Exception e) {
            System.err.println("保存轮廓图像失败: " + e.getMessage());
        }
    }


}