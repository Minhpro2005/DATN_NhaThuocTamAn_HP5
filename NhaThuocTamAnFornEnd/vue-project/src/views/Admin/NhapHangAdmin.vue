<template>
  <div class="container mt-4">
    <h4 class="mb-4 text-success fw-bold">📥 Quản lý Phiếu Nhập</h4>

    <div class="text-end mb-3">
      <button class="btn btn-success" @click="moFormNhap = true">➕ Tạo Phiếu Nhập</button>
    </div>

    <!-- Bảng danh sách -->
    <div class="table-responsive shadow-sm border">
      <table class="table table-bordered table-hover text-center align-middle">
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
          <tr v-for="pn in phieuNhaps" :key="pn.maPN">
            <td>{{ pn.maPN }}</td>
            <td>{{ formatDate(pn.ngayNhap) }}</td>
            <td>{{ pn.tenNV }}</td>
            <td>{{ pn.tenNCC }}</td>
            <td class="text-end text-danger">{{ formatCurrency(pn.tongTien) }}</td>
            <td><button class="btn btn-sm btn-info" @click="xemChiTiet(pn)">Xem</button></td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Modal tạo phiếu nhập -->
    <template v-if="moFormNhap">
      <div class="modal-backdrop fade show"></div>
      <div class="modal fade show d-block" @click.self="moFormNhap = false">
        <div class="modal-dialog modal-lg">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title">📝 Tạo Phiếu Nhập</h5>
              <button class="btn-close" @click="moFormNhap = false"></button>
            </div>

            <div class="modal-body">
              <!-- Thông tin chung -->
              <div class="row g-3 mb-3">
                <div class="col-md-6">
                  <label>Nhân viên:</label>
                  <input class="form-control" :value="currentUser.hoTen" disabled />
                </div>
                <div class="col-md-6">
                  <label>Nhà cung cấp:</label>
                  <select v-model="form.tenNCC" class="form-select">
                    <option disabled value="">-- Chọn nhà cung cấp --</option>
                    <option v-for="ncc in nhaCungCaps" :key="ncc.maNCC" :value="ncc.tenNCC">
                      {{ ncc.tenNCC }}
                    </option>
                  </select>
                </div>
              </div>

              <!-- Thêm chi tiết -->
              <div class="row g-2 mb-3">
                <div class="col-md-4 position-relative">
                  <input
                    v-model="searchThuoc"
                    class="form-control"
                    placeholder="Tìm tên thuốc..."
                    @input="filterThuoc"
                  />
                  <ul
                    class="list-group position-absolute"
                    v-if="suggestions.length"
                    style="z-index: 999; max-height: 200px; overflow-y: auto"
                  >
                    <li
                      v-for="s in suggestions"
                      :key="s.maBienThe"
                      class="list-group-item list-group-item-action"
                      @click="chonThuoc(s)"
                    >
                      {{ s.tenBienThe }}
                    </li>
                  </ul>
                </div>
                <div class="col-md-3">
                  <input
                    v-model.number="thuocNhap.soLuong"
                    type="number"
                    class="form-control"
                    placeholder="Số lượng"
                  />
                </div>
                <div class="col-md-3">
                  <input
                    v-model.number="thuocNhap.donGiaNhap"
                    type="number"
                    class="form-control"
                    placeholder="Đơn giá"
                  />
                </div>
                <div class="col-md-2">
                  <button class="btn btn-primary w-100" @click="themThuoc">Thêm</button>
                </div>
              </div>

              <!-- Danh sách -->
              <table class="table table-bordered">
                <thead>
                  <tr>
                    <th>#</th>
                    <th>Mã Biến Thể</th>
                    <th>Tên Biến Thể</th>
                    <th>SL</th>
                    <th>Đơn giá</th>
                    <th>Thành tiền</th>
                    <th>Xoá</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(t, i) in form.chiTiet" :key="i">
                    <td>{{ i + 1 }}</td>
                    <td>{{ t.maBienThe }}</td>
                    <td>{{ t.tenBienThe }}</td>
                    <td>{{ t.soLuong }}</td>
                    <td class="text-end">{{ formatCurrency(t.donGiaNhap) }}</td>
                    <td class="text-end">{{ formatCurrency(t.soLuong * t.donGiaNhap) }}</td>
                    <td>
                      <button class="btn btn-sm btn-danger" @click="form.chiTiet.splice(i, 1)">
                        Xoá
                      </button>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>

            <div class="modal-footer">
              <button class="btn btn-secondary" @click="moFormNhap = false">Hủy</button>
              <button class="btn btn-success" :disabled="!coTheLuu" @click="luuPhieuNhap">
                Lưu Phiếu Nhập
              </button>
            </div>
          </div>
        </div>
      </div>
    </template>

    <!-- Modal chi tiết -->
    <template v-if="chiTietModalVisible">
      <div class="modal-backdrop fade show"></div>
      <div class="modal fade show d-block" @click.self="closeModal">
        <div class="modal-dialog modal-lg">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title">Chi Tiết Phiếu Nhập #{{ selectedPN.maPN }}</h5>
              <button class="btn-close" @click="closeModal"></button>
            </div>
            <div class="modal-body">
              <p><b>Nhân viên:</b> {{ selectedPN.tenNV }}</p>
              <p><b>Nhà cung cấp:</b> {{ selectedPN.tenNCC }}</p>
              <p><b>Ngày nhập:</b> {{ formatDate(selectedPN.ngayNhap) }}</p>

              <table class="table table-striped">
                <thead>
                  <tr>
                    <th>#</th>
                    <th>Mã Biến Thể</th>
                    <th>Tên Biến Thể</th>
                    <th>SL</th>
                    <th>Đơn giá</th>
                    <th>Thành tiền</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(ct, idx) in selectedPN.chiTiet" :key="idx">
                    <td>{{ idx + 1 }}</td>
                    <td>{{ ct.maBienThe }}</td>
                    <td>{{ ct.tenBienThe }}</td>
                    <td>{{ ct.soLuong }}</td>
                    <td class="text-end">{{ formatCurrency(ct.donGiaNhap) }}</td>
                    <td class="text-end">{{ formatCurrency(ct.soLuong * ct.donGiaNhap) }}</td>
                  </tr>
                </tbody>
              </table>
            </div>
            <div class="modal-footer">
              <button class="btn btn-secondary" @click="closeModal">Đóng</button>
            </div>
          </div>
        </div>
      </div>
    </template>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

