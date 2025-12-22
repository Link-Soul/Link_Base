<template>
  <div class="window-manager">
    <transition-group name="window" tag="div">
      <Window
        v-for="window in windows"
        :key="window.id"
        :window-data="window"
        :is-active="window.id === activeWindowId"
        @close="closeWindow"
        @minimize="minimizeWindow"
        @maximize="maximizeWindow"
        @focus="setActiveWindow"
        @move="moveWindow"
        @resize="resizeWindow"
      />
    </transition-group>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useDesktopStore } from '@/stores/desktop'
import Window from './Window.vue'

const desktopStore = useDesktopStore()

const windows = computed(() => desktopStore.windows)
const activeWindowId = computed(() => desktopStore.activeWindowId)

const closeWindow = (windowId) => {
  desktopStore.closeWindow(windowId)
}

const minimizeWindow = (windowId) => {
  desktopStore.minimizeWindow(windowId)
}

const maximizeWindow = (windowId) => {
  desktopStore.maximizeWindow(windowId)
}

const setActiveWindow = (windowId) => {
  desktopStore.setActiveWindow(windowId)
}

const moveWindow = ({ windowId, x, y }) => {
  desktopStore.moveWindow(windowId, x, y)
}

const resizeWindow = ({ windowId, width, height }) => {
  desktopStore.resizeWindow(windowId, width, height)
}
</script>

<style scoped>
.window-manager {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
}

.window-manager > * {
  pointer-events: auto;
}

/* 窗口动画 */
.window-enter-active,
.window-leave-active {
  transition: all var(--duration-normal) var(--easing);
}

.window-enter-from {
  opacity: 0;
  transform: scale(0.8) translateY(-20px);
}

.window-leave-to {
  opacity: 0;
  transform: scale(0.8) translateY(20px);
}

.window-move {
  transition: transform var(--duration-normal) var(--easing);
}
</style>