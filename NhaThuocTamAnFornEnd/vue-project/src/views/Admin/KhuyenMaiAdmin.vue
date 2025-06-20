<template>
  <div class="container py-4">
    <h4 class="text-success mb-3">🎁 Quản Lý Khuyến Mãi</h4>

    <!-- Tìm kiếm -->
    <div class="input-group mb-3">
      <input v-model="keyword" class="form-control" placeholder="🔎 Tìm tên khuyến mãi..." />
      <button class="btn btn-outline-secondary" @click="keyword = ''">Xóa</button>
    </div>

    <!-- Thêm mới -->
    <div class="text-end mb-2">
      <button class="btn btn-success" @click="openCreateForm">➕ Thêm khuyến mãi</button>
    </div>

    <!-- Danh sách -->
    <table class="table table-bordered table-hover small align-middle">
      <thead class="table-success text-center">
        <tr>
          <th>Mã</th>
          <th>Tên KM</th>
          <th>Loại</th>
          <th>Giá trị</th>
          <th>Thời gian</th>
          <th>Mô tả</th>
          <th>Trạng thái</th>
          <th>Hành động</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="km in filteredKhuyenMai" :key="km.maKM" class="text-center">
          <td>{{ km.maKM }}</td>
          <td>{{ km.tenKM }}</td>
          <td>{{ km.loai }}</td>
          <td>{{ km.loai === 'percent' ? km.giaTri + '%' : formatCurrency(km.giaTri) }}</td>
          <td>{{ formatDate(km.ngayBatDau) }} → {{ formatDate(km.ngayKetThuc) }}</td>
          <td>{{ km.moTa || '-' }}</td>
          <td>
            <span :class="km.trangThai ? 'text-success' : 'text-danger'">
              {{ km.trangThai ? 'Áp dụng' : 'Ẩn' }}
            </span>
          </td>
          <td>
            <button class="btn btn-sm btn-warning me-1" @click="edit(km)">Sửa</button>
            <button class="btn btn-sm btn-danger me-1" @click="remove(km.maKM)">Xóa</button>
            <button class="btn btn-sm btn-outline-dark" @click="toggle(km)">
              {{ km.trangThai ? 'Ẩn' : 'Hiện' }}
            </button>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- Form Khuyến Mãi -->
    <div v-if="showForm" class="modal-overlay">
      <div class="modal-content card p-4">
        <h5 class="mb-3 text-success">{{ isEdit ? '✏️ Sửa' : '➕ Thêm' }} khuyến mãi</h5>
        <div class="row g-3">
          <div class="col-md-6">
            <label class="form-label">Tên khuyến mãi</label>
            <input v-model="form.tenKM" class="form-control" />
          </div>
          <div class="col-md-3">
            <label class="form-label">Loại</label>
            <select v-model="form.loai" class="form-select">
              <option value="percent">Phần trăm</option>
              <option value="money">Giảm trực tiếp</option>
            </select>
          </div>
          <div class="col-md-3">
            <label class="form-label">Giá trị</label>
            <input type="number" v-model="form.giaTri" class="form-control" />
          </div>

          <div class="col-md-6">
            <label class="form-label">Ngày bắt đầu</label>
            <input type="date" v-model="form.ngayBatDau" class="form-control" />
          </div>
          <div class="col-md-6">
            <label class="form-label">Ngày kết thúc</label>
            <input type="date" v-model="form.ngayKetThuc" class="form-control" />
          </div>

          <div class="col-12">
            <label class="form-label">Mô tả</label>
            <textarea v-model="form.moTa" class="form-control" rows="2"></textarea>
          </div>
        </div>

        <div class="text-end mt-3">
          <button class="btn btn-primary" @click="save">💾 Lưu</button>
          <button class="btn btn-secondary ms-2" @click="closeForm">❌ Hủy</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

const khuyenMaiList = ref(JSON.parse(localStorage.getItem('khuyenMaiList')) || [])
const keyword = ref('')
const showForm = ref(false)
const isEdit = ref(false)

const form = ref({
  maKM: null,
  tenKM: '',
  loai: 'percent',
  giaTri: 0,
  ngayBatDau: '',
  ngayKetThuc: '',
  moTa: '',
  trangThai: true,
})

const filteredKhuyenMai = computed(() =>
  khuyenMaiList.value.filter((k) => k.tenKM.toLowerCase().includes(keyword.value.toLowerCase())),
)

const formatDate = (d) => (d ? new Date(d).toLocaleDateString('vi-VN') : '-')
const formatCurrency = (v) => v.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' })

const openCreateForm = () => {
  form.value = {
    maKM: Date.now(),
    tenKM: '',
    loai: 'percent',
    giaTri: 0,
    ngayBatDau: '',
    ngayKetThuc: '',
    moTa: '',
    trangThai: true,
  }
  isEdit.value = false
  showForm.value = true
}

const edit = (km) => {
  form.value = { ...km }
  isEdit.value = true
  showForm.value = true
}

const closeForm = () => {
  showForm.value = false
}

const save = () => {
  if (!form.value.ngayBatDau || !form.value.ngayKetThuc) {
    alert('Vui lòng nhập đầy đủ thời gian khuyến mãi!')
    return
  }

  const index = khuyenMaiList.value.findIndex((k) => k.maKM === form.value.maKM)
  if (isEdit.value && index !== -1) {
    khuyenMaiList.value[index] = { ...form.value }
  } else {
    khuyenMaiList.value.push({ ...form.value })
  }
  localStorage.setItem('khuyenMaiList', JSON.stringify(khuyenMaiList.value))
  showForm.value = false
}

const remove = (id) => {
  if (confirm('Xóa khuyến mãi?')) {
    const index = khuyenMaiList.value.findIndex((k) => k.maKM === id)
    if (index !== -1) {
      khuyenMaiList.value.splice(index, 1)
      localStorage.setItem('khuyenMaiList', JSON.stringify(khuyenMaiList.value))
    }
  }
}

const toggle = (km) => {
  km.trangThai = !km.trangThai
  localStorage.setItem('khuyenMaiList', JSON.stringify(khuyenMaiList.value))
}
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.4);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 999;
}
.modal-content {
  background: white;
  border-radius: 8px;
  max-height: 90vh;
  overflow-y: auto;
  width: 700px;
}
</style>
