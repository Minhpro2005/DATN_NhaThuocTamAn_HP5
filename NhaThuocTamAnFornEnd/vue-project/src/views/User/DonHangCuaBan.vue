<template>
  <div class="card p-4 shadow-sm">
    <h5 class="fw-bold mb-4">Đơn hàng của bạn</h5>

    <ul class="nav nav-tabs mb-4">
      <li class="nav-item" v-for="status in tabs" :key="status.value">
        <button
          class="nav-link"
          :class="{ active: currentStatus === status.value }"
          @click="currentStatus = status.value"
        >
          {{ status.label }}
        </button>
      </li>
    </ul>

    <div v-if="filteredOrders.length === 0" class="alert alert-info">
      Không có đơn hàng nào thuộc trạng thái "{{ getStatusLabel(currentStatus) }}"
    </div>

    <div v-for="order in filteredOrders" :key="order.id" class="order-item mb-4 border rounded p-3">
      <div class="d-flex justify-content-between align-items-center mb-2">
        <div>
          <strong>Mã đơn hàng: #{{ order.id }}</strong
          ><br />
          <small class="text-muted">Ngày đặt: {{ order.date }}</small>
        </div>
        <span :class="statusClass(order.status)">{{ getStatusLabel(order.status) }}</span>
      </div>

      <div class="d-flex align-items-center gap-3">
        <img
          :src="order.productImage"
          class="img-thumbnail"
          style="width: 80px; height: 80px; object-fit: cover"
        />
        <div class="flex-grow-1">
          <div class="fw-semibold">{{ order.product }}</div>
          <div class="text-muted small">Số lượng: {{ order.quantity }}</div>
        </div>
        <div class="fw-bold text-success">{{ formatPrice(order.total) }}</div>
      </div>

      <div class="text-end mt-3 d-flex gap-2 justify-content-end">
        <button class="btn btn-outline-primary btn-sm" @click="viewDetail(order)">
          <i class="bi bi-eye me-1"></i> Xem chi tiết
        </button>
        <button
          v-if="order.status === 'cho-xac-nhan'"
          class="btn btn-outline-danger btn-sm"
          @click="cancelOrder(order)"
        >
          <i class="bi bi-x-circle me-1"></i> Hủy đơn
        </button>
        <button
          v-if="order.status === 'da-giao'"
          class="btn btn-outline-success btn-sm"
          @click="reviewOrder(order)"
        >
          <i class="bi bi-star me-1"></i> Đánh giá
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'

// Tabs trạng thái đơn hàng
const tabs = [
  { value: 'cho-xac-nhan', label: 'Chờ xác nhận' },
  { value: 'cho-lay-hang', label: 'Chờ lấy hàng' },
  { value: 'cho-giao-hang', label: 'Chờ giao hàng' },
  { value: 'da-giao', label: 'Đã giao' },
  { value: 'tra-hang', label: 'Trả hàng' },
  { value: 'da-huy', label: 'Đã huỷ' },
]

const currentStatus = ref('cho-xac-nhan')

// Tạm thời dùng dữ liệu giả lập (sau này sẽ thay bằng API)
const orders = ref([
  {
    id: 1001,
    date: '01/04/2025',
    status: 'cho-xac-nhan',
    product: 'Siro ho Prospan',
    quantity: 2,
    total: 198000,
    productImage: 'vue-project\public\Img\anhthuoc5.webp',
  },
  {
    id: 1002,
    date: '15/04/2025',
    status: 'da-giao',
    product: 'Vitamin C 500mg',
    quantity: 1,
    total: 95000,
    productImage: 'vue-project\public\Img\anhthuoc7.webp',
  },
  {
    id: 1003,
    date: '25/04/2025',
    status: 'da-huy',
    product: 'Khẩu trang y tế',
    quantity: 5,
    total: 75000,
    productImage: 'vue-project\public\Img\anhthuoc8.webp',
  },
])

// Lọc đơn theo tab hiện tại
const filteredOrders = computed(() =>
  orders.value.filter((order) => order.status === currentStatus.value),
)

// Format giá tiền
const formatPrice = (price) =>
  price?.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' })

const getStatusLabel = (status) => {
  const found = tabs.find((t) => t.value === status)
  return found ? found.label : status
}

const statusClass = (status) => {
  switch (status) {
    case 'da-giao':
      return 'badge bg-success'
    case 'cho-giao-hang':
      return 'badge bg-warning text-dark'
    case 'cho-lay-hang':
      return 'badge bg-info text-dark'
    case 'cho-xac-nhan':
      return 'badge bg-secondary'
    case 'tra-hang':
      return 'badge bg-danger'
    case 'da-huy':
      return 'badge bg-dark'
    default:
      return 'badge bg-light text-dark'
  }
}

// Các sự kiện (hiện tại chỉ alert demo)
const viewDetail = (order) => {
  alert(`Xem chi tiết đơn hàng #${order.id}`)
}
const cancelOrder = (order) => {
  alert(`Bạn đã huỷ đơn hàng #${order.id}`)
}
const reviewOrder = (order) => {
  alert(`Bạn muốn đánh giá đơn hàng #${order.id}`)
}
</script>

<style scoped>
.badge {
  font-size: 0.9rem;
  padding: 6px 12px;
}
</style>
