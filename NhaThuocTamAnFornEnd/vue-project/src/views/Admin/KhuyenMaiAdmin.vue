<template>
  <div class="container py-4">
    <h4 class="text-success mb-3">🎁 Quản Lý Voucher</h4>

    <!-- Bộ lọc -->
    <div class="card shadow-sm mb-3">
      <div class="card-body">
        <div class="row g-3 align-items-end">
          <div class="col-md-3">
            <label class="form-label">🔎 Tên hoặc Mã Code</label>
            <input
              v-model="filter.keyword"
              class="form-control"
              placeholder="Tên hoặc mã code..."
            />
          </div>

          <div class="col-md-2">
            <label class="form-label">📌 Trạng thái</label>
            <select v-model="filter.trangThai" class="form-select">
              <option value="">-- Tất cả --</option>
              <option value="true">Kích hoạt</option>
              <option value="false">Tạm ẩn</option>
            </select>
          </div>

          <div class="col-md-2">
            <label class="form-label">📅 Bắt đầu</label>
            <input type="date" v-model="filter.tuNgay" class="form-control" />
          </div>

          <div class="col-md-2">
            <label class="form-label">📅 Kết thúc</label>
            <input type="date" v-model="filter.denNgay" class="form-control" />
          </div>

          <div class="col-md-1">
            <button class="btn btn-outline-secondary w-100" @click="resetFilter">
              <i class="bi bi-x-circle"></i>
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Thêm mới -->
    <div class="text-end mb-2">
      <button class="btn btn-success" @click="openCreateForm">
        <i class="bi bi-plus-circle me-1"></i> Thêm voucher
      </button>
    </div>

    <!-- Danh sách -->
    <table class="table table-bordered text-center align-middle">
      <thead class="table-success">
        <tr>
          <th>Mã</th>
          <th>Tên KM</th>
          <th>Code</th>
          <th>Loại</th>
          <th>Giá trị</th>
          <th>Giảm tối đa</th>
          <th>Đơn tối thiểu</th>
          <th>Còn lại</th>
          <th>Thời gian</th>
          <th>Trạng thái</th>
          <th>Hành động</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="v in paginatedList" :key="v.maKM">
          <td>{{ v.maKM }}</td>
          <td>{{ v.tenKM }}</td>
          <td>
            <span class="badge bg-info text-dark">{{ v.maCode }}</span>
          </td>
          <td>{{ v.loaiKM === 'percent' ? 'Phần trăm' : 'Giảm tiền' }}</td>
          <td>{{ v.loaiKM === 'percent' ? v.giaTri + '%' : formatCurrency(v.giaTri) }}</td>
          <td>{{ formatCurrency(v.giaTriToiDa || 0) }}</td>
          <td>{{ formatCurrency(v.donHangToiThieu || 0) }}</td>
          <td>{{ v.soLuong - (v.daSuDung || 0) }}</td>
          <td>{{ formatDate(v.ngayBatDau) }} → {{ formatDate(v.ngayKetThuc) }}</td>
          <td>
            <span class="badge" :class="v.trangThai ? 'bg-success' : 'bg-secondary'">
              {{ v.trangThai ? 'Kích hoạt' : 'Tạm ẩn' }}
            </span>
          </td>
          <td>
            <div class="d-flex justify-content-center gap-2">
              <button class="btn btn-sm btn-warning" @click="edit(v)">
                <i class="bi bi-pencil"></i>
              </button>
              <button
                class="btn btn-sm"
                :class="v.trangThai ? 'btn-secondary' : 'btn-success'"
                @click="toggleTrangThai(v)"
              >
                <i :class="v.trangThai ? 'bi bi-toggle-off' : 'bi bi-toggle-on'"></i>
              </button>
              <button class="btn btn-sm btn-danger" @click="remove(v.maKM)">
                <i class="bi bi-trash"></i>
              </button>
            </div>
          </td>
        </tr>
        <tr v-if="filtered.length === 0">
          <td colspan="11" class="text-muted">Không có voucher nào phù hợp.</td>
        </tr>
      </tbody>
    </table>

    <!-- Phân trang -->
    <Pagination :current-page="currentPage" :total-pages="totalPages" @change-page="changePage" />

    <!-- Form tạo / sửa -->
    <div v-if="showForm" class="modal-overlay">
      <div class="modal-content p-4">
        <h5 class="bg-success text-white p-2 rounded">
          {{ isEdit ? 'Sửa voucher' : 'Thêm voucher' }}
        </h5>

        <div class="row g-3 mt-2">
          <div class="col-md-6">
            <input v-model="form.tenKM" class="form-control" placeholder="Tên khuyến mãi" />
          </div>
          <div class="col-md-6">
            <input v-model="form.maCode" class="form-control" placeholder="Mã code" />
          </div>

          <div class="col-md-4">
            <select v-model="form.loaiKM" class="form-select">
              <option value="percent">Phần trăm</option>
              <option value="money">Giảm tiền</option>
            </select>
          </div>

          <div class="col-md-4">
            <input
              type="number"
              v-model.number="form.giaTri"
              class="form-control"
              placeholder="Giá trị"
            />
          </div>
          <div class="col-md-4" v-if="form.loaiKM === 'percent'">
            <input
              type="number"
              v-model.number="form.giaTriToiDa"
              class="form-control"
              placeholder="Giảm tối đa"
            />
          </div>

          <div class="col-md-4">
            <input
              type="number"
              v-model.number="form.donHangToiThieu"
              class="form-control"
              placeholder="Đơn tối thiểu"
            />
          </div>
          <div class="col-md-4">
            <input
              type="number"
              v-model.number="form.soLuong"
              class="form-control"
              placeholder="Số lượng"
            />
          </div>
          <div class="col-md-4" v-if="isEdit">
            <label class="form-label">Đã sử dụng</label>
            <input type="number" v-model.number="form.daSuDung" class="form-control" readonly />
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
            <textarea
              v-model="form.moTa"
              class="form-control"
              placeholder="Mô tả"
              rows="2"
            ></textarea>
          </div>

          <div class="col-12 form-check">
            <input type="checkbox" class="form-check-input" v-model="form.trangThai" />
            <label class="form-check-label">Đang hoạt động</label>
          </div>
        </div>

        <div class="text-end mt-3">
          <button class="btn btn-primary" @click="save"><i class="bi bi-save me-1"></i> Lưu</button>
          <button class="btn btn-secondary ms-2" @click="closeForm">Hủy</button>
        </div>
      </div>
    </div>

    <!-- Toast -->
    <ToastMessage ref="toastRef" />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'
