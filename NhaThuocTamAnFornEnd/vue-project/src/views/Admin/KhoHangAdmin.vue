<template>
  <div class="container py-4">
    <h4 class="mb-4 text-success">📦 Quản Lý Kho Hàng</h4>

    <!-- Bộ lọc -->
    <div class="row mb-3 g-2">
      <div class="col-md-4">
        <input
          v-model="keyword"
          type="text"
          class="form-control"
          placeholder="🔎 Tìm kiếm theo tên biến thể..."
        />
      </div>
      <div class="col-md-3">
        <select v-model="filterStatus" class="form-select">
          <option value="">Tất cả trạng thái</option>
          <option value="het">❌ Hết hàng</option>
          <option value="saphet">⚠️ Sắp hết</option>
          <option value="binhthuong">✅ Bình thường</option>
        </select>
      </div>
    </div>

    <!-- Bảng kho -->
    <div class="table-responsive shadow-sm border">
      <table class="table table-bordered align-middle text-center">
        <thead class="table-success">
          <tr>
            <th>Mã Biến Thể</th>
            <th>Tên Biến Thể</th>
            <th>Số Lượng Tồn</th>
            <th>Ngày Cập Nhật</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="kho in filteredKhoHang" :key="kho.maBienThe">
            <td>{{ kho.maBienThe }}</td>
            <td>{{ kho.tenBienThe }}</td>
            <td>
              <span :class="getStockClass(kho.soLuongTon)" class="fw-bold">
                {{ kho.soLuongTon }}
              </span>
            </td>
            <td>{{ formatDate(kho.ngayCapNhat) }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

const khoHang = ref([
  {
    maBienThe: 101,
    tenBienThe: 'Paracetamol 500mg',
    soLuongTon: 120,
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
    soLuongTon: 0,
    ngayCapNhat: new Date().toISOString(),
  },
])

const keyword = ref('')
const filterStatus = ref('')

// Bộ lọc chính
const filteredKhoHang = computed(() => {
  return khoHang.value.filter((kho) => {
    const nameMatch = kho.tenBienThe.toLowerCase().includes(keyword.value.toLowerCase())

    let statusMatch = true
    if (filterStatus.value === 'het') {
      statusMatch = kho.soLuongTon === 0
    } else if (filterStatus.value === 'saphet') {
      statusMatch = kho.soLuongTon > 0 && kho.soLuongTon <= 30
    } else if (filterStatus.value === 'binhthuong') {
      statusMatch = kho.soLuongTon > 30
    }
    return nameMatch && statusMatch
  })
})

function getStockClass(soLuong) {
  if (soLuong === 0) return 'text-danger'
  if (soLuong <= 30) return 'text-warning'
  return 'text-success'
}

function formatDate(str) {
  return new Date(str).toLocaleDateString('vi-VN')
}
</script>

<style scoped>
.table td,
.table th {
  vertical-align: middle;
}
</style>
