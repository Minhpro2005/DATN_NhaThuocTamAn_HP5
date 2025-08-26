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
    <div class="card shadow-sm border-0">
      <div class="card-body p-0">
        <table class="table table-bordered table-hover text-center mb-0 align-middle">
          <thead class="table-success">
            <tr>
              <th>Mã</th>
              <th>Ảnh</th>
              <th class="text-start">Tên thuốc</th>
              <th>Danh mục</th>
              <th>Biến thể</th>
              <th>Giá bán</th>
              <th>Trạng thái</th>
              <th>Hành động</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="sp in danhSachSP" :key="sp.maThuoc">
              <!-- Mã thuốc màu xanh lá -->
              <td class="fw-bold text-success">#{{ sp.maThuoc }}</td>

              <!-- Ảnh: bỏ bo tròn -->
              <td>
                <img
                  v-if="sp.hinhAnhChinh"
                  :src="getImageUrl(sp.hinhAnhChinh)"
                  width="60"
                  height="60"
                  class="shadow-sm"
                />
                <i v-else class="bi bi-capsule fs-3 text-secondary"></i>
              </td>

              <!-- Thông tin -->
              <td class="text-start">{{ sp.tenThuoc }}</td>
              <td>{{ sp.tenDanhMuc || '-' }}</td>
              <td>
                <span class="badge bg-info">{{ sp.soLuongBienThe || 0 }} biến thể</span>
              </td>
              <td class="fw-bold text-success">{{ formatCurrency(sp.giaBan) }}</td>
              <td>
                <span :class="sp.trangThai ? 'badge bg-success' : 'badge bg-danger'">
                  {{ sp.trangThai ? 'Hoạt động' : 'Ngừng bán' }}
                </span>
              </td>

              <!-- Nút hành động -->
              <td>
                <div class="d-flex justify-content-center gap-2">
                  <!-- Thêm biến thể -->
                  <button
                    v-if="!sp.daXoa"
                    class="btn btn-action btn-success"
                    @click="openModalBienThe(sp.maThuoc)"
                    title="Thêm biến thể"
                  >
                    <i class="bi bi-plus-lg"></i>
                  </button>

                  <!-- Xem chi tiết -->
                  <router-link
                    class="btn btn-action btn-info"
                    :to="`/admin/thuoc/${sp.maThuoc}`"
                    title="Xem chi tiết"
                  >
                    <i class="bi bi-search"></i>
                  </router-link>

                  <!-- Sửa -->
                  <button
                    v-if="!sp.daXoa"
                    class="btn btn-action btn-warning"
                    @click="openModal(sp)"
                    title="Sửa"
                  >
                    <i class="bi bi-pencil"></i>
                  </button>

                  <!-- Xóa -->
                  <button
                    v-if="!sp.daXoa"
                    class="btn btn-action btn-danger"
                    @click="xoaThuoc(sp.maThuoc)"
                    title="Xóa"
                  >
                    <i class="bi bi-trash"></i>
                  </button>

                  <!-- Khôi phục -->
                  <button
                    v-if="sp.daXoa"
                    class="btn btn-action btn-secondary"
                    @click="khoiPhucThuoc(sp.maThuoc)"
                    title="Khôi phục"
                  >
                    <i class="bi bi-arrow-counterclockwise"></i>
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Modal thêm/sửa thuốc -->
    <div v-if="showModal" class="modal-backdrop fade show"></div>
    <div class="modal fade show d-block" v-if="showModal" @click.self="closeModal">
      <div class="modal-dialog modal-lg modal-dialog-scrollable">
        <div class="modal-content shadow-lg">
          <div class="modal-header bg-success text-white">
            <h5 class="modal-title">
              <i class="bi bi-capsule me-2"></i>
              {{ form.maThuoc ? 'Sửa thuốc' : 'Thêm thuốc mới' }}
            </h5>
            <button class="btn-close btn-close-white" @click="closeModal"></button>
          </div>

          <div class="modal-body">
            <div class="row g-3">
              <!-- Thông tin cơ bản -->
              <div class="col-12">
                <div class="card shadow-sm border-0 mb-3">
                  <div class="card-header bg-light fw-bold">📌 Thông tin cơ bản</div>
                  <div class="card-body row g-3">
                    <div class="col-md-6">
                      <input
                        v-model="form.tenThuoc"
                        class="form-control"
                        placeholder="Tên thuốc *"
                      />
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
                      <select v-model="form.trangThai" class="form-select">
                        <option :value="true">Hoạt động</option>
                        <option :value="false">Ngừng bán</option>
                      </select>
                    </div>
                  </div>
                </div>
              </div>

              <!-- Thông tin chi tiết -->
              <div class="col-12">
                <div class="card shadow-sm border-0 mb-3">
                  <div class="card-header bg-light fw-bold">💊 Thông tin chi tiết</div>
                  <div class="card-body">
                    <div class="row g-3">
                      <!-- Đơn vị tính -->
                      <div class="col-md-6">
                        <label class="form-label">Đơn vị tính</label>
                        <select v-model="form.maDVT" class="form-select">
                          <option disabled value="">-- Chọn đơn vị tính --</option>
                          <option
                            v-for="dvt in danhSachDonViTinh"
                            :key="dvt.maDVT"
                            :value="dvt.maDVT"
                          >
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
                        <label class="form-label">Quy cách</label>
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

                      <!-- Dạng bào chế -->
                      <div class="col-md-6">
                        <label class="form-label">Dạng bào chế</label>
                        <input
                          v-model="form.dangBaoChe"
                          class="form-control"
                          placeholder="VD: Viên nén"
                        />
                      </div>

                      <!-- Thành phần -->
                      <div class="col-md-6">
                        <label class="form-label">Thành phần</label>
                        <input
                          v-model="form.thanhPhan"
                          class="form-control"
                          placeholder="VD: Paracetamol 500mg"
                        />
                      </div>

                      <!-- Xuất xứ -->
                      <div class="col-md-6">
                        <label class="form-label">Xuất xứ</label>
                        <input
                          v-model="form.xuatXu"
                          class="form-control"
                          placeholder="VD: Việt Nam"
                        />
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              <!-- Thông tin bổ sung -->
              <div class="col-12">
                <div class="card shadow-sm border-0">
                  <div class="card-header bg-light fw-bold">📝 Thông tin bổ sung</div>
                  <div class="card-body row g-3">
                    <div class="col-12">
                      <textarea
                        v-model="form.congDung"
                        class="form-control"
                        placeholder="Công dụng"
                      ></textarea>
                    </div>
                    <div class="col-12">
                      <textarea
                        v-model="form.huongDanSuDung"
                        class="form-control"
                        placeholder="Hướng dẫn sử dụng"
                      ></textarea>
                    </div>
                    <div class="col-12">
                      <textarea
                        v-model="form.moTaThem"
                        class="form-control"
                        placeholder="Mô tả thêm"
                      ></textarea>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- Footer -->
          <div class="modal-footer">
            <button class="btn btn-success" @click="luuThuoc">
              <i class="bi bi-save me-1"></i> Lưu
            </button>
            <button class="btn btn-secondary" @click="closeModal">
              <i class="bi bi-x-circle me-1"></i> Hủy
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Modal thêm biến thể -->
    <div v-if="showModalBienThe" class="modal-backdrop fade show"></div>
    <div class="modal fade show d-block" v-if="showModalBienThe" @click.self="closeModalBienThe">
      <div class="modal-dialog modal-md">
        <div class="modal-content shadow-lg">
          <!-- Header màu xanh lá -->
          <div class="modal-header bg-success text-white">
            <h5 class="modal-title"><i class="bi bi-plus-lg me-2"></i> Thêm biến thể</h5>
            <button class="btn-close btn-close-white" @click="closeModalBienThe"></button>
          </div>

          <!-- Body -->
          <div class="modal-body row g-3">
            <div class="col-12">
              <input
                v-model="formBienThe.tenBienThe"
                class="form-control"
                placeholder="Tên biến thể *"
              />
            </div>
            <div class="col-12">
              <input
                v-model="formBienThe.giaBan"
                type="number"
                class="form-control"
                placeholder="Giá bán *"
              />
            </div>
            <div class="col-12">
              <select v-model="formBienThe.maDVT" class="form-select">
                <option disabled value="">-- Chọn đơn vị tính --</option>
                <option v-for="dvt in danhSachDonViTinh" :key="dvt.maDVT" :value="dvt.maDVT">
                  {{ dvt.ten }}
                </option>
              </select>
            </div>
            <div class="col-12">
              <select v-model="formBienThe.maQCDG" class="form-select">
                <option disabled value="">-- Chọn quy cách --</option>
                <option v-for="qc in danhSachQuyCach" :key="qc.maQCDG" :value="qc.maQCDG">
                  {{ qc.moTa }}
                </option>
              </select>
            </div>
            <div class="col-12">
              <textarea
                v-model="formBienThe.moTa"
                class="form-control"
                placeholder="Mô tả biến thể"
              ></textarea>
            </div>
            <div class="col-12">
              <select v-model="formBienThe.trangThai" class="form-select">
                <option :value="true">Hoạt động</option>
                <option :value="false">Ngừng bán</option>
              </select>
            </div>
            <div class="col-12">
              <input
                type="file"
                accept="image/*"
                class="form-control"
                @change="onFileChangeBienThe"
              />
            </div>
          </div>

          <!-- Footer -->
          <div class="modal-footer">
            <button class="btn btn-success" @click="luuBienThe">
              <i class="bi bi-save me-1"></i> Lưu
            </button>
            <button class="btn btn-secondary" @click="closeModalBienThe">
              <i class="bi bi-x-circle me-1"></i> Hủy
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Toast -->
    <ToastMessage ref="toast" />
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import axios from 'axios'
import ToastMessage from '../ToastMessage.vue'

