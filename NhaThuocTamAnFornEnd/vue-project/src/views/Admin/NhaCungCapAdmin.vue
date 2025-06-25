<template>
  <div class="container py-4">
    <h4 class="mb-4 text-success">Quản lý nhà cung cấp</h4>

    <!-- Thanh tìm kiếm -->
    <div class="mb-3">
      <div class="input-group w-100">
        <span class="input-group-text bg-white border-0">🔎</span>
        <input
          v-model="tuKhoa"
          @input="timKiemNCC"
          type="text"
          class="form-control border-0 shadow-none focus-border"
          placeholder="Tìm kiếm nhà cung cấp..."
        />
        <button class="btn btn-outline-secondary" @click="xoaTimKiem">Xóa</button>
      </div>
    </div>

    <!-- Nút thêm mới -->
    <div class="mb-3">
      <button class="btn btn-success" @click="openModal()">➕ Thêm nhà cung cấp</button>
    </div>

    <!-- Bảng danh sách -->
    <div class="card shadow-sm">
      <div class="card-body p-0">
        <table class="table table-bordered table-hover mb-0">
          <thead class="table-success text-center">
            <tr>
              <th>Mã NCC</th>
              <th>Tên nhà cung cấp</th>
              <th>Địa chỉ</th>
              <th>SĐT</th>
              <th>Email</th>
              <th>Hành động</th>
            </tr>
          </thead>
          <tbody class="text-center align-middle">
            <tr v-for="ncc in danhSachNCC" :key="ncc.maNCC">
              <td>{{ ncc.maNCC }}</td>
              <td>{{ ncc.tenNCC }}</td>
              <td>{{ ncc.diaChi }}</td>
              <td>{{ ncc.soDienThoai }}</td>
              <td>{{ ncc.email }}</td>
              <td>
                <button class="btn btn-sm btn-warning me-2" @click="openModal(ncc)">Sửa</button>
                <button class="btn btn-sm btn-danger" @click="xoaNCC(ncc.maNCC)">Xóa</button>
              </td>
            </tr>
            <tr v-if="danhSachNCC.length === 0">
              <td colspan="6">Không có dữ liệu.</td>
            </tr>
          </tbody>
        </table>
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
            <h5 class="modal-title">{{ form.maNCC ? 'Sửa' : 'Thêm' }} nhà cung cấp</h5>
            <button class="btn-close" @click="closeModal"></button>
          </div>
          <div class="modal-body">
            <input v-model="form.tenNCC" class="form-control mb-2" placeholder="Tên nhà cung cấp" />
            <input v-model="form.diaChi" class="form-control mb-2" placeholder="Địa chỉ" />
            <input
              v-model="form.soDienThoai"
              class="form-control mb-2"
              placeholder="Số điện thoại"
            />
            <input v-model="form.email" class="form-control mb-2" placeholder="Email" />
          </div>
          <div class="modal-footer">
            <button class="btn btn-primary" @click="luuNCC">Lưu</button>
            <button class="btn btn-secondary" @click="closeModal">Hủy</button>
          </div>
        </div>
      </div>
    </div>
    <div v-if="showModal" class="modal-backdrop fade show"></div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const serverUrl = 'http://localhost:8080/api/nhacungcap'
const danhSachNCC = ref([])
const showModal = ref(false)
const form = ref({})
const tuKhoa = ref('')

// Load danh sách
async function loadNCC() {
  try {
    const res = await axios.get(serverUrl)
    danhSachNCC.value = res.data
  } catch (err) {
    console.error('Lỗi load:', err)
  }
}

// Tìm kiếm
async function timKiemNCC() {
  try {
    if (!tuKhoa.value.trim()) {
      await loadNCC()
      return
    }
    const res = await axios.get(`${serverUrl}/search?tenNCC=${tuKhoa.value}`)
    danhSachNCC.value = res.data
  } catch (err) {
    console.error('Lỗi tìm kiếm:', err)
  }
}

function xoaTimKiem() {
  tuKhoa.value = ''
  loadNCC()
}

// Mở modal thêm/sửa
function openModal(ncc = null) {
  form.value = ncc
    ? { ...ncc }
    : { maNCC: null, tenNCC: '', diaChi: '', soDienThoai: '', email: '' }
  showModal.value = true
}

function closeModal() {
  showModal.value = false
}

// Lưu (thêm hoặc sửa)
async function luuNCC() {
  if (!form.value.tenNCC.trim()) {
    alert('Tên nhà cung cấp không được để trống.')
    return
  }
  try {
    if (form.value.maNCC) {
      await axios.put(`${serverUrl}/${form.value.maNCC}`, form.value)
    } else {
      await axios.post(serverUrl, form.value)
    }
    await loadNCC()
    closeModal()
  } catch (err) {
    console.error('Lỗi lưu:', err)
  }
}

// Xóa
async function xoaNCC(id) {
  if (!confirm('Xác nhận xóa?')) return
  try {
    await axios.delete(`${serverUrl}/${id}`)
    await loadNCC()
  } catch (err) {
    console.error('Lỗi xóa:', err)
  }
}

// Load lần đầu
onMounted(loadNCC)
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