import ToastMessage from '../ToastMessage.vue'
import Pagination from '../Pagination.vue'

const list = ref([])
const showForm = ref(false)
const isEdit = ref(false)
const form = ref({})
const toastRef = ref(null)
const baseURL = 'http://localhost:8080/api/khuyen-mai'

// Phân trang
const currentPage = ref(1)
const pageSize = 5

const filter = ref({
  keyword: '',
  trangThai: '',
  tuNgay: '',
  denNgay: '',
})

const filtered = computed(() => {
  return list.value.filter((v) => {
    const keywordMatch = (v.tenKM + v.maCode)
      .toLowerCase()
      .includes(filter.value.keyword.toLowerCase())

    const statusMatch =
      filter.value.trangThai === '' ? true : v.trangThai === (filter.value.trangThai === 'true')

    const ngayBD = new Date(v.ngayBatDau)
    const ngayKT = new Date(v.ngayKetThuc)
    const tuNgay = filter.value.tuNgay ? new Date(filter.value.tuNgay) : null
    const denNgay = filter.value.denNgay ? new Date(filter.value.denNgay) : null

    const dateMatch =
      (!tuNgay || ngayBD >= tuNgay || ngayKT >= tuNgay) &&
      (!denNgay || ngayBD <= denNgay || ngayKT <= denNgay)

    return keywordMatch && statusMatch && dateMatch
  })
})

