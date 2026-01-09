-
<template>
  <div class="dock" :class="{ expanded: isExpanded }">
    <div class="dock-container">
      <!-- 应用图标 -->
      <div
        v-for="app in dockApps"
        :key="app.id"
        class="dock-item"
        :class="{ active: isAppActive(app.id), running: isAppRunning(app.id) }"
        @click="handleDockClick(app)"
        @mouseenter="hoveredApp = app.id"
        @mouseleave="hoveredApp = null"
        @contextmenu.prevent="showAppContextMenu($event, app)"
      >
        <div class="app-icon">
          <span class="icon">{{ app.icon }}</span>
          <div v-if="isAppRunning(app.id)" class="running-indicator"></div>
        </div>

        <!-- 应用名称提示 -->
        <transition name="tooltip">
          <div v-if="hoveredApp === app.id" class="app-tooltip">
            {{ app.name }}
          </div>
        </transition>
      </div>

      <!-- 分隔线 -->
      <div class="dock-separator"></div>

      <!-- 系统应用 -->
      <div
        v-for="systemApp in systemApps"
        :key="systemApp.id"
        class="dock-item system-app"
        @click="handleSystemAppClick(systemApp)"
        @mouseenter="hoveredApp = systemApp.id"
        @mouseleave="hoveredApp = null"
      >
        <div class="app-icon">
          <span class="icon">{{ systemApp.icon }}</span>
        </div>

        <transition name="tooltip">
          <div v-if="hoveredApp === systemApp.id" class="app-tooltip">
            {{ systemApp.name }}
          </div>
        </transition>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, ref, reactive, onMounted, onUnmounted } from "vue";
import { useDesktopStore } from "@/stores/desktop";
import { useAppsStore } from "@/stores/apps";

const desktopStore = useDesktopStore();
const appsStore = useAppsStore();

const hoveredApp = ref(null);
const isExpanded = ref(false);

// Dock 应用
const dockApps = computed(() =>
  desktopStore.dockApps.filter((app) => app.id !== "trash")
);

// 系统应用
const systemApps = computed(() => [
  // { id: "finder", name: "访达", icon: "🗂️" },
  // { id: "gacha", name: "抽卡数据统计", icon: "🎰" },
  { id: "settings", name: "系统偏好设置", icon: "⚙️" },
]);

// 检查应用是否活动
const isAppActive = (appId) => {
  return (
    desktopStore.activeWindowId &&
    desktopStore.windows.some(
      (w) => w.id === desktopStore.activeWindowId && w.appId === appId
    )
  );
};

// 检查应用是否正在运行
const isAppRunning = (appId) => {
  return desktopStore.windows.some((w) => w.appId === appId && !w.isMinimized);
};

// 处理 Dock 点击
const handleDockClick = (app) => {
  const runningWindow = desktopStore.windows.find((w) => w.appId === app.id);

  if (runningWindow) {
    if (runningWindow.isMinimized) {
      // 恢复最小化窗口
      desktopStore.minimizeWindow(runningWindow.id);
      desktopStore.setActiveWindow(runningWindow.id);
    } else if (desktopStore.activeWindowId === runningWindow.id) {
      // 最小化当前活动窗口
      desktopStore.minimizeWindow(runningWindow.id);
    } else {
      // 切换到该窗口
      desktopStore.setActiveWindow(runningWindow.id);
    }
  } else {
    // 启动新应用
    appsStore.launchApp(app.id);
  }
};

// 处理系统应用点击
const handleSystemAppClick = (systemApp) => {
  switch (systemApp.id) {
    case "launchpad":
      // 打开应用启动器
      // 发送自定义事件，让父组件处理
      const launchpadEvent = new CustomEvent("open-launchpad");
      document.dispatchEvent(launchpadEvent);
      break;
    case "gacha":
      // 打开抽卡数据统计应用
      appsStore.launchApp("gacha");
      break;
    case "settings":
      // 打开系统设置
      appsStore.launchApp("settings");
      break;
  }
};

