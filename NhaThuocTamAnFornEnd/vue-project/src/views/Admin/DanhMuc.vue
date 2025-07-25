<template>
  <div class="container py-4">
    <h4 class="mb-4 text-success">Quản lý danh mục thuốc</h4>

    <!-- Thanh tìm kiếm -->
    <div class="mb-3">
      <div class="input-group" style="max-width: 100%">
        <span class="input-group-text bg-white border-0">🔎</span>
        <input
          v-model="tuKhoa"
          @input="timKiemDanhMuc"
          type="text"
          class="form-control border-0 shadow-none focus-border"
          placeholder="Tìm kiếm theo tên danh mục..."
        />
        <button class="btn btn-outline-secondary" @click="xoaTimKiem">Xóa</button>
      </div>
    </div>

    <!-- Nút thêm -->
    <div class="mb-3">
      <button class="btn btn-success" @click="openModal()">➕ Thêm danh mục</button>
    </div>

    <!-- Danh sách danh mục -->
    <div class="card shadow-sm">
      <div class="card-body p-0">
        <table class="table table-bordered table-hover mb-0">
          <thead class="table-success text-center">
            <tr>
              <th>Mã DM</th>
              <th>Tên danh mục</th>
              <th>Mô tả</th>
              <th>Hành động</th>
            </tr>
          </thead>
          <tbody class="text-center align-middle">
            <tr v-for="dm in danhMucTrang" :key="dm.maDM">
              <td>{{ dm.maDM }}</td>
              <td>{{ dm.tenDanhMuc }}</td>
              <td>{{ dm.moTa }}</td>
              <td>
                <button class="btn btn-sm btn-warning me-2" @click="openModal(dm)">Sửa</button>
                <button class="btn btn-sm btn-danger" @click="xoaDanhMuc(dm.maDM)">Xóa</button>
              </td>
            </tr>
            <tr v-if="danhSachLoc.length === 0">
              <td colspan="4">Không có danh mục nào.</td>
            </tr>
          </tbody>
        </table>

        <!-- Phân trang -->
        <Pagination
          :current-page="currentPage"
          :total-pages="totalPages"
          @change-page="changePage"
        />
      </div>
    </div>

    <!-- Modal thêm/sửa -->
    <div
      class="modal fade"
      :class="{ show: showModal }"
      :style="showModal ? 'display:block' : ''"
      @click.self="closeModal"
    >
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">{{ form.maDM ? 'Sửa' : 'Thêm' }} danh mục</h5>
            <button class="btn-close" @click="closeModal"></button>
          </div>
          <div class="modal-body">
            <input
              v-model="form.tenDanhMuc"
              type="text"
              class="form-control mb-3"
              placeholder="Tên danh mục"
            />
            <textarea v-model="form.moTa" class="form-control" placeholder="Mô tả"></textarea>
          </div>
          <div class="modal-footer">
            <button class="btn btn-primary" @click="luuDanhMuc">Lưu</button>
            <button class="btn btn-secondary" @click="closeModal">Hủy</button>
          </div>
        </div>
      </div>
    </div>
    <div v-if="showModal" class="modal-backdrop fade show"></div>

    <!-- Toast -->
    <ToastMessage ref="toastRef" />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'
import ToastMessage from '../ToastMessage.vue'
import Pagination from '../Pagination.vue'

// Biến
const danhSachDanhMuc = ref([])
const tuKhoa = ref('')
const showModal = ref(false)
const form = ref({})
const toastRef = ref(null)

// Phân trang
const currentPage = ref(1)
const pageSize = 5

const danhSachLoc = computed(() => danhSachDanhMuc.value)
const totalPages = computed(() => Math.ceil(danhSachLoc.value.length / pageSize))
const danhMucTrang = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  return danhSachLoc.value.slice(start, start + pageSize)
})
const changePage = (page) => {
  currentPage.value = page
}

// Load danh mục
async function loadDanhMuc() {
  try {
    const res = await axios.get('http://localhost:8080/api/danhmuc')
    danhSachDanhMuc.value = res.data
    currentPage.value = 1
  } catch (err) {
    toastRef.value.show('❌ Lỗi khi tải danh mục.', 'error')
  }
}

// Tìm kiếm
async function timKiemDanhMuc() {
  try {
    if (!tuKhoa.value.trim()) {
      await loadDanhMuc()
      return
    }
    const res = await axios.get(
      `http://localhost:8080/api/danhmuc/search?tenDanhMuc=${tuKhoa.value}`,
    )
    danhSachDanhMuc.value = res.data
    currentPage.value = 1
  } catch (err) {
    toastRef.value.show('❌ Lỗi tìm kiếm.', 'error')
  }
}

function xoaTimKiem() {
  tuKhoa.value = ''
  loadDanhMuc()
}

// Mở modal
function openModal(dm = null) {
  form.value = dm ? { ...dm } : { maDM: null, tenDanhMuc: '', moTa: '' }
  showModal.value = true
}

function closeModal() {
  showModal.value = false
}

// Lưu
async function luuDanhMuc() {
  if (!form.value.tenDanhMuc.trim()) {
    toastRef.value.show('❌ Tên danh mục không được để trống.', 'error')
    return
  }

  try {
    if (form.value.maDM) {
      await axios.put(`http://localhost:8080/api/danhmuc/${form.value.maDM}`, form.value)
      toastRef.value.show('✅ Cập nhật danh mục thành công!', 'success')
    } else {
      await axios.post('http://localhost:8080/api/danhmuc', form.value)
      toastRef.value.show('✅ Thêm danh mục thành công!', 'success')
    }
    await loadDanhMuc()
    closeModal()
  } catch (err) {
    toastRef.value.show('❌ Lỗi khi lưu danh mục.', 'error')
  }
}

// Xóa
async function xoaDanhMuc(id) {
  if (!confirm('Bạn có chắc chắn muốn xóa danh mục này?')) return
  try {
    await axios.delete(`http://localhost:8080/api/danhmuc/${id}`)
    await loadDanhMuc()
    toastRef.value.show('🗑️ Xóa danh mục thành công.', 'success')
  } catch (err) {
    toastRef.value.show('❌ Không thể xóa danh mục.', 'error')
  }
}

onMounted(loadDanhMuc)
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
