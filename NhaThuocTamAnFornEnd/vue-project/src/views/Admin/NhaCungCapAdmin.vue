<template>
  <div class="container py-4">
    <h4 class="mb-4 text-success fw-bold">🏢 Quản lý nhà cung cấp</h4>

    <!-- Thanh tìm kiếm -->
    <div class="mb-3">
      <div class="input-group shadow-sm rounded-pill overflow-hidden w-100">
        <span class="input-group-text bg-white border-0">🔎</span>
        <input
          v-model="tuKhoa"
          @input="timKiemNCC"
          type="text"
          class="form-control border-0 shadow-none"
          placeholder="Tìm kiếm nhà cung cấp..."
        />
        <button class="btn btn-outline-danger rounded-pill px-4" @click="xoaTimKiem">Xóa</button>
      </div>
    </div>

    <!-- Nút thêm + toggle -->
    <div class="mb-3 d-flex justify-content-between">
      <button class="btn btn-success shadow-sm rounded-pill px-4" @click="openModal()">
        ➕ Thêm nhà cung cấp
      </button>
      <button class="btn btn-outline-secondary rounded-pill px-4" @click="toggleDeleted">
        {{ hienThiDaXoa ? '📂 Xem NCC đang hoạt động' : '🗑️ Xem NCC đã xóa' }}
      </button>
    </div>

    <!-- Danh sách -->
    <div class="card shadow-sm border-0">
      <div class="card-body p-0">
        <table class="table table-hover align-middle text-center mb-0">
          <thead class="table-success">
            <tr>
              <th>Mã NCC</th>
              <th>Tên nhà cung cấp</th>
              <th>Địa chỉ</th>
              <th>SĐT</th>
              <th>Email</th>
              <th>Hành động</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="ncc in nccTrang" :key="ncc.maNCC">
              <td class="fw-bold text-success">#{{ ncc.maNCC }}</td>
              <td>{{ ncc.tenNCC }}</td>
              <td>{{ ncc.diaChi }}</td>
              <td>{{ ncc.soDienThoai }}</td>
              <td>{{ ncc.email }}</td>
              <td>
                <div class="d-flex justify-content-center gap-2">
                  <!-- Khi đang xem NCC hoạt động -->
                  <template v-if="!hienThiDaXoa">
                    <button
                      class="btn btn-warning btn-sm d-flex align-items-center justify-content-center action-square"
                      @click="openModal(ncc)"
                      title="Chỉnh sửa"
                    >
                      <i class="bi bi-pencil"></i>
                    </button>
                    <button
                      class="btn btn-danger btn-sm d-flex align-items-center justify-content-center action-square"
                      @click="xoaNCC(ncc.maNCC)"
                      title="Ẩn nhà cung cấp"
                    >
                      <i class="bi bi-trash"></i>
                    </button>
                  </template>

                  <!-- Khi đang xem NCC đã xóa -->
                  <template v-else>
                    <button
                      class="btn btn-success btn-sm d-flex align-items-center justify-content-center action-square"
                      @click="khoiPhucNCC(ncc.maNCC)"
                      title="Khôi phục"
                    >
                      <i class="bi bi-arrow-counterclockwise"></i>
                    </button>
                  </template>
                </div>
              </td>
            </tr>
            <tr v-if="dsLoc.length === 0">
              <td colspan="6" class="text-muted">Không có dữ liệu.</td>
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
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content shadow-lg border-0">
          <div class="modal-header bg-success text-white">
            <h5 class="modal-title">{{ form.maNCC ? '✏ Sửa' : '➕ Thêm' }} nhà cung cấp</h5>
            <button class="btn-close btn-close-white" @click="closeModal"></button>
          </div>
          <div class="modal-body">
            <div class="mb-3">
              <label class="form-label fw-bold">Tên nhà cung cấp</label>
              <input
                v-model="form.tenNCC"
                class="form-control shadow-sm"
                placeholder="Nhập tên..."
              />
            </div>
            <div class="mb-3">
              <label class="form-label fw-bold">Địa chỉ</label>
              <input
                v-model="form.diaChi"
                class="form-control shadow-sm"
                placeholder="Nhập địa chỉ..."
              />
            </div>
            <div class="mb-3">
              <label class="form-label fw-bold">Số điện thoại</label>
              <input
                v-model="form.soDienThoai"
                class="form-control shadow-sm"
                placeholder="Nhập số điện thoại..."
              />
            </div>
            <div>
              <label class="form-label fw-bold">Email</label>
              <input
                v-model="form.email"
                class="form-control shadow-sm"
                placeholder="Nhập email..."
              />
            </div>
          </div>
          <div class="modal-footer">
            <button class="btn btn-secondary" @click="closeModal">Hủy</button>
            <button class="btn btn-success px-4" @click="luuNCC">💾 Lưu</button>
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
import { ref, onMounted, computed } from 'vue'
import axios from 'axios'
import ToastMessage from '../ToastMessage.vue'
import Pagination from '../Pagination.vue'

