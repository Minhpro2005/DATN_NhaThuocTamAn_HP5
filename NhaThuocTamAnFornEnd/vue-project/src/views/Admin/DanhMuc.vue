<template>
  <div class="container py-4">
    <h4 class="mb-4 text-success fw-bold">📂 Quản lý danh mục thuốc</h4>

    <!-- Bộ lọc -->
    <div class="card shadow-sm border-0 mb-3">
      <div class="card-body">
        <div class="row g-2">
          <!-- Tìm kiếm theo tên -->
          <div class="col-md-4">
            <input
              v-model="tuKhoa"
              @input="locDanhMuc"
              class="form-control shadow-sm rounded-pill"
              placeholder="🔍 Tìm kiếm tên danh mục..."
            />
          </div>

          <!-- Lọc theo loại -->
          <div class="col-md-3">
            <select
              v-model="filterLoai"
              @change="locDanhMuc"
              class="form-select shadow-sm rounded-pill"
            >
              <option value="">-- Loại (Tất cả) --</option>
              <option value="cha">⭐ Cha</option>
              <option value="con">Con</option>
            </select>
          </div>

          <!-- Lọc theo danh mục cha -->
          <div class="col-md-3">
            <select
              v-model="filterCha"
              @change="locDanhMuc"
              class="form-select shadow-sm rounded-pill"
            >
              <option value="">-- Danh mục cha (Tất cả) --</option>
              <option v-for="dm in danhMucChaList" :key="dm.maDM" :value="dm.maDM">
                {{ dm.tenDanhMuc }}
              </option>
            </select>
          </div>

          <div class="col-md-2 text-end">
            <button class="btn btn-outline-danger rounded-pill px-4 w-100" @click="xoaLoc">
              Xóa lọc
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Nút thêm + toggle -->
    <div class="mb-3 d-flex justify-content-between align-items-center">
      <button class="btn btn-success shadow-sm rounded-pill px-4" @click="openModal()">
        ➕ Thêm danh mục
      </button>
      <button class="btn btn-outline-secondary rounded-pill px-4" @click="toggleDeleted">
        {{ hienThiDaXoa ? '📂 Xem danh mục đang hoạt động' : '🗑️ Xem danh mục đã xóa' }}
      </button>
    </div>

    <!-- Danh sách danh mục -->
    <div class="card shadow-sm border-0">
      <div class="card-body p-0">
        <table class="table table-hover align-middle mb-0">
          <thead class="table-success text-center">
            <tr>
              <th style="width: 80px">Mã DM</th>
              <th style="width: 260px" class="text-start">Tên danh mục</th>
              <th style="width: 110px">Loại</th>
              <th style="width: 200px">Danh mục cha</th>
              <th style="width: 150px">Số DM con</th>
              <th>Mô tả</th>
              <th style="width: 140px">Hành động</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="dm in danhMucTrang" :key="dm.maDM" :class="{ 'table-light': !dm.maDMCha }">
              <td class="fw-bold text-success text-center">#{{ dm.maDM }}</td>

              <!-- Tên: thụt lề nếu là con -->
              <td class="text-start">
                <span
                  v-if="dm.maDMCha"
                  class="text-secondary ms-4 d-inline-flex align-items-center"
                >
                  <i class="bi bi-arrow-return-right me-1"></i>
                  {{ dm.tenDanhMuc }}
                </span>
                <span v-else class="fw-bold">{{ dm.tenDanhMuc }}</span>
              </td>

              <td class="text-center">
                <span v-if="!dm.maDMCha" class="badge bg-success">⭐ Cha</span>
                <span v-else class="badge bg-secondary">Con</span>
              </td>

              <td class="text-center">
                <span v-if="dm.maDMCha">
                  {{ danhMucChaList.find((c) => c.maDM === dm.maDMCha)?.tenDanhMuc || '' }}
                </span>
                <span v-else>-</span>
              </td>

              <!-- Cột số lượng con -->
              <td class="text-center">
                <span v-if="!dm.maDMCha" class="badge rounded-pill bg-info">
                  {{ childCountByParent[dm.maDM] || 0 }}
                </span>
                <span v-else class="text-muted">-</span>
              </td>

              <td class="text-muted">{{ dm.moTa }}</td>

              <td class="text-center">
                <div class="d-flex justify-content-center gap-2">
                  <!-- Nếu đang ở chế độ hoạt động -->
                  <template v-if="!hienThiDaXoa">
                    <button
                      class="btn btn-warning btn-sm square-btn"
                      @click="openModal(dm)"
                      title="Chỉnh sửa"
                    >
                      <i class="bi bi-pencil"></i>
                    </button>
                    <button
                      class="btn btn-danger btn-sm square-btn"
                      @click="xoaDanhMuc(dm.maDM)"
                      title="Ẩn danh mục"
                    >
                      <i class="bi bi-trash"></i>
                    </button>
                  </template>

                  <!-- Nếu đang xem danh mục đã xóa -->
                  <template v-else>
                    <button
                      class="btn btn-success btn-sm square-btn"
                      @click="khoiPhucDanhMuc(dm.maDM)"
                      title="Khôi phục"
                    >
                      <i class="bi bi-arrow-counterclockwise"></i>
                    </button>
                  </template>
                </div>
              </td>
            </tr>
            <tr v-if="danhSachLoc.length === 0">
              <td colspan="7" class="text-muted text-center">Không có danh mục nào.</td>
            </tr>
          </tbody>
        </table>

        <!-- Phân trang -->
        <Pagination
          :current-page="currentPage"
          :total-pages="totalPages"
          @change-page="changePage"
        />
      </div>
    </div>

    <!-- Modal thêm/sửa -->
    <div
      class="modal fade"
      :class="{ show: showModal }"
      :style="showModal ? 'display:block' : ''"
      @click.self="closeModal"
    >
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content shadow-lg border-0">
          <div class="modal-header bg-success text-white">
            <h5 class="modal-title">{{ form.maDM ? '✏ Sửa' : '➕ Thêm' }} danh mục</h5>
            <button class="btn-close btn-close-white" @click="closeModal"></button>
          </div>
          <div class="modal-body">
            <div class="mb-3">
              <label class="form-label fw-bold">Tên danh mục</label>
              <input
                v-model="form.tenDanhMuc"
                type="text"
                class="form-control shadow-sm"
                placeholder="Nhập tên danh mục..."
              />
            </div>
            <div class="mb-3">
              <label class="form-label fw-bold">Mô tả</label>
              <textarea
                v-model="form.moTa"
                class="form-control shadow-sm"
                placeholder="Nhập mô tả..."
              ></textarea>
            </div>

            <!-- Checkbox "Đặt làm danh mục cha" -->
            <div class="form-check mb-3">
              <input
                class="form-check-input"
                type="checkbox"
                id="isParent"
                v-model="form.isParent"
              />
              <label class="form-check-label fw-bold" for="isParent">Đặt làm danh mục cha</label>
            </div>

            <!-- Dropdown chọn cha -->
            <div class="mb-3">
              <label class="form-label fw-bold">Danh mục cha</label>
              <select
                v-model="form.maDMCha"
                class="form-select shadow-sm"
                :disabled="form.isParent"
              >
                <option :value="null">-- Không có (danh mục gốc) --</option>
                <option
                  v-for="dm in danhMucChaList"
                  :key="dm.maDM"
                  :value="dm.maDM"
                  :disabled="form.maDM === dm.maDM"
                >
                  {{ dm.tenDanhMuc }}
                </option>
              </select>
            </div>
          </div>
          <div class="modal-footer">
            <button class="btn btn-secondary" @click="closeModal">Hủy</button>
            <button class="btn btn-success px-4" @click="luuDanhMuc">💾 Lưu</button>
          </div>
        </div>
      </div>
    </div>
    <div v-if="showModal" class="modal-backdrop fade show"></div>

    <!-- Toast -->
    <ToastMessage ref="toastRef" />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'
