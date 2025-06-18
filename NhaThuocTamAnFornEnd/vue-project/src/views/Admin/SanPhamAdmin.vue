<template>
  <div class="container mt-4">
    <h4 class="mb-4 text-success fw-bold"><i class="bi bi-capsule me-2"></i> Quản lý thuốc</h4>

    <!-- Bộ lọc -->
    <div class="card shadow-sm mb-4">
      <div class="card-body">
        <div class="row g-3">
          <div class="col-md-3">
            <input
              v-model="filter.tenThuoc"
              type="text"
              class="form-control"
              placeholder="Tên thuốc..."
            />
          </div>
          <div class="col-md-3">
            <input
              v-model="filter.hoatChat"
              type="text"
              class="form-control"
              placeholder="Hoạt chất..."
            />
          </div>
          <div class="col-md-3">
            <input
              v-model="filter.congDung"
              type="text"
              class="form-control"
              placeholder="Công dụng..."
            />
          </div>
          <div class="col-md-3">
            <select v-model="filter.trangThai" class="form-select">
              <option value="">Trạng thái</option>
              <option :value="1">Hoạt động</option>
              <option :value="0">Ngừng bán</option>
            </select>
          </div>
        </div>
      </div>
    </div>

    <!-- Thêm thuốc -->
    <div class="mb-3">
      <button class="btn btn-success" @click="openModal()">➕ Thêm thuốc</button>
    </div>

    <!-- Bảng thuốc -->
    <div class="table-responsive shadow-sm border">
      <table class="table table-bordered table-hover text-center bg-white mb-0">
        <thead class="table-success">
          <tr>
            <th>Mã</th>
            <th>Tên thuốc</th>
            <th>Hoạt chất</th>
            <th>Đơn vị</th>
            <th>Giá bán</th>
            <th>HSD</th>
            <th>NSX</th>
            <th>Danh mục</th>
            <th>Trạng thái</th>
            <th>Hành động</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="sp in danhSachLoc" :key="sp.maThuoc">
            <td>{{ sp.maThuoc }}</td>
            <td>{{ sp.tenThuoc }}</td>
            <td>{{ sp.hoatChat }}</td>
            <td>{{ sp.donViTinh }}</td>
            <td>{{ formatCurrency(sp.giaBan) }}</td>
            <td>{{ sp.hanSuDung }}</td>
            <td>{{ sp.ngaySanXuat }}</td>
            <td>{{ getTenDanhMuc(sp.maDM) }}</td>
            <td>
              <span :class="sp.trangThai ? 'text-success' : 'text-danger'">
                {{ sp.trangThai ? 'Hoạt động' : 'Ngừng bán' }}
              </span>
            </td>
            <td>
              <button class="btn btn-sm btn-warning me-1" @click="openModal(sp)">Sửa</button>
              <button class="btn btn-sm btn-danger" @click="xoaSanPham(sp.maThuoc)">Xóa</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Modal thêm/sửa -->
    <div v-if="showModal" class="modal-backdrop fade show"></div>
    <div class="modal fade show d-block" v-if="showModal" @click.self="closeModal">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">{{ form.maThuoc ? 'Sửa thuốc' : 'Thêm thuốc' }}</h5>
            <button class="btn-close" @click="closeModal"></button>
          </div>
          <div class="modal-body">
            <div class="row g-3">
              <!-- Các trường đầy đủ -->
              <div class="col-md-6">
                <input
                  v-model="form.tenThuoc"
                  class="form-control"
                  placeholder="Tên thuốc"
                  required
                />
              </div>
              <div class="col-md-6">
                <input v-model="form.hoatChat" class="form-control" placeholder="Hoạt chất" />
              </div>

              <div class="col-md-12">
                <textarea
                  v-model="form.congDung"
                  class="form-control"
                  placeholder="Công dụng"
                ></textarea>
              </div>

              <div class="col-md-4">
                <input v-model="form.donViTinh" class="form-control" placeholder="Đơn vị tính" />
              </div>
              <div class="col-md-4">
                <input
                  v-model="form.donViQuyDoi"
                  class="form-control"
                  placeholder="Đơn vị quy đổi"
                />
              </div>
              <div class="col-md-4">
                <input v-model="form.xuatXu" class="form-control" placeholder="Xuất xứ" />
              </div>

              <div class="col-md-4">
                <input
                  v-model.number="form.giaNhap"
                  type="number"
                  class="form-control"
                  placeholder="Giá nhập"
                />
              </div>
              <div class="col-md-4">
                <input
                  v-model.number="form.giaBan"
                  type="number"
                  class="form-control"
                  placeholder="Giá bán"
                />
              </div>

              <div class="col-md-4">
                <input v-model="form.ngaySanXuat" type="date" class="form-control" />
              </div>
              <div class="col-md-4">
                <input v-model="form.hanSuDung" type="date" class="form-control" />
              </div>

              <div class="col-md-12">
                <textarea
                  v-model="form.huongDanSuDung"
                  class="form-control"
                  placeholder="Hướng dẫn sử dụng"
                ></textarea>
              </div>
              <div class="col-md-12">
                <textarea
                  v-model="form.moTaThem"
                  class="form-control"
                  placeholder="Mô tả thêm"
                ></textarea>
              </div>

              <div class="col-md-6">
                <select v-model="form.maDM" class="form-select" required>
                  <option disabled value="">-- Chọn danh mục --</option>
                  <option v-for="dm in danhSachDanhMuc" :key="dm.maDM" :value="dm.maDM">
                    {{ dm.tenDanhMuc }}
                  </option>
                </select>
              </div>

              <div class="col-md-6">
                <select v-model="form.maNCC" class="form-select" required>
                  <option disabled value="">-- Chọn nhà cung cấp --</option>
                  <option v-for="ncc in danhSachNCC" :key="ncc.maNCC" :value="ncc.maNCC">
                    {{ ncc.tenNCC }}
                  </option>
                </select>
              </div>

              <div class="col-md-4">
                <select v-model="form.trangThai" class="form-select">
                  <option :value="1">Hoạt động</option>
                  <option :value="0">Ngừng bán</option>
                </select>
              </div>
            </div>
          </div>

          <div class="modal-footer">
            <button class="btn btn-primary" @click="saveSanPham">Lưu</button>
            <button class="btn btn-secondary" @click="closeModal">Hủy</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

