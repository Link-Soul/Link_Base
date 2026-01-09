<template>
  <div
    ref="windowRef"
    class="window"
    :class="{
      active: isActive,
      maximized: isMaximized,
      minimized: isMinimized,
    }"
    :style="windowStyle"
    @mousedown="handleWindowClick"
  >
    <!-- 窗口标题栏 -->
    <div
      class="window-header"
      @mousedown="startDrag"
      @dblclick="toggleMaximize"
    >
      <div class="window-title">
        <span class="app-icon">{{ windowData.icon || "📱" }}</span>
        <span class="title-text">{{ windowData.title }}</span>
      </div>

      <div class="window-controls">
        <button
          class="control-button minimize"
          @click.stop="handleMinimize"
          :title="'最小化'"
        >
          <span class="control-icon">−</span>
        </button>
        <button
          class="control-button maximize"
          @click.stop="handleMaximize"
          :title="isMaximized ? '还原' : '最大化'"
        >
          <span class="control-icon">{{ isMaximized ? "□" : "□" }}</span>
        </button>
        <button
          class="control-button close"
          @click.stop="handleClose"
          :title="'关闭'"
        >
          <span class="control-icon">×</span>
        </button>
      </div>
    </div>

    <!-- 窗口内容区域 -->
    <div class="window-content">
      <Suspense>
        <template #default>
          <component
            v-if="currentComponent"
            :is="currentComponent"
            v-bind="windowData.props"
            @window-action="handleWindowAction"
          />
          <div v-else class="empty-content">
            <p>窗口内容加载中...</p>
          </div>
        </template>
        <template #fallback>
          <div class="loading-content">
            <p>正在加载应用...</p>
          </div>
        </template>
      </Suspense>
    </div>

    <!-- 调整大小手柄 -->
    <div
      v-if="!isMaximized && windowData.resizable !== false"
      class="resize-handle resize-se"
      @mousedown="startResize('se')"
    ></div>
    <div
      v-if="!isMaximized && windowData.resizable !== false"
      class="resize-handle resize-e"
      @mousedown="startResize('e')"
    ></div>
    <div
      v-if="!isMaximized && windowData.resizable !== false"
      class="resize-handle resize-s"
      @mousedown="startResize('s')"
    ></div>
  </div>
</template>

<script setup>
import {
  ref,
  computed,
  nextTick,
  onMounted,
  onUnmounted,
  defineAsyncComponent,
} from "vue";

const props = defineProps({
  windowData: {
    type: Object,
    required: true,
  },
  isActive: {
    type: Boolean,
    default: false,
  },
});

// 动态导入组件
const getComponent = (componentName) => {
  const componentMap = {
    FilesApp: () => import("@/components/Apps/FilesApp.vue"),
    NotesApp: () => import("@/components/Apps/NotesApp.vue"),
    SettingsApp: () => import("@/components/Apps/SettingsApp.vue"),

    TerminalApp: () => import("@/components/Apps/PlaceholderApp.vue"),
    BrowserApp: () => import("@/components/Apps/PlaceholderApp.vue"),
    MusicApp: () => import("@/components/Apps/PlaceholderApp.vue"),
    CalendarApp: () => import("@/components/Apps/PlaceholderApp.vue"),
    GachaApp: () => import("@/components/Apps/GachaApp.vue"),
  };

  const loader =
    componentMap[componentName] ||
    (() => import("@/components/Apps/PlaceholderApp.vue"));
  return defineAsyncComponent(loader);
};

const currentComponent = computed(() => {
  if (props.windowData.component) {
    return getComponent(props.windowData.component);
  }
  return null;
});

const emit = defineEmits([
  "close",
  "minimize",
  "maximize",
  "focus",
  "move",
  "resize",
]);

const windowRef = ref(null);
const isDragging = ref(false);
const isResizing = ref(false);
const resizeDirection = ref("");
const dragStart = ref({ x: 0, y: 0, windowX: 0, windowY: 0 });
const resizeStart = ref({ width: 0, height: 0, x: 0, y: 0 });

