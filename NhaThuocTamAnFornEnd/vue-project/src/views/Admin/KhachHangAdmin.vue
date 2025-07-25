<template>
  <div class="container-xl mt-4">
    <h4 class="mb-4 text-success fw-bold">
      <i class="bi bi-people-fill me-2"></i> Quản lý khách hàng
    </h4>

    <!-- Thanh tìm kiếm -->
    <div class="mb-4">
      <div class="input-group w-100">
        <span class="input-group-text bg-white border-0">🔎</span>
        <input
          v-model="keyword"
          type="text"
          class="form-control border-0 shadow-none focus-border"
          placeholder="Tìm kiếm khách hàng theo tên, email, SĐT..."
        />
        <button class="btn btn-outline-secondary" @click="clearSearch">Xóa</button>
      </div>
    </div>

    <!-- Bảng dữ liệu -->
    <div class="table-responsive rounded shadow-sm border">
      <table class="table table-bordered table-hover text-center bg-white mb-0">
        <thead class="table-success">
          <tr>
            <th>Mã</th>
            <th>Họ tên</th>
            <th>Email</th>
            <th>SĐT</th>
            <th>Địa chỉ</th>
            <th>Trạng thái</th>
            <th>Xác thực</th>
            <th>Ảnh đại diện</th>
            <th>Hành động</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="kh in pagedData" :key="kh.maKH">
            <td>{{ kh.maKH }}</td>
            <td class="text-start">{{ kh.hoTen }}</td>
            <td>{{ kh.email }}</td>
            <td>{{ kh.soDienThoai }}</td>
            <td class="text-start">{{ kh.diaChi }}</td>
            <td>
              <span class="badge" :class="kh.trangThai ? 'bg-success' : 'bg-secondary'">
                {{ kh.trangThai ? 'Hoạt động' : 'Đã khóa' }}
              </span>
            </td>
            <td>
              <i
                :class="
                  kh.daXacThuc
                    ? 'bi bi-check-circle-fill text-success'
                    : 'bi bi-x-circle-fill text-danger'
                "
              ></i>
            </td>
            <td>
              <img
                :src="getFullImageUrl(kh.hinhAnh)"
                class="rounded-circle border"
                style="width: 48px; height: 48px; object-fit: cover"
              />
            </td>
            <td>
              <button
                class="btn btn-sm"
                :class="kh.trangThai ? 'btn-outline-danger' : 'btn-outline-success'"
                @click="toggleTrangThai(kh.maKH)"
              >
                {{ kh.trangThai ? 'Khoá' : 'Mở' }}
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Phân trang -->
    <div class="d-flex justify-content-between align-items-center mt-3">
      <small class="text-muted"
        >Hiển thị {{ pagedData.length }} / {{ khachHangLoc.length }} khách hàng</small
      >
      <ul class="pagination pagination-sm mb-0">
        <li class="page-item" :class="{ disabled: currentPage === 1 }">
          <button class="page-link" @click="currentPage--">«</button>
        </li>
        <li
          class="page-item"
          v-for="page in totalPages"
          :key="page"
          :class="{ active: page === currentPage }"
        >
          <button class="page-link" @click="currentPage = page">{{ page }}</button>
        </li>
        <li class="page-item" :class="{ disabled: currentPage === totalPages }">
          <button class="page-link" @click="currentPage++">»</button>
        </li>
      </ul>
    </div>

    <!-- Toast -->
    <ToastMessage ref="toastRef" />
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue'
import axios from 'axios'
import ToastMessage from '../ToastMessage.vue'

const serverUrl = 'http://localhost:8080'
const defaultAvatar = 'https://cdn-icons-png.flaticon.com/512/149/149071.png'

const khachHangList = ref([])
const keyword = ref('')
const currentPage = ref(1)
const pageSize = 20
let searchTimeout = null
const toastRef = ref(null)

onMounted(() => {
  taiDanhSach()
})

function taiDanhSach() {
  axios
    .get(`${serverUrl}/api/khachhang`)
    .then((res) => {
      khachHangList.value = res.data
      currentPage.value = 1
    })
    .catch(() => toastRef.value.show('❌ Lỗi khi tải danh sách khách hàng!', 'error'))
}

watch(keyword, () => {
  currentPage.value = 1
  if (searchTimeout) clearTimeout(searchTimeout)

  searchTimeout = setTimeout(() => {
    axios
      .get(`${serverUrl}/api/khachhang/search`, {
        params: {
          hoTen: keyword.value,
          email: null,
          sdt: null,
          diaChi: null,
        },
      })
      .then((res) => {
        khachHangList.value = res.data
        currentPage.value = 1
      })
      .catch(() => toastRef.value.show('❌ Lỗi tìm kiếm!', 'error'))
  }, 400)
})

function clearSearch() {
  keyword.value = ''
  taiDanhSach()
}

function getFullImageUrl(path) {
  return path ? `${serverUrl}${path}` : defaultAvatar
}

const khachHangLoc = computed(() => khachHangList.value)
const totalPages = computed(() => Math.ceil(khachHangLoc.value.length / pageSize))
const pagedData = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  return khachHangLoc.value.slice(start, start + pageSize)
})

function toggleTrangThai(maKH) {
  const kh = khachHangList.value.find((k) => k.maKH === maKH)
  if (!kh) return
  const newStatus = kh.trangThai ? 0 : 1
  axios
    .put(`${serverUrl}/api/khachhang/trang-thai/${maKH}?status=${newStatus}`)
    .then((res) => {
      kh.trangThai = res.data.trangThai
      toastRef.value.show('✅ Cập nhật trạng thái thành công!', 'success')
    })
    .catch(() => toastRef.value.show('❌ Lỗi cập nhật trạng thái!', 'error'))
}
</script>

<style scoped>
.table-responsive {
  overflow-x: auto;
}
.table td,
.table th {
  vertical-align: middle;
  white-space: nowrap;
  text-overflow: ellipsis;
  overflow: hidden;
}
.input-group-text {
  background-color: #f8f9fa;
  border-radius: 0.375rem 0 0 0.375rem;
  font-size: 1.1rem;
}
input::placeholder {
  font-style: italic;
  color: #999;
}
.focus-border:focus {
  border: 1px solid #198754 !important;
  box-shadow: none !important;
  outline: none;
}
.btn-sm {
  font-size: 13px;
  padding: 3px 8px;
}
@media (max-width: 768px) {
  .table th:nth-child(8),
  .table td:nth-child(8) {
    display: none;
  }
}
</style>
