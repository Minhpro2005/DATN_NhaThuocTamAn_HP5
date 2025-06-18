<template>
  <div class="container py-4">
    <h4 class="mb-4 text-success">📋 Quản lý nhân viên</h4>

    <!-- Bộ lọc -->
    <div class="card shadow-sm mb-4">
      <div class="card-body">
        <div class="row g-3">
          <div class="col-md-3">
            <input
              v-model="filter.hoTen"
              type="text"
              class="form-control"
              placeholder="Lọc theo họ tên..."
            />
          </div>
          <div class="col-md-3">
            <input
              v-model="filter.soDienThoai"
              type="text"
              class="form-control"
              placeholder="Lọc theo SĐT..."
            />
          </div>
          <div class="col-md-3">
            <select v-model="filter.vaiTro" class="form-select">
              <option value="">Vai trò</option>
              <option value="1">Admin</option>
              <option value="2">Nhân viên</option>
            </select>
          </div>
          <div class="col-md-3">
            <select v-model="filter.gioiTinh" class="form-select">
              <option value="">Giới tính</option>
              <option value="1">Nam</option>
              <option value="0">Nữ</option>
            </select>
          </div>
        </div>
      </div>
    </div>

    <!-- Nút thêm -->
    <div class="mb-3 text-end">
      <button class="btn btn-success" @click="openModal()">➕ Thêm nhân viên</button>
    </div>

    <!-- Bảng danh sách -->
    <div class="card shadow-sm">
      <div class="card-body p-0">
        <table class="table table-bordered table-hover mb-0">
          <thead class="table-success text-center">
            <tr>
              <th>Mã NV</th>
              <th>Tên TK</th>
              <th>Email</th>
              <th>Họ tên</th>
              <th>SĐT</th>
              <th>Địa chỉ</th>
              <th>Giới tính</th>
              <th>Vai trò</th>
              <th>Trạng thái</th>
              <th>Ảnh</th>
              <th>Hành động</th>
            </tr>
          </thead>
          <tbody class="text-center align-middle">
            <tr v-for="nv in pagedData" :key="nv.maNV">
              <td>{{ nv.maNV }}</td>
              <td>{{ nv.tenTaiKhoan }}</td>
              <td>{{ nv.email }}</td>
              <td>{{ nv.hoTen }}</td>
              <td>{{ nv.soDienThoai }}</td>
              <td>{{ nv.diaChi }}</td>
              <td>{{ nv.gioiTinh === 1 ? 'Nam' : 'Nữ' }}</td>
              <td>
                <span class="badge bg-primary">{{ nv.vaiTro === 1 ? 'Admin' : 'Nhân viên' }}</span>
              </td>
              <td>
                <span :class="nv.trangThai ? 'text-success' : 'text-danger'">
                  {{ nv.trangThai ? 'Hoạt động' : 'Đã khóa' }}
                </span>
              </td>
              <td>
                <img
                  v-if="nv.avata"
                  :src="nv.avata"
                  width="40"
                  height="40"
                  class="rounded-circle"
                />
              </td>
              <td>
                <button class="btn btn-warning btn-sm me-1" @click="openModal(nv)" title="Sửa">
                  ✏️
                </button>
                <button
                  class="btn btn-sm me-1"
                  :class="nv.trangThai ? 'btn-outline-danger' : 'btn-outline-success'"
                  @click="toggleTrangThai(nv.maNV)"
                >
                  {{ nv.trangThai ? 'Khoá' : 'Mở' }}
                </button>
                <button class="btn btn-danger btn-sm" @click="xoaNhanVien(nv.maNV)">🗑️</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Phân trang -->
    <div class="d-flex justify-content-between align-items-center mt-3">
      <div>Hiển thị {{ pagedData.length }} / {{ danhSachLoc.length }} nhân viên</div>
      <nav>
        <ul class="pagination pagination-sm mb-0">
          <li class="page-item" :class="{ disabled: currentPage === 1 }">
            <button class="page-link" @click="currentPage--">«</button>
          </li>
          <li
            class="page-item"
            v-for="page in totalPages"
            :key="page"
            :class="{ active: page === currentPage }"
          >
            <button class="page-link" @click="currentPage = page">{{ page }}</button>
          </li>
          <li class="page-item" :class="{ disabled: currentPage === totalPages }">
            <button class="page-link" @click="currentPage++">»</button>
          </li>
        </ul>
      </nav>
    </div>

    <!-- Modal thêm/sửa nhân viên -->
    <div
      v-if="showModal"
      class="modal fade show d-block"
      tabindex="-1"
      style="background: rgba(0, 0, 0, 0.5)"
    >
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <form @submit.prevent="saveNhanVien">
            <div class="modal-header">
              <h5 class="modal-title text-success">
                {{ form.maNV ? 'Cập nhật' : 'Thêm' }} nhân viên
              </h5>
              <button type="button" class="btn-close" @click="closeModal"></button>
            </div>
            <div class="modal-body">
              <div class="row g-3">
                <div class="col-md-6">
                  <label class="form-label">Tên tài khoản</label>
                  <input v-model="form.tenTaiKhoan" type="text" class="form-control" required />
                </div>
                <div class="col-md-6">
                  <label class="form-label">Email</label>
                  <input v-model="form.email" type="email" class="form-control" required />
                </div>
                <div class="col-md-6">
                  <label class="form-label">Họ tên</label>
                  <input v-model="form.hoTen" type="text" class="form-control" required />
                </div>
                <div class="col-md-6">
                  <label class="form-label">Số điện thoại</label>
                  <input v-model="form.soDienThoai" type="text" class="form-control" required />
                </div>
                <div class="col-md-6">
                  <label class="form-label">Địa chỉ</label>
                  <input v-model="form.diaChi" type="text" class="form-control" />
                </div>
                <div class="col-md-3">
                  <label class="form-label">Giới tính</label>
                  <select v-model="form.gioiTinh" class="form-select" required>
                    <option value="">-- Chọn --</option>
                    <option value="1">Nam</option>
                    <option value="0">Nữ</option>
                  </select>
                </div>
                <div class="col-md-3">
                  <label class="form-label">Vai trò</label>
                  <select v-model="form.vaiTro" class="form-select" required>
                    <option value="1">Admin</option>
                    <option value="2">Nhân viên</option>
                  </select>
                </div>
                <div class="col-12">
                  <label class="form-label">Link ảnh (base64 hoặc URL)</label>
                  <input v-model="form.avata" type="text" class="form-control" />
                  <div class="mt-2">
                    <img
                      v-if="form.avata"
                      :src="form.avata"
                      class="rounded-circle"
                      width="60"
                      height="60"
                    />
                  </div>
                </div>
              </div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" @click="closeModal">Hủy</button>
              <button type="submit" class="btn btn-success">Lưu</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

