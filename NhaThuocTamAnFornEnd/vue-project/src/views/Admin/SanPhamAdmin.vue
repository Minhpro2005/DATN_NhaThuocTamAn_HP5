<template>
  <div class="container mt-4">
    <h4 class="mb-4 text-success fw-bold"><i class="bi bi-capsule me-2"></i> Quản lý thuốc</h4>

    <!-- Bộ lọc -->
    <div class="card shadow-sm mb-4">
      <div class="card-body">
        <div class="row g-3">
          <div class="col-md-3">
            <input v-model="filter.maThuoc" class="form-control" placeholder="Mã thuốc..." />
          </div>
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
          <div class="col-md-3 d-flex align-items-center">
            <input
              class="form-check-input me-2"
              type="checkbox"
              v-model="filter.daXoa"
              id="showDeleted"
            />
            <label class="form-check-label" for="showDeleted">Hiển thị thuốc đã xóa</label>
          </div>
        </div>
      </div>
    </div>

    <!-- Nút thêm -->
    <div class="mb-3" v-if="!filter.daXoa">
      <button class="btn btn-success" @click="openModal()">➕ Thêm thuốc</button>
    </div>

    <!-- Bảng -->
    <div class="table-responsive shadow-sm border">
      <table class="table table-bordered table-hover text-center bg-white mb-0">
        <thead class="table-success">
          <tr>
            <th>Mã</th>
            <th>Ảnh</th>
            <th>Tên thuốc</th>
            <th>Danh mục</th>
            <th>SL Biến thể</th>
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
                :src="getImageUrl(sp.hinhAnhChinh)"
                width="60"
                height="60"
                class="rounded shadow-sm"
              />
            </td>
            <td>{{ sp.tenThuoc }}</td>
            <td>{{ sp.tenDanhMuc || '-' }}</td>
            <td>{{ sp.soLuongBienThe || 0 }}</td>
            <td>{{ formatCurrency(sp.giaBan) }}</td>
            <td :class="sp.trangThai ? 'text-success' : 'text-danger'">
              {{ sp.trangThai ? 'Hoạt động' : 'Ngừng bán' }}
            </td>
            <td>
              <router-link class="btn btn-sm btn-info me-1" :to="`/admin/thuoc/${sp.maThuoc}`">
                <i class="bi bi-search"></i>
              </router-link>

              <button v-if="!sp.daXoa" class="btn btn-sm btn-warning me-1" @click="openModal(sp)">
                ✏️
              </button>
              <button v-if="!sp.daXoa" class="btn btn-sm btn-danger" @click="xoaThuoc(sp.maThuoc)">
                🗑️
              </button>
              <button
                v-if="sp.daXoa"
                class="btn btn-sm btn-success"
                @click="khoiPhucThuoc(sp.maThuoc)"
              >
                Khôi phục
              </button>
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

              <!-- Đơn vị tính -->
              <div class="col-md-6">
                <select v-model="form.maDVT" class="form-select">
                  <option disabled value="">-- Chọn đơn vị tính --</option>
                  <option v-for="dvt in danhSachDonViTinh" :key="dvt.maDVT" :value="dvt.maDVT">
                    {{ dvt.ten }}
                  </option>
                  <option value="custom">➕ Thêm đơn vị tính mới...</option>
                </select>
                <input
                  v-if="form.maDVT === 'custom'"
                  v-model="tenDVTmoi"
                  class="form-control mt-2"
                  placeholder="Nhập đơn vị tính mới"
                />
              </div>

              <!-- Quy cách -->
              <div class="col-md-6">
                <select v-model="form.maQCDG" class="form-select">
                  <option disabled value="">-- Chọn quy cách --</option>
                  <option v-for="qc in danhSachQuyCach" :key="qc.maQCDG" :value="qc.maQCDG">
                    {{ qc.moTa }}
                  </option>
                  <option value="custom">➕ Thêm quy cách mới...</option>
                </select>
                <input
                  v-if="form.maQCDG === 'custom'"
                  v-model="moTaQCDGmoi"
                  class="form-control mt-2"
                  placeholder="Nhập quy cách mới"
                />
              </div>

              <div class="col-md-6">
                <input v-model="form.dangBaoChe" class="form-control" placeholder="Dạng bào chế" />
              </div>
              <div class="col-md-6">
                <input v-model="form.thanhPhan" class="form-control" placeholder="Thành phần" />
              </div>
              <div class="col-md-6">
                <input v-model="form.xuatXu" class="form-control" placeholder="Xuất xứ" />
              </div>
              <div class="col-md-6">
                <label>Ngày sản xuất</label
                ><input v-model="form.ngaySanXuat" type="date" class="form-control" />
              </div>
              <div class="col-md-6">
                <label>Hạn sử dụng</label
                ><input v-model="form.hanSuDung" type="date" class="form-control" />
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

    <ToastMessage ref="toast" />
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import ToastMessage from '../ToastMessage.vue'

