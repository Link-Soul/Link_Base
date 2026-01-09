<template>
  <div class="desktop" :style="desktopStyle">
    <!-- 桌面背景 -->
    <div
      class="desktop-background"
      @contextmenu.prevent="handleDesktopContextMenu"
    >
      <img
        :src="wallpaper.image"
        alt="Wallpaper"
        class="background-image"
        draggable="false"
        @error="handleWallpaperError"
      />
    </div>

    <!-- 桌面图标网格 -->
    <div
      v-if="settings.showDesktopIcons"
      class="desktop-icons"
      :class="{ 'show-grid': settings.showGrid }"
    >
      <div class="icon-grid">
        <div
          v-for="app in favoriteApps"
          :key="app.id"
          class="desktop-icon"
          @dblclick="launchApp(app.id)"
          @contextmenu.prevent="showIconContextMenu($event, app)"
        >
          <div class="icon-wrapper">
            <span class="icon">{{ app.icon }}</span>
          </div>
          <span class="icon-name">{{ app.name }}</span>
        </div>
      </div>
    </div>

    <!-- 窗口管理器 -->
    <WindowManager />

    <!-- Dock 栏 -->
    <Dock />

    <!-- 应用启动器 -->
    <AppLauncher v-if="showAppLauncher" @close="closeAppLauncher" />

    <!-- 右键菜单 -->
    <ContextMenu
      v-if="contextMenu.show"
      :x="contextMenu.x"
      :y="contextMenu.y"
      :items="contextMenu.items"
      @close="contextMenu.show = false"
      @select="handleContextMenuSelect"
    />
  </div>
</template>

<script setup>
import { computed, onMounted, onUnmounted, ref, reactive } from "vue";
import { useDesktopStore } from "@/stores/desktop";
import { useAppsStore } from "@/stores/apps";
import { useThemeStore } from "@/stores/theme";
import WindowManager from "@/components/Desktop/WindowManager.vue";
import Dock from "@/components/Desktop/Dock.vue";
import AppLauncher from "@/components/Desktop/AppLauncher.vue";
import ContextMenu from "@/components/Common/ContextMenu.vue";
import { settingsApi, desktopApi } from "@/services/api";

const desktopStore = useDesktopStore();
const appsStore = useAppsStore();
const themeStore = useThemeStore();

const showAppLauncher = ref(false);

const contextMenu = reactive({
  show: false,
  x: 0,
  y: 0,
  items: [],
  target: null,
});

// 计算属性
const wallpaper = computed(() => desktopStore.wallpaper);
const settings = computed(() => desktopStore.settings);
const favoriteApps = computed(() => appsStore.favoriteAppsList);

const desktopStyle = computed(() => ({
  ...themeStore.cssVariables,
}));

const gradientStyle = computed(() => ({
  background: `linear-gradient(135deg, ${wallpaper.value.gradient.start}, ${wallpaper.value.gradient.end})`,
}));

// 方法
const launchApp = (appId) => {
  appsStore.launchApp(appId);

  // 使用API启动应用
  // desktopApi
  //   .launchApp(appId)
  //   .then(() => {
  //     appsStore.launchApp(appId);
  //   })
  //   .catch((error) => {
  //     console.error("Failed to launch app via API:", error);
  //     // 降级到本地存储
  //     appsStore.launchApp(appId);
  //   });
};

const showIconContextMenu = (event, app) => {
  // 隐藏默认右键菜单
  event.preventDefault();


  contextMenu.show = true;
  contextMenu.x = event.clientX;
  contextMenu.y = event.clientY;
  contextMenu.target = app;
  contextMenu.items = [
    { label: "打开", action: "open", icon: "📂" },
    { label: "显示简介", action: "info", icon: "ℹ️" },
    { separator: true },
    {
      label: appsStore.favoriteApps.includes(app.id)
        ? "从收藏移除"
        : "添加到收藏",
      action: "toggle-favorite",
      icon: "⭐",
    },
    { separator: true },
    { label: "卸载", action: "uninstall", icon: "❌" },
  ];
};

const handleContextMenuSelect = (action) => {
  if (!contextMenu.target) return;

  switch (action) {
    case "open":
      launchApp(contextMenu.target.id);
      break;
    case "toggle-favorite":
      appsStore.toggleFavorite(contextMenu.target.id);
      break;
    case "info":
      showAppInfo(contextMenu.target);
      break;
    case "uninstall":
      // 使用API卸载应用
      desktopApi
        .uninstallApp(contextMenu.target.id)
        .then(() => {
          appsStore.uninstallApp(contextMenu.target.id);
        })
        .catch((error) => {
          console.error("Failed to uninstall app via API:", error);
          // 降级到本地存储
          appsStore.uninstallApp(contextMenu.target.id);
        });
      break;
    case "open-wallpaper":
      appsStore.launchApp("wallpaper");
      break;
    case "open-gacha":
      appsStore.launchApp("gacha");
      break;
    case "open-settings":
      appsStore.launchApp("settings");
      break;
    case "view-options":
      appsStore.launchApp("settings");
      break;
  }

  contextMenu.show = false;
};

const showAppInfo = (app) => {
  // 显示应用信息弹窗
  console.log("App info:", app);
};

const handleDesktopContextMenu = (event) => {
  // 显示桌面右键菜单
  contextMenu.show = true;
  contextMenu.x = event.clientX;
  contextMenu.y = event.clientY;
  contextMenu.target = "desktop";
  contextMenu.items = [
    { label: "壁纸设置", action: "open-wallpaper", icon: "🖼️" },
    { label: "抽卡数据统计", action: "open-gacha", icon: "🎰" },
    { separator: true },
    { label: "系统设置", action: "open-settings", icon: "⚙️" },
    { separator: true },
    { label: "显示视图选项", action: "view-options", icon: "👁️" },
  ];
};

