<template>
  <div class="container py-4">
    <h4 class="text-success mb-4 fw-bold">📥 Quản lý Phiếu Nhập Hàng</h4>

    <!-- Bộ lọc -->
    <div class="card shadow-sm mb-3 border-0">
      <div class="card-body">
        <div class="row g-2">
          <div class="col-md-4">
            <input
              v-model="keyword"
              class="form-control shadow-sm rounded-pill"
              placeholder="🔍 Tìm mã PN, nhân viên, NCC"
            />
          </div>
          <div class="col-md-3">
            <input type="date" v-model="tuNgay" class="form-control shadow-sm rounded-pill" />
          </div>
          <div class="col-md-3">
            <input type="date" v-model="denNgay" class="form-control shadow-sm rounded-pill" />
          </div>
        </div>
      </div>
    </div>

    <!-- Nút tạo mới -->
    <div class="text-end mb-3">
      <button class="btn btn-success shadow-sm rounded-pill px-4" @click="moTao = true">
        ➕ Tạo phiếu nhập hàng
      </button>
    </div>

    <!-- Danh sách phiếu nhập -->
    <div class="card shadow-sm border-0">
      <div class="card-body p-0">
        <table class="table table-hover align-middle text-center mb-0">
          <thead class="table-success">
            <tr>
              <th>Mã PN</th>
              <th>Ngày nhập</th>
              <th>Nhân viên</th>
              <th>Nhà cung cấp</th>
              <th>Tổng tiền</th>
              <th>Chi tiết</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="pn in paginatedPhieuNhaps" :key="pn.maPN">
              <td class="fw-bold text-success">#{{ pn.maPN }}</td>
              <td>{{ formatDate(pn.ngayNhap) }}</td>
              <td>{{ pn.nhanVien?.hoTen }}</td>
              <td>{{ pn.nhaCungCap?.tenNCC }}</td>
              <td class="text-end text-danger fw-bold">{{ formatPrice(pn.tongTien) }}</td>
              <td>
                <button class="btn btn-info btn-sm action-square" @click="xemChiTiet(pn)">
                  <i class="bi bi-search"></i>
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Phân trang -->
    <nav v-if="totalPages > 1" class="mt-3">
      <ul class="pagination justify-content-center">
        <li class="page-item" :class="{ disabled: currentPage === 0 }">
          <button class="page-link" @click="changePage(currentPage - 1)">Trước</button>
        </li>
        <li
          v-for="page in totalPages"
          :key="page"
          class="page-item"
          :class="{ active: currentPage === page - 1 }"
        >
          <button class="page-link" @click="changePage(page - 1)">{{ page }}</button>
        </li>
        <li class="page-item" :class="{ disabled: currentPage === totalPages - 1 }">
          <button class="page-link" @click="changePage(currentPage + 1)">Sau</button>
        </li>
      </ul>
    </nav>

    <!-- Modal tạo phiếu nhập -->
    <div v-if="moTao" class="modal fade show d-block" style="background: rgba(0, 0, 0, 0.5)">
      <div class="modal-dialog modal-lg modal-dialog-centered">
        <div class="modal-content shadow-lg border-0">
          <div class="modal-header bg-success text-white">
            <h5 class="modal-title">📝 Tạo Phiếu Nhập Hàng</h5>
            <button class="btn-close btn-close-white" @click="moTao = false"></button>
          </div>

          <div class="modal-body">
            <!-- Hàng: Tìm thuốc (trái) + NCC (phải) -->
            <div class="row g-3 mb-3">
              <!-- Tìm thuốc -->
              <div class="col-md-6 position-relative" ref="searchWrapper">
                <label class="form-label fw-bold">Thuốc / Biến thể</label>
                <input
                  v-model="search"
                  class="form-control shadow-sm"
                  placeholder="🔍 Tìm thuốc hoặc biến thể"
                  @input="filterSuggestions"
                />
                <ul
                  v-if="suggestions.length"
                  class="list-group position-absolute w-100 shadow border"
                  style="max-height: 300px; overflow-y: auto; z-index: 1056"
                >
                  <li
                    v-for="s in suggestions"
                    :key="s.ma"
                    class="list-group-item d-flex justify-content-between align-items-center suggestion-item"
                  >
                    <div class="d-flex align-items-center flex-grow-1">
                      <img
                        :src="getImageUrl(s.hinhAnh)"
                        class="me-3"
                        style="width: 60px; height: 60px; object-fit: cover; border-radius: 6px"
                      />
                      <div>
                        <div class="fw-semibold text-truncate" style="max-width: 300px">
                          {{ s.ten }}
                        </div>
                        <small class="text-muted">Mã: {{ s.ma }}</small>
                        <div>
                          <span
                            class="badge"
                            :class="s.type === 'thuoc' ? 'bg-primary' : 'bg-secondary'"
                          >
                            {{ s.type === 'thuoc' ? 'Thuốc' : 'Biến thể' }}
                          </span>
                        </div>
                      </div>
                    </div>
                    <button class="btn btn-outline-success btn-sm ms-3" @click="chonThuoc(s)">
                      ✔
                    </button>
                  </li>
                </ul>
              </div>

              <!-- Nhà cung cấp -->
              <div class="col-md-6">
                <label class="form-label fw-bold">Nhà cung cấp</label>
                <select v-model="form.maNCC" class="form-select shadow-sm rounded-pill">
                  <option disabled value="">-- Chọn --</option>
                  <option v-for="ncc in nhaCungCaps" :key="ncc.maNCC" :value="ncc.maNCC">
                    {{ ncc.tenNCC }}
                  </option>
                </select>
              </div>
            </div>

            <!-- Hàng nhập thông tin chi tiết -->
            <!-- Hàng nhập thông tin chi tiết -->
            <div class="row g-3 mb-3 align-items-end">
              <div class="col-md-2">
                <label class="form-label fw-bold">Số lượng</label>
                <input
                  type="number"
                  class="form-control shadow-sm"
                  v-model.number="thuoc.soLuong"
                />
              </div>

              <div class="col-md-2">
                <label class="form-label fw-bold">Đơn giá</label>
                <input
                  type="number"
                  class="form-control shadow-sm"
                  v-model.number="thuoc.donGiaNhap"
                />
              </div>

              <div class="col-md-3">
                <label class="form-label fw-bold">Ngày sản xuất</label>
                <input type="date" class="form-control shadow-sm" v-model="thuoc.ngaySanXuat" />
              </div>

              <div class="col-md-3">
                <label class="form-label fw-bold">Hạn sử dụng</label>
                <input type="date" class="form-control shadow-sm" v-model="thuoc.hanSuDung" />
              </div>

              <div class="col-md-2 d-flex">
                <button class="btn btn-primary flex-fill mt-2" @click="themThuoc">Thêm</button>
              </div>
            </div>

            <!-- Bảng chi tiết nhập -->
            <table class="table table-bordered">
              <thead class="table-light">
                <tr>
                  <th>STT</th>
                  <th>Tên</th>
                  <th>SL</th>
                  <th>Giá</th>
                  <th>Ngày SX</th>
                  <th>Hạn SD</th>
                  <th>Tổng</th>
                  <th></th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(t, i) in form.chiTiet" :key="i">
                  <td>{{ i + 1 }}</td>
                  <td>{{ t.ten }}</td>
                  <td>{{ t.soLuong }}</td>
                  <td class="text-end">{{ formatPrice(t.donGiaNhap) }}</td>
                  <td>{{ formatDate(t.ngaySanXuat) }}</td>
                  <td>{{ formatDate(t.hanSuDung) }}</td>
                  <td class="text-end text-danger fw-bold">
                    {{ formatPrice(t.soLuong * t.donGiaNhap) }}
                  </td>
                  <td>
                    <button class="btn btn-sm btn-danger" @click="form.chiTiet.splice(i, 1)">
                      Xoá
                    </button>
                  </td>
                </tr>
                <tr>
                  <td colspan="6" class="text-end fw-bold">Tổng cộng</td>
                  <td class="text-end text-danger fw-bold">{{ formatPrice(tinhTongTien()) }}</td>
                  <td></td>
                </tr>
              </tbody>
            </table>
          </div>

          <div class="modal-footer">
            <button class="btn btn-secondary" @click="moTao = false">Hủy</button>
            <button
              class="btn btn-success px-4"
              @click="luuPhieuNhap"
              :disabled="!form.maNCC || form.chiTiet.length === 0"
            >
              💾 Lưu
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Modal chi tiết -->
    <ChiTietPhieuNhap
      v-if="modalChiTiet"
      :phieuNhap="selected"
      :chiTietList="chiTietList"
      @close="modalChiTiet = false"
      @print="printPhieuNhap"
    />

    <ToastMessage ref="toast" />
  </div>
