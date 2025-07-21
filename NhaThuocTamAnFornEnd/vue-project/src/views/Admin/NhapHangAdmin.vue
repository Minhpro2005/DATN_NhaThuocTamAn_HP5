<template>
  <div class="container py-4">
    <h4 class="text-success mb-4 fw-bold">📥 Quản lý Phiếu Nhập</h4>

    <!-- Bộ lọc -->
    <div class="row g-2 mb-3">
      <div class="col-md-4">
        <input v-model="keyword" class="form-control" placeholder="🔍 Tìm mã PN, nhân viên, NCC" />
      </div>
      <div class="col-md-3">
        <input type="date" v-model="tuNgay" class="form-control" />
      </div>
      <div class="col-md-3">
        <input type="date" v-model="denNgay" class="form-control" />
      </div>
    </div>

    <!-- Nút tạo mới -->
    <div class="text-end mb-3">
      <button class="btn btn-success" @click="moTao = true">➕ Tạo phiếu nhập</button>
    </div>

    <!-- Danh sách phiếu nhập -->
    <table class="table table-bordered align-middle">
      <thead class="table-success text-center">
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
          <td class="text-center">{{ pn.maPN }}</td>
          <td>{{ formatDate(pn.ngayNhap) }}</td>
          <td>{{ pn.nhanVien?.hoTen }}</td>
          <td>{{ pn.nhaCungCap?.tenNCC }}</td>
          <td class="text-end text-danger fw-bold">{{ formatPrice(pn.tongTien) }}</td>
          <td class="text-center">
            <button class="btn btn-sm btn-info" @click="xemChiTiet(pn)">Xem</button>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- Phân trang -->
    <nav v-if="totalPages > 1" class="mt-3">
      <ul class="pagination justify-content-center">
        <li class="page-item" :class="{ disabled: currentPage === 0 }">
          <button class="page-link" @click="changePage(currentPage - 1)">Trước</button>
        </li>
        <li
          class="page-item"
          v-for="page in totalPages"
          :key="page"
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
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">📝 Tạo Phiếu Nhập</h5>
            <button class="btn-close" @click="moTao = false"></button>
          </div>
          <div class="modal-body">
            <div class="row g-3 mb-3">
              <div class="col-md-6">
                <label>Nhà cung cấp</label>
                <select v-model="form.maNCC" class="form-select">
                  <option disabled value="">-- Chọn --</option>
                  <option v-for="ncc in nhaCungCaps" :key="ncc.maNCC" :value="ncc.maNCC">
                    {{ ncc.tenNCC }}
                  </option>
                </select>
              </div>
            </div>

            <div class="row g-2 mb-3">
              <div class="col-md-4 position-relative">
                <input
                  v-model="search"
                  class="form-control"
                  placeholder="Tìm thuốc hoặc biến thể"
                  @input="filterSuggestions"
                />
                <ul v-if="suggestions.length" class="list-group position-absolute z-3">
                  <li
                    class="list-group-item list-group-item-action"
                    v-for="s in suggestions"
                    :key="s.ma"
                    @click="chonThuoc(s)"
                  >
                    {{ s.ten }}
                  </li>
                </ul>
              </div>
              <div class="col-md-3">
                <input
                  type="number"
                  class="form-control"
                  placeholder="Số lượng"
                  v-model.number="thuoc.soLuong"
                />
              </div>
              <div class="col-md-3">
                <input
                  type="number"
                  class="form-control"
                  placeholder="Đơn giá"
                  v-model.number="thuoc.donGiaNhap"
                />
              </div>
              <div class="col-md-2">
                <button class="btn btn-primary w-100" @click="themThuoc">Thêm</button>
              </div>
            </div>

            <table class="table table-bordered">
              <thead>
                <tr>
                  <th>#</th>
                  <th>Tên</th>
                  <th>SL</th>
                  <th>Giá</th>
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
                  <td class="text-end">{{ formatPrice(t.soLuong * t.donGiaNhap) }}</td>
                  <td>
                    <button class="btn btn-sm btn-danger" @click="form.chiTiet.splice(i, 1)">
                      Xoá
                    </button>
                  </td>
                </tr>
                <tr>
                  <td colspan="4" class="text-end fw-bold">Tổng cộng</td>
                  <td class="text-end text-danger fw-bold">{{ formatPrice(tinhTongTien()) }}</td>
                  <td></td>
                </tr>
              </tbody>
            </table>
          </div>
          <div class="modal-footer">
            <button class="btn btn-secondary" @click="moTao = false">Hủy</button>
            <button
              class="btn btn-success"
              @click="luuPhieuNhap"
              :disabled="!form.maNCC || form.chiTiet.length === 0"
            >
              Lưu
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Modal chi tiết phiếu nhập -->
    <div v-if="modalChiTiet" class="modal fade show d-block" style="background: rgba(0, 0, 0, 0.5)">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h5>Chi Tiết Phiếu Nhập #{{ selected?.maPN }}</h5>
            <button class="btn-close" @click="modalChiTiet = false"></button>
          </div>
          <div class="modal-body">
            <p><strong>Ngày nhập:</strong> {{ formatDate(selected.ngayNhap) }}</p>
            <p><strong>Nhân viên:</strong> {{ selected.nhanVien?.hoTen }}</p>
            <p><strong>Nhà cung cấp:</strong> {{ selected.nhaCungCap?.tenNCC }}</p>
            <table class="table table-striped">
              <thead>
                <tr>
                  <th>MaPN</th>
                  <th>Tên thuốc</th>
                  <th>SL</th>
                  <th>Đơn giá</th>
                  <th>Thành tiền</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(ct, i) in chiTietList" :key="i">
                  <td>{{ i + 1 }}</td>
                  <td>{{ ct.thuoc?.tenThuoc || ct.bienTheThuoc?.tenBienThe || 'Không rõ' }}</td>
                  <td>{{ ct.soLuong }}</td>
                  <td class="text-end">{{ formatPrice(ct.donGiaNhap) }}</td>
                  <td class="text-end">{{ formatPrice(ct.soLuong * ct.donGiaNhap) }}</td>
                </tr>
                <tr>
                  <td colspan="4" class="text-end fw-bold">Tổng cộng</td>
                  <td class="text-end text-danger fw-bold">{{ formatPrice(tongTienChiTiet) }}</td>
                </tr>
              </tbody>
            </table>
          </div>
          <div class="modal-footer">
            <button class="btn btn-secondary" @click="modalChiTiet = false">Đóng</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import axios from 'axios'

