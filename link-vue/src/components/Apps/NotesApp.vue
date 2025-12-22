<template>
  <div class="notes-app">
    <div class="notes-toolbar">
      <button class="toolbar-btn" @click="newNote" title="新建笔记">
        <span>📝</span>
      </button>
      <button class="toolbar-btn" @click="saveNote" title="保存笔记">
        <span>💾</span>
      </button>
      <button class="toolbar-btn" @click="deleteNote" title="删除笔记" :disabled="!currentNoteId">
        <span>🗑️</span>
      </button>
      <div class="toolbar-spacer"></div>
      <span class="note-count">{{ notes.length }} 条笔记</span>
    </div>
    
    <div class="notes-content">
      <div class="notes-list">
        <div
          v-for="note in notes"
          :key="note.id"
          class="note-item"
          :class="{ 'active': note.id === currentNoteId }"
          @click="selectNote(note.id)"
        >
          <div class="note-title">{{ note.title || '无标题' }}</div>
          <div class="note-preview">{{ note.content.substring(0, 50) }}...</div>
          <div class="note-date">{{ formatDate(note.updatedAt) }}</div>
        </div>
      </div>
      
      <div class="note-editor">
        <input
          v-model="currentTitle"
          type="text"
          class="note-title-input"
          placeholder="笔记标题..."
          @input="updateNote"
        />
        <textarea
          v-model="currentContent"
          class="note-content-input"
          placeholder="开始输入笔记内容..."
          @input="updateNote"
        ></textarea>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'

const notes = ref([])
const currentNoteId = ref(null)
const currentTitle = ref('')
const currentContent = ref('')

const currentNote = computed(() => {
  return notes.value.find(note => note.id === currentNoteId.value)
})

const newNote = () => {
  const newNote = {
    id: Date.now().toString(),
    title: '',
    content: '',
    createdAt: new Date().toISOString(),
    updatedAt: new Date().toISOString()
  }
  
  notes.value.unshift(newNote)
  selectNote(newNote.id)
}

const selectNote = (noteId) => {
  currentNoteId.value = noteId
  const note = notes.value.find(n => n.id === noteId)
  if (note) {
    currentTitle.value = note.title
    currentContent.value = note.content
  }
}

const updateNote = () => {
  if (!currentNoteId.value) return
  
  const noteIndex = notes.value.findIndex(n => n.id === currentNoteId.value)
  if (noteIndex !== -1) {
    notes.value[noteIndex] = {
      ...notes.value[noteIndex],
      title: currentTitle.value,
      content: currentContent.value,
      updatedAt: new Date().toISOString()
    }
  }
}

const saveNote = () => {
  if (!currentNoteId.value) return
  
  updateNote()
  localStorage.setItem('notes', JSON.stringify(notes.value))
}

const deleteNote = () => {
  if (!currentNoteId.value) return
  
  const noteIndex = notes.value.findIndex(n => n.id === currentNoteId.value)
  if (noteIndex !== -1) {
    notes.value.splice(noteIndex, 1)
    currentNoteId.value = null
    currentTitle.value = ''
    currentContent.value = ''
    
    localStorage.setItem('notes', JSON.stringify(notes.value))
  }
}

const formatDate = (dateString) => {
  const date = new Date(dateString)
  const now = new Date()
  const diff = now - date
  
  if (diff < 60000) return '刚刚'
  if (diff < 3600000) return `${Math.floor(diff / 60000)} 分钟前`
  if (diff < 86400000) return `${Math.floor(diff / 3600000)} 小时前`
  if (diff < 604800000) return `${Math.floor(diff / 86400000)} 天前`
  
  return date.toLocaleDateString()
}

const loadNotes = () => {
  const savedNotes = localStorage.getItem('notes')
  if (savedNotes) {
    notes.value = JSON.parse(savedNotes)
  } else {
    // 创建默认笔记
    newNote()
    currentTitle.value = '欢迎使用备忘录'
    currentContent.value = `这是你的第一条笔记！

你可以：
• 点击 📝 创建新笔记
• 点击 💾 保存笔记
• 点击 🗑️ 删除笔记
• 在左侧列表中选择不同的笔记`
    updateNote()
    saveNote()
  }
}

// 监听当前笔记变化，自动保存
watch([currentTitle, currentContent], () => {
  if (currentNoteId.value) {
    updateNote()
    localStorage.setItem('notes', JSON.stringify(notes.value))
  }
}, { deep: true })

onMounted(() => {
  loadNotes()
})
</script>

<style scoped>
.notes-app {
  display: flex;
  flex-direction: column;
  height: 100%;
  background: var(--color-background);
}

.notes-toolbar {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 16px;
  background: var(--color-surface);
  border-bottom: 1px solid var(--color-border);
}

.toolbar-btn {
  width: 32px;
  height: 32px;
  border: none;
  border-radius: calc(var(--border-radius) / 2);
  background: transparent;
  color: var(--color-text);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all var(--duration-fast) var(--easing);
}

.toolbar-btn:hover:not(:disabled) {
  background: var(--color-border);
}

.toolbar-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.toolbar-spacer {
  flex: 1;
}

.note-count {
  font-size: var(--font-size-xs);
  color: var(--color-text-secondary);
}

.notes-content {
  flex: 1;
  display: flex;
  overflow: hidden;
}

.notes-list {
  width: 250px;
  background: var(--color-surface);
  border-right: 1px solid var(--color-border);
  overflow-y: auto;
}

.note-item {
  padding: 12px 16px;
  border-bottom: 1px solid var(--color-border);
  cursor: pointer;
  transition: background-color var(--duration-fast) var(--easing);
}

.note-item:hover {
  background: var(--color-border);
}

.note-item.active {
  background: var(--color-primary);
  color: white;
}

.note-title {
  font-weight: 600;
  margin-bottom: 4px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.note-preview {
  font-size: var(--font-size-xs);
  color: var(--color-text-secondary);
  margin-bottom: 4px;
  line-height: 1.4;
}

.note-item.active .note-preview {
  color: rgba(255, 255, 255, 0.8);
}

.note-date {
  font-size: var(--font-size-xs);
  color: var(--color-text-secondary);
}

.note-item.active .note-date {
  color: rgba(255, 255, 255, 0.7);
}

.note-editor {
  flex: 1;
  display: flex;
  flex-direction: column;
  padding: 20px;
}

.note-title-input {
  width: 100%;
  padding: 12px 16px;
  border: none;
  border-bottom: 1px solid var(--color-border);
  background: transparent;
  font-size: 1.25rem;
  font-weight: 600;
  color: var(--color-text);
  outline: none;
  margin-bottom: 16px;
}

.note-title-input:focus {
  border-bottom-color: var(--color-primary);
}

.note-content-input {
  flex: 1;
  width: 100%;
  padding: 16px;
  border: none;
  background: transparent;
  font-size: var(--font-size-base);
  color: var(--color-text);
  outline: none;
  resize: none;
  line-height: 1.6;
}
</style>