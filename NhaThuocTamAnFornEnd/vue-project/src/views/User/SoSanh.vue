<template>
  <div>
    <Header />
    <Navbar />
  </div>

  <div class="container py-4">
    <h4 class="mb-4 text-success"><i class="bi bi-bar-chart-line me-2"></i>So sánh sản phẩm</h4>

    <div v-if="list.length === 0" class="alert alert-info">Chưa có sản phẩm nào để so sánh.</div>

    <div v-else>
      <!-- 🔹 Thông tin chung -->
      <div class="d-flex justify-content-between align-items-center mb-2">
        <h5 class="text-primary">Thông tin chung</h5>
        <button class="btn btn-outline-danger btn-sm" @click="xoaTatCa">Xóa tất cả</button>
      </div>

      <div class="table-responsive mb-4">
        <table class="table table-bordered text-center align-middle">
          <thead class="table-light">
            <tr>
              <th>Hình ảnh</th>
              <th v-for="sp in list" :key="sp.maThuoc">
                <img :src="getImageUrl(sp.hinhAnh)" alt="img" height="80" />
              </th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <th>Tên thuốc</th>
              <td v-for="sp in list">{{ sp.tenThuoc }}</td>
            </tr>
            <tr>
              <th>Giá bán</th>
              <td v-for="sp in list">{{ formatCurrency(sp.giaBan) }}</td>
            </tr>
            <tr>
              <th>Đơn vị tính</th>
              <td v-for="sp in list">{{ sp.donViTinh }}</td>
            </tr>
            <tr>
              <th>Loại</th>
              <td v-for="sp in list">{{ sp.loai }}</td>
            </tr>
            <tr>
              <th>Xóa</th>
              <td v-for="(sp, i) in list">
                <button class="btn btn-sm btn-danger" @click="xoa(i)">X</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- 🔹 Mô tả chuyên sâu (dạng bảng) -->
      <h5 class="mb-3 text-primary">Mô tả chuyên sâu</h5>
      <div class="table-responsive">
        <table class="table table-bordered text-center align-middle">
          <thead class="table-light">
            <tr>
              <th>Thông tin</th>
              <th v-for="sp in list" :key="sp.maThuoc">{{ sp.tenThuoc }}</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <th>Mô tả chi tiết</th>
              <td v-for="sp in list" v-html="sp.moTaThem || 'Đang cập nhật...'"></td>
            </tr>
            <tr>
              <th>Thành phần</th>
              <td v-for="sp in list" v-html="sp.thanhPhan || 'Đang cập nhật...'"></td>
            </tr>
            <tr>
              <th>Công dụng</th>
              <td v-for="sp in list" v-html="sp.congDung || 'Đang cập nhật...'"></td>
            </tr>
            <tr>
              <th>Cách dùng</th>
              <td v-for="sp in list" v-html="sp.huongDanSuDung || 'Đang cập nhật...'"></td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
  <Footer />
</template>

<script setup>
import { ref, onMounted } from 'vue'
import Header from './Header.vue'
import Navbar from './Navbar.vue'
import Footer from './Footer.vue'
const list = ref([])

onMounted(() => {
  list.value = JSON.parse(localStorage.getItem('compare_list')) || []
})

function getImageUrl(path) {
  return path?.startsWith('http') ? path : `http://localhost:8080/${path}`
}

function formatCurrency(v) {
  return v?.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' }) || ''
}

function xoa(index) {
  list.value.splice(index, 1)
  localStorage.setItem('compare_list', JSON.stringify(list.value))
}

function xoaTatCa() {
  list.value = []
  localStorage.removeItem('compare_list')
}
</script>
