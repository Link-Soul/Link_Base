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
          <h3 class="group-title">壁纸</h3>
          <div class="setting-item">
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
import { uploadApi, settingsApi } from "@/services/api";

const themeStore = useThemeStore();
const desktopStore = useDesktopStore();

const activeSection = ref("appearance");
const showDesktopIcons = ref(true);
const showGrid = ref(false);
const animationsEnabled = ref(true);
const animationSpeed = ref("normal");
const selectedImage = ref("");
const fileInput = ref(null);

const sections = [
  { id: "appearance", name: "外观", icon: "🎨" },
  // { id: "system", name: "系统", icon: "⚙️" },
  { id: "about", name: "关于", icon: "ℹ️" },
];

const currentTheme = computed(() => themeStore.currentTheme);

const setTheme = async (theme) => {
  // 更新本地状态
  themeStore.setTheme(theme);

  // 保存到系统配置
  try {
    await settingsApi.updateSetting("themeMode", theme);
  } catch (error) {
    console.error("Failed to save theme setting:", error);
  }
};

const updateWallpaper = async () => {
  debugger

  // 更新本地状态
  if (selectedImage.value) {
    desktopStore.changeImageWallpaper(selectedImage.value);
  }

  // 保存到系统配置
  try {
    await settingsApi.updateSetting(
      "wallpaper", selectedImage.value
    );
  } catch (error) {
    console.error("Failed to save wallpaper config:", error);
  }
};

const triggerFileInput = () => {
  if (fileInput.value) {
    fileInput.value.click();
  }
};

/**
 * 处理图片上传
 * @param {Event} event - 文件选择事件
 */
const handleImageUpload = async (event) => {
  const file = event.target.files[0];
  if (!file || !file.type.startsWith("image/")) return;

  try {
    // 上传文件到系统文件接口
    const response = await uploadApi.uploadSysFile(file);
    debugger;

    if (response && (response.fileUrl || response.url)) {
      selectedImage.value = response.fileUrl || response.url;
      await updateWallpaper();
    } else {
      // 如果上传失败，使用本地文件预览
      const reader = new FileReader();
      reader.onload = (e) => {
        selectedImage.value = e.target.result;
        updateWallpaper();
      };
      reader.readAsDataURL(file);
    }
  } finally {
    // 清空文件输入
    if (fileInput.value) {
      fileInput.value.value = "";
    }
  }
};

const updateDesktopSettings = async () => {
  // 更新本地状态
  desktopStore.settings.showDesktopIcons = showDesktopIcons.value;
  desktopStore.settings.showGrid = showGrid.value;

  // 保存到系统配置
  try {
    await settingsApi.updateSetting(
      "showDesktopIcons",
      JSON.stringify(showDesktopIcons.value)
    );
    await settingsApi.updateSetting("showGrid", JSON.stringify(showGrid.value));
  } catch (error) {
    console.error("Failed to save desktop settings:", error);
  }
};

const updateAnimationSettings = async () => {
  // 更新本地状态
  themeStore.toggleAnimations();

  // 保存到系统配置
  try {
    await settingsApi.updateSetting("themeMode", themeStore.currentTheme);
  } catch (error) {
    console.error("Failed to save animation settings:", error);
  }
};

const loadWallpaperConfig = async () => {
  try {
    const config = await settingsApi.getSettingByKey("wallpaper");
    if (config && config.value) {
      const wallpaperConfig = JSON.parse(config.value);
      
      if (wallpaperConfig.image) {
        selectedImage.value = wallpaperConfig.image;
      }
      
      // 更新本地状态
      await updateWallpaper();
    }
  } catch (error) {
    console.error("Failed to load wallpaper config:", error);
  }
};

// 从系统配置批量加载所有设置
const loadAllSettings = async () => {
  try {
    // 使用批量获取接口获取所有设置
    const settingsMap = await settingsApi.getSettingsMap();
    if (settingsMap) {
      // 加载壁纸配置
      if (settingsMap.wallpaper) {
        try {
          const wallpaperConfig = JSON.parse(settingsMap.wallpaper);
          if (wallpaperConfig.image) {
            selectedImage.value = wallpaperConfig.image;
            // 更新本地状态
            await updateWallpaper();
          }
        } catch (parseError) {
          console.error("Failed to parse wallpaper config:", parseError);
        }
      }
      
      // 加载显示桌面图标设置
      if (settingsMap.showDesktopIcons) {
        try {
          showDesktopIcons.value = JSON.parse(settingsMap.showDesktopIcons);
          desktopStore.settings.showDesktopIcons = showDesktopIcons.value;
        } catch (parseError) {
          console.error("Failed to parse showDesktopIcons config:", parseError);
        }
      }
      
      // 加载显示网格设置
      if (settingsMap.showGrid) {
        try {
          showGrid.value = JSON.parse(settingsMap.showGrid);
          desktopStore.settings.showGrid = showGrid.value;
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
  } catch (error) {
    console.error("Failed to load settings:", error);
  }
};

onMounted(async () => {
  // 首先尝试从系统配置批量加载所有设置
  await loadAllSettings();
  
  // 如果没有系统配置，使用本地存储的设置
  if (!selectedImage.value) {
    const wallpaper = desktopStore.wallpaper;
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