// Lọc và phân trang
const keyword = ref('')
const tuNgay = ref('')
const denNgay = ref('')
const currentPage = ref(0)
const pageSize = 10

// Dữ liệu phiếu nhập
const phieuNhaps = ref([])
const chiTietList = ref([])
const selected = ref(null)

// Modal & form
const moTao = ref(false)
const modalChiTiet = ref(false)
const currentUser = ref({ maNV: 1, hoTen: 'Nguyễn Văn A' })
const form = ref({ maNCC: '', chiTiet: [] })

// Dữ liệu phụ trợ
const nhaCungCaps = ref([])
const dsThuoc = ref([])
const dsBienThe = ref([])
const suggestions = ref([])
const search = ref('')
const thuoc = ref({ ma: null, ten: '', type: '', soLuong: 1, donGiaNhap: 0 })

// ===== Helpers =====
const formatPrice = (n) => (n ?? 0).toLocaleString('vi-VN') + ' đ'
const formatDate = (d) => new Date(d).toLocaleDateString('vi-VN')
const tinhTongTien = () => form.value.chiTiet.reduce((sum, t) => sum + t.soLuong * t.donGiaNhap, 0)

// ===== Search thuốc / biến thể =====
const filterSuggestions = () => {
  const kw = search.value.toLowerCase()
  suggestions.value = [
    ...dsThuoc.value
      .filter((t) => t.tenThuoc.toLowerCase().includes(kw))
      .map((t) => ({ ma: t.maThuoc, ten: t.tenThuoc, type: 'thuoc' })),
    ...dsBienThe.value
      .filter((bt) => bt.tenBienThe.toLowerCase().includes(kw))
      .map((bt) => ({ ma: bt.maBienThe, ten: bt.tenBienThe, type: 'bienthe' })),
  ]
}
const chonThuoc = (item) => {
  thuoc.value = { ma: item.ma, ten: item.ten, type: item.type, soLuong: 1, donGiaNhap: 0 }
  search.value = item.ten
  suggestions.value = []
}

