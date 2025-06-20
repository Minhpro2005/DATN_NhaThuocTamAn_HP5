<template>
  <div class="container py-4">
    <h4 class="text-success mb-3">📥 Xử Lý Đơn Thuốc Khách Gửi</h4>

    <!-- Bộ lọc tìm kiếm -->
    <div class="row g-2 mb-3">
      <div class="col-md-6">
        <input v-model="keyword" class="form-control" placeholder="Tìm theo tên khách hàng..." />
      </div>
    </div>

    <!-- Danh sách đơn thuốc đã gửi -->
    <table class="table table-bordered table-hover small align-middle">
      <thead class="table-success text-center">
        <tr>
          <th>Mã đơn</th>
          <th>Tên khách</th>
          <th>SĐT</th>
          <th>Ngày gửi</th>
          <th>Ghi chú</th>
          <th>Trạng thái</th>
          <th>Liên hệ</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="don in filteredDonThuoc" :key="don.maDon" class="text-center">
          <td>{{ don.maDon }}</td>
          <td class="text-start">{{ don.tenKhach }}</td>
          <td>{{ don.sdt }}</td>
          <td>{{ formatDate(don.ngayGui) }}</td>
          <td class="text-start">{{ don.ghiChu }}</td>
          <td>
            <span :class="don.daLienHe ? 'text-success' : 'text-danger'">
              {{ don.daLienHe ? 'Đã liên hệ' : 'Chưa liên hệ' }}
            </span>
          </td>
          <td>
            <button class="btn btn-sm btn-primary" @click="lienHe(don)" :disabled="don.daLienHe">
              📞 Liên hệ
            </button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

const keyword = ref('')

const danhSachDonThuoc = ref([
  {
    maDon: 101,
    tenKhach: 'Nguyễn Văn A',
    sdt: '0912345678',
    ngayGui: '2024-05-20',
    ghiChu: 'Đau họng và sốt cao',
    daLienHe: false,
  },
  {
    maDon: 102,
    tenKhach: 'Trần Thị B',
    sdt: '0987654321',
    ngayGui: '2024-05-21',
    ghiChu: 'Đau dạ dày, mệt mỏi',
    daLienHe: true,
  },
  {
    maDon: 103,
    tenKhach: 'Lê Văn C',
    sdt: '0909999888',
    ngayGui: '2024-05-23',
    ghiChu: 'Ho, cảm cúm kéo dài',
    daLienHe: false,
  },
])

const filteredDonThuoc = computed(() =>
  danhSachDonThuoc.value.filter((d) =>
    d.tenKhach.toLowerCase().includes(keyword.value.toLowerCase()),
  ),
)

const formatDate = (d) => new Date(d).toLocaleDateString('vi-VN')

const lienHe = (don) => {
  don.daLienHe = true
  alert(`📞 Đã liên hệ tư vấn cho khách: ${don.tenKhach}`)
}
</script>

<style scoped>
.table td {
  vertical-align: middle;
}
</style>
