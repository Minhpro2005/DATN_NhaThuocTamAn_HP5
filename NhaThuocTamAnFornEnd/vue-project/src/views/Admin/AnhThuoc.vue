<template>
  <div class="container py-4">
    <h4 class="mb-4 text-success">Quản lý ảnh thuốc</h4>

    <!-- Bộ lọc -->
    <div class="row mb-3">
      <div class="col-md-4">
        <input
          v-model="maThuocFilter"
          type="number"
          class="form-control"
          placeholder="Lọc theo mã thuốc..."
        />
      </div>
      <div class="col-md-4">
        <button class="btn btn-success" @click="openUploadModal()">➕ Thêm ảnh</button>
      </div>
    </div>

    <!-- Danh sách ảnh -->
    <div class="card shadow-sm">
      <div class="card-body">
        <table class="table table-bordered text-center">
          <thead class="table-success">
            <tr>
              <th>Mã thuốc</th>
              <th>ID</th>
              <th>Ảnh</th>
              <th>Chính</th>
              <th>Hành động</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="anh in anhTrang" :key="anh.maAnhThuoc">
              <td>{{ anh.maThuoc }}</td>
              <td>{{ anh.maAnhThuoc }}</td>
              <td><img :src="getFullImageUrl(anh.hinhAnh)" width="80" height="80" /></td>
              <td><span v-if="anh.anhChinh">✔️</span></td>
              <td>
                <button class="btn btn-warning btn-sm me-2" @click="openUploadModal(anh)">
                  Sửa
                </button>
                <button class="btn btn-danger btn-sm" @click="xoaAnh(anh.maAnhThuoc)">Xóa</button>
              </td>
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

    <!-- Modal -->
    <div
      class="modal fade"
      :class="{ show: showModal }"
      :style="showModal ? 'display:block' : ''"
      @click.self="closeModal"
    >
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">{{ isEditing ? 'Sửa' : 'Thêm' }} ảnh thuốc</h5>
            <button class="btn-close" @click="closeModal"></button>
          </div>
          <div class="modal-body">
            <input
              v-model.number="newImage.maThuoc"
              type="number"
              class="form-control mb-3"
              placeholder="Nhập mã thuốc"
            />
            <div class="form-check mb-3">
              <input
                class="form-check-input"
                type="checkbox"
                v-model="newImage.anhChinh"
                id="anhChinhCheck"
              />
              <label class="form-check-label" for="anhChinhCheck">Là ảnh chính</label>
            </div>
            <UploadImg :initial="newImage.hinhAnh" @file-selected="handleFileUpload" />
          </div>
          <div class="modal-footer">
            <button class="btn btn-primary" @click="luuAnh">Lưu</button>
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
import UploadImg from '../UploadImg.vue'
import ToastMessage from '../ToastMessage.vue'
import Pagination from '../Pagination.vue'

const danhSachAnh = ref([])
const maThuocFilter = ref('')
const showModal = ref(false)
const isEditing = ref(false)
const newImage = ref({ maAnhThuoc: null, maThuoc: null, hinhAnh: '', anhChinh: false })
const toastRef = ref(null)

// Phân trang
const currentPage = ref(1)
const pageSize = 5
const anhLoc = computed(() =>
  danhSachAnh.value.filter(
    (a) => !maThuocFilter.value || a.maThuoc === Number(maThuocFilter.value),
  ),
)
const totalPages = computed(() => Math.ceil(anhLoc.value.length / pageSize))
const anhTrang = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  return anhLoc.value.slice(start, start + pageSize)
})
const changePage = (page) => {
  currentPage.value = page
}

// Lấy dữ liệu
const fetchData = async () => {
  const res = await fetch('http://localhost:8080/api/anh-thuoc')
  danhSachAnh.value = await res.json()
}

// Xử lý ảnh
const getFullImageUrl = (path) => {
  return path ? `http://localhost:8080/${path.startsWith('/') ? path.slice(1) : path}` : ''
}

const openUploadModal = (anh = null) => {
  isEditing.value = !!anh
  newImage.value = anh
    ? { ...anh }
    : { maAnhThuoc: null, maThuoc: null, hinhAnh: '', anhChinh: false }
  showModal.value = true
}

const closeModal = () => {
  showModal.value = false
}

const handleFileUpload = async (file) => {
  const formData = new FormData()
  formData.append('file', file)
  const res = await fetch('http://localhost:8080/api/anh-thuoc/upload', {
    method: 'POST',
    body: formData,
  })
  const path = await res.text()
  newImage.value.hinhAnh = path
}

const luuAnh = async () => {
  if (!newImage.value.maThuoc) {
    toastRef.value.show('❌ Vui lòng nhập mã thuốc!', 'error')
    return
  }

  const method = isEditing.value ? 'PUT' : 'POST'
  try {
    const res = await fetch('http://localhost:8080/api/anh-thuoc', {
      method,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(newImage.value),
    })

    if (!res.ok) {
      const errorText = await res.text()
      toastRef.value.show(`❌ Lỗi: ${errorText}`, 'error')
      return
    }

    await fetchData()
    closeModal()
    toastRef.value.show('✅ Lưu ảnh thuốc thành công!', 'success')
  } catch (err) {
    toastRef.value.show('❌ Lỗi không xác định khi lưu ảnh!', 'error')
  }
}

const xoaAnh = async (id) => {
  if (confirm('Bạn có chắc chắn muốn xóa?')) {
    try {
      await fetch(`http://localhost:8080/api/anh-thuoc/${id}`, { method: 'DELETE' })
      await fetchData()
      toastRef.value.show('🗑️ Đã xóa ảnh thành công.', 'success')
    } catch (err) {
      toastRef.value.show('❌ Xóa ảnh thất bại.', 'error')
    }
  }
}

onMounted(fetchData)
</script>