</template>

<script setup>
import { ref, onMounted, computed, onBeforeUnmount } from 'vue'
import axios from 'axios'
import ToastMessage from '../ToastMessage.vue'
import ChiTietPhieuNhap from './ChiTietPhieuNhap.vue'

const toast = ref(null)
const keyword = ref('')
const tuNgay = ref('')
const denNgay = ref('')
const currentPage = ref(0)
const pageSize = 10

const phieuNhaps = ref([])
const chiTietList = ref([])
const selected = ref(null)
const moTao = ref(false)
const modalChiTiet = ref(false)
const currentUser = ref({ maNV: 1, hoTen: 'Nguyễn Văn A' })
const form = ref({ maNCC: '', chiTiet: [] })

const nhaCungCaps = ref([])
const dsThuoc = ref([])
const dsBienThe = ref([])
const suggestions = ref([])
const search = ref('')
const thuoc = ref({
  ma: null,
  ten: '',
  type: '',
  soLuong: 1,
  donGiaNhap: 0,
  ngaySanXuat: '',
  hanSuDung: '',
})

const formatPrice = (n) => (n ?? 0).toLocaleString('vi-VN') + ' đ'
const formatDate = (d) => (d ? new Date(d).toLocaleDateString('vi-VN') : '')
const tinhTongTien = () => form.value.chiTiet.reduce((sum, t) => sum + t.soLuong * t.donGiaNhap, 0)

