<template>
  <nav class="mt-3">
    <ul class="pagination justify-content-center">
      <li
        class="page-item"
        :class="{ disabled: currentPage === 1 }"
        @click="emitPage(currentPage - 1)"
      >
        <a class="page-link" href="#">Trước</a>
      </li>
      <li
        v-for="page in pages"
        :key="page"
        class="page-item"
        :class="{ active: page === currentPage }"
        @click="emitPage(page)"
      >
        <a class="page-link" href="#">{{ page }}</a>
      </li>
      <li
        class="page-item"
        :class="{ disabled: currentPage === totalPages }"
        @click="emitPage(currentPage + 1)"
      >
        <a class="page-link" href="#">Tiếp</a>
      </li>
    </ul>
  </nav>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  currentPage: { type: Number, required: true },
  totalPages: { type: Number, required: true },
})
const emit = defineEmits(['change-page'])

const pages = computed(() => {
  const arr = []
  for (let i = 1; i <= props.totalPages; i++) arr.push(i)
  return arr
})

function emitPage(page) {
  if (page >= 1 && page <= props.totalPages) {
    emit('change-page', page)
  }
}
</script>