// 计算属性
const isMaximized = computed(() => props.windowData.isMaximized);
const isMinimized = computed(() => props.windowData.isMinimized);

const windowStyle = computed(() => {
  if (isMaximized.value) {
    return {
      top: "0",
      left: "0",
      width: "100vw",
      height: "100vh",
      transform: "none",
      zIndex: props.windowData.zIndex || 1000,
    };
  }

  return {
    top: `${props.windowData.y || 100}px`,
    left: `${props.windowData.x || 100}px`,
    width: `${props.windowData.width || 800}px`,
    height: `${props.windowData.height || 600}px`,
    transform: isMinimized.value ? "scale(0)" : "scale(1)",
    zIndex: props.windowData.zIndex || 1000,
  };
});

// 方法
const handleWindowClick = () => {
  if (!props.isActive) {
    emit("focus", props.windowData.id);
  }
};

const handleClose = () => {
  emit("close", props.windowData.id);
};

const handleMinimize = () => {
  emit("minimize", props.windowData.id);
};

const handleMaximize = () => {
  emit("maximize", props.windowData.id);
};

const toggleMaximize = () => {
  emit("maximize", props.windowData.id);
};

const handleWindowAction = (action) => {
  switch (action) {
    case "close":
      handleClose();
      break;
    case "minimize":
      handleMinimize();
      break;
    case "maximize":
      handleMaximize();
      break;
  }
};

// 拖拽功能
const startDrag = (event) => {
  if (isMaximized.value) return;

  isDragging.value = true;
  dragStart.value = {
    x: event.clientX,
    y: event.clientY,
    windowX: props.windowData.x,
    windowY: props.windowData.y,
  };

  document.addEventListener("mousemove", onDrag);
  document.addEventListener("mouseup", stopDrag);
  event.preventDefault();
};

const onDrag = (event) => {
  if (!isDragging.value) return;

  const deltaX = event.clientX - dragStart.value.x;
  const deltaY = event.clientY - dragStart.value.y;

  const newX = Math.max(0, dragStart.value.windowX + deltaX);
  const newY = Math.max(0, dragStart.value.windowY + deltaY);

  emit("move", {
    windowId: props.windowData.id,
    x: newX,
    y: newY,
  });
};

const stopDrag = () => {
  isDragging.value = false;
  document.removeEventListener("mousemove", onDrag);
  document.removeEventListener("mouseup", stopDrag);
};

// 调整大小功能
const startResize = (direction) => {
  isResizing.value = true;
  resizeDirection.value = direction;
  resizeStart.value = {
    width: props.windowData.width,
    height: props.windowData.height,
    x: props.windowData.x,
    y: props.windowData.y,
    mouseX: event.clientX,
    mouseY: event.clientY,
  };

  document.addEventListener("mousemove", onResize);
  document.addEventListener("mouseup", stopResize);
  event.preventDefault();
};

const onResize = (event) => {
  if (!isResizing.value) return;

  const deltaX = event.clientX - resizeStart.value.mouseX;
  const deltaY = event.clientY - resizeStart.value.mouseY;

  let newWidth = resizeStart.value.width;
  let newHeight = resizeStart.value.height;
  let newX = resizeStart.value.x;
  let newY = resizeStart.value.y;

  switch (resizeDirection.value) {
    case "se":
      newWidth = Math.max(
        props.windowData.minWidth || 400,
        resizeStart.value.width + deltaX
      );
      newHeight = Math.max(
        props.windowData.minHeight || 300,
        resizeStart.value.height + deltaY
      );
      break;
    case "e":
      newWidth = Math.max(
        props.windowData.minWidth || 400,
        resizeStart.value.width + deltaX
      );
      break;
    case "s":
      newHeight = Math.max(
        props.windowData.minHeight || 300,
        resizeStart.value.height + deltaY
      );
      break;
  }

  emit("resize", {
    windowId: props.windowData.id,
    width: newWidth,
    height: newHeight,
  });
};

const stopResize = () => {
  isResizing.value = false;
  resizeDirection.value = "";
  document.removeEventListener("mousemove", onResize);
  document.removeEventListener("mouseup", stopResize);
};

