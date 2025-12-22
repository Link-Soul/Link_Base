<template>
  <div class="app-launcher-overlay" @click="handleOverlayClick">
    <div class="app-launcher" :class="{ 'expanded': isExpanded }" @click.stop>
      <!-- 搜索栏 -->
      <div class="search-section">
        <div class="search-container">
          <span class="search-icon">🔍</span>
          <input
            ref="searchInput"
            v-model="searchQuery"
            type="text"
            class="search-input"
            placeholder="搜索应用或文件..."
            @input="handleSearch"
            @keydown="handleKeydown"
          />
        </div>
      </div>

      <!-- 应用分类标签 -->
      <div class="category-tabs">
        <button
          v-for="category in categories"
          :key="category.id"
          class="category-tab"
          :class="{ 'active': selectedCategory === category.id }"
          @click="selectCategory(category.id)"
        >
          <span class="category-icon">{{ category.icon }}</span>
          <span class="category-name">{{ category.name }}</span>
        </button>
      </div>

      <!-- 应用网格 -->
      <div class="apps-grid-container">
        <div class="apps-grid">
          <div
            v-for="app in filteredApps"
            :key="app.id"
            class="app-item"
            @click="launchApp(app)"
            @mouseenter="hoveredApp = app.id"
            @mouseleave="hoveredApp = null"
          >
            <div class="app-icon-container">
              <span class="app-icon">{{ app.icon }}</span>
              <div v-if="isAppRunning(app.id)" class="running-dot"></div>
            </div>
            <span class="app-name">{{ app.name }}</span>
          </div>
        </div>
      </div>

      <!-- 最近使用 -->
      <div v-if="!searchQuery && selectedCategory === 'all'" class="recent-section">
        <h3 class="section-title">最近使用</h3>
        <div class="recent-apps">
          <div
            v-for="app in recentApps"
            :key="app.id"
            class="recent-app-item"
            @click="launchApp(app)"
          >
            <span class="recent-app-icon">{{ app.icon }}</span>
            <span class="recent-app-name">{{ app.name }}</span>
          </div>
        </div>
      </div>

      <!-- 底部操作栏 -->
      <div class="action-bar">
        <button class="action-button" @click="openAppStore">
          <span class="button-icon">🏪</span>
          <span>App Store</span>
        </button>
        <button class="action-button" @click="openSettings">
          <span class="button-icon">⚙️</span>
          <span>设置</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, nextTick } from 'vue'
import { useAppsStore } from '@/stores/apps'
import { useDesktopStore } from '@/stores/desktop'

const emit = defineEmits(['close'])

const appsStore = useAppsStore()
const desktopStore = useDesktopStore()

const searchQuery = ref('')
const selectedCategory = ref('all')
const hoveredApp = ref(null)
const isExpanded = ref(false)
const searchInput = ref(null)

const categories = computed(() => appsStore.categories)
const installedApps = computed(() => appsStore.installedApps)
const recentApps = computed(() => appsStore.mostUsedApps)

const filteredApps = computed(() => {
  let apps = installedApps.value
  
  // 按分类筛选
  if (selectedCategory.value !== 'all') {
    apps = apps.filter(app => app.category === selectedCategory.value)
  }
  
  // 按搜索查询筛选
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    apps = apps.filter(app => 
      app.name.toLowerCase().includes(query) ||
      app.id.toLowerCase().includes(query)
    )
  }
  
  return apps
})

const isAppRunning = (appId) => {
  return desktopStore.windows.some(w => w.appId === appId && !w.isMinimized)
}

const handleSearch = () => {
  // 搜索时自动展开
  if (searchQuery.value && !isExpanded.value) {
    isExpanded.value = true
  }
}

const handleKeydown = (event) => {
  // Esc 关闭启动器
  if (event.key === 'Escape') {
    emit('close')
  }
  
  // Enter 启动第一个匹配的应用
  if (event.key === 'Enter' && filteredApps.value.length > 0) {
    launchApp(filteredApps.value[0])
  }
  
  // 上下箭头导航
  if (event.key === 'ArrowDown' || event.key === 'ArrowUp') {
    event.preventDefault()
    // 实现键盘导航逻辑
  }
}

const selectCategory = (categoryId) => {
  selectedCategory.value = categoryId
  searchQuery.value = ''
}

const launchApp = (app) => {
  appsStore.launchApp(app.id)
  emit('close')
}

const openAppStore = () => {
  // 打开应用商店
  console.log('Open app store')
}

const openSettings = () => {
  appsStore.launchApp('settings')
  emit('close')
}

const handleOverlayClick = () => {
  emit('close')
}

onMounted(() => {
  // 自动聚焦搜索框
  nextTick(() => {
    searchInput.value?.focus()
  })
})
</script>

<style scoped>
.app-launcher-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.5);
  backdrop-filter: blur(10px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 2000;
  animation: fadeIn var(--duration-normal) var(--easing);
}

