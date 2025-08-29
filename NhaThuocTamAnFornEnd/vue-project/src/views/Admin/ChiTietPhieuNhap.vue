<template>
  <div class="container py-4">
    <h4 class="mb-3 text-success fw-bold">📦 Quản lý kho hàng</h4>

    <!-- Cards thống kê -->
    <div class="row g-2 mb-3">
      <div class="col-6 col-md-3">
        <div class="card shadow-sm bg-success text-white stat-card">
          <div class="card-body text-center p-2">
            <i class="bi bi-check-circle fs-4"></i>
            <div class="small">Lô còn hạn</div>
            <div class="fw-bold">{{ validList.length }}</div>
          </div>
        </div>
      </div>
      <div class="col-6 col-md-3">
        <div class="card shadow-sm bg-warning text-dark stat-card">
          <div class="card-body text-center p-2">
            <i class="bi bi-hourglass-split fs-4"></i>
            <div class="small">Sắp hết hạn</div>
            <div class="fw-bold">{{ soonExpireList.length }}</div>
          </div>
        </div>
      </div>
      <div class="col-6 col-md-3">
        <div class="card shadow-sm bg-danger text-white stat-card">
          <div class="card-body text-center p-2">
            <i class="bi bi-x-octagon fs-4"></i>
            <div class="small">Đã hết hạn</div>
            <div class="fw-bold">{{ expiredList.length }}</div>
          </div>
        </div>
      </div>
      <div class="col-6 col-md-3">
        <div class="card shadow-sm bg-info text-white stat-card">
          <div class="card-body text-center p-2">
            <i class="bi bi-box-seam fs-4"></i>
            <div class="small">Tổng tất cả</div>
            <div class="fw-bold">{{ allKhoList.length }}</div>
          </div>
        </div>
      </div>
    </div>

    <!-- Tabs -->
    <ul class="nav nav-tabs mb-3">
      <li class="nav-item">
        <button
          class="nav-link"
          :class="{ active: activeTab === 'all' }"
          @click="activeTab = 'all'"
        >
          Tất cả
          <span class="badge rounded-pill text-bg-secondary ms-1">{{
            filteredKhoList.length
          }}</span>
        </button>
      </li>
      <li class="nav-item">
        <button
          class="nav-link"
          :class="{ active: activeTab === 'soon' }"
          @click="activeTab = 'soon'"
        >
          Lô sắp hết hạn (≤90 ngày)
          <span class="badge rounded-pill text-bg-warning ms-1">{{ soonExpireList.length }}</span>
        </button>
      </li>
      <li class="nav-item">
        <button
          class="nav-link"
          :class="{ active: activeTab === 'expired' }"
          @click="activeTab = 'expired'"
        >
          Lô đã hết hạn
          <span class="badge rounded-pill text-bg-danger ms-1">{{ expiredList.length }}</span>
        </button>
      </li>
    </ul>

    <!-- Bộ lọc (chỉ áp dụng cho tab Tất cả) -->
    <div v-if="activeTab === 'all'" class="row g-2 mb-2">
      <div class="col-md-4">
        <input
          v-model="keyword"
          @input="timKiem"
          type="text"
          class="form-control"
          placeholder="🔍 Tìm theo mã, tên thuốc"
        />
      </div>
      <div class="col-md-3">
        <select v-model="filterLoai" class="form-select" @change="timKiem">
          <option value="">-- Tất cả loại --</option>
          <option value="thuoc">Thuốc</option>
          <option value="bienthe">Biến thể</option>
        </select>
      </div>
      <div class="col-md-3">
        <select v-model="filterTrangThai" class="form-select" @change="timKiem">
          <option value="">-- Tất cả trạng thái --</option>
          <option value="0">✅ Bình thường</option>
          <option value="1">⚠️ Sắp hết</option>
          <option value="2">❌ Hết hàng</option>
          <option value="3">⏳ Sắp hết hạn</option>
          <option value="4">☠️ Hết hạn</option>
        </select>
      </div>
      <div class="col-md-2 text-end">
        <button class="btn btn-outline-secondary w-100" @click="resetSearch">Xóa</button>
      </div>
    </div>

    <!-- Bảng kho hàng -->
    <table class="table table-bordered table-striped align-middle">
      <thead class="table-success text-center">
        <tr>
          <th>Mã kho</th>
          <th>Sản phẩm</th>
          <th>Đơn vị tính</th>
          <th>Số lượng tồn</th>
          <th>Trạng thái</th>
          <th>Ngày SX</th>
          <th>Hạn sử dụng</th>
          <th>Ngày cập nhật</th>
        </tr>
      </thead>

      <!-- Body cho tab Tất cả -->
      <tbody v-if="activeTab === 'all'">
        <tr v-for="k in paginatedKhoList" :key="k.maKhoHang">
          <td class="text-center">{{ k.maKhoHang }}</td>
          <td>
            <div class="fw-bold">
              {{ getTenSanPham(k) }}
              <span v-if="k.moTaQuyCach" class="fw-bold"> - {{ k.moTaQuyCach }}</span>
            </div>
            <div class="text-muted small">
              {{ k.maBienThe ? `Mã biến thể: ${k.maBienThe}` : `Mã thuốc: ${k.maThuoc}` }}
            </div>
          </td>
          <td class="text-center">{{ k.tenDonViTinh || '' }}</td>
          <td class="text-end fw-bold" :class="{ 'text-danger': k.soLuongTon <= 10 }">
            {{ k.soLuongTon }}
          </td>
          <td class="text-center">
            <span class="badge badge-pill badge-lg" :class="getTrangThaiClass(k.trangThai)">
              {{ getTrangThai(k.trangThai) }}
            </span>
          </td>
          <td class="text-center">{{ formatDate(k.ngaySanXuat) }}</td>
          <td class="text-center">{{ formatDate(k.hanSuDung) }}</td>
          <td class="text-end">{{ formatDateTime(k.ngayCapNhat) }}</td>
        </tr>
        <tr v-if="paginatedKhoList.length === 0">
          <td colspan="8" class="text-center text-muted">Không có dữ liệu</td>
        </tr>
      </tbody>

      <!-- Body cho tab Sắp hết hạn -->
      <tbody v-else-if="activeTab === 'soon'">
        <tr v-for="k in paginatedSoonList" :key="k.maKhoHang">
          <td class="text-center">{{ k.maKhoHang }}</td>
          <td>
            <div class="fw-bold">
              {{ getTenSanPham(k) }}
              <span v-if="k.moTaQuyCach" class="fw-bold"> - {{ k.moTaQuyCach }}</span>
            </div>
            <div class="text-muted small">
              {{ k.maBienThe ? `Mã biến thể: ${k.maBienThe}` : `Mã thuốc: ${k.maThuoc}` }}
            </div>
          </td>
          <td class="text-center">{{ k.tenDonViTinh || '' }}</td>
          <td class="text-end fw-bold text-danger">{{ k.soLuongTon }}</td>
          <td class="text-center">
            <span class="badge badge-pill badge-lg bg-warning text-dark">⏳ Sắp hết hạn</span>
          </td>
          <td class="text-center">{{ formatDate(k.ngaySanXuat) }}</td>
          <td class="text-center text-danger fw-semibold">{{ formatDate(k.hanSuDung) }}</td>
          <td class="text-end">{{ formatDateTime(k.ngayCapNhat) }}</td>
        </tr>
        <tr v-if="paginatedSoonList.length === 0">
          <td colspan="8" class="text-center text-muted">Không có lô sắp hết hạn</td>
        </tr>
      </tbody>

      <!-- Body cho tab Đã hết hạn -->
      <tbody v-else>
        <tr v-for="k in paginatedExpiredList" :key="k.maKhoHang">
          <td class="text-center">{{ k.maKhoHang }}</td>
          <td>
            <div class="fw-bold">
              {{ getTenSanPham(k) }}
              <span v-if="k.moTaQuyCach" class="fw-bold"> - {{ k.moTaQuyCach }}</span>
            </div>
            <div class="text-muted small">
              {{ k.maBienThe ? `Mã biến thể: ${k.maBienThe}` : `Mã thuốc: ${k.maThuoc}` }}
            </div>
          </td>
          <td class="text-center">{{ k.tenDonViTinh || '' }}</td>
          <td class="text-end fw-bold text-danger">{{ k.soLuongTon }}</td>
          <td class="text-center">
            <span class="badge badge-pill badge-lg bg-secondary text-white">☠️ Hết hạn</span>
          </td>
          <td class="text-center">{{ formatDate(k.ngaySanXuat) }}</td>
          <td class="text-center text-danger fw-semibold">{{ formatDate(k.hanSuDung) }}</td>
          <td class="text-end">{{ formatDateTime(k.ngayCapNhat) }}</td>
        </tr>
        <tr v-if="paginatedExpiredList.length === 0">
          <td colspan="8" class="text-center text-muted">Không có lô đã hết hạn</td>
        </tr>
      </tbody>
    </table>

    <!-- Phân trang -->
    <Pagination
      v-if="activeTab === 'all' && totalPages > 1"
      :current-page="currentPage + 1"
      :total-pages="totalPages"
      @change-page="(page) => changePage(page - 1)"
    />
    <Pagination
      v-if="activeTab === 'soon' && totalPagesSoon > 1"
      :current-page="currentPageSoon + 1"
      :total-pages="totalPagesSoon"
      @change-page="(page) => changePageSoon(page - 1)"
    />
    <Pagination
      v-if="activeTab === 'expired' && totalPagesExpired > 1"
      :current-page="currentPageExpired + 1"
      :total-pages="totalPagesExpired"
      @change-page="(page) => changePageExpired(page - 1)"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import axios from 'axios'
