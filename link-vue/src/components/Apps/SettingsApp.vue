<template>
  <div class="settings-app">
    <div class="settings-sidebar">
      <div
        class="sidebar-item"
        v-for="section in sections"
        :key="section.id"
        :class="{ active: activeSection === section.id }"
        @click="activeSection = section.id"
      >
        <span class="section-icon">{{ section.icon }}</span>
        <span class="section-name">{{ section.name }}</span>
      </div>
    </div>

    <div class="settings-content">
      <!-- 外观设置 -->
      <div v-if="activeSection === 'appearance'" class="settings-section">
        <h2 class="section-title">外观</h2>

        <div class="setting-group">
          <h3 class="group-title">主题</h3>
          <div class="setting-item">
            <label class="setting-label">主题模式</label>
            <div class="theme-selector">
              <button
                class="theme-btn"
                :class="{ active: currentTheme === 'light' }"
                @click="setTheme('light')"
              >
                <span class="theme-icon">☀️</span>
                <span>浅色</span>
              </button>
              <button
                class="theme-btn"
                :class="{ active: currentTheme === 'dark' }"
                @click="setTheme('dark')"
              >
                <span class="theme-icon">🌙</span>
                <span>深色</span>
              </button>
            </div>
          </div>
        </div>

        <div class="setting-group">
          <h3 class="group-title">桌面</h3>
          <div class="setting-item">
            <label class="setting-label">壁纸类型</label>
            <select
              v-model="wallpaperType"
              @change="updateWallpaper"
              class="setting-select"
            >
              <option value="color">纯色</option>
              <option value="gradient">渐变</option>
              <option value="image">图片</option>
            </select>
          </div>

          <div v-if="wallpaperType === 'color'" class="setting-item">
            <label class="setting-label">背景颜色</label>
            <input
              v-model="wallpaperColor"
              type="color"
              @change="updateWallpaper"
              class="setting-color"
            />
          </div>

          <div v-if="wallpaperType === 'gradient'" class="setting-item">
            <label class="setting-label">渐变起始色</label>
            <input
              v-model="gradientStart"
              type="color"
              @change="updateWallpaper"
              class="setting-color"
            />
          </div>

          <div v-if="wallpaperType === 'gradient'" class="setting-item">
            <label class="setting-label">渐变结束色</label>
            <input
              v-model="gradientEnd"
              type="color"
              @change="updateWallpaper"
              class="setting-color"
            />
          </div>

          <div v-if="wallpaperType === 'image'" class="setting-item">
            <label class="setting-label">图片壁纸</label>
            <div class="image-uploader">
              <input
                type="file"
                ref="fileInput"
                accept="image/*"
                style="display: none"
                @change="handleImageUpload"
              />
              <button class="upload-btn" @click="triggerFileInput">
                <span class="upload-icon">📁</span>
                <span>选择图片</span>
              </button>
              <div v-if="selectedImage" class="image-preview">
                <img :src="selectedImage" alt="预览" class="preview-img" />
              </div>
            </div>
          </div>

          <div class="setting-item">
            <label class="setting-label">显示桌面图标</label>
            <input
              v-model="showDesktopIcons"
              type="checkbox"
              @change="updateDesktopSettings"
              class="setting-checkbox"
            />
          </div>

          <div class="setting-item">
            <label class="setting-label">显示网格</label>
            <input
              v-model="showGrid"
              type="checkbox"
              @change="updateDesktopSettings"
              class="setting-checkbox"
            />
          </div>
        </div>
      </div>

      <!-- 系统设置 -->
      <div v-if="activeSection === 'system'" class="settings-section">
        <h2 class="section-title">系统</h2>

        <div class="setting-group">
          <h3 class="group-title">动画</h3>
          <div class="setting-item">
            <label class="setting-label">启用动画效果</label>
            <input
              v-model="animationsEnabled"
              type="checkbox"
              @change="updateAnimationSettings"
              class="setting-checkbox"
            />
          </div>

          <div class="setting-item">
            <label class="setting-label">动画速度</label>
            <select
              v-model="animationSpeed"
              @change="updateAnimationSettings"
              class="setting-select"
            >
              <option value="fast">快速</option>
              <option value="normal">正常</option>
              <option value="slow">慢速</option>
            </select>
          </div>
        </div>

        <div class="setting-group">
          <h3 class="group-title">语言和地区</h3>
          <div class="setting-item">
            <label class="setting-label">语言</label>
            <select class="setting-select">
              <option value="zh-CN">简体中文</option>
              <option value="en-US">English</option>
            </select>
          </div>
        </div>
      </div>

      <!-- 关于 -->
      <div v-if="activeSection === 'about'" class="settings-section">
        <h2 class="section-title">关于</h2>

        <div class="about-content">
          <div class="app-info">
            <h3>个人桌面系统</h3>
            <p class="version">版本 1.0.0</p>
            <p class="description">基于 Vue 3 构建的现代化桌面式个人网站</p>
          </div>

          <div class="tech-stack">
            <h4>技术栈</h4>
            <ul>
              <li>Vue 3 + Composition API</li>
              <li>Pinia 状态管理</li>
              <li>Sass 样式预处理</li>
              <li>Vue Router 路由管理</li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import { useThemeStore } from "@/stores/theme";
