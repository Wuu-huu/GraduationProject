<template>
  <section class="comment-list">
    <div v-for="item in comments" :key="item.commentId" class="comment-item">
      <div class="comment-head">
        <h4>{{ item.nickname }}</h4>
        <span>{{ item.createTime }}</span>
      </div>
      <p>{{ item.content }}</p>
      <div class="comment-meta">
        <span>{{ item.likeCount }} 点赞</span>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import type { CommentItem } from '@/types/comment'

defineProps<{
  comments: CommentItem[]
}>()
</script>

<style scoped lang="scss">
.comment-list {
  display: grid;
  gap: 14px;
}

.comment-item {
  padding: 16px;
  border-radius: 18px;
  border: 1px solid #e7ebf3;
  background: #fff;
}

.comment-head,
.comment-meta {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.comment-head {
  margin-bottom: 8px;

  h4 {
    margin: 0;
  }

  span {
    color: #94a3b8;
    font-size: 12px;
  }
}

.comment-meta {
  margin-top: 10px;
  color: #64748b;
  font-size: 13px;
}
</style>
