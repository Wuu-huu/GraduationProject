<template>
  <div class="action-bar">
    <el-button round :type="state?.liked ? 'primary' : 'default'" :loading="loading" @click="$emit('like')">
      {{ state?.liked ? '已点赞' : '点赞' }}
    </el-button>
    <el-button round :loading="loading" @click="$emit('coin')">
      投币 {{ state?.coinCount ?? 0 }}
    </el-button>
    <el-button round :type="state?.favorited ? 'primary' : 'default'" :loading="loading" @click="$emit('favorite')">
      {{ state?.favorited ? '已收藏' : '收藏' }}
    </el-button>
    <el-button round :type="state?.watchLater ? 'primary' : 'default'" :loading="loading" @click="$emit('watchLater')">
      {{ state?.watchLater ? '已加入稍后再看' : '稍后再看' }}
    </el-button>
  </div>
</template>

<script setup lang="ts">
import type { UserVideoState } from '@/types/interaction'

defineProps<{
  state?: UserVideoState | null
  loading?: boolean
}>()

defineEmits<{
  (e: 'like'): void
  (e: 'coin'): void
  (e: 'favorite'): void
  (e: 'watchLater'): void
}>()
</script>

<style scoped lang="scss">
.action-bar {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}
</style>