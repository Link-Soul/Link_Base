<template>
  <div class="files-app">
    <div class="files-header">
      <h2>文件管理</h2>
    </div>
    
    <div class="files-content">
      <div class="file-navigation">
        <div class="nav-item active">
          <span class="nav-icon">🏠</span>
          <span class="nav-text">我的文档</span>
        </div>
        <div class="nav-item">
          <span class="nav-icon">📁</span>
          <span class="nav-text">下载</span>
        </div>
        <div class="nav-item">
          <span class="nav-icon">🖼️</span>
          <span class="nav-text">图片</span>
        </div>
        <div class="nav-item">
          <span class="nav-icon">🎵</span>
          <span class="nav-text">音乐</span>
        </div>
        <div class="nav-item">
          <span class="nav-icon">🎬</span>
          <span class="nav-text">视频</span>
        </div>
      </div>
      
      <div class="file-list">
        <div class="file-list-header">
          <h3>最近文件</h3>
        </div>
        
        <div class="file-grid">
          <div class="file-item folder">
            <div class="file-icon">📁</div>
            <div class="file-info">
              <div class="file-name">文档</div>
              <div class="file-date">2025-12-23</div>
            </div>
          </div>
          
          <div class="file-item image">
            <div class="file-icon">🖼️</div>
            <div class="file-info">
              <div class="file-name">壁纸.jpg</div>
              <div class="file-date">2025-12-23</div>
            </div>
          </div>
          
          <div class="file-item text">
            <div class="file-icon">📄</div>
            <div class="file-info">
              <div class="file-name">笔记.txt</div>
              <div class="file-date">2025-12-23</div>
            </div>
          </div>
          
          <div class="file-item folder">
            <div class="file-icon">📁</div>
            <div class="file-info">
              <div class="file-name">下载</div>
              <div class="file-date">2025-12-23</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
// 文件应用的简单实现
</script>

<style scoped>
.files-app {
  display: flex;
  flex-direction: column;
  height: 100%;
  background: var(--color-background);
  padding: 20px;
}

.files-header {
  margin-bottom: 30px;
}

.files-header h2 {
  font-size: 1.5rem;
  font-weight: 600;
  color: var(--color-text);
}

.files-content {
  flex: 1;
  display: flex;
  gap: 20px;
}

.file-navigation {
  width: 200px;
  background: var(--color-surface);
  border-radius: var(--border-radius);
  padding: 16px;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  border-radius: calc(var(--border-radius) / 2);
  cursor: pointer;
  transition: all var(--duration-fast) var(--easing);
  margin-bottom: 8px;
  color: var(--color-text);
}

.nav-item:hover {
  background: var(--color-primary);
  color: white;
}

.nav-item.active {
  background: var(--color-primary);
  color: white;
}

.nav-icon {
  font-size: 20px;
}

.nav-text {
  font-size: var(--font-size-sm);
  font-weight: 500;
}

.file-list {
  flex: 1;
  background: var(--color-surface);
  border-radius: var(--border-radius);
  padding: 16px;
}

.file-list-header {
  margin-bottom: 20px;
}

.file-list-header h3 {
  font-size: 1.1rem;
  font-weight: 600;
  color: var(--color-text);
}

.file-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 16px;
}

.file-item {
  background: var(--color-background);
  border: 1px solid var(--color-border);
  border-radius: calc(var(--border-radius) / 2);
  padding: 16px;
  cursor: pointer;
  transition: all var(--duration-fast) var(--easing);
}

.file-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px var(--color-shadow);
}

.file-icon {
  font-size: 48px;
  margin-bottom: 12px;
  text-align: center;
}

.file-info {
  text-align: center;
}

.file-name {
  font-size: var(--font-size-sm);
  font-weight: 500;
  color: var(--color-text);
  margin-bottom: 4px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.file-date {
  font-size: var(--font-size-xs);
  color: var(--color-text-secondary);
}
</style>
