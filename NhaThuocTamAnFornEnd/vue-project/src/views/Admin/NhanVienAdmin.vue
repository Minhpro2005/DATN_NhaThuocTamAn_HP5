<template>
  <div class="container py-4">
    <h4 class="mb-4 text-success">📋 Quản lý nhân viên</h4>

    <!-- Ô tìm kiếm -->
    <!-- Ô tìm kiếm -->
    <div class="mb-3">
      <div class="input-group shadow-sm rounded-pill overflow-hidden">
        <span class="input-group-text bg-white border-0 px-3">🔎</span>
        <input
          v-model="keyword"
          @input="searchNhanVien"
          type="text"
          class="form-control border-0 shadow-none"
          placeholder="Tìm kiếm nhân viên..."
        />
        <!-- Nút Xóa -->
        <button class="btn btn-outline-danger rounded-pill px-4" @click="clearSearch">Xóa</button>
      </div>
    </div>

    <!-- Nút thêm mới -->
    <div class="text-end mb-3">
      <button class="btn btn-success shadow-sm px-4 py-2 rounded-pill" @click="openModal()">
        ➕ Thêm nhân viên
      </button>
    </div>

    <!-- Bảng dữ liệu -->
    <div class="table-responsive shadow-sm border rounded bg-white">
      <table class="table table-hover align-middle mb-0 text-center">
        <thead class="table-success">
          <tr>
            <th>Mã NV</th>
            <th>Họ tên</th>
            <th>Email</th>
            <th>SĐT</th>
            <th>Ảnh</th>
            <th>Vai trò</th>
            <th>Trạng thái</th>
            <th>Hành động</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="nv in nhanVienTrang" :key="nv.maNV">
            <td class="fw-bold text-success">#{{ nv.maNV }}</td>
            <td>{{ nv.hoTen }}</td>
            <td>{{ nv.email }}</td>
            <td>{{ nv.soDienThoai }}</td>
            <td>
              <img
                :src="fullImageUrl(nv.hinhAnh)"
                class="rounded-circle border shadow-sm"
                width="45"
                height="45"
                style="object-fit: cover"
              />
            </td>
            <td>
              <span :class="nv.vaiTro === 1 ? 'badge bg-primary' : 'badge bg-info'">
                {{ nv.vaiTro === 1 ? 'Admin' : 'Nhân viên' }}
              </span>
            </td>
            <td>
              <span :class="nv.trangThai ? 'badge bg-success' : 'badge bg-danger'">
                {{ nv.trangThai ? 'Hoạt động' : 'Khóa' }}
              </span>
            </td>
            <td>
              <div class="d-flex justify-content-center gap-2">
                <!-- Sửa -->
                <button
                  class="btn btn-warning btn-sm d-flex align-items-center justify-content-center action-square"
                  title="Chỉnh sửa"
                  @click="openModal(nv)"
                >
                  <i class="bi bi-pencil"></i>
                </button>

                <!-- Khóa / Mở -->
                <button
                  class="btn btn-secondary btn-sm d-flex align-items-center justify-content-center action-square"
                  :title="nv.trangThai ? 'Khóa tài khoản' : 'Mở tài khoản'"
                  @click="toggleTrangThai(nv)"
                >
                  <i :class="nv.trangThai ? 'bi bi-lock' : 'bi bi-unlock'"></i>
                </button>

                <!-- Xóa -->
                <button
                  class="btn btn-danger btn-sm d-flex align-items-center justify-content-center action-square"
                  title="Xóa"
                  @click="deleteNhanVien(nv.maNV)"
                >
                  <i class="bi bi-trash"></i>
                </button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>

      <!-- Phân trang -->
      <Pagination :current-page="currentPage" :total-pages="totalPages" @change-page="changePage" />
    </div>

    <!-- Modal thêm/sửa -->
    <div v-if="showModal" class="modal fade show d-block" style="background: rgba(0, 0, 0, 0.4)">
      <div class="modal-dialog modal-lg modal-dialog-centered">
        <div class="modal-content shadow-lg rounded-3 border-0">
          <form @submit.prevent="saveNhanVien">
            <div class="modal-header bg-success text-white">
              <h5 class="modal-title">{{ form.maNV ? 'Cập nhật' : 'Thêm' }} nhân viên</h5>
              <button
                type="button"
                class="btn-close btn-close-white"
                @click="closeModal()"
              ></button>
            </div>

            <div class="modal-body">
              <div class="row g-3">
                <div class="col-md-6">
                  <label>Họ tên</label>
                  <input v-model="form.hoTen" type="text" class="form-control" required />
                </div>
                <div class="col-md-6">
                  <label>Email</label>
                  <input v-model="form.email" type="email" class="form-control" required />
                </div>
                <div class="col-md-6">
                  <label>SĐT</label>
                  <input v-model="form.soDienThoai" type="text" class="form-control" required />
                </div>
                <div class="col-md-6">
                  <label>CCCD</label>
                  <input v-model="form.cccd" type="text" class="form-control" />
                </div>
                <div class="col-md-6">
                  <label>Ngày sinh</label>
                  <input v-model="form.ngaySinh" type="date" class="form-control" />
                </div>
                <div class="col-md-6">
                  <label>Địa chỉ</label>
                  <input v-model="form.diaChi" type="text" class="form-control" />
                </div>
                <div class="col-md-3">
                  <label>Giới tính</label>
                  <select v-model="form.gioiTinh" class="form-select">
                    <option :value="true">Nam</option>
                    <option :value="false">Nữ</option>
                  </select>
                </div>
                <div class="col-md-3">
                  <label>Vai trò</label>
                  <select v-model="form.vaiTro" class="form-select">
                    <option value="1">Admin</option>
                    <option value="2">Nhân viên</option>
                  </select>
                </div>
                <div class="col-md-6">
                  <label>Ảnh đại diện</label>
                  <input type="file" class="form-control" @change="handleFileChange" />
                  <div v-if="previewUrl" class="mt-2">
                    <img
                      :src="previewUrl"
                      width="80"
                      height="80"
                      style="object-fit: cover"
                      class="rounded border"
                    />
                  </div>
                </div>
              </div>
            </div>

            <div class="modal-footer">
              <button class="btn btn-secondary" @click="closeModal()">Hủy</button>
              <button class="btn btn-success px-4" type="submit">💾 Lưu</button>
            </div>
          </form>
        </div>
      </div>
    </div>

    <!-- Toast thông báo -->
    <ToastMessage ref="toastRef" />
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import axios from 'axios'
import ToastMessage from '../ToastMessage.vue'
import Pagination from '../Pagination.vue'