// ===== Thêm vào chi tiết nhập =====
const themThuoc = () => {
  if (thuoc.value.ma && thuoc.value.soLuong > 0 && thuoc.value.donGiaNhap > 0) {
    const exists = form.value.chiTiet.some(
      (t) => t.ma === thuoc.value.ma && t.type === thuoc.value.type,
    )
    if (exists) return alert('Sản phẩm đã tồn tại trong danh sách')
    form.value.chiTiet.push({ ...thuoc.value })
    thuoc.value = { ma: null, ten: '', type: '', soLuong: 1, donGiaNhap: 0 }
    search.value = ''
  } else {
    alert('Vui lòng nhập đúng số lượng và giá')
  }
}

// ===== Lưu phiếu nhập =====
const luuPhieuNhap = async () => {
  const chiTiet = form.value.chiTiet.map((t) => ({
    maThuoc: t.type === 'thuoc' ? t.ma : null,
    maBienThe: t.type === 'bienthe' ? t.ma : null,
    soLuong: t.soLuong,
    donGiaNhap: t.donGiaNhap,
  }))
  await axios.post('http://localhost:8080/api/phieu-nhap', {
    maNV: currentUser.value.maNV,
    maNCC: form.value.maNCC,
    chiTiet,
  })
  moTao.value = false
  form.value = { maNCC: '', chiTiet: [] }
  await loadPhieuNhaps()
}

// ===== Xem chi tiết =====
const xemChiTiet = async (pn) => {
  selected.value = pn
  const res = await axios.get(`http://localhost:8080/api/phieu-nhap/${pn.maPN}/chi-tiet`)
  chiTietList.value = res.data
  modalChiTiet.value = true
}

// ===== Tìm kiếm và phân trang =====
const filteredPhieuNhaps = computed(() => {
  let list = [...phieuNhaps.value]
  list.sort((a, b) => new Date(b.ngayNhap) - new Date(a.ngayNhap)) // ⬅️ Thêm dòng này

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

const tongTienChiTiet = computed(() =>
  chiTietList.value.reduce((sum, ct) => sum + ct.soLuong * ct.donGiaNhap, 0),
)

// ===== Load dữ liệu =====
const loadPhieuNhaps = async () => {
  const res = await axios.get('http://localhost:8080/api/phieu-nhap')
  phieuNhaps.value = res.data
}
const loadData = async () => {
  const [ncc, thuoc, bienThe] = await Promise.all([
    axios.get('http://localhost:8080/api/nhacungcap'),
    axios.get('http://localhost:8080/api/thuoc'),
    axios.get('http://localhost:8080/api/bienthe'),
  ])
  nhaCungCaps.value = ncc.data
  dsThuoc.value = thuoc.data

  // Thêm tên hiển thị đầy đủ cho biến thể
  dsBienThe.value = bienThe.data.map((bt) => ({
    ...bt,
    tenThuocFull:
      bt.tenThuoc && bt.tenBienThe
        ? `${bt.tenThuoc} - ${bt.tenBienThe}`
        : bt.tenBienThe || 'Biến thể',
  }))
}

onMounted(() => {
  loadPhieuNhaps()
  loadData()
})
</script>
