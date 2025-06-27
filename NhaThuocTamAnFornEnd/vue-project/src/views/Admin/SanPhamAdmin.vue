<template>
  <div class="container mt-4">
    <h4 class="mb-4 text-success fw-bold"><i class="bi bi-capsule me-2"></i> Quản lý thuốc</h4>

    <!-- Bộ lọc -->
    <div class="card shadow-sm mb-4">
      <div class="card-body">
        <div class="row g-3">
          <div class="col-md-3">
            <input v-model="filter.keyword" class="form-control" placeholder="Tên thuốc..." />
          </div>
          <div class="col-md-3">
            <select v-model="filter.maDM" class="form-select">
              <option value="">-- Danh mục --</option>
              <option v-for="dm in danhSachDanhMuc" :key="dm.maDM" :value="dm.maDM">
                {{ dm.tenDanhMuc }}
              </option>
            </select>
          </div>
          <div class="col-md-3">
            <select v-model="filter.trangThai" class="form-select">
              <option value="">-- Trạng thái --</option>
              <option :value="true">Hoạt động</option>
              <option :value="false">Ngừng bán</option>
            </select>
          </div>
          <div class="col-md-3">
            <select v-model="filter.maNCC" class="form-select">
              <option value="">-- Nhà cung cấp --</option>
              <option v-for="ncc in danhSachNCC" :key="ncc.maNCC" :value="ncc.maNCC">
                {{ ncc.tenNCC }}
              </option>
            </select>
          </div>
        </div>
      </div>
    </div>

    <!-- Nút thêm -->
    <div class="mb-3">
      <button class="btn btn-success" @click="openModal()">➕ Thêm thuốc</button>
    </div>

    <!-- Bảng hiển thị -->
    <div class="table-responsive shadow-sm border">
      <table class="table table-bordered table-hover text-center bg-white mb-0">
        <thead class="table-success">
          <tr>
            <th>Mã</th>
            <th>Ảnh</th>
            <th>Tên thuốc</th>
            <th>Danh mục</th>
            <th>Nhà cung cấp</th>
            <th>Giá bán</th>
            <th>Trạng thái</th>
            <th>Hành động</th>
          </tr>
        </thead>

        <tbody>
          <tr v-for="sp in danhSachSP" :key="sp.maThuoc">
            <td>{{ sp.maThuoc }}</td>
            <td>
              <img
                v-if="sp.hinhAnhChinh"
                :src="getFullImageUrl(sp.hinhAnhChinh)"
                width="60"
                height="60"
                class="rounded shadow-sm"
              />
            </td>
            <td>{{ sp.tenThuoc }}</td>
            <td>{{ sp.tenDanhMuc || '-' }}</td>
            <td>{{ sp.tenNhaCungCap || '-' }}</td>
            <td>{{ formatCurrency(sp.giaBan) }}</td>
            <td :class="sp.trangThai ? 'text-success' : 'text-danger'">
              {{ sp.trangThai ? 'Hoạt động' : 'Ngừng bán' }}
            </td>
            <td>
              <button class="btn btn-sm btn-info me-1" @click="moChiTiet(sp)">Xem</button>
              <button class="btn btn-sm btn-warning me-1" @click="openModal(sp)">Sửa</button>
              <button class="btn btn-sm btn-danger" @click="xoaThuoc(sp.maThuoc)">Xóa</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Modal Thêm/Sửa -->
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
              <div class="col-md-6">
                <input v-model="form.tenThuoc" class="form-control" placeholder="Tên thuốc *" />
              </div>
              <div class="col-md-6">
                <input
                  v-model="form.giaBan"
                  type="number"
                  class="form-control"
                  placeholder="Giá bán *"
                />
              </div>
              <div class="col-md-6">
                <select v-model="form.maDM" class="form-select">
                  <option disabled value="">-- Chọn danh mục --</option>
                  <option v-for="dm in danhSachDanhMuc" :key="dm.maDM" :value="dm.maDM">
                    {{ dm.tenDanhMuc }}
                  </option>
                </select>
              </div>
              <div class="col-md-6">
                <select v-model="form.maNCC" class="form-select">
                  <option disabled value="">-- Chọn nhà cung cấp --</option>
                  <option v-for="ncc in danhSachNCC" :key="ncc.maNCC" :value="ncc.maNCC">
                    {{ ncc.tenNCC }}
                  </option>
                </select>
              </div>
              <div class="col-md-6">
                <input v-model="form.donViTinh" class="form-control" placeholder="Đơn vị tính" />
              </div>
              <div class="col-md-6">
                <input v-model="form.dangBaoChe" class="form-control" placeholder="Dạng bào chế" />
              </div>
              <div class="col-md-6">
                <input
                  v-model="form.quyCach"
                  class="form-control"
                  placeholder="Quy cách đóng gói"
                />
              </div>
              <div class="col-md-6">
                <input v-model="form.thanhPhan" class="form-control" placeholder="Thành phần" />
              </div>
              <div class="col-md-6">
                <input v-model="form.xuatXu" class="form-control" placeholder="Xuất xứ" />
              </div>
              <div class="col-md-6">
                <label class="form-label">Ngày sản xuất</label>
                <input v-model="form.ngaySanXuat" type="date" class="form-control" />
              </div>
              <div class="col-md-6">
                <label class="form-label">Hạn sử dụng</label>
                <input v-model="form.hanSuDung" type="date" class="form-control" />
              </div>
              <div class="col-md-12">
                <textarea
                  v-model="form.congDung"
                  class="form-control"
                  placeholder="Công dụng"
                ></textarea>
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
                <select v-model="form.trangThai" class="form-select">
                  <option :value="true">Hoạt động</option>
                  <option :value="false">Ngừng bán</option>
                </select>
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <button class="btn btn-primary" @click="luuThuoc">Lưu</button>
            <button class="btn btn-secondary" @click="closeModal">Hủy</button>
          </div>
        </div>
      </div>
    </div>

    <ThuocChiTiet :thuoc="chiTietThuoc" :show="showChiTiet" @close="showChiTiet = false" />
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import ThuocChiTiet from './ThuocChiTiet.vue'
import { useRouter } from 'vue-router'
const router = useRouter()

