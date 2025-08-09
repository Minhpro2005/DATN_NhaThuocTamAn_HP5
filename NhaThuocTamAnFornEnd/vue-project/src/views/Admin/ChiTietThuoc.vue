<template>
  <div class="container mt-4">
    <router-link to="/admin/san-pham" class="btn btn-secondary mb-3">
      ← Quay lại danh sách
    </router-link>

    <h4 class="text-success fw-bold mb-3">
      <i class="bi bi-capsule me-2"></i> Chi tiết thuốc: {{ thuoc.tenThuoc }}
    </h4>

    <div class="row mb-4">
      <div class="col-md-4 text-center">
        <img
          v-if="thuoc.hinhAnhChinh"
          :src="getImageUrl(thuoc.hinhAnhChinh)"
          alt="Ảnh chính"
          class="img-thumbnail"
          style="max-width: 200px"
        />
      </div>
      <div class="col-md-8">
        <p><strong>Mã thuốc:</strong> {{ thuoc.maThuoc }}</p>
        <p><strong>Danh mục:</strong> {{ thuoc.tenDanhMuc || '-' }}</p>
        <p><strong>Giá bán:</strong> {{ formatCurrency(thuoc.giaBan) }}</p>
        <p><strong>Đơn vị tính:</strong> {{ thuoc.tenDonViTinh || '-' }}</p>
        <p><strong>Dạng bào chế:</strong> {{ thuoc.dangBaoChe }}</p>
        <p><strong>Quy cách:</strong> {{ thuoc.moTaQuyCach || '-' }}</p>
        <p><strong>Xuất xứ:</strong> {{ thuoc.xuatXu }}</p>
        <p><strong>Số lượng trong kho:</strong> {{ thuoc.soLuong }}</p>
        <p><strong>Tổng số lượng các biến thể:</strong> {{ tongSoLuongBienThe }}</p>
        <p>
          <strong>Trạng thái:</strong>
          <span :class="thuoc.trangThai ? 'text-success' : 'text-danger'">
            {{ thuoc.trangThai ? 'Hoạt động' : 'Ngừng bán' }}
          </span>
        </p>
      </div>
    </div>

    <div class="mb-4">
      <h5 class="text-primary">🧪 Thông tin bổ sung</h5>
      <p><strong>Thành phần:</strong> {{ thuoc.thanhPhan }}</p>
      <p><strong>Ngày sản xuất:</strong> {{ formatDate(thuoc.ngaySanXuat) }}</p>
      <p><strong>Hạn sử dụng:</strong> {{ formatDate(thuoc.hanSuDung) }}</p>
      <p><strong>Công dụng:</strong> {{ thuoc.congDung }}</p>
      <p><strong>Hướng dẫn sử dụng:</strong> {{ thuoc.huongDanSuDung }}</p>
      <p><strong>Mô tả thêm:</strong> {{ thuoc.moTaThem }}</p>
    </div>

    <h5 class="text-primary mb-2">🧬 Biến thể thuốc</h5>
    <table class="table table-bordered text-center">
      <thead class="table-light">
        <tr>
          <th>Mã biến thể</th>
          <th>Tên biến thể</th>
          <th>Giá bán</th>
          <th>Số lượng</th>
          <th>Trạng thái</th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="bienTheList.length === 0">
          <td colspan="5" class="text-muted">Không có biến thể nào.</td>
        </tr>
        <tr v-for="bt in bienTheList" :key="bt.maBienThe">
          <td>{{ bt.maBienThe }}</td>
          <td>{{ bt.tenBienThe }}</td>
          <td>{{ formatCurrency(bt.giaBan) }}</td>
          <td>{{ bt.soLuong }}</td>
          <td :class="bt.trangThai ? 'text-success' : 'text-danger'">
            {{ bt.trangThai ? 'Hoạt động' : 'Ngừng bán' }}
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const maThuoc = route.params.maThuoc

const thuoc = ref({})
const bienTheList = ref([])

function getImageUrl(path) {
  return path ? `http://localhost:8080/${path.replace(/^\/+/g, '')}` : ''
}

function formatCurrency(val) {
  return Number(val).toLocaleString('vi-VN') + 'đ'
}

function formatDate(dateStr) {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleDateString('vi-VN')
}

function fetchChiTietThuoc() {
  fetch(`http://localhost:8080/api/thuoc/${maThuoc}`)
    .then((res) => res.json())
    .then((data) => (thuoc.value = data))
}

function fetchBienThe() {
  fetch(`http://localhost:8080/api/bienthe/thuoc/${maThuoc}`)
    .then((res) => res.json())
    .then((data) => (bienTheList.value = data))
}

const tongSoLuongBienThe = computed(() =>
  bienTheList.value.reduce((sum, bt) => sum + (bt.soLuong || 0), 0),
)

onMounted(() => {
  fetchChiTietThuoc()
  fetchBienThe()
})
</script>
