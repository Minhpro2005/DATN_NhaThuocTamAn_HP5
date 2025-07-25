<template>
  <div class="container mt-4">
    <h4 class="mb-4 text-success fw-bold">Quản lý Biến Thể Thuốc</h4>

    <!-- Bộ lọc -->
    <div class="row mb-3 g-2">
      <div class="col-md-4">
        <input
          v-model="maThuocFilter"
          type="number"
          class="form-control"
          placeholder="Lọc theo mã thuốc..."
        />
      </div>
    </div>

    <!-- Nút thêm -->
    <div class="mb-3">
      <button class="btn btn-success" @click="openModal()">➕ Thêm biến thể</button>
    </div>

    <!-- Danh sách -->
    <div class="card shadow-sm">
      <div class="card-body p-0">
        <table class="table table-bordered table-hover text-center">
          <thead class="table-success">
            <tr>
              <th>Mã thuốc</th>
              <th>Mã biến thể</th>
              <th>Tên biến thể</th>
              <th>Giá bán</th>
              <th>Đơn vị</th>
              <th>Trạng thái</th>
              <th>Ảnh</th>
              <th>Hành động</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="bt in bienTheTrang" :key="bt.maBienThe">
              <td>{{ bt.maThuoc }}</td>
              <td>{{ bt.maBienThe }}</td>
              <td>{{ bt.tenBienThe }}</td>
              <td>{{ formatCurrency(bt.giaBan) }}</td>
              <td>{{ bt.donViTinh }}</td>
              <td :class="bt.trangThai ? 'text-success' : 'text-danger'">
                {{ bt.trangThai ? 'Hoạt động' : 'Ngừng bán' }}
              </td>
              <td>
                <img
                  v-if="bt.hinhAnh"
                  :src="getImageUrl(bt.hinhAnh)"
                  alt="ảnh"
                  width="60"
                  height="60"
                  class="rounded"
                />
              </td>
              <td>
                <button class="btn btn-sm btn-warning me-2" @click="openModal(bt)">Sửa</button>
                <button class="btn btn-sm btn-danger" @click="xoaBienThe(bt.maBienThe)">Xóa</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Modal -->
    <template v-if="showModal">
      <div class="modal-backdrop fade show"></div>
      <div class="modal fade show d-block" @click.self="closeModal">
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title">{{ form.maBienThe ? 'Sửa' : 'Thêm' }} Biến Thể</h5>
              <button class="btn-close" @click="closeModal"></button>
            </div>
            <div class="modal-body">
              <input
                v-model="form.maThuoc"
                type="number"
                class="form-control mb-3"
                placeholder="Mã thuốc"
              />
              <input
                v-model="form.tenBienThe"
                type="text"
                class="form-control mb-3"
                placeholder="Tên biến thể"
              />
              <input
                v-model="form.giaBan"
                type="number"
                class="form-control mb-3"
                placeholder="Giá bán"
              />
              <input
                v-model="form.donViTinh"
                type="text"
                class="form-control mb-3"
                placeholder="Đơn vị tính"
              />
              <textarea
                v-model="form.moTa"
                class="form-control mb-3"
                placeholder="Mô tả biến thể"
              ></textarea>
              <select v-model="form.trangThai" class="form-select mb-3">
                <option :value="true">Hoạt động</option>
                <option :value="false">Ngừng bán</option>
              </select>
              <input
                type="file"
                accept="image/*"
                class="form-control mb-3"
                @change="onFileChange"
              />
              <img
                v-if="form.hinhAnh"
                :src="getImageUrl(form.hinhAnh)"
                width="100"
                class="rounded border"
              />
            </div>
            <div class="modal-footer">
              <button class="btn btn-primary" @click="luuBienThe">Lưu</button>
              <button class="btn btn-secondary" @click="closeModal">Hủy</button>
            </div>
          </div>
        </div>
      </div>
    </template>
    <!-- Phân trang -->
    <Pagination :current-page="currentPage" :total-pages="totalPages" @change-page="changePage" />

    <!-- Toast -->
    <ToastMessage ref="toastRef" />
  </div>
</template>

<script setup>
import axios from 'axios'
import { ref, onMounted, computed } from 'vue'
import ToastMessage from '../ToastMessage.vue'
import Pagination from '../Pagination.vue'

const bienTheList = ref([])
const form = ref({})
const showModal = ref(false)
const maThuocFilter = ref('')
const fileAnh = ref(null)
const toastRef = ref(null)

const currentPage = ref(1)
const pageSize = 5 // Số dòng trên mỗi trang
const totalPages = computed(() => Math.ceil(bienTheLoc.value.length / pageSize))

const bienTheTrang = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  return bienTheLoc.value.slice(start, start + pageSize)
})

function changePage(page) {
  currentPage.value = page
}

onMounted(fetchData)

async function fetchData() {
  try {
    const res = await axios.get('http://localhost:8080/api/bienthe')
    bienTheList.value = Array.isArray(res.data) ? res.data : []
  } catch (err) {
    console.error('Lỗi fetch biến thể:', err)
    toastRef.value.show('❌ Lỗi khi tải danh sách biến thể.', 'error')
  }
}

function getImageUrl(path) {
  if (!path) return ''
  return `http://localhost:8080${path.startsWith('/') ? path : '/' + path}`
}

function openModal(bt = null) {
  form.value = bt
    ? { ...bt }
    : {
        maThuoc: '',
        tenBienThe: '',
        giaBan: '',
        donViTinh: '',
        moTa: '',
        trangThai: true,
        hinhAnh: '',
        maBienThe: null,
      }
  fileAnh.value = null
  showModal.value = true
}

function closeModal() {
  showModal.value = false
}

function onFileChange(e) {
  fileAnh.value = e.target.files[0]
}

async function luuBienThe() {
  try {
    const formData = new FormData()
    formData.append('data', new Blob([JSON.stringify(form.value)], { type: 'application/json' }))
    if (fileAnh.value) formData.append('file', fileAnh.value)

    if (form.value.maBienThe) {
      await axios.put(`http://localhost:8080/api/bienthe/${form.value.maBienThe}`, formData, {
        headers: { 'Content-Type': 'multipart/form-data' },
      })
      toastRef.value.show('✅ Cập nhật biến thể thành công!', 'success')
    } else {
      await axios.post('http://localhost:8080/api/bienthe', formData, {
        headers: { 'Content-Type': 'multipart/form-data' },
      })
      toastRef.value.show('✅ Thêm biến thể thành công!', 'success')
    }

    await fetchData()
    closeModal()
  } catch (err) {
    console.error('Lỗi lưu:', err)
    toastRef.value.show('❌ Lỗi khi lưu biến thể thuốc.', 'error')
  }
}

async function xoaBienThe(id) {
  if (confirm('Bạn có chắc muốn xóa biến thể này?')) {
    try {
      await axios.delete(`http://localhost:8080/api/bienthe/${id}`)
      await fetchData()
      toastRef.value.show('🗑️ Đã xóa biến thể thành công.', 'success')
    } catch (err) {
      console.error('Lỗi xóa:', err)
      toastRef.value.show('❌ Xóa biến thể thất bại.', 'error')
    }
  }
}

const bienTheLoc = computed(() => {
  return bienTheList.value.filter((bt) => !maThuocFilter.value || bt.maThuoc == maThuocFilter.value)
})

function formatCurrency(val) {
  return Number(val).toLocaleString('vi-VN') + '₫'
}
</script>

<style scoped>
.table td,
.table th {
  vertical-align: middle;
}
</style>
