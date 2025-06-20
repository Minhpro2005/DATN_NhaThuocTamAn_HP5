<template>
  <div class="container mt-4">
    <h3 class="mb-4 text-success">📝 Quản lý Đơn Hàng</h3>

    <!-- Bộ lọc trạng thái -->
    <div class="row mb-3">
      <div class="col-md-4">
        <select class="form-select" v-model="filterStatus">
          <option value="">-- Tất cả trạng thái --</option>
          <option v-for="item in trangThaiOptions" :key="item.value" :value="item.value">
            {{ item.label }}
          </option>
        </select>
      </div>
    </div>

    <!-- Bảng danh sách đơn hàng -->
    <div class="table-responsive">
      <table class="table table-bordered table-hover text-center align-middle">
        <thead class="table-success">
          <tr>
            <th>#</th>
            <th>Khách hàng</th>
            <th>Ngày đặt</th>
            <th>Thanh toán</th>
            <th>Trạng thái</th>
            <th>Hành động</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(item, index) in displayedDonHangs" :key="item.maDonHang">
            <td>{{ index + 1 }}</td>
            <td>{{ item.khachHang.hoTen }}</td>
            <td>{{ formatDate(item.ngayDat) }}</td>
            <td>
              {{ formatMoney(item.thanhToan) }}
              <br />
              <span :class="item.daThanhToan ? 'text-success' : 'text-danger'">
                {{ item.daThanhToan ? 'Đã TT' : 'Chưa TT' }}
              </span>
            </td>
            <td>{{ getTrangThaiLabel(item.trangThai) }}</td>
            <td>
              <div class="d-flex justify-content-center gap-2">
                <button class="btn btn-primary btn-sm" @click="xemChiTiet(item)">
                  <i class="bi bi-search"></i> Chi tiết
                </button>
                <button class="btn btn-warning btn-sm" @click="capNhatTrangThai(item)">
                  <i class="bi bi-arrow-repeat"></i> Cập nhật
                </button>
                <LichSuDonHang
                  :maDonHang="item.maDonHang"
                  :dataLichSu="getLichSu(item.maDonHang)"
                />
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Modal chi tiết -->
    <div class="modal fade" id="modalChiTiet" tabindex="-1" aria-hidden="true" ref="modal">
      <div class="modal-dialog modal-lg">
        <div class="modal-content" v-if="donHangSelected">
          <DonHangChiTiet :donHang="donHangSelected" />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import DonHangChiTiet from './DonHangChiTiet.vue'
import LichSuDonHang from './LichSuDonHang.vue'

export default {
  components: { DonHangChiTiet, LichSuDonHang },
  data() {
    return {
      filterStatus: '',
      donHangSelected: null,
      previousCount: 0, // để kiểm tra đơn mới

      trangThaiOptions: [
        { value: 0, label: 'Chờ xác nhận' },
        { value: 1, label: 'Đã xác nhận' },
        { value: 2, label: 'Đang giao' },
        { value: 3, label: 'Hoàn tất' },
        { value: 4, label: 'Đã hủy' },
      ],

      khoHang: [
        { maBienThe: 101, tenBienThe: 'Paracetamol 500mg', soLuongTon: 100 },
        { maBienThe: 102, tenBienThe: 'Vitamin C 1000mg', soLuongTon: 50 },
      ],

      donHangs: [
        {
          maDonHang: 1,
          ngayDat: '2024-06-10',
          thanhToan: 500000,
          giamGia: 0,
          trangThai: 0,
          daThanhToan: false,
          hinhThucThanhToan: 'cod',
          diaChiNhan: 'Hà Nội',
          soDienThoaiNhan: '0123456789',
          ghiChu: 'Giao giờ hành chính',
          khachHang: { hoTen: 'Nguyễn Văn A' },
          chiTiet: [
            {
              maDHCT: 1,
              soLuong: 2,
              donGia: 250000,
              thanhTien: 500000,
              bienTheThuoc: {
                maBienThe: 101,
                tenBienThe: 'Hộp 20 viên',
                thuoc: { tenThuoc: 'Paracetamol 500mg' },
              },
            },
          ],
        },
      ],

      lichSuMock: [
        {
          maDonHang: 1,
          trangThaiCu: null,
          trangThaiMoi: 0,
          thoiGian: '2024-06-10T08:00:00',
          ghiChu: 'Tạo đơn hàng',
        },
      ],
    }
  },

  computed: {
    displayedDonHangs() {
      if (!this.filterStatus) return this.donHangs
      return this.donHangs.filter((item) => item.trangThai === this.filterStatus)
    },
  },

  mounted() {
    this.previousCount = this.donHangs.length
    setInterval(this.kiemTraDonMoi, 10000) // Kiểm tra mỗi 10 giây
  },

  methods: {
    xemChiTiet(item) {
      this.donHangSelected = item
      new bootstrap.Modal(this.$refs.modal).show()
    },

    capNhatTrangThai(item) {
      const cu = item.trangThai
      if (cu === 0) {
        this.truTonKho(item)
        item.trangThai = 1
        this.themLichSu(item.maDonHang, cu, 1, 'Xác nhận đơn hàng & trừ kho')
        alert('✅ Đã xác nhận và trừ kho hàng.')
        return
      }
      if (cu < 3) {
        item.trangThai += 1
        this.themLichSu(item.maDonHang, cu, item.trangThai, 'Cập nhật trạng thái tiếp theo')

        if (item.trangThai === 3 && item.hinhThucThanhToan === 'cod' && !item.daThanhToan) {
          item.daThanhToan = true
          this.themLichSu(item.maDonHang, 3, 3, 'Đã thu tiền khi hoàn tất đơn COD')
          alert('💰 Đã tự động đánh dấu ĐÃ THANH TOÁN cho đơn COD.')
        }
      }
    },

    truTonKho(donHang) {
      donHang.chiTiet.forEach((ct) => {
        const bienThe = ct.bienTheThuoc
        const kho = this.khoHang.find((k) => k.maBienThe === bienThe.maBienThe)
        if (kho) {
          kho.soLuongTon -= ct.soLuong
          if (kho.soLuongTon < 0) kho.soLuongTon = 0
        }
      })
    },

    themLichSu(maDonHang, cu, moi, ghiChu) {
      this.lichSuMock.push({
        maDonHang,
        trangThaiCu: cu,
        trangThaiMoi: moi,
        thoiGian: new Date().toISOString(),
        ghiChu,
      })
    },

    getLichSu(maDonHang) {
      return this.lichSuMock.filter((ls) => ls.maDonHang === maDonHang)
    },

    kiemTraDonMoi() {
      // Thực tế sẽ gọi API - ở đây mình chỉ demo trên mảng donHangs
      const currentCount = this.donHangs.length
      if (currentCount > this.previousCount) {
        alert('🛎 Có đơn hàng mới vừa được tạo!')
        this.previousCount = currentCount
      }
    },

    formatDate(date) {
      return new Date(date).toLocaleDateString('vi-VN')
    },

    formatMoney(val) {
      return (val ?? 0).toLocaleString('vi-VN', { style: 'currency', currency: 'VND' })
    },

    getTrangThaiLabel(val) {
      const found = this.trangThaiOptions.find((x) => x.value === val)
      return found ? found.label : 'Không rõ'
    },
  },
}
</script>

<style scoped>
.btn-group button {
  min-width: 110px;
}
</style>
