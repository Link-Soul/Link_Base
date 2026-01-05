import { defineStore } from 'pinia';
import { useDesktopStore } from './desktop';

export const useAppsStore = defineStore('apps', {
  state: () => ({
    // 已安装的应用
    installedApps: [
      {
        id: 'files',
        name: '文件',
        icon: '📁',
        category: 'productivity',
        component: 'FilesApp',
        windowConfig: {
          width: 800,
          height: 600,
          resizable: true,
          minWidth: 600,
          minHeight: 400
        }
      },
      {
        id: 'calculator',
        name: '计算器',
        icon: '🧮',
        category: 'utility',
        component: 'CalculatorApp',
        windowConfig: {
          width: 320,
          height: 480,
          resizable: false,
          minWidth: 320,
          minHeight: 480
        }
      },
      {
        id: 'wallpaper',
        name: '壁纸设置',
        icon: '🖼️',
        category: 'utility',
        component: 'WallpaperApp',
        windowConfig: {
          width: 600,
          height: 500,
          resizable: false,
          minWidth: 600,
          minHeight: 500
        }
      },
      {
        id: 'notes',
        name: '备忘录',
        icon: '📝',
        category: 'productivity',
        component: 'NotesApp',
        windowConfig: {
          width: 600,
          height: 400,
          resizable: true,
          minWidth: 400,
          minHeight: 300
        }
      },
      
      {
        id: 'settings',
        name: '系统设置',
        icon: '⚙️',
        category: 'system',
        component: 'SettingsApp',
        windowConfig: {
          width: 700,
          height: 500,
          resizable: false,
          minWidth: 700,
          minHeight: 500
        }
      },
      {
        id: 'terminal',
        name: '终端',
        icon: '💻',
        category: 'development',
        component: 'TerminalApp',
        windowConfig: {
          width: 800,
          height: 500,
          resizable: true,
          minWidth: 400,
          minHeight: 300
        }
      },
      {
        id: 'browser',
        name: '浏览器',
        icon: '🌐',
        category: 'internet',
        component: 'BrowserApp',
        windowConfig: {
          width: 1024,
          height: 768,
          resizable: true,
          minWidth: 600,
          minHeight: 400
        }
      },
      {
        id: 'music',
        name: '音乐',
        icon: '🎵',
        category: 'entertainment',
        component: 'MusicApp',
        windowConfig: {
          width: 350,
          height: 500,
          resizable: false,
          minWidth: 350,
          minHeight: 500
        }
      },
      {
        id: 'calendar',
        name: '日历',
        icon: '📅',
        category: 'productivity',
        component: 'CalendarApp',
        windowConfig: {
          width: 800,
          height: 600,
          resizable: true,
          minWidth: 600,
          minHeight: 400
        }
      },
      {
        id: 'gacha',
        name: '抽卡数据查询',
        icon: '🎰',
        category: 'entertainment',
        component: 'GachaApp',
        windowConfig: {
          width: 1200,
          height: 800,
          resizable: true,
          minWidth: 800,
          minHeight: 600
        }
      }
    ],
    
    // 应用分类
    categories: [
      { id: 'all', name: '全部', icon: '📱' },
      { id: 'system', name: '系统', icon: '⚙️' },
      { id: 'productivity', name: '效率', icon: '💼' },
      { id: 'utility', name: '工具', icon: '🔧' },
      { id: 'development', name: '开发', icon: '💻' },
      { id: 'internet', name: '网络', icon: '🌐' },
      { id: 'entertainment', name: '娱乐', icon: '🎮' }
    ],
    
    // 当前选中的分类
    selectedCategory: 'all',
    
    // 应用使用统计
    usageStats: {},
    
    // 收藏的应用
    favoriteApps: ['calculator', 'notes', 'files', 'gacha']
  }),

  getters: {
    // 获取当前分类的应用
    appsByCategory: (state) => {
      if (state.selectedCategory === 'all') {
        return state.installedApps;
      }
      return state.installedApps.filter(app => app.category === state.selectedCategory);
    },
    
    // 获取收藏应用
    favoriteAppsList: (state) => {
      return state.installedApps.filter(app => state.favoriteApps.includes(app.id));
    },
    
    // 获取应用详情
    getAppById: (state) => {
      return (appId) => state.installedApps.find(app => app.id === appId);
    },
    
    // 获取最常用的应用
    mostUsedApps: (state) => {
      return [...state.installedApps]
        .sort((a, b) => (state.usageStats[b.id] || 0) - (state.usageStats[a.id] || 0))
        .slice(0, 6);
    }
  },

  actions: {
// 启动应用
    launchApp(appId) {
      const app = this.getAppById(appId)
      if (app) {
        // 更新使用统计
        this.usageStats[appId] = (this.usageStats[appId] || 0) + 1
        
        // 获取实际组件
        const component = this.getAppComponent(app.component)
        
        // 触发桌面store的窗口创建
        const desktopStore = useDesktopStore()
        desktopStore.addWindow({
          appId: app.id,
          title: app.name,
          component: component,
          icon: app.icon,
          ...app.windowConfig
        })
        
        return app
      }
    },
    
    // 获取应用组件
    getAppComponent(componentName) {
      // 直接返回组件名称，由Window组件使用动态导入
      return componentName
    },
    
    // 安装应用
    installApp(appData) {
      if (!this.getAppById(appData.id)) {
        this.installedApps.push({
          id: appData.id,
          name: appData.name,
          icon: appData.icon || '📱',
          category: appData.category || 'utility',
          component: appData.component,
          windowConfig: {
            width: 600,
            height: 400,
            resizable: true,
            minWidth: 400,
            minHeight: 300,
            ...appData.windowConfig
          }
        });
      }
    },
    
    // 卸载应用
    uninstallApp(appId) {
      const index = this.installedApps.findIndex(app => app.id === appId);
      if (index !== -1) {
        this.installedApps.splice(index, 1);
        
        // 从收藏中移除
        const favIndex = this.favoriteApps.indexOf(appId);
        if (favIndex !== -1) {
          this.favoriteApps.splice(favIndex, 1);
        }
        
        // 清除使用统计
        delete this.usageStats[appId];
      }
    },
    
    // 切换收藏
    toggleFavorite(appId) {
      const index = this.favoriteApps.indexOf(appId);
      if (index === -1) {
        this.favoriteApps.push(appId);
      } else {
        this.favoriteApps.splice(index, 1);
      }
    },
    
    // 设置分类
    setCategory(categoryId) {
      if (this.categories.find(cat => cat.id === categoryId)) {
        this.selectedCategory = categoryId;
      }
    },
    
    // 更新应用信息
    updateApp(appId, updates) {
      const app = this.getAppById(appId);
      if (app) {
        Object.assign(app, updates);
      }
    },
    
    // 搜索应用
    searchApps(query) {
      const lowerQuery = query.toLowerCase();
      return this.installedApps.filter(app => 
        app.name.toLowerCase().includes(lowerQuery) ||
        app.id.toLowerCase().includes(lowerQuery) ||
        app.category.toLowerCase().includes(lowerQuery)
      );
    }
  }
});