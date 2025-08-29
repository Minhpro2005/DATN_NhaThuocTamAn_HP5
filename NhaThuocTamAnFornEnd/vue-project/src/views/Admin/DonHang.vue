<template>
  <div class="container mt-4">
    <h3 class="mb-4 text-success fw-bold">📝 Quản lý Đơn Hàng</h3>

    <!-- Toast -->
    <div
      class="toast position-fixed top-0 end-0 m-3 p-3 text-bg-success shadow"
      role="alert"
      ref="toast"
      style="z-index: 1055; min-width: 280px"
    >
      <div class="d-flex">
        <div class="toast-body fw-bold">{{ toastMessage }}</div>
        <button
          type="button"
          class="btn-close btn-close-white me-2 m-auto"
          @click="hideToast"
        ></button>
      </div>
    </div>

    <!-- Bộ lọc -->
    <div class="card shadow-sm border-0 mb-3">
      <div class="card-body">
        <div class="row g-3">
          <div class="col-md-3">
            <input
              v-model="keyword"
              type="text"
              class="form-control shadow-sm rounded-pill"
              placeholder="🔍 Tìm mã, tên hoặc SĐT..."
            />
          </div>
          <div class="col-md-3">
            <select class="form-select shadow-sm rounded-pill" v-model="filterStatus">
              <option value="">-- Tất cả trạng thái --</option>
              <option v-for="item in trangThaiOptions" :key="item.value" :value="item.value">
                {{ item.label }}
              </option>
            </select>
          </div>
          <div class="col-md-2">
            <input
              type="date"
              class="form-control shadow-sm rounded-pill"
              v-model="filterFromDate"
            />
          </div>
          <div class="col-md-2">
            <input type="date" class="form-control shadow-sm rounded-pill" v-model="filterToDate" />
          </div>
          <div class="col-md-2">
            <button class="btn btn-outline-danger w-100 rounded-pill" @click="resetFilters">
              Xoá bộ lọc
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Bảng đơn hàng -->
    <div class="card shadow-sm border-0">
      <div class="card-body p-0">
        <table class="table table-hover text-center align-middle mb-0">
          <thead class="table-success">
            <tr>
              <th>Mã đơn hàng</th>
              <th>Khách hàng</th>
              <th>SĐT</th>
              <th>Ngày đặt</th>
              <th>Thanh toán</th>
              <th>Trạng thái</th>
              <th>Hành động</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in pagedDonHangs" :key="item.maDonHang">
              <td class="fw-bold text-success">#{{ item.maDonHang }}</td>
              <td>{{ getTenKhach(item) }}</td>
              <td>{{ getSoDienThoai(item) }}</td>
              <td>{{ formatDate(item.ngayDat) }}</td>
              <td>
                {{ formatMoney(item.thanhToan) }}<br />
                <span :class="item.daThanhToan ? 'badge bg-success' : 'badge bg-danger'">
                  {{ item.daThanhToan ? 'Đã TT' : 'Chưa TT' }}
                </span>
              </td>
              <td class="text-center">
                <span class="badge badge-pill badge-lg" :class="getTrangThaiClass(item.trangThai)">
                  {{ getTrangThaiLabel(item.trangThai) }}
                </span>
              </td>
              <td>
                <div class="d-flex justify-content-center gap-2 flex-wrap">
                  <button
                    class="btn btn-info btn-sm action-square"
                    @click="xemChiTiet(item)"
                    title="Chi tiết"
                  >
                    <i class="bi bi-search"></i>
                  </button>
                  <!-- Nút cập nhật trạng thái -->
                  <button
                    class="btn btn-warning btn-sm action-square"
                    v-if="item.trangThai < 3 && item.trangThai !== 4"
                    @click="capNhatTrangThai(item)"
                    :disabled="loadingId === item.maDonHang"
                  >
                    <span
                      v-if="loadingId === item.maDonHang"
                      class="spinner-border spinner-border-sm"
                    ></span>
                    <i v-else class="bi bi-arrow-repeat"></i>
                  </button>
                  <button
                    class="btn btn-secondary btn-sm action-square"
                    @click="xemLichSu(item)"
                    title="Lịch sử"
                  >
                    <i class="bi bi-clock-history"></i>
                  </button>
                  <!-- Nút hủy đơn -->
                  <button
                    class="btn btn-danger btn-sm action-square"
                    v-if="item.trangThai === 0"
                    @click="huyDonHang(item)"
                    :disabled="loadingId === item.maDonHang"
                  >
                    <span
                      v-if="loadingId === item.maDonHang"
                      class="spinner-border spinner-border-sm"
                    ></span>
                    <i v-else class="bi bi-x-circle"></i>
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Phân trang -->
    <nav class="mt-3">
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
          :class="{ active: page === currentPage }"
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

    <!-- Modal chi tiết -->
    <div class="modal fade" id="modalChiTiet" tabindex="-1" ref="modal">
      <div class="modal-dialog modal-lg">
        <div class="modal-content shadow-lg border-0" v-if="donHangSelected">
          <div class="modal-header bg-success text-white">
            <h5 class="modal-title">📦 Chi tiết đơn hàng</h5>
            <!-- bỏ nút close bootstrap mặc định -->
          </div>
          <!-- lắng nghe sự kiện close -->
          <DonHangChiTiet :donHang="donHangSelected" @close="dongChiTiet" />
        </div>
      </div>
    </div>
    <!-- Loading overlay toàn trang -->
    <div v-if="isLoading" class="loading-overlay">
      <div class="spinner-border text-light" style="width: 3rem; height: 3rem"></div>
    </div>

    <!-- Modal lịch sử -->
    <LichSuDonHang
      v-if="lichSuModalOpen"
      :maDonHang="lichSuMaDH"
      :dataLichSu="lichSuData"
      @close="lichSuModalOpen = false"
    />
  </div>
