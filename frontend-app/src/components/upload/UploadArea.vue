<template>
  <div class="upload-area">
    <div class="upload-area__head">
      <h3>{{ title }}</h3>
      <p v-if="description">{{ description }}</p>
    </div>
    <el-input :model-value="modelValue" :placeholder="placeholder" @update:model-value="$emit('update:modelValue', $event)" />
    <slot />
  </div>
</template>

<script setup lang="ts">
defineProps<{
  modelValue?: string
  title?: string
  description?: string
  placeholder?: string
}>()

defineEmits<{
  (e: 'update:modelValue', value: string): void
}>()
</script>

<style scoped lang="scss">
.upload-area {
  border: 1px dashed #94a3b8;
  border-radius: 20px;
  padding: 24px;
  background: #fff;
  display: grid;
  gap: 14px;
}

.upload-area__head {
  display: grid;
  gap: 6px;

  h3,
  p {
    margin: 0;
  }

  p {
    color: #64748b;
  }
}
</style>