// Dữ liệu mẫu
const danhSachSP = ref([
  {
    maThuoc: 1,
    tenThuoc: 'Paracetamol',
    hoatChat: 'Paracetamol 500mg',
    congDung: 'Hạ sốt, giảm đau',
    donViTinh: 'Hộp',
    donViQuyDoi: 'Viên',
    giaNhap: 15000,
    giaBan: 25000,
    hanSuDung: '2025-12-31',
    ngaySanXuat: '2024-01-01',
    xuatXu: 'Việt Nam',
    moTaThem: '',
    huongDanSuDung: 'Uống sau ăn',
    maDM: 1,
    maNCC: 1,
    trangThai: 1,
  },
])

const danhSachDanhMuc = ref([
  { maDM: 1, tenDanhMuc: 'Thuốc giảm đau' },
  { maDM: 2, tenDanhMuc: 'Kháng sinh' },
])

const danhSachNCC = ref([
  { maNCC: 1, tenNCC: 'Dược A' },
  { maNCC: 2, tenNCC: 'Dược B' },
])

const filter = ref({ tenThuoc: '', hoatChat: '', congDung: '', trangThai: '' })

const showModal = ref(false)
const form = ref({})

// Mở modal
function openModal(sp = null) {
  form.value = sp
    ? { ...sp }
    : {
        maThuoc: null,
        tenThuoc: '',
        hoatChat: '',
        congDung: '',
        donViTinh: '',
        donViQuyDoi: '',
        giaNhap: 0,
        giaBan: 0,
        hanSuDung: '',
        ngaySanXuat: '',
        xuatXu: '',
        moTaThem: '',
        huongDanSuDung: '',
        maDM: '',
        maNCC: '',
        trangThai: 1,
      }
  showModal.value = true
}
function closeModal() {
  showModal.value = false
}

function saveSanPham() {
  if (form.value.maThuoc) {
    const index = danhSachSP.value.findIndex((sp) => sp.maThuoc === form.value.maThuoc)
    danhSachSP.value[index] = { ...form.value }
  } else {
    const newID = Math.max(...danhSachSP.value.map((sp) => sp.maThuoc), 0) + 1
    danhSachSP.value.push({ ...form.value, maThuoc: newID })
  }
  closeModal()
}
function xoaSanPham(id) {
  if (confirm('Xóa thuốc này?'))
    danhSachSP.value = danhSachSP.value.filter((sp) => sp.maThuoc !== id)
}

const danhSachLoc = computed(() => {
  return danhSachSP.value.filter((sp) => {
    const matchTen = sp.tenThuoc.toLowerCase().includes(filter.value.tenThuoc.toLowerCase())
    const matchHoatChat = sp.hoatChat.toLowerCase().includes(filter.value.hoatChat.toLowerCase())
    const matchCongDung = sp.congDung.toLowerCase().includes(filter.value.congDung.toLowerCase())
    const matchTrangThai =
      filter.value.trangThai === '' || sp.trangThai.toString() === filter.value.trangThai
    return matchTen && matchHoatChat && matchCongDung && matchTrangThai
  })
})

const formatCurrency = (val) => val.toLocaleString('vi-VN') + '₫'
function getTenDanhMuc(maDM) {
  const dm = danhSachDanhMuc.value.find((d) => d.maDM === maDM)
  return dm ? dm.tenDanhMuc : ''
}
</script>
