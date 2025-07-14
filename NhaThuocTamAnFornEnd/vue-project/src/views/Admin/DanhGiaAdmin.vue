<template>
  <div class="container py-4">
    <h4 class="text-success mb-3">📝 Quản Lý Đánh Giá Sản Phẩm</h4>

    <!-- Bộ lọc -->
    <div class="row g-2 mb-3">
      <div class="col-md-4">
        <input
          v-model="keyword"
          class="form-control"
          placeholder="Tìm theo sản phẩm hoặc người dùng..."
        />
      </div>
      <div class="col-md-2">
        <select v-model="filterSao" class="form-select">
          <option value="">Tất cả sao</option>
          <option v-for="n in 5" :key="n" :value="n">{{ n }} sao</option>
        </select>
      </div>
      <div class="col-md-2">
        <input type="date" v-model="filterNgayTu" class="form-control" />
      </div>
      <div class="col-md-2">
        <input type="date" v-model="filterNgayDen" class="form-control" />
      </div>
      <div class="col-md-2">
        <button class="btn btn-outline-secondary w-100" @click="clearFilters">Xóa lọc</button>
      </div>
    </div>

    <!-- Bảng đánh giá -->
    <div v-if="isLoading" class="text-center text-muted my-3">Đang tải đánh giá...</div>

    <table v-else class="table table-bordered table-hover small align-middle">
      <thead class="table-success text-center">
        <tr>
          <th>ID</th>
          <th>Sản phẩm</th>
          <th>Người dùng</th>
          <th>Sao</th>
          <th>Nội dung & Ảnh</th>
          <th>Ngày đánh giá</th>
          <th>Hành động</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="dg in paginatedDanhGia" :key="dg.id" class="text-center">
          <td>{{ dg.id }}</td>
          <td class="text-start">{{ dg.sanPham }}</td>
          <td class="text-start d-flex align-items-center gap-2">
            <img :src="dg.avatar" class="rounded-circle" width="32" height="32" alt="avatar" />
            <span>{{ dg.nguoiDung }}</span>
          </td>
          <td>
            <span v-for="n in 5" :key="n">
              <i
                class="bi"
                :class="n <= dg.sao ? 'bi-star-fill text-warning' : 'bi-star text-muted'"
              ></i>
            </span>
          </td>
          <td class="text-start">
            {{ dg.noiDung }}
            <div v-if="dg.hinhAnh" class="mt-2">
              <img
                :src="getImageUrl(dg.hinhAnh)"
                alt="ảnh đánh giá"
                style="max-height: 60px; border-radius: 6px"
              />
            </div>
          </td>
          <td>{{ formatDate(dg.ngay) }}</td>
          <td>
            <button class="btn btn-sm btn-danger" @click="xoaDanhGia(dg.id)">Xóa</button>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- ✅ Phân trang giống Đơn Hàng -->
    <nav class="mt-3" v-if="totalPages > 1">
      <ul class="pagination justify-content-center">
        <li
          class="page-item"
          :class="{ disabled: currentPage === 1 }"
          @click="changePage(currentPage - 1)"
        >
          <a class="page-link" href="#">Trước</a>
        </li>
        <li
          v-for="page in totalPages"
          :key="page"
          class="page-item"
          :class="{ active: currentPage === page }"
          @click="changePage(page)"
        >
          <a class="page-link" href="#">{{ page }}</a>
        </li>
        <li
          class="page-item"
          :class="{ disabled: currentPage === totalPages }"
          @click="changePage(currentPage + 1)"
        >
          <a class="page-link" href="#">Tiếp</a>
        </li>
      </ul>
    </nav>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'

const serverUrl = 'http://localhost:8080'

// Bộ lọc
const keyword = ref('')
const filterSao = ref('')
const filterNgayTu = ref('')
const filterNgayDen = ref('')

// Phân trang
const currentPage = ref(1)
const pageSize = ref(5)

const danhGiaList = ref([])
const isLoading = ref(true)

// Avatar người dùng
function getAvatarUrl(path) {
  if (!path) return 'https://cdn-icons-png.flaticon.com/512/149/149071.png'
  if (path.startsWith('http')) return path
  return `${serverUrl}${path}`
}

// Ảnh đánh giá
function getImageUrl(path) {
  if (!path) return ''
  if (path.startsWith('http')) return path
  return `${serverUrl}${path.replace('/images/', '/uploads/')}`
}

// Lấy dữ liệu đánh giá
const fetchDanhGia = async () => {
  try {
    const res = await axios.get(`${serverUrl}/api/danh-gia`)
    danhGiaList.value = res.data.map((dg) => ({
      id: dg.maDG,
      sanPham: dg.tenBienThe ? `${dg.tenThuoc} - ${dg.tenBienThe}` : dg.tenThuoc || '---',
      nguoiDung: dg.tenKhachHang || 'Ẩn danh',
      avatar: getAvatarUrl(dg.hinhAnhKH),
      hinhAnh: dg.hinhAnh,
      sao: dg.soSao,
      noiDung: dg.binhLuan,
      ngay: dg.ngayDanhGia,
    }))
    currentPage.value = 1
  } catch (err) {
    console.error('❌ Lỗi khi tải đánh giá:', err)
    alert('Không thể tải dữ liệu đánh giá.')
  } finally {
    isLoading.value = false
  }
}

onMounted(() => {
  fetchDanhGia()
})

// Lọc và sắp xếp
const filteredDanhGia = computed(() => {
  return danhGiaList.value
    .filter((d) => {
      const matchKeyword =
        d.sanPham.toLowerCase().includes(keyword.value.toLowerCase()) ||
        d.nguoiDung.toLowerCase().includes(keyword.value.toLowerCase())

      const matchSao = filterSao.value ? d.sao === +filterSao.value : true

      const matchNgay =
        (!filterNgayTu.value || new Date(d.ngay) >= new Date(filterNgayTu.value)) &&
        (!filterNgayDen.value || new Date(d.ngay) <= new Date(filterNgayDen.value))

      return matchKeyword && matchSao && matchNgay
    })
    .sort((a, b) => new Date(b.ngay) - new Date(a.ngay))
})

const totalPages = computed(() => Math.ceil(filteredDanhGia.value.length / pageSize.value))

const paginatedDanhGia = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  return filteredDanhGia.value.slice(start, start + pageSize.value)
})

const changePage = (page) => {
  if (page < 1 || page > totalPages.value) return
  currentPage.value = page
}

const xoaDanhGia = async (id) => {
  if (confirm('Bạn có chắc muốn xóa đánh giá này không?')) {
    try {
      await axios.delete(`${serverUrl}/api/danh-gia/${id}`)
      danhGiaList.value = danhGiaList.value.filter((d) => d.id !== id)
    } catch (err) {
      alert('❌ Lỗi khi xóa đánh giá')
      console.error(err)
    }
  }
}

const clearFilters = () => {
  keyword.value = ''
  filterSao.value = ''
  filterNgayTu.value = ''
  filterNgayDen.value = ''
  currentPage.value = 1
}

const formatDate = (d) => {
  const date = new Date(d)
  return date.toLocaleDateString('vi-VN')
}
</script>

<style scoped>
.table td {
  vertical-align: middle;
}
.pagination .page-item .page-link {
  cursor: pointer;
}
</style>
