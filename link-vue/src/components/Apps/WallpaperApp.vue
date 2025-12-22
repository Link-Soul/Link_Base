<template>
  <div class="wallpaper-app">
    <div class="wallpaper-header">
      <h2>壁纸设置</h2>
    </div>
    
    <div class="wallpaper-content">
      <div class="wallpaper-options">
        <h3>选择壁纸类型</h3>
        
        <div class="option-group">
          <label class="option-label">
            <input
              type="radio"
              v-model="wallpaperType"
              value="color"
              @change="updateWallpaper"
            />
            <span>纯色背景</span>
          </label>
          
          <div v-if="wallpaperType === 'color'" class="color-picker">
            <input
              v-model="selectedColor"
              type="color"
              @change="updateWallpaper"
            />
            <span>{{ selectedColor }}</span>
          </div>
        </div>
        
        <div class="option-group">
          <label class="option-label">
            <input
              type="radio"
              v-model="wallpaperType"
              value="gradient"
              @change="updateWallpaper"
            />
            <span>渐变背景</span>
          </label>
          
          <div v-if="wallpaperType === 'gradient'" class="gradient-picker">
            <div class="gradient-color">
              <label>起始颜色</label>
              <input
                v-model="gradientStart"
                type="color"
                @change="updateWallpaper"
              />
            </div>
            <div class="gradient-color">
              <label>结束颜色</label>
              <input
                v-model="gradientEnd"
                type="color"
                @change="updateWallpaper"
              />
            </div>
          </div>
        </div>
      </div>
      
      <div class="preset-wallpapers">
        <h3>预设壁纸</h3>
        <div class="preset-grid">
          <div
            v-for="preset in presets"
            :key="preset.name"
            class="preset-item"
            :style="preset.style"
            @click="applyPreset(preset)"
            :title="preset.name"
          ></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useDesktopStore } from '@/stores/desktop'

const desktopStore = useDesktopStore()

const wallpaperType = ref('color')
const selectedColor = ref('#f0f2f5')
const gradientStart = ref('#667eea')
const gradientEnd = ref('#764ba2')

const presets = [
  {
    name: '默认灰色',
    type: 'color',
    color: '#f0f2f5',
    style: { backgroundColor: '#f0f2f5' }
  },
  {
    name: '深蓝渐变',
    type: 'gradient',
    start: '#667eea',
    end: '#764ba2',
    style: { background: 'linear-gradient(135deg, #667eea, #764ba2)' }
  },
  {
    name: '日落渐变',
    type: 'gradient',
    start: '#ff6b6b',
    end: '#feca57',
    style: { background: 'linear-gradient(135deg, #ff6b6b, #feca57)' }
  },
  {
    name: '海洋渐变',
    type: 'gradient',
    start: '#4facfe',
    end: '#00f2fe',
    style: { background: 'linear-gradient(135deg, #4facfe, #00f2fe)' }
  },
  {
    name: '森林渐变',
    type: 'gradient',
    start: '#43e97b',
    end: '#38f9d7',
    style: { background: 'linear-gradient(135deg, #43e97b, #38f9d7)' }
  },
  {
    name: '紫色梦幻',
    type: 'gradient',
    start: '#fa709a',
    end: '#fee140',
    style: { background: 'linear-gradient(135deg, #fa709a, #fee140)' }
  }
]

const updateWallpaper = () => {
  if (wallpaperType.value === 'color') {
    desktopStore.changeWallpaper('color', selectedColor.value)
  } else if (wallpaperType.value === 'gradient') {
    desktopStore.wallpaper.type = 'gradient'
    desktopStore.wallpaper.gradient = {
      start: gradientStart.value,
      end: gradientEnd.value
    }
  }
}

const applyPreset = (preset) => {
  wallpaperType.value = preset.type
  
  if (preset.type === 'color') {
    selectedColor.value = preset.color
    desktopStore.changeWallpaper('color', preset.color)
  } else if (preset.type === 'gradient') {
    gradientStart.value = preset.start
    gradientEnd.value = preset.end
    desktopStore.wallpaper.type = 'gradient'
    desktopStore.wallpaper.gradient = {
      start: preset.start,
      end: preset.end
    }
  }
}

onMounted(() => {
  // 初始化当前壁纸设置
  const wallpaper = desktopStore.wallpaper
  wallpaperType.value = wallpaper.type || 'color'
  
  if (wallpaper.type === 'color') {
    selectedColor.value = wallpaper.value || '#f0f2f5'
  } else if (wallpaper.type === 'gradient') {
    gradientStart.value = wallpaper.gradient?.start || '#667eea'
    gradientEnd.value = wallpaper.gradient?.end || '#764ba2'
  }
})
</script>

<style scoped>
.wallpaper-app {
  display: flex;
  flex-direction: column;
  height: 100%;
  background: var(--color-background);
  padding: 20px;
}

.wallpaper-header {
  margin-bottom: 30px;
}

.wallpaper-header h2 {
  font-size: 1.5rem;
  font-weight: 600;
  color: var(--color-text);
}

.wallpaper-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 30px;
}

.wallpaper-options h3,
.preset-wallpapers h3 {
  font-size: 1.1rem;
  font-weight: 600;
  margin-bottom: 16px;
  color: var(--color-text);
}

.option-group {
  margin-bottom: 20px;
}

.option-label {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 500;
  color: var(--color-text);
  cursor: pointer;
  margin-bottom: 12px;
}

.color-picker,
.gradient-picker {
  margin-left: 24px;
  display: flex;
  align-items: center;
  gap: 12px;
}

.gradient-picker {
  flex-direction: column;
  align-items: flex-start;
  gap: 12px;
}

.gradient-color {
  display: flex;
  align-items: center;
  gap: 8px;
}

.gradient-color label {
  font-size: var(--font-size-sm);
  color: var(--color-text-secondary);
  min-width: 60px;
}

input[type="color"] {
  width: 50px;
  height: 30px;
  border: 1px solid var(--color-border);
  border-radius: calc(var(--border-radius) / 2);
  cursor: pointer;
}

.preset-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(100px, 1fr));
  gap: 12px;
}

.preset-item {
  width: 100px;
  height: 75px;
  border-radius: var(--border-radius);
  cursor: pointer;
  transition: transform var(--duration-fast) var(--easing);
  border: 2px solid transparent;
}

.preset-item:hover {
  transform: scale(1.05);
  border-color: var(--color-primary);
}
</style>