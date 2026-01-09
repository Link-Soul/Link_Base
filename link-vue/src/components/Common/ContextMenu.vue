<template>
  <teleport to="body">
    <div
      ref="menuRef"
      v-if="show"
      class="context-menu"
      :style="menuStyle"
      @click.stop
    >
      <div
        v-for="(item, index) in items"
        :key="index"
        class="menu-item"
        :class="{
          separator: item.separator,
          disabled: item.disabled,
        }"
        @click="handleItemClick(item)"
      >
        <div v-if="!item.separator" class="menu-item-content">
          <span v-if="item.icon" class="menu-icon">{{ item.icon }}</span>
          <span class="menu-label">{{ item.label }}</span>
          <span v-if="item.shortcut" class="menu-shortcut">{{
            item.shortcut
          }}</span>
        </div>
      </div>
    </div>
  </teleport>
</template>

<script setup>
import { computed, onMounted, onUnmounted, ref } from "vue";

const props = defineProps({
  show: {
    type: Boolean,
    default: false,
  },
  x: {
    type: Number,
    default: 0,
  },
  y: {
    type: Number,
    default: 0,
  },
  items: {
    type: Array,
    default: () => [],
  },
});

const emit = defineEmits(["close", "select"]);

const menuRef = ref(null);

const menuStyle = computed(() => {
  return {
    left: `${props.x}px`,
    top: `${props.y}px`,
  };
});

const handleItemClick = (item) => {
  if (item.separator || item.disabled) return;

  emit("select", item.action);
  emit("close");
};

const handleClickOutside = (event) => {
  if (menuRef.value && !menuRef.value.contains(event.target)) {
    emit("close");
  }
};

const handleKeydown = (event) => {
  if (event.key === "Escape") {
    emit("close");
  }
};

onMounted(() => {
  document.addEventListener("click", handleClickOutside);
  document.addEventListener("keydown", handleKeydown);
});

onUnmounted(() => {
  document.removeEventListener("click", handleClickOutside);
  document.removeEventListener("keydown", handleKeydown);
});
</script>

<style scoped>
.context-menu {
  position: fixed;
  z-index: 9999;
  min-width: 180px;
  background: var(--color-window-background);
  backdrop-filter: var(--backdrop-filter);
  border: 1px solid var(--color-border);
  border-radius: var(--border-radius);
  box-shadow: 0 8px 32px var(--color-shadow);
  padding: 4px 0;
  animation: contextMenuFadeIn var(--duration-fast) var(--easing);
}

.menu-item {
  padding: 0;
  cursor: default;
}

.menu-item.separator {
  height: 1px;
  background: var(--color-border);
  margin: 4px 12px;
}

.menu-item.disabled {
  pointer-events: none;
}

.menu-item-content {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 8px 16px;
  color: var(--color-text);
  font-size: var(--font-size-sm);
  transition: background-color var(--duration-fast) var(--easing);
}

.menu-item:hover:not(.separator):not(.disabled) .menu-item-content {
  background: var(--color-surface);
}

.menu-item.disabled .menu-item-content {
  color: var(--color-text-secondary);
  opacity: 0.6;
}

.menu-icon {
  width: 16px;
  text-align: center;
  font-size: 14px;
}

.menu-label {
  flex: 1;
}

.menu-shortcut {
  color: var(--color-text-secondary);
  font-size: var(--font-size-xs);
}

@keyframes contextMenuFadeIn {
  from {
    opacity: 0;
    transform: scale(0.95);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}
</style>
