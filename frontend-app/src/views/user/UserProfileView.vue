<template>
  <div class="page-shell profile-page">
    <section class="profile-hero">
      <div class="profile-hero__main">
        <AuthorCard :name="displayName" :description="signatureText" />
        <div class="profile-actions">
          <el-button v-if="showFollowButton" :type="homeData?.followed ? 'default' : 'primary'" @click="handleFollowToggle">
            {{ homeData?.followed ? '已关注' : '关注' }}
          </el-button>
          <el-button v-if="showMessageButton" @click="handleStartConversation">发私信</el-button>
        </div>
      </div>
      <div class="profile-hero__stats">
        <article>
          <strong>{{ homeData?.fansCount ?? 0 }}</strong>
          <span>粉丝</span>
        </article>
        <article>
          <strong>{{ homeData?.followingCount ?? 0 }}</strong>
          <span>关注</span>
        </article>
        <article>
          <strong>{{ homeData?.videoCount ?? 0 }}</strong>
          <span>投稿</span>
        </article>
      </div>
    </section>

    <section class="profile-board">
      <div class="profile-main">
        <section class="section-card">
          <header class="section-heading">
            <div class="section-title-block">
              <p class="section-eyebrow">社交关系</p>
              <h2>粉丝与关注</h2>
            </div>
          </header>
          <div class="relation-grid">
            <div class="relation-panel">
              <h3>粉丝列表</h3>
              <div v-if="followers.length" class="relation-list">
                <RouterLink v-for="item in followers" :key="item.uid" :to="`/users/${item.uid}`" class="relation-item">
                  <strong>{{ item.nickname || item.username || `用户 ${item.uid}` }}</strong>
                  <span>{{ item.signature || '这个人还没有留下签名。' }}</span>
                </RouterLink>
              </div>
              <EmptyState v-else title="还没有粉丝" description="有人关注后会显示在这里。" />
            </div>

            <div class="relation-panel">
              <h3>关注列表</h3>
              <div v-if="following.length" class="relation-list">
                <RouterLink v-for="item in following" :key="item.uid" :to="`/users/${item.uid}`" class="relation-item">
                  <strong>{{ item.nickname || item.username || `用户 ${item.uid}` }}</strong>
                  <span>{{ item.signature || '这个人还没有留下签名。' }}</span>
                </RouterLink>
              </div>
              <EmptyState v-else title="还没有关注任何人" description="你关注的用户会显示在这里。" />
            </div>
          </div>
        </section>

        <section class="section-card">
          <header class="section-heading">
            <div class="section-title-block">
              <p class="section-eyebrow">投稿内容</p>
              <h2>用户投稿</h2>
            </div>
          </header>
          <VideoList :videos="userVideos" />
          <AppPagination :current-page="videoPageNum" :page-size="videoPageSize" :total="videoTotal" @change="handleVideoPageChange" />
        </section>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import AppPagination from '@/components/common/AppPagination.vue'
import EmptyState from '@/components/common/EmptyState.vue'
import AuthorCard from '@/components/video/AuthorCard.vue'
import VideoList from '@/components/video/VideoList.vue'
import { getConversations } from '@/api/message'
import { getUserVideos } from '@/api/video'
import { followUser, getFollowers, getFollowing, getUserProfile, unfollowUser } from '@/api/user'
import type { UserHome, UserProfile } from '@/types/user'
import type { VideoCard } from '@/types/video'
import { useMessageStore } from '@/stores/messageStore'
import { useUserStore } from '@/stores/userStore'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const messageStore = useMessageStore()
const homeData = ref<UserHome | null>(null)
const followers = ref<UserProfile[]>([])
const following = ref<UserProfile[]>([])
const userVideos = ref<VideoCard[]>([])
const videoTotal = ref(0)
const videoPageNum = ref(1)
const videoPageSize = ref(8)