const danhSachSP = ref([])
const danhSachDanhMuc = ref([])
const danhSachDonViTinh = ref([])
const danhSachQuyCach = ref([])

const tenDVTmoi = ref('')
const moTaQCDGmoi = ref('')

const showModal = ref(false)
const form = ref({})
const toast = ref(null)

const filter = ref({
  maThuoc: '',
  keyword: '',
  maDM: '',
  trangThai: null,
  daXoa: false,
})

function fetchThuoc() {
  const raw = { ...filter.value }
  const params = new URLSearchParams()
  Object.entries(raw).forEach(([k, v]) => {
    if (v !== '' && v !== null) params.append(k, v)
  })

  fetch(`http://localhost:8080/api/thuoc/search?${params.toString()}`)
    .then((res) => res.json())
    .then((data) => (danhSachSP.value = data))
}

function fetchDanhMuc() {
  fetch('http://localhost:8080/api/danhmuc')
    .then((res) => res.json())
    .then((data) => (danhSachDanhMuc.value = data))
}

function fetchDonViTinh() {
  fetch('http://localhost:8080/api/donvitinh')
    .then((res) => res.json())
    .then((data) => (danhSachDonViTinh.value = data))
}

function fetchQuyCachDongGoi() {
  fetch('http://localhost:8080/api/quycachdonggoi')
    .then((res) => res.json())
    .then((data) => (danhSachQuyCach.value = data))
}

function openModal(sp = null) {
  form.value = sp
    ? { ...sp }
    : {
        tenThuoc: '',
        congDung: '',
        moTaThem: '',
        huongDanSuDung: '',
        maDVT: '',
        maQCDG: '',
        dangBaoChe: '',
        thanhPhan: '',
        hanSuDung: '',
        ngaySanXuat: '',
        xuatXu: '',
        giaBan: '',
        maDM: '',
        trangThai: true,
      }
  tenDVTmoi.value = ''
  moTaQCDGmoi.value = ''
  showModal.value = true
}

function closeModal() {
  showModal.value = false
  form.value = {}
}

async function luuThuoc() {
  try {
    if (form.value.maDVT === 'custom' && tenDVTmoi.value.trim()) {
      const res = await fetch('http://localhost:8080/api/donvitinh', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ ten: tenDVTmoi.value }),
      })
      const newDVT = await res.json()
      form.value.maDVT = newDVT.maDVT
      await fetchDonViTinh()
    }

    if (form.value.maQCDG === 'custom' && moTaQCDGmoi.value.trim()) {
      const res = await fetch('http://localhost:8080/api/quycachdonggoi', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ moTa: moTaQCDGmoi.value }),
      })
      const newQC = await res.json()
      form.value.maQCDG = newQC.maQCDG
      await fetchQuyCachDongGoi()
    }

    const isUpdate = !!form.value.maThuoc
    const url = isUpdate
      ? `http://localhost:8080/api/thuoc/${form.value.maThuoc}`
      : `http://localhost:8080/api/thuoc`

    await fetch(url, {
      method: isUpdate ? 'PUT' : 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(form.value),
    })

    fetchThuoc()
    closeModal()
    toast.value.show(isUpdate ? '✅ Cập nhật thành công!' : '✅ Thêm mới thành công!', 'success')
  } catch (err) {
    toast.value.show('❌ Lỗi lưu: ' + err.message, 'error')
  }
}

function xoaThuoc(id) {
  if (!confirm('Bạn có chắc chắn muốn xóa thuốc này?')) return
  fetch(`http://localhost:8080/api/thuoc/${id}/xoa-mem`, { method: 'PUT' }).then(() => {
    fetchThuoc()
    toast.value.show('🗑️ Đã chuyển vào thùng rác!', 'success')
  })
}

function khoiPhucThuoc(id) {
  fetch(`http://localhost:8080/api/thuoc/${id}/khoi-phuc`, { method: 'PUT' }).then(() => {
    toast.value.show('♻️ Đã khôi phục thuốc!', 'success')
    filter.value.daXoa = false
  })
}

function getImageUrl(path) {
  return path ? `http://localhost:8080/${path.replace(/^\/+/, '')}` : ''
}

function formatCurrency(val) {
  return Number(val).toLocaleString('vi-VN') + 'đ'
}

watch(filter, fetchThuoc, { deep: true })
onMounted(() => {
  fetchDanhMuc()
  fetchDonViTinh()
  fetchQuyCachDongGoi()
  fetchThuoc()
})
</script>