const danhSachSP = ref([])
const danhSachDanhMuc = ref([])
const danhSachNCC = ref([])
const filter = ref({ keyword: '', maDM: '', trangThai: '', maNCC: '' })
const form = ref({})
const showModal = ref(false)
const showChiTiet = ref(false)
const chiTietThuoc = ref({})

function fetchThuoc() {
  const params = new URLSearchParams(filter.value).toString()
  fetch(`http://localhost:8080/api/thuoc/search?${params}`)
    .then((res) => res.json())
    .then((data) => (danhSachSP.value = data))
}

function fetchDanhMuc() {
  fetch('http://localhost:8080/api/danhmuc')
    .then((res) => res.json())
    .then((data) => (danhSachDanhMuc.value = data))
}

function fetchNCC() {
  fetch('http://localhost:8080/api/nhacungcap')
    .then((res) => res.json())
    .then((data) => (danhSachNCC.value = data))
}

function formatCurrency(value) {
  if (!value) return '0đ'
  return Number(value).toLocaleString('vi-VN') + 'đ'
}

function getFullImageUrl(path) {
  if (!path) return ''
  return `http://localhost:8080/${path.startsWith('/') ? path.slice(1) : path}`
}

function moChiTiet(sp) {
  chiTietThuoc.value = { ...sp }
  showChiTiet.value = true
}

function openModal(sp = null) {
  if (sp) {
    form.value = { ...sp }
  } else {
    form.value = {
      tenThuoc: '',
      congDung: '',
      moTaThem: '',
      huongDanSuDung: '',
      donViTinh: '',
      dangBaoChe: '',
      quyCach: '',
      thanhPhan: '',
      hanSuDung: '',
      ngaySanXuat: '',
      xuatXu: '',
      giaBan: '',
      maDM: '',
      maNCC: '',
      trangThai: true,
    }
  }
  showModal.value = true
}

function closeModal() {
  showModal.value = false
}

function luuThuoc() {
  const isUpdate = !!form.value.maThuoc
  const url = isUpdate
    ? `http://localhost:8080/api/thuoc/${form.value.maThuoc}`
    : `http://localhost:8080/api/thuoc`

  fetch(url, {
    method: isUpdate ? 'PUT' : 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(form.value),
  })
    .then((res) => {
      if (!res.ok) throw new Error('Lỗi server')
      return res.json()
    })
    .then(() => {
      fetchThuoc()
      closeModal()
    })
    .catch((err) => {
      alert('❌ Lỗi lưu thuốc: ' + err.message)
    })
}

function xoaThuoc(id) {
  if (confirm('Bạn có chắc muốn xóa thuốc này?')) {
    fetch(`http://localhost:8080/api/thuoc/${id}`, { method: 'DELETE' }).then(() => fetchThuoc())
  }
}

watch(filter, fetchThuoc, { deep: true })

onMounted(() => {
  fetchThuoc()
  fetchDanhMuc()
  fetchNCC()
})
</script>
