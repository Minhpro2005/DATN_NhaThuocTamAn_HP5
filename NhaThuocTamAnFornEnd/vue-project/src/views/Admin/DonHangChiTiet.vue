<template>
  <div class="border p-4 bg-white rounded shadow-sm">
    <h4 class="text-center mb-4 fw-bold text-primary">📦 PHIẾU GIAO HÀNG</h4>

    <!-- Mã đơn và ngày đặt -->
    <div class="mb-3">
      <div><strong>Mã đơn hàng:</strong> #{{ donHang.maDonHang }}</div>
      <div><strong>Ngày đặt:</strong> {{ formatDate(donHang.ngayDat) }}</div>
    </div>

    <!-- Người nhận & người gửi -->
    <div class="mb-3 row">
      <!-- Người nhận -->
      <div class="col-md-6">
        <strong>Thông tin người nhận</strong>
        <div>Khách hàng: {{ donHang.khachHang?.hoTen || donHang.hoTenNguoiNhan || '---' }}</div>
        <div>Địa chỉ: {{ donHang.diaChiNhan || '---' }}</div>
        <div>SĐT: {{ donHang.soDienThoaiNhan || '---' }}</div>
      </div>

      <!-- Người gửi -->
      <div class="col-md-6 text-md-end mt-3 mt-md-0">
        <strong>Thông tin người gửi</strong>
        <div>Tên shop: Nhà thuốc Tâm An</div>
        <div>Địa chỉ: 55 Cầu Đen Thị Trấn Núi Đối, Kiến Thụy, Hải Phòng</div>
        <div>SĐT: 0901 234 567</div>
      </div>
    </div>

    <!-- Danh sách sản phẩm -->
    <h6 class="fw-bold mt-4 mb-2">Danh sách sản phẩm</h6>
    <table class="table table-bordered table-sm text-center align-middle">
      <thead class="table-light">
        <tr>
          <th>Sản phẩm</th>
          <th>SL</th>
          <th>Đơn giá</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="ct in donHang.chiTiet" :key="ct.maDHCT">
          <td>{{ ct.bienTheThuoc?.thuoc?.tenThuoc || ct.thuoc?.tenThuoc || '---' }}</td>
          <td>{{ ct.soLuong }}</td>
          <td>{{ formatMoney(ct.donGia) }}</td>
        </tr>
      </tbody>
    </table>

    <!-- Tổng tiền + ghi chú -->
    <div class="mt-3">
      <div class="fw-bold fs-5 text-danger">
        Tổng thanh toán: {{ formatMoney(donHang.thanhToan) }}
      </div>
    </div>

    <div class="mt-3"><strong>Ghi chú:</strong> {{ donHang.ghiChu || '---' }}</div>

    <div v-if="donHang.trangThai === 4 && donHang.lyDoHuy" class="mt-2 text-danger">
      <strong>Lý do huỷ đơn:</strong> {{ donHang.lyDoHuy }}
    </div>

    <div class="mt-2">
      <strong>Hình thức thanh toán:</strong>
      {{
        donHang.hinhThucThanhToan === 'cod' ? 'Thanh toán khi nhận hàng' : 'Đã thanh toán Online'
      }}
    </div>

    <div class="text-center mt-4">
      <button class="btn btn-primary" @click="printOrder">
        <i class="bi bi-printer me-2"></i>In đơn hàng
      </button>
    </div>
  </div>
</template>

<script>
export default {
  props: ['donHang'],
  methods: {
    formatDate(date) {
      if (!date) return '-'
      return new Date(date).toLocaleDateString('vi-VN')
    },
    formatMoney(val) {
      return (val ?? 0).toLocaleString('vi-VN', {
        style: 'currency',
        currency: 'VND',
      })
    },
    printOrder() {
      window.print()
    },
  },
}
</script>

<style scoped>
@media print {
  button {
    display: none;
  }
  .border {
    border: none !important;
    box-shadow: none !important;
  }
}
</style>
