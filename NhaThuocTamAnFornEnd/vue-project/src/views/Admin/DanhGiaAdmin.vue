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
        <input type="date" v-model="filterNgayTu" class="form-control" placeholder="Từ ngày" />
      </div>
      <div class="col-md-2">
        <input type="date" v-model="filterNgayDen" class="form-control" placeholder="Đến ngày" />
      </div>
      <div class="col-md-2">
        <button class="btn btn-outline-secondary w-100" @click="clearFilters">Xóa lọc</button>
      </div>
    </div>

    <!-- Bảng đánh giá -->
    <table class="table table-bordered table-hover small align-middle">
      <thead class="table-success text-center">
        <tr>
          <th>ID</th>
          <th>Sản phẩm</th>
          <th>Người dùng</th>
          <th>Sao</th>
          <th>Nội dung</th>
          <th>Ngày đánh giá</th>
          <th>Hành động</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="dg in filteredDanhGia" :key="dg.id" class="text-center">
          <td>{{ dg.id }}</td>
          <td>{{ dg.sanPham }}</td>
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
          <td class="text-start">{{ dg.noiDung }}</td>
          <td>{{ formatDate(dg.ngay) }}</td>
          <td>
            <button class="btn btn-sm btn-danger" @click="xoaDanhGia(dg.id)">Xóa</button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

const keyword = ref('')
const filterSao = ref('')
const filterNgayTu = ref('')
const filterNgayDen = ref('')

const danhGiaList = ref([
  {
    id: 1,
    sanPham: 'Paracetamol',
    nguoiDung: 'Nguyễn Văn A',
    avatar: 'https://randomuser.me/api/portraits/men/32.jpg',
    sao: 5,
    noiDung: 'Thuốc rất hiệu quả!',
    ngay: '2024-05-15',
  },
  {
    id: 2,
    sanPham: 'Vitamin C',
    nguoiDung: 'Trần Thị B',
    avatar: 'https://randomuser.me/api/portraits/women/45.jpg',
    sao: 4,
    noiDung: 'Uống dễ chịu.',
    ngay: '2024-05-17',
  },
  {
    id: 3,
    sanPham: 'Amoxicillin',
    nguoiDung: 'Lê Văn C',
    avatar: 'https://randomuser.me/api/portraits/men/21.jpg',
    sao: 2,
    noiDung: 'Bị tác dụng phụ.',
    ngay: '2024-05-20',
  },
])

const filteredDanhGia = computed(() => {
  return danhGiaList.value.filter((d) => {
    const matchKeyword =
      d.sanPham.toLowerCase().includes(keyword.value.toLowerCase()) ||
      d.nguoiDung.toLowerCase().includes(keyword.value.toLowerCase())

    const matchSao = filterSao.value ? d.sao === +filterSao.value : true

    const matchNgay =
      (!filterNgayTu.value || new Date(d.ngay) >= new Date(filterNgayTu.value)) &&
      (!filterNgayDen.value || new Date(d.ngay) <= new Date(filterNgayDen.value))

    return matchKeyword && matchSao && matchNgay
  })
})

const formatDate = (d) => new Date(d).toLocaleDateString('vi-VN')

const xoaDanhGia = (id) => {
  if (confirm('Xóa đánh giá này?')) {
    const i = danhGiaList.value.findIndex((d) => d.id === id)
    if (i !== -1) danhGiaList.value.splice(i, 1)
  }
}

const clearFilters = () => {
  keyword.value = ''
  filterSao.value = ''
  filterNgayTu.value = ''
  filterNgayDen.value = ''
}
</script>

<style scoped>
.table td {
  vertical-align: middle;
}
</style>