import ToastMessage from '../ToastMessage.vue'
import Pagination from '../Pagination.vue'

const danhSachDanhMuc = ref([])
const danhMucChaList = ref([])
const tuKhoa = ref('')
const filterLoai = ref('')
const filterCha = ref('')
const showModal = ref(false)
const form = ref({})
const toastRef = ref(null)
const hienThiDaXoa = ref(false)

const currentPage = ref(1)
const pageSize = 5

// ✅ Đếm số con theo cha
const childCountByParent = computed(() => {
  const map = {}
  for (const dm of danhSachDanhMuc.value) {
    if (dm.maDMCha) {
      map[dm.maDMCha] = (map[dm.maDMCha] || 0) + 1
    }
  }
  return map
})

// ✅ Danh sách đã sắp xếp
const danhSachThuTu = computed(() => {
  const items = [...danhSachDanhMuc.value]
  const parents = items.filter((x) => !x.maDMCha)
  const byParent = items.reduce((acc, cur) => {
    if (cur.maDMCha) {
      ;(acc[cur.maDMCha] = acc[cur.maDMCha] || []).push(cur)
    }
    return acc
  }, {})

  parents.sort((a, b) => a.tenDanhMuc.localeCompare(b.tenDanhMuc, 'vi'))
  for (const key in byParent) {
    byParent[key].sort((a, b) => a.tenDanhMuc.localeCompare(b.tenDanhMuc, 'vi'))
  }

  const flatten = []
  for (const p of parents) {
    flatten.push(p)
    if (byParent[p.maDM]) flatten.push(...byParent[p.maDM])
  }

  const orphanChildren = items.filter(
    (x) => x.maDMCha && !parents.find((p) => p.maDM === x.maDMCha),
  )
  if (orphanChildren.length) flatten.push(...orphanChildren)

  return flatten
})

