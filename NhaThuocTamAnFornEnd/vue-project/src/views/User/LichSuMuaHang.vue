<template>
  <div class="card p-4 shadow-sm">
    <h5 class="fw-bold mb-4">Lịch sử mua hàng</h5>

    <div v-if="history.length === 0" class="alert alert-info">Bạn chưa có đơn hàng nào.</div>

    <div v-for="item in history" :key="item.id" class="order-item mb-4 border rounded p-3">
      <div class="d-flex justify-content-between align-items-center mb-2">
        <div>
          <strong>Mã đơn hàng: #{{ item.orderCode }}</strong
          ><br />
          <small class="text-muted">Ngày đặt: {{ item.date }}</small>
        </div>
        <span :class="statusClass(item.status)">{{ item.status }}</span>
      </div>

      <div class="d-flex align-items-center gap-3">
        <img
          :src="item.productImage"
          class="img-thumbnail"
          style="width: 80px; height: 80px; object-fit: cover"
        />
        <div class="flex-grow-1">
          <div class="fw-semibold">{{ item.product }}</div>
          <div class="text-muted small">Số lượng: {{ item.quantity }}</div>
        </div>
        <div class="fw-bold text-success">{{ formatPrice(item.total) }}</div>
      </div>

      <div class="text-end mt-3">
        <button class="btn btn-outline-danger btn-sm" @click="buyAgain(item)">
          <i class="bi bi-cart-plus me-1"></i> Mua lại
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

// Dữ liệu đơn hàng mẫu (sau này kết nối API backend)
const history = ref([
  {
    id: 1,
    orderCode: 'DH1001',
    date: '01/03/2025',
    product: 'Siro ho Prospan',
    quantity: 2,
    total: 198000,
    productImage: 'vue-project\public\Img\anhthuoc6.webp',
    status: 'Đã giao',
  },
  {
    id: 2,
    orderCode: 'DH1002',
    date: '10/03/2025',
    product: 'Vitamin C 500mg',
    quantity: 1,
    total: 95000,
    productImage: 'vue-project\public\Img\anhthuoc7.webp',
    status: 'Đã hủy',
  },
  {
    id: 3,
    orderCode: 'DH1003',
    date: '20/03/2025',
    product: 'Khẩu trang y tế',
    quantity: 5,
    total: 75000,
    productImage: 'vue-project\public\Img\anhthuoc8.webp',
    status: 'Đang giao',
  },
])

// Hàm định dạng tiền
const formatPrice = (price) =>
  price?.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' })

// Đổi màu theo trạng thái
const statusClass = (status) => {
  switch (status) {
    case 'Đã giao':
      return 'badge bg-success'
    case 'Đang giao':
      return 'badge bg-warning text-dark'
    case 'Đã hủy':
      return 'badge bg-secondary'
    default:
      return 'badge bg-light text-dark'
  }
}

// Xử lý khi bấm mua lại
const buyAgain = (item) => {
  alert(`✅ Đã thêm lại sản phẩm '${item.product}' vào giỏ hàng.`)
}
</script>

<style scoped>
.badge {
  font-size: 0.9rem;
  padding: 6px 12px;
}
</style>
