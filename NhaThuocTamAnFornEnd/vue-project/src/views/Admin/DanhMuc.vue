<template>
  <div class="container py-4">
    <h4 class="mb-4 text-success">Quản lý danh mục thuốc</h4>

    <!-- Nút thêm danh mục -->
    <div class="mb-3">
      <button class="btn btn-success" @click="openModal()">➕ Thêm danh mục</button>
    </div>

    <!-- Bảng danh sách danh mục -->
    <div class="card shadow-sm">
      <div class="card-body p-0">
        <table class="table table-bordered table-hover mb-0">
          <thead class="table-success text-center">
            <tr>
              <th>Mã DM</th>
              <th>Tên danh mục</th>
              <th>Mô tả</th>
              <th>Số lượng sản phẩm</th>
              <th>Hành động</th>
            </tr>
          </thead>
          <tbody class="text-center align-middle">
            <tr v-for="dm in danhSachDanhMuc" :key="dm.maDM">
              <td>{{ dm.maDM }}</td>
              <td>{{ dm.tenDanhMuc }}</td>
              <td>{{ dm.moTa }}</td>
              <td>{{ demSanPham(dm.maDM) }}</td>
              <td>
                <button class="btn btn-sm btn-warning me-2" @click="openModal(dm)">Sửa</button>
                <button class="btn btn-sm btn-danger" @click="xoaDanhMuc(dm.maDM)">Xóa</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Modal thêm/sửa danh mục -->
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
  </div>
</template>

<script setup>
import { ref } from 'vue'

// Danh sách danh mục mẫu
const danhSachDanhMuc = ref([
  { maDM: 1, tenDanhMuc: 'Thuốc giảm đau', moTa: 'Các loại thuốc hỗ trợ giảm đau' },
  { maDM: 2, tenDanhMuc: 'Kháng sinh', moTa: 'Nhóm thuốc điều trị nhiễm khuẩn' },
])

// Danh sách sản phẩm mẫu
const danhSachSanPham = ref([
  { id: 1, tenSP: 'Paracetamol', maDM: 1 },
  { id: 2, tenSP: 'Ibuprofen', maDM: 1 },
  { id: 3, tenSP: 'Amoxicillin', maDM: 2 },
])

// Modal và form
const showModal = ref(false)
const form = ref({})

// Mở modal thêm/sửa
function openModal(dm = null) {
  form.value = dm ? { ...dm } : { maDM: null, tenDanhMuc: '', moTa: '' }
  showModal.value = true
}

// Đóng modal
function closeModal() {
  showModal.value = false
}

// Lưu danh mục
function luuDanhMuc() {
  if (!form.value.tenDanhMuc.trim()) {
    alert('Tên danh mục không được để trống.')
    return
  }

  if (form.value.maDM) {
    const index = danhSachDanhMuc.value.findIndex((dm) => dm.maDM === form.value.maDM)
    if (index !== -1) danhSachDanhMuc.value[index] = { ...form.value }
  } else {
    const newId = Math.max(...danhSachDanhMuc.value.map((dm) => dm.maDM), 0) + 1
    danhSachDanhMuc.value.push({ ...form.value, maDM: newId })
  }

  closeModal()
}

// Xóa danh mục nếu không còn sản phẩm
function xoaDanhMuc(id) {
  const coSanPham = danhSachSanPham.value.some((sp) => sp.maDM === id)
  if (coSanPham) {
    alert('Danh mục này đang có sản phẩm, không thể xóa.')
    return
  }

  if (confirm('Bạn có chắc chắn muốn xóa danh mục này?')) {
    danhSachDanhMuc.value = danhSachDanhMuc.value.filter((dm) => dm.maDM !== id)
  }
}

// Đếm số sản phẩm theo mã danh mục
function demSanPham(maDM) {
  return danhSachSanPham.value.filter((sp) => sp.maDM === maDM).length
}
</script>

<style scoped>
.table td,
.table th {
  vertical-align: middle;
}
</style>