// 键盘快捷键
const handleKeydown = (event) => {
  // Cmd/Ctrl + Space 打开应用启动器
  if ((event.metaKey || event.ctrlKey) && event.code === "Space") {
    event.preventDefault();
    showAppLauncher.value = !showAppLauncher.value;
  }

  // Esc 关闭应用启动器
  if (event.code === "Escape" && showAppLauncher.value) {
    closeAppLauncher();
  }
};

const closeAppLauncher = () => {
  showAppLauncher.value = false;
};

// 处理拖放图片设置壁纸
/**
 * 处理拖放图片设置壁纸
 * @param {DragEvent} event - 拖放事件
 */
const handleDrop = (event) => {
  const files = event.dataTransfer.files;
  if (files.length > 0) {
    const file = files[0];
    if (file.type.startsWith("image/")) {
      // 使用API上传图片并设置壁纸
      import("@/services/api").then(({ uploadApi, settingsApi }) => {
        uploadApi
          .uploadSysFile(file)
          .then((uploadResult) => {
            const wallpaperData = {
              image: uploadResult.fileUrl || uploadResult.url,
            };
            // 保存到系统配置
            settingsApi
              .updateSetting("wallpaper", JSON.stringify(wallpaperData))
              .then(() => {
                desktopStore.changeImageWallpaper(wallpaperData.image);
              });
          })
          .catch((error) => {
            console.error("Failed to upload wallpaper via API:", error);
            // 降级到本地存储
            const reader = new FileReader();
            reader.onload = (e) => {
              desktopStore.changeImageWallpaper(e.target.result);
            };
            reader.readAsDataURL(file);
          });
      });
    }
  }
};

onMounted(() => {
  // 初始化主题
  themeStore.applyTheme();

  // 加载桌面数据从API
  loadDesktopData();

  // 添加键盘事件监听
  document.addEventListener("keydown", handleKeydown);
});

// 处理壁纸加载失败
const handleWallpaperError = () => {
  // 当壁纸加载失败时，使用默认壁纸
  desktopStore.changeImageWallpaper("/img/paper.jpg");
};

// 从API加载桌面数据
/**
 * 从API加载桌面数据
 */
const loadDesktopData = async () => {
  try {
    // 批量加载所有设置
    const settingsMap = await settingsApi.getSettingsMap();
    if (settingsMap) {
      // 加载壁纸配置
      if (settingsMap.wallpaper) {
        desktopStore.changeImageWallpaper(settingsMap.wallpaper);
      }

      // 加载显示桌面图标设置
      if (settingsMap.showDesktopIcons) {
        try {
          desktopStore.settings.showDesktopIcons = JSON.parse(
            settingsMap.showDesktopIcons
          );
        } catch (parseError) {
          console.error("Failed to parse showDesktopIcons config:", parseError);
        }
      }

      // 加载显示网格设置
      if (settingsMap.showGrid) {
        try {
          desktopStore.settings.showGrid = JSON.parse(settingsMap.showGrid);
        } catch (parseError) {
          console.error("Failed to parse showGrid config:", parseError);
        }
      }

      // 加载主题模式设置
      if (settingsMap.themeMode) {
        try {
          themeStore.setTheme(settingsMap.themeMode);
        } catch (parseError) {
          console.error("Failed to parse themeMode config:", parseError);
        }
      }
    }

    // 加载应用列表
    const apps = await desktopApi.getApps();
    if (apps) {
      console.log("Loaded apps from API:", apps);
    }
  } catch (error) {
    console.error("Failed to load desktop data from API:", error);
    // 降级到本地存储
  }
};

onUnmounted(() => {
  document.removeEventListener("keydown", handleKeydown);
});
</script>

<style scoped>
.desktop {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  overflow: hidden;
  background: var(--color-background);
}

.desktop-background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: -1;
}

.background-color,
.background-gradient,
.background-image {
  width: 100%;
  height: 100%;
}

.background-image {
  object-fit: cover;
  object-position: center;
}

.desktop-icons {
  position: absolute;
  top: 20px;
  left: 20px;
  right: 20px;
  bottom: 100px;
}

.icon-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, 80px);
  gap: 20px;
  width: fit-content;
  pointer-events: auto;
}

.desktop-icon {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 8px;
  border-radius: var(--border-radius);
  cursor: pointer;
  transition: all var(--duration-normal) var(--easing);
  user-select: none;
}

.desktop-icon:hover {
  background: var(--color-surface);
  backdrop-filter: blur(10px);
}

.icon-wrapper {
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 4px;
}

.icon {
  font-size: 32px;
  line-height: 1;
}

.icon-name {
  font-size: var(--font-size-xs);
  color: var(--color-text);
  text-align: center;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
  max-width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.show-grid .icon-grid {
  background-image: linear-gradient(
      rgba(255, 255, 255, 0.1) 1px,
      transparent 1px
    ),
    linear-gradient(90deg, rgba(255, 255, 255, 0.1) 1px, transparent 1px);
  background-size: 80px 80px;
  border-radius: var(--border-radius);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .icon-grid {
    grid-template-columns: repeat(auto-fill, 60px);
    gap: 15px;
  }

  .icon-wrapper {
    width: 36px;
    height: 36px;
  }

  .icon {
    font-size: 24px;
  }

  .icon-name {
    font-size: 10px;
  }
}
</style>
