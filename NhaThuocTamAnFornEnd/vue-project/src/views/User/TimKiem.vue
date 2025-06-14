<template>
  <div class="search-bar position-relative">
    <div class="input-group">
      <input
        v-model="keyword"
        type="text"
        class="form-control"
        placeholder="Tìm sản phẩm..."
        @input="filterProducts"
      />
      <button class="btn btn-light" type="button">
        <i class="bi bi-search"></i>
      </button>
    </div>

    <!-- Kết quả gợi ý -->
    <ul
      v-if="keyword && filteredProducts.length"
      class="list-group position-absolute w-100 mt-2 shadow z-3"
    >
      <router-link
        v-for="sp in filteredProducts"
        :key="sp.id"
        :to="`product-detail/:id`"
        class="list-group-item list-group-item-action d-flex align-items-center gap-3"
      >
        <img :src="sp.hinhAnh" width="50" height="50" class="rounded object-fit-cover" />
        <div>
          <div class="fw-semibold">{{ sp.ten }}</div>
          <div class="text-danger small">{{ formatGia(sp.gia) }}</div>
        </div>
      </router-link>
    </ul>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const keyword = ref('')
const filteredProducts = ref([])

const sanPhamMau = [
  {
    id: 1,
    ten: 'Paracetamol 500mg',
    gia: 25000,
    hinhAnh:
      'https://cdn.nhathuoclongchau.com.vn/unsafe/144x0/filters:quality(90)/https://cms-prod.s3-sgn09.fptcloud.com/IMG_0270_9483dadf83.jpg',
  },
  {
    id: 2,
    ten: 'Vitamin C 1000mg',
    gia: 39000,
    hinhAnh:
      'https://cdn.nhathuoclongchau.com.vn/unsafe/256x0/filters:quality(90)/https://cms-prod.s3-sgn09.fptcloud.com/00021929_perfect_white_60v_3739_600f_large_6794b33ce3.JPG',
  },
  {
    id: 3,
    ten: 'Khẩu trang y tế',
    gia: 12000,
    hinhAnh:
      'https://cdn.nhathuoclongchau.com.vn/unsafe/144x0/filters:quality(90)/https://cms-prod.s3-sgn09.fptcloud.com/DSC_09751_ac0903b1b2.jpg',
  },
  {
    id: 3,
    ten: 'vitamin b',
    gia: 12000,
    hinhAnh:
      'https://cdn.nhathuoclongchau.com.vn/unsafe/144x0/filters:quality(90)/https://cms-prod.s3-sgn09.fptcloud.com/00500234_hoat_huyet_truong_phuc_3x10_3439_6293_large_fce5c74dce.jpg',
  },
]

function filterProducts() {
  const keywordLower = keyword.value.toLowerCase()
  filteredProducts.value = sanPhamMau.filter((sp) => sp.ten.toLowerCase().includes(keywordLower))
}

function formatGia(gia) {
  return gia.toLocaleString('vi-VN') + '₫'
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
