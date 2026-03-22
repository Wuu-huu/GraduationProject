<template>
  <div class="comment-input">
    <el-input
      v-model="content"
      type="textarea"
      :rows="4"
      placeholder="写下你的评论内容..."
      maxlength="1000"
      show-word-limit
    />
    <div class="comment-input__footer">
      <el-button type="primary" :loading="loading" @click="handleSubmit">发布评论</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

defineProps<{
  loading?: boolean
}>()

const emit = defineEmits<{
  (e: 'submit', content: string): void
}>()

const content = ref('')

function handleSubmit() {
  const value = content.value.trim()

  if (!value) {
    ElMessage.warning('请输入评论内容')
    return
  }

  emit('submit', value)
  content.value = ''
}
</script>

<style scoped lang="scss">
.comment-input {
  display: grid;
  gap: 12px;

  &__footer {
    display: flex;
    justify-content: flex-end;
  }
}
</style>