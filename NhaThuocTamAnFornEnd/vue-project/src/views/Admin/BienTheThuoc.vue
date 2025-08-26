<template>
  <div class="container mt-4">
    <h4 class="mb-4 text-success fw-bold">
      <i class="bi bi-diagram-3-fill me-2"></i> Quản lý Biến Thể Thuốc
    </h4>

    <!-- Bộ lọc -->
    <div class="card shadow-sm mb-3 border-0">
      <div class="card-body row g-3 align-items-center">
        <div class="col-md-4">
          <input
            v-model="maThuocFilter"
            type="number"
            class="form-control"
            placeholder="🔎 Lọc theo mã thuốc..."
          />
        </div>
        <div class="col-md-4 d-flex align-items-center">
          <input
            class="form-check-input me-2"
            type="checkbox"
            v-model="hienDaXoa"
            id="showDeleted"
          />
          <label class="form-check-label fw-semibold" for="showDeleted">
            Hiển thị biến thể đã xóa
          </label>
        </div>
        <div class="col-md-4 text-end" v-if="!hienDaXoa">
          <button class="btn btn-success" @click="openModal()">
            <i class="bi bi-plus-circle me-1"></i> Thêm biến thể
          </button>
        </div>
      </div>
    </div>

    <!-- Danh sách -->
    <div class="card shadow-sm border-0">
      <div class="card-body p-0">
        <table class="table table-bordered table-hover align-middle text-center mb-0">
          <thead class="table-success">
            <tr>
              <th>Mã thuốc</th>
              <th>Mã biến thể</th>
              <th class="text-start">Tên biến thể</th>
              <th>Giá bán</th>
              <th>Đơn vị</th>
              <th>Quy cách</th>
              <th>Trạng thái</th>
              <th>Ảnh</th>
              <th>Hành động</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="bt in bienTheTrang" :key="bt.maBienThe">
              <td class="fw-bold text-success">#{{ bt.maThuoc }}</td>
              <td>{{ bt.maBienThe }}</td>
              <td class="text-start">{{ bt.tenBienThe }}</td>
              <td class="fw-bold text-success">{{ formatCurrency(bt.giaBan) }}</td>
              <td>{{ bt.tenDonViTinh }}</td>
              <td>{{ bt.moTaQuyCach }}</td>
              <td>
                <span :class="bt.trangThai ? 'badge bg-success' : 'badge bg-danger'">
                  {{ bt.trangThai ? 'Hoạt động' : 'Ngừng bán' }}
                </span>
              </td>
              <td>
                <img
                  v-if="bt.hinhAnh"
                  :src="getImageUrl(bt.hinhAnh)"
                  alt="ảnh"
                  width="55"
                  height="55"
                  class="border rounded shadow-sm"
                />
                <i v-else class="bi bi-image fs-3 text-secondary"></i>
              </td>
              <td>
                <div class="d-flex justify-content-center gap-2">
                  <template v-if="!hienDaXoa">
                    <button class="btn btn-sm btn-warning" @click="openModal(bt)" title="Sửa">
                      <i class="bi bi-pencil-square"></i>
                    </button>
                    <button
                      class="btn btn-sm btn-danger"
                      @click="xoaBienThe(bt.maBienThe)"
                      title="Xóa"
                    >
                      <i class="bi bi-trash"></i>
                    </button>
                  </template>
                  <template v-else>
                    <button class="btn btn-sm btn-success" @click="khoiPhucBienThe(bt.maBienThe)">
                      <i class="bi bi-arrow-counterclockwise me-1"></i> Khôi phục
                    </button>
                  </template>
                </div>
              </td>
            </tr>
            <tr v-if="bienTheTrang.length === 0">
              <td colspan="9" class="text-center text-muted py-3">
                <i class="bi bi-inbox me-2"></i> Không có dữ liệu
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
        <div class="modal-dialog modal-lg">
          <div class="modal-content shadow-lg border-0">
            <!-- Header -->
            <div class="modal-header bg-success text-white">
              <h5 class="modal-title">
                <i class="bi bi-capsule me-2"></i>
                {{ form.maBienThe ? 'Sửa biến thể' : 'Thêm biến thể mới' }}
              </h5>
              <button class="btn-close btn-close-white" @click="closeModal"></button>
            </div>

            <!-- Body -->
            <div class="modal-body row g-3">
              <div class="col-md-6">
                <label class="form-label">Mã thuốc</label>
                <input v-model="form.maThuoc" type="number" class="form-control" />
              </div>
              <div class="col-md-6">
                <label class="form-label">Tên biến thể</label>
                <input v-model="form.tenBienThe" type="text" class="form-control" />
              </div>
              <div class="col-md-6">
                <label class="form-label">Giá bán</label>
                <input v-model="form.giaBan" type="number" class="form-control" />
              </div>
              <div class="col-md-6">
                <label class="form-label">Trạng thái</label>
                <select v-model="form.trangThai" class="form-select">
                  <option :value="true">Hoạt động</option>
                  <option :value="false">Ngừng bán</option>
                </select>
              </div>
              <div class="col-md-6">
                <label class="form-label">Đơn vị tính</label>
                <select v-model="form.maDVT" class="form-select">
                  <option disabled value="">-- Chọn đơn vị tính --</option>
                  <option v-for="dvt in danhSachDonViTinh" :key="dvt.maDVT" :value="dvt.maDVT">
                    {{ dvt.ten }}
                  </option>
                </select>
              </div>
              <div class="col-md-6">
                <label class="form-label">Quy cách</label>
                <select v-model="form.maQCDG" class="form-select">
                  <option disabled value="">-- Chọn quy cách --</option>
                  <option v-for="qc in danhSachQuyCach" :key="qc.maQCDG" :value="qc.maQCDG">
                    {{ qc.moTa }}
                  </option>
                </select>
              </div>
              <div class="col-12">
                <label class="form-label">Mô tả</label>
                <textarea v-model="form.moTa" class="form-control" rows="2"></textarea>
              </div>
              <div class="col-12">
                <label class="form-label">Ảnh</label>
                <input type="file" accept="image/*" class="form-control" @change="onFileChange" />
                <img
                  v-if="form.hinhAnh"
                  :src="getImageUrl(form.hinhAnh)"
                  width="100"
                  class="rounded border mt-2"
                />
              </div>
            </div>

            <!-- Footer -->
            <div class="modal-footer">
              <button class="btn btn-success" @click="luuBienThe">
                <i class="bi bi-save me-1"></i> Lưu
              </button>
              <button class="btn btn-secondary" @click="closeModal">
                <i class="bi bi-x-circle me-1"></i> Hủy
              </button>
            </div>
          </div>
        </div>
      </div>
    </template>

    <!-- Pagination + Toast -->
    <Pagination :current-page="currentPage" :total-pages="totalPages" @change-page="changePage" />
    <ToastMessage ref="toastRef" />
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import axios from 'axios'
import ToastMessage from '../ToastMessage.vue'
import Pagination from '../Pagination.vue'

