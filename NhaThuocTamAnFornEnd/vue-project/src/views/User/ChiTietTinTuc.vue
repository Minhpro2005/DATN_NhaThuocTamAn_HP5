<template>
  <div>
    <Header />
    <Navbar />
  </div>

  <div class="container py-4" v-if="tin">
    <h4 class="text-success mb-3">📰 {{ tin.tieuDe }}</h4>

    <p class="text-muted">🗓 Ngày đăng: {{ formatDate(tin.ngayDang) }}</p>

    <img
      v-if="tin.hinhAnh"
      :src="getFullImageUrl(tin.hinhAnh)"
      alt="Ảnh bài viết"
      class="image-full rounded mb-4"
    />

    <!-- Nội dung -->
    <div class="noi-dung mt-4">
      <div v-html="isExpanded ? tin.noiDung : shortContent"></div>
      <div class="text-center mt-3">
        <button class="btn btn-lg btn-success fw-bold px-4" @click="isExpanded = !isExpanded">
          {{ isExpanded ? '▲ Thu gọn' : '▼ Xem thêm' }}
        </button>
      </div>
    </div>
  </div>

  <div v-else class="container py-5 text-center text-muted">
    <p>Không tìm thấy bài viết.</p>
  </div>

  <Footer />
</template>

<script setup>
import Header from '../User/Header.vue'
import Navbar from '../User/Navbar.vue'
import Footer from '../User/Footer.vue'

import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import axios from 'axios'

const route = useRoute()
const tin = ref(null)
const isExpanded = ref(false)

onMounted(async () => {
  const id = route.params.maTin
  try {
    const res = await axios.get(`http://localhost:8080/api/tintuc/${id}`)
    tin.value = res.data
  } catch (err) {
    console.error('❌ Không tìm thấy bài viết', err)
  }
})

function formatDate(dateStr) {
  const d = new Date(dateStr)
  return d.toLocaleDateString('vi-VN')
}

const shortContent = computed(() => {
  if (!tin.value || !tin.value.noiDung) return ''
  const tempDiv = document.createElement('div')
  tempDiv.innerHTML = tin.value.noiDung
  return tempDiv.innerHTML.slice(0, 600) + '...'
})

const getFullImageUrl = (path) => {
  if (!path) return ''
  return path.startsWith('http') ? path : `http://localhost:8080/${path}`
}
</script>

<style scoped>
.noi-dung {
  line-height: 1.7;
  font-size: 1rem;
  transition: all 0.3s ease;
}

.btn-success {
  font-size: 1.1rem;
  border-radius: 999px;
}

.image-fit {
  width: 100%;
  max-height: 400px;
  object-fit: contain; /* ✅ ảnh sẽ hiển thị đầy đủ */
  object-position: center;
  background-color: #f8f9fa; /* ✅ lấp khoảng trống nếu ảnh không phủ hết */
  border-radius: 12px;
}
</style>