const totalPages = computed(() => Math.ceil(filtered.value.length / pageSize))
const paginatedList = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  return filtered.value.slice(start, start + pageSize)
})
const changePage = (page) => {
  currentPage.value = page
}

function resetFilter() {
  filter.value = {
    keyword: '',
    trangThai: '',
    tuNgay: '',
    denNgay: '',
  }
}

onMounted(fetchData)

function fetchData() {
  axios
    .get(baseURL)
    .then((res) => (list.value = res.data))
    .catch((err) => toastRef.value.show('❌ Lỗi tải voucher: ' + err.message, 'error'))
}

function formatDate(d) {
  return d ? new Date(d).toLocaleDateString('vi-VN') : '-'
}

function formatCurrency(v) {
  return (v || 0).toLocaleString('vi-VN', { style: 'currency', currency: 'VND' })
}

function openCreateForm() {
  form.value = {
    tenKM: '',
    maCode: '',
    loaiKM: 'percent',
    giaTri: 0,
    giaTriToiDa: 0,
    donHangToiThieu: 0,
    soLuong: 1,
    daSuDung: 0,
    ngayBatDau: '',
    ngayKetThuc: '',
    moTa: '',
    trangThai: true,
  }
  isEdit.value = false
  showForm.value = true
}

function edit(v) {
  form.value = { ...v }
  isEdit.value = true
  showForm.value = true
}

function closeForm() {
  showForm.value = false
}

function toggleTrangThai(v) {
  const newStatus = !v.trangThai
  if (!confirm(`Bạn có chắc muốn ${newStatus ? 'bật' : 'tắt'} khuyến mãi này?`)) return

  axios
    .put(`${baseURL}/${v.maKM}`, { ...v, trangThai: newStatus })
    .then(() => {
      toastRef.value.show(`✅ Đã ${newStatus ? 'bật' : 'tắt'} khuyến mãi`, 'success')
      fetchData()
    })
    .catch((err) => toastRef.value.show('❌ Lỗi cập nhật: ' + err.message, 'error'))
}

function save() {
  const f = form.value
  if (!f.tenKM || !f.maCode || !f.giaTri || f.giaTri <= 0 || f.soLuong <= 0) {
    toastRef.value.show('⚠️ Nhập đầy đủ tên, mã code, giá trị > 0, số lượng > 0.', 'warning')
    return
  }

  if (f.loaiKM === 'percent' && (!f.giaTriToiDa || f.giaTriToiDa <= 0)) {
    toastRef.value.show('⚠️ Phải nhập giảm tối đa > 0 với loại phần trăm.', 'warning')
    return
  }

  if (!f.ngayBatDau || !f.ngayKetThuc || new Date(f.ngayBatDau) > new Date(f.ngayKetThuc)) {
    toastRef.value.show('⚠️ Ngày bắt đầu không được sau ngày kết thúc.', 'warning')
    return
  }

  const dto = {
    ...f,
    giaTri: +f.giaTri,
    giaTriToiDa: f.loaiKM === 'percent' ? +f.giaTriToiDa : null,
    donHangToiThieu: +f.donHangToiThieu,
    soLuong: +f.soLuong,
    daSuDung: +f.daSuDung || 0,
  }

  const request = isEdit.value ? axios.put(`${baseURL}/${dto.maKM}`, dto) : axios.post(baseURL, dto)

  request
    .then(() => {
      toastRef.value.show('✅ Lưu thành công', 'success')
      fetchData()
      closeForm()
    })
    .catch((err) => toastRef.value.show('❌ Lỗi lưu: ' + err.message, 'error'))
}

function remove(id) {
  if (confirm('❌ Bạn chắc chắn muốn xóa?')) {
    axios
      .delete(`${baseURL}/${id}`)
      .then(() => {
        toastRef.value.show('🗑️ Xóa thành công', 'success')
        fetchData()
      })
      .catch((err) => toastRef.value.show('❌ Lỗi xóa: ' + err.message, 'error'))
  }
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
  width: 700px;
  max-height: 95vh;
  overflow-y: auto;
}
</style>