import Pagination from '../Pagination.vue'

const allKhoList = ref([])
const keyword = ref('')
const filterTrangThai = ref('')
const filterLoai = ref('')
const activeTab = ref('all')

const currentPage = ref(0)
const currentPageSoon = ref(0)
const currentPageExpired = ref(0)
const pageSize = 5

const SOON_DAYS = 90

const loadKho = async () => {
  const res = await axios.get('http://localhost:8080/api/kho-hang')
  allKhoList.value = res.data
}

const timKiem = () => (currentPage.value = 0)
const resetSearch = () => {
  keyword.value = ''
  filterTrangThai.value = ''
  filterLoai.value = ''
  currentPage.value = 0
}

const getTenSanPham = (k) => (k.tenBienThe?.trim() ? k.tenBienThe : k.tenThuoc)

const isSoonExpire = (k) =>
  k.hanSuDung &&
  new Date(k.hanSuDung).getTime() > Date.now() &&
  new Date(k.hanSuDung).getTime() <= Date.now() + SOON_DAYS * 86400000

const isExpired = (k) => k.hanSuDung && new Date(k.hanSuDung).getTime() < Date.now()

const soonExpireList = computed(() =>
  allKhoList.value
    .filter(isSoonExpire)
    .sort((a, b) => new Date(a.hanSuDung || 0) - new Date(b.hanSuDung || 0)),
)

