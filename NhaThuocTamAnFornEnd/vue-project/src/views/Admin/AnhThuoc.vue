<template>
  <div class="container py-4">
    <h4 class="mb-4 text-success">Quản lý ảnh thuốc</h4>

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
            <tr v-for="anh in anhLoc" :key="anh.maAnhThuoc">
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
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import UploadImg from '../UploadImg.vue'

const danhSachAnh = ref([])
const maThuocFilter = ref('')
const showModal = ref(false)
const isEditing = ref(false)
const newImage = ref({ maAnhThuoc: null, maThuoc: null, hinhAnh: '', anhChinh: false })

const fetchData = async () => {
  const res = await fetch('http://localhost:8080/api/anh-thuoc')
  danhSachAnh.value = await res.json()
}

onMounted(fetchData)

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
    alert('❌ Vui lòng nhập mã thuốc!')
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
      alert('❌ Lỗi: ' + errorText)
      return
    }

    await fetchData()
    closeModal()
  } catch (err) {
    alert('❌ Lỗi không xác định khi lưu ảnh!')
  }
}

const xoaAnh = async (id) => {
  if (confirm('Bạn có chắc chắn muốn xóa?')) {
    await fetch(`http://localhost:8080/api/anh-thuoc/${id}`, { method: 'DELETE' })
    await fetchData()
  }
}

const anhLoc = computed(() => {
  return danhSachAnh.value.filter(
    (a) => !maThuocFilter.value || a.maThuoc === Number(maThuocFilter.value),
  )
})
</script>
