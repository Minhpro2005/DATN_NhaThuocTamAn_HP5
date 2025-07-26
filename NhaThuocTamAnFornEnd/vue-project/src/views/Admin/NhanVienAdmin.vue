<template>
  <div class="container py-4">
    <h4 class="mb-4 text-success">📋 Quản lý nhân viên</h4>

    <!-- Tìm kiếm -->
    <div class="mb-3">
      <div class="input-group w-100">
        <span class="input-group-text bg-white border-0">🔎</span>
        <input
          v-model="keyword"
          @input="searchNhanVien"
          type="text"
          class="form-control border-0 shadow-none focus-border"
          placeholder="Tìm kiếm nhân viên..."
        />
        <button class="btn btn-outline-secondary" @click="clearSearch">Xóa</button>
      </div>
    </div>

    <!-- Thêm mới -->
    <div class="text-end mb-3">
      <button class="btn btn-success" @click="openModal()">➕ Thêm nhân viên</button>
    </div>

    <!-- Bảng dữ liệu -->
    <div class="table-responsive shadow-sm border rounded bg-white">
      <table class="table table-bordered table-hover text-center mb-0">
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
            <td>{{ nv.maNV }}</td>
            <td>{{ nv.hoTen }}</td>
            <td>{{ nv.email }}</td>
            <td>{{ nv.soDienThoai }}</td>
            <td>
              <img
                :src="fullImageUrl(nv.hinhAnh)"
                class="rounded-circle border"
                width="50"
                height="50"
                style="object-fit: cover"
              />
            </td>
            <td>
              <span class="badge bg-primary">{{ nv.vaiTro === 1 ? 'Admin' : 'Nhân viên' }}</span>
            </td>
            <td>
              <span :class="nv.trangThai ? 'text-success' : 'text-danger'">{{
                nv.trangThai ? 'Hoạt động' : 'Khóa'
              }}</span>
            </td>
            <td>
              <button class="btn btn-warning btn-sm me-1" @click="openModal(nv)">✏️</button>
              <button
                class="btn btn-sm me-1"
                :class="nv.trangThai ? 'btn-outline-danger' : 'btn-outline-success'"
                @click="toggleTrangThai(nv)"
              >
                {{ nv.trangThai ? 'Khoá' : 'Mở' }}
              </button>
              <button class="btn btn-danger btn-sm" @click="deleteNhanVien(nv.maNV)">🗑️</button>
            </td>
          </tr>
        </tbody>
      </table>

      <!-- Phân trang -->
      <Pagination :current-page="currentPage" :total-pages="totalPages" @change-page="changePage" />
    </div>

    <!-- Modal thêm sửa -->
    <div v-if="showModal" class="modal fade show d-block" style="background: rgba(0, 0, 0, 0.5)">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <form @submit.prevent="saveNhanVien">
            <div class="modal-header">
              <h5 class="modal-title text-success">
                {{ form.maNV ? 'Cập nhật' : 'Thêm' }} nhân viên
              </h5>
              <button type="button" class="btn-close" @click="closeModal()"></button>
            </div>

            <div class="modal-body">
              <div class="row">
                <div class="col-md-6 mb-2">
                  <label>Họ tên</label>
                  <input v-model="form.hoTen" type="text" class="form-control" required />
                </div>
                <div class="col-md-6 mb-2">
                  <label>Email</label>
                  <input v-model="form.email" type="email" class="form-control" required />
                </div>
                <div class="col-md-6 mb-2">
                  <label>SĐT</label>
                  <input v-model="form.soDienThoai" type="text" class="form-control" required />
                </div>
                <div class="col-md-6 mb-2">
                  <label>CCCD</label>
                  <input v-model="form.cccd" type="text" class="form-control" />
                </div>
                <div class="col-md-6 mb-2">
                  <label>Ngày sinh</label>
                  <input v-model="form.ngaySinh" type="date" class="form-control" />
                </div>
                <div class="col-md-6 mb-2">
                  <label>Địa chỉ</label>
                  <input v-model="form.diaChi" type="text" class="form-control" />
                </div>
                <div class="col-md-3 mb-2">
                  <label>Giới tính</label>
                  <select v-model="form.gioiTinh" class="form-select">
                    <option :value="true">Nam</option>
                    <option :value="false">Nữ</option>
                  </select>
                </div>
                <div class="col-md-3 mb-2">
                  <label>Vai trò</label>
                  <select v-model="form.vaiTro" class="form-select">
                    <option value="1">Admin</option>
                    <option value="2">Nhân viên</option>
                  </select>
                </div>
                <div class="col-md-6 mb-2">
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
              <button class="btn btn-success" type="submit">Lưu</button>
            </div>
          </form>
        </div>
      </div>
    </div>

    <!-- Toast -->
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
</style>
