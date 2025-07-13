<template>
  <div class="card p-4 shadow-sm">
    <h5 class="fw-bold mb-4">Đơn hàng của bạn</h5>

    <!-- Tabs trạng thái -->
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
      Không có đơn hàng nào thuộc trạng thái "{{ getTrangThaiLabel(currentStatus) }}"
    </div>

    <div v-for="order in filteredOrders" :key="order.id" class="order-item mb-4 border rounded p-3">
      <div class="d-flex justify-content-between align-items-center mb-2">
        <div>
          <strong>Mã đơn hàng: #{{ order.id }}</strong
          ><br />
          <small class="text-muted">Ngày đặt: {{ formatDate(order.date) }}</small>
        </div>
        <span :class="getTrangThaiClass(order.status)">
          {{ getTrangThaiLabel(order.status) }}
        </span>
      </div>

      <div
        v-for="(sp, index) in order.products"
        :key="index"
        class="d-flex align-items-center gap-3 mb-2"
      >
        <img
          :src="sp.hinhAnh"
          class="img-thumbnail"
          style="width: 80px; height: 80px; object-fit: cover"
        />
        <div class="flex-grow-1">
          <div class="fw-semibold">{{ sp.tenThuoc }}</div>
          <div class="text-muted small">Số lượng: {{ sp.soLuong }}</div>
        </div>
        <div class="fw-bold text-success">{{ formatPrice(sp.donGia * sp.soLuong) }}</div>
      </div>

      <div class="text-end mt-2 fw-bold">
        Tổng đơn: <span class="text-success">{{ formatPrice(order.total) }}</span>
      </div>

      <div class="text-end mt-3 d-flex gap-2 justify-content-end">
        <button class="btn btn-outline-info btn-sm" @click="openDetail(order.id)">
          <i class="bi bi-receipt me-1"></i> Xem chi tiết
        </button>
        <button
          v-if="order.status === 0"
          class="btn btn-outline-danger btn-sm"
          @click="cancelOrder(order)"
        >
          <i class="bi bi-x-circle me-1"></i> Hủy đơn
        </button>
        <button
          v-if="order.status === 3"
          class="btn btn-outline-success btn-sm"
          @click="reviewOrder(order)"
        >
          <i class="bi bi-star me-1"></i> Đánh giá
        </button>
      </div>
    </div>

    <XemChiTietDonHang
      v-if="showDetailModal"
      :donHang="selectedOrder"
      @close="showDetailModal = false"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import XemChiTietDonHang from './XemChiTietDonHang.vue'

const router = useRouter()

const orders = ref([])
const currentStatus = ref(0)
const showDetailModal = ref(false)
const selectedOrder = ref(null)

const tabs = [
  { value: 0, label: 'Chờ xác nhận' },
  { value: 1, label: 'Đã xác nhận' },
  { value: 2, label: 'Đang giao' },
  { value: 3, label: 'Hoàn tất' },
  { value: 4, label: 'Đã huỷ' },
]

const formatPrice = (price) =>
  price?.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' })

const formatDate = (date) =>
  new Date(date).toLocaleDateString('vi-VN', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
  })

const getTrangThaiLabel = (val) => {
  switch (val) {
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
}

const getTrangThaiClass = (val) => {
  switch (val) {
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
}

const getFullImageUrl = (path) => {
  if (!path) return 'https://cdn-icons-png.flaticon.com/512/891/891462.png'
  if (path.startsWith('http')) return path
  const fixedPath = path.startsWith('uploads/') ? path : `uploads/${path}`
  return `http://localhost:8080/${fixedPath}`
}

const filteredOrders = computed(() =>
  orders.value
    .filter((order) => order.status === currentStatus.value)
    .sort((a, b) => new Date(b.date) - new Date(a.date)),
)

const cancelOrder = async (order) => {
  if (order.status !== 0) {
    alert('❌ Chỉ đơn hàng ở trạng thái "Chờ xác nhận" mới được hủy!')
    return
  }

  const lyDo = prompt(`📌 Nhập lý do bạn muốn hủy đơn hàng #${order.id}:`)
  if (!lyDo || lyDo.trim().length === 0) {
    alert('⚠️ Bạn phải nhập lý do để hủy đơn hàng!')
    return
  }

  try {
    await axios.put(
      `http://localhost:8080/api/donhang/update-status?id=${order.id}&status=4&lyDo=${encodeURIComponent(lyDo)}`,
    )
    order.status = 4
    alert('✅ Hủy đơn hàng thành công!')
  } catch (err) {
    console.error('❌ Lỗi khi hủy đơn:', err)
    alert('⚠️ Hủy đơn thất bại.')
  }
}

const reviewOrder = (order) => {
  const sp = order.products[0]
  const maThuoc = sp.maThuoc || null
  const maBienThe = sp.maBienThe || null

  if (!maThuoc && !maBienThe) {
    alert('Không tìm thấy mã sản phẩm.')
    return
  }

  router.push({
    name: 'chi-tiet-san-pham',
    params: { id: maThuoc || maBienThe },
    query: {
      review: 'true',
      loai: maBienThe ? 'bienThe' : 'thuoc',
    },
  })
}

const maKH = localStorage.getItem('maKH')
let refreshInterval = null

const fetchOrders = async () => {
  if (!maKH) return
  try {
    const res = await axios.get(`http://localhost:8080/api/donhang/khach-hang/${maKH}`)
    orders.value = res.data.map((dh) => ({
      id: dh.maDonHang,
      date: new Date(dh.ngayDat),
      status: dh.trangThai,
      total: dh.thanhToan,
      products: (dh.chiTietHienThi || []).map((sp) => ({
        maThuoc: sp.maThuoc,
        maBienThe: sp.maBienThe,
        tenThuoc: sp.tenThuoc,
        soLuong: sp.soLuong,
        donGia: sp.donGia,
        hinhAnh: getFullImageUrl(sp.hinhAnh || sp.hinhAnhChinh),
      })),
    }))
  } catch (err) {
    console.error('❌ Lỗi khi lấy đơn hàng:', err)
  }
}

const openDetail = async (orderId) => {
  try {
    const res = await axios.get(`http://localhost:8080/api/donhang/dto/${orderId}`)
    const donHang = res.data

    // ✅ Gắn lại chi tiết có đủ hinhAnh và tenThuoc
    donHang.chiTiet = (donHang.chiTietHienThi || []).map((ct) => {
      return {
        ...ct,
        hinhAnh: getFullImageUrl(ct.hinhAnh || ct.hinhAnhChinh),
        tenThuoc: ct.tenThuoc || '---',
      }
    })

    selectedOrder.value = donHang
    showDetailModal.value = true
  } catch (err) {
    console.error('❌ Lỗi khi lấy chi tiết đơn hàng:', err)
    alert('Lỗi lấy chi tiết đơn hàng')
  }
}

onMounted(async () => {
  await fetchOrders()
  refreshInterval = setInterval(fetchOrders, 15000)
})

onUnmounted(() => {
  if (refreshInterval) clearInterval(refreshInterval)
})
</script>

<style scoped>
.badge {
  font-size: 0.9rem;
  padding: 6px 12px;
}
</style>
