<template>
  <div class="tag-input">
    <el-tag v-for="tag in modelValue" :key="tag" closable @close="removeTag(tag)">{{ tag }}</el-tag>
    <el-input v-model="draft" class="tag-input__field" placeholder="输入标签后按回车添加" @keyup.enter="appendTag" />
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'

const props = defineProps<{
  modelValue: string[]
}>()

const emit = defineEmits<{
  (e: 'update:modelValue', value: string[]): void
}>()

const draft = ref('')

function appendTag() {
  const value = draft.value.trim()
  if (!value || props.modelValue.includes(value)) return
  emit('update:modelValue', [...props.modelValue, value])
  draft.value = ''
}

function removeTag(tag: string) {
  emit('update:modelValue', props.modelValue.filter((item) => item !== tag))
}
</script>

<style scoped lang="scss">
.tag-input {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  padding: 12px;
  border: 1px solid #e7ebf3;
  border-radius: 16px;
  background: #fff;
}

.tag-input__field {
  width: 180px;
}
</style>