const searchWrapper = ref(null)
const handleClickOutside = (event) => {
  if (searchWrapper.value && !searchWrapper.value.contains(event.target)) {
    suggestions.value = []
  }
}
onBeforeUnmount(() => {
  document.removeEventListener('click', handleClickOutside)
})

const filterSuggestions = () => {
  const kw = search.value.toLowerCase()
  suggestions.value = []

  dsThuoc.value.forEach((thuoc) => {
    const isMatch = thuoc.tenThuoc.toLowerCase().includes(kw)
    const matchedBienThes = thuoc.bienThes?.filter((bt) =>
      (thuoc.tenThuoc + ' - ' + bt.tenBienThe).toLowerCase().includes(kw),
    )

    if (isMatch) {
      suggestions.value.push({
        ma: thuoc.maThuoc,
        ten: thuoc.tenThuoc,
        type: 'thuoc',
        hinhAnh: thuoc.hinhAnhChinh,
      })
    }

    matchedBienThes?.forEach((bt) => {
      suggestions.value.push({
        ma: bt.maBienThe,
        ten: bt.tenBienThe,
        type: 'bienthe',
        hinhAnh: bt.hinhAnh,
      })
    })
  })
}

const chonThuoc = (item) => {
  thuoc.value = { ...item, soLuong: 1, donGiaNhap: 0, ngaySanXuat: '', hanSuDung: '' }
  search.value = item.ten
  suggestions.value = []
}

const getImageUrl = (path) =>
  path ? `http://localhost:8080/${path.startsWith('/') ? path.slice(1) : path}` : ''

const themThuoc = () => {
  if (
    thuoc.value.ma &&
    thuoc.value.soLuong > 0 &&
    thuoc.value.donGiaNhap > 0 &&
    thuoc.value.ngaySanXuat &&
    thuoc.value.hanSuDung
  ) {
    const exists = form.value.chiTiet.some(
      (t) => t.ma === thuoc.value.ma && t.type === thuoc.value.type,
    )
    if (exists) {
      toast.value.show('⚠️ Sản phẩm đã tồn tại trong danh sách', 'warning')
      return
    }
    form.value.chiTiet.push({ ...thuoc.value })
    thuoc.value = {
      ma: null,
      ten: '',
      type: '',
      soLuong: 1,
      donGiaNhap: 0,
      ngaySanXuat: '',
      hanSuDung: '',
    }
    search.value = ''
  } else {
    toast.value.show('❌ Vui lòng nhập đủ thông tin (SL, giá, ngày SX, HSD)', 'error')
  }
}

