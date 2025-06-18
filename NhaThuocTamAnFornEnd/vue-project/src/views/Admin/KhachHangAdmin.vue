<template>
  <div class="container-xl mt-4">
    <h4 class="mb-4 text-success fw-bold">
      <i class="bi bi-people-fill me-2"></i> Quản lý khách hàng
    </h4>

    <!-- Bộ lọc tìm kiếm -->
    <div class="card shadow-sm mb-4 border-0">
      <div class="card-body">
        <div class="row g-3">
          <div class="col-md-3">
            <input
              v-model="filter.ten"
              type="text"
              class="form-control"
              placeholder="🔎 Họ tên..."
            />
          </div>
          <div class="col-md-3">
            <input
              v-model="filter.email"
              type="text"
              class="form-control"
              placeholder="📧 Email..."
            />
          </div>
          <div class="col-md-3">
            <input v-model="filter.sdt" type="text" class="form-control" placeholder="📞 SĐT..." />
          </div>
          <div class="col-md-3">
            <input
              v-model="filter.diaChi"
              type="text"
              class="form-control"
              placeholder="🏠 Địa chỉ..."
            />
          </div>
        </div>
      </div>
    </div>

    <!-- Bảng dữ liệu khách hàng -->
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
                :src="kh.hinhAnh || defaultAvatar"
                class="rounded-circle border"
                style="width: 48px; height: 48px; object-fit: cover"
              />
            </td>
            <td>
              <div class="d-flex justify-content-center gap-1 flex-wrap">
                <button class="btn btn-sm btn-outline-info" @click="xemLichSu(kh)">
                  🕓Lịch sử
                </button>
                <button
                  class="btn btn-sm"
                  :class="kh.trangThai ? 'btn-outline-danger' : 'btn-outline-success'"
                  @click="toggleTrangThai(kh.maKH)"
                >
                  {{ kh.trangThai ? 'Khoá' : 'Mở' }}
                </button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Phân trang -->
    <div class="d-flex justify-content-between align-items-center mt-3">
      <small class="text-muted">
        Hiển thị {{ pagedData.length }} / {{ khachHangLoc.length }} khách hàng
      </small>
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
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const router = useRouter()
const khachHangList = ref([])
const filter = ref({ ten: '', email: '', sdt: '', diaChi: '' })
const currentPage = ref(1)
const pageSize = 20
const defaultAvatar = 'https://cdn-icons-png.flaticon.com/512/149/149071.png'

// Load dữ liệu lần đầu
onMounted(() => {
  taiDanhSach()
})

// API lấy danh sách khách hàng
function taiDanhSach() {
  axios
    .get('http://localhost:8080/api/khachhang')
    .then((res) => (khachHangList.value = res.data))
    .catch((err) => console.error('Lỗi tải danh sách:', err))
}

// Tự động tìm kiếm khi filter thay đổi
watch(
  filter,
  () => {
    currentPage.value = 1
    axios
      .get('http://localhost:8080/api/khachhang/search', { params: filter.value })
      .then((res) => (khachHangList.value = res.data))
      .catch((err) => console.error('Lỗi tìm kiếm:', err))
  },
  { deep: true },
)

const khachHangLoc = computed(() => khachHangList.value)
const totalPages = computed(() => Math.ceil(khachHangLoc.value.length / pageSize))
const pagedData = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  return khachHangLoc.value.slice(start, start + pageSize)
})

// Xử lý khoá mở tài khoản
function toggleTrangThai(maKH) {
  const kh = khachHangList.value.find((k) => k.maKH === maKH)
  if (!kh) return
  const newStatus = kh.trangThai ? 0 : 1
  axios
    .put(`http://localhost:8080/api/khachhang/trang-thai/${maKH}?status=${newStatus}`)
    .then((res) => (kh.trangThai = res.data.trangThai))
    .catch((err) => console.error('Lỗi cập nhật trạng thái:', err))
}

// Xem lịch sử mua hàng theo maKH
function xemLichSu(kh) {
  router.push(`/lich-su-mua-hang/${kh.maKH}`)
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