import { useDesktopStore } from "@/stores/desktop";

const themeStore = useThemeStore();
const desktopStore = useDesktopStore();

const activeSection = ref("appearance");
const wallpaperType = ref("color");
const wallpaperColor = ref("#f0f2f5");
const gradientStart = ref("#667eea");
const gradientEnd = ref("#764ba2");
const showDesktopIcons = ref(true);
const showGrid = ref(false);
const animationsEnabled = ref(true);
const animationSpeed = ref("normal");
const selectedImage = ref("");
const fileInput = ref(null);

const sections = [
  { id: "appearance", name: "外观", icon: "🎨" },
  { id: "system", name: "系统", icon: "⚙️" },
  { id: "about", name: "关于", icon: "ℹ️" },
];

const currentTheme = computed(() => themeStore.currentTheme);

const setTheme = (theme) => {
  themeStore.setTheme(theme);
};

const updateWallpaper = () => {
  if (wallpaperType.value === "color") {
    desktopStore.changeWallpaper("color", wallpaperColor.value);
  } else if (wallpaperType.value === "gradient") {
    desktopStore.changeGradientWallpaper(
      gradientStart.value,
      gradientEnd.value
    );
  } else if (wallpaperType.value === "image" && selectedImage.value) {
    desktopStore.changeImageWallpaper(selectedImage.value);
  }
};

const triggerFileInput = () => {
  if (fileInput.value) {
    fileInput.value.click();
  }
};

const handleImageUpload = (event) => {
  const file = event.target.files[0];
  if (file && file.type.startsWith("image/")) {
    const reader = new FileReader();
    reader.onload = (e) => {
      selectedImage.value = e.target.result;
      desktopStore.changeImageWallpaper(selectedImage.value);
    };
    reader.readAsDataURL(file);
  }
};

const updateDesktopSettings = () => {
  desktopStore.settings.showDesktopIcons = showDesktopIcons.value;
  desktopStore.settings.showGrid = showGrid.value;
};

const updateAnimationSettings = () => {
  themeStore.toggleAnimations();
  // 这里可以添加更多动画设置逻辑
};

onMounted(() => {
  // 初始化设置值
  const wallpaper = desktopStore.wallpaper;
  wallpaperType.value = wallpaper.type || "color";

  if (wallpaper.type === "color") {
    wallpaperColor.value = wallpaper.value || "#f0f2f5";
  } else if (wallpaper.type === "gradient") {
    gradientStart.value = wallpaper.gradient?.start || "#667eea";
    gradientEnd.value = wallpaper.gradient?.end || "#764ba2";
  } else if (wallpaper.type === "image") {
    selectedImage.value = wallpaper.image || "";
  }

  showDesktopIcons.value = desktopStore.settings.showDesktopIcons;
  showGrid.value = desktopStore.settings.showGrid;
  animationsEnabled.value = themeStore.animations.enabled;
});
</script>