const luuPhieuNhap = async () => {
  const chiTiet = form.value.chiTiet.map((t) => ({
    maThuoc: t.type === 'thuoc' ? t.ma : null,
    maBienThe: t.type === 'bienthe' ? t.ma : null,
    soLuong: t.soLuong,
    donGiaNhap: t.donGiaNhap,
    ngaySanXuat: t.ngaySanXuat,
    hanSuDung: t.hanSuDung,
  }))
  await axios.post('http://localhost:8080/api/phieu-nhap', {
    maNV: currentUser.value.maNV,
    maNCC: form.value.maNCC,
    chiTiet,
  })
  moTao.value = false
  form.value = { maNCC: '', chiTiet: [] }
  await loadPhieuNhaps()
  toast.value.show('✅ Tạo phiếu nhập thành công!', 'success')
}

const xemChiTiet = async (pn) => {
  selected.value = pn
  const res = await axios.get(`http://localhost:8080/api/phieu-nhap/${pn.maPN}/chi-tiet`)
  chiTietList.value = res.data
  modalChiTiet.value = true
}

const filteredPhieuNhaps = computed(() => {
  let list = [...phieuNhaps.value]
  list.sort((a, b) => new Date(b.ngayNhap) - new Date(a.ngayNhap))
  if (keyword.value) {
    const kw = keyword.value.toLowerCase()
    list = list.filter(
      (pn) =>
        pn.maPN.toString().includes(kw) ||
        pn.nhanVien?.hoTen?.toLowerCase().includes(kw) ||
        pn.nhaCungCap?.tenNCC?.toLowerCase().includes(kw),
    )
  }
  if (tuNgay.value) {
    list = list.filter((pn) => new Date(pn.ngayNhap) >= new Date(tuNgay.value))
  }
  if (denNgay.value) {
    list = list.filter((pn) => new Date(pn.ngayNhap) <= new Date(denNgay.value))
  }
  return list
})

const totalPages = computed(() => Math.ceil(filteredPhieuNhaps.value.length / pageSize))
const paginatedPhieuNhaps = computed(() => {
  const start = currentPage.value * pageSize
  return filteredPhieuNhaps.value.slice(start, start + pageSize)
})
const changePage = (page) => {
  if (page >= 0 && page < totalPages.value) currentPage.value = page
}

const loadPhieuNhaps = async () => {
  const res = await axios.get('http://localhost:8080/api/phieu-nhap')
  phieuNhaps.value = res.data
}
const loadData = async () => {
  const [ncc, thuocRes, bienTheRes] = await Promise.all([
    axios.get('http://localhost:8080/api/nhacungcap'),
    axios.get('http://localhost:8080/api/thuoc'),
    axios.get('http://localhost:8080/api/bienthe'),
  ])

  nhaCungCaps.value = ncc.data
  dsThuoc.value = thuocRes.data
  dsBienThe.value = bienTheRes.data

  dsThuoc.value.forEach((t) => {
    t.bienThes = dsBienThe.value.filter((bt) => bt.maThuoc === t.maThuoc)
  })
}

const printPhieuNhap = (htmlContent) => {
  const printWindow = window.open('', '_blank', 'width=900,height=650')
  printWindow.document.write(`
    <html>
      <head>
        <title>In phiếu nhập</title>
        <style>
          body { font-family: Arial, sans-serif; padding: 20px; }
          table { width: 100%; border-collapse: collapse; }
          th, td { border: 1px solid #ccc; padding: 8px; }
          th { background: #f8f8f8; }
          .text-end { text-align: right; }
          .fw-bold { font-weight: bold; }
          .text-danger { color: red; }
        </style>
      </head>
      <body>
        <h2 style="text-align:center">PHIẾU NHẬP HÀNG</h2>
        ${htmlContent}
      </body>
    </html>
  `)
  printWindow.document.close()
  printWindow.print()
}

onMounted(() => {
  document.addEventListener('click', handleClickOutside)
  loadPhieuNhaps()
  loadData()
})
</script>

<style scoped>
.list-group-item {
  padding: 10px 12px;
  white-space: nowrap;
}
.list-group {
  min-width: 600px;
  max-width: 800px;
}
</style>