const bienTheList = ref([])
const danhSachDonViTinh = ref([])
const danhSachQuyCach = ref([])
const maThuocFilter = ref('')
const hienDaXoa = ref(false)
const showModal = ref(false)
const form = ref({})
const fileAnh = ref(null)
const toastRef = ref(null)
const currentPage = ref(1)
const pageSize = 5

const bienTheLoc = computed(() => {
  return bienTheList.value.filter(
    (bt) => !maThuocFilter.value || bt.maThuoc == Number(maThuocFilter.value),
  )
})

const totalPages = computed(() => Math.ceil(bienTheLoc.value.length / pageSize))
const bienTheTrang = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  return bienTheLoc.value.slice(start, start + pageSize)
})

function formatCurrency(val) {
  return Number(val).toLocaleString('vi-VN') + '₫'
}

function getImageUrl(path) {
  return path ? `http://localhost:8080/${path.replace(/^\/+/, '')}` : ''
}

function openModal(bt = null) {
  form.value = bt
    ? { ...bt }
    : {
        maThuoc: '',
        tenBienThe: '',
        giaBan: '',
        maDVT: '',
        maQCDG: '',
        moTa: '',
        trangThai: true,
        hinhAnh: '',
      }
  fileAnh.value = null
  showModal.value = true
}

function closeModal() {
  showModal.value = false
  form.value = {}
}

function onFileChange(e) {
  fileAnh.value = e.target.files[0]
}

async function luuBienThe() {
  try {
    const formData = new FormData()
    formData.append('data', new Blob([JSON.stringify(form.value)], { type: 'application/json' }))
    if (fileAnh.value) formData.append('file', fileAnh.value)

    const url = form.value.maBienThe
      ? `http://localhost:8080/api/bienthe/${form.value.maBienThe}`
      : 'http://localhost:8080/api/bienthe'

    await axios({
      method: form.value.maBienThe ? 'put' : 'post',
      url,
      data: formData,
      headers: { 'Content-Type': 'multipart/form-data' },
    })

    await fetchData()
    closeModal()
    toastRef.value.show('✅ Lưu biến thể thành công!', 'success')
  } catch (err) {
    console.error(err)
    toastRef.value.show('❌ Lỗi lưu biến thể!', 'error')
  }
}

async function xoaBienThe(id) {
  if (!confirm('Bạn có chắc muốn xóa biến thể này?')) return
  try {
    await axios.delete(`http://localhost:8080/api/bienthe/${id}`)
    await fetchData()
    toastRef.value.show('🗑️ Đã xóa biến thể!', 'success')
  } catch (err) {
    toastRef.value.show('❌ Lỗi khi xóa biến thể!', 'error')
  }
}

async function khoiPhucBienThe(id) {
  if (!confirm('Bạn có chắc muốn khôi phục biến thể này?')) return
  try {
    await axios.put(`http://localhost:8080/api/bienthe/${id}/khoi-phuc`)
    await fetchData()
    toastRef.value.show('✅ Đã khôi phục biến thể!', 'success')
    hienDaXoa.value = false
  } catch (err) {
    toastRef.value.show('❌ Khôi phục thất bại!', 'error')
  }
}

function changePage(page) {
  currentPage.value = page
}

async function fetchData() {
  try {
    const url = hienDaXoa.value
      ? 'http://localhost:8080/api/bienthe/da-xoa'
      : 'http://localhost:8080/api/bienthe'
    const res = await axios.get(url)
    bienTheList.value = res.data
  } catch (err) {
    toastRef.value.show('❌ Lỗi khi tải biến thể!', 'error')
  }
}

async function fetchDonViTinh() {
  const res = await axios.get('http://localhost:8080/api/donvitinh')
  danhSachDonViTinh.value = res.data
}

async function fetchQuyCachDongGoi() {
  const res = await axios.get('http://localhost:8080/api/quycachdonggoi')
  danhSachQuyCach.value = res.data
}

onMounted(() => {
  fetchData()
  fetchDonViTinh()
  fetchQuyCachDongGoi()
})

watch(hienDaXoa, fetchData)
</script>