const serverUrl = 'http://localhost:8080/api/nhacungcap'
const danhSachNCC = ref([])
const showModal = ref(false)
const form = ref({})
const tuKhoa = ref('')
const toastRef = ref(null)
const hienThiDaXoa = ref(false) // false = đang hoạt động, true = đã xóa

// Phân trang
const currentPage = ref(1)
const pageSize = 5

const dsLoc = computed(() => danhSachNCC.value)
const totalPages = computed(() => Math.ceil(dsLoc.value.length / pageSize))
const nccTrang = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  return dsLoc.value.slice(start, start + pageSize)
})
const changePage = (page) => {
  currentPage.value = page
}

// Load danh sách
async function loadNCC() {
  try {
    const url = hienThiDaXoa.value ? `${serverUrl}/deleted` : serverUrl
    const res = await axios.get(url)
    danhSachNCC.value = res.data
    currentPage.value = 1
  } catch (err) {
    toastRef.value.show('❌ Lỗi khi tải danh sách.', 'error')
  }
}

// Toggle giữa hoạt động / đã xóa
function toggleDeleted() {
  hienThiDaXoa.value = !hienThiDaXoa.value
  loadNCC()
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
    currentPage.value = 1
  } catch (err) {
    toastRef.value.show('❌ Lỗi tìm kiếm.', 'error')
  }
}

function xoaTimKiem() {
  tuKhoa.value = ''
  loadNCC()
}

// Mở/Sửa
function openModal(ncc = null) {
  form.value = ncc
    ? { ...ncc }
    : { maNCC: null, tenNCC: '', diaChi: '', soDienThoai: '', email: '' }
  showModal.value = true
}

function closeModal() {
  showModal.value = false
}

// Lưu
async function luuNCC() {
  if (!form.value.tenNCC.trim()) {
    toastRef.value.show('❌ Tên nhà cung cấp không được để trống.', 'error')
    return
  }

  try {
    if (form.value.maNCC) {
      await axios.put(`${serverUrl}/${form.value.maNCC}`, form.value)
      toastRef.value.show('✅ Cập nhật nhà cung cấp thành công!', 'success')
    } else {
      await axios.post(serverUrl, form.value)
      toastRef.value.show('✅ Thêm nhà cung cấp thành công!', 'success')
    }
    await loadNCC()
    closeModal()
  } catch (err) {
    toastRef.value.show('❌ Lỗi khi lưu nhà cung cấp.', 'error')
  }
}

// Ẩn (xóa mềm)
async function xoaNCC(id) {
  if (!confirm('Bạn có chắc chắn muốn ẩn nhà cung cấp này?')) return
  try {
    await axios.delete(`${serverUrl}/${id}`)
    await loadNCC()
    toastRef.value.show('🚫 Đã ẩn nhà cung cấp.', 'success')
  } catch (err) {
    toastRef.value.show('❌ Không thể ẩn nhà cung cấp.', 'error')
  }
}

// Khôi phục
async function khoiPhucNCC(id) {
  if (!confirm('Bạn có chắc chắn muốn khôi phục nhà cung cấp này?')) return
  try {
    await axios.put(`${serverUrl}/restore/${id}`)
    await loadNCC()
    toastRef.value.show('✅ Khôi phục nhà cung cấp thành công.', 'success')
  } catch (err) {
    toastRef.value.show('❌ Không thể khôi phục nhà cung cấp.', 'error')
  }
}

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
