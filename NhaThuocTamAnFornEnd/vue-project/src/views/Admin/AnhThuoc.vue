<template>
  <div class="container py-4">
    <h4 class="mb-4 text-success">Quản lý ảnh thuốc</h4>

    <!-- Bộ lọc theo mã thuốc -->
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
        <table class="table table-bordered table-hover">
          <thead class="table-success text-center">
            <tr>
              <th>Mã thuốc</th>
              <th>ID</th>
              <th>Ảnh</th>
              <th>Hành động</th>
            </tr>
          </thead>
          <tbody class="text-center align-middle">
            <tr v-for="anh in anhLoc" :key="anh.id">
              <td>{{ anh.maThuoc }}</td>
              <td>{{ anh.id }}</td>
              <td>
                <img :src="anh.urlAnh" alt="Ảnh thuốc" width="80" height="80" class="rounded" />
              </td>
              <td>
                <button class="btn btn-sm btn-warning me-2" @click="openUploadModal(anh)">
                  Sửa
                </button>
                <button class="btn btn-sm btn-danger" @click="xoaAnh(anh.id)">Xóa</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Modal thêm/sửa ảnh -->
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
            <input type="file" accept="image/*" class="form-control" @change="uploadImage" />
            <img
              v-if="newImage.urlAnh"
              :src="newImage.urlAnh"
              class="mt-3 rounded"
              width="100"
              height="100"
            />
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
import { ref, computed } from 'vue'

const danhSachAnh = ref([
  { id: 1, maThuoc: 1, urlAnh: '' },
  { id: 2, maThuoc: 2, urlAnh: '' },
])

const maThuocFilter = ref('')
const showModal = ref(false)
const isEditing = ref(false)
const newImage = ref({ maThuoc: null, urlAnh: '', id: null })

const openUploadModal = (anh = null) => {
  if (anh) {
    isEditing.value = true
    newImage.value = { ...anh }
  } else {
    isEditing.value = false
    newImage.value = { maThuoc: null, urlAnh: '', id: null }
  }
  showModal.value = true
}

const closeModal = () => {
  showModal.value = false
}

const uploadImage = (e) => {
  const file = e.target.files[0]
  if (!file) return
  const reader = new FileReader()
  reader.onload = () => {
    newImage.value.urlAnh = reader.result
  }
  reader.readAsDataURL(file)
}

const luuAnh = () => {
  if (isEditing.value) {
    const index = danhSachAnh.value.findIndex((a) => a.id === newImage.value.id)
    if (index !== -1) {
      danhSachAnh.value[index] = { ...newImage.value }
    }
  } else {
    const newId = danhSachAnh.value.length ? Math.max(...danhSachAnh.value.map((a) => a.id)) + 1 : 1
    danhSachAnh.value.push({ ...newImage.value, id: newId })
  }
  closeModal()
}

const xoaAnh = (id) => {
  if (confirm('Bạn có chắc chắn muốn xóa ảnh này?')) {
    danhSachAnh.value = danhSachAnh.value.filter((a) => a.id !== id)
  }
}

const anhLoc = computed(() => {
  return danhSachAnh.value.filter(
    (a) => !maThuocFilter.value || a.maThuoc === Number(maThuocFilter.value),
  )
})
</script>

<style scoped>
.table td,
.table th {
  vertical-align: middle;
}
</style>
