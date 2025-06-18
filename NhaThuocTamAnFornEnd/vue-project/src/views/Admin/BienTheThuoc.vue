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

    <!-- Đưa nút thêm xuống đây -->
    <div class="mb-3">
      <button class="btn btn-success" @click="openModal()">➕ Thêm biến thể</button>
    </div>

    <!-- Danh sách biến thể -->
    <div class="card shadow-sm">
      <div class="card-body p-0">
        <table class="table table-bordered table-hover text-center">
          <thead class="table-success">
            <tr>
              <th>Mã thuốc</th>
              <th>Mã biến thể</th>
              <th>Tên biến thể</th>
              <th>Giá bán</th>
              <th>Ảnh</th>
              <th>Hành động</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="bt in bienTheLoc" :key="bt.maBienThe">
              <td>{{ bt.maThuoc }}</td>
              <td>{{ bt.maBienThe }}</td>
              <td>{{ bt.tenBienThe }}</td>
              <td>{{ formatCurrency(bt.giaBan) }}</td>
              <td>
                <img
                  v-if="bt.hinhAnh"
                  :src="bt.hinhAnh"
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

    <!-- Modal thêm/sửa -->
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
                v-model.number="form.maThuoc"
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
                v-model.number="form.giaBan"
                type="number"
                class="form-control mb-3"
                placeholder="Giá bán"
              />
              <input type="file" accept="image/*" class="form-control mb-3" @change="uploadImage" />
              <img
                v-if="form.hinhAnh"
                :src="form.hinhAnh"
                width="100"
                height="100"
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
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

// Dữ liệu mẫu ban đầu
const bienTheList = ref([
  { maBienThe: 1, maThuoc: 1, tenBienThe: 'Vỉ 10 viên', giaBan: 25000, hinhAnh: '' },
])

const maThuocFilter = ref('')
const showModal = ref(false)
const form = ref({})

// Mở modal
function openModal(bt = null) {
  form.value = bt
    ? { ...bt }
    : { maBienThe: null, maThuoc: null, tenBienThe: '', giaBan: 0, hinhAnh: '' }
  showModal.value = true
}

// Đóng modal
function closeModal() {
  showModal.value = false
}

// Lưu biến thể (Thêm / Sửa)
function luuBienThe() {
  if (form.value.maBienThe) {
    const index = bienTheList.value.findIndex((bt) => bt.maBienThe === form.value.maBienThe)
    bienTheList.value[index] = { ...form.value }
  } else {
    const newID = Math.max(...bienTheList.value.map((bt) => bt.maBienThe), 0) + 1
    bienTheList.value.push({ ...form.value, maBienThe: newID })
  }
  closeModal()
}

// Xóa biến thể
function xoaBienThe(id) {
  if (confirm('Xóa biến thể này?'))
    bienTheList.value = bienTheList.value.filter((bt) => bt.maBienThe !== id)
}

// Upload ảnh
function uploadImage(e) {
  const file = e.target.files[0]
  if (!file) return
  const reader = new FileReader()
  reader.onload = () => {
    form.value.hinhAnh = reader.result
  }
  reader.readAsDataURL(file)
}

// Lọc dữ liệu theo mã thuốc
const bienTheLoc = computed(() => {
  return bienTheList.value.filter(
    (bt) => !maThuocFilter.value || bt.maThuoc === Number(maThuocFilter.value),
  )
})

// Định dạng tiền
const formatCurrency = (val) => val.toLocaleString('vi-VN') + '₫'
</script>

<style scoped>
.table td,
.table th {
  vertical-align: middle;
}
</style>
