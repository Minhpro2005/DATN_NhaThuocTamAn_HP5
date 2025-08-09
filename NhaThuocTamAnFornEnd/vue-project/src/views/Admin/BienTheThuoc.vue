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
      <div class="col-md-4 d-flex align-items-center">
        <input class="form-check-input me-2" type="checkbox" v-model="hienDaXoa" id="showDeleted" />
        <label class="form-check-label" for="showDeleted">Hiển thị biến thể đã xóa</label>
      </div>
    </div>

    <!-- Nút thêm -->
    <div class="mb-3" v-if="!hienDaXoa">
      <button class="btn btn-success" @click="openModal()">➕ Thêm biến thể</button>
    </div>

    <!-- Danh sách -->
    <div class="card shadow-sm">
      <div class="card-body p-0">
        <table class="table table-bordered table-hover text-center">
          <thead class="table-success">
            <tr>
              <th>Mã thuốc</th>
              <th>Mã biến thể</th>
              <th>Tên biến thể</th>
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
              <td>{{ bt.maThuoc }}</td>
              <td>{{ bt.maBienThe }}</td>
              <td>{{ bt.tenBienThe }}</td>
              <td>{{ formatCurrency(bt.giaBan) }}</td>
              <td>{{ bt.tenDonViTinh }}</td>
              <td>{{ bt.moTaQuyCach }}</td>
              <td :class="bt.trangThai ? 'text-success' : 'text-danger'">
                {{ bt.trangThai ? 'Hoạt động' : 'Ngừng bán' }}
              </td>
              <td>
                <img
                  v-if="bt.hinhAnh"
                  :src="getImageUrl(bt.hinhAnh)"
                  alt="ảnh"
                  width="60"
                  height="60"
                  class="rounded"
                />
              </td>
              <td>
                <template v-if="!hienDaXoa">
                  <button class="btn btn-sm btn-warning me-2" @click="openModal(bt)">✏️</button>
                  <button class="btn btn-sm btn-danger" @click="xoaBienThe(bt.maBienThe)">
                    🗑️
                  </button>
                </template>
                <template v-else>
                  <button class="btn btn-sm btn-success" @click="khoiPhucBienThe(bt.maBienThe)">
                    Khôi phục
                  </button>
                </template>
              </td>
            </tr>
            <tr v-if="bienTheTrang.length === 0">
              <td colspan="9" class="text-center text-muted">Không có dữ liệu</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Modal -->
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
                v-model="form.maThuoc"
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
                v-model="form.giaBan"
                type="number"
                class="form-control mb-3"
                placeholder="Giá bán"
              />

              <select v-model="form.maDVT" class="form-select mb-3">
                <option disabled value="">-- Chọn đơn vị tính --</option>
                <option v-for="dvt in danhSachDonViTinh" :key="dvt.maDVT" :value="dvt.maDVT">
                  {{ dvt.ten }}
                </option>
              </select>

              <select v-model="form.maQCDG" class="form-select mb-3">
                <option disabled value="">-- Chọn quy cách --</option>
                <option v-for="qc in danhSachQuyCach" :key="qc.maQCDG" :value="qc.maQCDG">
                  {{ qc.moTa }}
                </option>
              </select>

              <textarea
                v-model="form.moTa"
                class="form-control mb-3"
                placeholder="Mô tả biến thể"
              ></textarea>
              <select v-model="form.trangThai" class="form-select mb-3">
                <option :value="true">Hoạt động</option>
                <option :value="false">Ngừng bán</option>
              </select>
              <input
                type="file"
                accept="image/*"
                class="form-control mb-3"
                @change="onFileChange"
              />
              <img
                v-if="form.hinhAnh"
                :src="getImageUrl(form.hinhAnh)"
                width="100"
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