const targetUid = computed(() => Number(route.params.uid))
const displayName = computed(() => homeData.value?.profile?.nickname || homeData.value?.username || '用户主页')
const signatureText = computed(() => homeData.value?.profile?.signature || '这个人很神秘，还没有留下签名。')
const isSelf = computed(() => userStore.currentUser?.uid === targetUid.value)
const showFollowButton = computed(() => userStore.isLoggedIn && !isSelf.value)
const showMessageButton = computed(() => userStore.isLoggedIn && !isSelf.value)

onMounted(async () => {
  if (userStore.isLoggedIn && !userStore.currentUser) {
    await userStore.fetchCurrentUser()
  }
  await loadUserHome()
})

watch(
  () => route.params.uid,
  async () => {
    videoPageNum.value = 1
    await loadUserHome()
  }
)

async function loadUserHome() {
  if (Number.isNaN(targetUid.value)) return

  const [profileResponse, followerResponse, followingResponse, videoResponse] = await Promise.all([
    getUserProfile(targetUid.value),
    getFollowers(targetUid.value, 1, 6),
    getFollowing(targetUid.value, 1, 6),
    getUserVideos(targetUid.value, videoPageNum.value, videoPageSize.value)
  ])

  homeData.value = profileResponse.data
  followers.value = followerResponse.data.records
  following.value = followingResponse.data.records
  userVideos.value = videoResponse.data.records
  videoTotal.value = videoResponse.data.total
  videoPageNum.value = videoResponse.data.pageNum
  videoPageSize.value = videoResponse.data.pageSize
}

async function handleVideoPageChange(page: number) {
  videoPageNum.value = page
  await loadUserHome()
}

async function handleFollowToggle() {
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录')
    return
  }

  if (homeData.value?.followed) {
    await unfollowUser(targetUid.value)
    ElMessage.success('已取消关注')
  } else {
    await followUser(targetUid.value)
    ElMessage.success('关注成功')
  }

  await loadUserHome()
}

async function handleStartConversation() {
  if (!userStore.isLoggedIn) {
    ElMessage.warning('请先登录')
    return
  }

  const existing = await getConversations(1, 50)
  const matched = existing.data.records.find((item) => item.targetUid === targetUid.value)

  if (matched) {
    await router.push(`/messages/conversations/${matched.conversationId}`)
    return
  }

  const conversation = await messageStore.createConversation(targetUid.value)
  await router.push(`/messages/conversations/${conversation.conversationId}`)
}
</script>

<style scoped lang="scss">
.profile-page,
.profile-main {
  display: grid;
  gap: 22px;
}

.profile-hero {
  padding: 28px;
  border-radius: 30px;
  background: linear-gradient(135deg, #dbeafe, #fff7ed 58%, #fae8ff);
  display: grid;
  gap: 22px;
}

.profile-hero__main,
.profile-hero__stats {
  display: grid;
  gap: 16px;
}

.profile-actions {
  display: flex;
  gap: 12px;
}

.profile-hero__stats {
  grid-template-columns: repeat(3, minmax(0, 1fr));
}

.profile-hero__stats article {
  padding: 18px;
  border-radius: 18px;
  background: rgba(255, 255, 255, 0.72);
  display: grid;
  gap: 6px;
}

.profile-hero__stats strong {
  font-size: 28px;
}

.profile-board,
.relation-grid {
  display: grid;
  gap: 20px;
}

.relation-grid {
  grid-template-columns: repeat(2, minmax(0, 1fr));
}

.section-card,
.relation-panel {
  padding: 22px;
  border-radius: 24px;
  border: 1px solid #e7ebf3;
  background: #fff;
}

.relation-panel h3 {
  margin: 0 0 14px;
}

.relation-list {
  display: grid;
  gap: 10px;
}

.relation-item {
  display: grid;
  gap: 4px;
  padding: 12px 14px;
  border-radius: 16px;
  background: #f8fafc;
}

.relation-item span {
  color: #64748b;
  font-size: 13px;
}
</style>