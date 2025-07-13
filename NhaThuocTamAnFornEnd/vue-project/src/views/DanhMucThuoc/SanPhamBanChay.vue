<template>
  <section>
    <div class="container">
      <h2 class="section-title d-flex align-items-center">
        <img
          src="https://cdn.nhathuoclongchau.com.vn/unsafe/24x0/filters:quality(90)/https://cms-prod.s3-sgn09.fptcloud.com/smalls/tpcn_vitamin_khoang_chat_level_2_91b99b5a64.png"
          alt="Vitamin Icon"
          width="24"
          height="24"
          class="me-2"
        />
        Vitamin và Khoáng chất
      </h2>

      <div class="row">
        <div class="col-2-4 mb-4" v-for="item in products" :key="item.maThuoc">
          <router-link :to="`/chi-tiet/${item.maThuoc}`" class="text-decoration-none">
            <div class="card h-100">
              <img
                :src="getFullImage(item.hinhAnhChinh)"
                class="card-img-top"
                :alt="item.tenThuoc"
              />
              <div class="card-body">
                <h5 class="card-title">{{ item.tenThuoc }}</h5>
                <p class="card-text text-success fw-bold">{{ formatCurrency(item.giaBan) }}</p>
                <button class="btn btn-green w-100">Mua ngay</button>
              </div>
            </div>
          </router-link>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const products = ref([])

function getFullImage(path) {
  if (!path) return 'https://via.placeholder.com/150'
  return `http://localhost:8080${path.startsWith('/') ? path : '/' + path}`
}

function formatCurrency(value) {
  return Number(value).toLocaleString('vi-VN') + 'đ'
}

async function fetchSanPhamTheoDanhMuc(maDM) {
  try {
    const res = await fetch(`http://localhost:8080/api/thuoc/search?maDM=${maDM}`)
    const data = await res.json()
    products.value = data.slice(0, 10) // Lấy 10 sản phẩm đầu tiên
  } catch (err) {
    console.error('Lỗi khi lấy danh sách thuốc:', err)
  }
}

onMounted(() => {
  fetchSanPhamTheoDanhMuc(1) // Thay số 1 bằng mã danh mục mong muốn
})
</script>

<style scoped>
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
  box-shadow: 0 6px 24px rgba(0, 100, 0, 0.4);
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
.section-title {
  margin-top: 20px;
  margin-bottom: 20px;
  font-weight: bold;
}

/* Tên sản phẩm: cắt sau 2 dòng có ... */
.card-title {
  font-size: 1rem;
  font-weight: 500;
  margin-bottom: 12px;
  display: -webkit-box;
  -webkit-line-clamp: 2; /* chỉ 2 dòng */
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: normal;
  line-height: 1.6;
  height: calc(1.6em * 2); /* đúng chiều cao 2 dòng */
}
</style>
