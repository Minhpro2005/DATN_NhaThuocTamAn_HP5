<template>
  <div class="upload-avatar">
    <input type="file" class="form-control mb-2" @change="handleFileChange" />
    <div v-if="previewUrl" class="preview">
      <img :src="previewUrl" class="rounded" width="80" height="80" style="object-fit: cover" />
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'

const props = defineProps({ initial: String })
const emit = defineEmits(['file-selected'])

const previewUrl = ref(null)

watch(
  () => props.initial,
  () => {
    previewUrl.value = props.initial ? getFullImageUrl(props.initial) : null
  },
  { immediate: true },
)

function getFullImageUrl(path) {
  return path ? `http://localhost:8080/${path.startsWith('/') ? path.slice(1) : path}` : ''
}

function handleFileChange(e) {
  const file = e.target.files[0]
  previewUrl.value = URL.createObjectURL(file)
  emit('file-selected', file)
}
</script>
