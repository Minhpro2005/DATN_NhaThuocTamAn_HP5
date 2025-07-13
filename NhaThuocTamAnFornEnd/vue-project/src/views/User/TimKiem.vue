<template>
  <div class="search-bar position-relative">
    <div class="input-group">
      <input
        v-model="keyword"
        type="text"
        class="form-control"
        placeholder="Tìm sản phẩm..."
        @input="filterProducts"
        @keydown.enter="goToSearchPage"
      />
      <button class="btn btn-light" type="button" @click="goToSearchPage">
        <i class="bi bi-search"></i>
      </button>
    </div>

    <!-- Gợi ý -->
    <ul
      v-if="keyword && filteredProducts.length"
      class="list-group position-absolute w-100 mt-2 shadow z-3"
    >
      <router-link
        v-for="sp in filteredProducts.slice(0, 3)"
        :key="sp.maThuoc"
        :to="`/chi-tiet/${sp.maThuoc}`"
        class="list-group-item list-group-item-action d-flex align-items-center gap-3"
      >
        <img
          :src="getImageUrl(sp.hinhAnhChinh)"
          width="50"
          height="50"
          class="rounded object-fit-cover"
        />
        <div>
          <div class="fw-semibold">{{ sp.tenThuoc }}</div>
          <div class="text-danger small">{{ formatCurrency(sp.giaBan) }}</div>
        </div>
      </router-link>
    </ul>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const keyword = ref('')
const filteredProducts = ref([])
const router = useRouter()

function filterProducts() {
  const kw = keyword.value.trim()
  if (!kw) {
    filteredProducts.value = []
    return
  }

  fetch(`http://localhost:8080/api/thuoc/search?keyword=${encodeURIComponent(kw)}`)
    .then((res) => res.json())
    .then((data) => {
      filteredProducts.value = data
    })
    .catch((err) => {
      console.error('Lỗi khi tìm kiếm thuốc:', err)
      filteredProducts.value = []
    })
}

function goToSearchPage() {
  const kw = keyword.value.trim()
  if (kw) {
    router.push({ path: '/tim-kiem', query: { keyword: kw } })
  }
}

function formatCurrency(value) {
  return Number(value).toLocaleString('vi-VN') + '₫'
}

function getImageUrl(path) {
  if (!path) return 'https://via.placeholder.com/100'
  return path.startsWith('http')
    ? path
    : `http://localhost:8080/${path.startsWith('/') ? path.slice(1) : path}`
}
</script>

<style scoped>
.search-bar {
  max-width: 500px;
}
.list-group-item:hover {
  background-color: #f3f3f3;
}
.object-fit-cover {
  object-fit: cover;
}
</style>