const currentUser = ref({ maNV: 1, hoTen: 'Nguyễn Văn A' })
const nhaCungCaps = ref([{ maNCC: 1, tenNCC: 'Dược ABC' }])
const phieuNhaps = ref([])

// Mock dữ liệu kho tồn kho hàng
const khoHang = ref([
  {
    maBienThe: 101,
    tenBienThe: 'Paracetamol 500mg',
    soLuongTon: 100,
    ngayCapNhat: new Date().toISOString(),
  },
  {
    maBienThe: 102,
    tenBienThe: 'Vitamin C 1000mg',
    soLuongTon: 50,
    ngayCapNhat: new Date().toISOString(),
  },
  {
    maBienThe: 103,
    tenBienThe: 'Amoxicillin 250mg',
    soLuongTon: 10,
    ngayCapNhat: new Date().toISOString(),
  },
])

const dsBienTheThuoc = ref([
  { maBienThe: 101, tenBienThe: 'Paracetamol 500mg' },
  { maBienThe: 102, tenBienThe: 'Vitamin C 1000mg' },
  { maBienThe: 103, tenBienThe: 'Amoxicillin 250mg' },
])

const moFormNhap = ref(false)
const chiTietModalVisible = ref(false)
const selectedPN = ref(null)
const form = ref({ tenNCC: '', chiTiet: [] })

const searchThuoc = ref('')
const suggestions = ref([])

const thuocNhap = ref({ maBienThe: '', tenBienThe: '', soLuong: 1, donGiaNhap: 0 })

function filterThuoc() {
  suggestions.value = dsBienTheThuoc.value.filter((bt) =>
    bt.tenBienThe.toLowerCase().includes(searchThuoc.value.toLowerCase()),
  )
}

function chonThuoc(bt) {
  thuocNhap.value.maBienThe = bt.maBienThe
  thuocNhap.value.tenBienThe = bt.tenBienThe
  suggestions.value = []
  searchThuoc.value = bt.tenBienThe
}

function themThuoc() {
  if (thuocNhap.value.maBienThe && thuocNhap.value.soLuong > 0) {
    form.value.chiTiet.push({ ...thuocNhap.value })
    thuocNhap.value = { maBienThe: '', tenBienThe: '', soLuong: 1, donGiaNhap: 0 }
    searchThuoc.value = ''
  }
}

const coTheLuu = computed(() => form.value.tenNCC && form.value.chiTiet.length)

function luuPhieuNhap() {
  const tongTien = form.value.chiTiet.reduce((sum, t) => sum + t.soLuong * t.donGiaNhap, 0)
  const newPN = {
    maPN: Date.now(),
    ngayNhap: new Date().toISOString(),
    tenNV: currentUser.value.hoTen,
    tenNCC: form.value.tenNCC,
    tongTien,
    chiTiet: [...form.value.chiTiet],
  }
  phieuNhaps.value.unshift(newPN)

  // ✅ Cập nhật tồn kho
  form.value.chiTiet.forEach((ct) => {
    const kho = khoHang.value.find((k) => k.maBienThe === ct.maBienThe)
    if (kho) {
      kho.soLuongTon += ct.soLuong
      kho.ngayCapNhat = new Date().toISOString()
    } else {
      khoHang.value.push({
        maBienThe: ct.maBienThe,
        tenBienThe: ct.tenBienThe,
        soLuongTon: ct.soLuong,
        ngayCapNhat: new Date().toISOString(),
      })
    }
  })

  alert('✅ Đã lưu phiếu nhập & cập nhật kho hàng!')
  form.value = { tenNCC: '', chiTiet: [] }
  moFormNhap.value = false
}

function xemChiTiet(pn) {
  selectedPN.value = pn
  chiTietModalVisible.value = true
}

function closeModal() {
  chiTietModalVisible.value = false
}

const formatCurrency = (val) => (val ?? 0).toLocaleString('vi-VN') + ' ₫'
const formatDate = (str) => new Date(str).toLocaleDateString('vi-VN')
</script>
