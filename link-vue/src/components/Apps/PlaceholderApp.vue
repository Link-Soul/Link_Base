<template>
  <div class="placeholder-app">
    <div class="placeholder-content">
      <div class="app-icon">{{ icon || "📱" }}</div>
      <h2 class="app-title">{{ title || "应用" }}</h2>
      <p class="app-description">{{ description || "此应用正在开发中..." }}</p>
      <div class="app-actions">
        <button class="action-btn primary" @click="refresh">
          <span>🔄</span>
          刷新
        </button>
        <button class="action-btn secondary" @click="close">
          <span>❌</span>
          关闭
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from "vue";

const props = defineProps({
  title: {
    type: String,
    default: "",
  },
  description: {
    type: String,
    default: "",
  },
  icon: {
    type: String,
    default: "",
  },
});

const emit = defineEmits(["window-action"]);

const refresh = () => {
  console.log("Refreshing app...");
};

const close = () => {
  emit("window-action", "close");
};
</script>

<style scoped>
.placeholder-app {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  background: var(--color-background);
  padding: 40px;
}

.placeholder-content {
  text-align: center;
  max-width: 400px;
}

.app-icon {
  font-size: 4rem;
  margin-bottom: 20px;
}

.app-title {
  font-size: 1.5rem;
  font-weight: 600;
  margin-bottom: 12px;
  color: var(--color-text);
}

.app-description {
  font-size: var(--font-size-base);
  color: var(--color-text-secondary);
  margin-bottom: 30px;
  line-height: 1.6;
}

.app-actions {
  display: flex;
  gap: 12px;
  justify-content: center;
}

.action-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 20px;
  border: none;
  border-radius: var(--border-radius);
  font-size: var(--font-size-sm);
  font-weight: 500;
  cursor: pointer;
  transition: all var(--duration-fast) var(--easing);
}

.action-btn.primary {
  background: var(--color-primary);
  color: white;
}

.action-btn.primary:hover {
  background: var(--color-secondary);
}

.action-btn.secondary {
  background: var(--color-surface);
  color: var(--color-text);
  border: 1px solid var(--color-border);
}

.action-btn.secondary:hover {
  background: var(--color-border);
}
</style>
