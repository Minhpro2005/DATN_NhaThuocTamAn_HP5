<template>
  <div class="modal fade show d-block" tabindex="-1" style="background-color: rgba(0, 0, 0, 0.5)">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">Chi tiết đơn hàng #{{ donHang.maDonHang }}</h5>
          <button type="button" class="btn-close" @click="$emit('close')"></button>
        </div>

        <div class="modal-body">
          <div class="mb-2">
            <strong>Ngày đặt:</strong> {{ formatDate(donHang.ngayDat) }}<br />
            <strong>Trạng thái:</strong>
            <span :class="getStatusClass(donHang.trangThai)">
              {{ getStatusLabel(donHang.trangThai) }}
            </span>
          </div>

          <div class="mb-3">
            <strong>Thông tin người nhận:</strong>
            <div>Họ tên: {{ donHang.hoTenNguoiNhan || donHang.khachHang?.hoTen }}</div>
            <div>SĐT: {{ donHang.soDienThoaiNhan }}</div>
            <div>Địa chỉ: {{ donHang.diaChiNhan }}</div>
          </div>

          <h6 class="fw-bold mb-2">Danh sách sản phẩm</h6>
          <table class="table table-bordered table-sm text-center align-middle">
            <thead class="table-light">
              <tr>
                <th>Ảnh</th>
                <th>Sản phẩm</th>
                <th>SL</th>
                <th>Đơn giá</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="ct in donHang.chiTiet" :key="ct.maDHCT">
                <td>
                  <img
                    :src="ct.hinhAnh"
                    class="img-thumbnail"
                    style="width: 60px; height: 60px; object-fit: cover"
                    alt="ảnh sản phẩm"
                  />
                </td>
                <td>{{ ct.tenThuoc || '---' }}</td>
                <td>{{ ct.soLuong }}</td>
                <td>{{ formatMoney(ct.donGia) }}</td>
              </tr>
            </tbody>
          </table>

          <div class="mt-3">
            <div><strong>Giảm giá:</strong> {{ formatMoney(donHang.giamGia) }}</div>
            <div class="fw-bold fs-5 text-danger">
              Tổng thanh toán: {{ formatMoney(donHang.thanhToan) }}
            </div>
          </div>

          <div class="mt-2"><strong>Ghi chú:</strong> {{ donHang.ghiChu || '---' }}</div>

          <div v-if="donHang.trangThai === 4 && donHang.lyDoHuy" class="mt-2 text-danger">
            <strong>Lý do hủy đơn:</strong> {{ donHang.lyDoHuy }}
          </div>

          <div class="mt-2">
            <strong>Hình thức thanh toán:</strong>
            {{
              donHang.hinhThucThanhToan === 'cod'
                ? 'Thanh toán khi nhận hàng'
                : 'Đã thanh toán Online'
            }}
          </div>
        </div>

        <div class="modal-footer">
          <button class="btn btn-secondary" @click="$emit('close')">Đóng</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: ['donHang'],
  methods: {
    formatDate(date) {
      return new Date(date).toLocaleDateString('vi-VN')
    },
    formatMoney(val) {
      return (val ?? 0).toLocaleString('vi-VN', {
        style: 'currency',
        currency: 'VND',
      })
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

<style scoped>
.modal-dialog {
  max-width: 800px;
}
</style>
