<template>
  <div class="modal fade show d-block" tabindex="-1" style="background: rgba(0, 0, 0, 0.5)">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <!-- Header -->
        <div class="modal-header">
          <h5 class="modal-title">Chi tiết đơn hàng #{{ donHang?.maDonHang || '---' }}</h5>
          <button type="button" class="btn-close" @click="closeModal"></button>
        </div>

        <!-- Body -->
        <div class="modal-body">
          <!-- Ngày đặt & Trạng thái -->
          <div class="mb-3">
            <div><strong>Ngày đặt:</strong> {{ formatDate(donHang?.ngayDat) }}</div>
            <div class="mt-1">
              <strong>Trạng thái:</strong>
              <span :class="getStatusClass(donHang?.trangThai)">
                {{ getStatusLabel(donHang?.trangThai) }}
              </span>
            </div>
          </div>

          <!-- Thông tin người nhận -->
          <div class="mb-3">
            <div class="fw-bold">Thông tin người nhận:</div>
            <div>Họ tên: {{ donHang?.hoTenNguoiNhan || donHang?.khachHang?.hoTen || '---' }}</div>
            <div>SĐT: {{ donHang?.soDienThoaiNhan || '---' }}</div>
            <div>Địa chỉ: {{ donHang?.diaChiNhan || '---' }}</div>
          </div>

          <!-- Danh sách sản phẩm -->
          <div class="fw-bold mb-2">Danh sách sản phẩm</div>
          <table class="table table-bordered table-sm text-center align-middle">
            <thead class="table-light">
              <tr>
                <th class="text-start">Sản phẩm</th>
                <th style="width: 130px">Đơn giá</th>
                <th style="width: 70px">SL</th>
                <th style="width: 140px">Thành tiền</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="ct in donHang?.chiTiet || []" :key="ct.maDHCT">
                <td class="text-start">
                  {{
                    ct.bienTheThuoc?.tenBienThe ||
                    ct.bienTheThuoc?.thuoc?.tenThuoc ||
                    ct.thuoc?.tenThuoc ||
                    ct.tenThuoc ||
                    '---'
                  }}
                </td>

                <td>{{ formatMoney(ct.donGia) }}</td>
                <td>{{ ct.soLuong }}</td>
                <td>{{ formatMoney((ct.donGia || 0) * (ct.soLuong || 0)) }}</td>
              </tr>
            </tbody>
          </table>

          <!-- Tiền -->
          <div class="mt-2">
            <div><strong>Giảm giá:</strong> {{ formatMoney(donHang?.giamGia) }}</div>
            <div class="fw-bold fs-5 text-danger">
              Tổng thanh toán: {{ formatMoney(donHang?.thanhToan) }}
            </div>
          </div>

          <!-- Ghi chú & Lý do hủy -->
          <div class="mt-2"><strong>Ghi chú:</strong> {{ donHang?.ghiChu || '---' }}</div>
          <div v-if="donHang?.trangThai === 4 && donHang?.lyDoHuy" class="mt-2 text-danger">
            <strong>Lý do hủy đơn:</strong> {{ donHang.lyDoHuy }}
          </div>

          <!-- Hình thức thanh toán -->
          <div class="mt-2">
            <strong>Hình thức thanh toán:</strong>
            {{
              donHang.hinhThucThanhToan === 'cod'
                ? 'Thanh toán khi nhận hàng (COD)'
                : donHang.hinhThucThanhToan === 'momo'
                  ? 'Thanh toán MoMo'
                  : donHang.hinhThucThanhToan === 'vnpay'
                    ? 'Thanh toán VNPay'
                    : 'Đã thanh toán Online'
            }}
          </div>
        </div>

        <!-- Footer -->
        <!-- Footer -->
        <div class="modal-footer d-flex justify-content-between align-items-center">
          <!-- Nút Đóng bên phải -->
          <button class="btn btn-secondary ms-auto" @click="closeModal">Đóng</button>

          <!-- Nút In đơn hàng căn giữa -->
          <button
            class="btn btn-primary position-absolute start-50 translate-middle-x"
            @click="printOrder"
          >
            <i class="bi bi-printer me-2"></i> In đơn hàng
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: { donHang: { type: Object, required: true } },
  methods: {
    formatDate(date) {
      if (!date) return '---'
      try {
        return new Date(date).toLocaleDateString('vi-VN')
      } catch {
        return '---'
      }
    },
    formatMoney(val) {
      return (val ?? 0).toLocaleString('vi-VN', { style: 'currency', currency: 'VND' })
    },
    printOrder() {
      window.print()
    },
    closeModal() {
      this.$emit('close')
    },
    getStatusLabel(status) {
      switch (status) {
        case 0:
          return 'Chờ xác nhận'
        case 1:
          return 'Đã xác nhận'
        case 2:
          return 'Đang giao'
        case 3:
          return 'Hoàn tất'
        case 4:
          return 'Đã huỷ'
        default:
          return 'Không rõ'
      }
    },
    getStatusClass(status) {
      switch (status) {
        case 0:
          return 'badge bg-secondary'
        case 1:
          return 'badge bg-primary'
        case 2:
          return 'badge bg-warning text-dark'
        case 3:
          return 'badge bg-success'
        case 4:
          return 'badge bg-dark'
        default:
          return 'badge bg-light text-dark'
      }
    },
  },
}
</script>