// 键盘事件处理
const handleKeydown = (event) => {
  if (!props.isActive) return;

  // Cmd/Ctrl + W 关闭窗口
  if ((event.metaKey || event.ctrlKey) && event.key === "w") {
    event.preventDefault();
    handleClose();
  }

  // Cmd/Ctrl + M 最小化窗口
  if ((event.metaKey || event.ctrlKey) && event.key === "m") {
    event.preventDefault();
    handleMinimize();
  }

  // F11 或 Cmd/Ctrl + Enter 最大化/还原
  if (
    event.key === "F11" ||
    ((event.metaKey || event.ctrlKey) && event.key === "Enter")
  ) {
    event.preventDefault();
    handleMaximize();
  }
};

onMounted(() => {
  document.addEventListener("keydown", handleKeydown);
});

onUnmounted(() => {
  document.removeEventListener("keydown", handleKeydown);
  document.removeEventListener("mousemove", onDrag);
  document.removeEventListener("mouseup", stopDrag);
  document.removeEventListener("mousemove", onResize);
  document.removeEventListener("mouseup", stopResize);
});
</script>

<style scoped>
.window {
  position: absolute;
  background: var(--color-window-background);
  backdrop-filter: var(--backdrop-filter);
  border-radius: var(--border-radius);
  box-shadow: 0 10px 40px var(--color-shadow);
  border: 1px solid var(--color-border);
  overflow: hidden;
  transition: transform var(--duration-normal) var(--easing);
  display: flex;
  flex-direction: column;
  min-width: 400px;
  min-height: 300px;
}

.window.active {
  box-shadow: 0 15px 50px var(--color-shadow);
}

.window.maximized {
  border-radius: 0;
}

.window.minimized {
  pointer-events: none;
}

.window-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8px 12px;
  background: var(--color-surface);
  border-bottom: 1px solid var(--color-border);
  cursor: move;
  user-select: none;
  -webkit-app-region: drag;
}

.window-title {
  display: flex;
  align-items: center;
  gap: 8px;
  flex: 1;
  overflow: hidden;
}

.app-icon {
  font-size: 16px;
  line-height: 1;
}

.title-text {
  font-size: var(--font-size-sm);
  color: var(--color-text);
  font-weight: 500;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.window-controls {
  display: flex;
  align-items: center;
  gap: 8px;
  -webkit-app-region: no-drag;
}

.control-button {
  width: 16px;
  height: 16px;
  border-radius: 50%;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all var(--duration-fast) var(--easing);
  font-size: 12px;
  font-weight: bold;
}

.control-button:hover {
  transform: scale(1.1);
}

.control-button.minimize {
  background: #ffbd2e;
  color: #995700;
}

.control-button.maximize {
  background: #27ca3f;
  color: #006500;
}

.control-button.close {
  background: #ff5f56;
  color: #8b0000;
}

.control-icon {
  line-height: 1;
  opacity: 0;
  transition: opacity var(--duration-fast) var(--easing);
}

.control-button:hover .control-icon {
  opacity: 1;
}

.window-content {
  flex: 1;
  overflow: auto;
  background: var(--color-background);
}

.empty-content,
.loading-content {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: var(--color-text-secondary);
}

.loading-content {
  opacity: 0.7;
}

.resize-handle {
  position: absolute;
  background: transparent;
}

.resize-se {
  bottom: 0;
  right: 0;
  width: 16px;
  height: 16px;
  cursor: se-resize;
}

.resize-e {
  top: 0;
  right: 0;
  width: 8px;
  height: 100%;
  cursor: e-resize;
}

.resize-s {
  bottom: 0;
  left: 0;
  width: 100%;
  height: 8px;
  cursor: s-resize;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .window {
    min-width: 300px;
    min-height: 200px;
  }

  .window-header {
    padding: 6px 8px;
  }

  .control-button {
    width: 14px;
    height: 14px;
  }

  .title-text {
    font-size: 12px;
  }
}
</style>
