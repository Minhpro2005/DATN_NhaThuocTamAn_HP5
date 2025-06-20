<template>
  <div class="container py-4">
    <h4 class="mb-4 text-success">Danh Sách Phiếu Nhập</h4>

    <table class="table table-bordered align-middle">
      <thead class="table-success text-center">
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
          <td class="text-center">{{ pn.maPN }}</td>
          <td>{{ formatDate(pn.ngayNhap) }}</td>
          <td>{{ pn.tenNV }}</td>
          <td>{{ pn.tenNCC }}</td>
          <td class="text-end text-danger">{{ pn.tongTien.toLocaleString() }} đ</td>
          <td class="text-center">
            <button class="btn btn-sm btn-info" @click="xemChiTiet(pn.maPN)">
              <i class="bi bi-eye"></i> Xem
            </button>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- Modal chi tiết -->
    <div
      v-if="chiTietModalVisible"
      class="modal fade show d-block"
      style="background: rgba(0, 0, 0, 0.4)"
    >
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">Chi Tiết Phiếu Nhập #{{ selectedPN?.maPN }}</h5>
            <button type="button" class="btn-close" @click="chiTietModalVisible = false"></button>
          </div>
          <div class="modal-body">
            <p><strong>Nhân viên:</strong> {{ selectedPN?.tenNV }}</p>
            <p><strong>Nhà cung cấp:</strong> {{ selectedPN?.tenNCC }}</p>
            <p><strong>Ngày nhập:</strong> {{ formatDate(selectedPN?.ngayNhap) }}</p>

            <table class="table table-striped">
              <thead>
                <tr>
                  <th>#</th>
                  <th>Tên thuốc</th>
                  <th>Số lượng</th>
                  <th>Đơn giá</th>
                  <th>Thành tiền</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(ct, index) in selectedPN?.chiTiet || []" :key="index">
                  <td>{{ index + 1 }}</td>
                  <td>{{ ct.tenThuoc }}</td>
                  <td class="text-center">{{ ct.soLuong }}</td>
                  <td class="text-end">{{ ct.donGiaNhap.toLocaleString() }}</td>
                  <td class="text-end">{{ (ct.soLuong * ct.donGiaNhap).toLocaleString() }}</td>
                </tr>
              </tbody>
            </table>
          </div>
          <div class="modal-footer">
            <button class="btn btn-secondary" @click="chiTietModalVisible = false">Đóng</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

// Demo: danh sách phiếu nhập (gọi API thực tế thay bằng fetch hoặc axios)
const phieuNhaps = ref([
  {
    maPN: 1001,
    ngayNhap: '2024-06-01',
    tenNV: 'Nguyễn Văn A',
    tenNCC: 'Công ty ABC',
    tongTien: 500000,
    chiTiet: [
      { tenThuoc: 'Paracetamol', soLuong: 50, donGiaNhap: 5000 },
      { tenThuoc: 'Vitamin C', soLuong: 30, donGiaNhap: 8000 },
    ],
  },
  {
    maPN: 1002,
    ngayNhap: '2024-06-05',
    tenNV: 'Trần Thị B',
    tenNCC: 'Công ty XYZ',
    tongTien: 240000,
    chiTiet: [{ tenThuoc: 'Cảm cúm F', soLuong: 20, donGiaNhap: 12000 }],
  },
])

const chiTietModalVisible = ref(false)
const selectedPN = ref(null)

const xemChiTiet = (maPN) => {
  selectedPN.value = phieuNhaps.value.find((pn) => pn.maPN === maPN)
  chiTietModalVisible.value = true
}

const formatDate = (dateStr) => {
  const d = new Date(dateStr)
  return d.toLocaleDateString('vi-VN')
}
</script>
