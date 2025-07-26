<template>
  <section class="py-4">
    <div class="container">
      <h2 class="text-success fw-bold mb-4">📰 Tin tức y tế</h2>
      <div class="row">
        <div class="col-md-4 mb-4" v-for="news in newsList" :key="news.maTin">
          <div class="card news-card shadow-sm h-100">
            <img :src="news.hinhAnh" class="card-img-top" :alt="news.tieuDe" />
            <div class="card-body d-flex flex-column justify-content-between">
              <div>
                <p class="text-muted small mb-1">
                  <i class="bi bi-calendar2-week me-1"></i>{{ formatDate(news.ngayDang) }}
                </p>
                <h5 class="card-title">{{ news.tieuDe }}</h5>
              </div>
              <router-link
                :to="`/tin-tuc/${news.maTin}`"
                class="btn btn-outline-success btn-sm mt-3 px-3 py-1 align-self-start"
              >
                ĐỌC THÊM
              </router-link>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const newsList = ref([])

const taiTinTuc = async () => {
  try {
    const res = await axios.get('http://localhost:8080/api/tintuc')
    newsList.value = res.data
      .filter((t) => t.trangThai) // ✅ chỉ hiển thị bài đang mở
      .map((t) => ({
        ...t,
        hinhAnh: t.hinhAnh.startsWith('http') ? t.hinhAnh : 'http://localhost:8080/' + t.hinhAnh,
      }))
  } catch (err) {
    console.error('❌ Lỗi khi tải tin tức:', err)
  }
}

const formatDate = (dateStr) => {
  const d = new Date(dateStr)
  return d.toLocaleDateString('vi-VN', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
  })
}

onMounted(taiTinTuc)
</script>

<style scoped>
.news-card {
  border-radius: 12px;
  overflow: hidden;
  transition: all 0.3s ease;
  border: 1px solid #eee;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.news-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 24px rgba(0, 123, 255, 0.15);
  border-color: #4caf50;
}

.card-img-top {
  width: 100%;
  height: 200px;
  object-fit: cover;
  border-top-left-radius: 12px;
  border-top-right-radius: 12px;
}

.card-body {
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
</style>
