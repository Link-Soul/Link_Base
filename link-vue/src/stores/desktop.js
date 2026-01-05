import { defineStore } from 'pinia';
import { useAppsStore } from './apps';

export const useDesktopStore = defineStore('desktop', {
  state: () => ({
    // 桌面壁纸设置
    wallpaper: {
      type: 'image', // 'color' | 'image' | 'gradient'
      value: '#f0f2f5',
      image: '/img/paper.jpg', // 默认图片壁纸
      gradient: {
        start: '#667eea',
        end: '#764ba2'
      }
    },
    // 窗口管理
    windows: [],
    // 活动窗口
    activeWindowId: null,
    // Dock栏应用
    dockApps: [
      { id: 'finder', name: '访达', icon: '🗂️', active: true },
      // { id: 'settings', name: '系统偏好', icon: '⚙️', active: false }
    ],
    // 桌面设置
    settings: {
      showGrid: false,
      gridSize: 10,
      autoArrange: true,
      showDesktopIcons: true
    }
  }),

  getters: {
    // 获取活动窗口
    activeWindow: (state) => {
      return state.windows.find(window => window.id === state.activeWindowId);
    },
    
    // 获取窗口数量
    windowCount: (state) => state.windows.length,
    
    // 获取最小化的窗口
    minimizedWindows: (state) => {
      return state.windows.filter(window => window.isMinimized);
    }
  },

  actions: {
    // 添加窗口
    addWindow(windowData) {
      const newWindow = {
        id: Date.now().toString(),
        title: '新窗口',
        width: 800,
        height: 600,
        x: 100,
        y: 100,
        isMinimized: false,
        isMaximized: false,
        zIndex: 1000,
        component: null,
        ...windowData
      };
      
      this.windows.push(newWindow);
      this.setActiveWindow(newWindow.id);
      return newWindow;
    },

    // 关闭窗口
    closeWindow(windowId) {
      const index = this.windows.findIndex(w => w.id === windowId);
      if (index !== -1) {
        this.windows.splice(index, 1);
        
        // 如果关闭的是活动窗口，设置下一个窗口为活动窗口
        if (this.activeWindowId === windowId && this.windows.length > 0) {
          const remainingWindows = [...this.windows].sort((a, b) => b.zIndex - a.zIndex);
          this.activeWindowId = remainingWindows[0].id;
        }
      }
    },

    // 设置活动窗口
    setActiveWindow(windowId) {
      this.activeWindowId = windowId;
      
      // 更新窗口层级
      const maxZIndex = Math.max(...this.windows.map(w => w.zIndex), 0);
      const window = this.windows.find(w => w.id === windowId);
      if (window) {
        window.zIndex = maxZIndex + 1;
      }
    },

    // 最小化窗口
    minimizeWindow(windowId) {
      const window = this.windows.find(w => w.id === windowId);
      if (window) {
        window.isMinimized = true;
      }
    },

    // 最大化窗口
    maximizeWindow(windowId) {
      const window = this.windows.find(w => w.id === windowId);
      if (window) {
        window.isMaximized = !window.isMaximized;
      }
    },

    // 移动窗口
    moveWindow(windowId, x, y) {
      const window = this.windows.find(w => w.id === windowId);
      if (window) {
        window.x = x;
        window.y = y;
      }
    },

    // 调整窗口大小
    resizeWindow(windowId, width, height) {
      const window = this.windows.find(w => w.id === windowId);
      if (window) {
        window.width = width;
        window.height = height;
      }
    },

    // 更改壁纸
    changeWallpaper(type, value) {
      this.wallpaper.type = type;
      this.wallpaper.value = value;
    },

    // 更改图片壁纸
    changeImageWallpaper(imagePath) {
      this.wallpaper.type = 'image';
      this.wallpaper.image = imagePath;
    },

    // 更改渐变壁纸
    changeGradientWallpaper(start, end) {
      this.wallpaper.type = 'gradient';
      this.wallpaper.gradient = {
        start,
        end
      };
    },

    // 切换Dock应用状态
    toggleDockApp(appId) {
      const app = this.dockApps.find(a => a.id === appId);
      if (app) {
        app.active = !app.active;
      }
    }
  }
});