// 显示应用右键菜单
const showAppContextMenu = (event, app) => {
  // 实现右键菜单逻辑
  console.log("Show context menu for app:", app);
};

// 监听鼠标移动来扩展 Dock
const handleMouseMove = (event) => {
  const dock = document.querySelector(".dock");
  const rect = dock.getBoundingClientRect();
  const distance = Math.abs(event.clientY - rect.top);

  if (distance < 50) {
    isExpanded.value = true;
  } else if (distance > 100) {
    isExpanded.value = false;
  }
};

// 生命周期
onMounted(() => {
  document.addEventListener("mousemove", handleMouseMove);
});

onUnmounted(() => {
  document.removeEventListener("mousemove", handleMouseMove);
});
</script>

<style scoped>
.dock {
  position: fixed;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  z-index: 1000;
  pointer-events: none;
}

.dock-container {
  display: flex;
  align-items: flex-end;
  gap: 4px;
  padding: 8px;
  background: var(--color-dock-background);
  backdrop-filter: var(--backdrop-filter);
  border-radius: calc(var(--border-radius) * 2);
  box-shadow: 0 8px 32px var(--color-shadow);
  border: 1px solid var(--color-border);
  pointer-events: auto;
  transition: all var(--duration-normal) var(--easing);
}

.dock-item {
  position: relative;
  width: 50px;
  height: 50px;
  cursor: pointer;
  transition: all var(--duration-fast) var(--easing);
}

.dock-item:hover {
  transform: translateY(-8px) scale(1.2);
}

.dock-item.active {
  transform: translateY(-4px) scale(1.1);
}

.dock.expanded .dock-item {
  width: 60px;
  height: 60px;
}

.app-icon {
  position: relative;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: calc(var(--border-radius) / 2);
  background: var(--color-surface);
  transition: all var(--duration-fast) var(--easing);
}

.dock-item:hover .app-icon,
.dock-item.active .app-icon {
  background: var(--color-primary);
  box-shadow: 0 4px 16px var(--color-shadow);
}

.icon {
  font-size: 24px;
  line-height: 1;
  transition: all var(--duration-fast) var(--easing);
}

.dock-item:hover .icon,
.dock-item.active .icon {
  transform: scale(1.1);
}

.running-indicator {
  position: absolute;
  bottom: -4px;
  left: 50%;
  transform: translateX(-50%);
  width: 4px;
  height: 4px;
  background: var(--color-primary);
  border-radius: 50%;
  box-shadow: 0 0 8px var(--color-primary);
}

.dock-separator {
  width: 1px;
  height: 30px;
  background: var(--color-border);
  margin: 0 8px;
}

.system-app .app-icon {
  background: transparent;
}

.app-tooltip {
  position: absolute;
  bottom: 100%;
  left: 50%;
  transform: translateX(-50%);
  margin-bottom: 8px;
  padding: 4px 8px;
  background: var(--color-surface);
  color: var(--color-text);
  border-radius: calc(var(--border-radius) / 2);
  font-size: var(--font-size-xs);
  white-space: nowrap;
  pointer-events: none;
  z-index: 1001;
}

/* 动画 */
.tooltip-enter-active,
.tooltip-leave-active {
  transition: all var(--duration-fast) var(--easing);
}

.tooltip-enter-from,
.tooltip-leave-to {
  opacity: 0;
  transform: translateX(-50%) translateY(4px);
}

@keyframes shake {
  0%,
  100% {
    transform: translateX(-50%) rotate(0deg);
  }
  25% {
    transform: translateX(-52%) rotate(-5deg);
  }
  75% {
    transform: translateX(-48%) rotate(5deg);
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .dock {
    bottom: 10px;
  }

  .dock-item {
    width: 40px;
    height: 40px;
  }

  .icon {
    font-size: 20px;
  }

  .dock.expanded .dock-item {
    width: 48px;
    height: 48px;
  }
}
</style>