</template>

<script>
import DonHangChiTiet from './DonHangChiTiet.vue'
import LichSuDonHang from './LichSuDonHang.vue'

export default {
  components: { DonHangChiTiet, LichSuDonHang },
  data() {
    return {
      donHangs: [],
      danhSachCu: [],
      filterStatus: '',
      filterFromDate: '',
      filterToDate: '',
      keyword: '',
      currentPage: 1,
      pageSize: 10,
      donHangSelected: null,
      lichSuModalOpen: false,
      lichSuMaDH: null,
      isLoading: false,
      lichSuData: [],
      trangThaiOptions: [
        { value: 0, label: 'Chờ xác nhận' },
        { value: 1, label: 'Đã xác nhận' },
        { value: 2, label: 'Đang giao' },
        { value: 3, label: 'Hoàn tất' },
        { value: 4, label: 'Đã hủy' },
      ],
      toastMessage: '',
      audio: new Audio('/sounds/ding.mp3'),
      toastTimeout: null,
    }
  },
  computed: {
    filteredDonHangs() {
      let filtered = this.donHangs

      // ✅ Tìm kiếm theo mã đơn, tên khách, SĐT
      if (this.keyword.trim() !== '') {
        const kw = this.keyword.trim().toLowerCase()
        filtered = filtered.filter((d) => {
          const ma = String(d.maDonHang || '')
          const ten = (d.khachHang?.hoTen || d.hoTenNguoiNhan || '').toLowerCase()
          const sdt = (d.khachHang?.soDienThoai || d.soDienThoaiNhan || '').toLowerCase()
          return ma.includes(kw) || ten.includes(kw) || sdt.includes(kw)
        })
      }

      // ✅ Lọc theo trạng thái
      if (this.filterStatus !== '') {
        filtered = filtered.filter((d) => d.trangThai == this.filterStatus)
      }

      // ✅ Lọc theo ngày
      if (this.filterFromDate) {
        const from = new Date(this.filterFromDate)
        filtered = filtered.filter((d) => new Date(d.ngayDat) >= from)
      }

      if (this.filterToDate) {
        const to = new Date(this.filterToDate)
        to.setHours(23, 59, 59, 999)
        filtered = filtered.filter((d) => new Date(d.ngayDat) <= to)
      }

      return filtered
    },

    totalPages() {
      return Math.ceil(this.filteredDonHangs.length / this.pageSize) || 1
    },
    pagedDonHangs() {
      const start = (this.currentPage - 1) * this.pageSize
      return this.filteredDonHangs.slice(start, start + this.pageSize)
    },
    startIndex() {
      return (this.currentPage - 1) * this.pageSize
    },
  },
  mounted() {
    this.loadData()
    this.interval = setInterval(this.loadData, 10000)
  },
  beforeUnmount() {
    clearInterval(this.interval)
  },
  methods: {
    async loadData() {
      try {
        const res = await fetch('http://localhost:8080/api/donhang')
        const dataMoi = await res.json()

        // ✅ Sắp xếp theo ngày đặt mới nhất
        dataMoi.sort((a, b) => new Date(b.ngayDat) - new Date(a.ngayDat))

        const idsCu = this.danhSachCu.map((d) => d.maDonHang)
        const donMoiChoXacNhan = dataMoi.find(
          (d) => !idsCu.includes(d.maDonHang) && d.trangThai === 0,
        )

        if (donMoiChoXacNhan) {
          this.toastMessage = '🔔 Có đơn hàng mới *chờ xác nhận*!'
          this.showToast()
          this.audio.play().catch(() => {})
        }

        this.donHangs = dataMoi
        this.danhSachCu = [...dataMoi]
      } catch (err) {
        console.error('❌ Lỗi load đơn hàng:', err)
      }
    },

    showToast() {
      if (this.toastTimeout) clearTimeout(this.toastTimeout)
      const toast = new bootstrap.Toast(this.$refs.toast)
      toast.show()
      this.toastTimeout = setTimeout(() => this.hideToast(), 5000)
    },
    hideToast() {
      const toast = bootstrap.Toast.getInstance(this.$refs.toast)
      if (toast) toast.hide()
    },
    resetFilters() {
      this.filterStatus = ''
      this.filterFromDate = ''
      this.filterToDate = ''
    },
    changePage(page) {
      if (page < 1 || page > this.totalPages) return
      this.currentPage = page
    },
    getTenKhach(item) {
      return item.khachHang?.hoTen || item.hoTenNguoiNhan || '---'
    },
    getSoDienThoai(item) {
      return item.khachHang?.soDienThoai || item.soDienThoaiNhan || '---'
    },

    xemChiTiet(item) {
      this.donHangSelected = item
      new bootstrap.Modal(this.$refs.modal).show()
    },
    dongChiTiet() {
      // ẩn modal khi con emit('close')
      const modal = bootstrap.Modal.getInstance(this.$refs.modal)
      if (modal) modal.hide()
      this.donHangSelected = null
    },
    async capNhatTrangThai(item) {
      const trangThaiMoi = item.trangThai + 1
      const xacNhan = confirm(
        `📌 Bạn có chắc muốn chuyển đơn hàng #${item.maDonHang}\n` +
          `Từ: ${this.getTrangThaiLabel(item.trangThai)}\n` +
          `Sang: ${this.getTrangThaiLabel(trangThaiMoi)} ?`,
      )
      if (!xacNhan) return

      this.isLoading = true
      try {
        const res = await fetch(
          `http://localhost:8080/api/donhang/update-status?id=${item.maDonHang}&status=${trangThaiMoi}`,
          { method: 'PUT' },
        )

        if (!res.ok) {
          const errMsg = await res.text()
          this.toastMessage = `⚠️ ${errMsg}`
          this.showToast()
          return
        }

        item.trangThai = trangThaiMoi

        // Nếu chuyển sang Hoàn tất (3) mà chưa thanh toán → tự động cập nhật thanh toán
        if (trangThaiMoi === 3 && !item.daThanhToan) {
          await fetch(
            `http://localhost:8080/api/donhang/cap-nhat-thanh-toan?id=${item.maDonHang}&daThanhToan=true`,
            { method: 'PUT' },
          )
          item.daThanhToan = true
        }

        this.toastMessage = `✅ Cập nhật trạng thái đơn hàng #${item.maDonHang} thành công!`
        this.showToast()
      } catch (err) {
        console.error('❌ Lỗi cập nhật trạng thái:', err)
        this.toastMessage = '⚠️ Lỗi hệ thống khi cập nhật trạng thái!'
        this.showToast()
      } finally {
        this.isLoading = false
      }
    },

    async xemLichSu(item) {
      this.lichSuMaDH = item.maDonHang
      this.lichSuModalOpen = true
      try {
        const res = await fetch(`http://localhost:8080/api/donhang/lich-su/${item.maDonHang}`)
        this.lichSuData = await res.json()
      } catch (err) {
        console.error('❌ Lỗi tải lịch sử:', err)
      }
    },
    async huyDonHang(item) {
      const lyDo = prompt(`📌 Nhập lý do huỷ đơn hàng #${item.maDonHang}:`)
      if (!lyDo || lyDo.trim().length === 0) {
        alert('⚠️ Bạn phải nhập lý do để huỷ đơn!')
        return
      }

      this.isLoading = true
      try {
        await fetch(
          `http://localhost:8080/api/donhang/update-status?id=${item.maDonHang}&status=4&lyDo=${encodeURIComponent(
            lyDo,
          )}`,
          { method: 'PUT' },
        )
        item.trangThai = 4
        alert('✅ Huỷ đơn hàng thành công!')
      } catch (err) {
        console.error('❌ Lỗi huỷ đơn:', err)
        alert('⚠️ Huỷ đơn thất bại.')
      } finally {
        this.isLoading = false
      }
    },
    formatDate(date) {
      return new Date(date).toLocaleDateString('vi-VN')
    },
    formatMoney(val) {
      return (val ?? 0).toLocaleString('vi-VN', {
        style: 'currency',
        currency: 'VND',
      })
    },
    getTrangThaiLabel(val) {
      const opt = this.trangThaiOptions.find((o) => o.value === val)
      return opt?.label || 'Không rõ'
    },
    getTrangThaiClass(val) {
      switch (val) {
        case 0:
          return 'bg-warning text-dark' // ⏳ Chờ xử lý
        case 1:
          return 'bg-info text-white' // 🔄 Đang xử lý
        case 2:
          return 'bg-primary text-white' // 🚚 Đang giao
        case 3:
          return 'bg-success text-white' // ✅ Hoàn tất
        case 4:
          return 'bg-danger text-white' // ❌ Đã hủy
        default:
          return 'bg-secondary text-white'
      }
    },
  },
}
</script>

<style scoped>
.toast {
  min-width: 280px;
}
.table td {
  vertical-align: middle;
}
.loading-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.4);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 2000; /* cao hơn modal và toast */
}
</style>