.app-launcher {
  width: 600px;
  max-width: 90vw;
  max-height: 80vh;
  background: var(--color-window-background);
  backdrop-filter: var(--backdrop-filter);
  border-radius: calc(var(--border-radius) * 1.5);
  box-shadow: 0 20px 60px var(--color-shadow);
  border: 1px solid var(--color-border);
  overflow: hidden;
  display: flex;
  flex-direction: column;
  transition: all var(--duration-normal) var(--easing);
}

.app-launcher.expanded {
  max-height: 90vh;
}

.search-section {
  padding: 24px 24px 16px;
}

.search-container {
  position: relative;
  display: flex;
  align-items: center;
}

.search-icon {
  position: absolute;
  left: 16px;
  font-size: 18px;
  color: var(--color-text-secondary);
}

.search-input {
  width: 100%;
  padding: 12px 16px 12px 48px;
  border: none;
  border-radius: var(--border-radius);
  background: var(--color-surface);
  color: var(--color-text);
  font-size: var(--font-size-base);
  outline: none;
  transition: all var(--duration-fast) var(--easing);
}

.search-input:focus {
  background: var(--color-background);
  box-shadow: 0 0 0 2px var(--color-primary);
}

.category-tabs {
  display: flex;
  gap: 8px;
  padding: 0 24px 16px;
  overflow-x: auto;
  scrollbar-width: none;
}

.category-tabs::-webkit-scrollbar {
  display: none;
}

.category-tab {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 12px;
  border: none;
  border-radius: calc(var(--border-radius) / 2);
  background: transparent;
  color: var(--color-text-secondary);
  cursor: pointer;
  transition: all var(--duration-fast) var(--easing);
  white-space: nowrap;
}

.category-tab:hover {
  background: var(--color-surface);
  color: var(--color-text);
}

.category-tab.active {
  background: var(--color-primary);
  color: white;
}

.category-icon {
  font-size: 16px;
}

.category-name {
  font-size: var(--font-size-sm);
  font-weight: 500;
}

.apps-grid-container {
  flex: 1;
  overflow-y: auto;
  padding: 0 24px;
}

.apps-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(80px, 1fr));
  gap: 16px;
  padding-bottom: 16px;
}

.app-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 12px 8px;
  border-radius: var(--border-radius);
  cursor: pointer;
  transition: all var(--duration-fast) var(--easing);
}

.app-item:hover {
  background: var(--color-surface);
  transform: translateY(-2px);
}

.app-icon-container {
  position: relative;
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 8px;
  border-radius: calc(var(--border-radius) / 2);
  background: var(--color-surface);
}

.app-item:hover .app-icon-container {
  background: var(--color-primary);
}

.app-icon {
  font-size: 24px;
  line-height: 1;
}

.running-dot {
  position: absolute;
  bottom: -2px;
  right: -2px;
  width: 8px;
  height: 8px;
  background: #00ff00;
  border-radius: 50%;
  box-shadow: 0 0 4px #00ff00;
}

.app-name {
  font-size: var(--font-size-xs);
  color: var(--color-text);
  text-align: center;
  line-height: 1.2;
  max-width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.recent-section {
  padding: 16px 24px;
  border-top: 1px solid var(--color-border);
}

.section-title {
  font-size: var(--font-size-sm);
  font-weight: 600;
  color: var(--color-text-secondary);
  margin-bottom: 12px;
}

.recent-apps {
  display: flex;
  gap: 12px;
  overflow-x: auto;
  scrollbar-width: none;
}

.recent-apps::-webkit-scrollbar {
  display: none;
}

.recent-app-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  border-radius: calc(var(--border-radius) / 2);
  background: var(--color-surface);
  cursor: pointer;
  transition: all var(--duration-fast) var(--easing);
  white-space: nowrap;
}

.recent-app-item:hover {
  background: var(--color-primary);
  color: white;
}

.recent-app-icon {
  font-size: 16px;
}

.recent-app-name {
  font-size: var(--font-size-sm);
  font-weight: 500;
}

.action-bar {
  display: flex;
  gap: 12px;
  padding: 16px 24px;
  border-top: 1px solid var(--color-border);
}

.action-button {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  border: none;
  border-radius: calc(var(--border-radius) / 2);
  background: var(--color-surface);
  color: var(--color-text);
  cursor: pointer;
  transition: all var(--duration-fast) var(--easing);
  font-size: var(--font-size-sm);
  font-weight: 500;
}

.action-button:hover {
  background: var(--color-primary);
  color: white;
}

.button-icon {
  font-size: 16px;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: scale(0.9);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .app-launcher {
    width: 95vw;
    max-height: 95vh;
  }
  
  .search-section {
    padding: 16px 16px 12px;
  }
  
  .category-tabs {
    padding: 0 16px 12px;
  }
  
  .apps-grid-container {
    padding: 0 16px;
  }
  
  .apps-grid {
    grid-template-columns: repeat(auto-fill, minmax(60px, 1fr));
    gap: 12px;
  }
  
  .app-icon-container {
    width: 36px;
    height: 36px;
  }
  
  .app-icon {
    font-size: 20px;
  }
  
  .action-bar {
    padding: 12px 16px;
  }
}
</style>