const expiredList = computed(() =>
  allKhoList.value
    .filter(isExpired)
    .sort((a, b) => new Date(a.hanSuDung || 0) - new Date(b.hanSuDung || 0)),
)

const validList = computed(() => allKhoList.value.filter((k) => !isExpired(k) && !isSoonExpire(k)))

const filteredKhoList = computed(() => {
  let list = [...validList.value] // ✅ chỉ lấy thuốc còn hạn

  // Lọc theo từ khóa
  if (keyword.value.trim()) {
    const kw = keyword.value.toLowerCase()
    list = list.filter(
      (k) =>
        k.maKhoHang.toString().includes(kw) ||
        k.maThuoc?.toString().includes(kw) ||
        k.maBienThe?.toString().includes(kw) ||
        k.tenThuoc?.toLowerCase().includes(kw) ||
        k.tenBienThe?.toLowerCase().includes(kw),
    )
  }

  // Lọc theo loại
  if (filterLoai.value === 'thuoc') list = list.filter((k) => !k.maBienThe)
  if (filterLoai.value === 'bienthe') list = list.filter((k) => !!k.maBienThe)

  // Lọc theo trạng thái
  if (filterTrangThai.value !== '') {
    list = list.filter((k) => k.trangThai.toString() === filterTrangThai.value)
  }

  return list.sort((a, b) => new Date(b.ngayCapNhat).getTime() - new Date(a.ngayCapNhat).getTime())
})

// Phân trang tab Tất cả
const totalPages = computed(() => Math.ceil(filteredKhoList.value.length / pageSize))
const paginatedKhoList = computed(() => {
  const start = currentPage.value * pageSize
  return filteredKhoList.value.slice(start, start + pageSize)
})
const changePage = (page) => {
  if (page >= 0 && page < totalPages.value) currentPage.value = page
}

// Phân trang tab Sắp hết hạn
const totalPagesSoon = computed(() => Math.ceil(soonExpireList.value.length / pageSize))
const paginatedSoonList = computed(() => {
  const start = currentPageSoon.value * pageSize
  return soonExpireList.value.slice(start, start + pageSize)
})
const changePageSoon = (page) => {
  if (page >= 0 && page < totalPagesSoon.value) currentPageSoon.value = page
}

// Phân trang tab Đã hết hạn
const totalPagesExpired = computed(() => Math.ceil(expiredList.value.length / pageSize))
const paginatedExpiredList = computed(() => {
  const start = currentPageExpired.value * pageSize
  return expiredList.value.slice(start, start + pageSize)
})
const changePageExpired = (page) => {
  if (page >= 0 && page < totalPagesExpired.value) currentPageExpired.value = page
}

// Reset khi đổi tab
watch(activeTab, () => {
  currentPage.value = 0
  currentPageSoon.value = 0
  currentPageExpired.value = 0
})

const formatDate = (d) => (d ? new Date(d).toLocaleDateString('vi-VN') : '')
const formatDateTime = (d) => (d ? new Date(d).toLocaleString('vi-VN', { hour12: false }) : '')

const getTrangThai = (t) =>
  t === 0
    ? '✅ Bình thường'
    : t === 1
      ? '⚠️ Sắp hết'
      : t === 2
        ? '❌ Hết hàng'
        : t === 3
          ? '⏳ Sắp hết hạn'
          : '☠️ Hết hạn'

const getTrangThaiClass = (t) =>
  t === 0
    ? 'bg-success text-white'
    : t === 1
      ? 'bg-warning text-dark'
      : t === 2
        ? 'bg-danger text-white'
        : t === 3
          ? 'bg-warning text-dark'
          : 'bg-secondary text-white'

onMounted(loadKho)
</script>

<style scoped>
.stat-card {
  border-radius: 0.5rem;
  min-height: 90px;
}
.stat-card .fw-bold {
  font-size: 1.1rem;
}
.stat-card .small {
  font-size: 0.8rem;
}
.badge-pill {
  border-radius: 50rem;
}
.badge-lg {
  font-size: 0.85rem;
  padding: 0.45em 1em;
  font-weight: 600;
}
</style>
