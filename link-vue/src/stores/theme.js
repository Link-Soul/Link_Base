import { defineStore } from 'pinia';

export const useThemeStore = defineStore('theme', {
  state: () => ({
    // 当前主题
    currentTheme: 'light', // 'light' | 'dark' | 'auto'
    
    // 主题配置
    themes: {
      light: {
        name: '浅色',
        colors: {
          primary: '#007AFF',
          secondary: '#5856D6',
          background: '#FFFFFF',
          surface: '#F2F2F7',
          text: '#000000',
          textSecondary: '#8E8E93',
          border: '#C6C6C8',
          shadow: 'rgba(0, 0, 0, 0.1)',
          windowBackground: 'rgba(255, 255, 255, 0.8)',
          dockBackground: 'rgba(255, 255, 255, 0.7)'
        },
        borderRadius: '12px',
        backdropFilter: 'blur(20px)'
      },
      dark: {
        name: '深色',
        colors: {
          primary: '#0A84FF',
          secondary: '#5E5CE6',
          background: '#000000',
          surface: '#1C1C1E',
          text: '#FFFFFF',
          textSecondary: '#98989F',
          border: '#38383A',
          shadow: 'rgba(0, 0, 0, 0.3)',
          windowBackground: 'rgba(28, 28, 30, 0.8)',
          dockBackground: 'rgba(28, 28, 30, 0.7)'
        },
        borderRadius: '12px',
        backdropFilter: 'blur(20px)'
      }
    },
    
    // 动画设置
    animations: {
      enabled: true,
      duration: {
        fast: '0.2s',
        normal: '0.3s',
        slow: '0.5s'
      },
      easing: 'cubic-bezier(0.4, 0.0, 0.2, 1)'
    },
    
    // 字体设置
    typography: {
      fontFamily: '-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif',
      fontSize: {
        xs: '12px',
        sm: '14px',
        base: '16px',
        lg: '18px',
        xl: '20px',
        '2xl': '24px',
        '3xl': '30px'
      }
    }
  }),

  getters: {
    // 获取当前主题配置
    currentThemeConfig: (state) => {
      return state.themes[state.currentTheme];
    },
    
    // 获取当前颜色
    colors: (state) => {
      return state.themes[state.currentTheme].colors;
    },
    
    // 是否为深色主题
    isDark: (state) => {
      return state.currentTheme === 'dark';
    },
    
    // 获取CSS变量
    cssVariables: (state) => {
      const theme = state.themes[state.currentTheme];
      const vars = {};
      
      // 颜色变量
      Object.entries(theme.colors).forEach(([key, value]) => {
        vars[`--color-${key}`] = value;
      });
      
      // 其他变量
      vars['--border-radius'] = theme.borderRadius;
      vars['--backdrop-filter'] = theme.backdropFilter;
      
      // 动画变量
      Object.entries(state.animations.duration).forEach(([key, value]) => {
        vars[`--duration-${key}`] = value;
      });
      vars['--easing'] = state.animations.easing;
      
      // 字体变量
      vars['--font-family'] = state.typography.fontFamily;
      Object.entries(state.typography.fontSize).forEach(([key, value]) => {
        vars[`--font-size-${key}`] = value;
      });
      
      return vars;
    }
  },

  actions: {
    // 切换主题
    toggleTheme() {
      this.currentTheme = this.currentTheme === 'light' ? 'dark' : 'light';
      this.applyTheme();
    },
    
    // 设置主题
    setTheme(theme) {
      if (this.themes[theme]) {
        this.currentTheme = theme;
        this.applyTheme();
      }
    },
    
    // 应用主题到DOM
    applyTheme() {
      const root = document.documentElement;
      const vars = this.cssVariables;
      
      Object.entries(vars).forEach(([property, value]) => {
        root.style.setProperty(property, value);
      });
      
      // 设置data属性
      root.setAttribute('data-theme', this.currentTheme);
    },
    
    // 更新主题配置
    updateTheme(themeName, updates) {
      if (this.themes[themeName]) {
        this.themes[themeName] = {
          ...this.themes[themeName],
          ...updates
        };
        
        // 如果是当前主题，立即应用
        if (this.currentTheme === themeName) {
          this.applyTheme();
        }
      }
    },
    
    // 切换动画
    toggleAnimations() {
      this.animations.enabled = !this.animations.enabled;
    },
    
    // 设置动画时长
    setAnimationDuration(type, duration) {
      if (this.animations.duration[type]) {
        this.animations.duration[type] = duration;
      }
    }
  }
});