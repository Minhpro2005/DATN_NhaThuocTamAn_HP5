<template>
  <div class="card p-4 shadow-sm">
    <h5 class="fw-bold mb-4">Lịch sử mua hàng</h5>

    <!-- Toast thông báo -->
    <div
      class="toast position-fixed bottom-0 end-0 p-3 text-bg-success"
      role="alert"
      ref="toastRef"
      style="z-index: 1055"
    >
      <div class="d-flex">
        <div class="toast-body fw-bold">🛒 Đã thêm lại sản phẩm vào giỏ hàng!</div>
        <button
          type="button"
          class="btn-close btn-close-white me-2 m-auto"
          @click="hideToast"
        ></button>
      </div>
    </div>

    <div v-if="history.length === 0" class="alert alert-info">Bạn chưa có đơn hàng nào.</div>

    <div v-for="item in history" :key="item.maDonHang" class="order-item mb-4 border rounded p-3">
      <div class="d-flex justify-content-between align-items-center mb-2">
        <div>
          <strong>Mã đơn hàng: #{{ item.maDonHang }}</strong
          ><br />
          <small class="text-muted">Ngày đặt: {{ formatDate(item.ngayDat) }}</small>
        </div>
        <span :class="statusClass(item.trangThai)">
          {{ getStatusLabel(item.trangThai) }}
        </span>
      </div>

      <div class="d-flex align-items-center gap-3" v-if="item.chiTietHienThi?.length">
        <router-link :to="`/chi-tiet/${item.chiTietHienThi[0].maThuoc}`" class="d-inline-block">
          <img
            :src="
              getImageUrl(item.chiTietHienThi[0].hinhAnh || item.chiTietHienThi[0].hinhAnhChinh)
            "
            class="img-thumbnail"
            style="width: 80px; height: 80px; object-fit: cover; cursor: pointer"
          />
        </router-link>

        <div class="flex-grow-1">
          <div class="fw-semibold">{{ item.chiTietHienThi[0].tenThuoc }}</div>
          <div class="text-muted small">Số lượng: {{ item.chiTietHienThi[0].soLuong }}</div>
        </div>
        <div class="fw-bold text-success">
          {{ formatPrice(item.thanhToan) }}
        </div>
      </div>

      <div class="text-end mt-3 d-flex justify-content-end gap-2">
        <button class="btn btn-outline-danger btn-sm" @click="buyAgain(item)">
          <i class="bi bi-cart-plus me-1"></i> Mua lại
        </button>

        <button
          v-if="item.trangThai === 3 || item.trangThai === 4"
          class="btn btn-outline-success btn-sm"
          @click="reviewOrder(item)"
        >
          <i class="bi bi-star me-1"></i> Đánh giá
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { CartStore } from './CartStore.js'

const router = useRouter()
const history = ref([])
const toastRef = ref(null)
let toastInstance = null

onMounted(() => {
  fetchHistory()
  if (toastRef.value) {
    toastInstance = new bootstrap.Toast(toastRef.value, { delay: 3000 })
  }
})

const fetchHistory = async () => {
  const maKH = localStorage.getItem('maKH')
  try {
    const res = await fetch(`http://localhost:8080/api/donhang/khach-hang/${maKH}`)
    const data = await res.json()

    history.value = data
      .filter((item) => item.trangThai === 3 || item.trangThai === 4) // Hoàn tất hoặc Hủy
      .sort((a, b) => new Date(b.ngayDat) - new Date(a.ngayDat))
  } catch (err) {
    console.error('❌ Lỗi khi lấy lịch sử mua hàng:', err)
  }
}

const formatPrice = (price) =>
  price?.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' })

const formatDate = (dateStr) => new Date(dateStr).toLocaleDateString('vi-VN')

const getStatusLabel = (status) => {
  switch (status) {
    case 3:
      return 'Hoàn tất'
    case 4:
      return 'Đã hủy'
    default:
      return 'Không xác định'
  }
}

const statusClass = (status) => {
  switch (status) {
    case 3:
      return 'badge bg-success'
    case 4:
      return 'badge bg-secondary'
    default:
      return 'badge bg-light text-dark'
  }
}

const getImageUrl = (path) => {
  if (!path) return 'https://cdn-icons-png.flaticon.com/512/891/891462.png'
  if (path.startsWith('http')) return path
  if (!path.startsWith('uploads/')) path = 'uploads/' + path
  return `http://localhost:8080/${path}`
}

const showToast = () => {
  if (toastInstance) toastInstance.show()
}

const hideToast = () => {
  if (toastInstance) toastInstance.hide()
}

const buyAgain = (item) => {
  const chiTiet = item.chiTietHienThi || []
  if (!chiTiet.length) return

  const maKH = localStorage.getItem('maKH')
  const storageKey = maKH ? `cart_${maKH}` : 'cart_temp'
  const gioHang = JSON.parse(localStorage.getItem(storageKey) || '[]')

  chiTiet.forEach((sp) => {
    const existing = gioHang.find(
      (g) =>
        g.maThuoc === sp.maThuoc &&
        (g.maBienThe === sp.maBienThe || (!g.maBienThe && !sp.maBienThe)),
    )
    if (existing) {
      existing.soLuong += sp.soLuong
    } else {
      gioHang.push({
        maThuoc: sp.maThuoc,
        maBienThe: sp.maBienThe || null,
        tenThuoc: sp.tenThuoc,
        soLuong: sp.soLuong,
        giaBan: sp.giaBan || item.thanhToan / sp.soLuong,
        hinhAnh: sp.hinhAnh || sp.hinhAnhChinh || '',
        donViTinh: sp.donViTinh || 'hộp',
      })
    }
  })

  localStorage.setItem(storageKey, JSON.stringify(gioHang))
  CartStore.updateCount()
  showToast()
  setTimeout(() => router.push('/gio-hang'), 1000)
}

const reviewOrder = (item) => {
  const sp = item.chiTietHienThi?.[0]
  if (!sp) return alert('Không tìm thấy sản phẩm để đánh giá')

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
</script>

<style scoped>
.badge {
  font-size: 0.85rem;
  padding: 4px 10px;
}
.toast {
  min-width: 280px;
}
</style>
