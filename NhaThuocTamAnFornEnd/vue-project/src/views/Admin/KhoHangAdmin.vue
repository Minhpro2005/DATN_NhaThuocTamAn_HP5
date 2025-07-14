<template>
  <div class="container py-4">
    <h4 class="mb-4 text-success fw-bold">📦 Quản lý kho hàng</h4>

    <!-- Bộ lọc -->
    <div class="row g-2 mb-3">
      <div class="col-md-6">
        <input
          v-model="keyword"
          @input="timKiem"
          type="text"
          class="form-control"
          placeholder="🔍 Tìm theo mã, tên thuốc "
        />
      </div>
      <div class="col-md-4">
        <select v-model="filterTrangThai" class="form-select" @change="timKiem">
          <option value="">-- Tất cả trạng thái --</option>
          <option value="0">✅ Bình thường</option>
          <option value="1">⚠️ Sắp hết</option>
          <option value="2">❌ Hết hàng</option>
        </select>
      </div>
      <div class="col-md-2">
        <button class="btn btn-outline-secondary w-100" @click="resetSearch">Xóa</button>
      </div>
    </div>

    <!-- Bảng kho hàng -->
    <table class="table table-bordered table-striped align-middle">
      <thead class="table-success text-center">
        <tr>
          <th>Mã</th>
          <th>Tên thuốc</th>
          <th>Số lượng tồn</th>
          <th>Trạng thái</th>
          <th>Ngày cập nhật</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="k in paginatedKhoList" :key="k.maKhoHang">
          <td class="text-center">{{ k.maKhoHang }}</td>
          <td>{{ k.tenThuoc || k.tenBienThe }}</td>
          <td class="text-end">{{ k.soLuongTon }}</td>
          <td :class="trangThaiClass(k.trangThai)">
            {{ getTrangThai(k.trangThai) }}
          </td>
          <td class="text-end">{{ formatDate(k.ngayCapNhat) }}</td>
        </tr>
        <tr v-if="paginatedKhoList.length === 0">
          <td colspan="5" class="text-center text-muted">Không có dữ liệu</td>
        </tr>
      </tbody>
    </table>

    <!-- Phân trang -->
    <nav v-if="totalPages > 1" class="mt-3">
      <ul class="pagination justify-content-center">
        <li class="page-item" :class="{ disabled: currentPage === 0 }">
          <button class="page-link" @click="changePage(currentPage - 1)">Trước</button>
        </li>
        <li
          class="page-item"
          v-for="page in totalPages"
          :key="page"
          :class="{ active: currentPage === page - 1 }"
        >
          <button class="page-link" @click="changePage(page - 1)">{{ page }}</button>
        </li>
        <li class="page-item" :class="{ disabled: currentPage === totalPages - 1 }">
          <button class="page-link" @click="changePage(currentPage + 1)">Sau</button>
        </li>
      </ul>
    </nav>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'

// State
const allKhoList = ref([])
const keyword = ref('')
const filterTrangThai = ref('')
const currentPage = ref(0)
const pageSize = 10

// Load dữ liệu
const loadKho = async () => {
  try {
    const res = await axios.get('http://localhost:8080/api/kho-hang')
    allKhoList.value = res.data
  } catch (err) {
    console.error('Lỗi khi load kho:', err)
  }
}

const timKiem = () => {
  currentPage.value = 0
}

const resetSearch = () => {
  keyword.value = ''
  filterTrangThai.value = ''
  currentPage.value = 0
}

// Lọc và sắp xếp
const filteredKhoList = computed(() => {
  let list = [...allKhoList.value]
  if (keyword.value.trim()) {
    const kw = keyword.value.toLowerCase()
    list = list.filter(
      (k) =>
        k.maKhoHang.toString().includes(kw) ||
        k.tenThuoc?.toLowerCase().includes(kw) ||
        k.tenBienThe?.toLowerCase().includes(kw),
    )
  }
  if (filterTrangThai.value !== '') {
    list = list.filter((k) => k.trangThai.toString() === filterTrangThai.value)
  }
  return list.sort((a, b) => new Date(b.ngayCapNhat) - new Date(a.ngayCapNhat))
})

// Phân trang
const totalPages = computed(() => Math.ceil(filteredKhoList.value.length / pageSize))
const paginatedKhoList = computed(() => {
  const start = currentPage.value * pageSize
  return filteredKhoList.value.slice(start, start + pageSize)
})
const changePage = (page) => {
  if (page >= 0 && page < totalPages.value) currentPage.value = page
}

// Helpers
const formatDate = (d) => {
  if (!d) return ''
  return new Date(d).toLocaleDateString('vi-VN')
}
const getTrangThai = (t) => {
  switch (t) {
    case 0:
      return '✅ Bình thường'
    case 1:
      return '⚠️ Sắp hết'
    case 2:
      return '❌ Hết hàng'
    default:
      return 'Không rõ'
  }
}
const trangThaiClass = (t) =>
  t === 2 ? 'text-danger fw-bold' : t === 1 ? 'text-warning fw-bold' : 'text-success fw-bold'

// Mounted
onMounted(() => {
  loadKho()
})
</script>

<style scoped>
.text-danger {
  color: red !important;
}
.text-warning {
  color: orange !important;
}
.text-success {
  color: green !important;
}
</style>
