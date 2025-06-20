<template>
  <div class="border p-4 bg-white rounded shadow-sm">
    <h4 class="text-center mb-4 fw-bold text-primary">📦 PHIẾU GIAO HÀNG</h4>

    <div class="mb-3">
      <div><strong>Mã đơn hàng:</strong> #{{ donHang.maDonHang }}</div>
      <div><strong>Ngày đặt:</strong> {{ formatDate(donHang.ngayDat) }}</div>
      <div><strong>Ngày thanh toán:</strong> {{ formatDate(donHang.ngayThanhToan) }}</div>
    </div>

    <div class="mb-3">
      <strong>Thông tin người nhận</strong>
      <div>Khách hàng: {{ donHang.khachHang.hoTen }}</div>
      <div>Địa chỉ: {{ donHang.diaChiNhan || '---' }}</div>
      <div>SĐT: {{ donHang.soDienThoaiNhan || '---' }}</div>
    </div>

    <h6 class="fw-bold mt-4 mb-2">Danh sách sản phẩm</h6>
    <table class="table table-bordered table-sm text-center align-middle">
      <thead class="table-light">
        <tr>
          <th>Sản phẩm</th>
          <th>Biến thể</th>
          <th>SL</th>
          <th>Đơn giá</th>
          <th>Thành tiền</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="ct in donHang.chiTiet" :key="ct.maDHCT">
          <td>{{ ct.bienTheThuoc?.thuoc?.tenThuoc || '---' }}</td>
          <td>{{ ct.bienTheThuoc?.tenBienThe || '---' }}</td>
          <td>{{ ct.soLuong }}</td>
          <td>{{ formatMoney(ct.donGia) }}</td>
          <td>{{ formatMoney(ct.thanhTien) }}</td>
        </tr>
      </tbody>
    </table>

    <div class="mt-3">
      <div><strong>Giảm giá:</strong> {{ formatMoney(donHang.giamGia) }}</div>
      <div class="fw-bold fs-5 text-danger">
        Tổng thanh toán: {{ formatMoney(donHang.thanhToan) }}
      </div>
    </div>

    <div class="mt-3"><strong>Ghi chú:</strong> {{ donHang.ghiChu || '---' }}</div>

    <div class="mt-2">
      <strong>Hình thức thanh toán:</strong>
      {{
        donHang.hinhThucThanhToan === 'cod'
          ? 'COD - Thanh toán khi nhận hàng'
          : 'Đã thanh toán Online'
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
      return (val ?? 0).toLocaleString('vi-VN', { style: 'currency', currency: 'VND' })
    },
    printOrder() {
      window.print()
    },
  },
}
</script>
