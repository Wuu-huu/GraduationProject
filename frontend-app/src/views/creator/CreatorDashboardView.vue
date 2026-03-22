<template>
  <div class="page-shell">
    <section class="creator-overview">
      <article>
        <strong>{{ stats.videoCount }}</strong>
        <span>Ͷ������</span>
      </article>
      <article>
        <strong>{{ stats.totalPlays }}</strong>
        <span>�ۼƲ���</span>
      </article>
      <article>
        <strong>{{ stats.totalLikes }}</strong>
        <span>�ۼƵ���</span>
      </article>
      <article>
        <strong>{{ stats.draftCount }}</strong>
        <span>�ݸ�����</span>
      </article>
    </section>

    <section class="panel-card">
      <div class="panel-head">
        <div>
          <p class="section-eyebrow">���Ͷ��</p>
          <h2>���������Ƶ</h2>
        </div>
      </div>
      <VideoList :videos="videos.slice(0, 4)" />
    </section>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import VideoList from '@/components/video/VideoList.vue'
import { getUserVideos } from '@/api/video'
import type { VideoCard } from '@/types/video'
import { useUserStore } from '@/stores/userStore'

const userStore = useUserStore()
const videos = ref<VideoCard[]>([])

const stats = computed(() => ({
  videoCount: videos.value.length,
  totalPlays: videos.value.reduce((sum, item) => sum + (item.playCount ?? 0), 0),
  totalLikes: videos.value.reduce((sum, item) => sum + (item.likeCount ?? 0), 0),
  draftCount: videos.value.filter((item) => !item.publishTime).length
}))

onMounted(async () => {
  if (!userStore.currentUser) {
    await userStore.fetchCurrentUser()
  }

  if (userStore.currentUser?.uid) {
    const response = await getUserVideos(userStore.currentUser.uid, 1, 12)
    videos.value = response.data.records
  }
})
</script>

<style scoped lang="scss">
.creator-overview {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 16px;

  article {
    padding: 20px;
    border-radius: 22px;
    border: 1px solid #e7ebf3;
    background: #fff;
    display: grid;
    gap: 6px;
  }
}

.panel-card {
  padding: 22px;
  border-radius: 24px;
  border: 1px solid #e7ebf3;
  background: #fff;
  display: grid;
  gap: 16px;
}

.panel-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.section-eyebrow {
  margin: 0 0 6px;
  color: #94a3b8;
  font-size: 12px;
  letter-spacing: 0.08em;
  text-transform: uppercase;
}
</style>
