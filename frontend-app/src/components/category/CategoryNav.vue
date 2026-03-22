<template>
  <div class="category-nav">
    <button
      v-for="item in items"
      :key="item.value"
      class="category-pill"
      :class="{ active: item.value === activeValue }"
      type="button"
      @click="$emit('change', Number(item.value))"
    >
      <span class="category-pill__icon">{{ getIcon(item.label) }}</span>
      <span class="category-pill__label">{{ item.label }}</span>
    </button>
  </div>
</template>

<script setup lang="ts">
import type { OptionItem } from '@/types/api'

defineProps<{
  items: OptionItem[]
  activeValue: number
}>()

defineEmits<{
  change: [value: number]
}>()

function getIcon(label: string | number) {
  switch (String(label)) {
    case '动画':
      return '番'
    case '音乐':
      return '音'
    case '游戏':
      return '游'
    case '知识':
      return '知'
    default:
      return '分'
  }
}
</script>

<style scoped lang="scss">
.category-nav {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 14px;
}

.category-pill {
  padding: 18px 16px;
  border: 1px solid #e6edf7;
  border-radius: 22px;
  background: #fff;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 12px;
  text-align: left;
  transition: all 0.2s ease;
}

.category-pill:hover {
  transform: translateY(-2px);
  box-shadow: 0 10px 24px rgba(15, 23, 42, 0.08);
}

.category-pill.active {
  border-color: #0f172a;
  background: #0f172a;
  color: #fff;
}

.category-pill__icon {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: #eff6ff;
  color: #0f172a;
  display: grid;
  place-items: center;
  font-weight: 700;
}

.category-pill.active .category-pill__icon {
  background: rgba(255, 255, 255, 0.18);
  color: #fff;
}

.category-pill__label {
  font-size: 15px;
  font-weight: 600;
}
</style>