const danhSachSP = ref([])
const danhSachDanhMuc = ref([])
const danhSachDonViTinh = ref([])
const danhSachQuyCach = ref([])

const tenDVTmoi = ref('')
const moTaQCDGmoi = ref('')

const showModal = ref(false)
const showModalBienThe = ref(false)

const form = ref({})
const formBienThe = ref({})
const fileAnhBienThe = ref(null)

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

/* Modal thuốc */
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
        // hanSuDung: '',
        // ngaySanXuat: '',
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

/* ===== Modal Biến thể ===== */
function openModalBienThe(maThuoc) {
  formBienThe.value = {
    maThuoc,
    tenBienThe: '',
    giaBan: '',
    maDVT: '',
    maQCDG: '',
    moTa: '',
    trangThai: true,
    hinhAnh: '',
  }
  fileAnhBienThe.value = null
  showModalBienThe.value = true
}

function closeModalBienThe() {
  showModalBienThe.value = false
  formBienThe.value = {}
}

function onFileChangeBienThe(e) {
  fileAnhBienThe.value = e.target.files[0]
}

async function luuBienThe() {
  try {
    const formData = new FormData()
    formData.append(
      'data',
      new Blob([JSON.stringify(formBienThe.value)], { type: 'application/json' }),
    )
    if (fileAnhBienThe.value) {
      formData.append('file', fileAnhBienThe.value)
    }

    await axios.post('http://localhost:8080/api/bienthe', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    })

    toast.value.show('✅ Thêm biến thể thành công!', 'success')
    closeModalBienThe()
    fetchThuoc() // cập nhật lại số lượng biến thể
  } catch (err) {
    toast.value.show('❌ Lỗi thêm biến thể!', 'error')
  }
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
