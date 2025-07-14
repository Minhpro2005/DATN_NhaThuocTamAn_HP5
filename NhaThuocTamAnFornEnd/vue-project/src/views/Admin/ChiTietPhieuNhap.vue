<template>
  <div class="container py-4">
    <h4 class="mb-4 fw-bold text-success">📦 Danh Sách Phiếu Nhập</h4>

    <!-- Bảng danh sách -->
    <div class="card shadow-sm">
      <div class="card-body p-0">
        <table class="table table-bordered align-middle mb-0">
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
              <td>{{ pn.nhanVien?.hoTen }}</td>
              <td>{{ pn.nhaCungCap?.tenNCC }}</td>
              <td class="text-end text-danger fw-bold">{{ formatPrice(pn.tongTien) }}</td>
              <td class="text-center">
                <button class="btn btn-sm btn-outline-info" @click="xemChiTiet(pn)">
                  <i class="bi bi-eye-fill"></i> Xem
                </button>
              </td>
            </tr>
            <tr v-if="phieuNhaps.length === 0">
              <td colspan="6" class="text-center text-muted py-3">Không có dữ liệu phiếu nhập.</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Modal chi tiết -->
    <div
      v-if="chiTietModalVisible"
      class="modal fade show d-block"
      style="background: rgba(0, 0, 0, 0.4)"
    >
      <div class="modal-dialog modal-lg modal-dialog-scrollable">
        <div class="modal-content shadow">
          <div class="modal-header bg-success text-white">
            <h5 class="modal-title">📄 Chi Tiết Phiếu Nhập #{{ selectedPN?.maPN }}</h5>
            <button
              type="button"
              class="btn-close btn-close-white"
              @click="chiTietModalVisible = false"
            ></button>
          </div>
          <div class="modal-body">
            <div class="mb-3">
              <p><strong>👨‍⚕️ Nhân viên:</strong> {{ selectedPN?.nhanVien?.hoTen }}</p>
              <p><strong>🏢 Nhà cung cấp:</strong> {{ selectedPN?.nhaCungCap?.tenNCC }}</p>
              <p><strong>📅 Ngày nhập:</strong> {{ formatDate(selectedPN?.ngayNhap) }}</p>
            </div>

            <div class="table-responsive">
              <table class="table table-striped table-bordered align-middle">
                <thead class="table-light text-center">
                  <tr>
                    <th>MaPN</th>
                    <th>Tên thuốc</th>
                    <th>Số lượng</th>
                    <th>Đơn giá</th>
                    <th>Thành tiền</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(ct, index) in chiTietList" :key="index">
                    <td class="text-center">{{ index + 1 }}</td>
                    <td>{{ ct.tenThuoc || ct.tenBienThe || 'Không rõ' }}</td>
                    <td class="text-center">{{ ct.soLuong }}</td>
                    <td class="text-end">{{ formatPrice(ct.donGiaNhap) }}</td>
                    <td class="text-end fw-semibold">
                      {{ formatPrice(ct.soLuong * ct.donGiaNhap) }}
                    </td>
                  </tr>
                  <tr v-if="chiTietList.length === 0">
                    <td colspan="5" class="text-center text-muted py-3">
                      Không có chi tiết phiếu nhập
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
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
import { ref, onMounted } from 'vue'
import axios from 'axios'

const phieuNhaps = ref([])
const selectedPN = ref(null)
const chiTietList = ref([])
const chiTietModalVisible = ref(false)

const formatDate = (dateStr) => {
  const d = new Date(dateStr)
  return d.toLocaleDateString('vi-VN')
}

const formatPrice = (num) => {
  return num?.toLocaleString('vi-VN') + ' đ'
}

const loadPhieuNhaps = async () => {
  try {
    const res = await axios.get('http://localhost:8080/api/phieu-nhap')
    phieuNhaps.value = res.data
  } catch (err) {
    console.error('❌ Lỗi khi load phiếu nhập:', err)
  }
}

const xemChiTiet = async (pn) => {
  try {
    const res = await axios.get(`http://localhost:8080/api/phieu-nhap/${pn.maPN}/chi-tiet`)
    selectedPN.value = pn
    chiTietList.value = res.data
    chiTietModalVisible.value = true
  } catch (err) {
    console.error('❌ Lỗi khi xem chi tiết:', err)
  }
}

onMounted(loadPhieuNhaps)
</script>

<style scoped>
.modal.fade.show {
  display: block;
}
</style>