<style scoped>
.settings-app {
  display: flex;
  height: 100%;
  background: var(--color-background);
}

.settings-sidebar {
  width: 200px;
  background: var(--color-surface);
  border-right: 1px solid var(--color-border);
  padding: 20px 0;
}

.sidebar-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 20px;
  cursor: pointer;
  transition: background-color var(--duration-fast) var(--easing);
  color: var(--color-text);
}

.sidebar-item:hover {
  background: var(--color-border);
}

.sidebar-item.active {
  background: var(--color-primary);
  color: white;
}

.section-icon {
  font-size: 18px;
}

.section-name {
  font-weight: 500;
}

.settings-content {
  flex: 1;
  padding: 30px;
  overflow-y: auto;
}

.settings-section {
  max-width: 600px;
}

.section-title {
  font-size: 1.5rem;
  font-weight: 600;
  margin-bottom: 30px;
  color: var(--color-text);
}

.setting-group {
  margin-bottom: 30px;
}

.group-title {
  font-size: 1.1rem;
  font-weight: 600;
  margin-bottom: 20px;
  color: var(--color-text);
}

.setting-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
  padding: 12px 0;
  border-bottom: 1px solid var(--color-border);
}

.setting-label {
  font-weight: 500;
  color: var(--color-text);
}

.theme-selector {
  display: flex;
  gap: 12px;
}

.theme-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  border: 1px solid var(--color-border);
  border-radius: var(--border-radius);
  background: var(--color-background);
  color: var(--color-text);
  cursor: pointer;
  transition: all var(--duration-fast) var(--easing);
}

.theme-btn:hover {
  border-color: var(--color-primary);
}

.theme-btn.active {
  background: var(--color-primary);
  border-color: var(--color-primary);
  color: white;
}

.theme-icon {
  font-size: 16px;
}

.setting-select,
.setting-color {
  padding: 6px 12px;
  border: 1px solid var(--color-border);
  border-radius: calc(var(--border-radius) / 2);
  background: var(--color-background);
  color: var(--color-text);
}

.setting-color {
  width: 50px;
  height: 30px;
  padding: 2px;
  cursor: pointer;
}

.setting-checkbox {
  width: 18px;
  height: 18px;
  cursor: pointer;
}

.image-uploader {
  display: flex;
  flex-direction: column;
  gap: 12px;
  align-items: flex-start;
}

.upload-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  border: 1px solid var(--color-border);
  border-radius: calc(var(--border-radius) / 2);
  background: var(--color-background);
  color: var(--color-text);
  cursor: pointer;
  transition: all var(--duration-fast) var(--easing);
}

.upload-btn:hover {
  background: var(--color-primary);
  color: white;
  border-color: var(--color-primary);
}

.upload-icon {
  font-size: 16px;
}

.image-preview {
  border: 1px solid var(--color-border);
  border-radius: calc(var(--border-radius) / 2);
  padding: 8px;
  background: var(--color-surface);
}

.preview-img {
  max-width: 200px;
  max-height: 150px;
  object-fit: cover;
  border-radius: calc(var(--border-radius) / 2);
}

.about-content {
  display: flex;
  flex-direction: column;
  gap: 30px;
}

.app-info h3 {
  font-size: 1.3rem;
  margin-bottom: 8px;
  color: var(--color-text);
}

.version {
  font-size: var(--font-size-sm);
  color: var(--color-text-secondary);
  margin-bottom: 12px;
}

.description {
  color: var(--color-text-secondary);
  line-height: 1.6;
}

.tech-stack h4 {
  font-size: 1.1rem;
  margin-bottom: 12px;
  color: var(--color-text);
}

.tech-stack ul {
  list-style: none;
  padding: 0;
}

.tech-stack li {
  padding: 4px 0;
  color: var(--color-text-secondary);
}

.tech-stack li::before {
  content: "• ";
  color: var(--color-primary);
  font-weight: bold;
}
</style>