const serverUrl = 'http://localhost:8080/api/nhanvien'
const nhanVienList = ref([])
const keyword = ref('')
const showModal = ref(false)
const form = ref({})
const fileUpload = ref(null)
const previewUrl = ref(null)
const toastRef = ref(null)

// Phân trang
const currentPage = ref(1)
const pageSize = 5
const totalPages = computed(() => Math.ceil(nhanVienList.value.length / pageSize))
const nhanVienTrang = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  return nhanVienList.value.slice(start, start + pageSize)
})
const changePage = (page) => {
  currentPage.value = page
}

// Load danh sách
onMounted(loadNhanVien)

function fullImageUrl(path) {
  if (!path) return 'https://cdn-icons-png.flaticon.com/512/149/149071.png'
  return `http://localhost:8080${path}`
}

function loadNhanVien() {
  axios.get(serverUrl).then((res) => {
    nhanVienList.value = res.data
    currentPage.value = 1
  })
}

function searchNhanVien() {
  axios
    .get(`${serverUrl}/search?keyword=${keyword.value}`)
    .then((res) => {
      nhanVienList.value = res.data
      currentPage.value = 1
    })
    .catch(() => toastRef.value.show('❌ Lỗi tìm kiếm!', 'error'))
}

function clearSearch() {
  keyword.value = ''
  loadNhanVien()
}

function openModal(nv = null) {
  form.value = nv
    ? { ...nv }
    : {
        maNV: null,
        hoTen: '',
        email: '',
        soDienThoai: '',
        gioiTinh: true,
        vaiTro: 2,
        hinhAnh: '',
        cccd: '',
        ngaySinh: null,
        diaChi: '',
        trangThai: true,
      }
  previewUrl.value = nv?.hinhAnh ? fullImageUrl(nv.hinhAnh) : null
  fileUpload.value = null
  showModal.value = true
}

function closeModal() {
  showModal.value = false
}

async function saveNhanVien() {
  try {
    let res = form.value.maNV
      ? await axios.put(`${serverUrl}/${form.value.maNV}`, form.value)
      : await axios.post(serverUrl, form.value)

    if (!form.value.maNV) form.value.maNV = res.data.maNV

    if (fileUpload.value) {
      const formData = new FormData()
      formData.append('file', fileUpload.value)
      await axios.post(`${serverUrl}/${form.value.maNV}/avatar`, formData, {
        headers: { 'Content-Type': 'multipart/form-data' },
      })
    }

    toastRef.value.show('✅ Lưu nhân viên thành công!', 'success')
    loadNhanVien()
    closeModal()
  } catch (err) {
    toastRef.value.show('❌ Lỗi khi lưu nhân viên!', 'error')
  }
}

function handleFileChange(e) {
  fileUpload.value = e.target.files[0]
  previewUrl.value = URL.createObjectURL(fileUpload.value)
}

function deleteNhanVien(id) {
  if (!confirm('Xóa nhân viên này?')) return
  axios
    .delete(`${serverUrl}/${id}`)
    .then(() => {
      loadNhanVien()
      toastRef.value.show('🗑️ Đã xóa nhân viên.', 'success')
    })
    .catch(() => toastRef.value.show('❌ Lỗi khi xóa nhân viên!', 'error'))
}

function toggleTrangThai(nv) {
  const newStatus = !nv.trangThai
  axios
    .put(`${serverUrl}/${nv.maNV}/trang-thai?status=${newStatus}`)
    .then(() => {
      loadNhanVien()
      toastRef.value.show('✅ Đã cập nhật trạng thái.', 'success')
    })
    .catch(() => toastRef.value.show('❌ Lỗi cập nhật trạng thái!', 'error'))
}
</script>

<style scoped>
.table td,
.table th {
  vertical-align: middle;
}
.focus-border:focus {
  border: 1px solid #198754 !important;
  box-shadow: none !important;
  outline: none;
}
.action-square {
  width: 36px;
  height: 36px;
  border-radius: 6px; /* Bo nhẹ */
  font-size: 1rem;
  transition: all 0.2s ease-in-out;
}

.action-square:hover {
  transform: scale(1.05);
  opacity: 0.9;
}
</style>
