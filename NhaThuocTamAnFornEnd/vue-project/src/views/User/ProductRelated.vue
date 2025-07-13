<template>
  <section class="related-wrapper mt-5" v-if="products.length">
    <div class="related-box">
      <h5 class="section-title mb-4">
        <i class="bi bi-fire text-success me-2"></i>
        Sản phẩm liên quan
      </h5>
      <div class="row">
        <div class="col-2-4 mb-4" v-for="item in products" :key="item.maThuoc">
          <router-link :to="`/chi-tiet/${item.maThuoc}`" class="text-decoration-none">
            <div class="card h-100">
              <img
                :src="getImageUrl(item.hinhAnhChinh)"
                class="card-img-top"
                :alt="item.tenThuoc"
              />
              <div class="card-body">
                <h5 class="card-title">{{ item.tenThuoc }}</h5>
                <p class="card-text text-success fw-bold">Giá: {{ formatCurrency(item.giaBan) }}</p>
                <router-link :to="`/chi-tiet/${item.maThuoc}`" class="btn btn-green w-100">
                  Mua ngay
                </router-link>
              </div>
            </div>
          </router-link>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref, watch } from 'vue'

const props = defineProps({
  maDM: Number,
  excludeId: Number,
})

const products = ref([])

watch(
  () => [props.maDM, props.excludeId],
  async ([maDM, excludeId]) => {
    if (maDM && excludeId != null) {
      try {
        const res = await fetch(`http://localhost:8080/api/thuoc/search?maDM=${maDM}`)
        const all = await res.json()
        console.log('✅ API trả về:', all)

        const filtered = all.filter((p) => p.maThuoc !== excludeId)
        products.value = shuffleArray(filtered).slice(0, 5)
      } catch (err) {
        console.error('Lỗi fetch sản phẩm liên quan:', err)
      }
    }
  },
  { immediate: true },
)

function shuffleArray(array) {
  const shuffled = array.slice()
  for (let i = shuffled.length - 1; i > 0; i--) {
    const j = Math.floor(Math.random() * (i + 1))
    ;[shuffled[i], shuffled[j]] = [shuffled[j], shuffled[i]]
  }
  return shuffled
}

function getImageUrl(path) {
  return path?.startsWith('http') ? path : `http://localhost:8080/${path}`
}

function formatCurrency(value) {
  return Number(value).toLocaleString('vi-VN') + 'đ'
}
</script>

<style scoped>
.related-wrapper {
  padding-top: 32px;
}
.related-box {
  background-color: #fff;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.06);
  max-width: 960px;
  margin: auto;
}
.col-2-4 {
  flex: 0 0 auto;
  width: 20%;
}
@media (max-width: 768px) {
  .col-2-4 {
    width: 50%;
  }
}
@media (max-width: 576px) {
  .col-2-4 {
    width: 100%;
  }
}
.card {
  border-radius: 12px;
  overflow: hidden;
  transition:
    transform 0.3s ease,
    box-shadow 0.3s ease;
  box-shadow: 0 0 8px rgba(0, 0, 0, 0.05);
}
.card:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 24px rgba(0, 100, 0, 0.3);
}
.card-img-top {
  border-top-left-radius: 12px;
  border-top-right-radius: 12px;
  object-fit: cover;
}
.btn-green {
  background-color: #2e7d32;
  color: white;
  border: none;
  border-radius: 8px;
  padding: 10px 20px;
  transition: background-color 0.3s ease;
}
.btn-green:hover {
  background-color: #1b5e20;
}
.card-title {
  font-size: 1rem;
  font-weight: 500;
  margin-bottom: 12px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: normal;
  line-height: 1.6;
  height: calc(1.6em * 2);
}
</style>