// ✅ Lọc dữ liệu theo bộ lọc
const danhSachLoc = computed(() => {
  let ds = [...danhSachThuTu.value]
  if (tuKhoa.value.trim()) {
    ds = ds.filter((x) => x.tenDanhMuc.toLowerCase().includes(tuKhoa.value.toLowerCase()))
  }
  if (filterLoai.value === 'cha') {
    ds = ds.filter((x) => !x.maDMCha)
  } else if (filterLoai.value === 'con') {
    ds = ds.filter((x) => x.maDMCha)
  }
  if (filterCha.value) {
    ds = ds.filter((x) => x.maDMCha === filterCha.value)
  }
  return ds
})

// ✅ Phân trang
const totalPages = computed(() => Math.ceil(danhSachLoc.value.length / pageSize) || 1)
const danhMucTrang = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  return danhSachLoc.value.slice(start, start + pageSize)
})
const changePage = (page) => (currentPage.value = page)

// ✅ Tải danh mục
async function loadDanhMuc() {
  try {
    const url = hienThiDaXoa.value
      ? 'http://localhost:8080/api/danhmuc/deleted'
      : 'http://localhost:8080/api/danhmuc'
    const res = await axios.get(url)
    danhSachDanhMuc.value = res.data
    currentPage.value = 1
    await loadDanhMucCha()
  } catch (err) {
    toastRef.value?.show('❌ Lỗi khi tải danh mục.', 'error')
  }
}

// ✅ Tải danh mục cha
async function loadDanhMucCha() {
  try {
    const res = await axios.get('http://localhost:8080/api/danhmuc')
    danhMucChaList.value = res.data.filter((dm) => !dm.maDMCha)
  } catch (err) {
    toastRef.value?.show('❌ Lỗi khi tải danh mục cha.', 'error')
  }
}

function toggleDeleted() {
  hienThiDaXoa.value = !hienThiDaXoa.value
  loadDanhMuc()
}

function locDanhMuc() {
  currentPage.value = 1
}

function xoaLoc() {
  tuKhoa.value = ''
  filterLoai.value = ''
  filterCha.value = ''
  loadDanhMuc()
}

// ✅ modal
function openModal(dm = null) {
  form.value = dm
    ? { ...dm, isParent: !dm.maDMCha }
    : { maDM: null, tenDanhMuc: '', moTa: '', maDMCha: null, isParent: false }
  loadDanhMucCha()
  showModal.value = true
}
function closeModal() {
  showModal.value = false
}

// ✅ lưu
async function luuDanhMuc() {
  if (!form.value.tenDanhMuc?.trim()) {
    toastRef.value?.show('❌ Tên danh mục không được để trống.', 'error')
    return
  }
  if (form.value.isParent) form.value.maDMCha = null

  try {
    if (form.value.maDM) {
      await axios.put(`http://localhost:8080/api/danhmuc/${form.value.maDM}`, form.value)
      toastRef.value?.show('✅ Cập nhật danh mục thành công!', 'success')
    } else {
      await axios.post('http://localhost:8080/api/danhmuc', form.value)
      toastRef.value?.show('✅ Thêm danh mục thành công!', 'success')
    }
    await loadDanhMuc()
    closeModal()
  } catch (e) {
    toastRef.value?.show('❌ Lỗi khi lưu danh mục.', 'error')
  }
}

// ✅ xoá mềm
async function xoaDanhMuc(id) {
  if (!confirm('Bạn có chắc chắn muốn ẩn danh mục này?')) return
  try {
    await axios.delete(`http://localhost:8080/api/danhmuc/${id}`)
    await loadDanhMuc()
    toastRef.value?.show('🚫 Ẩn danh mục thành công.', 'success')
  } catch (e) {
    toastRef.value?.show('❌ Không thể ẩn danh mục.', 'error')
  }
}

// ✅ khôi phục
async function khoiPhucDanhMuc(id) {
  if (!confirm('Bạn có chắc chắn muốn khôi phục danh mục này?')) return
  try {
    await axios.put(`http://localhost:8080/api/danhmuc/restore/${id}`)
    await loadDanhMuc()
    toastRef.value?.show('✅ Khôi phục danh mục thành công.', 'success')
  } catch (e) {
    toastRef.value?.show('❌ Không thể khôi phục danh mục.', 'error')
  }
}

onMounted(loadDanhMuc)
</script>

<style scoped>
.square-btn {
  width: 34px;
  height: 34px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}
</style>