const danhSachNV = ref([
  {
    maNV: 1,
    tenTaiKhoan: 'admin1',
    email: 'admin@email.com',
    hoTen: 'Nguyễn Văn A',
    soDienThoai: '0123456789',
    diaChi: 'Hà Nội',
    gioiTinh: 1,
    vaiTro: 1,
    avata: 'vue-project\public\Img\avatanhanvien.jpg',
    trangThai: 1,
  },
  {
    maNV: 2,
    tenTaiKhoan: 'nv1',
    email: 'nv1@email.com',
    hoTen: 'Trần Thị B',
    soDienThoai: '0987654321',
    diaChi: 'Hồ Chí Minh',
    gioiTinh: 0,
    vaiTro: 2,
    avata: 'vue-project\public\Img\avatanhanvien2.jpg',
    trangThai: 1,
  },
])

const filter = ref({ hoTen: '', soDienThoai: '', vaiTro: '', gioiTinh: '' })

const danhSachLoc = computed(() => {
  return danhSachNV.value.filter((nv) => {
    const matchTen = nv.hoTen.toLowerCase().includes(filter.value.hoTen.toLowerCase())
    const matchSDT = nv.soDienThoai.includes(filter.value.soDienThoai)
    const matchVaiTro = filter.value.vaiTro === '' || nv.vaiTro.toString() === filter.value.vaiTro
    const matchGioiTinh =
      filter.value.gioiTinh === '' || nv.gioiTinh?.toString() === filter.value.gioiTinh
    return matchTen && matchSDT && matchVaiTro && matchGioiTinh
  })
})

// Phân trang
const currentPage = ref(1)
const pageSize = 20
const totalPages = computed(() => Math.ceil(danhSachLoc.value.length / pageSize))
const pagedData = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  return danhSachLoc.value.slice(start, start + pageSize)
})

// Hành động
const showModal = ref(false)
const form = ref({})

const openModal = (nv = null) => {
  form.value = nv
    ? { ...nv }
    : {
        maNV: null,
        tenTaiKhoan: '',
        email: '',
        hoTen: '',
        soDienThoai: '',
        diaChi: '',
        gioiTinh: '',
        vaiTro: '2',
        avata: '',
        trangThai: 1,
      }
  showModal.value = true
}
const closeModal = () => (showModal.value = false)

const saveNhanVien = () => {
  const isNew = !form.value.maNV
  const exists = danhSachNV.value.some(
    (nv) =>
      nv.tenTaiKhoan.toLowerCase() === form.value.tenTaiKhoan.toLowerCase() &&
      (!form.value.maNV || nv.maNV !== form.value.maNV),
  )
  if (exists) return alert('Tên tài khoản đã tồn tại!')
  if (isNew) {
    const newID = Math.max(...danhSachNV.value.map((nv) => nv.maNV)) + 1
    danhSachNV.value.push({ ...form.value, maNV: newID })
  } else {
    const index = danhSachNV.value.findIndex((nv) => nv.maNV === form.value.maNV)
    danhSachNV.value[index] = { ...form.value }
  }
  closeModal()
}

const xoaNhanVien = (id) => {
  if (confirm('Bạn có chắc chắn muốn xóa nhân viên này?')) {
    danhSachNV.value = danhSachNV.value.filter((nv) => nv.maNV !== id)
  }
}

const toggleTrangThai = (maNV) => {
  const nv = danhSachNV.value.find((nv) => nv.maNV === maNV)
  if (nv) nv.trangThai = nv.trangThai ? 0 : 1
}
</script>

<style scoped>
.table td,
.table th {
  vertical-align: middle;
}
.table img {
  object-fit: cover;
  border: 1px solid #ccc;
}
</style>
